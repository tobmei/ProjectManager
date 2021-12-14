package com.example.projectmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    //Angabe Klassenname für spätere Logausgabe
    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    //Variable für DB Name
    private static final String DB_NAME = "project_manager.db3";
    //Varible für DB Version (wichtig um DB auf Endgeräten bei Bedraf aktualisieren zu können)
    private static final int DB_VERSION = 1;
    //Variable für den Tabellennamen
    static final String TABLE_CUSTOMER = "customer";
    static final String TABLE_PROJECT = "project";
    static final String TABLE_TASK = "task";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "Name";
    static final String COLUMN_DESCRIPTION = "Description";
    static final String COLUMN_CREATEDATE = "Erstelldatum";

    //Spalten für Customer
    static final String COLUMN_ADDRESS = "Address";
    static final String COLUMN_EMAIL = "Email";
    static final String COLUMN_PHONE = "Phone";
    static final String COLUMN_CONTACT = "Contact";

    //Spalten für Project
    static final String COLUMN_CUSTOMER_KEY = "Customer";
    static final String COLUMN_PROJECT_STATUS = "Status";

    //Spalten für Task
    static final String COLUMN_PROJECT_KEY = "Project";
    static final String COLUMN_TASK_TIME_START = "time_started";
    static final String COLUMN_TASK_TIME_END = "time_ended";
    static final String COLUMN_TASK_TIME_WORKED = "time_worked";

    //String zum erstellen der Tabelle
    private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE " + TABLE_CUSTOMER +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PHONE + " TEXT NOT NULL, " +
                    COLUMN_CONTACT + " TEXT NOT NULL);";

    private static final String SQL_CREATE_PROJECT =
            "CREATE TABLE " + TABLE_PROJECT +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CUSTOMER_KEY + " INTEGER NOT NULL, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_PROJECT_STATUS + " BOOLEAN DEFAULT 0, " +
                    COLUMN_CREATEDATE + " TEXT NOT NULL);";

    private static final String SQL_CREATE_TASK =
            "CREATE TABLE " + TABLE_TASK +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROJECT_KEY + " INTEGER NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_CREATEDATE + " TEXT NOT NULL, " +
                    //COLUMN_TASK_TIME_START + " TEXT NOT NULL, " +
                    //COLUMN_TASK_TIME_END + " TEXT NOT NULL, " +
                    COLUMN_TASK_TIME_WORKED + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank: " + getDatabaseName() + " erzeugt");
    }

    //Wird immer dann aufgerufen, wenn es noch keine Datenbank gibt
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            Log.d(LOG_TAG, "Tabelle anelgen: " + SQL_CREATE_CUSTOMER);
            Log.d(LOG_TAG, "Tabelle anelgen: " + SQL_CREATE_PROJECT);
            Log.d(LOG_TAG, "Tabelle anelgen: " + SQL_CREATE_TASK);
            sqLiteDatabase.execSQL(SQL_CREATE_CUSTOMER);
            sqLiteDatabase.execSQL(SQL_CREATE_PROJECT);
            sqLiteDatabase.execSQL(SQL_CREATE_TASK);
        }
        catch(Exception ex){
            Log.e(LOG_TAG, "Fehler beim anlegen der Tabellen: " + ex.getMessage());
        }
    }

    //Wird aufgerufen, sobald die neue Versionsnummer größer als die alte ist.
    //und somit ein Upgrade der Datenbank notwendig ist
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Die Tabelle mit der Versionsnummer " + oldVersion + " wird entfernt");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(sqLiteDatabase);

        //Um die Datensätze zu erhalten, müssten diese vorher in eine temporäre Tabelle gespeichert
        //werden und die Tabelle neu gebaut werden und alle Spalten, die neu hinzugekommen sind für
        //die bisherigen Datensätze befüllt werden, falls diese Spalten nicht leer sein dürfen.
    }
}
