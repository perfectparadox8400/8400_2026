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



@Autonomous(name="rcornerFast", group = "auto")
public class rcornerFast extends LinearOpMode {

    @Override
    public void runOpMode()  {
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

                    // Drive to shooting position
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)
                    .waitSeconds(0.25)

                    // SHOOT 1 Sequence
                    .stopAndAdd(new Auto.Shoot())
                    
                    // LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())

                    // New pickup
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


//                   LOAD 2
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.RedCornerPrep2, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.RedCornerBackup, Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .afterTime(.25, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.RedCornerCorner, Math.toRadians(90))

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)

                    // SHOOT 3 Sequence
                    .stopAndAdd(new Auto.Shoot())


//                  LOAD 3: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(Auto.RedCornerPrep2, Math.toRadians(90))
                    .strafeToLinearHeading(Auto.RedCornerBackup, Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .afterTime(.25, new Auto.setSpindexerPos3())
                    .strafeToLinearHeading(Auto.RedCornerCorner, Math.toRadians(90))

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(.5, new Auto.setSpindexerPos4())
//                    .afterTime(.6, new Auto.ReverseIntake())
//                    .afterTime(.7, new Auto.StartIntake())
                    .strafeToLinearHeading(Auto.shootRedLong, Auto.shootRedLongAngle)

                    // SHOOT 4 Sequence
                    .stopAndAdd(new Auto.Shoot())


                    .lineToX(Auto.LongRedEndPosit);

            waitForStart();
            
            // Center the turret at the START of the match
            if (Launcher.turret != null) {
                Launcher.turret.setPosition(0.5);
            }

            // Run background loops and the trajectory builder in parallel
            // Target ID 24 with 1 degree offset for red corner
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(), 
                    new Auto.AutoTurretTrack(24, 2.0),
                    step1.build()               
            ));
        }
    }
}
