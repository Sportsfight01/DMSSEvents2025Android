package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalminds.dmssevent.models.BookMyGameModel;
import com.digitalminds.dmssevent.models.ConfirmBookingResponse;
import com.google.gson.JsonObject;

public class BookMyGameViewModel extends ViewModel {

    private MutableLiveData<BookMyGameModel> mutableLiveData;
    private BookingDateNTimesRepository totalBookingsRepository;
    private MutableLiveData<ConfirmBookingResponse> mutableLiveDataStatusResonse;
    public void init(String callFrom,int GameID,int Day){
       /* if (mutableLiveData != null){
            return;
        }*/
        totalBookingsRepository = BookingDateNTimesRepository.getInstance();
        if(callFrom.equalsIgnoreCase("getTimings")){
            mutableLiveData = totalBookingsRepository.getTotalBookingsData(GameID,Day);
        }
    }
    public void initBooking(String userId,String callFrom,JsonObject jsonObject){
        totalBookingsRepository = BookingDateNTimesRepository.getInstance();
        if (callFrom.equalsIgnoreCase("callBookMyGameService")) {
            mutableLiveDataStatusResonse = totalBookingsRepository.callBookMyGameData(userId,jsonObject);
        }
    }



    public LiveData<BookMyGameModel> getTotalGamesDetailsLiveData()
    {
        return mutableLiveData;
    }

    public LiveData<ConfirmBookingResponse> getFinalBookingResponseData() {
        return mutableLiveDataStatusResonse;
    }


}
