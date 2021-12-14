package com.example.projectmanager.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.projectmanager.R;
import com.example.projectmanager.View.CustomerDetailActivity;
import com.example.projectmanager.View.ListViewActivity;
import com.example.projectmanager.View.MainActivity;
import com.example.projectmanager.model.Customer;

public class CustomerDetailActivityListener implements View.OnClickListener {

    private CustomerDetailActivity customerDetailActivity;

    static final int REQUEST_CALL_PHONE = 123;

    private Customer customer;

    public CustomerDetailActivityListener(CustomerDetailActivity customerDetailActivity) {
        this.customerDetailActivity = customerDetailActivity;
        customerDetailActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customerDetailActivity.getSupportActionBar().setTitle(R.string.detail_view_title_customer);

        customer = customerDetailActivity.getIntent().getParcelableExtra("customer");

        customerDetailActivity.txtvCustomerDetailName.setText(customer.getName());
        customerDetailActivity.txtvCustomerDetailAddress.setText(customer.getAddress());
        customerDetailActivity.txtvCustomerDetailContact.setText(customer.getContact());
        customerDetailActivity.txtvCustomerDetailPhone.setText(customer.getPhone());
        customerDetailActivity.txtvCustomerDetailEmail.setText(customer.getEmail());

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_CALL_PHONE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCall();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnShowProjects:
                Intent intent = new Intent(customerDetailActivity, ListViewActivity.class);
                intent.putExtra("mode", "project_customer");
                intent.putExtra("customer", (Parcelable) customer);
                customerDetailActivity.startActivity(intent);
                break;
            case R.id.imgbtnPhone:
                if(customerDetailActivity.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    startCall();
                else
                    ActivityCompat.requestPermissions(customerDetailActivity, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                break;
            case R.id.imgbtnEmail:
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{customerDetailActivity.txtvCustomerDetailEmail.getText().toString()});
                email.setData(Uri.parse("mailto:"));
                customerDetailActivity.startActivity(email);
                break;
        }
    }

    private void startCall(){
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + customerDetailActivity.txtvCustomerDetailPhone.getText().toString()));
        customerDetailActivity.startActivity(call);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.omDashboard:
                customerDetailActivity.startActivity(new Intent(customerDetailActivity, MainActivity.class));
                break;
            case android.R.id.home:
                customerDetailActivity.finish();
                break;
        }
        return true;
    }

}
