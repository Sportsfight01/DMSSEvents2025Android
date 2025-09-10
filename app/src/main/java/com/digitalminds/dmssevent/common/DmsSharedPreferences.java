package com.digitalminds.dmssevent.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.digitalminds.dmssevent.R;
import com.digitalminds.dmssevent.models.UserProfileModel;
import com.google.gson.Gson;

/**
 * DmsSharedPreferences.java -
 *
 * @author Jaya Krishna
 * @version 1.0
 * @since 09-03-2017.
 */
public class DmsSharedPreferences {

    @SuppressWarnings("static-access")
    public static void saveUserDetails(Context context, UserProfileModel userProfileModel) {
        Gson gson = new Gson();
        String userProfileModelJson = gson.toJson(userProfileModel);
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.pref_userDetails), userProfileModelJson)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static UserProfileModel getUserDetails(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        String vendorsDetailsJson = pref.getString(context.getString(R.string.pref_userDetails), null);
        Gson gson = new Gson();
        return gson.fromJson(vendorsDetailsJson, UserProfileModel.class);
    }
    @SuppressWarnings("static-access")
    public static void saveUserLoggedInStatus(Context context, boolean status) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putBoolean(context.getString(R.string.pref_userLoggedIn), status)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static boolean isUserLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getBoolean(context.getString(R.string.pref_userLoggedIn), false);
    }

    @SuppressWarnings("static-access")
    public static void saveLastLoggedInId(Context context, int id) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putInt(context.getString(R.string.pref_lastLoggedInId), id)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static int getLastLoggedInId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getInt(context.getString(R.string.pref_lastLoggedInId), 0);
    }

    @SuppressWarnings("static-access")
    public static void saveProfilePicUrl(Context context, String url) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.pref_profilePicUrl), url)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static String getProfilePicUrl(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getString(context.getString(R.string.pref_profilePicUrl), "");
    }

    @SuppressWarnings("static-access")
    public static void saveOwnerLoggedInStatus(Context context, boolean status) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putBoolean("isOwner", status)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static boolean isOwnerLoggedIn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getBoolean("isOwner", false);
    }


    @SuppressWarnings("static-access")
    public static void saveIntoductionReadOrNot(Context context, boolean status) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putBoolean("isRead", status)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static boolean isIntoductionReadOrNot(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getBoolean("isRead", false);
    }

    @SuppressWarnings("static-access")
    public static void saveNominationCount(Context context, int count) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putInt(context.getString(R.string.pref_nominationCount), count)
                .apply();
    }

    @SuppressWarnings("static-access")
    public static int getNominationCount(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getInt(context.getString(R.string.pref_nominationCount), 2);
    }
    @SuppressWarnings("static-access")
    public static void saveFCMToken(Context context, String token) {
        context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.pref_fcm_token_id), token)
                .apply();
    }


    @SuppressWarnings("static-access")
    public static String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.dmsSharedEventsPreference), context.MODE_PRIVATE);
        return pref.getString(context.getString(R.string.pref_fcm_token_id), "");
    }
}
