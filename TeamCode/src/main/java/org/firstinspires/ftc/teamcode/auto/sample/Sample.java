package org.firstinspires.ftc.teamcode.auto.sample;

import static org.firstinspires.ftc.teamcode.auto.Auto.*;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Sample", group = "sample")
public class Sample extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-16, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startArm(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .lineToY(55)
                .strafeToConstantHeading(new Vector2d(-35, 55))
                .strafeTo(new Vector2d(-35, 10))
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(-44, 10))
                .turnTo(Math.toRadians(90))
                .lineToY(55)
                .lineToY(10)
                .strafeToConstantHeading(new Vector2d(-54, 10))
                .turnTo(Math.toRadians(90))
                .lineToY(55)
                .lineToY(10)
                .strafeToConstantHeading(new Vector2d(-61, 10))
                .turnTo(Math.toRadians(90))
                .lineToY(55)
                .lineToY(48)
                .strafeToConstantHeading(new Vector2d(-48, 48))
                .turnTo(Math.toRadians(270))
                .lineToY(60);

        waitForStart();
        startWrist();
        Actions.runBlocking(new ParallelAction(
                Auto.setPose(drive),
                new SequentialAction(
                        step1.build()
                )
        ));
    }
}
