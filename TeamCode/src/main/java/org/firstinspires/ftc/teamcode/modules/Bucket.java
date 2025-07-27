package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.auto.Auto.*;

public class Bucket {
    private static boolean hasSample = true;
    public static boolean inPosition = false;
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
            int upTarget = 0;
            int upGo = 0;
            int back = 0;
            int pickup = 0;
            if (Robot.robotName().equals("mentorbot")) {
                upTarget = 1100;
                upGo = 900;
                pickup = 100;
            } else if (Robot.robotName().equals("nancy")) {
                upTarget = 1105;
                upGo = 1000;
                pickup = 250;
            }
            if (hasSample) {
                switch (state) {
                    case MOVE_UP:
                        armTargetPosition = upTarget;
                        if (armPosition >= upGo) {
                            wrist1.setPosition(0.5);
                            wrist2.setPosition(0.5);
                            state = ArmState.EXTEND;
                        }
                        break;

                    case EXTEND:
                        slideTargetPosition = slideLimit;
                        if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
                            wrist1.setPosition(Wrist.Drop.wrist1);
                            wrist2.setPosition(Wrist.Drop.wrist2);
                            state = ArmState.DISPENSE;
                        }
                        break;

                    case DISPENSE:
                        if (inPosition) {
                            if (dispenseStartTime == 0) {
                                intake.setPower(Intake.Out.intake1);
                                intakeOther.setPower(Intake.Out.intake2);
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
                        }
                        break;

                    case RETRACT:
                        slideTargetPosition = back;
                        if (slidePosition < 800) {
                            state = ArmState.MOVE_DOWN;
                        }
                        break;

                    case MOVE_DOWN:
                        armTargetPosition = pickup;
                        state = ArmState.DONE;
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
            int slideOut = 0;
            if (Robot.robotName().equals("mentorbot")) {
                slideOut = 600;
            } else if (Robot.robotName().equals("nancy")) {
                slideOut = 250;
            }
            if (armBucketDone && !hasSample) {
                slideTargetPosition = slideOut;
                wrist1.setPosition(Wrist.Grab.wrist1);
                wrist2.setPosition(Wrist.Grab.wrist2);
                if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                    intake.setPower(Intake.In.intake1);
                    intakeOther.setPower(Intake.In.intake2);
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
            if (System.currentTimeMillis() - grabStartTime >= 100) {
                if (System.currentTimeMillis() - grabStartTime >= 700) {
                    intake.setPower(0);
                    intakeOther.setPower(0);
                    wrist1.setPosition(Wrist.Home.wrist1);
                    wrist2.setPosition(Wrist.Home.wrist2);
                    return false;
                }
                armTargetPosition = 0;
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