package com.example.expense_tracker;

import static com.example.expense_tracker.create_group.GetUserid;

import static java.util.Objects.*;

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
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class Group extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    ImageButton create_group;
    ImageButton join_group;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_group);


    create_group = findViewById(R.id.creategroup);
    create_group.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            open_create_groups();
            finish();
        }
    });
    join_group = findViewById(R.id.joingroup);
    join_group.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            open_join_groups();
            finish();
        }
    });


////////////////////////////////////////////////////////////////
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

    public void open_create_groups(){
        Intent intent = new Intent(this, create_group.class);
        startActivity(intent);
    }

    public void open_join_groups(){
        Intent intent = new Intent(this, join_group.class);
        startActivity(intent);
    }





}