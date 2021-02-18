package com.mertgolcu.zonezero.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mertgolcu.zonezero.R;
import com.mertgolcu.zonezero.api.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {

    private List<Doctor> mDoctorModels;
    public IAdapterListener listener;
    private Context context;

    public String genderFilter = "";

    public DoctorsAdapter(Context context, List<Doctor> data, IAdapterListener listener) {
        this.context = context;
        this.mDoctorModels = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_item, parent, false);

        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        holder.nameText.setText(mDoctorModels.get(position).getFull_name());


        Picasso.with(context).load(mDoctorModels.get(position).getImage().getUrl()).fit().centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.smallImage);

        holder.linear.setOnClickListener(view -> listener.onItemClick(view, mDoctorModels.get(position)));

    }

    @Override
    public int getItemCount() {
        return mDoctorModels.size();
    }


    public void updateList(List<Doctor> list) {
        mDoctorModels = list;
        applyGenderFilter();
        //notifyDataSetChanged();
    }

    public void applyGenderFilter() {
        if (genderFilter.equals("")) {
            listener.onUpdate(mDoctorModels.size());
            notifyDataSetChanged();
            return;
        }

        List<Doctor> temp = new ArrayList();
        for (Doctor item : mDoctorModels) {
            if (item.getGender().equals(genderFilter))
                temp.add(item);
        }
        mDoctorModels = temp;
        listener.onUpdate(mDoctorModels.size());
        notifyDataSetChanged();
    }

    public void setGenderFilter(String filter) {
        genderFilter = filter;
        updateList(mDoctorModels);
    }


    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText;
        private CircleImageView smallImage;
        private LinearLayout linear;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.doctor_name_text);
            smallImage = itemView.findViewById(R.id.doctor_small_image);
            linear = itemView.findViewById(R.id.item_linear);
        }
    }
}
