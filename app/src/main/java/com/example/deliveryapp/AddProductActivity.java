package com.example.deliveryapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * The type Add product activity.
 * Aktywność do dodawania produktów do bazy danych.
 */
public class AddProductActivity extends AppCompatActivity {

    /**
     * The Input product name.
     */
    EditText inputName;
    /**
     * The Input product description.
     */
    EditText inputDesc;
    /**
     * The Input product image url.
     */
    EditText inputImage;
    /**
     * The Input product price.
     */
    EditText inputPrice;
    /**
     * The Add product button.
     */
    Button addButton;

    /**
     * The Database reference.
     */
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Menu");
    private ValueEventListener drefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_product);


        inputName = (EditText) findViewById(R.id.inputName);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        inputImage = (EditText) findViewById(R.id.inputImage);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        addButton = (Button) findViewById(R.id.btnAdd);

        /**
         * Event Listener for checking changes in database.
         */
        drefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                int newID = 0;
                for (int i = 1; i<= count; i++){
                    if (dataSnapshot.child(i+"").exists()){
                        newID = i+1;
                    }
                    else
                    {
                        count++;
                    }
                }
                String Name, Desc, Image, Price;
                Name = inputName.getText().toString();
                Desc = inputDesc.getText().toString();
                Image = inputImage.getText().toString();
                Price = inputPrice.getText().toString();

                /**
                 * Setting up product in database using values of inputs.
                 */
                dref.child(newID+"/Desc").setValue(Desc);
                dref.child(newID+"/Name").setValue(Name);
                dref.child(newID+"/Price").setValue(Price);
                dref.child(newID+"/Url").setValue(Image);
                dref.child(newID+"/Visilibity").setValue("0");

                Toast.makeText(AddProductActivity.this, "Dodano "+ Name, Toast.LENGTH_SHORT).show();

                inputName.setText("");
                inputDesc.setText("");
                inputImage.setText("");
                inputPrice.setText("");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        /**
         * Commit changes in database after clicking button.
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dref.addListenerForSingleValueEvent(drefListener);
            }
        });
    }

}
