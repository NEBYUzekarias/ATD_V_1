package com.example.android.tflitecamerademo;


import android.Manifest;
//import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
////import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
//import io.reactivex.annotations.NonNull;

public class TakeImage extends AppCompatActivity implements View.OnClickListener {

    private final int GET_SINGLE_IMAGE = 1;
    private final int CAPTURE_IMAGE = 2;

    private final int REQUEST_WRITE_EXTERNAL = 1;

    private final String CURRENT_STAGE = "stage";
    private final String CURRENT_CAPTURED_PHOTO_PATH = "capturedPhotoPath";
    private final String CURRENT_DATA_PATH = "data_path";
    private final String CURRENT_IS_CAPTURED_MODE = "isCapturedMode";

    private RadioGroup mStageRadios;
    private RadioButton[] mRadios;
    private List<CardView> mCards;
    private ImageView mSelectedImage;
    private ImageView helpImage1;
    private ImageView helpImage2;
    private Uri mSelectedUri;

    private ImageViewModel mDataViewModel;
    private Image mData;

    private boolean isCaptureMode;
    private String mCapturedPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collector);

        if (savedInstanceState != null) {

            mData.stage = savedInstanceState.getInt(CURRENT_STAGE);
            Log.i("stage", "stage: " + mData.stage);

            String path = savedInstanceState.getString(CURRENT_DATA_PATH);
            if (path != null) {
                mData.imagePath = path;
            }

            String capturedPhotoPath = savedInstanceState.getString(CURRENT_CAPTURED_PHOTO_PATH);
            if (capturedPhotoPath != null) {
                mCapturedPhotoPath = capturedPhotoPath;
            }

            isCaptureMode = savedInstanceState.getBoolean(CURRENT_IS_CAPTURED_MODE);
        }

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view -> finish());

        // set up button listeners
        findViewById(R.id.fab_add).setOnClickListener(this);
        findViewById(R.id.fab_gal).setOnClickListener(this);
        findViewById(R.id.fab_cam).setOnClickListener(this);

        mSelectedImage = (ImageView) findViewById(R.id.selected_image);
        helpImage1 = findViewById(R.id.im_data1);
        helpImage2 = findViewById(R.id.im_data2);

        helpImage1.setImageBitmap( decodeSampledBitmapFromResource(getResources(), R.drawable.image_1, 50, 50));

        helpImage2.setImageBitmap( decodeSampledBitmapFromResource(getResources(), R.drawable.image_2, 50, 50));




//        mCards.add((CardView) findViewById(R.id.card_5));

        // get view model for data source
        mDataViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        // create fresh data to save details
        mData = new Image();


    }

    private void chooseFromGallery() {
        Intent getContent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        getContent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(getContent, "Select picture"),
                GET_SINGLE_IMAGE);
    }

    private void captureImage() {
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            // Error occurred while creating the File
            Log.i("file_creation", "could not create file");
            e.printStackTrace();
        }

        // Continue only if the File was successfully created
        if (photoFile != null) {
//            Uri photoURI = FileProvider.getUriForFile(this,
//                    "com.example.android.fileprovider",
//                    photoFile);
            // Save a file path for use when returning to this Activity
            mCapturedPhotoPath = photoFile.getAbsolutePath();

            Uri photoURI = Uri.fromFile(photoFile);
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePicture, CAPTURE_IMAGE);
        } else {
            Log.i("photo", "photo: null");
        }
    }

    private File createImageFile() throws IOException {
        File image = null;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "DATA_" + timeStamp + "_";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL);
        } else {
            Log.i("my", "permission granted");
            // handle storage direcotory
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            storageDir.mkdirs();

            // create temp file
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }

        return image;
    }

    public boolean storeDataDetails() {
//        if (mData.stage == -1) {
//            Toast.makeText(this, "No Stage Selected", Toast.LENGTH_LONG)
//                    .show();
//            return false;
//        }
//

            if (mSelectedUri != null) {
                mData.imagePath = mSelectedUri.toString();

                // insert data only when it is valid, otherwise notify users
                mDataViewModel.insertImage(mData);
                return true;
            } else {
                Toast.makeText(this, "No Picture Selected", Toast.LENGTH_LONG)
                        .show();
                return false;
            }

    }

    public void resetState() {
        mSelectedUri = null;
        mCapturedPhotoPath = null;
        mData = new Image();

        mSelectedImage.setImageResource(R.drawable.img_baseball);
//        setRadio(-1);   // resets radio buttons
    }

    @Override
    public void onClick(View view) {
        int view_id = view.getId();

        switch (view_id) {
            case R.id.fab_gal:
                isCaptureMode = false;
                chooseFromGallery();
                break;

            case R.id.fab_cam:
                isCaptureMode = true;
                captureImage();
                break;

            case R.id.fab_add:
                boolean detailsStored1 = storeDataDetails();
                if (detailsStored1){
                    Toast.makeText(this,"Data add",Toast.LENGTH_LONG).show();
                    resetState();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("result", "in result");
        if (resultCode == RESULT_OK) {
            Log.i("result", "ok code: " + requestCode);
            if (requestCode == GET_SINGLE_IMAGE && data != null) {
                Uri uri = data.getData();
                //Todo: may most probably need to process to get uri that is file path, but who cares
                if (uri != null) {
                    Log.i("uri", String.format("uri: %s", uri.toString()));

                    try {
                        Bitmap bitmap =
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                        mSelectedImage.setImageBitmap(bitmap);
                        mSelectedUri = uri;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("uri", "uri: null");
                }
            } else if (requestCode == CAPTURE_IMAGE) {
                Log.i("capture", "photopath: " + mCapturedPhotoPath);

                if (mCapturedPhotoPath != null) {
                    File f = new File(mCapturedPhotoPath);
                    Uri uri = Uri.fromFile(f);

                    mSelectedUri = uri;
                    Log.i("capture", "uri: " + uri.toString());
                    mSelectedImage.setImageURI(uri);
                }
            }
        } else {
            Log.i("result", "not ok");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // since request is granted after asking to capture, let 'em capture
                    captureImage();
                } else {
                    Toast toast = Toast.makeText(this,
                            "You have to grant permission to take picture.",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem uploadItem = menu.findItem(R.id.upload_all);
        uploadItem.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                startActivity(new Intent(this, InfoActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mData != null) {
            if (mData.imagePath != null) {
                outState.putString(CURRENT_DATA_PATH, mData.imagePath);
            }

            outState.putInt(CURRENT_STAGE, mData.stage);
        } else {
            outState.putInt(CURRENT_STAGE, -1);
        }

        outState.putString(CURRENT_CAPTURED_PHOTO_PATH, mCapturedPhotoPath);
        outState.putBoolean(CURRENT_IS_CAPTURED_MODE, isCaptureMode);
    }




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

}
