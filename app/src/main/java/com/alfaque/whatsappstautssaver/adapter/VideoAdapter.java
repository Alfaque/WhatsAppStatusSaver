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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyVideoHolder> {
    Context context;
    List<StatusModel> list;
    OnVideoClicked onVideoClicked;

    public VideoAdapter(Context context, List<StatusModel> list) {
        this.context = context;
        this.list = list;
        this.onVideoClicked = (OnVideoClicked) context;
    }

    @NonNull
    @Override
    public MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyVideoHolder(ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyVideoHolder holder, int position) {

        holder.itemLayoutBinding.itemImageView.setImageBitmap(list.get(position).getThumbNail());
        holder.itemLayoutBinding.itemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoClicked.onVideoDownloadClickedListener(list.get(position));
            }
        });
        holder.itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVideoClicked.onViewVideoClickedListener(list.get(position));
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

    public class MyVideoHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding itemLayoutBinding;

        public MyVideoHolder(@NonNull ItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }
    }

    public interface OnVideoClicked {
        public void onVideoDownloadClickedListener(StatusModel statusModel);

        public void onViewVideoClickedListener(StatusModel statusModel);
    }
}
