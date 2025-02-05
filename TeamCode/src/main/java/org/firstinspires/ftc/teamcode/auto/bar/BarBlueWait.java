package org.firstinspires.ftc.teamcode.auto.bar;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Bar Blue Wait", group = "bar")
public class BarBlueWait extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.Wait)
                        .lineToY(55)
                        .turnTo(Math.toRadians(0))
                        .lineToX(33)
                        .turnTo(Math.toRadians(270))
                        .lineToY(10)
                        .turnTo(Math.toRadians(180))
                        .lineToX(25)
                        .build());
        armTarget = 800;
        wrist1.setPosition(0.9);
        wrist2.setPosition(0.1);
        while (opModeIsActive()) {
            arm_loop();
            arm();
        }
    }
}
