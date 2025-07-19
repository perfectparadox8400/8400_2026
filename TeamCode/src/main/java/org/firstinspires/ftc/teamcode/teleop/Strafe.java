package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Controller.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Strafe")
public class Strafe extends OpMode {

    boolean xButton = false;

    @Override
    public void init() {
        init_drive(hardwareMap);
        telemetry.addData("Press", "Start");
        telemetry.update();
    }
    @Override
    public void loop() {
        if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        } else if (((controllerChange(gamepad1) || controllerChange(gamepad2)) && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        } else {
            DrivePower = 2;
        }

        if (!gamepad1.x) {
            drive(gamepad2, 2.5);
        }

        drive(gamepad1, DrivePower);

        if (gamepad1.x) {
            brake(true);
            xButton = true;
        } else {
            if (xButton) {
                xButton = false;
                brake(false);
            }
        }

        drive_telemetry(telemetry);
        telemetry.update();
    }
}