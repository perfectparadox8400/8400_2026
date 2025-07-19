package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Controller.*;
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
    public boolean goB = false;
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

    @Override
    public void loop() {
        //Arm Loop
        arm_loop();

        //Controller1 Buttons
        DrivePower = 2;
        if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        }
        if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        }

        if (gamepad1.dpad_down && (hang1.getPosition() <= hookOpen1 && hang2.getPosition() >= hookOpen2)) {
            hang1.setPosition(hang1.getPosition() + .013);
            hang2.setPosition(hang2.getPosition() - .013);
        } else if (gamepad1.dpad_up && (hang1.getPosition() >= hookClosed1 && hang2.getPosition() <= hookClosed2)) {
            hang1.setPosition(hang1.getPosition() - .013);
            hang2.setPosition(hang2.getPosition() + .013);
        } else if (gamepad1.dpad_left) {
            hang1.setPosition(hookOpen1);
            hang2.setPosition(hookOpen2);
        } else if (gamepad1.dpad_right) {
            hang1.setPosition(hookClosed1);
            hang2.setPosition(hookClosed2);
        }

        if (gamepad1.x) {
            armTargetPosition = -350;
            slideTargetPosition = 0;
            wrist1.setPosition(0);
            wrist2.setPosition(1);
            hang1.setPosition(0);
            hang2.setPosition(1);
            hang = true;
        }

        if (gamepad1.guide) {
            hang = false;
            rumble(gamepad1, 1000, 1, 1);
        }

        if (gamepad2.guide) {
            start = false;
            goX = false;
            goLowX = false;
            goY = false;
            goHome = false;
            goB = false;
            armTargetPosition = armPosition;
            slideTargetPosition = slidePosition;
            rumble(gamepad2, 1000, 1, 1);
        }

        drive(gamepad1, DrivePower);

        //Telemetry
        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

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
        if (gamepad2.dpad_left) {
            strafeFactor = -1.0 / DrivePower; // Strafe left
        } else if (gamepad2.dpad_right) {
            strafeFactor = 1.0 / DrivePower; // Strafe right
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
        if (gamepad2.back) {
            goY = false;
            goX = false;
            goLowX = false;
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
            slideTargetPosition = 0;
            goHome = true;
            rumble(gamepad2, 500, 0.8, 0.8);
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

        if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad2.b)) {
            armTargetPosition = 900;
            goB = true;
            rumbleBlip(gamepad2, 1);
        }

        if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad2.a)) {
            wrist1.setPosition(0.8);
            wrist2.setPosition(0.2);
            rumbleBlip(gamepad2, 1);
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
            goB = false;
            goHome = false;
            wrist1.setPosition(0.4);
            wrist2.setPosition(0.6);
            rumbleBlip(gamepad2, 1);
        }

        if (gamepad2.y) {
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
            rumble(gamepad2, 1000, 0.8, 0.8);
        }

        //Go X
        if (goX && (slidePosition <= 2000)) {
            armTargetPosition = 200;
            goX = false;
            rumbleBlip(gamepad2, 2);
        }

        if (goLowX && (armPosition >= 175)) {
            slideTargetPosition = 0;
            goLowX = false;
            rumbleBlip(gamepad2, 2);
        }

        //Go Y
        if (goY && (armPosition >= 950)) {
            slideTargetPosition = slideLimit;
            wrist1.setPosition(0.75);
            wrist2.setPosition(0.25);
            goY = false;
            rumbleBlip(gamepad2, 2);
        }

        //Go B
        if (goB && (armPosition >= 800)) {
            slideTargetPosition = 1500;
            wrist1.setPosition(0.85);
            wrist2.setPosition(0.15);
            goB = false;
            rumbleBlip(gamepad2, 2);
        }

        //Slide Power
        slide();

        //Arm Power
        arm();

        if (gamepad1.touchpadWasPressed()) {
            rumbleWiiii(gamepad1, 5000);
        }

        if (gamepad2.touchpadWasPressed()) {
            rumbleWiiii(gamepad2, 5000);
        }
    }
}