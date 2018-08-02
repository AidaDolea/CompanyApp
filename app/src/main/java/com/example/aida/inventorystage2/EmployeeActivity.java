package com.example.aida.inventorystage2;

/**
 * Created by Aida on 7/29/2018.
 */

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_ID;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NAME;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_SALARY;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NUMBER;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.CONTENT_URI;

public class EmployeeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EMPLOYEES_LOADER = 0;
    CompanyAdapter cursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            
            
        //changing status bar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.DKGRAY);
        }

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeActivity.this, EditComActivity.class);
                startActivity(intent);
            }
        });
        ListView itListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        itListView.setEmptyView(emptyView);
        cursorAdapter = new CompanyAdapter(this, null);
        itListView.setAdapter(cursorAdapter);
        itListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(EmployeeActivity.this, R.string.edit_employee,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EmployeeActivity.this, EditComActivity.class);
                Uri currentUri = ContentUris.withAppendedId(CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(EMPLOYEES_LOADER, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/main_menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_data:
                Intent intent = new Intent(EmployeeActivity.this, EditComActivity.class);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_SALARY,
                COLUMN_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link CompanyAdapter} with this new cursor containing updated it device data
        cursorAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        cursorAdapter.swapCursor(null);
    }
    private void deleteAll() {
        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
        Log.v("EmployeeActivity", rowsDeleted + " rows deleted from it database");
    }
}
