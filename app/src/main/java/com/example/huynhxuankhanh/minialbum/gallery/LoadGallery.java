package com.example.huynhxuankhanh.minialbum.gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HUYNHXUANKHANH on 11/2/2017.
 */

public class LoadGallery {
    private final String[] projection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA, // path file
            MediaStore.Images.ImageColumns.DISPLAY_NAME, //file name
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, // folder name
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.ORIENTATION // date taken
    };
    // declare some tool variable for querying database.
    private ContentResolver contentResolver;
    private Cursor cursor;
    //  private List<String> listPathImage;
    private ArrayList<InfoImage> listImage;

    public LoadGallery() {
        contentResolver = null;
        cursor = null;
        listImage = new ArrayList<>();
    }

    public ArrayList<InfoImage> getListImage() {
        return listImage;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void queryPathImage(Uri url) {
        cursor = contentResolver.query(url, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            while (!cursor.isFirst()) {
                // load data to temp.
                int iD = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                String pathFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String nameFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String nameBucket = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long sizeFile = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                Long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                Date tempDate = new Date();
                tempDate.setTime(date);

                //set item for list
                String orientation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
                InfoImage infoImage = new InfoImage(iD, sizeFile, pathFile, nameFile, nameBucket, tempDate.toString(), orientation);
                listImage.add(infoImage);
                cursor.moveToPrevious();
            }
        }
    }

    public ArrayList<InfoFolder> getListBucketName() {
        ArrayList<String> tempNameFolder = new ArrayList<>();
        ArrayList<InfoFolder> listFolder = new ArrayList<>();
        InfoFolder infoFolder;
        for (int i = 0; i < listImage.size(); ++i) {
            if (tempNameFolder.contains(listImage.get(i).getNameBucket())) { // neu ma da chứa
                int pos = tempNameFolder.indexOf(listImage.get(i).getNameBucket());
                listFolder.get(pos).getListImage().add(listImage.get(i));
            } else { // neu chưa chứa
                tempNameFolder.add(listImage.get(i).getNameBucket());
                infoFolder = new InfoFolder();
                infoFolder.setNameBucket(listImage.get(i).getNameBucket());
                infoFolder.getListImage().add(listImage.get(i));
                listFolder.add(infoFolder);
            }
        }
        return listFolder;
    }

    //get <numberImageUpdate> latest image and add to current list
    public void updateLastItem(Uri url, int numberImageUpdate) {
        cursor = contentResolver.query(url, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            for (int i = 0; i < numberImageUpdate; ++i) {
                int iD = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                String pathFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String nameFile = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String nameBucket = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                long sizeFile = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)));
                Long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                Date tempDate = new Date();
                tempDate.setTime(date);

                //set item for list
                String orientation = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
                InfoImage infoImage = new InfoImage(iD, sizeFile, pathFile, nameFile, nameBucket, tempDate.toString(), orientation);
                listImage.add(0, infoImage);
                cursor.moveToPrevious();
            }
        }
    }

}
