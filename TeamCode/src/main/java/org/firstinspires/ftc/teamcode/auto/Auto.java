package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntakeRear;
import static org.firstinspires.ftc.teamcode.modules.Launcher.flyWheel;
import static org.firstinspires.ftc.teamcode.modules.Launcher.loader;
import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntake;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setPosition;
import static org.firstinspires.ftc.teamcode.modules.Teleop.Nancy.launchPosition;
import static org.firstinspires.ftc.teamcode.modules.Teleop.Nancy.loadPosition;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.modules.Spindexer;
import org.firstinspires.ftc.teamcode.modules.Teleop;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Auto {
    public static final double Wait = 5;
    public static final double None = 0;
    private static ElapsedTime autoRunTime = new ElapsedTime();
    private static ElapsedTime autoRunTimeSpindexer = new ElapsedTime();

    /* -----------LAUNCHER AUTO LOGIC--------------*/
    public static void startLauncher(HardwareMap hardwareMap) {
        Launcher.Nancy.init_launcher(hardwareMap);
//        loader.setPosition(0);
//        flyWheel.setPower(1);
//        ballIntake.setPower(-1);
    }
    public static void startSpindexer(HardwareMap hardwareMap) {
        Spindexer.Nancy.init_spindexer(hardwareMap);
    }

    /* --------SHOOT IN AUTO LOGIC---------*/

    public static class Shoot implements InstantFunction {
        @Override
        public void run() {
            loader.setPosition(0.05);
            autoRunTime.reset();

            while (autoRunTime.milliseconds() <= 250) {/*waiting for 250 ms before lowering loader*/}
            loader.setPosition(0.325);
        }

    }

    /*---------Spindexer Logic---------*/

    public static class ShootSpindexer implements InstantFunction {
        @Override
        public void run() {
            setPosition(6);
            autoRunTimeSpindexer.reset();

            while (autoRunTimeSpindexer.milliseconds() <= 500) {}
        }

    }

    public static class LoadSpindexer implements InstantFunction {
        @Override
        public void run() {
            if (loadPosition > 2)
                loadPosition = 1;
            else loadPosition++;
            setPosition(loadPosition);
        }

    }

    /* --------INTAKE AUTO LOGIC---------*/

    public static class StartIntake implements InstantFunction {
        @Override
        public void run() {
            ballIntake.setPower(-1);
//            ballIntakeRear.setPower(-1);
        }
    }

    /*----------- FLYWHEEL AUTO LOGIC ----------*/
    public static class StartFlyWheel implements InstantFunction {
        @Override
        public void run() {
            flyWheel.setPower(-1);
        }
    }


    public static class SetPose implements Action {
        private final MecanumDrive drive;

        public SetPose(MecanumDrive drive) {
            this.drive = drive;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            PoseStorage.currentPose = drive.localizer.getPose();
            return true;
        }
    }


    public static Action setPose(MecanumDrive drive) {
        return new SetPose(drive);
    }
}
