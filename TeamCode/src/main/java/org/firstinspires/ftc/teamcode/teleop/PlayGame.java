package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {
    public boolean goX = false;
    public boolean goY = false;
    public boolean goLowX = false;
    public boolean goHome = false;
    public boolean start = false;
    public final FtcDashboard dashboard = FtcDashboard.getInstance();
    private boolean armLimitReset = false;
    private boolean armDownReset = false;

    @Override
    public void init() {
        //Init Arm
        init_arm(hardwareMap);
        //Init Wheels
        init_drive(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void start() {
        goX = false;
        goY = false;
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

        if (gamepad1.dpad_down) {
            hang1.setPosition(hang1.getPosition() + .013);
            hang2.setPosition(hang2.getPosition() - .013);
        } else if (gamepad1.dpad_up) {
            hang1.setPosition(hang1.getPosition() - .013);
            hang2.setPosition(hang2.getPosition() + .013);
        } else if (gamepad1.dpad_left) {
            hang1.setPosition(1);
            hang2.setPosition(0);
        } else if (gamepad1.dpad_right) {
            hang1.setPosition(0);
            hang2.setPosition(1);
        }

        if (gamepad1.x) {
            armTargetPosition = -350;
            slideTargetPosition = 0;
            wrist1.setPosition(0);
            wrist2.setPosition(1);
            hang1.setPosition(1);
            hang2.setPosition(0);
            hang = true;
        }

        if (gamepad1.guide) {
            hang = false;
        }

        if (gamepad2.guide) {
            start = false;
            goX = false;
            goLowX = false;
            goY = false;
            goHome = false;
            armTargetPosition = armPosition;
            slideTargetPosition = slidePosition;
        }

        drive(gamepad1);

        //Telemetry
        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

        telemetry.addData("Start", start);
        telemetry.addData("goLowX", goLowX);
        telemetry.addData("goX", goX);
        telemetry.addData("goY", goY);
        telemetry.addData("goHome", goHome);


        telemetry.update();

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

        turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) / DriveSlowPower;
        strafeFactor = 0.0;
        if (gamepad2.dpad_left) {
            strafeFactor = -1.0 / DrivePower; // Strafe left
        } else if (gamepad2.dpad_right) {
            strafeFactor = 1.0 / DrivePower; // Strafe right
        }

        /* ------ SLIDE LOGIC ------ */
        if (gamepad2.left_bumper) {
            slideTargetPosition -= 55;
        } else if (gamepad2.right_bumper) {
            slideTargetPosition += 55;
        }

        /* ------ RESET ------ */
        if (gamepad2.back) {
            goY = false;
            goX = false;
            goLowX = false;
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

        if (gamepad2.left_stick_button && !armLimitReset) {
            armLimit += 50;
            armLimitReset = true;
        } else if (!gamepad2.left_stick_button) {
            armLimitReset = false;
        }

        if (gamepad2.right_stick_button && !armDownReset) {
            armDownLimit -= 10;
            armDownReset = true;
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
            goHome = false;
            wrist1.setPosition(0.4);
            wrist2.setPosition(0.6);
        }

        if (gamepad2.y) {
            goY = true;
            goX = false;
            goLowX = false;
            goHome = false;
            armTargetPosition = 1110;
            wrist1.setPosition(0.5);
            wrist2.setPosition(0.5);
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

        if (goLowX && (armPosition >= 175)) {
            slideTargetPosition = 0;
            goLowX = false;
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