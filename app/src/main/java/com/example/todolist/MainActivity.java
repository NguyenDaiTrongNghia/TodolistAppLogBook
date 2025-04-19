package com.example.todolist;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Task> taskList = new ArrayList<Task>();
    TaskDatabaseHelper dbHelper = new TaskDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onclickAdd(View v){
        Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(i);
    }

    protected void onStart(){
        super.onStart();
        ListView lv = findViewById(R.id.listView);
        taskList = dbHelper.getAllTasks();
        TaskAdapter adapter = new TaskAdapter(this,taskList);
        lv.setAdapter(adapter);
    }

    public class TaskAdapter extends ArrayAdapter<Task>{
        public TaskAdapter(Context context, ArrayList<Task> tasks) {super(context,0,tasks);}
        public View getView(int position, View convertView, ViewGroup parent) {

            Task t = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item,parent,false);
            TextView tvTaskName = (TextView)convertView.findViewById(R.id.tvTaskName);
            TextView tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);
            TextView tvDeadLine = (TextView)convertView.findViewById(R.id.tvDeadline);
            TextView tvDescriptions = (TextView)convertView.findViewById(R.id.tvDescriptions);
            Button btnEdit = convertView.findViewById(R.id.btnEdit);
            Button btnDelete = convertView.findViewById(R.id.btnDelete);

            tvTaskName.setText(t.name);
            tvDeadLine.setText(t.deadLine.toString().substring(0,10));
            tvDuration.setText(String.valueOf(t.duration));
            tvDescriptions.setText(String.valueOf(t.description));

            CheckBox cbCompleteStatus = convertView.findViewById(R.id.cbCompleteStatus);
            cbCompleteStatus.setChecked(t.completed);

            cbCompleteStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                t.completed = isChecked;
                dbHelper.updateTask(t.getId(), t);
            });

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra("taskIndex", t.getId());
                intent.putExtra("taskName", t.name);
                intent.putExtra("taskDeadline", t.deadLine.getTime());
                intent.putExtra("taskDuration", t.duration);
                intent.putExtra("taskDescription", t.description);
                intent.putExtra("taskCompleted", t.completed);

                getContext().startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteTask(t.getId());
                MainActivity.taskList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }
    }
}