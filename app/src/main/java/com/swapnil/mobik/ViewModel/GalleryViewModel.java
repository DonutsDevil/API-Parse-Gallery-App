package com.swapnil.mobik.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.swapnil.mobik.Model.Image;
import com.swapnil.mobik.Repositry.ImageRepositry;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel
        implements ImageRepositry.ImageDataI {

    private static final String TAG = "GalleryViewModel";

    private final MutableLiveData<ArrayList<Image>> fullSizeImageList = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Image>> thumbSizeImageList = new MutableLiveData<>();

    public GalleryViewModel(){
    }

    public void getImagesData(String url) {
        ImageRepositry.getInstance().getImagesDataFromUrl(url,this);
    }

    private void setFullSizeImageList(ArrayList<Image> fullSizeList) {
        this.fullSizeImageList.setValue(fullSizeList);
    }

    private void setThumbSizeImageList(ArrayList<Image> thumbSizeList) {
        this.thumbSizeImageList.setValue(thumbSizeList);
    }

    public LiveData<ArrayList<Image>> getFullSizeImageList() {
        return fullSizeImageList;
    }

    public LiveData<ArrayList<Image>> getThumbSizeImageList() {
        return thumbSizeImageList;
    }

    @Override
    public void onSuccess(ArrayList<Image>[] imageListArr) {
        setFullSizeImageList(imageListArr[0]);
        setThumbSizeImageList(imageListArr[1]);
    }
}
