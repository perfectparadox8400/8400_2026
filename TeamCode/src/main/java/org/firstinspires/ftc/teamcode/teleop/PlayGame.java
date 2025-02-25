package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Drive.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {

    @Override
    public void init() {
        Teleop.init(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        Teleop.start();
    }

    @Override
    public void loop() {
        //Drive Power
        drive(gamepad1);

        Teleop.play_loop(gamepad1, gamepad2);
        Teleop.telemetry_drive();
    }
}