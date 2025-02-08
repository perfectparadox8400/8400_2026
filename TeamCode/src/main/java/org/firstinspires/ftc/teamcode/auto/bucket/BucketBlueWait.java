package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Start;
import org.firstinspires.ftc.teamcode.auto.bar.Bar;
import org.firstinspires.ftc.teamcode.auto.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bucket Blue Wait", group = "bucket")
public class BucketBlueWait extends LinearOpMode {
    @Override
    public void runOpMode()  {
        init_arm(hardwareMap);
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(Wait.Wait)
                .lineToY(52)
                .turnTo(Math.toRadians(0));

        TrajectoryActionBuilder step2 = step1.endTrajectory().fresh()
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54);

        TrajectoryActionBuilder step3 = step2.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5,40))
                .turnTo(Math.toRadians(270))
                .lineToY(35);

        TrajectoryActionBuilder step4 = step3.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45));

        TrajectoryActionBuilder step5 = step4.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(48.5,40))
                .turnTo(Math.toRadians(270))
                .lineToY(35);

        TrajectoryActionBuilder step6 = step5.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45));

        TrajectoryActionBuilder step7 = step6.endTrajectory().fresh()
                .turnTo(Math.toRadians(270))
                .lineToY(10)
                .turnTo(Math.toRadians(180))
                .lineToX(25);

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
                                                Bucket.getSample(),
                                                step3.build(),
                                                Bucket.grabSample(),
                                                new ParallelAction(
                                                        Bucket.armBucket(),
                                                        new SequentialAction(
                                                                step4.build(),
                                                                Bucket.setInPosition(),
                                                                Bucket.getSample(),
                                                                step5.build(),
                                                                Bucket.grabSample(),
                                                                new ParallelAction(
                                                                        Bucket.armBucket(),
                                                                        new SequentialAction(
                                                                                step6.build(),
                                                                                Bucket.setInPosition(),
                                                                                step7.build(),
                                                                                Bar.armBar()
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
    }
}