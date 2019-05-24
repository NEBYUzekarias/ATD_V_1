package com.example.android.tflitecamerademo;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ImageRepository {

    private ImageDao mImageDao;
    private LiveData<List<Image>> mAllImages;


    ImageRepository(Application application) {
        ImageRoomDatabase db = ImageRoomDatabase.getDatabase(application);
        mImageDao = db.imageDao();
        mAllImages = mImageDao.getAll();
    }



    LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }


    public void insert (Image image) {
        new insertAsyncTask(mImageDao).execute(image);
    }


    private static class insertAsyncTask extends AsyncTask<Image, Void, Void> {

        private ImageDao mAsyncTaskDao;

        insertAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Image... images) {
            mAsyncTaskDao.insertImage(images[0]);
            return null;
        }
    }


    public void delete(Image image){
        new DeleteAsyncTask(mImageDao).execute(image);
    }
    private static class DeleteAsyncTask extends AsyncTask<Image, Void, Void> {
        private ImageDao mAsyncTaskDao;

        DeleteAsyncTask(ImageDao imageDao) {
            mAsyncTaskDao = imageDao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mAsyncTaskDao.deletImage(images[0]);
            return null;
        }
    }

}
