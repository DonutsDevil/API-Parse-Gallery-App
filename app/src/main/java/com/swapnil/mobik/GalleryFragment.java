package com.swapnil.mobik;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swapnil.mobik.Adapters.fullImageAdapter;
import com.swapnil.mobik.Adapters.thumbImageAdapter;
import com.swapnil.mobik.Model.Image;
import com.swapnil.mobik.Repositry.ImageRepositry;
import com.swapnil.mobik.ViewModel.GalleryViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GalleryFragment extends Fragment implements
        thumbImageAdapter.ThumbImageClickListener {
            private static final String TAG = "GalleryFragment";
    //UI
    private ViewPager2 mFullImageVP;
    private RecyclerView mThumbImageRv;

    //VAR
    private fullImageAdapter mFullImageAdapter;
    private thumbImageAdapter mThumbImageAdapter;
    private GalleryViewModel mViewModel;


    private static final String JSON_URL = "https://api.unsplash.com/photos/?client_id=238b4f660e017edb7dadc5ce864869daf68441fd58249d0f773123334f11ef9f";

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
        mViewModel.getImagesData(JSON_URL);
        init(view);
        mViewModel.getFullSizeImageList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Image>>() {
            @Override
            public void onChanged(ArrayList<Image> fullSizeImages) {
                if(!fullSizeImages.isEmpty()) {
                    Log.d(TAG, "onChanged: full "+fullSizeImages.size());
                    initFullImageRecycler(fullSizeImages);
                }
            }
        });

        mViewModel.getThumbSizeImageList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Image>>() {
            @Override
            public void onChanged(ArrayList<Image> thumbSizeImages) {
                if (!thumbSizeImages.isEmpty()) {
                    Log.d(TAG, "onChanged: thumb "+thumbSizeImages.size());
                    initThumbImageRecycler(thumbSizeImages);
                }
            }
        });

        mFullImageVP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mThumbImageRv.smoothScrollToPosition(position);
            }
        });
    }

    private void initThumbImageRecycler(ArrayList<Image>thumbImageList) {
        mThumbImageAdapter = new thumbImageAdapter(getActivity(),thumbImageList,this);
        mThumbImageRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mThumbImageRv.setAdapter(mThumbImageAdapter);
        mThumbImageRv.setHasFixedSize(true);
    }

    private void initFullImageRecycler(ArrayList<Image> fullImageList) {
        mFullImageAdapter = new fullImageAdapter(getActivity(),fullImageList);
        mFullImageVP.setAdapter(mFullImageAdapter);
    }

    private void init(View view) {
        mFullImageVP = view.findViewById(R.id.full_image_VP);
        mThumbImageRv = view.findViewById(R.id.thumb_image_rv);
    }

    @Override
    public void onClick(int position) {
        mFullImageVP.setCurrentItem(position);
    }
}