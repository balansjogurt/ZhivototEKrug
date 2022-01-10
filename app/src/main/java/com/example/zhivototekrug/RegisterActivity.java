package com.example.zhivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail, editTextPhone, editTextPassword, editTextPassword2, ediTextName;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ediTextName = (EditText) findViewById(R.id.registerName);
        editTextEmail = (EditText) findViewById(R.id.registerEmail);
        editTextPhone = (EditText) findViewById(R.id.registerPhone);
        editTextPassword = (EditText) findViewById(R.id.registerPassword);
        editTextPassword2 = (EditText) findViewById(R.id.confirmPassword);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void Register(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        String name = ediTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String tip = "Normal user";

        int ID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(ID);

        if(radioButton.getId() == R.id.radioVolunteer) {
            tip = "Volunteer";
        }

        if(name.equals("")) {
            ediTextName.setError("Please enter a First and Last Name!");
            ediTextName.requestFocus();
            return;
        }

        if(email.equals("")) {
            editTextEmail.setError("Please enter an Email address!");
            editTextEmail.requestFocus();
            return;
        } else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please enter a valid Email address!!");
                editTextEmail.requestFocus();
                return;
            }
        }

        if(password.equals("")) {
            editTextPassword.setError("Please enter a Password!");
            editTextPassword.requestFocus();
            return;
        } else {
            if(password.length() < 6) {
                editTextPassword.setError("Password has to contain at least 6 characters!");
                editTextPassword.requestFocus();
                return;
            }
        }

        if(password2.equals("")) {
            editTextPassword2.setError("Repeat the password!");
            editTextPassword2.requestFocus();
            return;
        }

        if(!password.equals(password2)) {
            editTextPassword.setError("Password doesn't match!");
            editTextPassword.requestFocus();
            return;
        }

        if(phone.equals("")) {
            editTextPhone.setError("Please enter a Phone number!");
            editTextPhone.requestFocus();
            return;
        } else {
            if(!Patterns.PHONE.matcher(phone).matches()) {
                editTextPhone.setError("Please enter a valid Phone number!");
                editTextPhone.requestFocus();
                return;
            }
        }


        String finalTip = tip;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    User user = new User(email, phone, finalTip, name, 0, 0);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Unable to register!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Unable to register!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}