package org.firstinspires.ftc.teamcode.auto.sample;

import static org.firstinspires.ftc.teamcode.modules.Arm.init_arm;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Wait;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Sample Blue", group = "park")
public class SampleBlue extends LinearOpMode {
    @Override
    public void runOpMode()  {
        Pose2d start = new Pose2d(-16, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);
        waitForStart();
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(180))
                        .lineToX(-36)
                        .turnTo(Math.toRadians(270))
                        .lineToY(10)
                        .turnTo(Math.toRadians(180))
                        .lineToX(-42)
                        .turnTo(Math.toRadians(90))
                        .lineToY(55)
                        .lineToY(10)
                        .strafeToConstantHeading(new Vector2d(-58,10))
                        .turnTo(Math.toRadians(90))
                        .lineToY(55)
                        .lineToY(10)
                        .strafeToConstantHeading(new Vector2d(-63,10))
                        .turnTo(Math.toRadians(90))
                        .lineToY(55)
                        .lineToY(48)
                        .strafeToConstantHeading(new Vector2d(-48,48))
                        .turnTo(Math.toRadians(270))
                        .lineToY(60)
                        .build());
    }
}
