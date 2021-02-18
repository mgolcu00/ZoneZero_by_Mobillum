package com.mertgolcu.zonezero.ui;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mertgolcu.zonezero.R;
import com.mertgolcu.zonezero.api.models.Doctor;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailFragment extends Fragment {

    private Doctor doctor;
    private TextView nameText, premiumText, button_text;
    private CircleImageView profileImage;
    private CardView buttonCard;

    private boolean isPremium;

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        //init
        doctor = gsonToDoctor(DetailFragmentArgs.fromBundle(getArguments()).getDoctorGson());

        //hooks
        nameText = v.findViewById(R.id.profile_name_text);
        premiumText = v.findViewById(R.id.premium_text);
        profileImage = v.findViewById(R.id.profile_image);
        buttonCard = v.findViewById(R.id.button_card);
        button_text = v.findViewById(R.id.button_text);


        //set data
        isPremium = doctor.getUser_status().equals("premium");

        nameText.setText(doctor.getFull_name());
        premiumText.setVisibility(isPremium ? View.VISIBLE : View.GONE);
        button_text.setText(isPremium ? getResources().getString(R.string.get_appointment) : getResources().getString(R.string.get_premium));

        buttonCard.setOnClickListener(view -> {
            NavDirections action;
            action = isPremium ? DetailFragmentDirections.actionDetailFragmentToAppointmentFragment() : DetailFragmentDirections.actionDetailFragmentToPaymentFragment();
            Navigation.findNavController(v).navigate(action);
        });
        Picasso.with(getContext()).load(doctor.getImage().getUrl()).fit().centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(profileImage);


        return v;
    }

    public Doctor gsonToDoctor(String json) {
        Gson gson = new Gson();
        Doctor d = gson.fromJson(json, Doctor.class);
        return d;
    }
}
