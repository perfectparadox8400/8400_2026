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
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="rcornerFast", group = "auto")
public class rcornerFast extends LinearOpMode {

    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(61.5,14.5, 3.142);
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
                    .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(162))
                    .waitSeconds(0.1) 

                    // SHOOT 1 Sequence
                    .stopAndAdd(new Auto.Shoot())
//                    .stopAndAdd(new Auto.setSpindexerPos5()) // Rotate while waiting
//                    .waitSeconds(0.4)
//                    .stopAndAdd(new Auto.Shoot())
//                    .stopAndAdd(new Auto.setSpindexerPos6()) // Rotate while waiting
//                    .waitSeconds(0.4)
//                    .stopAndAdd(new Auto.Shoot())
                    
                    // LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(new Vector2d(58, 61), Math.toRadians(90))

                    //.waitSeconds(0.2)
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .waitSeconds(1)
                    .strafeToLinearHeading(new Vector2d(61.5, 61), Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos3())
                    .waitSeconds(1)
                    //.waitSeconds(0.8) // Wait for rings to load

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(0, new Auto.setSpindexerPos4())
                    .afterTime(.5, new Auto.ReverseIntake())
                    .afterTime(.6, new Auto.StartIntake())
                    .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                    // SHOOT 2 Sequence
                    .stopAndAdd(new Auto.Shoot())


// LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(new Vector2d(58, 61), Math.toRadians(90))

                    //.waitSeconds(0.2)
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .waitSeconds(1)
                    .strafeToLinearHeading(new Vector2d(61.5, 61), Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos3())
                    .waitSeconds(1)
                    //.waitSeconds(0.8) // Wait for rings to load

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(0, new Auto.setSpindexerPos4())
                    .afterTime(.5, new Auto.ReverseIntake())
                    .afterTime(.6, new Auto.StartIntake())
                    .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                    // SHOOT 3 Sequence
                    .stopAndAdd(new Auto.Shoot())


// LOAD 1: Strafe and rotate spindexer to Load Pos 1 at the SAME TIME
                    .afterTime(0, new Auto.setSpindexerPos1())
                    .strafeToLinearHeading(new Vector2d(58, 61), Math.toRadians(90))

                    //.waitSeconds(0.2)
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .waitSeconds(1)
                    .strafeToLinearHeading(new Vector2d(61.5, 61), Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos3())
                    .waitSeconds(1)
                    //.waitSeconds(0.8) // Wait for rings to load

                    // Return: Prep launch pos and clear intake WHILE driving
                    .afterTime(0, new Auto.setSpindexerPos4())
                    .afterTime(.5, new Auto.ReverseIntake())
                    .afterTime(.6, new Auto.StartIntake())
                    .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                    // SHOOT 4 Sequence
                    .stopAndAdd(new Auto.Shoot())


                    .lineToX(35);

            waitForStart();

            // Run the spindexer PID loop and the trajectory builder in parallel
            Actions.runBlocking(new RaceAction(
                    new Auto.UpdateSpindexer(), 
                    step1.build()               
            ));
        }
    }
}
