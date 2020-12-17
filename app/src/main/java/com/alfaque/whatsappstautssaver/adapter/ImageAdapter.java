package com.alfaque.whatsappstautssaver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfaque.whatsappstautssaver.databinding.ItemLayoutBinding;
import com.alfaque.whatsappstautssaver.models.StatusModel;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyImageHolder> {
    Context context;
    List<StatusModel> list;
    OnImageClicked onImageClicked;


    public ImageAdapter(Context context, List<StatusModel> list) {
        this.context = context;
        this.list = list;
        this.onImageClicked = (OnImageClicked) context;
    }

    @NonNull
    @Override
    public MyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyImageHolder(ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageHolder holder, int position) {
        StatusModel statusModel = list.get(position);

        holder.itemLayoutBinding.itemImageView.setImageBitmap(statusModel.getThumbNail());

        holder.itemLayoutBinding.itemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClicked.onImageDownloadClickedListener(list.get(position));
            }
        });

        holder.itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClicked.onViewImageClickedListener(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<StatusModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class MyImageHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding itemLayoutBinding;

        public MyImageHolder(@NonNull ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }
    }

    public interface OnImageClicked {
        public void onImageDownloadClickedListener(StatusModel statusModel);

        public void onViewImageClickedListener(StatusModel statusModel);
    }
}
