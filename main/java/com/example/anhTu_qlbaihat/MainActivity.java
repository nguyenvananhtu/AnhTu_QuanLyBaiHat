package com.example.ngomanhthong_qlbaihat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lstBaiHat;
    String DATABASE_NAME = "QLBH1.db";
    SQLiteDatabase database;
    ArrayList<BaiHat> list;
    BaseAdapter adapter;
    Button theBaiHat, btnSearch;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstBaiHat = (ListView) findViewById(R.id.listview_BaiHat);
        list = new ArrayList<>();
        adapter = new BaiHatAdapter(MainActivity.this, list);
        lstBaiHat.setAdapter(adapter);
        database = Database.initDatabase(MainActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM qlbh", null);

        list.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            byte[] image = cursor.getBlob(0);
            String name = cursor.getString(1);
            String singe = cursor.getString(2);
            int id = cursor.getInt(3);

            list.add(new BaiHat(image, name, singe,id));
        }
        adapter.notifyDataSetChanged();
        theBaiHat = (Button) findViewById(R.id.buttonThem);
        theBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertBaiHat.class);
                startActivity(intent);
            }
        });
        search = (EditText) findViewById(R.id.editTextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch) ;
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchBaiHat.class);
                intent.putExtra("searchString",search.getText().toString());
                startActivity(intent);

            }
        });

    }
}