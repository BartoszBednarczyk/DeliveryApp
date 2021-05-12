package com.example.deliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The type Delivery address activity.
 * Activity for chaning users delivery address.
 */
public class DeliveryAddressActivity extends AppCompatActivity {

    /**
     * The Input name.
     */
    EditText inputName;
    /**
     * The Input last name.
     */
    EditText inputLastName;
    /**
     * The Input address.
     */
    EditText inputAddress;
    /**
     * The Input city.
     */
    EditText inputCity;
    /**
     * The Input zip.
     */
    EditText inputZip;
    /**
     * The Save delivery address btn.
     */
    Button saveDeliveryAddressBtn;

    private ValueEventListener databaseListener;
    private DatabaseReference mDataBaseRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        inputName = (EditText)findViewById(R.id.editTextName);
        inputLastName = (EditText)findViewById(R.id.editTextLastName);
        inputAddress = (EditText)findViewById(R.id.editTextAddress);
        inputCity = (EditText)findViewById(R.id.editTextCity);
        inputZip = (EditText)findViewById(R.id.editTextZip);
        saveDeliveryAddressBtn = (Button)findViewById(R.id.saveDeliveryAddressButton);

        mAuth = FirebaseAuth.getInstance();

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("Users/"
                +FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Delivery");

        databaseListener = mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name, lastName, address, city, zip;
                name = String.valueOf(dataSnapshot.child("Name").getValue(String.class));
                lastName = String.valueOf(dataSnapshot.child("LastName").getValue(String.class));
                address = String.valueOf(dataSnapshot.child("Address").getValue(String.class));
                city = String.valueOf(dataSnapshot.child("City").getValue(String.class));
                zip = String.valueOf(dataSnapshot.child("Zip").getValue(String.class));
                if(name.equals("") || name.equals("null")){
                    inputName.setText("Imie");
                }else{
                    inputName.setText(name);
                }
                if(lastName.equals("") || lastName.equals("null")){
                    inputLastName.setText("Nazwisko");
                }else{
                    inputLastName.setText(lastName);
                }
                if(address.equals("") || address.equals("null")){
                    inputAddress.setText("Adres");
                }else{
                    inputAddress.setText(address);
                }
                if(city.equals("") || city.equals("null")){
                    inputCity.setText("Miasto");
                }else{
                    inputCity.setText(city);
                }
                if(zip.equals("") || zip.equals("null")){
                    inputZip.setText("Kod pocztowy");
                }else{
                    inputZip.setText(zip);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        saveDeliveryAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, lastName, address, city, zip;
                name = String.valueOf(inputName.getText());
                lastName = String.valueOf(inputLastName.getText());
                address = String.valueOf(inputAddress.getText());
                city = String.valueOf(inputCity.getText());
                zip = String.valueOf(inputZip.getText());

                if(name.equals("Imie")){
                    mDataBaseRef.child("Name").setValue("");
                }else{
                    mDataBaseRef.child("Name").setValue(name);
                }
                if(lastName.equals("Nazwisko")){
                    mDataBaseRef.child("LastName").setValue("");
                }else{
                    mDataBaseRef.child("LastName").setValue(lastName);
                }
                if(address.equals("Adres")){
                    mDataBaseRef.child("Address").setValue("");
                }else{
                    mDataBaseRef.child("Address").setValue(address);
                }
                if(city.equals("Miasto")){
                    mDataBaseRef.child("City").setValue("");
                }else{
                    mDataBaseRef.child("City").setValue(city);
                }
                if(zip.equals("Kod pocztowy")){
                    mDataBaseRef.child("Zip").setValue("");
                }else{
                    mDataBaseRef.child("Zip").setValue(zip);
                }

                Toast.makeText(DeliveryAddressActivity.this, "Zaktualizowane dane", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseRef.removeEventListener(databaseListener);
    }
}
