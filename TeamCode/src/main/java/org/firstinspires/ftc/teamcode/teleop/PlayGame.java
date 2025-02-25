package org.firstinspires.ftc.teamcode.teleop;

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
        Teleop.play_loop(gamepad1, gamepad2);
        Teleop.telemetry_drive();
    }
}