package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Strafe")
public class Strafe extends OpMode {
    @Override
    public void init() {
        init_drive(hardwareMap);
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

        drive(gamepad1);

        drive_telemetry(telemetry);

        telemetry.update();
    }
}