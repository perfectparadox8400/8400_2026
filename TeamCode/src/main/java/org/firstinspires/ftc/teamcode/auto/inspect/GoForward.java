package org.firstinspires.ftc.teamcode.auto.inspect;

import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;
import static org.firstinspires.ftc.teamcode.modules.Arm.init_arm;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Go Forward", group = "z_inspect")
public class GoForward extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d start = new Pose2d(0, 0, 0);
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        init_arm(hardwareMap);

        TrajectoryActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .lineToX(48);

        waitForStart();
        startWrist();
        Actions.runBlocking(new ParallelAction(
                Auto.setPose(drive),
                new SequentialAction(
                        step1.build()
                )
        ));
    }
}
