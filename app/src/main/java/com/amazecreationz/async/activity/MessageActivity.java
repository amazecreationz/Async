package com.amazecreationz.async.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazecreationz.async.R;
import com.amazecreationz.async.constants.AppConstants;
import com.amazecreationz.async.constants.FirebaseConstants;
import com.amazecreationz.async.models.User;
import com.amazecreationz.async.services.FirebaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageActivity extends AppCompatActivity {
    private final String TAG = "MessageActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        String messageCode = getIntent().getStringExtra(FirebaseConstants.MSG_CODE);
        switch (messageCode) {
            case FirebaseConstants.USER_NOT_VERIFIED:
                setContentView(R.layout.layout_verify);
                setVerifyUI();
                break;
            default: setContentView(R.layout.activity_message);
        }
    }

    private void setVerifyUI() {
        findViewById(R.id.check_verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getString(R.string.check_dialog_msg));
                progressDialog.show();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                final User user = new User(getApplicationContext());
                user.setUser(firebaseUser);
                if(firebaseUser != null) {
                    firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                Log.d(TAG, "User Email Verified!");
                                FirebaseService.getInstance().setData(user);
                                user.setIsUserVerified(true);
                                progressDialog.dismiss();
                                progressDialog = null;
                                Toast.makeText(getApplicationContext(), AppConstants.VERIFY_SUCCESS, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), AppConstants.VERIFY_FAILED, Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.resend_verify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getString(R.string.send_dialog_msg));
                progressDialog.show();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Resend verification email success!");
                            Toast.makeText(getApplicationContext(), AppConstants.RESEND_VERIFY_SUCCESS, Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Resend verification email failed!");
                            Toast.makeText(getApplicationContext(), AppConstants.RESEND_VERIFY_FAILED, Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    });
                }
            }
        });
    }
}