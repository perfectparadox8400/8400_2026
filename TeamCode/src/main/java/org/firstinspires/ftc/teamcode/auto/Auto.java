package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Auto {
    public static final double Wait = 5;
    public static final double None = 0;

    public static void startWrist() {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
    }

    public static class ArmStart implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            startWrist();
            if (slideBack.isPressed()){
                slide_reset(false);
                if(armDown.isPressed()){
                    arm_reset(false);
                    packet.put("Resetting", "Done");
                    return false;
                } else {
                    packet.put("Resetting", "Arm");
                    arm_reset(true);
                    arm();
                }
            } else {
                packet.put("Resetting", "Slide");
                slide_reset(true);
            }
            return true;
        }
    }

    public static Action armStart() {
        return new ArmStart();
    }

    public static class ArmLoop implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            packet.put("Arm Position", armPosition);
            packet.put("Arm Target", armTargetPosition);
            packet.put("Slide Position", slidePosition);
            packet.put("Slide Target", slideTargetPosition);
            arm_loop();
            arm();
            slide();
            return true;
        }
    }

    public static Action armLoop() {
        return new ArmLoop();
    }

    public static class SetPose implements Action {
        private final Pose2d robotPose;

        public SetPose(MecanumDrive drive) { this.robotPose = drive.localizer.getPose();}

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            PoseStorage.currentPose = robotPose;
            return true;
        }
    }


    public static Action setPose(MecanumDrive drive) {
        return new SetPose(drive);
    }
}
