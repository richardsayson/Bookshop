package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {



    Button login;
    TextView signup;
    EditText email,password;
    FirebaseAuth firebaseAuth;
    TextView errorEmail,errorPassword;
    private AlertDialog loadingDialog;

    LinearLayout googlelogin,facebookLogin;

    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        errorEmail = findViewById(R.id.tv_error_email);
        errorPassword = findViewById(R.id.tv_error_password);
        firebaseAuth = FirebaseAuth.getInstance();
        /////-----------binding the resources from the ui
        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.tv_signup);
        googlelogin = findViewById(R.id.google_logIn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(login.this, "Work In Progress!", Toast.LENGTH_LONG).show();
            }
        });
        facebookLogin = findViewById(R.id.facebook_login);
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(login.this, "Work In Progress!", Toast.LENGTH_LONG).show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), signup.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              LogSuccess();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1234) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                 AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                 FirebaseAuth.getInstance().signInWithCredential(credential)
                                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                     @Override
                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                         if (task.isSuccessful()){
                                             Intent i = new Intent(getApplicationContext(),home.class);
                                             startActivity(i);
                                         }else{

                                         }
                                     }
                                 });

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
               e.printStackTrace();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            // ...
                            Intent intent = new Intent(login.this, home.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // ...
                        }
                    }
                });
    }
    private void GoogleSuccess() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        ///--------checking if there is a current user log in
       FirebaseUser user = firebaseAuth.getCurrentUser();
       if (user != null){
           Intent intent = new Intent(login.this, home.class);
           startActivity(intent);
       }
       else {
       }
    }
    private void showLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        builder.setView(view);
        loadingDialog = builder.create();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
    private void LogSuccess() {
        String Email = email.getText().toString(),
                Password = password.getText().toString();

        if (!Email.isEmpty()){
            errorEmail.setText("");
            if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                if (!Password.isEmpty()) {
                    errorPassword.setText("");

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                         /* dialog = new Dialog(com.example.bookshopcc106.signup.this);
                          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                          dialog.setCanceledOnTouchOutside(false);
                          dialog.show();

                          */
                    firebaseAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Toast.makeText(login.this, "Successful", Toast.LENGTH_LONG).show();
                                        FirebaseUser User = firebaseAuth.getCurrentUser();
                                        if (User != null) {
                                            Intent intent = new Intent(login.this, home.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else {
                                        errorEmail.setText("Wrong Email or Password");
                                        email.requestFocus();
                                    }

                                }
                            });

                } else {
                    errorPassword.setText("Enter Password");
                    password.requestFocus();
                }
            } else {
                errorEmail.setText("Please Enter A Valid Email Address");
                email.requestFocus();
            }

        }
        else {
            errorEmail.setText("Enter Email");
            email.requestFocus();
        }
    }
}