package com.example.todo20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo20.Adapter.ToDoAdapter;
import com.example.todo20.DataHolde.TaskDataMode;
import com.example.todo20.DatabaseManager.DbHelperClass;
import com.example.todo20.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    ArrayList<TaskDataMode> list;
    Cursor cursor;
    ToDoAdapter adapter;
    private DbHelperClass dbHelperClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //------------Adding Task In The Database-----------//
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.cusum_dialog_for_adding_task);
                dialog.show();
                dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtTask=dialog.findViewById(R.id.txtTask);
                        String task=txtTask.getText().toString();
                        if(!task.isEmpty()){
                            dialog.dismiss();
                            boolean errCode=true;
                            try {
                                errCode= new DbHelperClass(MainActivity.this).addDataToSQL(task,0);
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if(errCode) {
//                                Toast.makeText(MainActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                                list.add(0,new TaskDataMode(task,0,list.size()));
                                adapter.notifyItemInserted(0);
                            }
                            else
                                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Insert Some Task", Toast.LENGTH_SHORT).show();
                        }

                    }
                });//end of Second Button i.e Save Button
            }
        });//end of first Button i.e FloatingActionButton
        //----------------------------------------------------------//
        //------Showing the Task in the RecyclerView------//
        //Steeping the recycler view
        recyclerView=(RecyclerView) binding.recTodo;
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        list=new ArrayList<>();
        //creating the Cursor for reading all the data from the database
        try {
            cursor=new DbHelperClass(MainActivity.this).getAllData();
            while (cursor.moveToNext()){
                list.add(new TaskDataMode(cursor.getString(1), cursor.getInt(2), cursor.getInt(0) ));
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        dbHelperClass=new DbHelperClass(MainActivity.this);
        //setting up the Adapter
        adapter=new ToDoAdapter(MainActivity.this,list,dbHelperClass);
        recyclerView.setAdapter(adapter);

    }
}