package com.example.projectmanager.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.projectmanager.Controller.ListViewActivityListener;
import com.example.projectmanager.R;

public class ListViewActivity extends AppCompatActivity {

    ListViewActivityListener listViewActivityListener;
    public ListView lvListView;

    private String dataToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        lvListView = findViewById(R.id.lvList);
        listViewActivityListener = new ListViewActivityListener(this);
        lvListView.setOnItemClickListener(listViewActivityListener);
        registerForContextMenu(lvListView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        listViewActivityListener.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return listViewActivityListener.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return listViewActivityListener.onContextItemSeleceted(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listViewActivityListener.onResume();
    }
}