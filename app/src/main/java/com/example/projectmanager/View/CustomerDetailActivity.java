package com.example.projectmanager.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectmanager.Controller.CustomerDetailActivityListener;
import com.example.projectmanager.R;

public class CustomerDetailActivity extends AppCompatActivity {

    public TextView txtvCustomerDetailName, txtvCustomerDetailAddress, txtvCustomerDetailContact,
            txtvCustomerDetailPhone, txtvCustomerDetailEmail;
    ImageView imgvIconName, imgvIconAddress, imgvIconContact;
    ImageButton btnShowProjects;
    ImageButton imgbtnPhone, imgbtnEmail;

    CustomerDetailActivityListener customerDetailActivityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);


        txtvCustomerDetailName = findViewById(R.id.txtvCustomerDetailName);
        txtvCustomerDetailAddress = findViewById(R.id.txtvCustomerDetailAddress);
        txtvCustomerDetailContact = findViewById(R.id.txtvCustomerDetailContact);
        txtvCustomerDetailPhone = findViewById(R.id.txtvCustomerDetailPhone);
        txtvCustomerDetailEmail = findViewById(R.id.txtvCustomerDetailEmail);

        imgvIconName = findViewById(R.id.imgvIconName);
        imgvIconAddress = findViewById(R.id.imgvIconAddress);
        imgvIconContact = findViewById(R.id.imgcIconContact);
        imgbtnPhone = findViewById(R.id.imgbtnPhone);
        imgbtnEmail = findViewById(R.id.imgbtnEmail);

        btnShowProjects = findViewById(R.id.btnShowProjects);

        customerDetailActivityListener = new CustomerDetailActivityListener(this);

        btnShowProjects.setOnClickListener(customerDetailActivityListener);
        imgbtnPhone.setOnClickListener(customerDetailActivityListener);
        imgbtnEmail.setOnClickListener(customerDetailActivityListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        customerDetailActivityListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu_detail_project, menu);
        MenuItem menuItem = menu.findItem(R.id.omCheck);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return customerDetailActivityListener.onOptionsItemSelected(item);
    }
}