package com.amazecreationz.async.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amazecreationz.async.R;
import com.amazecreationz.async.constants.FirebaseConstants;
import com.amazecreationz.async.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SetupActivity extends AppCompatActivity {

    private static final String TAG = "SetupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final User user = new User(this);
        if (user.getEmail() != null) {
            if (user.isUserVerified()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            boolean value = firebaseAuth.getCurrentUser().isEmailVerified();
                            user.setIsUserVerified(value);
                            Intent intent = new Intent(getApplicationContext(), value ? MainActivity.class : MessageActivity.class);
                            intent.putExtra(FirebaseConstants.MSG_CODE, FirebaseConstants.USER_NOT_VERIFIED);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        } else {
            showLoginPage();
        }
    }

    public void showLoginPage() {
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_login);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://amazecreationz.in/auth?redirect=async";
                new CustomTabsIntent.Builder().build().launchUrl(SetupActivity.this, Uri.parse(url));
            }
        });
    }
}