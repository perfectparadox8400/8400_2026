package org.firstinspires.ftc.teamcode.auto.launch.red;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startLauncher;
import static org.firstinspires.ftc.teamcode.auto.Auto.startSpindexer;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.init_spindexer;
//import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.modules.Spindexer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="rLongShot", group = "auto")
public class rLongShot extends LinearOpMode {


    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(61.5,14.5, 3.142);
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
                    .lineToX(55)
                    .turnTo(Math.toRadians(159))
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
                    .lineToX(35)
                    .turnTo(Math.toRadians(90))
                    .stopAndAdd(new Auto.setSpindexerPos1())
                    .lineToY(36)
                    .stopAndAdd(new Auto.setSpindexerPos2())
                    .lineToY(42)
                    .stopAndAdd(new Auto.setSpindexerPos3())
                    .lineToY(62.5)
                    .lineToY(14.5)
                    .turnTo(Math.toRadians(180))
                    .lineToX(55)
                    .turnTo(Math.toRadians(159))
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
                    .lineToX(35);
//                    .lineToX(12)
//                    .turnTo(Math.toRadians(90))
//                    .lineToY(62.5)
//                    .lineToY(14.5)
//                    .turnTo(Math.toRadians(180))
//                    .lineToX(40);


            waitForStart();
            Actions.runBlocking(new ParallelAction(
                    new Auto.UpdateSpindexer(),
                    step1.build()
            ));
        }
    }

}
