package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The type Basket activity.
 * Activity showing the content of users basket
 */
public class BasketActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPrices = new ArrayList<>();

    private FirebaseAuth mAuth;

    private long backPressedTime;
    private Toast backToast;

    private ArrayList<Integer> ids = new ArrayList<>();
    private TextView totalCostTextView;
    private ArrayList<String> totalCost = new ArrayList<>();

    double totalCost2=0;


    private DatabaseReference mDataBaseRef;
    private ValueEventListener mDataBaseRefListener;

    private DatabaseReference mDataBaseRefOrders;
    private ValueEventListener mDataBaseRefListenerOrders;

    private String name, lastName, address, city, zip;

    private int orderID;
    private long userOrdersNumber;


    Button getOrder;

    private long number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        getOrder = (Button)findViewById(R.id.getOrder);
        /**
         * Commiting order after button click.
         */
        getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrder();
            }
        });



        totalCostTextView = findViewById(R.id.total_cost);

        mAuth = FirebaseAuth.getInstance();

        /**
         * Eventlistener for checking changes in database.
         */
        mDataBaseRefOrders = FirebaseDatabase.getInstance().getReference("Orders");
        mDataBaseRefListenerOrders = mDataBaseRefOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long orderNumber = dataSnapshot.getChildrenCount();

                /**
                 * Checking new order ID.
                 */
                for(int i=1; i<=orderNumber; i++)
                {
                    if (dataSnapshot.child(String.valueOf(i)).getValue()!=null){
                        orderID = i;
                    }
                    else{
                        orderNumber++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDataBaseRef = FirebaseDatabase.getInstance().getReference(
                "Users/" +FirebaseAuth.getInstance().getCurrentUser().getUid());


        /**
         * Eventlistener for initting RecyclerView
         */
        mDataBaseRefListener = mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userOrdersNumber = dataSnapshot.child("Orders").getChildrenCount();
                userOrdersNumber++;
                mNames.clear();
                mPrices.clear();
                ids.clear();
                //totalCost.clear();
                totalCost2 = 0;
                //totalCost.add("1");
                initRecyclerView();
                number = dataSnapshot.child("Basket").getChildrenCount();
                for(int i=1; i<=number; i++)
                {
                    if(dataSnapshot.child("Basket/"+i+"/Name").getValue(String.class)!=null)
                    {
                        mNames.add(dataSnapshot.child("Basket/"+i+"/Name").getValue(String.class));
                        String cost = dataSnapshot.child("Basket/"+i+"/Price").getValue(String.class);
                        mPrices.add(cost);
                        totalCost.add(cost);
                        totalCost2 += Double.parseDouble(cost);
                        totalCost2 = Math.round(totalCost2 * 100.0)/100.0;
                        ids.add(i);
                    }
                    else {
                        number++;
                    }
                }
                initBasketList();
                totalCostTextView.setText(String.valueOf(totalCost2));
                Log.d("Listener3", "Wykonal sie");


                name = String.valueOf(dataSnapshot.child("Delivery").child("Name").getValue(String.class));
                lastName = String.valueOf(dataSnapshot.child("Delivery").child("LastName").getValue(String.class));
                address = String.valueOf(dataSnapshot.child("Delivery").child("Address").getValue(String.class));
                city = String.valueOf(dataSnapshot.child("Delivery").child("City").getValue(String.class));
                zip = String.valueOf(dataSnapshot.child("Delivery").child("Zip").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log the message from databaseError
            }
        });

        /**
         * Navbar.
         */

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_basket);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        bottomNavigationView.setSelectedItemId(R.id.nav_basket);
                        finish();
                        openMainActivity();
                        break;
                    case R.id.nav_basket:

                        break;
                    case R.id.nav_profile:
                        bottomNavigationView.setSelectedItemId(R.id.nav_basket);
                        finish();
                        openProfileActivity();
                        break;
                }
                return true;
            }
        });

        initBasketList();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseRef.removeEventListener(mDataBaseRefListener);
    }


    private void initBasketList(){
        for(int i=0; i<number; i++){

            //totalCost2+= Float.parseFloat(totalCost.get(i));
            if(i==(number-1)){
                //totalCostTextView.setText(String.valueOf(totalCost2));
            }
        }
        initRecyclerView();

    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view_basket);
        RecyclerViewAdapterBasket adapter = new RecyclerViewAdapterBasket(mNames, mPrices, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnBasketItemClickListener(new RecyclerViewAdapterBasket.OnBasketItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

                Toast.makeText(BasketActivity.this, "Usunięto", Toast.LENGTH_SHORT).show();
                mDataBaseRef.child("Basket").child(String.valueOf(ids.get(position))).setValue(null);
            }
        });


    }

    /**
     * Open main activity.
     */
    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
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

    /**
     * Open profile activity.
     */
    public void openProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }


    /**
     * Get order.
     */
    public void getOrder(){
            if(name.equals("null") || name.equals("") || lastName.equals("null") || lastName.equals("")
                    || address.equals("null") || address.equals("") || zip.equals("null") ||
                    zip.equals("") || city.equals("null") || city.equals("")){
                Toast.makeText(this, "Wprowadż adres dostawy w profilu", Toast.LENGTH_SHORT).show();
            }
            else{
                if(mNames.isEmpty()){
                    Toast.makeText(this, "Brak produktów w koszyku", Toast.LENGTH_SHORT).show();

                }else{
                    DatabaseReference userOrderPath = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders")
                            .child(String.valueOf(orderID+1));
                    DatabaseReference allOrdersPath = FirebaseDatabase.getInstance().getReference("Orders/"+(orderID+1));

                    DatabaseReference userOrderPath2 = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders");

                    userOrderPath2.child(String.valueOf(userOrdersNumber)).setValue(String.valueOf(orderID+1));

                    allOrdersPath.child("User").child("ID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    allOrdersPath.child("User").child("Name").setValue(name);
                    allOrdersPath.child("User").child("LastName").setValue(lastName);
                    allOrdersPath.child("User").child("Address").setValue(address);
                    allOrdersPath.child("User").child("City").setValue(city);
                    allOrdersPath.child("User").child("Zip").setValue(zip);

//                    userOrderPath.child("User").child("Name").setValue(name);
//                    userOrderPath.child("User").child("LastName").setValue(lastName);
//                    userOrderPath.child("User").child("Address").setValue(address);
//                    userOrderPath.child("User").child("City").setValue(city);
//                    userOrderPath.child("User").child("Zip").setValue(zip);

                    for(int i=1; i<=mNames.size(); i++){
                        allOrdersPath.child("Products").child(String.valueOf(i)).child("Product").setValue(String.valueOf(mNames.get((i-1))));
                        allOrdersPath.child("Products").child(String.valueOf(i)).child("Price").setValue(String.valueOf(mPrices.get((i-1))));
//                        userOrderPath.child("Products").child(String.valueOf(i)).child("Product").setValue(String.valueOf(mNames.get((i-1))));
//                        userOrderPath.child("Products").child(String.valueOf(i)).child("Price").setValue(String.valueOf(mPrices.get((i-1))));
                    }
                    allOrdersPath.child("Total").setValue(String.valueOf(totalCost2));
                    //userOrderPath.child("Total").setValue(String.valueOf(totalCost2));

                    allOrdersPath.child("Status").setValue("Zamówiono");
                    //userOrderPath.child("Status").setValue("Zamówiono");

                    FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Basket").setValue(null);
                    Toast.makeText(this, "Złożono zamówienie", Toast.LENGTH_SHORT).show();
                }
            }



    }
}


