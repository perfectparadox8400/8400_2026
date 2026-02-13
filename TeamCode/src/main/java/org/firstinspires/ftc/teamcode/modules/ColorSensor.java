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
    public static void init_colorSensor(HardwareMap hardwareMap, Telemetry telemetry) {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        telemetry.addData("Color Sensor", "Initializing");
        telemetry.update();
    }

    public static String checkColor(Telemetry telemetry) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(colors.toColor(), hsv);

        float hue = hsv[0];
        float sat = hsv[1];
        float val = hsv[2];

        telemetry.addData("Hue",hue);
        telemetry.addData("Sat", sat);
        telemetry.addData("Val", val);

        // Detects "No Color"
        if (sat < 0.3f || val < 0.1f) {
            telemetry.addData("Color Sensor", "none");
            telemetry.update();
            return "none";
        }

        // Green
        if (hue >= 80  && hue <= 160) {
            telemetry.addData("Color Sensor", "green");
            telemetry.update();
            return "green";
        }

        // Purple
        if (hue >= 270 && hue <= 320) {
            telemetry.addData("Color Sensor", "purple");
            telemetry.update();
            return "purple";
        }

        return "unknown";
    }
}

// Sincerely, Crosby