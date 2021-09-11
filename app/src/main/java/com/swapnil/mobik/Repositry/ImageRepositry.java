package com.swapnil.mobik.Repositry;


import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.swapnil.mobik.Model.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageRepositry {

    private static final String TAG = "ImageRespositry";
    private static ImageRepositry mInstance;

    private  ArrayList<Image> fullImageList = new ArrayList<>();
    private  ArrayList<Image> thumbImageList = new ArrayList<>();
    private ImageDataI mCallback;

    private ImageRepositry() {}

    public static ImageRepositry getInstance() {
        if (mInstance == null) {
            mInstance = new ImageRepositry();
        }
        return mInstance;
    }

    private ArrayList<Image> getFullImageList() {
        return fullImageList;
    }

    private void setFullImageList(ArrayList<Image> fullImageList) {
        this.fullImageList.addAll(fullImageList);
        Log.d(TAG, "setFullImageList: "+this.fullImageList.size());
    }

    private ArrayList<Image> getThumbImageList() {
        return thumbImageList;
    }

    private void setThumbImageList(ArrayList<Image> thumbImageList) {
        this.thumbImageList.addAll(thumbImageList);
        Log.d(TAG, "setThumbImageList: "+this.thumbImageList.size());
    }

    public void getImagesDataFromUrl(String url, ImageDataI mCallback) {
        new ImageAsync().execute(url);
        this.mCallback = mCallback;
    }
    class ImageAsync extends AsyncTask<String,Void,ArrayList<Image>[]> {

        @Override
        protected ArrayList<Image>[] doInBackground(String... url) {
            if (url.length > 0 && url[0].length() > 0) {
                return JSONParser.fetchData(url[0]);
            }
            return null;
        }

        /**
         * @param arrayLists  index 0 contains list of Full size image
         *                    index 1 contains list of thumb size image
         * */
        @Override
        protected void onPostExecute(ArrayList<Image>[] arrayLists) {
            super.onPostExecute(arrayLists);
            if (arrayLists[0].size() <= 0 || arrayLists[1].size() <= 0) {
                Log.d(TAG, "onPostExecute: return arraylist for full size is "
                        + arrayLists[0].size()+" , thumb size is "+arrayLists[1].size());
                return;
            }
            setFullImageList(arrayLists[0]);
            setThumbImageList(arrayLists[1]);
            ArrayList<Image>[] imageArr = new ArrayList[2];
            imageArr[0] = getFullImageList();
            imageArr[1] = getThumbImageList();
            mCallback.onSuccess(imageArr);
        }
    }

    public interface ImageDataI {
        void onSuccess(ArrayList<Image>[] imageListArr);
    }
}
