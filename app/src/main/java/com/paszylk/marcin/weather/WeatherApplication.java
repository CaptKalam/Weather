package com.paszylk.marcin.weather;

import android.app.Application;
import android.util.Log;

import com.paszylk.marcin.weather.security.RootDetection;
import com.paszylk.marcin.weather.security.dbencryption.DeviceId;
import com.paszylk.marcin.weather.security.dbencryption.Secret;
import com.paszylk.marcin.weather.cities.domain.model.DaoMaster;
import com.paszylk.marcin.weather.cities.domain.model.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.Arrays;

public class WeatherApplication extends Application {

    /**
     * Insert your own APPID from openweathermap.org
     */
    //TODO: remove my key
    public static final String OPEN_WEATHER_MAP_APP_ID = "badf15c19867188348efccc22abb3d1c";

    private char[] joinedArray;
    private Secret secret;

    /** I'm not going to support rooted devices. **/
    static {
        if(RootDetection.isRooted()){
            Log.e("Root detection", "device is rooted. Exiting!");
            //TODO: uncomment this line
            //System.exit(0);
        }
    }

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDb();
    }

    private void initializeDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weather.db");
        joinArrays();
        try {
            Database db = helper.getEncryptedWritableDb(joinedArray);
            daoSession = new DaoMaster(db).newSession();
        }catch (net.sqlcipher.database.SQLiteException e){
            Log.e("DB tampering detected", "Database has been tampered with. Exiting.");
            cleanArrays();
            System.exit(0);
        }finally {
            cleanArrays();
        }
    }

    private void cleanArrays() {
        secret.clean();
        Arrays.fill(joinedArray, '\0');
    }

    private void joinArrays() {
        char[] deviceId = DeviceId.generate();
        secret = new Secret();
        joinedArray = new char[deviceId.length + secret.getLength()];
        System.arraycopy(deviceId, 0, joinedArray, 0, deviceId.length);
        System.arraycopy(secret.generate(), 0, joinedArray, deviceId.length, secret.getLength());
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }

}
