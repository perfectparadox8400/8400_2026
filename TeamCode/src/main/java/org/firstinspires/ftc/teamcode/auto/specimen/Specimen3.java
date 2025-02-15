package org.firstinspires.ftc.teamcode.auto.specimen;

import static org.firstinspires.ftc.teamcode.modules.Arm.init_arm;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Start;
import org.firstinspires.ftc.teamcode.auto.Wait;
import org.firstinspires.ftc.teamcode.auto.bucket.Bucket;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bucket 3", group = "bucket")
public class Specimen3 extends LinearOpMode {
    @Override
    public void runOpMode()  {
        init_arm(hardwareMap);
        Pose2d start = new Pose2d(-8, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

//                 .lineToY(38)
//                .turnTo(Math.toRadians(90))
//                .strafeToConstantHeading(new Vector2d(-35,58))
//                .turnTo(Math.toRadians(270))
//                .strafeToConstantHeading(new Vector2d(-4,38))
//                .turnTo(Math.toRadians(90))
//                .strafeToConstantHeading(new Vector2d(-35,58))
//                .turnTo(Math.toRadians(270))
//                .strafeToConstantHeading(new Vector2d(-2,38))

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(Wait.None)
                .lineToY(52);

        TrajectoryActionBuilder step2 = step1.endTrajectory().fresh()
                .turnTo(Math.toRadians(0))
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step3 = step2.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5,45))
                .turnTo(Math.toRadians(270))
                .lineToY(44);

        TrajectoryActionBuilder step4 = step3.endTrajectory().fresh()
                .lineToY(36);

        TrajectoryActionBuilder step5 = step4.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(52.5,52))
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step6 = step5.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(48.5,45))
                .turnTo(Math.toRadians(270))
                .lineToY(44);

        TrajectoryActionBuilder step7 = step6.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(52.5,52))
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step8 = step7.endTrajectory().fresh()
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(61.5,40))
                .turnTo(Math.toRadians(90))
                .lineToY(12)
                .strafeToConstantHeading(new Vector2d(66,12))
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(60,50))
                .lineToY(45);

        Bucket.resetBucket();
        waitForStart();
        Actions.runBlocking(new SequentialAction(
                Start.armStart(),
                new ParallelAction(
                        Start.armLoop(),
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