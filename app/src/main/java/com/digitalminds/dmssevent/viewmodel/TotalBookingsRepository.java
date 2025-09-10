package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.digitalminds.dmssevent.models.BookingStatusResonse;
import com.digitalminds.dmssevent.models.RemovePlayerModel;
import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;
import com.digitalminds.dmssevent.service.RetrofitService;
import com.digitalminds.dmssevent.service.TotalBookingsApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalBookingsRepository {

    private static TotalBookingsRepository newsRepository;

    public static TotalBookingsRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new TotalBookingsRepository();
        }
        return newsRepository;
    }

    private TotalBookingsApi totalBookingsApi;

    public TotalBookingsRepository() {
        totalBookingsApi = RetrofitService.cteateService(TotalBookingsApi.class);
    }

    public MutableLiveData<TotalBookingGamesResonse> getTotalBookingsData(String PersonId) {
        final MutableLiveData<TotalBookingGamesResonse> bookingResponseData = new MutableLiveData<>();
        totalBookingsApi.getTotalGamesListData(PersonId).enqueue(new Callback<TotalBookingGamesResonse>() {
            @Override
            public void onResponse(Call<TotalBookingGamesResonse> call, Response<TotalBookingGamesResonse> response) {

                if (response.isSuccessful()) {
                    bookingResponseData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<TotalBookingGamesResonse> call, Throwable t) {
                bookingResponseData.setValue(null);
            }
        });
        return bookingResponseData;
    }


    public MutableLiveData<BookingStatusResonse> getBookingStatusData(String userId, int BookingID, String BookingStatus) {
        final MutableLiveData<BookingStatusResonse> bookingResponseData = new MutableLiveData<>();
        totalBookingsApi.acceptOrRejectStatus(userId, BookingID, BookingStatus).enqueue(new Callback<BookingStatusResonse>() {
            @Override
            public void onResponse(Call<BookingStatusResonse> call, Response<BookingStatusResonse> response) {
                if (response.isSuccessful()) {
                    bookingResponseData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BookingStatusResonse> call, Throwable t) {
                bookingResponseData.setValue(null);
            }
        });
        return bookingResponseData;
    }

    public MutableLiveData<BookingStatusResonse> getRemovePlayerResponseData1(int gameID, int playerId, int userId) {
        final MutableLiveData<BookingStatusResonse> removePlayerResponseData = new MutableLiveData<>();
        totalBookingsApi.removePlayerStatus1(gameID, playerId, userId).enqueue(new Callback<BookingStatusResonse>() {
            @Override
            public void onResponse(Call<BookingStatusResonse> call, Response<BookingStatusResonse> response) {
                removePlayerResponseData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BookingStatusResonse> call, Throwable t) {
                removePlayerResponseData.setValue(null);
            }
        });

        return removePlayerResponseData;
    }

    public MutableLiveData<RemovePlayerModel> getRemovePlayerResponseData(JsonObject jsonRemovePlayer) {
        final MutableLiveData<RemovePlayerModel> removePlayerResponseData = new MutableLiveData<>();
        totalBookingsApi.removePlayerStatus(jsonRemovePlayer).enqueue(new Callback<RemovePlayerModel>() {
            @Override
            public void onResponse(Call<RemovePlayerModel> call, Response<RemovePlayerModel> response) {
                removePlayerResponseData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RemovePlayerModel> call, Throwable t) {
                removePlayerResponseData.setValue(null);
            }
        });

        return removePlayerResponseData;
    }
}

