package org.firstinspires.ftc.teamcode.auto.park;

import static org.firstinspires.ftc.teamcode.modules.Arm.wrist1;
import static org.firstinspires.ftc.teamcode.modules.Arm.wrist2;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Park Red Short", group = "park")
public class ParkRedShort extends LinearOpMode {
    @Override
    public void runOpMode()  {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        Pose2d start = new Pose2d(16, -63, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.None)
                        .lineToY(-55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(48)
                        .turnTo(Math.toRadians(90))
                        .lineToY(-63)
                        .build());
    }
}
