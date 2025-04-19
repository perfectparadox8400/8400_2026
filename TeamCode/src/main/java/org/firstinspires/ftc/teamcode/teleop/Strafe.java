package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Strafe")
public class Strafe extends OpMode {
    @Override
    public void init() {
        init_drive(hardwareMap);
        init_arm(hardwareMap);
    }
    @Override
    public void start() {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        hang1.setPosition(0);
        hang2.setPosition(1);
    }
    @Override
    public void loop() {
        boolean change = !gamepad1.start && !gamepad2.start;

        DrivePower = 2;
        if ((change && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        }
        if ((change && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        }

        if (!gamepad1.x) {
            drive(gamepad2, 2.5);
        }

        drive(gamepad1, DrivePower);

        brake(gamepad1.x);

        drive_telemetry(telemetry);

        telemetry.update();
    }
}