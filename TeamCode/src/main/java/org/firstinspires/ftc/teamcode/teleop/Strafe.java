package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Controller.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Strafe")
public class Strafe extends OpMode {
    private boolean fieldDriving = false;
    @Override
    public void init() {
        init_drive(hardwareMap);
        telemetry.addData("Press", "Start");
        telemetry.update();
    }
    @Override
    public void loop() {
        if ((controllerChange(gamepad1, gamepad2) && gamepad1.bWasPressed()) || gamepad1.rightBumperWasPressed()) {
            rumble(gamepad1, 250, 0.8, 0.8);
        } else if ((controllerChange(gamepad1, gamepad2) && gamepad1.aWasPressed()) || gamepad1.leftBumperWasPressed()) {
            rumble(gamepad1, 250, 0.3, 0.3);
        }
        if ((controllerChange(gamepad1, gamepad2) && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        } else if ((controllerChange(gamepad1, gamepad2) && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        } else {
            DrivePower = 2;
        }

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

        if (gamepad1.xWasPressed()) {
            rumbleBlip(gamepad1, 3);
        }

        if (gamepad1.x) {
            brake(true);
        } else {
            drive(gamepad2, 2.5);
        }

        if (gamepad1.xWasReleased()) {
            brake(false);
        }

        if (gamepad1.yWasReleased()){
            rumbleBlip(gamepad1, 1);
            setBrakeMode(!brakeMode);
        }

        drive_telemetry(telemetry);
        telemetry.update();
    }
}