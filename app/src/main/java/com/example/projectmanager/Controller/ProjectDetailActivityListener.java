package com.example.projectmanager.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectmanager.DataSource;
import com.example.projectmanager.View.ProjectDetailActivity;
import com.example.projectmanager.R;
import com.example.projectmanager.View.TaskActivity;
import com.example.projectmanager.Utility;
import com.example.projectmanager.View.ListViewActivity;
import com.example.projectmanager.View.MainActivity;
import com.example.projectmanager.model.Project;

public class ProjectDetailActivityListener implements View.OnClickListener {

    ProjectDetailActivity projectDetailActivity;
    DataSource dataSource;

    private Project project;

    public ProjectDetailActivityListener(ProjectDetailActivity projectDetailActivity) {
        this.projectDetailActivity = projectDetailActivity;
        projectDetailActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        projectDetailActivity.getSupportActionBar().setTitle(R.string.detail_view_title_project);

        dataSource = new DataSource(projectDetailActivity);

        project = projectDetailActivity.getIntent().getParcelableExtra("project");

        String customerName = dataSource.getCustomerByID(project.getCustomerID()).getName();
        projectDetailActivity.txtvProjectDetailCustomer.setText(customerName);
        projectDetailActivity.txtvProjectName.setText(project.getName());
        projectDetailActivity.txtvProjectDetailCreateDate.setText(project.getCreateDate());
        projectDetailActivity.txtvProjectDetailDescription.setText(project.getDescription());

        if(project.isStatus())
            projectDetailActivity.txtvProjectName.setPaintFlags(
                    projectDetailActivity.txtvProjectName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        updateTimeWorked();
    }

    private void updateTimeWorked(){
        int timeWorked = dataSource.getTotalTimeByProjectID(project.getDbID());
        if(timeWorked > 0)
            projectDetailActivity.txtvTimeWorked.setText(Utility.getFormattedTime(timeWorked));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.btnNewTask:
                intent = new Intent(projectDetailActivity, TaskActivity.class);
                intent.putExtra("project", (Parcelable) project);
                newTaskDialog(intent);
                break;
            case R.id.btnShowTasks:
                intent = new Intent(projectDetailActivity, ListViewActivity.class);
                intent.putExtra("mode", "task_project");
                intent.putExtra("projectID", project.getDbID());
                projectDetailActivity.startActivity(intent);
                break;
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.omDetailHome:
                projectDetailActivity.startActivity(new Intent(projectDetailActivity, MainActivity.class));
                projectDetailActivity.finish();
                break;
            case android.R.id.home:
                projectDetailActivity.finish();
                break;
            case R.id.omCheck:
                newProcjectFinishedConfirmationDialog();
                break;
        }
        return true;
    }

    private void newTaskDialog(Intent intent){
        final EditText description = new EditText(projectDetailActivity);

        AlertDialog.Builder alert = new AlertDialog.Builder(projectDetailActivity);
        alert.setTitle("Neue Zeiterfassung")
                .setMessage("Beschreibung eingeben")
                .setView(description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(description.getText().toString().isEmpty())
                            Toast.makeText(projectDetailActivity, "Bitte alles ausfüllen", Toast.LENGTH_SHORT);
                        else {
                            intent.putExtra("description", description.getText().toString());
                            projectDetailActivity.startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void newProcjectFinishedConfirmationDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(projectDetailActivity);
        alert.setTitle("Projektstatus ändern")
                .setMessage(project.isStatus() ? R.string.alert_message_uncheck_projekt :
                        R.string.alert_message_check_projekt)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        project.setStatus(!project.isStatus());
                        dataSource.updateProject(project);
                        projectDetailActivity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    public void onResume() {
        updateTimeWorked();
    }
}
