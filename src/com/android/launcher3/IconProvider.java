package com.android.launcher3;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.launcher3.util.ResourceBasedOverride;

public class IconProvider implements ResourceBasedOverride {

    public static IconProvider newInstance(Context context) {
        return Overrides.getObject(IconProvider.class, context, R.string.icon_provider_class);
    }

    public IconProvider() { }

    public String getSystemStateForPackage(String systemState, String packageName) {
        return systemState;
    }

    /**
     * @param flattenDrawable true if the caller does not care about the specification of the
     *                        original icon as long as the flattened version looks the same.
     */
    public Drawable getIcon(LauncherActivityInfo info, int iconDpi, boolean flattenDrawable) {
        if (info.getComponentName().toShortString().contains("contacts")) {
            return Drawable.createFromPath("/sdcard/0pixel/icon/contacts.png");
        }
        Log.d("huangqw", "get default Icon: " + info.getName());
        return info.getIcon(iconDpi);
    }
}
