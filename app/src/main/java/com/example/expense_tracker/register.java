package com.example.expense_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class register extends AppCompatActivity {
    TextView textView;
    EditText mFullName, mEmail, mPassword, mRePassword;
    ImageButton mSignupBtn;
    static FirebaseAuth fAuth;
    private DatabaseReference databaseReference_name;
    String email;
    int condition;


// updated with user check to see if user has been registered or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_register);

        // IF ALREADY HAVE AN ACCOUNT, login text
        textView = (TextView) findViewById(R.id.signin);
        textView.setOnClickListener(v -> openlogin());

        // variables for registering
        mFullName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextEmailAddress);
        mPassword = findViewById(R.id.editTextTextPassword);
        mRePassword = findViewById(R.id.editTextTextPassword2);
        mSignupBtn = findViewById(R.id.signupbt);
        fAuth = FirebaseAuth.getInstance();



        // if the user is already logged in then
        if (fAuth.getCurrentUser() != null) {
            opengroup();
            finish();
        }

        mSignupBtn.setOnClickListener(v -> {
            String name = mFullName.getText().toString().trim();
            email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String RePassword = mRePassword.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                mFullName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password must be of at least 6 characters");
                return;
            }
            if (!password.equals(RePassword)) {
                mRePassword.setError("Password entered does not match");
                return;
            }

            // registering the user
            check_user(email);
            // checks whether the user already registered or not


            HashMap<String, String> hashMap1 = new HashMap<>();
            hashMap1.put("Username", name);

            if (condition == 2){
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    String uid = user.getUid();

                    databaseReference_name = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Name");

                    if (task.isSuccessful()) {
                        // inserting the users in the real time database
                        databaseReference_name.setValue(hashMap1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(register.this, "User Created", Toast.LENGTH_SHORT).show();
                                        opengroup();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(register.this, "Task failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(register.this, "Error! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        }
        });
    }


    public void openlogin() {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }

    public void opengroup() {
        Intent intent = new Intent(this, Group.class);
        startActivity(intent);
        finish();
    }

    public void check_user(String mail){

        FirebaseAuth user_auth = FirebaseAuth.getInstance();
        user_auth.fetchSignInMethodsForEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        if (check){
                          condition = 1;
                            Log.i("status", "user already exist "+condition);

                        }
                        else{
                            condition = 2;
                            Log.i("status", "user doesn't already exist "+condition);

                        }
                        if (condition == 1) {
                            Toast.makeText(register.this, "The user already exist", Toast.LENGTH_SHORT).show();
                            Log.i("status", "opening logging with condition "+condition);
                        }
                    }
                });


    }
}