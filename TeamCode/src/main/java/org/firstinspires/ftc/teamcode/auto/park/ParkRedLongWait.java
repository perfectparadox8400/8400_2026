package org.firstinspires.ftc.teamcode.auto.park;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Park Red Long Wait", group = "park")
public class ParkRedLongWait extends OpMode {
    private MecanumDrive drive;
    private Pose2d start;

    @Override
    public void init() {
        start = new Pose2d(-16, -63, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, start);
    }

    @Override
    public void loop() {
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .waitSeconds(Wait.Long)
                        .lineToY(-55)
                        .turnTo(Math.toRadians(0))
                        .strafeTo(new Vector2d(48, -63))
                        .build());
    }
}
