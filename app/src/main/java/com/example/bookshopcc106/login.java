package com.example.bookshopcc106;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {


    Button login;
    TextView signup;
    EditText email,password;
    FirebaseAuth firebaseAuth;
    TextView errorEmail,errorPassword;
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