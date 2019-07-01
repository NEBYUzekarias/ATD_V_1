package com.example.android.tflitecamerademo;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.widget.Toast.makeText;

//import android.arch.lifecycle.Observer;
//import android.arch.lifecycle.ViewModelProviders;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityOptionsCompat;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
////import android.support.v7.widget.LinearLayoutManager;
////import android.support.v7.widget.RecyclerViewiew;
//import android.support.v4.util.Pair;

/**
 * Activity to upload and download photos from Firebase Storage.
 * <p>
 */
public class Main extends AppCompatActivity implements View.OnClickListener {


    public static final String DATA_ID = "data_id";
    public static final String DATA_STAGE = "data_stage";
    //private FirebaseAuth mAuth;
    static final int DIM_IMG_SIZE_X = 224;
    static final int DIM_IMG_SIZE_Y = 224;
//    private ImageView imageView;
    private static final String LOG_TAG =
            Main.class.getSimpleName();
    private static final String TAG = "Storage#MainActivity";
    private static final int RC_TAKE_PICTURE = 101;
    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    private final int imageWidthPixels = 1024;
    private final int imageHeightPixels = 768;
    public CountDrawable badge;
    public CountDrawable countDrawable;
    public Image position;
    ImageButton floatButton;
    //    ImageButton hospitalMap;
    View upload;
    Toolbar mToolbar;
    // Recycle view
//    private RecyclerView mRecyclerView;
    private ImageListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LayerDrawable icon;
    private ImageClassifier classifier;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    private Drawable reuse;
    private int count;
    private Uri mDownloadUrl = null;
    private Uri mFileUri = null;
    private int stg = 0;
    private ImageViewModel mDataViewModel;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MultiDex.install(this);
        setContentView(R.layout.activity_main);

        // start RecycleView
        EmptyRecyclerView mRecyclerView;
        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.my_recycler_view);

        mToolbar = findViewById(R.id.home_toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        View emptyView = findViewById(R.id.todo_list_empty_view);
        mRecyclerView.setEmptyView(emptyView);

        RecyclerViewClickListener listener = (view, image, id) -> {
            if (id == R.id.upload) {
                Log.d(LOG_TAG, "Button GrpcActivity !");

                Intent intent = new Intent(this, GrpcActivity.class);
                startActivity(intent);
            } else {


                Intent intent = new Intent(this, DetailActivity.class);
// Pass data object in the bundle and populate details activity.
                Pair<View, String> p1 = Pair.create((View) findViewById(R.id.im_stage), "image");

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, p1);
                startActivity(intent, options.toBundle());
//                startActivity(new Intent(this, DetailActivity.class));


            }
        };

        ImageListAdapter.ButtonListener buttonListener = (v, position1) -> {
            position = position1;
//                showNoticeDialog();
            try {
                classifier = new ImageClassifier(Main.this);

            } catch (IOException e) {
                Log.e(TAG, "Failed to initialize an image classifier.");
            }


            Uri uri = Uri.parse(position1.imagePath);
            float[] var = classifyFrame(uri);
            Log.i("HomeActivity", "onCreate: Classifier" + var.toString());
            if ((var[0] > var[2]) && (var[0] > var[4])) {

                stg = (int) var[1];
            }
            if ((var[2] > var[0]) && (var[2] > var[4])) {

                stg = (int) var[3];
            } else {
                stg = (int) var[5];
            }


            ImageRepository stage = new ImageRepository(getApplicationContext());

            stage.stage(stg, position1.id);
            // here is were i change teh listner
//                position.stage = 3;


        };


        ImageListAdapter.Delete butn = new ImageListAdapter.Delete() {
            @Override
            public void deleteOnClick(View v, Image position1) {
                position = position1;
//                showNoticeDialog();
                try {
                    classifier = new ImageClassifier(Main.this);

                } catch (IOException e) {
                    Log.e(TAG, "Failed to initialize an image classifier.");
                }


                Uri uri = Uri.parse(position1.imagePath);
                float[] var = classifyFrame(uri);


                ImageRepository stage = new ImageRepository(getApplicationContext());

                stage.stage(var[0], position1.id);
                stage.delete(position);
                // here is were i change teh listner
//                position.stage = 3;


            }
        };


        mAdapter = new ImageListAdapter(listener, buttonListener, butn);
        mRecyclerView.setAdapter(mAdapter);
        countDrawable = new CountDrawable();

        // end of RecycleView

        // get view model for data
        mDataViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        // assign observer for possible data change
        mDataViewModel.getAllImage().observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> datas) {
                // Update the cached copy of the data in the adapter.
                mAdapter.setDatas(datas);

//                if (reuse != null && reuse instanceof CountDrawable) {
//                    badge = (CountDrawable) reuse;
//                } else {
//                    badge = new CountDrawable(getApplicationContext());
//                }

                count = 0;
                if (datas != null) {
                    for (Image data : datas) {
                        if (!data.isUpload) {
                            count = count + 1;
                        }
                    }
                } else {
                    Log.d("data", "datas: null");
                }
                if (icon != null)
                    setCount(getApplicationContext(), Integer.toString(count), icon);
            }
        });

        // Click listeners

        //floating button
        floatButton = (ImageButton) findViewById(R.id.add_fab_btn);
//        hospitalMap = findViewById(R.id.hospitals);
//        imageView = findViewById(R.id.mainIm);
//        imageView.setImageBitmap( decodeSampledBitmapFromResource(getResources(), R.drawable.image_1, 50, 50));

        floatButton.setOnClickListener(this);
//        hospitalMap.setOnClickListener(this);

        // Restore instance state
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }

        onNewIntent(getIntent());

        // Local broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideProgressDialog();

                switch (intent.getAction()) {

                    // write your code here
//                    case MyUploadService.UPLOAD_COMPLETED:
//                    case MyUploadService.UPLOAD_ERROR:
//                        onUploadResultIntent(intent);
//                        break;
                }
            }
        };
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        // Check if this Activity was launched by clicking on an upload notification
//        if (intent.hasExtra(MyUploadService.EXTRA_DOWNLOAD_URL)) {
//            onUploadResultIntent(intent);
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // Register receiver for uploads and downloads

        /// sume blabal


//        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
//        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();
//      djfa

//        // Unregister download receiver
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }

    private void uploadFromUri(Uri fileUri, Image data) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any
        //  updateUI(mAuth.getCurrentUser());
        mDownloadUrl = null;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
//        ComponentName componentName = startService(new Intent(this, MyUploadService.class)
//                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
//                .putExtra(DATA_ID, data.id)
//                .putExtra(DATA_STAGE, data.stage)
//                .setAction(MyUploadService.ACTION_UPLOAD));
//
//        // Show loading spinner
//        showProgressDialog(getString(R.string.progress_uploading));
    }

    private void onUploadResultIntent(Intent intent) {
//        // Got a new intent from MyUploadService with a success or failure
//        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
//        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
    }

    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                startActivity(new Intent(this, InfoActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.hospital_map:
                startActivity(new Intent(this, HospitalActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.add_fab_btn) {
            Intent open_collector = new Intent(this, TakeImage.class);
            startActivity(open_collector);
        }
//        if(i == R.id.hospitals){
//
//            Intent open_collector = new Intent(this, HospitalActivity.class);
//            startActivity(open_collector);
//        }


    }
//
//    public void showNoticeDialog() {
//        // Create an instance of the dialog fragment and show it
//        DialogFragment dialog = new DeleteDialogFragment();
//        dialog.show(getFragmentManager(),"DeleteDialogFragment");
//    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.upload_all);
        menuItem.setOnMenuItemClickListener(menuItem1 -> {
            List<Image> datas = mDataViewModel.getAllImage().getValue();

            if (datas != null) {
                for (Image data : datas) {
                    if (data.isUpload) {
                        makeText(getApplicationContext(),
                                "Already uploaded2", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Uri uri;
                        if (data.imagePath.startsWith("file:///")) {
                            File file = new File(data.imagePath.substring(8));
                            uri = Uri.fromFile(file);
                        } else {
                            uri = Uri.parse(data.imagePath);
                        }

                        uploadFromUri(uri, data);
                    }
                }
            } else {
                Log.d("data", "datas: null");
            }

            return true;
        });

        icon = (LayerDrawable) menuItem.getIcon();

        List<Image> datas = mDataViewModel.getAllImage().getValue();
        count = 0;
        if (datas != null) {
            for (Image data : datas) {
                if (!data.isUpload) {
                    count = count + 1;
                }
            }
        } else {
            Log.d("data", "datas: null");
        }
        setCount(getApplicationContext(), Integer.toString(this.count), icon);

        return true;
    }


//    public void onDialogPositiveClick(DialogFragment dialog ) {
//        // User touched the dialog's positive button
//
//
//        mDataViewModel.deleteImage(position);
//
//    }
//
//    @Override
//    public void onDialogNegativeClick(DialogFragment dialog) {
//        // User touched the dialog's negative button
//    }

    public void setCount(Context context, String count, LayerDrawable icon) {

        reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    public boolean isOnline() {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
        return true;

    }

    public void launchGrpcActivity(View view) {

        Intent intent = new Intent(this, GrpcActivity.class);
        startActivity(intent);


        Log.d(LOG_TAG, "Button2 clicked!");
    }

    public void launchSecondActivity(View view) {

        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);


        Log.d(LOG_TAG, "Button clicked!");
    }

    @Override
    public void onDestroy() {
        classifier.close();
        super.onDestroy();
    }


    /**
     * Classifies a frame from the preview stream.
     */
    private float[] classifyFrame(Uri imageUri) {
        if (classifier == null || Main.this == null) {
            Log.e(TAG, "Image classifier has not been initialized; Skipped.");
            float[] a = {0, 0};
            return a;
        }
        Bitmap bitmap =
                null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            bitmap = getResizedBitmap(bitmap, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y);

        } catch (IOException e) {
            e.printStackTrace();
        }
        float[] textToShow = classifier.classifyFrame(bitmap);

        Log.d(LOG_TAG, textToShow + "neba");


        bitmap.recycle();

        return textToShow;

    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


}
