package com.amazecreationz.async.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazecreationz.async.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageActivity extends AppCompatActivity {
    private final String TAG = "MESSAGE_ACTIVITY";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

        String messageCode = getIntent().getStringExtra(getString(R.string.message_code));
        switch (messageCode) {
            case "USER_TO_BE_VERIFIED":
            case "USER_NOT_VERIFIED":
                setContentView(R.layout.layout_verify);
                setVerifyUI();
                break;
            default: setContentView(R.layout.activity_message);
        }
    }

    private void setVerifyUI() {
        Button checkVerify = findViewById(R.id.check_verify_btn);
        Button resendVerify = findViewById(R.id.resend_verify_btn);

        checkVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getString(R.string.check_dialog_msg));
                progressDialog.show();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null) {
                    firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.verify_failed), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });
                }
            }
        });

        resendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage(getString(R.string.send_dialog_msg));
                progressDialog.show();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null) {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(getApplicationContext(), getString(R.string.resend_verify), Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });
    }
}
