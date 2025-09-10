package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digitalminds.dmssevent.models.RecentPlayersModel;
import com.google.gson.JsonObject;

public class SelectPlayerViewModel extends ViewModel {

    private MutableLiveData<RecentPlayersModel> mutableLiveData;
    private SelectPlayerRepository selectPlayerRepository;

    public void init(String userId,String callFrom, String GameName, JsonObject GameMateIds){

        selectPlayerRepository=SelectPlayerRepository.getInstance();
        if(callFrom.equalsIgnoreCase("selectedplayer")){
            mutableLiveData=selectPlayerRepository.getSelectedPlayersData(userId,GameName,GameMateIds);
        }
    }

    public LiveData<RecentPlayersModel> getTotalSelectedPlayers()
    {
        return mutableLiveData;
    }

}
