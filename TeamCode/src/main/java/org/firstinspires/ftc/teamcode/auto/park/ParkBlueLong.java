package org.firstinspires.ftc.teamcode.auto.park;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Park Blue Long", group = "park")
public class ParkBlueLong extends OpMode {
    private MecanumDrive drive;
    private Pose2d start;

    @Override
    public void init() {
        start = new Pose2d(31, 63, Math.toRadians(270));
        drive = new MecanumDrive(hardwareMap, start);
    }

    @Override
    public void loop() {
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.None)
                        .lineToY(55)
                        .turnTo(Math.toRadians(180))
                        .strafeTo(new Vector2d(-48, 63))
                        .build());
    }
}
