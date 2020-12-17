package com.alfaque.whatsappstautssaver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alfaque.whatsappstautssaver.adapter.ImageAdapter;
import com.alfaque.whatsappstautssaver.adapter.PagerAdapter;
import com.alfaque.whatsappstautssaver.R;
import com.alfaque.whatsappstautssaver.adapter.VideoAdapter;
import com.alfaque.whatsappstautssaver.databinding.ActivityMainBinding;
import com.alfaque.whatsappstautssaver.helper_classes.Constants;
import com.alfaque.whatsappstautssaver.models.StatusModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.OnImageClicked, VideoAdapter.OnVideoClicked {
    ActivityMainBinding activityMainBinding;
    PagerAdapter pagerAdapter;
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        titles.add("Images");
        titles.add("Videos");
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), titles);
        TabLayout tabLayout = activityMainBinding.tablayout;
        activityMainBinding.tablayout.addTab(activityMainBinding.tablayout.newTab().setText("Images"));
        activityMainBinding.tablayout.addTab(activityMainBinding.tablayout.newTab().setText("Videos"));
        activityMainBinding.viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(activityMainBinding.viewpager);


    }

    private void downloadFile(StatusModel statusModel) {
        File file = new File(Constants.App_DIR);


        if (!file.exists()) {
            file.mkdirs();
        }


        File destinationFile = new File(file + File.separator + statusModel.getTitle());
        if (destinationFile.exists()) {
            destinationFile.delete();
        }

        try {
            copyFile(statusModel.getFile(), destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Downloaded: " + destinationFile.getPath(), Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(destinationFile));
        sendBroadcast(intent);
    }

    private void copyFile(File file, File destinationFile) throws IOException {
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }


        if (!destinationFile.exists()) {

            destinationFile.createNewFile();

        }
        FileChannel source = null;
        FileChannel destination = null;

        source = new FileInputStream(file).getChannel();
        destination = new FileOutputStream(destinationFile).getChannel();

        destination.transferFrom(source, 0, source.size());

        source.close();
        destination.close();

    }

    @Override
    public void onImageDownloadClickedListener(StatusModel statusModel) {

        downloadFile(statusModel);
    }

    boolean isShowingImage = false;

    @Override
    public void onViewImageClickedListener(StatusModel statusModel) {

        isShowingImage = true;
        activityMainBinding.mainImagviewLayout.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(statusModel.getFile().getAbsolutePath());
        activityMainBinding.mainImagview.setImageBitmap(bitmap);
        activityMainBinding.mainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMainBinding.mainImagviewLayout.setVisibility(View.GONE);

                isShowingImage = false;
            }
        });
    }


    @Override
    public void onVideoDownloadClickedListener(StatusModel statusModel) {
        downloadFile(statusModel);

    }

    Gson gson= new Gson();
public static final String STATUS_MODEL="STATUS_MODEL";
    @Override
    public void onViewVideoClickedListener(StatusModel statusModel) {

        String model=gson.toJson(statusModel);
        Intent intent=new Intent(MainActivity.this,PlayerActivity.class);
        intent.putExtra(STATUS_MODEL,model);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        if (isShowingImage) {
            isShowingImage = false;
            activityMainBinding.mainImagviewLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}