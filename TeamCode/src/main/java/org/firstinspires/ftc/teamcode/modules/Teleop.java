package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Controller.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Teleop {
    public static Telemetry telemetry;
    public static final FtcDashboard dashboard = FtcDashboard.getInstance();
    public static class Nancy {
        private static boolean goX = false;
        private static boolean goY = false;
        private static boolean goLowX = false;
        private static boolean goB = false;
        private static boolean goHome = false;
        private static boolean start = false;
        private static boolean armLimitReset = false;
        private static boolean armDownReset = false;
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init Arm
            Arm.Nancy.init_arm(hardwareMap);
            // Init Wheels
            init_drive(hardwareMap);
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            telemetry.addData("Press", "Start");
            telemetry.update();
            RevBlinkinLedDriver blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
            blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
        }

        public static void start(Gamepad gamepad1, Gamepad gamepad2) {
            goX = false;
            goY = false;
            goB = false;
            goLowX = false;
            goHome = false;
            start = true;
            hang = false;
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
            hang1.setPosition(1);
            hang2.setPosition(0);
            armLimitReset = false;
            armDownReset = false;
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Arm Loop
            Arm.Nancy.arm_loop();

            // Controller1 Buttons
            // Speed Control
            speedControl(gamepad1, gamepad2);

            if (gamepad1.start && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 2);
                fieldDriving = !fieldDriving;
            }

            if (gamepad1.back && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 4);
                imu.resetYaw();
            }

            if (gamepad2.guideWasPressed()) {
                start = false;
                goX = false;
                goLowX = false;
                goY = false;
                goHome = false;
                goB = false;
                armTargetPosition = armPosition;
                slideTargetPosition = slidePosition;
                rumble(gamepad2, 750, 1, 1);
            }

            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
            }

            if (gamepad1.yWasReleased()){
                rumbleBlip(gamepad1, 1);
                setBrakeMode(!brakeMode);
            }

            //Telemetry
            drive_telemetry(telemetry);
            Arm.Nancy.arm_telemetry(telemetry);

            telemetry.addData("Start", start);
            telemetry.addData("goLowX", goLowX);
            telemetry.addData("goX", goX);
            telemetry.addData("goY", goY);
            telemetry.addData("goB", goB);
            telemetry.addData("goHome", goHome);
            telemetry.update();

            if (gamepad1.guideWasPressed()) {
                hang = false;
                rumble(gamepad1, 500, 1, 1);
            }


            // Controller2 Buttons
            turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) * DriveSlowPower;
            if (gamepad2.dpad_left) {
                strafeFactor = -1.0 * DrivePower; // Strafe left
            } else if (gamepad2.dpad_right) {
                strafeFactor = DrivePower; // Strafe right
            } else {
                strafeFactor = 0.0;
            }

            if (gamepad1.touchpadWasPressed()) {
                rumbleWiiii(gamepad1, 5000);
            }

            if (gamepad2.touchpadWasPressed()) {
                rumbleWiiii(gamepad2, 5000);
            }
        }
    }

    public static class MentorBot {
        private static boolean goY = false;
        private static boolean goX = false;
        private static boolean goA = false;
        private static boolean goHome = false;
        private static boolean start = false;
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init Arm
            Arm.MentorBot.init_arm(hardwareMap);
            // Init Wheels
            init_drive(hardwareMap);
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            telemetry.addData("Press", "Start");
            telemetry.update();
        }

        public static void start(Gamepad gamepad1, Gamepad gamepad2) {
            goX = false;
            goY = false;
            goA = false;
            goHome = false;
            start = true;
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Controller1 Buttons
            // Speed Control
            speedControl(gamepad1, gamepad2);

            if (gamepad1.start && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 2);
                fieldDriving = !fieldDriving;
            }

            if (gamepad1.back && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 4);
                imu.resetYaw();
            }

            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
            }

            if (gamepad1.yWasReleased()){
                rumbleBlip(gamepad1, 1);
                setBrakeMode(!brakeMode);
            }

            //Telemetry
            drive_telemetry(telemetry);
            Arm.MentorBot.arm_telemetry(telemetry);

            telemetry.addData("Start", start);
            telemetry.addData("goA", goA);
            telemetry.addData("goX", goX);
            telemetry.addData("goY", goY);
            telemetry.addData("goHome", goHome);
            telemetry.update();


            // Controller2 Buttons
            turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) * DriveSlowPower;
            if (gamepad2.left_bumper) {
                strafeFactor = -1.0 * DriveSlowPower; // Strafe left
            } else if (gamepad2.right_bumper) {
                strafeFactor = DriveSlowPower; // Strafe right
            } else {
                strafeFactor = 0.0;
            }
            if (gamepad2.dpad_up) {
                driveFactor = DriveSlowPower;
            } else if (gamepad2.dpad_down) {
                driveFactor = -DriveSlowPower;
            } else {
                driveFactor = 0;
            }


            if (gamepad1.touchpadWasPressed()) {
                rumbleWiiii(gamepad1, 5000);
            }

            if (gamepad2.touchpadWasPressed()) {
                rumbleWiiii(gamepad2, 5000);
            }
        }
    }
}
