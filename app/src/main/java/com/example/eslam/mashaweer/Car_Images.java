package com.example.eslam.mashaweer;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Car_Images extends AppCompatActivity {


    private static final int RESULT_LOAD_IMAGE = 1;

    List<String> imageUrlList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__images);
    }


    public void Button1(View view) {
        openMultiFileChooser();
    }
    public void Button2(View view) {
    }
    public void Button3(View view) {
    }
    public void Button4(View view) {
    }


    //============Open phone Storage To Get File (Image )=====================================
    private void openMultiFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Images"),RESULT_LOAD_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            if (data.getClipData() != null){

                int totalItemSelected = data.getClipData().getItemCount();
                for (int i = 0 ; i < totalItemSelected ; i ++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String filename = getImageUrl(fileUri);

                    imageUrlList.add(filename);
                }



                //Toast.makeText(Car_Images.this,"Done",Toast.LENGTH_LONG).show();
            }
        }


    }


    public String getImageUrl(Uri uri){
        String result = null ;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result ;
        }







}
