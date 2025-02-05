package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Bucket {
    public static class ArmBucket implements Action {
        private boolean up = true;
        private boolean isUp = false;
        private boolean down = false;
        private boolean isDown = false;
        private boolean in = true;
        private boolean out = false;
        private boolean dispense = false;
        private long dispenseStartTime = 0;
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            arm_loop();
            if (up && !isUp) {
                armTargetPosition = armLimit;
                if (armPosition >= 1000) {
                    isUp = true;
                    wrist1.setPosition(0.5);
                    wrist2.setPosition(0.5);
                }
            } else if (up) {
                armTargetPosition = armLimit;
                if (!out) {
                    in = false;
                    slideTargetPosition = slideLimit;
                    if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
                        out = true;
                    }
                } else if (!dispense) {
                    intake.setPower(-1);
                    intakeOther.setPower(1);
                    if (dispenseStartTime == 0) {
                        dispenseStartTime = System.currentTimeMillis();
                    }
                    if (System.currentTimeMillis() - dispenseStartTime >= 2000) {
                        intake.setPower(0);
                        intakeOther.setPower(0);
                        dispense = true;
                    }
                } else {
                    up = false;
                    down = true;
                    wrist1.setPosition(0.15);
                    wrist2.setPosition(0.85);
                }
            } else if (down && !isDown) {
                isUp = false;
                dispense = false;
                dispenseStartTime = 0;
                if (out) {
                    slideTargetPosition = 0;
                    if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
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
            } else return !isDown;
            arm();
            slide();
            return true;
        }
    }

    public static Action armBucket() {
        return new ArmBucket();
    }

    public static class IntakeOn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(1);
            intakeOther.setPower(-1);
            return false;
        }
    }

    public static Action intakeOn() {
        return new IntakeOn();
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
}
