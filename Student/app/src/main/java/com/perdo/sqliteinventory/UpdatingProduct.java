package com.perdo.sqliteinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdatingProduct extends AppCompatActivity {

    EditText eCode;
    EditText eName;
    EditText eadd;


    String stCode;
    String stName;
    String stadd;


    List<POJO> productDetails;
    OpenHelper openHelper;
    SQLiteDatabase sqLiteDatabase;

    int rowId=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);
        openHelper = new OpenHelper(this);
        sqLiteDatabase=openHelper.getWritableDatabase();

        eCode=findViewById(R.id.editCodeUpdate);
        eName=findViewById(R.id.editNameUpdate);
        eadd=findViewById(R.id.editDesUpdate);


       rowId = getIntent().getIntExtra("prodId", -1);

        Cursor cursor = sqLiteDatabase.query(DatabaseInfo.TABLE_NAME, null, DatabaseInfo._ID + " = " + rowId, null, null,null, null);
         productDetails= new ArrayList<POJO>();
         productDetails.clear();

         if(cursor != null && cursor.getCount() != 0){
             while (cursor.moveToNext()) {
                 eCode.setText(cursor.getString(cursor.getColumnIndex(DatabaseInfo.productCode)));
                 eName.setText(cursor.getString(cursor.getColumnIndex(DatabaseInfo.productName)));
                 eadd.setText(cursor.getString(cursor.getColumnIndex(DatabaseInfo.productDes)));

             }
         }else{
             Toast.makeText(this, "No Data Found!!", Toast.LENGTH_SHORT).show();
         }

    }

    public void clickUpdate(View view){
        stCode=eCode.getText().toString();
        stName=eName.getText().toString();
        stadd=eadd.getText().toString();


        if(TextUtils.isEmpty(stCode) || TextUtils.isEmpty(stName) || TextUtils.isEmpty(stadd)){
            Toast.makeText(this, "Check the Empty Fields", Toast.LENGTH_SHORT).show();
        }else {

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInfo.productCode, stCode);
            contentValues.put(DatabaseInfo.productDes, stadd);
            contentValues.put(DatabaseInfo.productName, stName);


            int updateId = sqLiteDatabase.update(DatabaseInfo.TABLE_NAME, contentValues, DatabaseInfo._ID + " = " + rowId, null);
            if (updateId != -1) {
                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();

            } else {
                Toast.makeText(this, "Something Wrong!!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        sqLiteDatabase.close();
        super.onDestroy();
    }
}