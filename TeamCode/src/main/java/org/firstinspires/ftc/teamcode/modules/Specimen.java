package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.armPosition;
import static org.firstinspires.ftc.teamcode.modules.Arm.armTarget;
import static org.firstinspires.ftc.teamcode.modules.Arm.armTargetPosition;
import static org.firstinspires.ftc.teamcode.modules.Arm.armTolerance;
import static org.firstinspires.ftc.teamcode.modules.Arm.intake;
import static org.firstinspires.ftc.teamcode.modules.Arm.intakeOther;
import static org.firstinspires.ftc.teamcode.modules.Arm.slideBack;
import static org.firstinspires.ftc.teamcode.modules.Arm.slideLimit;
import static org.firstinspires.ftc.teamcode.modules.Arm.slidePosition;
import static org.firstinspires.ftc.teamcode.modules.Arm.slideTarget;
import static org.firstinspires.ftc.teamcode.modules.Arm.slideTargetPosition;
import static org.firstinspires.ftc.teamcode.modules.Arm.slideTolerance;
import static org.firstinspires.ftc.teamcode.modules.Arm.wrist1;
import static org.firstinspires.ftc.teamcode.modules.Arm.wrist2;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Specimen {
    private static boolean hasSample = true;
    private static boolean inPosition = false;
    private static boolean armDone = false;

    public static void resetSpecimen() {
        hasSample = true;
        inPosition = false;
        armDone = false;
        armTargetPosition = 0;
        armTarget = 0;
        slideTargetPosition = 0;
        slideTarget = 0;
    }

    public enum ArmState {
        MOVE_UP, EXTEND, DISPENSE, RETRACT, MOVE_DOWN, DONE
    }

    public static class ArmSpecimen implements Action {
        private ArmState state = ArmState.MOVE_UP;
        private long dispenseStartTime = 0;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (hasSample) {
                switch (state) {
                    case MOVE_UP:
                        armTargetPosition = 900;
                        if (armPosition >= 800) {
                            wrist1.setPosition(0.5);
                            wrist2.setPosition(0.5);
                            state = ArmState.EXTEND;
                        }
                        break;

                    case EXTEND:
                        slideTargetPosition = slideLimit;
                        if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
                            wrist1.setPosition(0.6);
                            wrist2.setPosition(0.4);
                            state = ArmState.DISPENSE;
                        }
                        break;

                    case DISPENSE:
                        if (dispenseStartTime == 0) {
                            intake.setPower(1);
                            intakeOther.setPower(-1);
                            dispenseStartTime = System.currentTimeMillis();
                        }
                        if (System.currentTimeMillis() - dispenseStartTime >= 600) {
                            intake.setPower(0);
                            intakeOther.setPower(0);
                            wrist1.setPosition(0.15);
                            wrist2.setPosition(0.85);
                            inPosition = false;
                            state = ArmState.RETRACT;
                        }
                        break;

                    case RETRACT:
                        slideTargetPosition = 0;
                        if (slideBack.isPressed()) {
                            state = ArmState.MOVE_DOWN;
                        }
                        break;

                    case MOVE_DOWN:
                        armTargetPosition = 0;
                        if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                            state = ArmState.DONE;
                        }
                        break;

                    case DONE:
                        armDone = true;
                        hasSample = false;
                        return false;
                }
            }
            return true;
        }
    }

    public static Action armSpecimen() {
        return new ArmSpecimen();
    }

    public static class SetInPosition implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            inPosition = true;
            return false;
        }
    }

    public static Action setInPosition() {
        return new SetInPosition();
    }

    public static class BucketDone implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            return inPosition;
        }
    }

    public static Action bucketDone() {
        return new BucketDone();
    }
}