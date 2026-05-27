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
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.modules.Limelight;
import org.firstinspires.ftc.teamcode.modules.Spindexer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import java.util.List;

public class Auto {
    // ----- RED -------
    public static final Pose2d startRedShort = new Pose2d(-40, 56, Math.toRadians(270));
    public static final Vector2d shootRedShort = new Vector2d(-16, 14);
    public static final double shootRedShortAngle = Math.toRadians(135);
    public static final Pose2d startRedLong = new Pose2d(61.5, 14.5, Math.toRadians(180));
    public static final Vector2d shootRedLong = new Vector2d(55, 14.5);
    public static final double shootRedLongAngle = Math.toRadians(162);
    public static final Vector2d RedLine1 = new Vector2d(-12,25);
    public static final Vector2d RedPickup1 = new Vector2d(-12,54);
    public static final Vector2d RedMid1 = new Vector2d(-12, 40);
    public static final Vector2d RedLine2 = new Vector2d(15,25);
    public static final Vector2d RedPickup2 = new Vector2d(15,60);
    public static final Vector2d RedMid2 = new Vector2d(15, 40);
    public static final Vector2d RedLine3 = new Vector2d(36,27);
    public static final Vector2d RedPickup3 = new Vector2d(36,62);
    public static final Vector2d RedMid3 = new Vector2d(36, 42);
    public static final double RedLoadAngle = Math.toRadians(90);
    public static final Vector2d moveToRedGate = new Vector2d(-6,45);
    public static final Vector2d openRedGate = new Vector2d(5,53);

    public static final double openRedGateAngle = Math.toRadians(180);
    public static final Vector2d ShortRedEndPosit = new Vector2d(0, 36);

    public static final Vector2d RedCornerPrep = new Vector2d(58, 40);
    public static final Vector2d RedCornerGoto = new Vector2d(58, 60);
    public static final Vector2d RedCornerSlide = new Vector2d(65, 59.5);
    public static final Vector2d RedCornerPrep2 = new Vector2d(65, 35);
    public static final Vector2d RedCornerCorner = new Vector2d(65, 60);
    public static final Vector2d RedCornerBackup = new Vector2d(65, 58);
    public static final int LongRedEndPosit = 35;
    // ----- BLUE -------
    public static final Pose2d startBlueShort = new Pose2d(-40, -56, Math.toRadians(90));
    public static final Vector2d shootBlueShort = new Vector2d(-16, -14);
    public static final double shootBlueShortAngle = Math.toRadians(225);
    public static final Pose2d startBlueLong = new Pose2d(61.5, -14.5, Math.toRadians(180));
    public static final Vector2d shootBlueLong = new Vector2d(55, -14.5);
    public static final double shootBlueLongAngle = Math.toRadians(198);
    public static final Vector2d BlueLine1 = new Vector2d(-12,-25);
    public static final Vector2d BluePickup1 = new Vector2d(-12,-54);
    public static final Vector2d BlueMid1 = new Vector2d(-12, -40);
    public static final Vector2d BlueLine2 = new Vector2d(15,-25);
    public static final Vector2d BluePickup2 = new Vector2d(15,-60);
    public static final Vector2d BlueMid2 = new Vector2d(15, -40);
    public static final Vector2d BlueLine3 = new Vector2d(36,-27);
    public static final Vector2d BluePickup3 = new Vector2d(36,-62);
    public static final Vector2d BlueMid3 = new Vector2d(36, -42);
    public static final double BlueLoadAngle = Math.toRadians(270);
    public static final Vector2d moveToBlueGate = new Vector2d(6,-45);
    public static final Vector2d openBlueGate = new Vector2d(5,-53);

    public static final double openBlueGateAngle = Math.toRadians(360);
    public static final Vector2d ShortBlueEndPosit = new Vector2d(0, -36);

    public static final Vector2d BlueCornerPrep = new Vector2d(58, -40);
    public static final Vector2d BlueCornerPrep2 = new Vector2d(65, -35);
    public static final Vector2d BlueCornerGoto = new Vector2d(58, -60);
    public static final Vector2d BlueCornerSlide = new Vector2d(65, -59.5);
    public static final Vector2d BlueCornerCorner = new Vector2d (65, -60);
    public static final Vector2d BlueCornerBackup = new Vector2d(65, -58);
    public static final int LongBlueEndPosit = 35;
    public static final double Wait = 5;
    public static final double None = 0;
    
    // Constant for turret range (15 degrees off center is approx 0.083 for 180-deg servo)
    // Adjust this value if you need more or less travel.
    public static final double TURRET_LIMIT = 0.083;

    public static void startLauncher(HardwareMap hardwareMap) {
        Launcher.Nancy.init_launcher(hardwareMap);
        Limelight.init_limelight(hardwareMap);
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
            } else if (timer.milliseconds() < 1850) {
                loader.setPosition(0.325);
                return true;
            } else if (timer.milliseconds() < 2100) {
                setPosition(6);
                return true;
            } else if (timer.milliseconds() < 2750) {
                loader.setPosition(0.00);
                return true;
            } else {
                loader.setPosition(0.325);
                return false;
            }
        }
    }

    /* --------LIMELIGHT AUTO TRACKING ACTION---------*/
    public static class AutoTurretTrack implements Action {
        private double turretPosition = 0.5;
        private final double Kp = 0.00075; 
        private final int targetID;
        private final double offset;
        private double lastTimestamp = -1;

        public AutoTurretTrack(int targetID, double offset) {
            this.targetID = targetID;
            this.offset = offset;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (Limelight.limelight == null) return true;

            LLResult result = Limelight.limelight.getLatestResult();
            
            if (result != null && result.isValid() && result.getTimestamp() != lastTimestamp) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                if (fiducials != null) {
                    for (LLResultTypes.FiducialResult fr : fiducials) {
                        if (fr.getFiducialId() == targetID) {
                            double tx = fr.getTargetXDegrees();
                            double aimedTx = tx + offset;
                            turretPosition -= aimedTx * Kp;
                            lastTimestamp = result.getTimestamp(); 
                            break;
                        }
                    }
                }
            }
            // Limited rotation: Only allow 15 degrees (TURRET_LIMIT) off center (0.5)
            turretPosition = Range.clip(turretPosition, 0.5 - TURRET_LIMIT, 0.5 + TURRET_LIMIT);
            turret.setPosition(turretPosition);
            return true; 
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
        private double lastUpdateTime = 0;
        private ElapsedTime timer = new ElapsedTime();

        @Override
        public boolean run(@NonNull TelemetryPacket packet){
            double currentTime = timer.seconds();
            if (currentTime - lastUpdateTime >= 0.01) {
                spindexer_loop();
                lastUpdateTime = currentTime;
            }
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
            flyWheel.setVelocity(-2250);
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
