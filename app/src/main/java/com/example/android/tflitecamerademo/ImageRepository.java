package com.example.android.tflitecamerademo;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ImageRepository {

    private ImageDao mImageDao;
    private LiveData<List<Image>> mAllImages;


    ImageRepository(Context application) {
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




    public void stage(int stage, int data_id) {
        new UpdateUploadStatusTask(mImageDao).execute(new UpdateParams(data_id, stage));
    }

    public static class UpdateParams {
        int id;
        int stage;

        public UpdateParams(int id, int stage) {
            this.id = id;
            this.stage = stage;
        }
    }

    private static class UpdateUploadStatusTask extends AsyncTask<UpdateParams, Void, Void> {
        private ImageDao mAsyncTaskDao;

        UpdateUploadStatusTask(ImageDao dataDao) {
            mAsyncTaskDao = dataDao;
        }

        @Override
        protected Void doInBackground(UpdateParams... params) {
            mAsyncTaskDao.stage(params[0].stage, params[0].id);
            return null;
        }
    }

}
