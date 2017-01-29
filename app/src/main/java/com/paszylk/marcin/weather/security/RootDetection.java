package com.paszylk.marcin.weather.security;

import java.io.File;

public class RootDetection {

    private static final String[] POSSIBLE_ROOT_PLACES = {"/sbin/", "/system/bin/",
            "/system/xbin/", "/data/local/xbin/","/data/local/bin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/"};

    private RootDetection(){}

    public static boolean isRooted() {
        return findBinary("su");
    }

    private static boolean findBinary(String binaryName) {
        for (String where : POSSIBLE_ROOT_PLACES) {
            if ( new File( where + binaryName ).exists() ) {
                return true;
            }
        }
        return false;
    }
}
