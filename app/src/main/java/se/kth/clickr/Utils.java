package se.kth.clickr;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ElectricCookie on 29.08.2016.
 */
public class Utils {

    public static int getPixels(int unit, float size) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(unit, size, metrics);
    }
}
