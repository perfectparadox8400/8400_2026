package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Drive.DrivePower;
import static org.firstinspires.ftc.teamcode.modules.Drive.DriveSlowPower;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controller {
    private static long lastSwitchTime = 0;
    private static boolean buttonsBlocked = false;
    private static final long cooldownMillis = 1000;
    //speedControl allows to press "b" to change speed
    public static void speedControl(Gamepad gamepad1, Gamepad gamepad2) {
        if ((controllerChange(gamepad1, gamepad2) && gamepad1.b) || gamepad1.rightBumperWasPressed()) {
            rumble(gamepad1, 250, 0.4, 0.4);
        } else if ((controllerChange(gamepad1, gamepad2) && gamepad1.a) || gamepad1.leftBumperWasPressed()) {
            rumble(gamepad1, 250, 0.2, 0.2);
        }
        if ((controllerChange(gamepad1, gamepad2) && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = DriveSlowPower;
        } else {
            DrivePower = 1;
        }
    }

    public static boolean controllerChange(Gamepad gamepad1, Gamepad gamepad2) {
        // Detect switch combo (start + A or B)
        boolean switched =
                (gamepad1.start && (gamepad1.a || gamepad1.b)) ||
                        (gamepad2.start && (gamepad2.a || gamepad2.b));

        if (switched) {
            lastSwitchTime = System.currentTimeMillis();
            buttonsBlocked = true;
        }

        boolean cooldownPassed = (System.currentTimeMillis() - lastSwitchTime) > cooldownMillis;

        // If cooldown passed and both A and B are released, unblock them
        if (cooldownPassed && !gamepad2.a && !gamepad2.b) {
            buttonsBlocked = false;
        }

        // Return true if we're safe to allow button input
        return cooldownPassed && !buttonsBlocked;
    }

    public static boolean controllerJoyStickActive(Gamepad gamepad) {
        return gamepad.left_stick_x >= 0.05 || gamepad.left_stick_x <= -0.05 || gamepad.left_stick_y >= 0.05 || gamepad.left_stick_y <= -0.05 || gamepad.right_stick_x >= 0.05 || gamepad.right_stick_x <= -0.05 || gamepad.right_stick_y >= 0.05 || gamepad.right_stick_y >= -0.05;
    }

    public static void rumbleBlip(Gamepad gamepad, int blips) {
        gamepad.rumbleBlips(blips);
    }

    public static void rumble(Gamepad gamepad, int time, double rumble1, double rumble2) {
        gamepad.rumble(rumble1, rumble2, time);
    }

    public static void rumbleWiiii(Gamepad gamepad, int time) {
        gamepad.stopRumble();
        gamepad.setLedColor(0.2510, 0.0078, 0.3686, time);
        rumble(gamepad, time, 1, 1);
    }
}
