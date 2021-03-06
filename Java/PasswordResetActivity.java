package com.example.lastmanstanding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private static final String TAG = "PasswordReset";

    private EditText mEmailField;
    private Button msendEmailBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mProgressBar = (ProgressBar) findViewById(R.id.passwordResetProgressBar);
        mProgressBar.setVisibility(View.GONE);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.passwordResetEmail);
        mEmailField.setVisibility(View.VISIBLE);

        msendEmailBtn = (Button) findViewById(R.id.passwordResetButton);


        msendEmailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                msendEmailBtn.setEnabled(false);
                hideKeyboard();
                mEmailField.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);

                String emailAddress = mEmailField.getText().toString().trim();

                if (TextUtils.isEmpty(emailAddress)) {
                    mProgressBar.setVisibility(View.GONE);
                    mEmailField.setVisibility(View.VISIBLE);
                    msendEmailBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    mEmailField.setText("");
                                    mProgressBar.setVisibility(View.GONE);
                                    mEmailField.setVisibility(View.VISIBLE);
                                    msendEmailBtn.setEnabled(true);
                                    Toast.makeText(PasswordResetActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mProgressBar.setVisibility(View.GONE);
                                    mEmailField.setVisibility(View.VISIBLE);
                                    msendEmailBtn.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), "Email failed to send." + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public void logIn(View view) {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}