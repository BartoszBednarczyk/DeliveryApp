package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Profile activity.
 * Activity with users details.
 */
public class ProfileActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    /**
     * The Delivery address button.
     */
    Button deliveryAddressButton;
    /**
     * The User orders button.
     */
    Button userOrdersButton;
    /**
     * The Go admin button.
     */
    Button goAdminButton;
    /**
     * The Logout button.
     */
    Button logoutButton;

    private DatabaseReference mDataBaseRef;
    private ValueEventListener mDataBaseRefListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        goAdminButton = (Button) findViewById(R.id.goAdminButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                openLoginScreen();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Users");
        if(mAuth.getCurrentUser().getUid().equals("Q5Ldr9cd19e0DjYMtkM9ZxKxYy42")){
            goAdminButton.setVisibility(View.VISIBLE);
        }

        /**
         * Navbar.
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        openMainActivity();
                        break;
                    case R.id.nav_basket:
                        openBasketActivity();
                        break;
                    case R.id.nav_profile:
                        break;
                }
                return true;
            }
        });

        userOrdersButton = (Button) findViewById(R.id.ordersButton);
        userOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserOrdersActivity();
            }
        });
        deliveryAddressButton = (Button) findViewById(R.id.deliveryButton);
        deliveryAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeliveryAddressActivity();
            }
        });

        goAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdmin();
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
     * Open main activity.
     */
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Open delivery address activity.
     */
    public void openDeliveryAddressActivity(){
        Intent intent = new Intent(this, DeliveryAddressActivity.class);
        startActivity(intent);
    }

    /**
     * Open user orders activity.
     */
    public void openUserOrdersActivity(){
        Intent intent = new Intent(this, UserOrdersActivity.class);
        startActivity(intent);
    }

    /**
     * Open login screen.
     */
    public void openLoginScreen(){
        Intent intent = new Intent(this, LandingScreen.class);
        finish();
        startActivity(intent);
    }

    /**
     * Open admin.
     */
    public void openAdmin(){
        Intent intent = new Intent(this, MainActivityAdmin.class);
        finish();
        startActivity(intent);
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
}
