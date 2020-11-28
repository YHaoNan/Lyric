package site.lilpig.lyric;

import android.os.Environment;

import java.io.File;

public class Config {
    public static final String DEVSERVER = "http://192.168.43.155:8080/";
    public static final String PRODSERVER = "http://106.54.85.24/";
    public static final String ACTIVE_SERVER = PRODSERVER;
    public static final String APPVER = "v1.4.4";
    public static final String DEFAULT_DATA_DIR = new File(Environment.getExternalStorageDirectory(),"mj_lyric").getAbsolutePath();
    public static final String DEFAULT_IMG_DIR = new File(DEFAULT_DATA_DIR,"imagesOut").getAbsolutePath();
    public static final String DEFAULT_LRC_DIR = new File(DEFAULT_DATA_DIR,"lyrics").getAbsolutePath();
}
