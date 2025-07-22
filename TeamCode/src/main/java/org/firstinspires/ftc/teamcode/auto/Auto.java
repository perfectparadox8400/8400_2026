package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Teleop;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Auto {
    public static final double Wait = 5;
    public static final double None = 0;

    public static void startWrist() {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
    }

    public static void startArm(HardwareMap hardwareMap) {
        if (Robot.robotName().equals("mentorbot")) {
            MentorBot.init_arm(hardwareMap);
        } else if (Robot.robotName().equals("nancy")) {
            Nancy.init_arm(hardwareMap);
        }
    }

    public static class ArmStart implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            startWrist();
            if (Robot.robotName().equals("mentorbot")) {
                return false;
            } else if (Robot.robotName().equals("nancy")) {
                if (slideBack.isPressed()){
                    slide_reset(false);
                    if(armDown.isPressed()){
                        Nancy.arm_reset(false);
                        packet.put("Resetting", "Done");
                        return false;
                    } else {
                        packet.put("Resetting", "Arm");
                        Nancy.arm_reset(true);
                        Nancy.arm();
                    }
                } else {
                    packet.put("Resetting", "Slide");
                    slide_reset(true);
                }
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
            if (Robot.robotName().equals("mentorbot")) {
                return false;
            } else if (Robot.robotName().equals("nancy")) {
                Nancy.arm_loop();
                Nancy.arm();
            }
            slide();
            return true;
        }
    }

    public static Action armLoop() {
        return new ArmLoop();
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
