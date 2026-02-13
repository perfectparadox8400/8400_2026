package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntakeRear;
import static org.firstinspires.ftc.teamcode.modules.Launcher.flyWheel;
import static org.firstinspires.ftc.teamcode.modules.Launcher.loader;
import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntake;
import static org.firstinspires.ftc.teamcode.modules.Launcher.turret;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setPosition;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_loop;
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

import java.time.temporal.Temporal;

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
            autoRunTime.reset();
            loader.setPosition(0.05);
//            turret.setPosition(0.5);
// :)

            while (autoRunTime.milliseconds() <= 250) {/*waiting for 250 ms before lowering loader*/}
            loader.setPosition(0.325);
        }

    }

    public static class loaderDown implements InstantFunction {
        @Override
        public void run() {
            loader.setPosition(0.325);
    }
    }

    /*---------Spindexer Logic---------*/

    public static class UpdateSpindexer implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            spindexer_loop();
            return true;
        }
    }

    public static class setSpindexerPos1 implements InstantFunction {
        @Override
        public void run() {
            setPosition(1);
        }
    }
    public static class setSpindexerPos2 implements InstantFunction {
        @Override
        public void run() {
            setPosition(2);
        }
    }
    public static class setSpindexerPos3 implements InstantFunction {
        @Override
        public void run() {
            setPosition(3);
        }
    }
    public static class setSpindexerPos4 implements InstantFunction {
        @Override
        public void run() {
            setPosition(4);
        }
    }
    public static class setSpindexerPos5 implements InstantFunction {
        @Override
        public void run() {
            setPosition(5);
        }
    }
    public static class setSpindexerPos6 implements InstantFunction {
        @Override
        public void run() {
            setPosition(6);
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
            flyWheel.setVelocity(-2350);
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
