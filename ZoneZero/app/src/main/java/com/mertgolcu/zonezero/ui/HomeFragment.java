package com.mertgolcu.zonezero.ui;


import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mertgolcu.zonezero.R;
import com.mertgolcu.zonezero.adapters.DoctorsAdapter;
import com.mertgolcu.zonezero.adapters.IAdapterListener;
import com.mertgolcu.zonezero.api.IService;
import com.mertgolcu.zonezero.api.RetrofitClientInstance;
import com.mertgolcu.zonezero.api.models.Doctor;
import com.mertgolcu.zonezero.api.models.Doctors;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements IAdapterListener {

    private List<Doctor> mDoctorModels;
    private RecyclerView doctorsRecycler;
    private DoctorsAdapter doctorsAdapter;
    private EditText searchEdit;
    private CheckBox manCheckBox, womanCheckBox;
    private CardView recyclerCard, emptyListCard;
    private TextView emptyListText;


    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Hooks
        doctorsRecycler = v.findViewById(R.id.doctor_recycler);
        searchEdit = v.findViewById(R.id.search_edit);
        manCheckBox = v.findViewById(R.id.man_checkbox);
        womanCheckBox = v.findViewById(R.id.woman_checkbox);
        recyclerCard = v.findViewById(R.id.recycler_card);
        emptyListCard = v.findViewById(R.id.empty_list_card);
        emptyListText = v.findViewById(R.id.empty_list_text);

        //     init

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        doctorsRecycler.setLayoutManager(layoutManager);
        // RecyclerView line itmes between
        Drawable mDivider = getResources().getDrawable(R.drawable.line_divider);
        //Normally when we use a divider, we underline the last item and Override the onDraw function to prevent it.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(doctorsRecycler.getContext(), DividerItemDecoration.VERTICAL) {

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                int dividerLeft = parent.getPaddingLeft();
                int dividerRight = parent.getWidth() - parent.getPaddingRight();

                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount - 1; i++) {
                    View child = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int dividerTop = child.getBottom() + params.bottomMargin;
                    int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();
                    mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                    mDivider.draw(c);
                }
            }
        };
        doctorsRecycler.addItemDecoration(dividerItemDecoration);

        //get Data
        IService service = RetrofitClientInstance.getRetrofitInstance().create(IService.class);
        Call<Doctors> call = service.getAllDoctors();

        //retrofit call
        call.enqueue(new Callback<Doctors>() {
            @Override
            public void onResponse(Call<Doctors> call, Response<Doctors> response) {
                Log.i("RetrofitCall", response.toString());
                setDataList(response.body().getDoctors());
            }

            @Override
            public void onFailure(Call<Doctors> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.connection_fail), Toast.LENGTH_LONG).show();
                onUpdate(0);
                manCheckBox.setEnabled(false);
                womanCheckBox.setEnabled(false);
                searchEdit.setEnabled(false);
                t.printStackTrace();
            }
        });


        manCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                doctorsAdapter.setGenderFilter("male");
                womanCheckBox.setChecked(!b);
            } else {
                if (!womanCheckBox.isChecked())
                    doctorsAdapter.setGenderFilter("");
                filter(searchEdit.getText().toString());
            }

        });
        womanCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                doctorsAdapter.setGenderFilter("female");
                manCheckBox.setChecked(!b);
            } else {
                if (!manCheckBox.isChecked())
                    doctorsAdapter.setGenderFilter("");
                filter(searchEdit.getText().toString());
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        return v;
    }

    private void filter(String query) {
        List<Doctor> temp = new ArrayList();
        for (Doctor d : mDoctorModels) {
            if (d.getFull_name().toLowerCase().contains(query.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        doctorsAdapter.updateList(temp);
    }


    private void setDataList(List<Doctor> doctorList) {
        mDoctorModels = doctorList;
        //adapter
        doctorsAdapter = new DoctorsAdapter(getActivity(), mDoctorModels, this);
        doctorsRecycler.setAdapter(doctorsAdapter);
        //get now data
        //for the come back other fragments
        filter(searchEdit.getText().toString());
        doctorsAdapter.setGenderFilter(manCheckBox.isChecked() ? "male" : womanCheckBox.isChecked() ? "female" : "");
    }

    @Override
    public void onUpdate(int size) {
        if (size == 0) {
            recyclerCard.setVisibility(View.GONE);
            emptyListCard.setVisibility(View.VISIBLE);
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            recyclerCard.setVisibility(View.VISIBLE);
            emptyListCard.setVisibility(View.GONE);
            emptyListText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View v, Doctor doctor) {
//        Toast.makeText(getActivity(), doctor.getFull_name(), Toast.LENGTH_SHORT).show();
        NavDirections action =
                HomeFragmentDirections
                        .actionHomeFragmentToDetailFragment().setDoctorGson(doctorToGson(doctor));

        Navigation.findNavController(v).navigate(action);
    }

    private String doctorToGson(Doctor d) {
        Gson gson = new Gson();
        String res = gson.toJson(d);
        return res;
    }


}