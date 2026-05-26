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


@Autonomous(name="bcornerFast", group = "auto")
public class bcornerFast extends LinearOpMode {

    @Override
    public void runOpMode()  {
        // Mirrored start pose for Blue Corner
        Pose2d start = Auto.startBlueLong;
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

                    // Drive to shooting position (Mirrored Heading 180-162 = 18)
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)
                    .waitSeconds(0.25)

                    // SHOOT 1 Sequence
                    .stopAndAdd(new Auto.Shoot())
                    
                    // LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())

                    // Mirrored pickup trajectories
                    .strafeToLinearHeading(Auto.BlueCornerPrep, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.BlueCornerGoto, Math.toRadians(90))
                    .afterTime(.25, new Auto.setSpindexerPos2())
                    .waitSeconds(.25)
                    .afterTime(.5, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.BlueCornerSlide, Math.toRadians(90))

                    .waitSeconds(.5)

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)

                    // SHOOT 2 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    // LOAD 2
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueCornerPrep2, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.BlueCornerBackup, Math.toRadians(90))
                    .afterTime(.5, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BlueCornerSlide, Math.toRadians(90))
                    .afterTime(.25, new Auto.setSpindexerPos3())

                    .waitSeconds(.5)

                    // Return
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)

                    // SHOOT 3 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    // LOAD 3
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.BlueCornerPrep2, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.BlueCornerBackup, Math.toRadians(90))
                    .afterTime(.5, new Auto.setSpindexerPos2())
                    .strafeToLinearHeading(Auto.BlueCornerSlide, Math.toRadians(90))
                    .afterTime(.5, new Auto.setSpindexerPos3())

                    // Return
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootBlueLong, Auto.shootBlueLongAngle)

                    // SHOOT 4 Sequence
                    .stopAndAdd(new Auto.Shoot())

                    .lineToX(Auto.LongBlueEndPosit);

            waitForStart();
            
            // Center the turret at the START of the match
            if (Launcher.turret != null) {
                Launcher.turret.setPosition(0.5);
            }

            // Run background loops and the trajectory builder in parallel
            // Target ID 20 with -1.0 degree offset for Blue Corner middle aim
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(), 
                    new Auto.AutoTurretTrack(20, -2.0),
                    step1.build()               
            ));
        }
    }
}
