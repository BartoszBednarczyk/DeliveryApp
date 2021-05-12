package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * The type Main activity.
 * Main activity of app where user can browse restaurant offer and add product to basket.
 */
public class MainActivity extends AppCompatActivity {

    //Offer vars
    private ArrayList<String> mProductNames = new ArrayList<>();
    private ArrayList<String> mProductDescs = new ArrayList<>();
    private ArrayList<String> mProductPrices = new ArrayList<>();
    private ArrayList<String> mProductImageUrls = new ArrayList<>();

    private ArrayList<String> dProductNames = new ArrayList<>();
    private ArrayList<String> dProductDescs = new ArrayList<>();
    private ArrayList<String> dProductPrices = new ArrayList<>();
    private ArrayList<String> dProductImageUrls = new ArrayList<>();

    private long backPressedTime;
    private Toast backToast;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private DatabaseReference mDataBaseRef;
    private DatabaseReference mDataBaseRefBasket;

    private ValueEventListener mDataBaseRefListener;
    private ValueEventListener mDataBaseRefBasketListener;


    private long number = 0;
    private long basketNumber = 0;
    private long lastNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        mAuth = FirebaseAuth.getInstance();
       // mDatabase = FirebaseDatabase.getInstance().getReference("Menu");

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Menu");
        mDataBaseRefBasket = FirebaseDatabase.getInstance().getReference("Users");

        /**
         * Event listener displaying offer in RecyclerView.
         */
        mDataBaseRefListener = mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductImageUrls.clear();
                mProductPrices.clear();
                mProductNames.clear();
                mProductImageUrls.clear();
                initRecyclerView();
                number = dataSnapshot.getChildrenCount();

                for(int i=1; i<=number; i++)
                {
                    if((dataSnapshot.child(i+"/Visibility").getValue(String.class)).equals("1"))
                    {
                        mProductDescs.add(dataSnapshot.child(i+"/Desc").getValue(String.class));
                        mProductNames.add(dataSnapshot.child(+i+"/Name").getValue(String.class));
                        mProductImageUrls.add(dataSnapshot.child(+i+"/Url").getValue(String.class));
                        mProductPrices.add(dataSnapshot.child(+i+"/Price").getValue(String.class));
                    }else{

                    }


                }
                initImageBitmaps();

                Log.d("Listener1", "Wykonal sie");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the message from databaseError
            }
        });

        mDataBaseRefBasketListener = mDataBaseRefBasket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                basketNumber = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Basket").getChildrenCount();

                for(int i=1; i<=basketNumber; i++)
                {
                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Basket/"+i+"/Name").getValue()!=null){
                        lastNumber = i;
                    }
                    else{
                        basketNumber++;
                    }
                }
                Log.d("Listener2", "Wykonal sie");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the message from databaseError
            }
        });


        /**
         * Navbar.
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_basket:
                        openBasketActivity();
                        break;
                    case R.id.nav_profile:
                        openProfileActivity();
                        break;
                }
                return true;
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseRef.removeEventListener(mDataBaseRefListener);
        mDataBaseRef.removeEventListener(mDataBaseRefBasketListener);
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void initImageBitmaps(){
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mProductNames, mProductDescs,
                mProductPrices, mProductImageUrls, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onButtonClick(int position) {
                Toast.makeText(MainActivity.this, "Dodano do koszyka", Toast.LENGTH_SHORT).show();

                DatabaseReference basketPath = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Basket")
                        .child(String.valueOf(lastNumber+1));
                basketPath.child("Name").setValue(mProductNames.get(position));
                basketPath.child("Price").setValue(mProductPrices.get(position));

//


            }
        });
    }


    /**
     * Open basket activity.
     */
    public void openBasketActivity(){
        Intent intent = new Intent(this, BasketActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Open profile activity.
     */
    public void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
