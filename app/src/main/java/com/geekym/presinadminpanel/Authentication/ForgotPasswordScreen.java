package com.geekym.presinadminpanel.Authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geekym.presinadminpanel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordScreen extends AppCompatActivity {

    FirebaseAuth auth;
    EditText email;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        reset = findViewById(R.id.respassword);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                if(txt_email.isEmpty()){
                    email.setError("Field can't be empty");
                    email.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    email.setError("Please Enter a Valid Email ID");
                    email.requestFocus();
                    return;
                }else{
                    auth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            try {
                                if (task.isSuccessful()){
                                    Toast.makeText(ForgotPasswordScreen.this, "Reset Password Mail sent!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ForgotPasswordScreen.this, "Went Wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch(Exception e){
                                e.printStackTrace();
                                Log.d(TAG,"Email Sent");
                                return;
                            }
                        }
                    });
                }
            }
        });
    }
}