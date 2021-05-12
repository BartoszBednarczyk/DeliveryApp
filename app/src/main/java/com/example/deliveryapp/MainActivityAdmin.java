package com.example.deliveryapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type Main activity admin.
 * Activity for admins to change visibility of each product.
 */
public class MainActivityAdmin extends AppCompatActivity {


    private ArrayList<String> mProductNames = new ArrayList<>();
    private ArrayList<String> mProductDescs = new ArrayList<>();
    private ArrayList<String> mProductPrices = new ArrayList<>();
    private ArrayList<String> mProductImageUrls = new ArrayList<>();
    private ArrayList<String> mProductVisibility = new ArrayList<>();
    private long number = 0;

    private DatabaseReference mDataBaseRef;
    private ValueEventListener mDataBaseRefListener;
    /**
     * The Bottom navigation view.
     */
    BottomNavigationView bottomNavigationView;


    /**
     * The Relative layout.
     */
    RelativeLayout relativeLayout;
    /**
     * The Popup window.
     */
    PopupWindow popupWindow;
    /**
     * The Popup view.
     */
    View popupView;
    /**
     * The M current x.
     */
    int mCurrentX, /**
     * The M current y.
     */
    mCurrentY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openAddProductActivity();
            }
        });

        /**
         * Event listener displaying restaurant offer.
         */
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Menu");
        mDataBaseRefListener = mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProductImageUrls.clear();
                mProductPrices.clear();
                mProductNames.clear();
                mProductImageUrls.clear();
                mProductVisibility.clear();
                initRecyclerView();
                number = dataSnapshot.getChildrenCount();

                for(int i=1; i<=number; i++)
                {
                    if((dataSnapshot.child(i+"/Visibility").getValue(String.class)).equals("0"))
                    {
                        mProductDescs.add(dataSnapshot.child(i+"/Desc").getValue(String.class));
                        mProductNames.add("***"+dataSnapshot.child(+i+"/Name").getValue(String.class)+"***");
                        mProductImageUrls.add(dataSnapshot.child(+i+"/Url").getValue(String.class));
                        mProductPrices.add(dataSnapshot.child(+i+"/Price").getValue(String.class));
                        mProductVisibility.add(dataSnapshot.child(+i+"/Visibility").getValue(String.class));
                    }
                    else {
                        mProductDescs.add(dataSnapshot.child(i + "/Desc").getValue(String.class));
                        mProductNames.add(dataSnapshot.child(+i + "/Name").getValue(String.class));
                        mProductImageUrls.add(dataSnapshot.child(+i + "/Url").getValue(String.class));
                        mProductPrices.add(dataSnapshot.child(+i + "/Price").getValue(String.class));
                        mProductVisibility.add(dataSnapshot.child(+i+"/Visibility").getValue(String.class));
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

        /**
         * Navbar.
         */
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.nav_basket:
                        openOrdersAdminActivity();
                        break;
                    case R.id.nav_profile:
                        openStandardActivity();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Open add product activity.
     */
    public void openAddProductActivity(){
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

    private void initImageBitmaps(){
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_admin);
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

                if(mProductVisibility.get(position).equals("1")){
                    int pos = position + 1;
                    mDataBaseRef.child(pos+"/Visibility").setValue("0");
                }
                else {
                    int pos = position + 1;
                    mDataBaseRef.child(pos+"/Visibility").setValue("1");
                }

            }
        });
    }

    /**
     * Open orders admin activity.
     */
    public void openOrdersAdminActivity(){
        Intent intent = new Intent(this, OrdersActivityAdmin.class);
        finish();
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
