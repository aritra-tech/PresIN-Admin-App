package com.geekym.presinadminpanel.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geekym.presinadminpanel.HomeScreen;
import com.geekym.presinadminpanel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private EditText email , password;
    private TextView forgotpass;
    private Button login;
    private CheckBox checkBox;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        email =  findViewById(R.id.editTextTextEmailAddress);
        password =  findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);
        auth = FirebaseAuth.getInstance();
        forgotpass = findViewById(R.id.forgetpass);
        checkBox = findViewById(R.id.checkBox);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(LoginScreen.this , ForgotPasswordScreen.class);
                startActivity(a);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }
                else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString().trim();
                String txt_pass = password.getText().toString().trim();

                if(txt_email.isEmpty()){
                    email.setError("Field can't be empty");
                    email.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    email.setError("Please enter a valid Email Id");
                    email.requestFocus();
                    return;
                }
                else if (txt_pass.isEmpty()){
                    password.setError("Field can't be empty");
                    password.requestFocus();
                    return;
                }
                else{
                    auth.signInWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()){
                                    Intent intent2 = new Intent(getApplicationContext(),HomeScreen.class);
                                    startActivity(intent2);
                                    finishAffinity();
                                }
                                else{
                                    user.sendEmailVerification();
                                    Toast.makeText(LoginScreen.this, "Check your email to verify your account and Login again", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginScreen.this, "Failed to Login! Please check your credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}