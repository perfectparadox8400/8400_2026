package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import android.graphics.Color;

// Dear member of the programming team,
// Unfortunately for your convenience and sanity, no comments have been left for you

public class ColorSensor {
    public static NormalizedColorSensor colorSensor;
    public static void init_colorSensor(HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
    }


    public static boolean isPurple(float hue) {
        return hue >= 240 && hue <= 270;
    }

    public static boolean isGreen(float hue) {
        return hue >= 140 && hue <= 170;
    }

    public static String getColor () {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(Color.argb(255,
                (int)(colors.red * 255),
                (int)(colors.green * 255),
                (int)(colors.blue * 255)), hsv);
        float hue = hsv[0];
        if (isPurple(hue)) {
            return "purple";
        } else if (isGreen(hue)) {
            return "green";
        } else {
            return "unknown";
        }
    }

    public static void rotatePositions(String [] spindexerSlots){
        String temp = "";
        temp = spindexerSlots[0];
        for (int i = 0; i < 2; i++) {
            spindexerSlots[i] = spindexerSlots[i + 1];
        }
        spindexerSlots[2] = temp;
    }
    public static void colorSensor_telemetry (Telemetry telemetry, String[]spindexerSlots,String spindexerMode){
        if (colorSensor == null){
            telemetry.addData("ColorSensor", "Not initialized");
            telemetry.update();
            return;
        }

        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(Color.argb(255,
                (int)(colors.red * 255),
                (int)(colors.green * 255),
                (int)(colors.blue * 255)), hsv);
        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        telemetry.addData("Hue", hue);
        telemetry.addData("Saturation", sat);

        for (int i = 0; i < spindexerSlots.length; i++) {
            telemetry.addData("Slot " + (i + 1), spindexerSlots[i]);
        }

        telemetry.addData("Spindexer Mode",spindexerMode);

        if (isPurple(hue)) {
            telemetry.addData("Detected Color", "Purple");
        } else if (isGreen(hue)) {
            telemetry.addData("Detected Color", "Green");
        } else {
            telemetry.addData("Detected Color", "Unknown");
        }


        telemetry.update();

    }
}

// Sincerely, Crosby