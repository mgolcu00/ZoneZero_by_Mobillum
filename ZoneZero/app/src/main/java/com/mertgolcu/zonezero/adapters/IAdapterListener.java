package com.mertgolcu.zonezero.adapters;

import android.view.View;

import com.mertgolcu.zonezero.api.models.Doctor;

public interface IAdapterListener {
    //on recycler item clicked
    void onItemClick(View v, Doctor doctor);

    //on adapter data updated
    void onUpdate(int size);
}
