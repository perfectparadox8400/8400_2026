package org.firstinspires.ftc.teamcode.auto.park;

import static org.firstinspires.ftc.teamcode.modules.Arm.init_arm;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Park Blue Wait", group = "park")
public class ParkBlueWait extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-16, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.Wait)
                        .lineToY(55)
                        .turnTo(Math.toRadians(180))
                        .lineToX(-48)
                        .turnTo(Math.toRadians(270))
                        .lineToY(63)
                        .build());
    }
}
