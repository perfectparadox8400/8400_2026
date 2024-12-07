package org.firstinspires.ftc.teamcode.auto.bar;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bar Blue Long Wait", group = "bar")
public class BarBlueLongWait extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.Long)
                        .lineToY(10)
                        .turnTo(Math.toRadians(180))
                        .lineToX(25)
                        .build());
    }
}
