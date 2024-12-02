package org.firstinspires.ftc.teamcode.auto.inspect;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name="Go Forward", group = "inspect")
public class GoForward extends OpMode {
    private MecanumDrive drive;
    private Pose2d start;

    @Override
    public void init() {
        start = new Pose2d(0, 0, 0);
        drive = new MecanumDrive(hardwareMap, start);
    }

    @Override
    public void loop() {
        Actions.runBlocking(
                drive.actionBuilder(start)
                        .lineToX(48)
                        .build());
    }
}
