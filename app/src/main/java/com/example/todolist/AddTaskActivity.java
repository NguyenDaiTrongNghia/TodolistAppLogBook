package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private EditText etTaskName, etDuration, etDescription;
    private DatePicker dpDeadline;
    private CheckBox cbCompleted;

    private int taskIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);


        etTaskName = findViewById(R.id.etTaskName);
        etDuration = findViewById(R.id.etDuration);
        etDescription = findViewById(R.id.etmDescription);
        dpDeadline = findViewById(R.id.dpDeadline);
        cbCompleted = findViewById(R.id.cbCompleted);


        Calendar c = Calendar.getInstance();
        dpDeadline.init(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),null);

        Intent intent = getIntent();
        if (intent.hasExtra("taskIndex")) {
            taskIndex = intent.getIntExtra("taskIndex", -1);

            etTaskName.setText(intent.getStringExtra("taskName"));
            etDescription.setText(intent.getStringExtra("taskDescription"));
            etDuration.setText(String.valueOf(intent.getIntExtra("taskDuration", 0)));

            boolean completed = intent.getBooleanExtra("taskCompleted", false);
            cbCompleted.setChecked(completed);


            long deadlineMillis = intent.getLongExtra("taskDeadline", 0);
            if (deadlineMillis > 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(deadlineMillis);
                dpDeadline.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onClickAddTask(View v) throws ParseException {
        String name = etTaskName.getText().toString();
        String description = etDescription.getText().toString();
        int duration = Integer.parseInt(etDuration.getText().toString());

        String dateText = dpDeadline.getDayOfMonth() + "/" + (dpDeadline.getMonth() + 1) + "/" + dpDeadline.getYear();
        Date deadline = new SimpleDateFormat("dd/MM/yyyy").parse(dateText);
        boolean completed = cbCompleted.isChecked();

        Task newTask = new Task(name, deadline, duration, description, completed);
        TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(this);

        if (taskIndex >= 0) {
            dbHelper.updateTask(taskIndex, newTask);
            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addTask(newTask);
            Toast.makeText(this, "Task Created", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}