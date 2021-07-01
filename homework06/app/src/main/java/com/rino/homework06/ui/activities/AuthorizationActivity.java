package com.rino.homework06.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;
import com.rino.homework06.R;

public class AuthorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initView();
    }

    private void initView() {
        MaterialButton continueWithoutAuthButton = findViewById(R.id.continue_without_auth_button);
        continueWithoutAuthButton.setOnClickListener(view -> {
            MainActivity.startActivity(this);
            finish();
        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);
    }
}