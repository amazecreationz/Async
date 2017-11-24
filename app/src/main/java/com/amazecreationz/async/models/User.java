package com.amazecreationz.async.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.amazecreationz.async.R;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by anandmoghan on 23/11/17.
 */

public class User {
    private Context context;
    private SharedPreferences sharedPreferences;

    public User(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_pref_key), Context.MODE_PRIVATE);
    }

    public void setUser(FirebaseUser user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.user_name), user.getDisplayName());
        editor.putString(context.getString(R.string.user_email), user.getEmail());
        editor.putString(context.getString(R.string.user_id), user.getUid());
        editor.putBoolean(context.getString(R.string.user_verified), user.isEmailVerified());
        editor.apply();
    }

    public void setIsUserVerified(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.user_verified), value);
        editor.apply();
    }

    public void deleteUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getString(R.string.user_name));
        editor.remove(context.getString(R.string.user_email));
        editor.remove(context.getString(R.string.user_verified));
        editor.remove(context.getString(R.string.user_photo_url));
        editor.remove(context.getString(R.string.user_id));
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(context.getString(R.string.user_name), null);
    }

    public String getEmail() {
        return sharedPreferences.getString(context.getString(R.string.user_email), null);
    }

    public boolean isUserVerified() {
        return sharedPreferences.getBoolean(context.getString(R.string.user_verified), false);
    }

    public String getPhotoURL() {
        return sharedPreferences.getString(context.getString(R.string.user_photo_url), null);
    }

    public String getUID() {
        return sharedPreferences.getString(context.getString(R.string.user_id), null);
    }
}
