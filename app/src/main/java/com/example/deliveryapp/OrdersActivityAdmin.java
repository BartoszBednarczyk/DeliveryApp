package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * The type Orders activity admin.
 * Activity for admins to control each order.
 */
public class OrdersActivityAdmin extends AppCompatActivity {

    private ArrayList<String> mIDs = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mLastNames = new ArrayList<>();
    private ArrayList<String> mAddresses = new ArrayList<>();
    private ArrayList<String> mZips = new ArrayList<>();
    private ArrayList<String> mCities = new ArrayList<>();
    private ArrayList<String> mProducts = new ArrayList<>();
    private ArrayList<String> mTotals = new ArrayList<>();
    private ArrayList<String> mStates = new ArrayList<>();
    private String Products = "";

    /**
     * The Bottom navigation view.
     */
    BottomNavigationView bottomNavigationView;
    /**
     * The M database ref.
     */
    DatabaseReference mDatabaseRef;
    /**
     * The M database ref listener.
     */
    ValueEventListener mDatabaseRefListener;
    /**
     * The M database ref listener single value.
     */
    ValueEventListener mDatabaseRefListenerSingleValue;

    /**
     * The Current.
     */
    String current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_admin);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_basket);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        openMainActivity();
                        break;
                    case R.id.nav_basket:
                        //openOrdersAdminActivity();
                        break;
                    case R.id.nav_profile:
                        openStandardActivity();
                        break;
                }
                return true;
            }
        });

        mDatabaseRefListenerSingleValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String actual = String.valueOf(dataSnapshot.child(current+"/Status").getValue());

                if(actual.equals("Zamówiono")){
                    mDatabaseRef.child(current+"/Status").setValue("Przygotowywane");
                }else if(actual.equals("Przygotowywane")){
                    mDatabaseRef.child(current+"/Status").setValue("W drodze");
                }else if(actual.equals("W drodze")){
                    mDatabaseRef.child(current+"/Status").setValue("Dostarczono");
                }else if(actual.equals("Dostarczono")){
                    mDatabaseRef.child(current+"/Status").setValue("Zamówiono");
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders");
        mDatabaseRefListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearArrays();
                long number = dataSnapshot.getChildrenCount();
                for(int i=1; i<=number; i++)
                {
                    if (dataSnapshot.child(String.valueOf(i)).getValue()!=null){
                        mIDs.add(String.valueOf(i));
                    }
                    else{
                        number++;
                    }
                }

                for(int j=0; j<dataSnapshot.getChildrenCount();j++){
                    mNames.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("User").child("Name").getValue(String.class));
                    mLastNames.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("User").child("LastName").getValue(String.class));
                    mAddresses.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("User").child("Address").getValue(String.class));
                    mZips.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("User").child("Zip").getValue(String.class));
                    mCities.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("User").child("City").getValue(String.class));
                    mTotals.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("Total").getValue(String.class));
                    mStates.add(dataSnapshot.child(String.valueOf(mIDs.get(j))).child("Status").getValue(String.class));
                    Products = "";
                    for(int k=1; k<=dataSnapshot.child(String.valueOf(mIDs.get(j))).child("Products").getChildrenCount(); k++){
                        Products+=dataSnapshot.child(String.valueOf(mIDs.get(j))).child("Products").
                                child(String.valueOf(k)).child("Product").getValue(String.class);
                        if(k!=dataSnapshot.child(String.valueOf(mIDs.get(j))).child("Products").getChildrenCount()){
                            Products+=", ";
                        }

                    }
                    mProducts.add(Products);
                }

                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDatabaseRefListener);
    }

    private void initRecyclerView(){
        Collections.reverse(mIDs);
        Collections.reverse(mNames);
        Collections.reverse(mLastNames);
        Collections.reverse(mAddresses);
        Collections.reverse(mZips);
        Collections.reverse(mCities);
        Collections.reverse(mProducts);
        Collections.reverse(mTotals);
        Collections.reverse(mStates);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_userOrders);
        RecyclerViewAdapterUserOrders adapter = new RecyclerViewAdapterUserOrders(mIDs,mNames,
                mLastNames,mAddresses,mZips,mCities,mProducts,mTotals,mStates, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnBasketItemClickListener(new RecyclerViewAdapterUserOrders.OnBasketItemClickListener() {
            @Override
            public void onItemClick(int position) {
                current = String.valueOf(mIDs.get(position));
                mDatabaseRef.addListenerForSingleValueEvent(mDatabaseRefListenerSingleValue);
            }

            @Override
            public void onDeleteClick(int position) {
            }
        });


    }

    private void clearArrays(){
        mIDs.clear();
        mNames.clear();
        mLastNames.clear();
        mAddresses.clear();
        mZips.clear();
        mCities.clear();
        mProducts.clear();
        mTotals.clear();
        mStates.clear();
    }

    /**
     * Open main activity.
     */
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivityAdmin.class);
        startActivity(intent);
    }

    /**
     * Open standard activity.
     */
    public void openStandardActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
