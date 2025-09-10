package com.digitalminds.dmssevent.service;

import com.digitalminds.dmssevent.models.AddedPlayersModel;
import com.digitalminds.dmssevent.models.BookMyGameModel;
import com.digitalminds.dmssevent.models.BookingStatusResonse;
import com.digitalminds.dmssevent.models.ConfirmBookingResponse;
import com.digitalminds.dmssevent.models.RecentPlayersModel;
import com.digitalminds.dmssevent.models.RemovePlayerModel;
import com.digitalminds.dmssevent.models.ResultOFDepartment;
import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;
import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TotalBookingsApi {

    @GET("bookings/GetBookingsByPersonId/{PersonId}")
    Call<TotalBookingGamesResonse> getTotalGamesListData(@Path("PersonId") String PersonId);

    /*@POST("bookings/Invitation")
    Call<TotalBookingGamesResonse> getsTotalGamesListData(@Path("PersonId") String PersonId);*/

    @FormUrlEncoded
    @POST("bookings/Invitation")
    Call<BookingStatusResonse> acceptOrRejectStatus(@Field("MyId") String userID, @Field("BookingID") int BookingID, @Field("BookingStatus") String BookingStatus);

    @GET("bookings/getBookings/{GameID}/{Day}")
    Call<BookMyGameModel> getBookingDetails(@Path("GameID") int GameID, @Path("Day") int Day);

    @POST("bookings/GameMate/{MyId}/{GameName}")
   Call<AddedPlayersModel> recentPlayersListCall(@Path("MyId") String userID, @Path("GameName") String GameName, @Body JsonObject GameMateIds);

    @POST("bookings/GameMate/{MyId}/{GameName}")
    Call<RecentPlayersModel> testRecentPlayersListCall(@Path("MyId") String userID, @Path("GameName") String GameName,@Body JsonObject GameMateIds);

    @GET("nomine/searchEmployee/{Keyword}/")
    Call<RecentPlayersModel> getAddMorePlayerDetails(@Path("Keyword") String keyword);
    @GET("nomine/getemp/{DeptId}/")
    Call<RecentPlayersModel> getPlayerDetailsByDepart(@Path("DeptId") int DeptId);
    @GET("nomine/getdept")
    Call<ResultOFDepartment> getDepartmentsDetails();



    @POST("bookings/BookMyGame/{MyId}")
    Call<ConfirmBookingResponse> callBookMyGameService(@Path("MyId") String userID, @Body JsonObject bookingData);

    @FormUrlEncoded
    @POST("bookings/RemovePlayer")
    Call<BookingStatusResonse> removePlayerStatus1(@Field("BookingId") int gameId, @Field("RemovePersonId") int personId, @Field("MyId") int MyID);

    @POST("bookings/RemovePlayer")
    Call<RemovePlayerModel> removePlayerStatus(@Body JsonObject removeplayer);

}
