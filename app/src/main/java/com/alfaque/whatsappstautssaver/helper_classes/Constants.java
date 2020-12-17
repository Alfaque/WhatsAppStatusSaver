package com.alfaque.whatsappstautssaver.helper_classes;

import android.os.Environment;

import java.io.File;

public class Constants {


    public static final File STATUS_DIR = new File(Environment.getExternalStorageDirectory()
            + File.separator + "WhatsApp/Media/.Statuses");
    public static final String App_DIR = Environment.getExternalStorageDirectory()
            + File.separator + "WhatsAppStatusSaver";

    public static final int ThumbNailSize = 128;

}
