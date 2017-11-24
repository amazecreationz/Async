package com.amazecreationz.async.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amazecreationz.async.R;
import com.amazecreationz.async.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        Context context = getApplicationContext();
        User user = new User(context);
        if(user.getEmail() != null) {
            if (user.isUserVerified()) {
                intent = new Intent(this, MainActivity.class);
            } else {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                boolean value = false;
                if (firebaseUser != null) {
                    firebaseUser.reload();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    value = firebaseUser.isEmailVerified();
                    user.setIsUserVerified(value);
                }
                intent = new Intent(this, value ? MainActivity.class : MessageActivity.class);
                intent.putExtra(getString(R.string.message_code), getString(R.string.user_not_verified_code));
            }
        }
        startActivity(intent);
        finish();
    }
}
