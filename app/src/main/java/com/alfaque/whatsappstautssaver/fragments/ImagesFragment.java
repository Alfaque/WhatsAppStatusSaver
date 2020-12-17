package com.alfaque.whatsappstautssaver.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alfaque.whatsappstautssaver.R;
import com.alfaque.whatsappstautssaver.adapter.ImageAdapter;
import com.alfaque.whatsappstautssaver.databinding.FragmentImagesBinding;
import com.alfaque.whatsappstautssaver.helper_classes.Constants;
import com.alfaque.whatsappstautssaver.models.StatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesFragment extends Fragment {


    FragmentImagesBinding fragmentImagesBinding;
    List<StatusModel> list = new ArrayList<>();
    ImageAdapter imageAdapter;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        fragmentImagesBinding = FragmentImagesBinding.bind(view);
        return fragmentImagesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageAdapter = new ImageAdapter(getActivity(), list);
        fragmentImagesBinding.imageRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentImagesBinding.imageRecyclerview.setAdapter(imageAdapter);

        getStatuses();

    }

    public static final String TAG = "ImagesFragment";

    private void getStatuses() {
        if (Constants.STATUS_DIR.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    File[] statusFiles = Constants.STATUS_DIR.listFiles();
                    if (statusFiles != null && statusFiles.length > 0) {

                        list = new ArrayList<>();
                        for (File file : statusFiles) {

                            StatusModel statusModel = new StatusModel(file, file.getName(), file.getAbsolutePath());
                            statusModel.setThumbNail(getThumNail(statusModel));
                            if (!statusModel.isVideo()) {
                                list.add(statusModel);
                            }
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageAdapter = new ImageAdapter(getActivity(), list);
                                fragmentImagesBinding.imageRecyclerview.setAdapter(imageAdapter);
                                imageAdapter.notifyDataSetChanged();
                            }
                        });


                    } else {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Dir is empty", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).start();

        } else {
            Toast.makeText(getActivity(), "WhatsApp Dir not found", Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getThumNail(StatusModel statusModel) {

        if (statusModel.isVideo()) {
            return ThumbnailUtils.createVideoThumbnail(statusModel.getFile().getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        } else {
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath()),
                    Constants.ThumbNailSize, Constants.ThumbNailSize);
        }

    }
}