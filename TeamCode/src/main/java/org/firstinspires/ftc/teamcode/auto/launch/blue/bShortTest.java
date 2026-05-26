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

@Autonomous(name="bShortTest", group = "auto")
public class bShortTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Start: Back to wall, directly under center AprilTag (X = -36)
        // Position derived from meepmeeptesting.java
        Pose2d start = Auto.startBlueShort;
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        if (Robot.robotName().equals("nancy")) {
            TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                    // Initial Setup: Start intake/flywheel while driving
                    .afterTime(0, new Auto.StartIntake())
                    .afterTime(0, new Auto.StartFlyWheelLow())
                    .afterTime(0, new Auto.loaderDown())
                    .afterTime(0, new Auto.setSpindexerPos4())

                    // 1. Initial drive to shooting position and face goal
                    .strafeToLinearHeading(Auto.shootBlueShort, Auto.shootBlueShortAngle)
                    .waitSeconds(0.2)
                    .stopAndAdd(new Auto.Shoot())

                    // 5. Pickup from line 2
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueLine2, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.BlueMid2, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BluePickup2, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos3())
                    .waitSeconds(0.5)

                    // 6. Open Gate maneuver 1
//                    .strafeToLinearHeading(Auto.moveToBlueGate, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.openBlueGate, Auto.openBlueGateAngle)

                    // 4. Return to shooting pos
                    .strafeToLinearHeading(new Vector2d(3, -25), Math.toRadians(270))

                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueShort, Auto.shootBlueShortAngle)
                    .stopAndAdd(new Auto.Shoot())


                    // 2. Pickup from line 1
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueLine1, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(Auto.BlueMid1, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BluePickup1, Auto.BlueLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos3())
                    .waitSeconds(0.5)


                    // 3. Open Gate maneuver 2
                    .strafeToLinearHeading(Auto.moveToBlueGate, Auto.BlueLoadAngle)
                    .strafeToLinearHeading(new Vector2d(3, -45), Math.toRadians(360))
                    .strafeToLinearHeading(Auto.openBlueGate, Auto.openBlueGateAngle)

                    // 7. Return and Launch
                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueShort, Auto.shootBlueShortAngle)
                    .stopAndAdd(new Auto.Shoot())

                    // 8. Pickup 3 from last line (X = 12)
//                    .afterTime(0, new Auto.setSpindexerPos1())
//                    .strafeToLinearHeading(Auto.BlueLine3, Auto.BlueLoadAngle)
//                    .afterTime(0.1, new Auto.setSpindexerPos2())
//                    .strafeToLinearHeading(Auto.BluePickup3, Auto.BlueLoadAngle)
//                    .afterTime(0.1, new Auto.setSpindexerPos3())
//                    .waitSeconds(0.5)

//                    // 9. Open Gate maneuver 3
//                    .strafeToLinearHeading(new Vector2d(6, -45), Auto.BlueLoadAngle)
//                    .strafeToLinearHeading(new Vector2d(0, -53), Auto.BlueLoadAngle)

                    // 10. Final Return and Launch
//                    .afterTime(0, new Auto.setSpindexerPos4())
//                    .afterTime(0.2, new Auto.ReverseIntake())
//                    .afterTime(0.6, new Auto.StartIntake())
//                    .strafeToLinearHeading(Auto.shootBlueShort, Math.toRadians(225))
//                    .stopAndAdd(new Auto.Shoot())

                    // 11. Park outside big triangle
                    .strafeToLinearHeading(Auto.ShortBlueEndPosit, Math.toRadians(0));

            waitForStart();

            if (Launcher.turret != null) {
                Launcher.turret.setPosition(0.5);
            }

            // Run Spindexer PID, Limelight Tracking (ID 20, Offset -1.0), and Drivetrain path in parallel
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(), 
                    new Auto.AutoTurretTrack(20, -1.0),
                    step1.build()               
            ));
        }
    }
}
