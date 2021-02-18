package com.mertgolcu.zonezero.api;

import com.mertgolcu.zonezero.api.models.Doctors;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IService {
    @GET("android/doctors.json")
    Call<Doctors> getAllDoctors();
}
