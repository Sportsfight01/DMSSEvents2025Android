package com.digitalminds.dmssevent.interfaces;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("http://www.digitalminds.solutions/DMSSApp/api/login/AddEmpLocation")
    Call<ResponseBody> postLocationLatLong(@Body HashMap<String, String> body);
}
