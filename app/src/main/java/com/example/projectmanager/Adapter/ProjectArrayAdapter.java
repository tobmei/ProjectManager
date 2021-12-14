package com.example.projectmanager.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectmanager.DataSource;
import com.example.projectmanager.R;
import com.example.projectmanager.model.Customer;
import com.example.projectmanager.model.Project;

import java.util.List;

public class ProjectArrayAdapter extends ArrayAdapter<Project> {

    private LayoutInflater layoutInflater;
    private List<Project> projectList;
    private DataSource dataSource;

    public ProjectArrayAdapter(@NonNull Context context, List<Project> projectList, DataSource dataSource) {
        super(context, R.layout.list_row_project_detail, projectList);

        this.layoutInflater = LayoutInflater.from(context);
        this.projectList = projectList;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView;

        if(convertView == null)
            rowView = layoutInflater.inflate(R.layout.list_row_project_detail, parent, false);
        else
            rowView = convertView;

        Project project = projectList.get(position);
        Customer customer = dataSource.getCustomerByID(project.getCustomerID());

        TextView txtvProjectName = (TextView) rowView.findViewById(R.id.txtvProjectAllName);
        TextView txtvProjectCustomer = (TextView) rowView.findViewById(R.id.txtvProjectDetailAllCustomer);
        TextView txtvProjectAllCreateDate = (TextView) rowView.findViewById(R.id.txtvProjectAllCreateDate);

        txtvProjectName.setText(project.getName());
        txtvProjectCustomer.setText(customer.getName());
        txtvProjectAllCreateDate.setText(project.getCreateDate());

        if(project.isStatus())
            txtvProjectName.setPaintFlags(txtvProjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return rowView;
    }
}
