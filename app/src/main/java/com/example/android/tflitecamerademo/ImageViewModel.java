package com.example.android.tflitecamerademo;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ImageViewModel extends AndroidViewModel {

    private ImageRepository mRepository;
    private LiveData<List<Image>> mAllImage;


    public ImageViewModel (Application application) {
        super(application);
        mRepository = new ImageRepository(application);
        mAllImage = mRepository.getAllImages();
    }


    LiveData<List<Image>> getAllImage() { return mAllImage; }
    public void insertImage(Image image) { mRepository.insert(image); }
    public void deleteImage(Image image) { mRepository.delete(image);}



}
