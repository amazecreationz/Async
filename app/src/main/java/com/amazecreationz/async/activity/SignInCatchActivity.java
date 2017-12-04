package com.amazecreationz.async.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazecreationz.async.R;
import com.amazecreationz.async.constants.FirebaseConstants;
import com.amazecreationz.async.models.User;
import com.amazecreationz.async.services.FirebaseService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URI;
import java.net.URL;

public class SignInCatchActivity extends AppCompatActivity {

    private static final String TAG = "SignInCatchActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.validate_dialog_msg));

        Intent intent = getIntent();
        Uri uri = intent.getData();
        if(uri != null) {
            progressDialog.show();
            String token = uri.getQueryParameter(FirebaseConstants.TOKEN_VALUE);
            Log.d(TAG, token);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = FirebaseConstants.CREATE_CUSTOM_TOKEN_URL + token;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String cToken) {
                            login(cToken);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Sign in failed! Try Again", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }
    }

    private void login(String token) {
        FirebaseAuth.getInstance().signInWithCustomToken(token).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCustomToken:success");
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Context context = getApplicationContext();
                    if(firebaseUser != null) {
                        Intent intent;
                        User user = new User(context);
                        user.setUser(firebaseUser);
                        if(firebaseUser.isEmailVerified()) {
                            FirebaseService.getInstance().setData(user);
                            intent = new Intent(context, MainActivity.class);
                        } else {
                            intent = new Intent(context, MessageActivity.class);
                            intent.putExtra(FirebaseConstants.MSG_CODE, FirebaseConstants.USER_NOT_VERIFIED);
                        }
                        progressDialog.dismiss();
                        progressDialog = null;
                        Toast.makeText(context, "Welcome "+user.getName(), Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "Sign in failed! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
