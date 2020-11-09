package org.kuliah.sqlitecrudexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "mydatabase";

    SQLiteDatabase mDatabase;

    EditText editTextName, editTextSalary;
    Spinner spinnerDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTable();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSalary = (EditText) findViewById(R.id.editTextSalary);
        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);

        findViewById(R.id.buttonAddEmployee).setOnClickListener(this);
        findViewById(R.id.textViewViewEmployees).setOnClickListener(this);
    }

    private void createTable(){
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS employee (\n" +
                        "    `_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    `name` varchar(200) NOT NULL,\n" +
                        "    `department` varchar(200) NOT NULL,\n" +
                        "    `joiningdate` datetime NOT NULL,\n" +
                        "    `salary` double NOT NULL\n" +
                        ");"
        );
    }

    private void addEmployee(){
        String name = editTextName.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String dept = spinnerDepartment.getSelectedItem().toString();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return;
        }

        if (salary.isEmpty() || Integer.parseInt(salary) <= 0) {
            editTextSalary.setError("Please enter salary");
            editTextSalary.requestFocus();
            return;
        }

        String insertSQL = "INSERT INTO employee \n" +
                "(name, department, joiningdate, salary) \n" +
                "VALUES \n" +
                "(?, ?, ?, ?);";

        mDatabase.execSQL(insertSQL, new String[]{name, dept, joiningDate, salary});

        Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddEmployee:

                addEmployee();

                break;
            case R.id.textViewViewEmployees:

                startActivity(new Intent(this, EmployeeActivity.class));

                break;
        }
    }
}