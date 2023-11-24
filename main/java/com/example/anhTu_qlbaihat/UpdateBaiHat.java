package com.example.ngomanhthong_qlbaihat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

public class UpdateBaiHat extends AppCompatActivity {
    EditText txtName, txtSinge;
    Button btnChonHinh, btnChupHinh, btnSua, btnQuayLai;
    ImageView anhSua;
    SQLiteDatabase database;
    String DATABASE_NAME = "QLBH1.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOSE_PHOTO = 321;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bai_hat);

        addControl();
        loadData();
        addEvent();
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
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateBaiHat.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addControl() {
        txtName = (EditText) findViewById(R.id.editTextName);
        txtSinge = (EditText) findViewById(R.id.editTextSinge);
        btnChonHinh = (Button) findViewById(R.id.buttonChonHinh);
        btnChupHinh = (Button) findViewById(R.id.buttonChupHinh);
        btnSua = (Button) findViewById(R.id.buttonSua);
        btnQuayLai = (Button) findViewById(R.id.buttonQuayLai);
        anhSua = (ImageView) findViewById(R.id.imageSua);
    }

    public void update(){
        String name = txtName.getText().toString();
        String singe = txtSinge.getText().toString();
        byte[] image = getByteArrayFromImageView(anhSua);
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image);
        contentValues.put("name", name);
        contentValues.put("singe", singe);
        SQLiteDatabase database = Database.initDatabase(UpdateBaiHat.this, DATABASE_NAME);
        database.update("qlbh", contentValues, "id = ?", new String[]{id + ""});
        Intent intent = new Intent(UpdateBaiHat.this, MainActivity.class);
        startActivity(intent);
    }
    public void loadData(){
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);
        if (id != -1) {
            database = Database.initDatabase(UpdateBaiHat.this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("Select * From qlbh Where id = ?", new String[]{id + ""});
            cursor.moveToFirst();
            byte[] anh = cursor.getBlob(0);
            String name = cursor.getString(1);
            String singe = cursor.getString(2);
            if (anh != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
                anhSua.setImageBitmap(bitmap);
            }
            txtName.setText(name);
            txtSinge.setText(singe);

        }
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
                    anhSua.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                anhSua.setImageBitmap(bitmap);
            }
        }
    }


}