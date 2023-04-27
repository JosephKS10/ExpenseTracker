package com.example.expense_tracker;

import static com.example.expense_tracker.create_group.GetUserid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;
import java.util.Objects;


public class opening extends AppCompatActivity {
    String code;
    String status;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_opening);
        StrictMode.enableDefaults();


        ImageButton getstarted = (ImageButton) findViewById(R.id.startbt);
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_group();
            }

        });


    }

   public void check_group() {
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
                                name = Objects.requireNonNull(map.get("Username")).toString();
                                Log.i("status", "check_group: username is "+name);
                            } catch (Exception e) {
                                name= null;
                            }




                        }
                        else if (snapshot.getKey().matches("Group_Status")) {
                            assert map != null;
                            try {
                                code = Objects.requireNonNull(map.get("group_code")).toString();
                                status = Objects.requireNonNull(map.get("status")).toString();
                                Log.i("status", "check_group: code is "+code);
                                Log.i("status", "check_group: status is "+status);

                            } catch (Exception e) {
                                code = null;
                                status = null;
                            }
                        }
                        group_to_open(status,code,name);
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



    public void openfinalgroup() {
        Intent intent = new Intent(this, final_group_page.class);
        startActivity(intent);
        finish();
    }

    public void openregister(){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish();
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



}