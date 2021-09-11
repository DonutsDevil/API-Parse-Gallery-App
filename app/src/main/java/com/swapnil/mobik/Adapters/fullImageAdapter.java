package com.swapnil.mobik.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.swapnil.mobik.Model.Image;
import com.swapnil.mobik.R;

import java.util.ArrayList;

public class fullImageAdapter extends RecyclerView.Adapter<fullImageAdapter.Holder> {
    private Context mContext;
    private ArrayList<Image> mImagesList;

    public fullImageAdapter(Context context, ArrayList<Image> imagesList) {
        this.mContext = context;
        this.mImagesList = imagesList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_image_list_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Image currentImage = mImagesList.get(position);
        Glide.with(mContext)
                .load(currentImage.getUrl())
                .centerCrop()
                .into(holder.mFullImageIV);
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView mFullImageIV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mFullImageIV = itemView.findViewById(R.id.full_image_iv);
        }
    }
}
