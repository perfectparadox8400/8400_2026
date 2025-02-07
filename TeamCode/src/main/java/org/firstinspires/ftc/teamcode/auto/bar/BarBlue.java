package org.firstinspires.ftc.teamcode.auto.bar;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import org.firstinspires.ftc.teamcode.auto.Start;
import org.firstinspires.ftc.teamcode.auto.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bar Blue", group = "bar")
public class BarBlue extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .stopAndAdd(Start.armStart())
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(33)
                        .turnTo(Math.toRadians(270))
                        .lineToY(10)
                        .turnTo(Math.toRadians(180))
                        .lineToX(28)
                        .stopAndAdd(Bar.armBar())
                        .build());
    }
}
