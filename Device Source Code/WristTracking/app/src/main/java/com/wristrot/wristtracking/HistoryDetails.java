package com.wristrot.wristtracking;

import adapter.HistoryAdapter;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import database.DatabaseHelper;
import model.DataModel;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wristtracking.R;

import org.w3c.dom.Text;

import java.util.Currency;
import java.util.List;

public class HistoryDetails extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    HistoryAdapter adapter;
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    List<DataModel> list;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
       recyclerView =  findViewById(R.id.recyclerView);
        dbHelper = new DatabaseHelper(this);
        title.setText("History Details");
        list = dbHelper.getData();
        if(list.size()>0) {
            adapter = new HistoryAdapter(list,this);
        }
        else{
            Toast.makeText(this, "No History Found", Toast.LENGTH_SHORT).show();
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:{
                finish();
                break;
            }
        }
    }

}