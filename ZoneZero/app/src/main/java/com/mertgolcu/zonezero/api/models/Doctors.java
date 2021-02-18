package com.mertgolcu.zonezero.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Doctors {

    @SerializedName("doctors")
    @Expose
    private List<Doctor> doctors = null;

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

}