package org.firstinspires.ftc.teamcode.auto.bucket;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bucket {
    private static boolean hasSample = true;
    private static boolean inPosition = false;
    private static boolean armBucketDone = false;
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // Arm movement states
    private enum ArmState {
        MOVE_UP, EXTEND_SLIDE, DISPENSE, MOVE_DOWN, IDLE
    }

    private static ArmState armState = ArmState.IDLE;

    public static class ArmBucket implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            startArmBucketLoop();
            return false;
        }
    }

    public static Action armBucket() {
        return new ArmBucket();
    }

    private static void startArmBucketLoop() {
        armBucketDone = false;
        armState = ArmState.MOVE_UP;
        executorService.scheduleWithFixedDelay(Bucket::updateArmState, 0, 50, TimeUnit.MILLISECONDS);
    }

    private static void updateArmState() {
        switch (armState) {
            case MOVE_UP:
                armTargetPosition = armLimit;
                if (armPosition >= 1000) {
                    wrist1.setPosition(0.5);
                    wrist2.setPosition(0.5);
                    armState = ArmState.EXTEND_SLIDE;
                }
                break;

            case EXTEND_SLIDE:
                slideTargetPosition = slideLimit;
                if (Math.abs(slidePosition - slideTargetPosition) < slideTolerance) {
                    armState = ArmState.DISPENSE;
                }
                break;

            case DISPENSE:
                if (inPosition) {
                    intake.setPower(1);
                    intakeOther.setPower(-1);
                    executorService.schedule(() -> {
                        intake.setPower(0);
                        intakeOther.setPower(0);
                        wrist1.setPosition(0.15);
                        wrist2.setPosition(0.85);
                        inPosition = false;
                        armState = ArmState.MOVE_DOWN;
                    }, 1, TimeUnit.SECONDS);
                }
                break;

            case MOVE_DOWN:
                slideTargetPosition = 0;
                if (slideBack.isPressed()) {
                    armTargetPosition = 0;
                    if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                        armBucketDone = true;
                        hasSample = false;
                        armState = ArmState.IDLE;
                        executorService.shutdown();
                    }
                }
                break;

            case IDLE:
                break;
        }
    }

    // Sample retrieval states
    private enum SampleState {
        MOVE_ARM, EXTEND_SLIDE, GRAB_SAMPLE, RETRACT, IDLE
    }

    private static SampleState sampleState = SampleState.IDLE;

    public static class GetSample implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (armBucketDone) {
                startSampleLoop();
                return false;
            }
            return true;
        }
    }

    public static Action getSample() {
        return new GetSample();
    }

    private static void startSampleLoop() {
        hasSample = false;
        sampleState = SampleState.MOVE_ARM;
        executorService.scheduleWithFixedDelay(Bucket::updateSampleState, 0, 50, TimeUnit.MILLISECONDS);
    }

    private static void updateSampleState() {
        switch (sampleState) {
            case MOVE_ARM:
                armTargetPosition = 5;
                wrist1.setPosition(0.60);
                wrist2.setPosition(0.40);
                if (Math.abs(armPosition - armTargetPosition) < armTolerance) {
                    sampleState = SampleState.EXTEND_SLIDE;
                }
                break;

            case EXTEND_SLIDE:
                slideTargetPosition = 100;
                intake.setPower(-1);
                intakeOther.setPower(1);
                sampleState = SampleState.GRAB_SAMPLE;
                break;

            case GRAB_SAMPLE:
                hasSample = true;
                sampleState = SampleState.RETRACT;
                break;

            case RETRACT:
                slideTargetPosition = 0;
                intake.setPower(0);
                intakeOther.setPower(0);
                sampleState = SampleState.IDLE;
                executorService.shutdown();
                break;

            case IDLE:
                break;
        }
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
}
