package lt.vu.mif.www.kursinis;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class UniqueCodeGenerator {

    public static String generate(Activity ac){
        String tmDevice, tmSerial, androidId;

        TelephonyManager tm = (TelephonyManager) ac.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(ac.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return tmDevice + "-" + tmSerial + "-" + androidId + "-" + deviceId;
    }
}
