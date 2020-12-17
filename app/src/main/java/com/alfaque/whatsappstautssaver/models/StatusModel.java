package com.alfaque.whatsappstautssaver.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class StatusModel implements Parcelable {

    private static final String MP4 = ".mp4";
    private  File file;
    private Bitmap thumbNail;
    private final String title;
    private final String path;
    private boolean isVideo;

    public StatusModel(File file, String title, String path) {
        this.file = file;
        this.title = title;
        this.path = path;
        this.isVideo = file.getName().endsWith(MP4);

    }

    protected StatusModel(Parcel in) {
        thumbNail = in.readParcelable(Bitmap.class.getClassLoader());
        title = in.readString();
        path = in.readString();
        isVideo = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(thumbNail, flags);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeByte((byte) (isVideo ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StatusModel> CREATOR = new Creator<StatusModel>() {
        @Override
        public StatusModel createFromParcel(Parcel in) {
            return new StatusModel(in);
        }

        @Override
        public StatusModel[] newArray(int size) {
            return new StatusModel[size];
        }
    };

    public File getFile() {
        return file;
    }

    public Bitmap getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(Bitmap thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    @Override
    public String toString() {
        return "StatusModel{" +
                "file=" + file +
                ", thumbNail=" + thumbNail +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", isVideo=" + isVideo +
                '}';
    }
}
