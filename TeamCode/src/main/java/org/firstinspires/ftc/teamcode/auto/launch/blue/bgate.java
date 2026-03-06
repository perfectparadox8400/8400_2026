package org.firstinspires.ftc.teamcode.auto.launch.blue;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
import static org.firstinspires.ftc.teamcode.auto.Auto.startSpindexer;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="bgate", group = "auto")
public class bgate extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-37,-55, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .stopAndAdd(new Auto.StartIntake())
                .stopAndAdd(new Auto.StartFlyWheelLow())
                .stopAndAdd(new Auto.setSpindexerPos4())
                .stopAndAdd(new Auto.loaderDown())

                .strafeToLinearHeading(new Vector2d(-8, -14.5), Math.toRadians(217))

                //SHOOT
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos5())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos6())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .stopAndAdd(new Auto.setSpindexerPos1())

                .strafeToLinearHeading(new Vector2d(-11.5, -30), Math.toRadians(270))

                .strafeToLinearHeading(new Vector2d(-11.5, -55), Math.toRadians(270))

                //PICKUP
                .stopAndAdd(new Auto.setSpindexerPos2())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos3())

                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-2, -56.5, Math.toRadians(180)), Math.toRadians(360))

                .strafeToLinearHeading(new Vector2d(-8, -14.5), Math.toRadians(217))

                //SHOOT
                .stopAndAdd(new Auto.setSpindexerPos4())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos5())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos6())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .stopAndAdd(new Auto.setSpindexerPos1())

                .strafeToLinearHeading(new Vector2d(12.5, -30), Math.toRadians(270))

                .strafeToLinearHeading(new Vector2d(12.5, -61.5), Math.toRadians(270))

                //PICKUP
                .stopAndAdd(new Auto.setSpindexerPos2())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos3())

                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-8, -14.5, Math.toRadians(217)), Math.toRadians(180))

                //SHOOT
                .stopAndAdd(new Auto.setSpindexerPos4())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos5())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.setSpindexerPos6())
                .waitSeconds(0.75)
                .stopAndAdd(new Auto.Shoot())
                .stopAndAdd(new Auto.setSpindexerPos1())

                .strafeToLinearHeading(new Vector2d(-9, -30), Math.toRadians(0));

        waitForStart();
        Actions.runBlocking(new ParallelAction(
                new Auto.UpdateSpindexer(),
                step1.build()
        ));
    }
}
