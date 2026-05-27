package org.firstinspires.ftc.teamcode.auto.launch.blue;

import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
import static org.firstinspires.ftc.teamcode.auto.Auto.startSpindexer;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="bLongLines", group = "auto")
public class bLongLines extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Start: Back to wall, directly under center AprilTag (X = -36)
        // Position derived from meepmeeptesting.java
        Pose2d start = new Pose2d(61.5, -14.5, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        if (Robot.robotName().equals("nancy")) {
            TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                    // Initial Setup: Start intake/flywheel while driving
                    .afterTime(0, new Auto.StartIntake())
                    .afterTime(0, new Auto.StartFlyWheel())
                    .afterTime(0, new Auto.loaderDown())
                    .afterTime(0, new Auto.setSpindexerPos4())

                    // 1. Initial drive to shooting position and face goal
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)
                    .waitSeconds(0.2)
                    .stopAndAdd(new Auto.Shoot())

                    // 5. Pickup from corner
                    .strafeToLinearHeading(Auto.BlueCornerPrep, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.BlueCornerGoto, Auto.BlueLoadAngle)
                    .afterTime(.25, new Auto.setSpindexerPos2())
                    .waitSeconds(.25)
                    .afterTime(.5, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.BlueCornerSlide, Auto.BlueLoadAngle)

                    // 4. Return to shooting pos
                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)
                    .stopAndAdd(new Auto.Shoot())


                    // 2. Pickup from line 3
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueLine3, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.BlueMid3, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BluePickup3, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos3())
                    .waitSeconds(0.5)

                    // 7. Return and Launch
                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)
                    .stopAndAdd(new Auto.Shoot())

                    // 2. Pickup from line 2
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueLine2, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.BlueMid2, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BluePickup2, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos3())
                    .waitSeconds(0.5)

                    // 7. Return and Launch
                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)
                    .stopAndAdd(new Auto.Shoot())


                    .lineToX(Auto.LongBlueEndPosit);

            waitForStart();

            if (Launcher.turret != null) {
                Launcher.turret.setPosition(0.5);
            }

            // Run Spindexer PID, Limelight Tracking (ID 20, Offset -1.0), and Drivetrain path in parallel
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(),
                    new Auto.AutoTurretTrack(20, -2.0),
                    step1.build()
            ));
        }
    }
}
