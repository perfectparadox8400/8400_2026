package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Start;
import org.firstinspires.ftc.teamcode.auto.bar.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bucket Blue", group = "bucket")
public class BucketBlue extends LinearOpMode {
    @Override
    public void runOpMode()  {
        init_arm(hardwareMap);
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .stopAndAdd(Start.armStart())
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(55)
                        .turnTo(Math.toRadians(45))
                        .lineToX(53)
                        .stopAndAdd(Bucket.armBucket())
                        .turnTo(Math.toRadians(-180))
                        .lineToX(35)
                        .turnTo(Math.toRadians(270))
                        .lineToY(10)
                        .turnTo(Math.toRadians(180))
                        .lineToX(25)
                        .build());
    }
}
