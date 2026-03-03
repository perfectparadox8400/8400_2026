package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import android.graphics.Color;

public class ColorSensor {
    public static NormalizedColorSensor colorSensor;

    // These now point to the static inner class defined at the bottom
    public static Servo leftLED;
    public static Servo centerLED;
    public static Servo rightLED;
    public static final double PURPLE = 0.722;
    public static final double GREEN = 0.500;
    public static final double OFF = 0.0;
    public static void init_colorSensor(HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        // Correct initialization using hardwareMap.get instead of "new"
        leftLED = hardwareMap.get(Servo.class, "ledLeft");
        centerLED = hardwareMap.get(Servo.class, "ledCenter");
        rightLED = hardwareMap.get(Servo.class, "ledRight");

        // Initial Test
    }
    static void setLEDColor(Servo led, String color) {
        ((ServoImplEx) leftLED).setPwmRange(new PwmControl.PwmRange(1000, 2000));
        ((ServoImplEx) centerLED).setPwmRange(new PwmControl.PwmRange(1000, 2000));
        ((ServoImplEx) rightLED).setPwmRange(new PwmControl.PwmRange(1000, 2000));

        if (led == null || color == null) return;
        if (color.equalsIgnoreCase("purple")) led.setPosition(PURPLE);
        else if (color.equalsIgnoreCase("green")) led.setPosition(GREEN);
        else led.setPosition(OFF);
    }

    public static boolean isPurple(float hue) {
        return hue >= 240 && hue <= 270;
    }

    public static boolean isGreen(float hue) {
        return hue >= 120 && hue <= 180;
    }

    public static String getColor () {
        if (colorSensor == null) return "unknown";
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(Color.argb(255,
                (int)(colors.red * 255),
                (int)(colors.green * 255),
                (int)(colors.blue * 255)), hsv);

        if (isPurple(hsv[0])) return "purple";
        if (isGreen(hsv[0])) return "green";
        return "unknown";
    }

    public static void updateLEDs(String[] spindexerSlots) {
        if (!spindexerSlots[0].equals("empty")) setLEDColor(leftLED,   spindexerSlots[0]);
        if (!spindexerSlots[1].equals("empty")) setLEDColor(centerLED, spindexerSlots[1]);
        if (!spindexerSlots[2].equals("empty")) setLEDColor(rightLED,  spindexerSlots[2]);
    }

    public static void rotatePositions(String[] spindexerSlots) {
        String temp = spindexerSlots[0];
        for (int i = 0; i < 2; i++) {
            spindexerSlots[i] = spindexerSlots[i + 1];
        }
        spindexerSlots[2] = temp;
    }

    public static void colorSensor_telemetry (Telemetry telemetry, String[] spindexerSlots, String spindexerMode){
        if (colorSensor == null){
            telemetry.addData("ColorSensor", "Not initialized");
            return;
        }

        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.colorToHSV(Color.argb(255,
                (int)(colors.red * 255),
                (int)(colors.green * 255),
                (int)(colors.blue * 255)), hsv);

        telemetry.addData("Hue", hsv[0]);
        for (int i = 0; i < spindexerSlots.length; i++) {
            telemetry.addData("Slot " + (i + 1), spindexerSlots[i]);
        }
        telemetry.addData("Current Color:",getColor());
        telemetry.addData("Spindexer Mode", spindexerMode);
    }

}