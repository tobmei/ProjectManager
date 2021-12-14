package com.example.projectmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectmanager.R;
import com.example.projectmanager.model.Customer;

import java.util.List;

public class CustomerArrayAdapter extends ArrayAdapter<Customer> {

    private LayoutInflater layoutInflater;
    private List<Customer> customerList;

    public CustomerArrayAdapter(@NonNull Context context, List<Customer> customerList) {
        super(context, R.layout.list_row_costumer, customerList);

        this.layoutInflater = LayoutInflater.from(context);
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView;

        if(convertView == null)
            rowView = layoutInflater.inflate(R.layout.list_row_costumer, parent, false);
        else
            rowView = convertView;

        Customer customer = customerList.get(position);

        TextView txtvCustomerName = (TextView) rowView.findViewById(R.id.txtvCustomerName);

        txtvCustomerName.setText(customer.getName());

        return rowView;
    }
}
