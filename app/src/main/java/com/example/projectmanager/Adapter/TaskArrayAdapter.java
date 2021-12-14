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
import com.example.projectmanager.Utility;
import com.example.projectmanager.model.Task;

import java.util.List;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private LayoutInflater layoutInflater;
    private List<Task> taskList;

    public TaskArrayAdapter(@NonNull Context context, List<Task> taskList) {
        super(context, R.layout.list_row_task, taskList);

        this.layoutInflater = LayoutInflater.from(context);
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView;

        if(convertView == null)
            rowView = layoutInflater.inflate(R.layout.list_row_task, parent, false);
        else
            rowView = convertView;

        Task task = taskList.get(position);

        TextView txtvDescription = (TextView) rowView.findViewById(R.id.txtvTaskListDescription);
        TextView txtvCreateDate = (TextView) rowView.findViewById(R.id.txtvTaskDetailCreateDate);
        TextView txtvTimeWorked = (TextView) rowView.findViewById(R.id.txtvTaskDetailTimeWorked);

        txtvDescription.setText(task.getDescription());
        txtvCreateDate.setText(task.getCreateDate());
        txtvTimeWorked.setText(Utility.getFormattedTime(task.getTime_worked()));

        return rowView;
    }
}
