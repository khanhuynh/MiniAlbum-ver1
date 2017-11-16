package com.example.huynhxuankhanh.minialbum.gallary;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HUYNHXUANKHANH on 11/2/2017.
 */

public class LoadGallary {

    // declare some tool variable for querying database.
    private ContentResolver contentResolver;
    private Cursor cursor;
  //  private List<String> listPathImage;
    private ArrayList<InfoImage> listImage;
    private final String[] projection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA, // path file
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // folder name
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_TAKEN, // date taken
    };
    public LoadGallary(){
        contentResolver = null;
        cursor = null;
        listImage = new ArrayList<InfoImage>();
    }
    public ArrayList<InfoImage> getListImage() {
        return listImage;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    public void query_PathImage(Uri url){
        cursor = contentResolver.query(url,projection,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // load data to temp.
                int iD = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                String pathFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String nameFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String nameBucket = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long sizeFile = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                Integer date = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date);
                //set item for list
                InfoImage infoImage = new InfoImage(iD,sizeFile, pathFile, nameFile, nameBucket, cal);
                if (infoImage != null)
                    listImage.add(infoImage);
                cursor.moveToNext();
            }
        }
    }
    public InfoImage getInfoImage(int position){
        return listImage.get(position);
    }
}
