package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.bar.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bucket Blue Long", group = "bar")
public class BucketBlueLong extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        boolean goY = false;

        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(55)
                        .turnTo(Math.toRadians(45))
                        .lineToX(53)
                        .build());
        init_arm(hardwareMap);
        slideTarget = 1500;
        armTarget = 1100;
        wrist1.setPosition(0.7);
        wrist2.setPosition(0.3);
        goY = true;
        while (opModeIsActive()) {
            arm_loop();
            arm();
            if (goY && Math.abs(armPosition - armTarget) <= armTolerance) {
                slideTarget = slideLimit;
                armTarget = 1200;
                goY = false;
            }
        }
    }
}
