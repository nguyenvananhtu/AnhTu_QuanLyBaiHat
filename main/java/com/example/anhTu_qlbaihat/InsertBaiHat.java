package com.example.ngomanhthong_qlbaihat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InsertBaiHat extends AppCompatActivity {
    EditText txtName, txtSinge, txtId;
    Button btnChonHinh, btnChupHinh, btnThem, btnQuayLai;
    ImageView anhThem;
    SQLiteDatabase database;
    String DATABASE_NAME = "QLBH1.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOSE_PHOTO = 321;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_bai_hat);
        addControl();
        addEvent();
    }

    public void addControl() {
        txtName = (EditText) findViewById(R.id.editTextName);
        txtSinge = (EditText) findViewById(R.id.editTextSinge);
        txtId = (EditText) findViewById(R.id.editTextId) ;
        btnChonHinh = (Button) findViewById(R.id.buttonChonHinh);
        btnChupHinh = (Button) findViewById(R.id.buttonChupHinh);
        btnThem = (Button) findViewById(R.id.buttonThem);
        btnQuayLai = (Button) findViewById(R.id.buttonQuayLai);
        anhThem = (ImageView) findViewById(R.id.imageAdd);
    }
    public void addEvent() {
        btnChupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePhoto();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertBaiHat.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void add() {
        String name = txtName.getText().toString();
        String singe = txtSinge.getText().toString();
        String id = txtId.getText().toString();
        byte[] anh = getByteArrayFromImageView(anhThem);
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", anh);
        contentValues.put("name", name);
        contentValues.put("singe", singe);
        contentValues.put("id", id);
        SQLiteDatabase database = Database.initDatabase(InsertBaiHat.this, DATABASE_NAME);
        database.insert("qlbh", null, contentValues);
        Intent intent = new Intent(InsertBaiHat.this, MainActivity.class);
        startActivity(intent);
    }

    public byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    public void chosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    anhThem.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                anhThem.setImageBitmap(bitmap);
            }
        }
    }
}