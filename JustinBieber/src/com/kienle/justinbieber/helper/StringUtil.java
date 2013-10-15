package com.kienle.justinbieber.helper;

import com.kienle.justinbieber.MyApplication;

public class StringUtil {
    public static String cutFirstZeroCharacters(String originalString) {
        for (int i = 0; i < originalString.length(); i++) {
            if (originalString.startsWith("0")) {
                originalString = originalString.substring(1);
            }
        }
        return originalString;
    }

    public static String getString(int resourceId) {
        return MyApplication.getInstance().getAppContext().getResources().getString(resourceId);
    }
}
