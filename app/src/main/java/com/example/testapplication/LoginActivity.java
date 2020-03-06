package com.example.testapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Code here
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.loginButton);
        final EditText emailEditText = findViewById(R.id.loginEmailEditText);
        final EditText passwordEditText = findViewById(R.id.loginPasswordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() && password.isEmpty()){
                    emailEditText.setError("Please key in your email address!");
                    emailEditText.requestFocus();
                    passwordEditText.setError("Please key in your password!");
                    passwordEditText.requestFocus();
                } else if(email.isEmpty()){
                    emailEditText.setError("Please key in your email address!");
                    emailEditText.requestFocus();
                } else if(password.isEmpty()){
                    passwordEditText.setError("Please key in your password!");
                    passwordEditText.requestFocus();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // TODO: Change to home page.
//                                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}
