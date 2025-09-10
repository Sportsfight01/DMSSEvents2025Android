package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalminds.dmssevent.models.BookingStatusResonse;
import com.digitalminds.dmssevent.models.RemovePlayerModel;
import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;
import com.google.gson.JsonObject;

public class TotalBookingViewModel extends ViewModel {

    private MutableLiveData<TotalBookingGamesResonse> mutableLiveData;
    private TotalBookingsRepository totalBookingsRepository;
    private MutableLiveData<BookingStatusResonse> mutableLiveDataStatusResonse;
    private MutableLiveData<RemovePlayerModel> mutableLiveDataRemovePLStatusResonse;

    public void init(String userID){
       /* if (mutableLiveData != null){
            return;
        }*/
        totalBookingsRepository = TotalBookingsRepository.getInstance();
        mutableLiveData = totalBookingsRepository.getTotalBookingsData(userID);
    }

    public void initWithDrawUser(String userId,String callFrom,int bookingIds,String bookingStatus){
        if(callFrom.equalsIgnoreCase("withdraw")){
            mutableLiveDataStatusResonse = totalBookingsRepository.getBookingStatusData(userId,bookingIds,bookingStatus);
        }

    }
    public void initRemoveUser(int gameID,int playerId,int userId){
       // mutableLiveDataRemovePLStatusResonse = totalBookingsRepository.getRemovePlayerResponseData1(gameID,playerId,userId);

    }

    public LiveData<TotalBookingGamesResonse> getTotalGamesData() {
        return mutableLiveData;
    }

    public LiveData<BookingStatusResonse> getWithdrawResponseData() {
        return mutableLiveDataStatusResonse;
    }
    public LiveData<RemovePlayerModel> getRemovePlayerResponseData() {
        return mutableLiveDataRemovePLStatusResonse;
    }


    public void initRemoveUser(JsonObject jsonRemovePlayer) {
        mutableLiveDataRemovePLStatusResonse = totalBookingsRepository.getRemovePlayerResponseData(jsonRemovePlayer);

    }
}
