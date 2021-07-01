package com.rino.homework06.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.rino.homework06.R;
import com.rino.homework06.common.utils.Utils;

import java.text.MessageFormat;

public class AuthorizationActivity extends AppCompatActivity {

    private static final String TAG = "GoogleAuth";

    // Клиент для регистрации пользователя через Гугл
    private GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> startSignInForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Когда сюда возвращается Task, результаты по нему уже готовы.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInResult(task);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        initViews();
    }

    public void startMainActivity(GoogleSignInAccount account) {
        MainActivity.startActivity(
                this,
                account == null ? null : account.getDisplayName(),
                account == null ? null : account.getEmail()
        );

        finish();
    }

    private void initViews() {
        MaterialButton continueWithoutAuthButton = findViewById(R.id.continue_without_auth_button);

        continueWithoutAuthButton.setOnClickListener(view -> startMainActivity(null));

        SignInButton signInButton = findViewById(R.id.sign_in_button);

        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл (регулируется параметром)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Получить клиента для регистрации, а также данных по клиенту
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(v -> signIn());

        MaterialButton signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(view -> signOut());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startSignInForResult.launch(signInIntent);
    }

    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получение данных пользователя
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Регистрация прошла успешно
            String msg = MessageFormat.format("Sign in with google account {0}!", account.getEmail());
            Utils.showToastShort(this, msg);

            startMainActivity(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            String msg = "signInResult: failed code=" + e.getStatusCode();
            Log.w(TAG, msg);

            Utils.showToastShort(this, msg);
        }
    }

    // Выход из учетной записи в приложении
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this,
                        task -> Utils.showToastShort(this, "Sign out from google account!"));
    }
}