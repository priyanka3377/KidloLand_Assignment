package com.example.kidloland_assignment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TABLE_NAME = "users";
    private static final String COL_NAME = "name";
    private static final String COL_DOB = "dob";
    private static final String COL_EMAIL = "email";

    private SQLiteDatabase db;

    EditText edtName, edtDob, edtEmail;

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
        edtEmail = findViewById(R.id.edtEmail);
        btnSubmit = findViewById(R.id.btnSubmit);

        edtDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format("%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        edtDob.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();
        });

        db = openOrCreateDatabase("UserDB", MODE_PRIVATE, null);
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COL_NAME + " TEXT, " +
                        COL_DOB + " TEXT, " +
                        COL_EMAIL + " TEXT)"
        );

        btnSubmit.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String dob = edtDob.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            boolean hasError = false;

            if (TextUtils.isEmpty(name)) {
                edtName.setError("Name is required");
                edtName.requestFocus();
                hasError = true;
            }

            if (TextUtils.isEmpty(dob)) {
                edtDob.setError("Please select your date of birth");
                edtDob.requestFocus();
                hasError = true;
            }

            if (TextUtils.isEmpty(email)) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                hasError = true;
            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Enter a valid email address");
                edtEmail.requestFocus();
                hasError = true;
            }

            if (hasError) return;

            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_DOB, dob);
            values.put(COL_EMAIL, email);
            db.insert(TABLE_NAME, null, values);

            Intent intent = new Intent(MainActivity.this, UserDetails.class);
            intent.putExtra("name", name);
            intent.putExtra("dob", dob);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
    }
}