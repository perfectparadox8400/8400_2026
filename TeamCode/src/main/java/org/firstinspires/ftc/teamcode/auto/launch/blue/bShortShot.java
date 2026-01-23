package org.firstinspires.ftc.teamcode.auto.launch.blue;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
import static org.firstinspires.ftc.teamcode.auto.Auto.startSpindexer;
//import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;

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

@Autonomous(name="bShortShot", group = "auto")
public class bShortShot extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-55.5,-45.5, 0.942478);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .stopAndAdd(new Auto.StartIntake())
                .stopAndAdd(new Auto.StartFlyWheel())
                .stopAndAdd(new Auto.setSpindexerPos4())
                .lineToX(-30)
                .turnTo(Math.toRadians(234))
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.setSpindexerPos5())
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.5)
                .stopAndAdd(new Auto.setSpindexerPos6())
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .turnTo(Math.toRadians(180))
                .lineToX(-11.5)
                .turnTo(Math.toRadians(270))
                .stopAndAdd(new Auto.setSpindexerPos1())
                .lineToY(-36)
                .stopAndAdd(new Auto.setSpindexerPos2())
                .lineToY(-42)
                .stopAndAdd(new Auto.setSpindexerPos3())
                .lineToY(-55)
                .lineToY(-10)
                .turnTo(Math.toRadians(180))
                .lineToX(-30)
                .turnTo(Math.toRadians(234))
                .stopAndAdd(new Auto.setSpindexerPos4())
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(1)
                .stopAndAdd(new Auto.setSpindexerPos5())
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(1)
                .stopAndAdd(new Auto.setSpindexerPos6())
                .waitSeconds(1)
                .stopAndAdd(new Auto.Shoot())
                .strafeTo(new Vector2d(10, -16));

        waitForStart();
        Actions.runBlocking(new ParallelAction(
                new Auto.UpdateSpindexer(),
                step1.build()
        ));
    }
}
