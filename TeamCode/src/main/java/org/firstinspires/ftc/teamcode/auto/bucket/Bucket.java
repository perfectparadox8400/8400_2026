package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Bucket {
    private static boolean hasSample = true;
    private static boolean inPosition = false;
    private static boolean armBucketDone = false;

    public static class ArmBucket implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            ArmBucketLoop.start();
            return false;
        }
    }

    public static Action armBucket() {
        return new ArmBucket();
    }

    private static final Runnable armBucketLoop = new Runnable() {
        private boolean up = true;
        private boolean isUp = false;
        private boolean down = false;
        private boolean isDown = true;
        private boolean in = true;
        private boolean out = false;
        private boolean dispense = false;
        private long dispenseStartTime = 0;

        @Override
        public void run() {
            armBucketDone = false;
            while (!armBucketDone) {
                if (hasSample) {
                    if (up && !isUp) {
                        isDown = false;
                        armTargetPosition = armLimit;
                        if (armPosition >= 1000) {
                            isUp = true;
                            wrist1.setPosition(0.5);
                            wrist2.setPosition(0.5);
                        }
                    } else if (up) {
                        if (!out) {
                            in = false;
                            slideTargetPosition = slideLimit;
                            if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
                                out = true;
                            }
                        } else if (!dispense && inPosition) {
                            intake.setPower(1);
                            intakeOther.setPower(-1);
                            if (dispenseStartTime == 0) {
                                dispenseStartTime = System.currentTimeMillis();
                            }
                            if (System.currentTimeMillis() - dispenseStartTime >= 1000) {
                                intake.setPower(0);
                                intakeOther.setPower(0);
                                dispense = true;
                                hasSample = false;
                            }
                        } else if (inPosition) {
                            up = false;
                            down = true;
                            wrist1.setPosition(0.15);
                            wrist2.setPosition(0.85);
                            inPosition = false;
                        }
                    } else if (down && !isDown) {
                        isUp = false;
                        dispense = false;
                        dispenseStartTime = 0;
                        if (out) {
                            slideTargetPosition = 0;
                            if (slideBack.isPressed()) {
                                out = false;
                                in = true;
                            }
                        } else if (in) {
                            armTargetPosition = 0;
                            if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                                down = false;
                                isDown = true;
                            }
                        }
                    } else if (isDown) {
                        up = true;
                        armBucketDone = true;
                    }
                }
            }
        }
    };

    private static final Thread ArmBucketLoop = new Thread(armBucketLoop);

    public static Action getSample() {
        return new GetSample();
    }

    public static class GetSample implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (armBucketDone) {
                GetSampleLoop.start();
                return false;
            } else {
                return true;
            }
        }
    }

    private static final Runnable getSampleLoop = () -> {
        while (!hasSample) {
            armTargetPosition = 5;
            wrist1.setPosition(0.85);
            wrist2.setPosition(0.15);
            if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                slideTargetPosition = 100;
                intake.setPower(-1);
                intakeOther.setPower(1);
                hasSample = true;
            }
        }
    };

    private static final Thread GetSampleLoop = new Thread(getSampleLoop);

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
}
