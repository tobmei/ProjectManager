package com.example.projectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.projectmanager.model.Customer;
import com.example.projectmanager.model.Project;
import com.example.projectmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private static final String LOG_TAG = DataSource.class.getSimpleName();

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private String[] columns_customer = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_NAME,
            DBHelper.COLUMN_ADDRESS,
            DBHelper.COLUMN_PHONE,
            DBHelper.COLUMN_EMAIL,
            DBHelper.COLUMN_CONTACT
    };
    private String[] columns_project = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_CUSTOMER_KEY,
            DBHelper.COLUMN_NAME,
            DBHelper.COLUMN_DESCRIPTION,
            DBHelper.COLUMN_CREATEDATE,
            DBHelper.COLUMN_PROJECT_STATUS
    };
    private String[] columns_task = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_PROJECT_KEY,
            DBHelper.COLUMN_DESCRIPTION,
            DBHelper.COLUMN_CREATEDATE,
            //DBHelper.COLUMN_TASK_TIME_START,
            //DBHelper.COLUMN_TASK_TIME_END,
            DBHelper.COLUMN_TASK_TIME_WORKED
    };

    public DataSource(Context context) {
        Log.d(LOG_TAG, "DataSource erzeugt dbHelper");
        dbHelper = new DBHelper(context);
    }

    private void open_writeable() {
        Log.d(LOG_TAG, "Eine schreibende Referenz auf die Datenbank wird jetzt angefragt");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbankreferenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    private void open_readable() {
        Log.d(LOG_TAG, "Eine lesende Referenz auf die Datenbank wird jetzt angefragt");
        database = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Datenbankreferenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    private void close_db() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DBHelper geschlossen");
    }

    private Customer cursorToCustomer(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DBHelper.COLUMN_NAME);
        int idAddress = cursor.getColumnIndex(DBHelper.COLUMN_ADDRESS);
        int idPhone = cursor.getColumnIndex(DBHelper.COLUMN_PHONE);
        int idEmail = cursor.getColumnIndex(DBHelper.COLUMN_EMAIL);
        int idContact = cursor.getColumnIndex(DBHelper.COLUMN_CONTACT);

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        String address = cursor.getString(idAddress);
        String phone = cursor.getString(idPhone);
        String email = cursor.getString(idEmail);
        String contact = cursor.getString(idContact);

        return new Customer(id, name, address, email, phone, contact);
    }

    private Project cursorToProject(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(DBHelper.COLUMN_NAME);
        int idCustomer = cursor.getColumnIndex(DBHelper.COLUMN_CUSTOMER_KEY);
        int idDesc = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);
        int idDate = cursor.getColumnIndex(DBHelper.COLUMN_CREATEDATE);
        int idStatus = cursor.getColumnIndex(DBHelper.COLUMN_PROJECT_STATUS);

        int id = cursor.getInt(idIndex);
        String name = cursor.getString(idName);
        int customer = cursor.getInt(idCustomer);
        String description = cursor.getString(idDesc);
        String createDate = cursor.getString(idDate);
        int valueChecked = cursor.getInt(idStatus);
        boolean status = (valueChecked != 0);

        return new Project(id, name, customer, description, createDate, status);
    }

    private Task cursorToTask(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idProject = cursor.getColumnIndex(DBHelper.COLUMN_PROJECT_KEY);
        int idDesc = cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION);
        //int idEnd = cursor.getColumnIndex(DBHelper.COLUMN_TASK_TIME_END);
        //int idStart = cursor.getColumnIndex(DBHelper.COLUMN_TASK_TIME_START);
        int idWorked = cursor.getColumnIndex(DBHelper.COLUMN_TASK_TIME_WORKED);
        int idDate = cursor.getColumnIndex(DBHelper.COLUMN_CREATEDATE);

        int id = cursor.getInt(idIndex);
        int project = cursor.getInt(idProject);
        String descripton = cursor.getString(idDesc);
        //String end = cursor.getString(idEnd);
        //String start = cursor.getString(idStart);
        int worked = cursor.getInt(idWorked);
        String createDate = cursor.getString(idDate);

        return new Task(id, project, descripton, createDate, worked);
    }

    public Customer insertCustomer(Customer customer){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, customer.getName());
        values.put(DBHelper.COLUMN_PHONE, customer.getPhone());
        values.put(DBHelper.COLUMN_ADDRESS, customer.getAddress());
        values.put(DBHelper.COLUMN_EMAIL, customer.getEmail());
        values.put(DBHelper.COLUMN_CONTACT, customer.getContact());

        open_writeable();
        long insertId = database.insert(DBHelper.TABLE_CUSTOMER, null, values);

        /*Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(insertId)}, null, null, null);*/
        Cursor cursor = database.query(DBHelper.TABLE_CUSTOMER, columns_customer,
                DBHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Customer currentCustomer = cursorToCustomer(cursor);
        cursor.close();
        close_db();

        return currentCustomer;
    }

    public Project insertProject(Project project){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CUSTOMER_KEY, project.getCustomerID());
        values.put(DBHelper.COLUMN_NAME, project.getName());
        values.put(DBHelper.COLUMN_DESCRIPTION, project.getDescription());
        values.put(DBHelper.COLUMN_PROJECT_STATUS, project.isStatus());
        values.put(DBHelper.COLUMN_CREATEDATE, project.getCreateDate());

        open_writeable();
        long insertId = database.insert(DBHelper.TABLE_PROJECT, null, values);

        /*Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(insertId)}, null, null, null);*/
        Cursor cursor = database.query(DBHelper.TABLE_PROJECT, columns_project,
                DBHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Project currentProject = cursorToProject(cursor);
        cursor.close();
        close_db();

        return currentProject;
    }

    public Task insertTask(Task task){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PROJECT_KEY, task.getProjectID());
        values.put(DBHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(DBHelper.COLUMN_CREATEDATE, task.getCreateDate());
        //values.put(DBHelper.COLUMN_TASK_TIME_START, task.getTimeStart());
        //values.put(DBHelper.COLUMN_TASK_TIME_END, task.getTimeEnd());
        values.put(DBHelper.COLUMN_TASK_TIME_WORKED, task.getTime_worked());

        open_writeable();
        long insertId = database.insert(DBHelper.TABLE_TASK, null, values);

        /*Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(insertId)}, null, null, null);*/
        Cursor cursor = database.query(DBHelper.TABLE_TASK, columns_task,
                DBHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Task currentTask = cursorToTask(cursor);
        cursor.close();
        close_db();

        return currentTask;
    }

    public void deleteCustomer(Customer customer) {
        open_writeable();

        //Hole alle ProjektIDs zum customer
        Cursor cursor = database.query(DBHelper.TABLE_PROJECT, new String[]{DBHelper.COLUMN_ID},
                DBHelper.COLUMN_CUSTOMER_KEY + " = " + customer.getDbID() ,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                //Lösche alle Tasks mit geholten ProjektIDs
                database.delete(DBHelper.TABLE_TASK,
                        DBHelper.COLUMN_PROJECT_KEY + " = " + cursor.getInt(0),
                        null);
            } while (cursor.moveToNext());
        }
        cursor.close();

        database.delete(DBHelper.TABLE_PROJECT,
                DBHelper.COLUMN_CUSTOMER_KEY + " = " + customer.getDbID(),
                null);

        database.delete(DBHelper.TABLE_CUSTOMER,
                DBHelper.COLUMN_ID + " = " + customer.getDbID(),
                null);


        close_db();
    }

    public void deleteProject(Project project) {
        open_writeable();
        database.delete(DBHelper.TABLE_PROJECT,
                DBHelper.COLUMN_ID + " = " + project.getDbID(),
                null);

        database.delete(DBHelper.TABLE_TASK,
                DBHelper.COLUMN_PROJECT_KEY + " = " + project.getDbID(),
                null);
        close_db();
    }

    public void deleteTask(Task task) {
        open_writeable();
        database.delete(DBHelper.TABLE_TASK,
                DBHelper.COLUMN_ID + " = " + task.getDbID(),
                null);
        close_db();
    }

    public void updateCustomer(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, customer.getName());
        values.put(DBHelper.COLUMN_ADDRESS, customer.getAddress());
        values.put(DBHelper.COLUMN_PHONE, customer.getPhone());
        values.put(DBHelper.COLUMN_EMAIL, customer.getEmail());
        values.put(DBHelper.COLUMN_CONTACT, customer.getContact());

        open_writeable();
        database.update(DBHelper.TABLE_CUSTOMER, values, DBHelper.COLUMN_ID + " = " + customer.getDbID(),
                null);
        close_db();
    }

    public void updateProject(Project project) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DESCRIPTION, project.getDescription());
        values.put(DBHelper.COLUMN_PROJECT_STATUS, project.isStatus());

        open_writeable();
        database.update(DBHelper.TABLE_PROJECT, values, DBHelper.COLUMN_ID + " = " + project.getDbID(),
                null);
        close_db();
    }

    public void updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DESCRIPTION, task.getDescription());

        open_writeable();
        database.update(DBHelper.TABLE_TASK, values, DBHelper.COLUMN_ID + " = " + task.getDbID(),
                null);
        close_db();
    }

    public List<Customer> getAllCustomers(){
        List<Customer> list = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLE_CUSTOMER;
        open_readable();

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToCustomer(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public Customer getCustomerByID(int id){
        open_readable();

        Cursor cursor = database.query(DBHelper.TABLE_CUSTOMER, columns_customer,
                DBHelper.COLUMN_ID + " = " + id ,
                null, null, null, null);
        cursor.moveToFirst();
        Customer customer = cursorToCustomer(cursor);
        cursor.close();
        close_db();
        return customer;
    }

    public int getTotalTimeByProjectID(int id){
        open_readable();
        int time = 0;

        Cursor cursor = database.query(DBHelper.TABLE_TASK, new String[]{DBHelper.COLUMN_TASK_TIME_WORKED},
                DBHelper.COLUMN_PROJECT_KEY + " = " + id ,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                time += Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return time;
    }

    public List<Project> getAllProjects(){
        List<Project> list = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLE_PROJECT;
        open_readable();

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToProject(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public List<Project> getAllProjectsUndone(){
        List<Project> list = new ArrayList<>();
        open_readable();

        Cursor cursor = database.query(DBHelper.TABLE_PROJECT, columns_project,
                DBHelper.COLUMN_PROJECT_STATUS + " = 0" ,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToProject(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public List<Task> getAllTasksForProject(int projectID) {
        List<Task> list = new ArrayList<>();
        open_readable();

        Cursor cursor = database.query(DBHelper.TABLE_TASK, columns_task,
                DBHelper.COLUMN_PROJECT_KEY + " = " + projectID ,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public List<Project> getProjectsForCustomer(int customerID){
        List<Project> list = new ArrayList<>();
        open_readable();

        Cursor cursor = database.query(DBHelper.TABLE_PROJECT, columns_project,
                DBHelper.COLUMN_CUSTOMER_KEY + " = " + customerID ,
                null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToProject(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public List<Project> getProjectsForCustomerUndone(int customerID){
        List<Project> list = new ArrayList<>();
        open_readable();

        Cursor cursor = database.query(DBHelper.TABLE_PROJECT, columns_project,
                DBHelper.COLUMN_CUSTOMER_KEY + " = " + customerID + " AND "
                        + DBHelper.COLUMN_PROJECT_STATUS + " = 0",
                null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToProject(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }

    public List<Task> getAllTasks(){
        List<Task> list = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLE_PROJECT;
        open_readable();

        Cursor cursor = database.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;
    }


}
