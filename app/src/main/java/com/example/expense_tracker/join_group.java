package com.example.expense_tracker;

import static com.example.expense_tracker.create_group.GetUserid;

import static java.lang.Integer.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class join_group extends AppCompatActivity {
    EditText mUnique_Code;
    String member_count_string;
    int member_count;
    int member_count_present;
    String UniqueCode;
    String userid;
    int condition;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_join_group);

        mUnique_Code = findViewById(R.id.unique_code);

        ImageButton join_btn = findViewById(R.id.joingroup_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userid = GetUserid();
                UniqueCode = mUnique_Code.getText().toString().trim();
                if (TextUtils.isEmpty(UniqueCode)){
                    mUnique_Code.setError("This field cannot be empty");
                    return;
                }

                // finding the group in the database
                if (userid!= null){
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");
                    ref1.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if (snapshot.exists()){
                                Map map1 = (Map) snapshot.getValue();

                                if (snapshot.getKey().matches(UniqueCode)) {
                                    assert map1 != null;
                                    condition = 1;
                                    Log.i("status", "group found " + UniqueCode +" condition "+ condition);
                                    if (condition == 1){

                                        Log.i("status", "Entered the if condition");
                                        try {
                                            getMember_count();
                                            Log.i("status", "loaded getmember count");
                                        }
                                        catch (Exception e){
                                            Log.i("status", "Error couldn't load get member count");
                                        }
                                    }
                                }
                                else{
                                    condition = 0;
                                    Toast.makeText(join_group.this, "Invalid group code", Toast.LENGTH_SHORT).show();
                                }

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



            }
        });

    }
    public void getMember_count(){
        // taking out the total number of members that can be in the group

        DatabaseReference ref_member_number = FirebaseDatabase.getInstance().getReference("Groups").child(UniqueCode);
        ref_member_number.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Map map2 = (Map) snapshot.getValue();
                    if (snapshot.getKey().matches("No_of_Members")) {
                        if(map2 != null) {
                            try {
                                member_count_string = Objects.requireNonNull(map2.get("No_Of_Members")).toString();
                                Log.i("status", "The number of members in the group are " + member_count_string);
                            } catch (Exception e) {
                                member_count_string = null;
                            }
                            finally {
                                getMember_count_present();
                                Log.i("status", "loaded get member count present ");
                            }
                        }
                    }
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

    public void getMember_count_present(){
        DatabaseReference ref_member_count = FirebaseDatabase.getInstance().getReference("Groups").child(UniqueCode).child("Member_Info");
        // taking out the number of members present in the group
        ref_member_count.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    try {
                        member_count_present = (int) snapshot.getChildrenCount();
                        Log.i("status", "The number of users present in the group are " + member_count_present);
                    }
                    catch (Exception e){
                        member_count_present = 0;
                    }
                    finally {
                            try {
                                member_count = Integer.parseInt(member_count_string);
                                Log.i("status", "member count is "+member_count);
                                Log.i("status", "member count present is "+member_count_present);
                            }
                            catch (Exception e){
                                Log.i("status", "Error if condition");
                                Log.i("status", "member count is "+member_count);
                                Log.i("status", "member count present is "+member_count_present);
                            }
                            updating(member_count_present, member_count);
                        }
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

    public void updating(int count, int total_count) {

        DatabaseReference Ref_user = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Group_Status");
        DatabaseReference ref_member_info = FirebaseDatabase.getInstance().getReference("Groups").child(UniqueCode).child("Member_Info").child(userid).child("Admin_status");
        HashMap<String, String> Member_status = new HashMap<>();
        Member_status.put("Member", userid);
        group_status user2 = new group_status();
        user2.setStatus(true);
        user2.setGroup_code(UniqueCode);
        if (count != 0 && total_count != 0){
            if (count < total_count) {
                Log.i("status", "member count " + member_count + " member count present " + member_count_present);

                ref_member_info.setValue(Member_status)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Ref_user.setValue(user2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(join_group.this, "You have been added to the group", Toast.LENGTH_SHORT).show();
                                                finish();


                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } else {
                Toast.makeText(join_group.this, "The group you are trying to join is full", Toast.LENGTH_SHORT).show();
                Log.i("status", "The group you are trying to join is full " + UniqueCode);
            }
    }
        else{
            Log.i("status", "could not update the user");
        }
    }
}