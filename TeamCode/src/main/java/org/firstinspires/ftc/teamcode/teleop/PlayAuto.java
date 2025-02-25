package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Drive.*;
import static org.firstinspires.ftc.teamcode.teleop.Teleop.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auto.PoseStorage;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@TeleOp(name = "! Play Zauto")
public class PlayAuto extends OpMode {
    private MecanumDrive drive = null;

    @Override
    public void init() {
       Teleop.init(hardwareMap, telemetry);
       drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);
    }

    @Override
    public void start() {
        Teleop.start();
    }

    @Override
    public void loop() {
        Teleop.play_loop(gamepad1, gamepad2);
        //Drive Power
        drive(gamepad1);

        auto_drive(drive, dashboard);
        telemetry_auto(drive);
    }
}