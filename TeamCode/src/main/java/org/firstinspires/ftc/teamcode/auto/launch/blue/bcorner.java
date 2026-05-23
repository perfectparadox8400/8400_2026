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

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="bcorner", group = "auto")
public class bcorner extends LinearOpMode {


    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(61.5,-14.5, 3.142);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startLauncher(hardwareMap);
        startSpindexer(hardwareMap);

        if (Robot.robotName().equals("nancy")) {
            TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                    .waitSeconds(None)
                    .stopAndAdd(new Auto.StartIntake())
                    .stopAndAdd(new Auto.StartFlyWheel())
                    .stopAndAdd(new Auto.setSpindexerPos4())
                    .stopAndAdd(new Auto.loaderDown())

                    .strafeToLinearHeading(new Vector2d(55, -14.5), Math.toRadians(201))
                    .waitSeconds(0.25)
                    // shoot
//                    .stopAndAdd(new Auto.setSpindexerPos4())
//                    .waitSeconds(1)
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

                    .strafeToLinearHeading(new Vector2d(58, -60.5), Math.toRadians(270))

                    // pickup
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .waitSeconds(0.5)
                    .strafeToLinearHeading(new Vector2d(61.5, -60.5), Math.toRadians(270))
                    .waitSeconds(0.25)
                    .stopAndAdd(new Auto.setSpindexerPos3())

                    .strafeToLinearHeading(new Vector2d(55, -14.5), Math.toRadians(205))

                    //shoot
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

                    .strafeToLinearHeading(new Vector2d(35, -20.5), Math.toRadians(270))

                    .strafeToLinearHeading(new Vector2d(35, -62.5), Math.toRadians(270))

                    // pickup
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .waitSeconds(0.75)
                    .stopAndAdd(new Auto.setSpindexerPos3())

                    .strafeToLinearHeading(new Vector2d(55, -14.5), Math.toRadians(205))

                    //shoot
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

                    .lineToX(35);


            waitForStart();
            Actions.runBlocking(new ParallelAction(
                    new Auto.UpdateSpindexer(),
                    step1.build()
            ));
        }
    }

}
