package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    public static void rummbleBlip(Gamepad gamepad, int blips) {
        gamepad.rumbleBlips(blips);
    }
    public static void rummble(Gamepad gamepad, int time, double rumble1, double rumble2) {
        gamepad.rumble(rumble1, rumble2, time);
    }

    public static void rummbleStop(Gamepad gamepad) {
        gamepad.stopRumble();
    }
}
