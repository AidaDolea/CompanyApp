package com.example.aida.inventorystage2.data;

/**
 * Created by Aida on 7/29/2018.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_ID;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.TABLE_NAME;


public class EmployeeProvider extends ContentProvider {

    public static final String LOG_TAG = EmployeeProvider.class.getSimpleName();
    private static final int EMPLOYEES = 10;
    private static final int EMPLOYEE_ID = 11;
    private static final UriMatcher uri = new UriMatcher(UriMatcher.NO_MATCH);

    // This runs the first.
    static {
        uri.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INVENTORY, EMPLOYEES);
        uri.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INVENTORY + "/#", EMPLOYEE_ID);
    }

    // Database helper object
    private DbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        int match = EmployeeProvider.uri.match(uri);
        switch (match) {
            case EMPLOYEES:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case EMPLOYEE_ID:
                selection = COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        // Return the cursor
        return cursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = EmployeeProvider.uri.match(uri);
        switch (match) {
            case EMPLOYEES:
                return insertIt(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertIt(Uri uri, ContentValues values) {
        String name = values.getAsString(Contract.CompanyEntry.COLUMN_NAME);
        if ((name == null) || (name.isEmpty())) {
            throw new IllegalArgumentException("The employee requires a name");
        }
        Integer salary = values.getAsInteger(Contract.CompanyEntry.COLUMN_SALARY);
        if (salary != null && salary < 0) {
            throw new IllegalArgumentException("The salary field requires valid number");
        }
        Integer numberOfEmployees = values.getAsInteger(Contract.CompanyEntry.COLUMN_NUMBER);
        if (numberOfEmployees != null && numberOfEmployees < 0) {
            throw new IllegalArgumentException("The numberOfEmployees field requires valid number");
        }
        String sname = values.getAsString(Contract.CompanyEntry.COLUMN_COMPANY_NAME);
        if (sname == null) {
            throw new IllegalArgumentException("The company requires a name");
        }
        String phone = values.getAsString(Contract.CompanyEntry.COLUMN_COMPANY_PHONE);
        if (phone == null) {
            throw new IllegalArgumentException("The company requires a phone number");
        }
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new it device with the given values
        long id = database.insert(TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the it device content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = EmployeeProvider.uri.match(uri);
        switch (match) {
            case EMPLOYEES:
                return updateIt(uri, contentValues, selection, selectionArgs);
            case EMPLOYEE_ID:
                selection = Contract.CompanyEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateIt(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateIt(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(Contract.CompanyEntry.COLUMN_NAME)) {
            String name = values.getAsString(Contract.CompanyEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("The employee requires a name");
            }
        }
        if (values.containsKey(Contract.CompanyEntry.COLUMN_SALARY)) {
            Integer salary = values.getAsInteger(Contract.CompanyEntry.COLUMN_SALARY);
            if (salary != null && salary < 0) {
                throw new IllegalArgumentException("The salary field requires valid number");
            }
        }
        Integer no_of_employees = values.getAsInteger(Contract.CompanyEntry.COLUMN_NUMBER);
        if (no_of_employees != null && no_of_employees < 0) {
            throw new IllegalArgumentException("The no_of_employees field requires valid number");
        }
        if (values.containsKey(Contract.CompanyEntry.COLUMN_COMPANY_NAME)) {
            String sname = values.getAsString(Contract.CompanyEntry.COLUMN_COMPANY_NAME);
            if (sname == null) {
                throw new IllegalArgumentException("The company requires a name");
            }
        }
        if (values.containsKey(Contract.CompanyEntry.COLUMN_COMPANY_NAME)) {
            String phone = values.getAsString(Contract.CompanyEntry.COLUMN_COMPANY_PHONE);
            if (phone == null) {
                throw new IllegalArgumentException("The company requires a phone number");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = EmployeeProvider.uri.match(uri);
        switch (match) {
            case EMPLOYEES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                // Delete a single row given by the ID in the URI
                selection = Contract.CompanyEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
    @Override
    public String getType(Uri uri) {
        final int match = EmployeeProvider.uri.match(uri);
        switch (match) {
            case EMPLOYEES:
                return Contract.CompanyEntry.CONTENT_LIST_TYPE;
            case EMPLOYEE_ID:
                return Contract.CompanyEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

