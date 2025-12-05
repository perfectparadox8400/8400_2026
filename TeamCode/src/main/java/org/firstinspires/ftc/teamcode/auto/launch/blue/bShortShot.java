package org.firstinspires.ftc.teamcode.auto.launch.blue;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="bShortShot", group = "auto")
public class bShortShot extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(61.5,14.5, 3.142);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .waitSeconds(5)
                .lineToX(36)
                .turnTo(Math.toRadians(90))
                .lineToY(36)
                .waitSeconds(0.5)
                .lineToY(40)
                .waitSeconds(0.5)
                .lineToY(45)
                .waitSeconds(0.5)
                .lineToY(14.5)
                .turnTo(Math.toRadians(180))
                .lineToX(61.5);

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
