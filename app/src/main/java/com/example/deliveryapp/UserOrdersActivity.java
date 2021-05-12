package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * The type User orders activity.
 * Activity where user can check orders states.
 */
public class UserOrdersActivity extends AppCompatActivity {

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
     * The M database ref.
     */
    DatabaseReference mDatabaseRef;
    /**
     * The M database ref listener.
     */
    ValueEventListener mDatabaseRefListener;
    /**
     * The M database ref 2.
     */
    DatabaseReference mDatabaseRef2;
    /**
     * The M database ref listener 2.
     */
    ValueEventListener mDatabaseRefListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders");
        mDatabaseRefListener2 = mDatabaseRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = dataSnapshot.getChildrenCount();
                mIDs.clear();
                for(int i=1; i<=number; i++){
                    mIDs.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders");
        mDatabaseRefListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearArrays();
                long number = mIDs.size();

                for (int j=0; j<number; j++){
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_userOrders);
        RecyclerViewAdapterUserOrders adapter = new RecyclerViewAdapterUserOrders(mIDs,mNames,
                mLastNames,mAddresses,mZips,mCities,mProducts,mTotals,mStates, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void clearArrays(){
        //mIDs.clear();
        mNames.clear();
        mLastNames.clear();
        mAddresses.clear();
        mZips.clear();
        mCities.clear();
        mProducts.clear();
        mTotals.clear();
        mStates.clear();
    }
}
