package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Launcher.flyWheel;
import static org.firstinspires.ftc.teamcode.modules.Launcher.loader;
import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntake;
import static org.firstinspires.ftc.teamcode.modules.Launcher.turret;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setPosition;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_loop;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.modules.Spindexer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Auto {
    public static final double Wait = 5;
    public static final double None = 0;

    public static void startLauncher(HardwareMap hardwareMap) {
        Launcher.Nancy.init_launcher(hardwareMap);
    }
    public static void startSpindexer(HardwareMap hardwareMap) {
        Spindexer.Nancy.init_spindexer(hardwareMap);
    }

    /* --------NON-BLOCKING SHOOT ACTION---------*/
    public static class Shoot implements Action {
        private ElapsedTime timer = null;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (timer == null) {
                timer = new ElapsedTime();
                loader.setPosition(0.00);
            }


            if (timer.milliseconds() < 250) {
                return true;
            } else if (timer.milliseconds() < 1000) {
                loader.setPosition(0.325);
                setPosition(5);
                return true;
            } else if (timer.milliseconds() < 1750) {
                loader.setPosition(0.00);
                return true;
            } else if (timer.milliseconds() < 2000) {
                loader.setPosition(0.325);
                setPosition(6);
                return true;
            } else if (timer.milliseconds() < 2750) {
                loader.setPosition(0.00);
                return true;
            } else {
                loader.setPosition(0.325);
                return false;
            }

//            if (timer.milliseconds() < 250) {
//                return true; // Still waiting
//            } else {
//                loader.setPosition(0.325);
//                new setSpindexerPos1();
//                return false; // Finished
//            }
        }
    }

    public static class loaderDown implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            loader.setPosition(0.325);
            return false;
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

    public static class setSpindexerPos1 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(1); return false; }
    }
    public static class setSpindexerPos2 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(2); return false; }
    }
    public static class setSpindexerPos3 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(3); return false; }
    }
    public static class setSpindexerPos4 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(4); return false; }
    }
    public static class setSpindexerPos5 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(5); return false; }
    }
    public static class setSpindexerPos6 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) { setPosition(6); return false; }
    }

    /* --------INTAKE AUTO LOGIC---------*/
    public static class StartIntake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ballIntake.setPower(1);
            return false;
        }
    }
    public static class ReverseIntake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ballIntake.setPower(-1);
            return false;
        }
    }
    /*----------- FLYWHEEL AUTO LOGIC ----------*/
    public static class StartFlyWheel implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            flyWheel.setVelocity(-2300);
            turret.setPosition(0.5);
            return false;
        }
    }

    public static class StartFlyWheelLow implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            flyWheel.setVelocity(-1980);
            turret.setPosition(0.5);
            return false;
        }
    }

    public static class SetPose implements Action {
        private final MecanumDrive drive;
        public SetPose(MecanumDrive drive) { this.drive = drive; }
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
