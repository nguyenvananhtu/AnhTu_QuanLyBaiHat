package com.example.ngomanhthong_qlbaihat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchBaiHat extends AppCompatActivity {
    ListView lstSearch;
    String DATABASE_NAME = "QLBH1.db";
    SQLiteDatabase database;
    ArrayList<BaiHat> list;
    BaseAdapter adapter;
    Button quayLai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bai_hat);

        lstSearch = (ListView) findViewById(R.id.listSearch);
        list = new ArrayList<>();
        adapter = new BaiHatAdapter(SearchBaiHat.this, list);
        lstSearch.setAdapter(adapter);
        database = Database.initDatabase(SearchBaiHat.this, DATABASE_NAME);
        Intent receivedIntent = getIntent();
        String searchString = receivedIntent.getStringExtra("searchString");
        Cursor cursor = database.rawQuery("SELECT * FROM qlbh WHERE name LIKE '%"+searchString+"%'", null);

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

        quayLai = (Button) findViewById(R.id.buttonQuayLai);
        quayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchBaiHat.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}