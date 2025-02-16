package org.firstinspires.ftc.teamcode.auto.bar;

import static org.firstinspires.ftc.teamcode.auto.Auto.Wait;
import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Bar.armBar;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bar Wait", group = "bar")
public class BarWait extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(Wait)
                .lineToY(55)
                .turnTo(Math.toRadians(0))
                .lineToX(33)
                .turnTo(Math.toRadians(270))
                .lineToY(10)
                .turnTo(Math.toRadians(180))
                .lineToX(28)
                .stopAndAdd(armBar());

        waitForStart();
        Actions.runBlocking(new SequentialAction(
                Auto.armStart(),
                new ParallelAction(
                        Auto.armLoop(),
                        Auto.setPose(drive),
                        new SequentialAction(
                                step1.build()
                        )
                )
        ));
    }
}
