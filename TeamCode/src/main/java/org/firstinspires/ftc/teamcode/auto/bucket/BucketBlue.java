package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Start;
import org.firstinspires.ftc.teamcode.auto.bar.Bar;
import org.firstinspires.ftc.teamcode.auto.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bucket Blue", group = "bucket")
public class BucketBlue extends LinearOpMode {
    @Override
    public void runOpMode()  {
        init_arm(hardwareMap);
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        opModeIsActive();
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .stopAndAdd(Start.armStart()) // Reset arm
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(55)
                        .turnTo(Math.toRadians(55))
                        .lineToX(54)
                        .stopAndAdd(Bucket.armBucket())
                        .turnTo(Math.toRadians(0))
                        .lineToX(60)
                        .turnTo(Math.toRadians(270))
                        .lineToY(36)
                        .turnTo(Math.toRadians(-175))
                        .lineToX(38)
                        .turnTo(Math.toRadians(280))
                        .lineToY(10)
                        .turnTo(Math.toRadians(190))
                        .lineToX(28)
                        .stopAndAdd(Bar.armBar())
                        .build());
    }
}

/*
                .stopAndAdd(Start.armStart()) // Reset arm
                .waitSeconds(Wait.None)
                .lineToY(52)
                .turnTo(Math.toRadians(0))
                .stopAndAdd(Bucket.armBucket())
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54)
                .stopAndAdd(Bucket.setInPosition()) // Bucket 1
                .stopAndAdd(Bucket.getSample())
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5,40))
                .turnTo(Math.toRadians(270))
                .stopAndAdd(Arm.intakeIn())
                .lineToY(35)
                .stopAndAdd(Bucket.grabSample())
                .stopAndAdd(Bucket.armBucket())
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45))
                .stopAndAdd(Bucket.setInPosition()) // Bucket 2
                .stopAndAdd(Bucket.getSample())
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(48.5,40))
                .turnTo(Math.toRadians(270))
                .stopAndAdd(Arm.intakeIn())
                .lineToY(35)
                .stopAndAdd(Bucket.grabSample())
                .stopAndAdd(Bucket.armBucket())
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45))
                .stopAndAdd(Bucket.setInPosition()) // Bucket 3
                .turnTo(Math.toRadians(270))
                .lineToY(10)
                .turnTo(Math.toRadians(180))
                .lineToX(28)
                .stopAndAdd(Bar.armBar())
*/