package com.example.navbar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class SignUp<context> extends AppCompatActivity {

    private WebView mWebView;
    Activity Context;
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth rAuth;
    SignUp context;








    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_signup);



        context = this;




        //connects XML elements to variables

        mFullName =  findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn =  findViewById(R.id.LoginBtn);
        mLoginBtn =  findViewById(R.id.createText);



        rAuth = FirebaseAuth.getInstance();


        //when register button is clicked, checks if the required parameters are met
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }
                if (password.length() < 6){
                    mPassword.setError("Password must be at least 6 characters");
                    return;
                }

                rAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Context.getApplicationContext(), MainActivity.class));

                        }else{
                            System.out.println("Registration not successful");


                        }
                    }
                });
            }
        });

        //if the user is already saved then no need to re-sign-up

        if (rAuth.getCurrentUser() != null){
            Intent intent;
            intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);

        }

        //if the user already has an account, go to the signin page

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));


            }
        });












    }









}
