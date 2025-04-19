package org.firstinspires.ftc.teamcode;
import android.content.Context;

public class Robot {
    private static Context appContext;  // <- hold it here

    public static void init(Context context) {
        appContext = context.getApplicationContext(); // safe to keep forever
    }

    public static String robotName() {
        return appContext.getString(R.string.robot);
    }
}
