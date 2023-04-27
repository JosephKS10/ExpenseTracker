package com.example.expense_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class create_group extends AppCompatActivity {
    private TextView code;
    public String group_code;
    private DatabaseReference ref_group_name;
    private DatabaseReference ref_no_of_members;
    private DatabaseReference ref_member_info;
    private DatabaseReference ref_member_transaction_info;
    private DatabaseReference ref_user_group_status;



    EditText mGroup_Name, mNo_Of_Members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_create_group);

        mGroup_Name = findViewById(R.id.group_name);
        mNo_Of_Members = findViewById(R.id.group_member_no);

        // random code generated to for the group code
        code = findViewById(R.id.randomcode);
        group_code = generate();

        //////////////////////////////////////////////


        // info button regarding the group code
        ImageButton code_info_button = findViewById(R.id.code_info_btn);
        code_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_info_dailogue();
            }
        });
        ///////////////////////////////////////////////


        // firebase model for making groups

       ImageButton create_btn = findViewById(R.id.creategroup_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = GetUserid();
                String GroupName = mGroup_Name.getText().toString().trim();
                int group_members = Integer.parseInt(mNo_Of_Members.getText().toString());
                if(TextUtils.isEmpty(GroupName)){
                    mGroup_Name.setError("Group Name is required");
                    return;
                }
                if (group_members<=0) {
                    mNo_Of_Members.setError("No of members cannot be empty");
                    return;
                }
                if (group_members>50){
                    mNo_Of_Members.setError("No of members cannot be greater than 50");
                    return;
                }

                // now registering the group in the firebase realtime database

                HashMap<String, String> hashMap_name = new HashMap<>();
                HashMap<String, Integer> hashMap2 = new HashMap<>();
                HashMap<String, String> hashMap_status = new HashMap<>();


                group_status user1 = new group_status();
                user1.setStatus(true);
                user1.setGroup_code(group_code);

                hashMap_name.put("Group_Name", GroupName);
                hashMap2.put("No_Of_Members", group_members);
                hashMap_status.put("Admin", user_id);



                ref_group_name = FirebaseDatabase.getInstance().getReference().child("Groups").child(group_code).child("Group_name");
                ref_no_of_members = FirebaseDatabase.getInstance().getReference().child("Groups").child(group_code).child("No_of_Members");
                ref_member_info = FirebaseDatabase.getInstance().getReference().child("Groups").child(group_code).child("Member_Info").child(user_id).child("Admin_Status");
                ref_user_group_status = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Group_Status");
                ref_group_name.setValue(hashMap_name)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ref_no_of_members.setValue(hashMap2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                ref_member_info.setValue(hashMap_status)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                // updating the group status of the user
                                                                ref_user_group_status.setValue(user1);
                                                                Toast.makeText(create_group.this, "Group has been created", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(create_group.this, "Task failed", Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });



    }

    private void open_info_dailogue() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private String generate() {
        Randomstring randomstring = new Randomstring();
        String result = randomstring.generateAlphaNumeric(6);
        code.setText(result);
        return result;
    }

// updated function
    static public String GetUserid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid;
        if (user != null) {
             userid = user.getUid();
        }
        else {
            userid = null;
        }
        return userid;
    }

    public void openfinalgroup() {
        Intent intent = new Intent(this, final_group_page.class);
        startActivity(intent);
        finish();
    }




}