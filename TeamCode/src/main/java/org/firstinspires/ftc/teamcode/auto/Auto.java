package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class Auto {
    public static final double Wait = 5;
    public static final double None = 0;
    public static class Intake {
        public static class In {
            public static int intake1 = 0;
            public static int intake2 = 0;
        }
        public static class Out {
            public static int intake1 = 0;
            public static int intake2 = 0;
        }
    }
    public static class Wrist {
        public static class Home {
            public static double wrist1 = 0;
            public static double wrist2 = 0;
        }
        public static class Pickup {
            public static double wrist1 = 0;
            public static double wrist2 = 0;
        }
        public static class Grab {
            public static double wrist1 = 0;
            public static double wrist2 = 0;
        }
        public static class Drop {
            public static double wrist1 = 0;
            public static double wrist2 = 0;
        }
    }

    public static void startWrist() {
        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
    }

    public static void startArm(HardwareMap hardwareMap) {
        if (Robot.robotName().equals("mentorbot")) {
            MentorBot.init_arm(hardwareMap);
            Intake.In.intake1 = MentorBot.Intake.In.intake1;
            Intake.In.intake2 = MentorBot.Intake.In.intake2;
            Intake.Out.intake1 = MentorBot.Intake.Out.intake1;
            Intake.Out.intake2 = MentorBot.Intake.Out.intake2;
        } else if (Robot.robotName().equals("nancy")) {
            Nancy.init_arm(hardwareMap);
            Intake.In.intake1 = Nancy.Intake.In.intake1;
            Intake.In.intake2 = Nancy.Intake.In.intake2;
            Intake.Out.intake1 = Nancy.Intake.Out.intake1;
            Intake.Out.intake2 = Nancy.Intake.Out.intake2;
        }
    }

    public static class ArmStart implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            startWrist();
            if (Robot.robotName().equals("mentorbot")) {
                if (slideBack.isPressed()){
                    slide_reset(false);
                    MentorBot.arm_reset();
                    return false;
                } else {
                    slide_reset(true);
                }
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
                MentorBot.arm_loop();
                MentorBot.arm();
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

    public static class IntakeIn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(Intake.In.intake1);
            intakeOther.setPower(Intake.In.intake2);
            return false;
        }
    }

    public static Action intakeIn() {
        return new IntakeIn();
    }

    public static class IntakeOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(Intake.Out.intake1);
            intakeOther.setPower(Intake.Out.intake1);
            return false;
        }
    }

    public static Action intakeOut() {
        return new IntakeOut();
    }

    public static class IntakeOff implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(0);
            intakeOther.setPower(0);
            return false;
        }
    }

    public static Action intakeOff() {
        return new IntakeOff();
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
