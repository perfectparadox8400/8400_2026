package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

/** @noinspection unused*/
public class Arm {
    public static class MoveArm implements Action {
        private final int targetPosition;

        public MoveArm(int targetPosition) {
            this.targetPosition = targetPosition;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            armTargetPosition = targetPosition;
            return false;
        }
    }

    public static Action moveArm(int targetPosition) {
        return new MoveArm(targetPosition);
    }

    public static class MoveSlide implements Action {
        private final int targetPosition;

        public MoveSlide(int targetPosition) {
            this.targetPosition = targetPosition;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            slideTargetPosition = targetPosition;
            return false;
        }
    }

    public static Action moveSlide(int targetPosition) {
        return new MoveSlide(targetPosition);
    }

    public static class IntakeIn implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(-1);
            intakeOther.setPower(1);
            return false;
        }
    }

    public static Action intakeIn() {
        return new IntakeIn();
    }

    public static class IntakeOut implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(1);
            intakeOther.setPower(-1);
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
}
