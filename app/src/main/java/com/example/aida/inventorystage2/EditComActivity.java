package com.example.aida.inventorystage2;

/**
 * Created by Aida on 7/29/2018.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Locale;

import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_ID;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NAME;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_SALARY;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NUMBER;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_COMPANY_NAME;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_COMPANY_PHONE;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.CONTENT_URI;

public class EditComActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_EMPLOYEE_LOARDER = 0;
    private Uri currentEmployeeUri;
    private EditText name;
    private EditText salary;
    private EditText number;
    private EditText CustomerName;
    private EditText CompanyPhone;
    private boolean EmployeesHired = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            EmployeesHired = true;
            return false;
        }
    };
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        currentEmployeeUri = intent.getData();
        if (currentEmployeeUri == null) {
            setTitle("Add a new employee");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit employee");
            getLoaderManager().initLoader(EXISTING_EMPLOYEE_LOARDER, null, this);
        }
        name = findViewById(R.id.employee_name);
        salary = findViewById(R.id.employee_salary);
        number = findViewById(R.id.no_of_employee);
        CustomerName = findViewById(R.id.company_name);
        CompanyPhone = findViewById(R.id.company_phone);
        Button mIncrease = findViewById(R.id.increase_button);
        Button mDecrease = findViewById(R.id.decrease_button);
        Button mOrder = findViewById(R.id.hire_button);
        name.setOnTouchListener(mTouchListener);
        salary.setOnTouchListener(mTouchListener);
        number.setOnTouchListener(mTouchListener);
        CustomerName.setOnTouchListener(mTouchListener);
        CompanyPhone.setOnTouchListener(mTouchListener);
        mIncrease.setOnTouchListener(mTouchListener);
        mDecrease.setOnTouchListener(mTouchListener);
        mIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = EditComActivity.this.number.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    EditComActivity.this.number.setText("1");
                } else {
                    int not_null_quantity = Integer.parseInt(EditComActivity.this.number.getText().toString().trim());
                    not_null_quantity++;
                    EditComActivity.this.number.setText(String.valueOf(not_null_quantity));
                }
            }
        });
        mDecrease.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String quantity = EditComActivity.this.number.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    EditComActivity.this.number.setText("0");
                } else {
                    int new_quantity = Integer.parseInt(EditComActivity.this.number.getText().toString().trim());
                    if (new_quantity > 0) {
                        new_quantity--;
                        EditComActivity.this.number.setText(String.valueOf(new_quantity));
                    } else {
                        Toast.makeText(EditComActivity.this, "The number cannot be negative!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = CompanyPhone.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
    private void saveEmployee() {
        String nameString = name.getText().toString().trim();
        String salaryString = salary.getText().toString().trim();
        String numberString = number.getText().toString().trim();
        String companyNameString = CustomerName.getText().toString().trim();
        String companyPhoneString = CompanyPhone.getText().toString().trim();
        if (currentEmployeeUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(salaryString) &&
                TextUtils.isEmpty(numberString) && TextUtils.isEmpty(companyNameString) &&
                TextUtils.isEmpty(companyPhoneString)) {
            Toast.makeText(this, R.string.nothing_was_changed, Toast.LENGTH_LONG).show();
            // Exit activity
            finish();
            return;
        }

        if (TextUtils.isEmpty(nameString)) {
            name.requestFocus();
            name.setError(getString(R.string.empty_field_error));
            Toast.makeText(this, getString(R.string.enter_the_employee_name), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(salaryString)) {
            salary.requestFocus();
            salary.setError(getString(R.string.empty_field_error));
            Toast.makeText(this, getString(R.string.enter_the_salary_for_employee), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(numberString) || Integer.parseInt(numberString) == 0) {
            number.requestFocus();
            number.setError(getString(R.string.empty_field_error));
            Toast.makeText(this, getString(R.string.enter_the_no_of_employee), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(companyNameString)) {
            CustomerName.requestFocus();
            CustomerName.setError(getString(R.string.empty_field_error));
            Toast.makeText(this, getString(R.string.enter_the_company_name), Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(companyPhoneString)) {
            CompanyPhone.requestFocus();
            CompanyPhone.setError(getString(R.string.empty_field_error));
            Toast.makeText(this, getString(R.string.enter_the_company_phone_number), Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nameString);
        float priceFloat = Float.parseFloat(salaryString);
        values.put(COLUMN_SALARY, priceFloat);
        values.put(COLUMN_NUMBER, numberString);
        values.put(COLUMN_COMPANY_NAME, companyNameString);
        values.put(COLUMN_COMPANY_PHONE, companyPhoneString);
        if (currentEmployeeUri == null) {
            Uri newUri = getContentResolver().insert(CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, R.string.save_error,
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, R.string.added_msg,
                        Toast.LENGTH_SHORT).show();
                // Exit activity
                finish();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentEmployeeUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.update_error,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.updated_msg,
                        Toast.LENGTH_SHORT).show();
            }
            // Exit activity
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentEmployeeUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveEmployee();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!EmployeesHired) {
                    NavUtils.navigateUpFromSameTask(EditComActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditComActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!EmployeesHired) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_SALARY,
                COLUMN_NUMBER,
                COLUMN_COMPANY_NAME,
                COLUMN_COMPANY_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentEmployeeUri,         // Query the content URI for the current it device
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(COLUMN_SALARY);
            int quantityColumnIndex = cursor.getColumnIndex(COLUMN_NUMBER);
            int supplier_nameColumnIndex = cursor.getColumnIndex(COLUMN_COMPANY_NAME);
            int supplier_phoneColumnIndex = cursor.getColumnIndex(COLUMN_COMPANY_PHONE);
            String name = cursor.getString(nameColumnIndex);
            Float price = cursor.getFloat(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier_name = cursor.getString(supplier_nameColumnIndex);
            String phone = cursor.getString(supplier_phoneColumnIndex);
            this.name.setText(name);
            this.salary.setText(String.format(Float.toString(price), Locale.getDefault()));
            this.number.setText(String.format(Integer.toString(quantity), Locale.getDefault()));
            CustomerName.setText(supplier_name);
            CompanyPhone.setText(phone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        name.setText(R.string.blank);
        salary.setText(R.string.blank);
        number.setText(R.string.blank);
        CustomerName.setText(R.string.blank);
        CompanyPhone.setText(R.string.blank);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_your_changes_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItDevice();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteItDevice() {
        if (currentEmployeeUri != null) {
            int rowsDeleted = getContentResolver().delete(currentEmployeeUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.deleting_error_msg,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.device_deleted,
                        Toast.LENGTH_SHORT).show();
            }
            // Close the activity
            finish();
        }
    }
}

