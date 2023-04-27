package com.example.expense_tracker;

import static com.example.expense_tracker.create_group.GetUserid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class final_group_page extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, input_dialog.ExampleDailogListener {
    String group_code;
    String user_id;
    String admin;
    ImageButton split_btn;
    ImageButton input_btn;
    ListView input_list;
    ArrayList<input_details> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_group_page);
        check_group_code();
        split_btn = findViewById(R.id.split_btn);
        input_btn = findViewById(R.id.input_btn);
        /*
        ArrayAdapter arrayAdapter = new ArrayAdapter<input_details>(final_group_page.this, android.R.layout.simple_list_item_1, (List<input_details>) input_list);
        input_list = findViewById(R.id.list_of_inputs);
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups").child();

        */

        input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    open_input_dailog();
            }
        });



    }

    private void open_input_dailog() {
            input_dialog in = new input_dialog();
            in.show(getSupportFragmentManager(), "input dialog");
    }

     public static class input_details{
        String input_given_by;
        String name;

         public String getInput_given_by() {
             return input_given_by;
         }
         public void setInput_given_by(String input_given_by) {
             this.input_given_by = input_given_by;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getAmount() {
             return amount;
         }

         public void setAmount(int amount) {
             this.amount = amount;
         }

         int amount;
     }

    // updating the input in the firebase;
    @Override
    public void updatetext(String name, int amount) {
        String userid = GetUserid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups").child(group_code).child("Inputs received");
        input_details input1 = new input_details();
        input1.setInput_given_by(userid);
        input1.setName(name);
        input1.setAmount(amount);
        ref.push().setValue(input1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(final_group_page.this, "Input has been updated", Toast.LENGTH_SHORT).show();
                        Log.i("status", "input has been updated to the firebase name "+ name+" amount "+ amount);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("status", "Could not update ");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();

        @SuppressLint("RestrictedApi") MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this,(MenuBuilder) popup.getMenu(),v);
        menuPopupHelper.setForceShowIcon(true);
        menuPopupHelper.show();
    }

    @SuppressLint({"NonConstantResourceId", "ShowToast"})
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                Toast.makeText(this, "Info section is still under process", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                openlogin();
                return true;
            default:
                return false;
        }
    }

    public void openlogin(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
    public void check_admin(){

        DatabaseReference ref_admin = FirebaseDatabase.getInstance().getReference("Groups").child(group_code).child("Member_Info").child(user_id);
        ref_admin.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Log.i("status", "Entering if");

                    Map map = (Map) snapshot.getValue();
                    if (Objects.requireNonNull(snapshot.getKey()).matches("Admin_status")){
                        Log.i("status", "Entering if");
                        assert map != null;
                        try {
                            admin = Objects.requireNonNull(map.get("Admin")).toString();
                            Log.i("status", "group admin "+group_code + " "+admin);

                        }
                        catch (Exception e){
                            admin = null;
                            Log.i("status", "group admin in catch  "+group_code + " "+admin);

                        }
                        finally {
                            split_btn_visibility();
                        }


                    }
                    else{
                        Log.i("status", "the group admin "+group_code);
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

    public void check_group_code(){
        user_id = GetUserid();
        assert user_id != null;
        DatabaseReference ref_user = FirebaseDatabase.getInstance().getReference("Users").child(user_id);
        ref_user.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    Map map = (Map) snapshot.getValue();

                    if(snapshot.getKey().matches("Group_Status")){
                        assert map != null;

                        try {
                            group_code = Objects.requireNonNull(map.get("group_code")).toString();
                            Log.i("status", "check_group: group code retrieved ");

                        }
                        catch (Exception e){
                                group_code = null;
                        }
                        finally {
                                check_admin();
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

    public void split_btn_visibility(){
        if (admin != null){
            split_btn.setVisibility(View.VISIBLE);
        }
        else {
            split_btn.setVisibility(View.INVISIBLE);
        }
    }
}