package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    public static void rumbleBlip(Gamepad gamepad, int blips) {
        gamepad.rumbleBlips(blips);
    }

    public static void rumble(Gamepad gamepad, int time, double rumble1, double rumble2) {
        gamepad.rumble(rumble1, rumble2, time);
    }

    public static void rumbleWiiii(Gamepad gamepad, int time) {
        gamepad.stopRumble();
        gamepad.setLedColor(0.3686, 0.0235, 0.5412, time);
        rumble(gamepad, time, 1, 1);
    }
}
