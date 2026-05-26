package org.firstinspires.ftc.teamcode.auto.launch.red;

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


@Autonomous(name="rLong", group = "auto")
public class rLong extends LinearOpMode {

    @Override
    public void runOpMode()  {
        // MirroRed start pose for Red Corner
        Pose2d start = Auto.startRedLong;
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        if (Robot.robotName().equals("nancy")) {
            TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                    // Start systems immediately while driving to first shot
                    .afterTime(0, new Auto.StartIntake())
                    .afterTime(0, new Auto.StartFlyWheel())
                    .afterTime(0, new Auto.loaderDown())
                    .afterTime(0, new Auto.setSpindexerPos4())

                    // Drive to shooting position (MirroRed Heading 180-162 = 18)
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)
                    .waitSeconds(0.25)

                    // SHOOT 1 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    // LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())

                    // MirroRed pickup trajectories
                    .strafeToLinearHeading(Auto.RedCornerPrep, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.RedCornerGoto, Math.toRadians(90))
                    .afterTime(.25, new Auto.setSpindexerPos2())
                    .waitSeconds(.25)
                    .afterTime(.5, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.RedCornerSlide, Math.toRadians(90))

                    .waitSeconds(.5)

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)

                    // SHOOT 2 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    // 5. Pickup from line 3
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.RedLine3, Auto.RedLoadAngle)
                    .strafeToLinearHeading(Auto.RedMid3, Auto.RedLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.RedPickup3, Auto.RedLoadAngle)
                    .afterTime(0.25, new Auto.setSpindexerPos3())
                    .waitSeconds(0.5)

                    // Return
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)

                    // SHOOT 4 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    // LOAD 2
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.RedCornerPrep2, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.RedCornerBackup, Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .afterTime(.25, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.RedCornerCorner, Math.toRadians(90))

                    // Return
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)

                    // SHOOT 3 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    .lineToX(Auto.LongRedEndPosit);

            waitForStart();

            // Center the turret at the START of the match
            if (Launcher.turret != null) {
                Launcher.turret.setPosition(0.5);
            }

            // Run background loops and the trajectory builder in parallel
            // Target ID 20 with -1.0 degree offset for Red Corner middle aim
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(),
                    new Auto.AutoTurretTrack(20, -2.0),
                    step1.build()
            ));
        }
    }
}
