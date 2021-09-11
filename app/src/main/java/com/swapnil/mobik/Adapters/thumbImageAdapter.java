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

public class thumbImageAdapter extends RecyclerView.Adapter<thumbImageAdapter.Holder> {

    private Context mContext;
    private ArrayList<Image> mImagesList;
    private ThumbImageClickListener mCallBack;

    public thumbImageAdapter(Context context, ArrayList<Image> imagesList, ThumbImageClickListener mCallBack) {
        this.mContext = context;
        this.mImagesList = imagesList;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumb_image_list_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Image currentImage = mImagesList.get(position);
        Glide.with(mContext)
                .load(currentImage.getUrl())
                .centerCrop()
                .into(holder.mThumbImageIV);
    }

    @Override
    public int getItemCount() {
        return mImagesList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView mThumbImageIV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mThumbImageIV = itemView.findViewById(R.id.thumb_image_iv);
            mThumbImageIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onClick(getLayoutPosition());
                }
            });
        }
    }

    public interface ThumbImageClickListener {
        void onClick(int position);
    }
}
