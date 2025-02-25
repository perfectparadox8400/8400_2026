package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Teleop {
    public static boolean goX = false;
    public static boolean goY = false;
    public static boolean goHome = false;
    public static boolean start = false;
    public static final FtcDashboard dashboard = FtcDashboard.getInstance();
    private static Telemetry telemetry = null;

    public static void init(HardwareMap hardwareMap, Telemetry robot_telemetry) {
        //Init Arm
        init_arm(hardwareMap);
        //Init Wheels
        init_drive(hardwareMap);
        telemetry = new MultipleTelemetry(robot_telemetry, dashboard.getTelemetry());
    }

    public static void start() {
        goX = false;
        goY = false;
        goHome = false;
        start = true;
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        hang1.setPosition(1);
        hang2.setPosition(0);
    }

    public static void play_loop(Gamepad gamepad1, Gamepad gamepad2) {
        //Arm Loop
        arm_loop();

        //Controller Button
        boolean change = !gamepad1.start && !gamepad2.start;

        //Controller1 Buttons
        DrivePower = 2;
        if ((change && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        }
        if ((change && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        }

        if (gamepad1.dpad_down) {
            hang1.setPosition(hang1.getPosition() + .012);
            hang2.setPosition(hang2.getPosition() - .012);
        } else if (gamepad1.dpad_up) {
            hang1.setPosition(hang1.getPosition() - .012);
            hang2.setPosition(hang2.getPosition() + .012);
        } else if (gamepad1.dpad_left) {
            hang1.setPosition(1);
            hang2.setPosition(0);
        } else if (gamepad1.dpad_right) {
            hang1.setPosition(0.65);
            hang2.setPosition(0.35);
        }

        //Drive Power
        drive(gamepad1);

        if (gamepad2.guide) {
            start = false;
            goX = false;
            goY = false;
            goHome = false;
        }

        if(start) {
            if (slideBack.isPressed()){
                slide_reset(false);
                if(armDown.isPressed()){
                    arm_reset(false);
                    start = false;
                } else {
                    arm_reset(true);
                    arm();
                }
            } else {
                slide_reset(true);
            }
            return;
        }

        //Controller2 Buttons

        /* ------ SLIDE LOGIC ------ */
        if (gamepad2.left_bumper) {
            slideTargetPosition -= 55;
        } else if (gamepad2.right_bumper) {
            slideTargetPosition += 55;
        }

        /* ------ RESET ------ */
        if (gamepad2.back) {
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
            slideTargetPosition = 0;
            goHome = true;
        }

        /* ------WRIST LOGIC------ */
        if (gamepad2.dpad_up) {
            wrist1.setPosition(wrist1.getPosition() + .012);
            wrist2.setPosition(wrist2.getPosition() - .012);
        }
        if (gamepad2.dpad_down) {
            wrist1.setPosition(wrist1.getPosition() - .012);
            wrist2.setPosition(wrist2.getPosition() + .012);
        }

        /* ------ MAIN BOOM LOGIC ------ */
        if (gamepad2.left_stick_y > 0.2){
            armTargetPosition -= 10;
        } else if (gamepad2.left_stick_y < -0.2){
            armTargetPosition += 15;
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

        if ((change && gamepad2.b)) {
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
        }

        if ((change && gamepad2.a)) {
            wrist1.setPosition(0.8);
            wrist2.setPosition(0.2);
        }

        /* ------ PRESETS ------ */
        if (gamepad2.x) {
            goX = true;
            if (armPosition < 200) {
                slideTargetPosition = 0;
                armTargetPosition = 200;
            } else {
                slideTargetPosition = 0;
            }
            wrist1.setPosition(0.4);
            wrist2.setPosition(0.6);
        }

        if (gamepad2.y) {
            goY = true;
            armTargetPosition = armLimit;
            wrist1.setPosition(0.5);
            wrist2.setPosition(0.5);
        }

        turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) / DriveSlowPower;
        strafeFactor = 0.0;
        if (gamepad2.dpad_left) {
            strafeFactor = -1.0 / DrivePower; // Strafe left
        } else if (gamepad2.dpad_right) {
            strafeFactor = 1.0 / DrivePower; // Strafe right
        }

        if (goHome && (slidePosition <= 2000)) {
            slideTargetPosition = 0;
            armTargetPosition = 0;
        }

        if (goHome && armDown.isPressed()) {
            goHome = false;
        }

        //Go X
        if (goX && (slidePosition <= 2000)) {
            armTargetPosition = 200;
            goX = false;
        }

        //Go Y
        if (goY && (armPosition >= 950)) {
            slideTargetPosition = slideLimit;
            wrist1.setPosition(0.6);
            wrist2.setPosition(0.4);
            goY = false;
        }

        //Slide Power
        slide();

        //Arm Power
        arm();
    }

    public static void telemetry_drive() {
        //Telemetry
        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

        telemetry.update();
    }

    public static void telemetry_auto(MecanumDrive drive) {
        //Telemetry
        auto_telemetry(telemetry, drive);
        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

        telemetry.update();
    }
}
