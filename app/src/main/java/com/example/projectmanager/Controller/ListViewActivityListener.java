package com.example.projectmanager.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectmanager.Adapter.CustomerArrayAdapter;
import com.example.projectmanager.Adapter.ProjectArrayAdapter;
import com.example.projectmanager.Adapter.TaskArrayAdapter;
import com.example.projectmanager.DataSource;
import com.example.projectmanager.View.ProjectDetailActivity;
import com.example.projectmanager.R;
import com.example.projectmanager.Utility;
import com.example.projectmanager.View.CustomerDetailActivity;
import com.example.projectmanager.View.ListViewActivity;
import com.example.projectmanager.View.MainActivity;
import com.example.projectmanager.model.Customer;
import com.example.projectmanager.model.Project;
import com.example.projectmanager.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListViewActivityListener implements AdapterView.OnItemClickListener{

    ListViewActivity listViewActivity;
    DataSource dataSource;

    List<Customer> customerList;
    List<Project> projectList;
    List<Task> taskList;

    CustomerArrayAdapter customerArrayAdapter;
    ProjectArrayAdapter projectArrayAdapter;
    TaskArrayAdapter taskArrayAdapter;

    private final String mode;
    private Customer projectCustomer = null;
    private int projectID;
    

    public ListViewActivityListener(ListViewActivity listViewActivity) {
        this.listViewActivity = listViewActivity;
        listViewActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataSource = new DataSource(listViewActivity);

        //createDummyEntries(3);

        //Welche Daten sollen angezeigt werden?
        //Kunden, alle Projekte, Projekte zu Kunde, Zeiterfassungen zu Projekt
        mode = listViewActivity.getIntent().getStringExtra("mode");
        if(mode.equals("customer"))
            listViewActivity.getSupportActionBar().setTitle(R.string.text_customer);

        else if(mode.equals("project_all"))
            listViewActivity.getSupportActionBar().setTitle(R.string.text_projects_all);

        if(mode.equals("project_customer")){
            projectCustomer = listViewActivity.getIntent().getParcelableExtra("customer");
            listViewActivity.getSupportActionBar().setTitle(R.string.text_projects);
        }
        else if(mode.equals("task_project")){
            projectID = listViewActivity.getIntent().getIntExtra("projectID", 0);
            listViewActivity.getSupportActionBar().setTitle(R.string.text_overview_task);
        }

        initListView();
    }
    private void createDummyEntries(int count) {
        for(int i=0;i<count;i++){
            Customer customer = dataSource.insertCustomer(new Customer("Kunde"+i+" GmbH", "Fakestreet "+i, "info"+i+"@testmail.de", "123456"+i, "Kontaktperson "+i));
            for(int j=0;j<count;j++){
                dataSource.insertProject(new Project("Projekt "+j, customer.getDbID(), Utility.loremIpsum, Utility.getCurrentTime(), false));
            }
        }
    }

    private void initListView(){
        SharedPreferences sp = listViewActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean onlyUndone = false;
        if(sp.contains("OnlyUndone")){
            onlyUndone = sp.getBoolean("OnlyUndone", false);
        }
        switch(mode){
            case "customer":
                customerList = new ArrayList<>();
                customerList = dataSource.getAllCustomers();
                customerArrayAdapter = new CustomerArrayAdapter(listViewActivity, customerList);
                listViewActivity.lvListView.setAdapter(customerArrayAdapter);
                break;
            case "project_all":
                projectList = new ArrayList<>();
                if(onlyUndone)
                    projectList = dataSource.getAllProjectsUndone();
                else
                    projectList = dataSource.getAllProjects();
                projectArrayAdapter = new ProjectArrayAdapter(listViewActivity, projectList, dataSource);
                listViewActivity.lvListView.setAdapter(projectArrayAdapter);
                break;
            case "project_customer":
                projectList = new ArrayList<>();
                if(onlyUndone)
                    projectList = dataSource.getProjectsForCustomerUndone(projectCustomer.getDbID());
                else
                    projectList = dataSource.getProjectsForCustomer(projectCustomer.getDbID());
                projectArrayAdapter = new ProjectArrayAdapter(listViewActivity, projectList, dataSource);
                listViewActivity.lvListView.setAdapter(projectArrayAdapter);
                break;
            case "task_project":
                taskList = new ArrayList<>();
                taskList = dataSource.getAllTasksForProject(projectID);
                taskArrayAdapter = new TaskArrayAdapter(listViewActivity, taskList);
                listViewActivity.lvListView.setAdapter(taskArrayAdapter);
                break;
        }
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.omNew:
                switch(mode){
                    case "customer":
                        addCustomerDialog(customerList);
                        break;
                    case "project_all":
                    case "project_customer":
                        if(dataSource.getAllCustomers().isEmpty())
                            Toast.makeText(listViewActivity, "Sie müssen zuerst einen Kunden anlegen", Toast.LENGTH_SHORT).show();
                        else
                            addProjectDialog();
                        break;
                    case "task_project":
                        break;
                }
                break;
            case R.id.omDashboard:
                listViewActivity.startActivity(new Intent(listViewActivity, MainActivity.class));
                break;
            case android.R.id.home:
                listViewActivity.finish();
                break;
            case R.id.omDummies:
                createDummyEntries(2);
                initListView();
                break;
        }
        return true;
    }

    public boolean onContextItemSeleceted(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Customer selectedCustomer;
        Project selectedProject;
        Task selectedTask;
        switch(item.getItemId()){
            case R.id.cmDelete:
                switch(mode){
                    case "customer":
                        selectedCustomer = customerList.get(info.position);
                        customerDeleteConfirmationDialog(selectedCustomer);
                        break;
                    case "project_all":
                    case "project_customer":
                        selectedProject = projectList.get(info.position);
                        projectDeleteConfirmationDialog(selectedProject);
                        break;
                    case "task_project":
                        selectedTask = taskList.get(info.position);
                        taskDeleteConfirmationDialog(selectedTask);
                        break;
                }
                break;
            case R.id.cmEdit:
                switch(mode){
                    case "customer":
                        selectedCustomer = customerList.get(info.position);
                        editCustomerDialog(selectedCustomer);
                        break;
                    case "project_all":
                    case "project_customer":
                        selectedProject = projectList.get(info.position);
                        editProjectDialog(selectedProject);
                        break;
                    case "task_project":
                        selectedTask = taskList.get(info.position);
                        editTaskDialog(selectedTask);
                }
                break;
        }
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch(mode){
            case "customer":
                Customer currentCustomer = (Customer) parent.getItemAtPosition(position);
                intent = new Intent(listViewActivity, CustomerDetailActivity.class);
                intent.putExtra("customer", currentCustomer);
                listViewActivity.startActivity(intent);
                break;
            case "project_all":
            case "project_customer":
                Project currentProject = (Project) parent.getItemAtPosition(position);
                intent = new Intent(listViewActivity, ProjectDetailActivity.class);
                intent.putExtra("project", currentProject);
                listViewActivity.startActivity(intent);
                break;
        }
    }

    //region Alert Dialoge

    private void customerDeleteConfirmationDialog(Customer customer){
        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(customer.getName()+ " entfernen")
                .setMessage(R.string.alert_message_delete_customer)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataSource.deleteCustomer(customer);
                        customerList.remove(customer);
                        customerArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void projectDeleteConfirmationDialog(Project project){
        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(project.getName()+ " entfernen")
                .setMessage(R.string.alert_message_delete_projekt)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataSource.deleteProject(project);
                        projectList.remove(project);
                        projectArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void taskDeleteConfirmationDialog(Task task){
        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle("Zeiterfassung entfernen")
                .setMessage(R.string.alert_message_delete_task)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataSource.deleteTask(task);
                        taskList.remove(task);
                        taskArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void addCustomerDialog(List<Customer> customerList){
        LayoutInflater inflater = LayoutInflater.from(listViewActivity);
        View v = inflater.inflate(R.layout.new_customer_scroll, null, false);
        final EditText etName = v.findViewById(R.id.etCustomerName);
        final EditText etAddress = v.findViewById(R.id.etCustomerAddress);
        final EditText etContact = v.findViewById(R.id.etCustomerContact);
        final EditText etPhone = v.findViewById(R.id.etCustomerPhone);
        final EditText etEmail = v.findViewById(R.id.etCustomerEmail);

        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle("Neuen Kunden anlegen")
                .setView(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String customerName = etName.getText().toString();
                        String customerAddress = etAddress.getText().toString();
                        String customerContact = etContact.getText().toString();
                        String customerPhone = etPhone.getText().toString();
                        String customerEmail = etEmail.getText().toString();
                        if(customerName.isEmpty() || customerEmail.isEmpty() ||
                                customerAddress.isEmpty() || customerContact.isEmpty() || customerPhone.isEmpty())
                            Toast.makeText( listViewActivity, "Bitte alles ausfüllen", Toast.LENGTH_SHORT).show();
                        else {

                            Customer customer = new Customer(customerName, customerAddress, customerEmail, customerPhone, customerContact);
                            customerList.add(0, dataSource.insertCustomer(customer));
                            customerArrayAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void editCustomerDialog(Customer selectedCustomer){
        LayoutInflater inflater = LayoutInflater.from(listViewActivity);
        View v = inflater.inflate(R.layout.new_customer_scroll, null, false);
        final EditText etName = v.findViewById(R.id.etCustomerName);
        final EditText etAddress = v.findViewById(R.id.etCustomerAddress);
        final EditText etContact = v.findViewById(R.id.etCustomerContact);
        final EditText etPhone = v.findViewById(R.id.etCustomerPhone);
        final EditText etEmail = v.findViewById(R.id.etCustomerEmail);
        etName.setText(selectedCustomer.getName());
        etAddress.setText(selectedCustomer.getAddress());
        etContact.setText(selectedCustomer.getContact());
        etPhone.setText(selectedCustomer.getPhone());
        etEmail.setText(selectedCustomer.getEmail());

        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(selectedCustomer.getName() + " bearbeiten")
                .setView(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedCustomer.setName(etName.getText().toString());
                        selectedCustomer.setAddress(etAddress.getText().toString());
                        selectedCustomer.setContact(etContact.getText().toString());
                        selectedCustomer.setPhone(etPhone.getText().toString());
                        selectedCustomer.setEmail(etEmail.getText().toString());
                        dataSource.updateCustomer(selectedCustomer);
                        customerArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void addProjectDialog(){
        LayoutInflater inflater = LayoutInflater.from(listViewActivity);
        View v = inflater.inflate(R.layout.new_project, null, false);
        final EditText etName = v.findViewById(R.id.etProjectName);
        final Spinner ddCustomer = v.findViewById(R.id.ddCustomer);
        final EditText etDescription = v.findViewById(R.id.etProjectDescription);

        List<Customer> customerList;
        if(projectCustomer == null)
            customerList = dataSource.getAllCustomers();
        else
            customerList = Arrays.asList(projectCustomer);

        ArrayAdapter<Customer> spinnerArrayAdapter = new ArrayAdapter<>(listViewActivity, android.R.layout.simple_spinner_item, customerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddCustomer.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(R.string.alert_title_new_project)
                .setView(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = etName.getText().toString();
                        int customerID = ((Customer)ddCustomer.getSelectedItem()).getDbID();
                        String description = etDescription.getText().toString();
                        if(description.isEmpty())
                            Toast.makeText( listViewActivity, "Bitte alles ausfüllen", Toast.LENGTH_SHORT).show();
                        else {
                            Project project = new Project(name, customerID, description, Utility.getCurrentTime(), false);
                            projectList.add(0, dataSource.insertProject(project));
                            projectArrayAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void editProjectDialog(Project selectedProject){
        LayoutInflater inflater = LayoutInflater.from(listViewActivity);
        View v = inflater.inflate(R.layout.new_project, null, false);
        final EditText etName = v.findViewById(R.id.etProjectName);
        final Spinner ddCustomer = v.findViewById(R.id.ddCustomer);
        final EditText etDescription = v.findViewById(R.id.etProjectDescription);
        etName.setText(selectedProject.getName());
        etDescription.setText(selectedProject.getDescription());
        //ddCustomer.setVisibility(View.INVISIBLE);

        List<Customer> customerList = new ArrayList<>();
        customerList.add(dataSource.getCustomerByID(selectedProject.getCustomerID()));

        ArrayAdapter<Customer> spinnerArrayAdapter = new ArrayAdapter<>(listViewActivity, android.R.layout.simple_spinner_item, customerList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddCustomer.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(R.string.alert_title_edit_project)
                .setView(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedProject.setCustomerID(((Customer)ddCustomer.getSelectedItem()).getDbID());
                        selectedProject.setDescription(etDescription.getText().toString());
                        selectedProject.setName(etName.getText().toString());
                        dataSource.updateProject(selectedProject);
                        projectArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    private void editTaskDialog(Task task){
        final EditText etDescription = new EditText(listViewActivity);
        etDescription.setText(task.getDescription());

        AlertDialog.Builder alert = new AlertDialog.Builder(listViewActivity);
        alert.setTitle(R.string.alert_title_edit_project)
                .setView(etDescription)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.setDescription(etDescription.getText().toString());
                        dataSource.updateTask(task);
                        taskArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    //endregion

    public boolean onCreateOptionsMenu(Menu menu){
        listViewActivity.getMenuInflater().inflate(R.menu.optionsmenu, menu);
        if(mode.equals("task_project"))
            menu.findItem(R.id.omNew).setVisible(false);
        if(!mode.equals("customer"))
            menu.findItem(R.id.omDummies).setVisible(false);
        return true;
    }

    public void onResume() {
        initListView();
    }
}
