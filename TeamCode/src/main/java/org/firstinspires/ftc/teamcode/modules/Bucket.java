package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Bucket {
    private static boolean hasSample = true;
    private static boolean inPosition = false;
    private static boolean armBucketDone = false;

    public static void resetBucket() {
        hasSample = true;
        inPosition = false;
        armBucketDone = false;
        armTargetPosition = 0;
        armTarget = 0;
        slideTargetPosition = 0;
        slideTarget = 0;
    }

    public enum ArmState {
        MOVE_UP, EXTEND, DISPENSE, RETRACT, MOVE_DOWN, DONE
    }

    public static class ArmBucket implements Action {
        private ArmState state = ArmState.MOVE_UP;
        private long dispenseStartTime = 0;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (hasSample) {
                switch (state) {
                    case MOVE_UP:
                        armTargetPosition = armLimit;
                        if (armPosition >= 1000) {
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
                        if (System.currentTimeMillis() - dispenseStartTime >= 1000) {
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
                        armBucketDone = true;
                        hasSample = false;
                        return false;
                }
            }
            return true;
        }
    }
    
    public static Action armBucket() {
        return new ArmBucket();
    }

    public static class GetSample implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (armBucketDone && !hasSample) {
                armTargetPosition = 0;
                wrist1.setPosition(0.6044);
                wrist2.setPosition(0.3922);
                if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                    slideTargetPosition = 0;
                    intake.setPower(-1);
                    intakeOther.setPower(1);
                    hasSample = true;
                    return false;
                }
            }
            return true;
        }
    }

    public static Action getSample() {
        return new GetSample();
    }

    public static class GrabSample implements Action {
        private long grabStartTime = 0;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (grabStartTime == 0) {
                grabStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - grabStartTime >= 500) {
                intake.setPower(0);
                intakeOther.setPower(0);
                return false;
            }
            return true;
        }
    }

    public static Action grabSample() {
        return new GrabSample();
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