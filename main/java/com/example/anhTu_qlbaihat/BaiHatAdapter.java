package com.example.ngomanhthong_qlbaihat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BaiHatAdapter extends BaseAdapter {
    Context context;
    ArrayList<BaiHat> baiHatArrayList;
    SQLiteDatabase database;
    String DATABASE_NAME = "QLBH1.db";

    public BaiHatAdapter(Context context, ArrayList<BaiHat> baiHatArrayList) {
        this.context = context;
        this.baiHatArrayList = baiHatArrayList;
    }

    @Override
    public int getCount() {
        return baiHatArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService((context.LAYOUT_INFLATER_SERVICE));
        View row = inflater.inflate(R.layout.items,null);

        // anh xa
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        TextView txtName = (TextView) row.findViewById(R.id.textViewName);
        TextView txtSinge = (TextView) row.findViewById(R.id.textViewSinge);
        TextView txtId = (TextView) row.findViewById(R.id.textViewId);
        Button btnSua = (Button) row.findViewById(R.id.buttonSua);
        Button btnXoa = (Button) row.findViewById(R.id.buttonXoa);

        BaiHat baiHat =baiHatArrayList.get(i);
        if (baiHat.image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(baiHat.image, 0, baiHat.image.length);
            image.setImageBitmap(bitmap);
        } else {
            // Handle the case where the byte array is null
        }
        txtName.setText(baiHat.name);
        txtSinge.setText(baiHat.singe);
        txtId.setText(baiHat.id + "");

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateBaiHat.class);
                intent.putExtra("ID", baiHat.id);
                intent.putExtra("name", baiHat.name);
                intent.putExtra("singe", baiHat.singe);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có muốn xóa không");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(baiHat.id);
                    }

                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
        return row;


    }
    private void delete(int maId) {
        database = Database.initDatabase((Activity) context, DATABASE_NAME);
        database.delete("qlbh", " id = ?", new String[]{maId + ""});

        Cursor cursor = database.rawQuery("Select * from qlbh", null);
        baiHatArrayList.clear();
        while (cursor.moveToNext()) {
            byte[] removeAnh = cursor.getBlob(0);
            String name = cursor.getString(1);
            String singe = cursor.getString(2);
            int id = cursor.getInt(3);

            baiHatArrayList.add(new BaiHat(removeAnh, name, singe, id));
        }
        notifyDataSetChanged();
    }
}
