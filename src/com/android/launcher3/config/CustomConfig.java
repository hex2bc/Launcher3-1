package com.android.launcher3.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;

public class CustomConfig {
    private static final String TAG = "CustomConfig";
    private volatile static CustomConfig mCustomConfig = null;
//    private final File CONFIG_FILE = new File("/system/launcher/config.xml");
    private final File CONFIG_FILE = new File("/sdcard/0pixel/launcher/config.xml");
    private HashMap<String,String> mConfigsMap = new HashMap<String,String>();
    public HashMap<String,String> mIconPathMap = new HashMap<String,String>();
    private List<String> mStyles = new ArrayList<>();
    private String mCurStyle;

    private CustomConfig() {
        try {
            loadXMLConfigs();
            loadDefaultConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultConfigs() {
        String[] style = getConfig("styles").split(";");
        if (style.length > 0) {
            mStyles.addAll(Arrays.asList(style));
        }
        mCurStyle = mStyles.get(0);
    }

    public List<String> getStyleList() {
        return mStyles;
    }

    public void setStyle(int index) {
        if (index >= mStyles.size()) return;
        mCurStyle = mStyles.get(index);
    }

    public int getCutStyleIndex() {
        return mStyles.indexOf(mCurStyle);
    }

    public static synchronized CustomConfig getInstance() {
        if (mCustomConfig == null) {
            synchronized (CustomConfig.class) {
                if (mCustomConfig == null) {
                    mCustomConfig = new CustomConfig();
                }
            }
            return mCustomConfig;
        }
        return mCustomConfig;
    }

    private boolean checkConfigsExist(String config) {
        if (mConfigsMap.isEmpty()) {
            return false;
        }
        if (mConfigsMap.containsKey(config))
            return true;
        return false;
    }

    public String getIconPathFromComponentName(String component) {
        if (mIconPathMap.isEmpty()) {
            return null;
        }
        String path = getConfig("custom_path_prefix");
        if (path != null && mIconPathMap.get(component) != null) {
            return String.format(path + mCurStyle + "/" + mIconPathMap.get(component));
        }
        return null;
    }

    public List<String> getAllStyleThumbPath() {
        List<String> thumb_list = new ArrayList<>();
        for (String style : mStyles) {
            String path = getConfig("custom_path_prefix") + style + "/thumbnail.png";
            thumb_list.add(path);
        }
        return thumb_list;
    }

    public String getConfig(String config) {
        return getConfig(config, null);
    }

    public String getConfig(String config, String def) {
        if (!checkConfigsExist(config)) {
            return def;
        }
        return mConfigsMap.get(config);
    }

    public boolean getBooleanConfig(String config) {
        return "true".equals(getConfig(config));
    }

    public int getIntConfig(String config, int def) {
        String num = getConfig(config);
        if (num == null) {
            return def;
        }
        int value = def;
        try {
            value = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void loadXMLConfigs() throws Exception {
        mIconPathMap.clear();
        mConfigsMap.clear();
        FileInputStream sourceFile = null;

        if (!CONFIG_FILE.exists()) {
            return;
        }
        try {
            XmlPullParser parser = Xml.newPullParser();
            sourceFile = new FileInputStream(CONFIG_FILE);
            parser.setInput(sourceFile, "utf-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG &&
                        parser.getName().equals("icon")) {
                    String icon_class = parser.getAttributeValue(null, "componentName");
                    String icon_image = parser.getAttributeValue(null, "image");
                    if (!icon_class.equals("null") && !icon_image.equals("null")) {
                        mIconPathMap.put(icon_class, icon_image);
                    }
                }

                if (event == XmlPullParser.START_TAG &&
                        parser.getName().equals("general_config")) {
                    for (int i = 0;i < parser.getAttributeCount();i++) {
                        mConfigsMap.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                    }
                }

                if (event == XmlPullParser.END_TAG && parser.getName().equals("default_layout")) {
                    break;
                }
                event = parser.next();
            }
        } finally {
            if(sourceFile != null){
                sourceFile.close();
            }
        }
    }

    public Bitmap loadIcon(String icon_path) {
//        Log.v(TAG, "huangqw loadIcon: ");
        Bitmap icon = null;
        FileInputStream resourceFile = null;
        try {
            File image_file = new File(icon_path);
            if(!image_file.exists()){
                Log.v(TAG, "loadIcon: image file no exists");
                return null;
            }
            resourceFile = new FileInputStream(image_file);
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            int bytesRead = 0;
            while (bytesRead >= 0) {
                bytes.write(buffer, 0, bytesRead);
                bytesRead = resourceFile.read(buffer, 0, buffer.length);
            }
            icon = BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.size());
            if (icon == null) {
                Log.w(TAG, "failed to decode pre-load icon for ");
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            Log.w(TAG, "failed to read pre-load icon for: ", e);
        } finally {
            if (resourceFile != null) {
                try {
                    resourceFile.close();
                } catch (IOException e) {
                    Log.d(TAG, "failed to manage pre-load icon file: ", e);
                }
            }
        }
        return icon;
    }

}
