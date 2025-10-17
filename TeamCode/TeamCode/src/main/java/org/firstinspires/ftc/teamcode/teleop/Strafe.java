package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Controller.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Strafe")
public class Strafe extends OpMode {
    @Override
    public void init() {
        init_drive(hardwareMap);
        telemetry.addData("Press", "Start");
        telemetry.update();
    }
    @Override
    public void loop() {
        speedControl(gamepad1, gamepad2);

        if (gamepad1.start && gamepad1.guideWasReleased()) {
            rumbleBlip(gamepad1, 2);
            fieldDriving = !fieldDriving;
        }

        if (gamepad1.back && gamepad1.guideWasReleased()) {
            rumbleBlip(gamepad1, 4);
            imu.resetYaw();
        }

        if (gamepad1.xWasPressed()) {
            rumbleBlip(gamepad1, 3);
        }

        if (gamepad1.x) {
            brake(true);
        } else if (!controllerJoyStickActive(gamepad1)) {
            drive(gamepad2, 0.5);
        }

        if (gamepad1.xWasReleased()) {
            brake(false);
        }
        if (controllerJoyStickActive(gamepad1)) {
            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
            }
        }

        if (gamepad1.yWasReleased()){
            rumbleBlip(gamepad1, 1);
            setBrakeMode(!brakeMode);
        }

        drive_telemetry(telemetry);
        telemetry.update();
    }
}