package com.digitalminds.dmssevent.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.digitalminds.dmssevent.models.AddedPlayersModel;
import com.digitalminds.dmssevent.models.RecentPlayersModel;
import com.digitalminds.dmssevent.models.ResultOFDepartment;
import com.digitalminds.dmssevent.service.RetrofitService;
import com.digitalminds.dmssevent.service.TotalBookingsApi;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMorePlayerRepository {
    private static AddMorePlayerRepository selectPlayerRepository;

    public static AddMorePlayerRepository getInstance(){
        if(selectPlayerRepository==null){
            selectPlayerRepository=new AddMorePlayerRepository();
        }
        return selectPlayerRepository;
    }

   private static TotalBookingsApi totalBookingsApi;

    public AddMorePlayerRepository() {
        totalBookingsApi= RetrofitService.cteateService(TotalBookingsApi.class);
    }

    public static MutableLiveData<RecentPlayersModel> getAddMorePlayersData(String searchKeyWord) {
        final MutableLiveData<RecentPlayersModel> addMorePlayerResponse = new MutableLiveData<>();
        totalBookingsApi.getAddMorePlayerDetails(searchKeyWord).enqueue(new Callback<RecentPlayersModel>() {
            @Override
            public void onResponse(Call<RecentPlayersModel> call, Response<RecentPlayersModel> response) {
                addMorePlayerResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RecentPlayersModel> call, Throwable t) {
                addMorePlayerResponse.setValue(null);
            }
        });
    return addMorePlayerResponse;
    }

    public static MutableLiveData<RecentPlayersModel> getPlayersDataByDepart(int DeptId) {
        final MutableLiveData<RecentPlayersModel> addMorePlayerResponse = new MutableLiveData<>();
        totalBookingsApi.getPlayerDetailsByDepart(DeptId).enqueue(new Callback<RecentPlayersModel>() {
            @Override
            public void onResponse(Call<RecentPlayersModel> call, Response<RecentPlayersModel> response) {
                addMorePlayerResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RecentPlayersModel> call, Throwable t) {
                addMorePlayerResponse.setValue(null);
            }
        });
        return addMorePlayerResponse;
    }

    public static MutableLiveData<ResultOFDepartment> getTotalDepartments(){
        final MutableLiveData<ResultOFDepartment> departmentResponse=new MutableLiveData<>();
        totalBookingsApi.getDepartmentsDetails().enqueue(new Callback<ResultOFDepartment>() {
            @Override
            public void onResponse(Call<ResultOFDepartment> call, Response<ResultOFDepartment> response) {
                departmentResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResultOFDepartment> call, Throwable t) {
                departmentResponse.setValue(null);
            }
        });
        return departmentResponse;
    }

    public static MutableLiveData<AddedPlayersModel> getAddedSelectedPlayersData(String userId, String gameName, JsonObject gameMateIds) {
        final MutableLiveData<AddedPlayersModel> addMorePlayersResponse = new MutableLiveData<>();
        totalBookingsApi.recentPlayersListCall(userId, gameName, gameMateIds).enqueue(new Callback<AddedPlayersModel>() {
            @Override
            public void onResponse(Call<AddedPlayersModel> call, Response<AddedPlayersModel> response) {
                addMorePlayersResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddedPlayersModel> call, Throwable t) {
                addMorePlayersResponse.setValue(null);
            }
        });
        return addMorePlayersResponse;
    }
}
