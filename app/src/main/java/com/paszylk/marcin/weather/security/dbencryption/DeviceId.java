package com.paszylk.marcin.weather.security.dbencryption;

import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceId {

    private static final String WIFI_INTERFACE_NAME = "wlan0";
    private static final String MAC_ADDRESS_RETURNED_BY_NEW_DEVICES = "020000000000";

    private DeviceId()
    {
    }

    public static char[] generate(){
        return getMacAddress();
    }

    private static char[] getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase(WIFI_INTERFACE_NAME)) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    continue;
                }

                StringBuilder macAddressBuilder = new StringBuilder();
                for (byte b : macBytes) {
                    macAddressBuilder.append(Integer.toHexString(b & 0xFF));
                }

                if (macAddressBuilder.length() > 0) {
                    macAddressBuilder.deleteCharAt(macAddressBuilder.length() - 1);
                }
                return macAddressBuilder.toString().toCharArray();
            }
        } catch (Exception ex) {
            Log.d("DeviceId", "Couldn't get MAC address, using default.");
        }
        return MAC_ADDRESS_RETURNED_BY_NEW_DEVICES.toCharArray();
    }

}
