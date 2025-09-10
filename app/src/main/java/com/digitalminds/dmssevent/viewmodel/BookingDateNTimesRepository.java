package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.digitalminds.dmssevent.models.BookMyGameModel;
import com.digitalminds.dmssevent.models.ConfirmBookingResponse;
import com.digitalminds.dmssevent.service.RetrofitService;
import com.digitalminds.dmssevent.service.TotalBookingsApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDateNTimesRepository {

    private static BookingDateNTimesRepository newsRepository;

    public static BookingDateNTimesRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new BookingDateNTimesRepository();
        }
        return newsRepository;
    }

    private TotalBookingsApi totalBookingsApi;

    public BookingDateNTimesRepository() {
        totalBookingsApi = RetrofitService.cteateService(TotalBookingsApi.class);
    }

    public MutableLiveData<BookMyGameModel> getTotalBookingsData(int gameId,int day) {
        final MutableLiveData<BookMyGameModel> bookingResponseData = new MutableLiveData<>();
        totalBookingsApi.getBookingDetails(gameId,day).enqueue(new Callback<BookMyGameModel>() {
            @Override
            public void onResponse(Call<BookMyGameModel> call, Response<BookMyGameModel> response) {
                bookingResponseData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BookMyGameModel> call, Throwable t) {
                bookingResponseData.setValue(null);
            }
        });
        return bookingResponseData;
    }

    public MutableLiveData<ConfirmBookingResponse> callBookMyGameData(String userId, JsonObject jsonObject) {
        final MutableLiveData<ConfirmBookingResponse> bookingResponseData = new MutableLiveData<>();
        totalBookingsApi.callBookMyGameService(userId,jsonObject).enqueue(new Callback<ConfirmBookingResponse>() {
            @Override
            public void onResponse(Call<ConfirmBookingResponse> call, Response<ConfirmBookingResponse> response) {
                bookingResponseData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ConfirmBookingResponse> call, Throwable t) {
                bookingResponseData.setValue(null);
            }
        });
        return bookingResponseData;
    }


}
