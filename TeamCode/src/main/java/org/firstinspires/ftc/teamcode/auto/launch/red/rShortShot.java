package org.firstinspires.ftc.teamcode.auto.launch.red;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
//import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="rShortShot", group = "auto")
public class rShortShot extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-49,49, 5.340708);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .stopAndAdd(new Auto.StartIntake())
                .stopAndAdd(new Auto.StartFlyWheel())
                .lineToX(-25)
                .turnTo(Math.toRadians(126))
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.Shoot())
                .turnTo(Math.toRadians(180))
                .lineToX(-11.5)
                .turnTo(Math.toRadians(90))
                .lineToY(55)
                .lineToY(16)
                .turnTo(Math.toRadians(180))
                .lineToX(-25)
                .turnTo(Math.toRadians(126))
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.Shoot());

        waitForStart();
        Actions.runBlocking(new ParallelAction(
                Auto.setPose(drive),
                new SequentialAction(
                        step1.build()
                )
        ));
    }
}
