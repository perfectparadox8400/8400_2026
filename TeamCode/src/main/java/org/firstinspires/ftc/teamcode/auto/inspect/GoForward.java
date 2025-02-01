package org.firstinspires.ftc.teamcode.auto.inspect;

import static org.firstinspires.ftc.teamcode.modules.Arm.wrist1;
import static org.firstinspires.ftc.teamcode.modules.Arm.wrist2;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Go Forward", group = "z_inspect")
public class GoForward extends LinearOpMode {
    @Override
    public void runOpMode()  {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        Pose2d start = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .lineToX(48)
                        .build());
    }
}
