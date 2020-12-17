package com.alfaque.whatsappstautssaver.fragments;

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
import com.alfaque.whatsappstautssaver.adapter.VideoAdapter;
import com.alfaque.whatsappstautssaver.databinding.FragmentVideosBinding;
import com.alfaque.whatsappstautssaver.helper_classes.Constants;
import com.alfaque.whatsappstautssaver.models.StatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideosFragment extends Fragment {

    FragmentVideosBinding fragmentVideosBinding;
    List<StatusModel> list = new ArrayList<>();
    VideoAdapter videoAdapter;
    Handler handler = new Handler();
    public static final String TAG = "VideosFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        fragmentVideosBinding = FragmentVideosBinding.bind(view);
        return fragmentVideosBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoAdapter = new VideoAdapter(getActivity(), list);
        fragmentVideosBinding.videoRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentVideosBinding.videoRecyclerview.setAdapter(videoAdapter);
        getStatuses();

    }

    private void getStatuses() {
        Log.d(TAG, "getStatuses: ");
        if (Constants.STATUS_DIR.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    File[] statusFiles = Constants.STATUS_DIR.listFiles();

                    if (statusFiles != null && statusFiles.length > 0) {
                        Log.d(TAG, "run: 1");
                        for (File file : statusFiles) {

                            StatusModel statusModel = new StatusModel(file, file.getName(), file.getAbsolutePath());
                            statusModel.setThumbNail(getThumNail(statusModel));
                            Log.d(TAG, "run: statusModel -> " + statusModel.toString());
                            if (statusModel.isVideo()) {
                                list.add(statusModel);
                                Log.d(TAG, "run: 2");
                            }
                        }

                        Log.d(TAG, "run: VideoList -> " + list.size());
                        Log.d(TAG, "run: 3");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run: 4");
                                videoAdapter = new VideoAdapter(getActivity(), list);
                                fragmentVideosBinding.videoRecyclerview.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Dir is empty", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "run: statusFiles is null");
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