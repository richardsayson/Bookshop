package com.example.bookshopcc106;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    Button Signup;
    EditText email,password,confirmpassword;
    FirebaseAuth firebaseAuth;
    Dialog dialog;
    TextView login,errorEmail,errorPassword,errorConfirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Signup = findViewById(R.id.btn_signup);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        errorEmail = findViewById(R.id.tv_error_email);
        errorPassword = findViewById(R.id.tv_error_password);
        errorConfirmpassword = findViewById(R.id.tv_error_confirmpassword);
        confirmpassword = findViewById(R.id.et_confirmpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString(),
                        Password = password.getText().toString(),
                        Confirmpassword = confirmpassword.getText().toString();
         if (!Email.isEmpty()){
             errorEmail.setText("");
                if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    if (!Password.isEmpty()) {
                        errorPassword.setText("");
                        if (Password.length() >= 8){
                            errorPassword.setText("");
                        if (!Confirmpassword.isEmpty()) {
                            errorConfirmpassword.setText("");
                            if (Password.matches(Confirmpassword)) {

                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                         /* dialog = new Dialog(com.example.bookshopcc106.signup.this);
                          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                          dialog.setCanceledOnTouchOutside(false);
                          dialog.show();

                          */

                                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(signup.this, "Successful", Toast.LENGTH_LONG).show();
                                                    FirebaseUser User = firebaseAuth.getCurrentUser();
                                                    if (User != null) {


                                                        Intent intent = new Intent(signup.this, home.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                errorConfirmpassword.setText("Password Not Match");
                                confirmpassword.requestFocus();
                            }
                        } else {
                            errorConfirmpassword.setText("Confirm Password");
                            confirmpassword.requestFocus();
                        }
                    }else {
                            errorPassword.setText("Password Too Short");
                            password.requestFocus();
                        }
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
        });

        login = findViewById(R.id.tv_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(signup.this,login.class);
                startActivity(i);
            }
        });
    }
}