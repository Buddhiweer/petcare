package com.bStudio.petcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    ImageView facebookBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.sign_in_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.signup_text_view_btn);

        loginBtn.setOnClickListener((v)-> loginUser());
        createAccountBtnTextView.setOnClickListener((v -> startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class))));

        googleBtn = findViewById(R.id.google_btn);
        facebookBtn = findViewById(R.id.facebook_btn);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null){
            navigateToSecondActivity();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Handle the successful sign-in
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Use the account information
                navigateToSecondActivity();
            } catch (ApiException e) {
                // Handle failed sign-in
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this,Home.class);
        startActivity(intent);
    }

    void loginUser(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email,password);
        if (!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);
    }


    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        // go to mainactivity
                        startActivity(new Intent(LoginActivity.this,Home.class));
                    }else{
                        Utility.showToast(LoginActivity.this,"Email not verified, Please verified your email.");
                    }

                }else {
                    //login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){
        //validate the data that are input by user.

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return  false;
        }
        if (password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }

}