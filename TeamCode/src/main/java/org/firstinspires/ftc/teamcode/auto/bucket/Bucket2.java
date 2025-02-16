package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.modules.Arm.init_arm;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.modules.Bucket;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Bucket 2", group = "bucket")
public class Bucket2 extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .lineToY(52);

        TrajectoryActionBuilder step2 = step1.endTrajectory().fresh()
                .turnTo(Math.toRadians(0))
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step3 = step2.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5, 45))
                .turnTo(Math.toRadians(270))
                .lineToY(44);

        TrajectoryActionBuilder step4 = step3.endTrajectory().fresh()
                .lineToY(36);

        TrajectoryActionBuilder step5 = step4.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(52.5, 52))
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step6 = step5.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(61.5, 40))
                .turnTo(Math.toRadians(90))
                .lineToY(12)
                .strafeToConstantHeading(new Vector2d(66, 12))
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(60, 50))
                .strafeToConstantHeading(new Vector2d(55, 12))
                .strafeToConstantHeading(new Vector2d(48, 12))
                .turnTo(Math.toRadians(80))
                .lineToY(54)
                .lineToY(45);

        Bucket.resetBucket();

        waitForStart();
        Actions.runBlocking(new SequentialAction(
                Auto.armStart(),
                new ParallelAction(
                        Auto.armLoop(),
                        Auto.setPose(drive),
                        new SequentialAction(
                                step1.build(),
                                new ParallelAction(
                                        Bucket.armBucket(),
                                        new SequentialAction(
                                                step2.build(),
                                                Bucket.setInPosition(),
                                                Bucket.bucketDone(),
                                                step3.build(),
                                                Bucket.getSample(),
                                                step4.build(),
                                                Bucket.grabSample(),
                                                new ParallelAction(
                                                        Bucket.armBucket(),
                                                        new SequentialAction(
                                                                step5.build(),
                                                                Bucket.setInPosition(),
                                                                Bucket.bucketDone(),
                                                                step6.build()
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
    }
}