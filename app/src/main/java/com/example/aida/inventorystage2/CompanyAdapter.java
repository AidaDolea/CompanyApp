package com.example.aida.inventorystage2;

/**
 * Created by Aida on 7/29/2018.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_ID;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NAME;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_SALARY;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.COLUMN_NUMBER;
import static com.example.aida.inventorystage2.data.Contract.CompanyEntry.CONTENT_URI;

public class CompanyAdapter extends CursorAdapter {
    public CompanyAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.employee_name);
        TextView salaryTextView = view.findViewById(R.id.employee_salary);
        TextView no_of_empl_TextView = view.findViewById(R.id.no_of_employee);
        Button hireButton = view.findViewById(R.id.sell);
        int nameColumnIndex = cursor.getColumnIndex(COLUMN_NAME);
        int salaryColumnIndex = cursor.getColumnIndex(COLUMN_SALARY);
        int no_of_empl_index = cursor.getColumnIndex(COLUMN_NUMBER);
        final String employee_name = cursor.getString(nameColumnIndex);
        final String employee_salary = cursor.getString(salaryColumnIndex);
        String no_of_empl = cursor.getString(no_of_empl_index);
        nameTextView.setText(employee_name);
        salaryTextView.setText(employee_salary);
        no_of_empl_TextView.setText(no_of_empl);
        final int idColumnIndex = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        final int currNoOfEmplIndex = cursor.getColumnIndex(COLUMN_NUMBER);
        final int currNoOfEmpl = Integer.valueOf(cursor.getString(currNoOfEmplIndex));
        hireButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currNoOfEmpl > 0) {
                    int newCurrNoOfEmpl = currNoOfEmpl - 1;
                    Uri noOfEmplUri = ContentUris.withAppendedId(CONTENT_URI, idColumnIndex);
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NUMBER, newCurrNoOfEmpl);
                    context.getContentResolver().update(noOfEmplUri, values, null, null);
                    Toast.makeText(context, "The hiring was successfully! \nThe new number of employees for "+ employee_name + " is: " + newCurrNoOfEmpl, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You can't hire, because " + employee_name + " is not available!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
