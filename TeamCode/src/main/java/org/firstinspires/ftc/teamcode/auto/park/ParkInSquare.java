package org.firstinspires.ftc.teamcode.auto.park;



import static org.firstinspires.ftc.teamcode.auto.Auto.None;
import static org.firstinspires.ftc.teamcode.auto.Auto.startArm;
import static org.firstinspires.ftc.teamcode.auto.Auto.startWrist;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auto.Auto;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import com.acmerobotics.roadrunner.ActionBuilder; // ✅ Make sure this import exists

@Autonomous(name="Park", group = "park")
public class ParkInSquare extends LinearOpMode {

    @Override
    public void runOpMode() {
        Pose2d start = new Pose2d(-16, 63, Math.toRadians(270));
        MecanumDrive drive = new MecanumDrive(hardwareMap, start);
        startArm(hardwareMap);

        // ✅ Use ActionBuilder instead of TrajectoryActionBuilder
        ActionBuilder step1 = drive.actionBuilder(start)
                .waitSeconds(None)
                .lineToY(55)
                .forward(50); // ✅ This now works!

        waitForStart();
        startWrist();

        Actions.runBlocking(new ParallelAction(
                Auto.setPose(drive),
                step1.build() // ✅ No need for SequentialAction wrapper
        ));
    }
}