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
        private static RevBlinkinLedDriver blinkinLedDriver;
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init Arm
            Arm.Nancy.init_arm(hardwareMap);
            // Init Wheels
            init_drive(hardwareMap);
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
            telemetry.addData("Press", "Start");
            telemetry.update();
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

            if(start) {
                if (slideBack.isPressed()){
                    slide_reset(false);
                    if(armDown.isPressed()){
                        Arm.Nancy.arm_reset(false);
                        start = false;
                    } else {
                        Arm.Nancy.arm_reset(true);
                        Arm.Nancy.arm();
                    }
                } else {
                    slide_reset(true);
                }
                return;
            }

            if (gamepad1.guideWasPressed()) {
                hang = false;
                rumble(gamepad1, 500, 1, 1);
            }

            if (gamepad1.dpad_down && (hang1.getPosition() <= Arm.Nancy.hookOpen1 && hang2.getPosition() >= Arm.Nancy.hookOpen2)) {
                hang1.setPosition(hang1.getPosition() + .013);
                hang2.setPosition(hang2.getPosition() - .013);
            } else if (gamepad1.dpad_up && (hang1.getPosition() >= Arm.Nancy.hookClosed1 && hang2.getPosition() <= Arm.Nancy.hookClosed2)) {
                hang1.setPosition(hang1.getPosition() - .013);
                hang2.setPosition(hang2.getPosition() + .013);
            } else if (gamepad1.dpadLeftWasPressed()) {
                hang1.setPosition(Arm.Nancy.hookOpen1);
                hang2.setPosition(Arm.Nancy.hookOpen2);
                rumbleBlip(gamepad1, 1);
            } else if (gamepad1.dpadRightWasPressed()) {
                hang1.setPosition(Arm.Nancy.hookClosed1);
                hang2.setPosition(Arm.Nancy.hookClosed2);
                rumbleBlip(gamepad1, 1);
            }

            if (gamepad1.xWasPressed()) {
                armTargetPosition = -350;
                slideTargetPosition = 0;
                wrist1.setPosition(0);
                wrist2.setPosition(1);
                hang1.setPosition(0);
                hang2.setPosition(1);
                hang = true;
                rumbleBlip(gamepad1, 1);
                rumbleBlip(gamepad2, 1);
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

            /* ------ SLIDE LOGIC ------ */
            if (gamepad2.left_bumper) {
                slideTargetPosition -= 65;
            } else if (gamepad2.right_bumper) {
                slideTargetPosition += 65;
            }

            /* ------ RESET ------ */
            if (gamepad2.backWasPressed()) {
                goY = false;
                goX = false;
                goLowX = false;
                wrist1.setPosition(0.15);
                wrist2.setPosition(0.85);
                slideTargetPosition = 0;
                goHome = true;
                rumble(gamepad2, 500, 0.6, 0.6);
            }

            /* ------WRIST LOGIC------ */
            if (gamepad2.dpad_up) {
                wrist1.setPosition(wrist1.getPosition() + .012);
                wrist2.setPosition(wrist2.getPosition() - .012);
            } else if (gamepad2.dpad_down) {
                wrist1.setPosition(wrist1.getPosition() - .012);
                wrist2.setPosition(wrist2.getPosition() + .012);
            }

            /* ------ MAIN BOOM LOGIC ------ */
            if (gamepad2.left_stick_y > 0.2){
                armTargetPosition -= 13;
            } else if (gamepad2.left_stick_y < -0.2){
                armTargetPosition += 15;
            }

            if (gamepad2.leftStickButtonWasPressed() && !armLimitReset) {
                armLimit += 50;
                armLimitReset = true;
                rumble(gamepad2, 250, 1, 1);
            } else if (!gamepad2.left_stick_button) {
                armLimitReset = false;
            }

            if (gamepad2.rightStickButtonWasPressed() && !armDownReset) {
                armDownLimit -= 10;
                armDownReset = true;
                rumble(gamepad2, 250, 1, 1);
            } else if (!gamepad2.right_stick_button) {
                armDownReset = false;
            }

            /* ------ INTAKE LOGIC ------ */
            if (gamepad2.right_stick_y > 0.2){
                intake.setPower(-1);
                intakeOther.setPower(1);
            } else if (gamepad2.right_stick_y < -0.2){
                intake.setPower(1);
                intakeOther.setPower(-1);
            } else if (gamepad2.right_stick_x > 0.2){
                intake.setPower(-1);
                intakeOther.setPower(-1);
            } else if (gamepad2.right_stick_x < -0.2){
                intake.setPower(1);
                intakeOther.setPower(1);
            } else {
                intake.setPower(0);
                intakeOther.setPower(0);
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.b) {
                armTargetPosition = 900;
                goB = true;
                rumbleBlip(gamepad2, 1);
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.a) {
                wrist1.setPosition(0.8);
                wrist2.setPosition(0.2);
                rumbleBlip(gamepad2, 1);
            }

            /* ------ PRESETS ------ */
            if (gamepad2.xWasPressed()) {
                if (armPosition < 300) {
                    goLowX = true;
                    goX = false;
                    armTargetPosition = 200;
                } else {
                    goX = true;
                    goLowX = false;
                    slideTargetPosition = 0;
                }
                goY = false;
                goB = false;
                goHome = false;
                wrist1.setPosition(0.4);
                wrist2.setPosition(0.6);
                rumbleBlip(gamepad2, 1);
            }

            if (gamepad2.yWasPressed()) {
                goY = true;
                goX = false;
                goLowX = false;
                goHome = false;
                goB = false;
                armTargetPosition = 1110;
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
                rumbleBlip(gamepad2, 1);
            }

            if (goHome && (slidePosition <= 2000)) {
                slideTargetPosition = 0;
                armTargetPosition = 0;
            }

            if (goHome && armDown.isPressed()) {
                goHome = false;
                rumble(gamepad2, 500, 0.8, 0.8);
            }

            //Go X
            if (goX && (slidePosition <= 2000)) {
                armTargetPosition = 200;
                goX = false;
            }

            if (goLowX && (armPosition >= 175)) {
                slideTargetPosition = 0;
                goLowX = false;
            }

            //Go Y
            if (goY && (armPosition >= 950)) {
                slideTargetPosition = slideLimit;
                wrist1.setPosition(0.75);
                wrist2.setPosition(0.25);
                goY = false;
            }

            //Go B
            if (goB && (armPosition >= 800)) {
                slideTargetPosition = 1500;
                wrist1.setPosition(0.85);
                wrist2.setPosition(0.15);
                goB = false;
            }

            //Slide Power
            slide();

            //Arm Power
            //Arm.Nancy.arm();

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
        private static boolean goLowX = false;
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
            goLowX = false;
            goY = false;
            goA = false;
            goHome = false;
            start = true;
            wrist1.setPosition(0.05);
            wrist2.setPosition(0.95);
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Arm Loop
            Arm.MentorBot.arm_loop();

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
            telemetry.addData("goLowX", goLowX);
            telemetry.addData("goY", goY);
            telemetry.addData("goHome", goHome);
            telemetry.update();

            if(start) {
                if (slideBack.isPressed()){
                    slide_reset(false);
                    Arm.MentorBot.arm_reset();
                    start = false;
                } else {
                    slide_reset(true);
                }
                return;
            }

            // Controller2 Buttons
            turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) * DriveSlowPower;
            if (gamepad2.left_bumper) {
                strafeFactor = -1.0 * DriveSlowPower; // Strafe left
            } else if (gamepad2.right_bumper) {
                strafeFactor = DriveSlowPower; // Strafe right
            } else {
                strafeFactor = 0.0;
            }

            /* ------ SLIDE LOGIC ------ */
            slideTargetPosition += (-gamepad2.left_stick_y * 55);

            /* ------ RESET ------ */
            if (gamepad2.backWasPressed()) {
                goY = false;
                goX = false;
                goA = false;
                goLowX = false;
                wrist1.setPosition(0.15);
                wrist2.setPosition(0.85);
                slideTargetPosition = 0;
                goHome = true;
                rumble(gamepad2, 500, 0.6, 0.6);
            }

            /* ------WRIST LOGIC------ */
            if (gamepad2.dpad_up) {
                wrist1.setPosition(wrist1.getPosition() + .012);
                wrist2.setPosition(wrist2.getPosition() - .012);
            } else if (gamepad2.dpad_down) {
                wrist1.setPosition(wrist1.getPosition() - .012);
                wrist2.setPosition(wrist2.getPosition() + .012);
            }

            /* ------ MAIN BOOM LOGIC ------ */
            if (gamepad2.right_stick_y > 0.2){
                armTargetPosition += 15;
            } else if (gamepad2.right_stick_y < -0.2){
                armTargetPosition -= 15;
            }

            /* ------ INTAKE LOGIC ------ */
            if (gamepad2.right_stick_x > 0.5){
                intake.setPower(-1);
                intakeOther.setPower(1);
            } else if (gamepad2.right_stick_x < -0.5){
                intake.setPower(1);
                intakeOther.setPower(-1);
            } else if (gamepad2.dpad_right){
                intake.setPower(-1);
                intakeOther.setPower(-1);
            } else if (gamepad2.dpad_left){
                intake.setPower(1);
                intakeOther.setPower(1);
            } else {
                intake.setPower(0);
                intakeOther.setPower(0);
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.b) {
                start = false;
                goX = false;
                goLowX = false;
                goA = false;
                goY = false;
                goHome = false;
                armTargetPosition = armPosition;
                slideTargetPosition = slidePosition;
                rumble(gamepad2, 500, 1, 1);
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.a) {
                goY = false;
                goX = false;
                goLowX = false;
                goA = true;
                goHome = false;
                armTargetPosition = 1110;
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
                rumbleBlip(gamepad2, 1);
            }

            /* ------ PRESETS ------ */
            if (gamepad2.xWasPressed()) {
                if (armPosition < 300) {
                    goLowX = true;
                    goX = false;
                    armTargetPosition = 200;
                } else {
                    goX = true;
                    goLowX = false;
                    slideTargetPosition = 0;
                }
                goY = false;
                goA = false;
                goHome = false;
                wrist1.setPosition(0.4);
                wrist2.setPosition(0.6);
                rumbleBlip(gamepad2, 1);
            }

            if (gamepad2.yWasPressed()) {
                goY = false;
                goX = false;
                goLowX = false;
                goHome = false;
                goA = false;
                armTargetPosition = 500;
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
                rumbleBlip(gamepad2, 1);
            }

            if (goHome && (slidePosition <= 2000)) {
                slideTargetPosition = 0;
                armTargetPosition = 0;
            }

            if (goHome && (armPosition <= 50)) {
                goHome = false;
                rumble(gamepad2, 500, 0.8, 0.8);
            }

            //Go X
            if (goX && (slidePosition <= 2000)) {
                armTargetPosition = 200;
                goX = false;
            }

            if (goLowX && (armPosition >= 175)) {
                slideTargetPosition = 0;
                goLowX = false;
            }

            //Go Y
            if (goY && (armPosition >= 950)) {
                slideTargetPosition = slideLimit;
                wrist1.setPosition(0.75);
                wrist2.setPosition(0.25);
                goY = false;
            }

            //Go B
            if (goA && (armPosition >= 800)) {
                slideTargetPosition = 1500;
                wrist1.setPosition(0.85);
                wrist2.setPosition(0.15);
                goA = false;
            }

            //Slide Power
            slide();

            //Arm Power
            Arm.MentorBot.arm();

            if (gamepad1.touchpadWasPressed()) {
                rumbleWiiii(gamepad1, 5000);
            }

            if (gamepad2.touchpadWasPressed()) {
                rumbleWiiii(gamepad2, 5000);
            }
        }
    }
}
