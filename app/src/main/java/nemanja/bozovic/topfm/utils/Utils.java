package nemanja.bozovic.topfm.utils;


import android.app.ActivityManager;
import android.content.Context;

public class Utils {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo i : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(i.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
