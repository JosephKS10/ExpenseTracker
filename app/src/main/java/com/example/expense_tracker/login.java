package com.example.expense_tracker;

import static com.example.expense_tracker.create_group.GetUserid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

// check whether the group is present or not on login in

public class login extends AppCompatActivity {
    TextView textView;
    EditText mEmail,mPassword;
    ImageButton mLoginBtn;
    FirebaseAuth fAuth;
    String name_login, code_login, status_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_login);

        textView = (TextView) findViewById(R.id.signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openregister();
            }
        });

        mEmail = findViewById(R.id.loginemail);
        mPassword = findViewById(R.id.loginpassword);
        mLoginBtn = findViewById(R.id.signinbt);
        fAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
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

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            // check whether the user has group or not
                            check_group_login();
                        }
                        else{
                            Toast.makeText(login.this, "Invalid Credentials " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void openregister(){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish();
    }
    public void opengroup(){
        Intent intent = new Intent(this, Group.class);
        startActivity(intent);
        finish();
    }
    public void check_group_login() {
        String user_id = GetUserid();
        Log.i("status", "check_group: user id of the user is "+user_id);
        if (user_id != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {
                        Map map = (Map) snapshot.getValue();

                        if (snapshot.getKey().matches("Name")){
                            assert map != null;
                            try {
                                name_login = Objects.requireNonNull(map.get("Username")).toString();
                                Log.i("status", "check_group: username is "+name_login);


                            } catch (Exception e) {
                                name_login= null;
                            }




                        }
                        else if (snapshot.getKey().matches("Group_Status")) {
                            assert map != null;
                            try {
                                code_login = Objects.requireNonNull(map.get("group_code")).toString();
                                status_login = Objects.requireNonNull(map.get("status")).toString();
                                Log.i("status", "check_group: code is "+code_login);
                                Log.i("status", "check_group: status is "+status_login);

                            } catch (Exception e) {
                                code_login = null;
                                status_login = null;
                            }
                        }
                        group_to_open(status_login,code_login,name_login);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else{
            openregister();
        }

    }
    public void group_to_open(String string, String code_string, String name){
        if(name != null) {
            if (code_string != null) {
                if (string != null) {
                    if (string.equals("true")) {
                        openfinalgroup();
                        finish();
                        Log.i("status", "page open is final group");

                    }
                }
            }
            else {
                openregister();
                finish();
                Log.i("status", "page open is open register ");
            }
        }
        finish();
    }

    public void openfinalgroup() {
        Intent intent = new Intent(this, final_group_page.class);
        startActivity(intent);
        finish();
    }



}