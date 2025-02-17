package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auto.PoseStorage;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "! Play Auto")
public class PlayAuto extends OpMode {
    public boolean goX = false;
    public boolean goY = false;
    public boolean goHome = false;
    public boolean start = false;

    private MecanumDrive drive = null;

    private final FtcDashboard dashboard = FtcDashboard.getInstance();

    @Override
    public void init() {
        //Init Arm
        init_arm(hardwareMap);
        //Init Wheels
        init_drive(hardwareMap);

        drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void start() {
        start = true;
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
    }

    @Override
    public void loop() {
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

        //Drive Power
        drive(gamepad1);

        auto_telemetry(telemetry, drive);
        drive_telemetry(telemetry);
        arm_telemetry(telemetry);
        auto_drive(drive, dashboard);
        telemetry.update();

        if (gamepad2.guide) {
            start = false;
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
            if(wrist1.getPosition() < 0.15 || wrist2.getPosition() > 0.85) {
                wrist1.setPosition(0.15);
                wrist2.setPosition(0.85);
            }
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

        if (gamepad2.b) {
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
        }

        if (gamepad2.a) {
            wrist1.setPosition(0.8);
            wrist2.setPosition(0.2);
        }

        /* ------ PRESETS ------ */
        if (gamepad2.x) {
            goX = true;
            slideTargetPosition = 0;
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
}