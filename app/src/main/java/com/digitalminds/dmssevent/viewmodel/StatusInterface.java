package com.digitalminds.dmssevent.viewmodel;

import com.digitalminds.dmssevent.models.TotalBookingGamesResonse;

public interface StatusInterface {

    public void onSuccess(TotalBookingGamesResonse gamesResonse);

    public void onError(String error);
}
