package com.amazecreationz.async.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.amazecreationz.async.constants.AppConstants;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anandmoghan on 23/11/17.
 */

public class User implements AppConstants {
    private SharedPreferences sharedPreferences;
    private Context context;

    public User(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(USER_PREF_KEY, Context.MODE_PRIVATE);
    }



    public void setUser(FirebaseUser user) {
        String devId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, user.getDisplayName());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.putString(USER_ID, user.getUid());
        editor.putBoolean(USER_VERIFIED, user.isEmailVerified());
        editor.putString(DEVICE_ID, devId);
        editor.apply();
    }

    public void setIsUserVerified(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(USER_VERIFIED, value);
        editor.apply();
    }

    public void deleteUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_NAME);
        editor.remove(USER_EMAIL);
        editor.remove(USER_VERIFIED);
        editor.remove(USER_PHOTO_URL);
        editor.remove(USER_ID);
        editor.remove(DEVICE_ID);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(USER_EMAIL, null);
    }

    public boolean isUserVerified() {
        return sharedPreferences.getBoolean(USER_VERIFIED, false);
    }

    public String getPhotoURL() {
        return sharedPreferences.getString(USER_PHOTO_URL, null);
    }

    public String getUID() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public String getDeviceID() {
        return sharedPreferences.getString(DEVICE_ID, null);
    }

    public Map<String, String> getUser() {
        Map<String, String> user = new HashMap<>();
        user.put(F_UNAME, getName());
        user.put(F_UEMAIL, getEmail());
        user.put(F_UPURL, getPhotoURL());
        return user;
    }
}