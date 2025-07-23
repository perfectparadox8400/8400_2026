package org.firstinspires.ftc.teamcode.modules;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * @noinspection ALL
 */
public class Arm {
    public static CRServo intake = null;
    public static CRServo intakeOther = null;
    public static Servo wrist1 = null;
    public static Servo wrist2 = null;
    public static Servo hang1 = null;
    public static Servo hang2 = null;

    private static DcMotor mainBoom = null;
    private static DcMotor slide = null;

    // Switchs
    public static TouchSensor armDown = null;
    public static TouchSensor slideBack = null;

    //PID For Arm
    private static PIDController armController;
    private static PIDController slideController;
    private static double armP = 0.0, armI = 0.0, armD = 0.0;
    private static double slideP = 0.005, slideI = 0.0, slideD = 0.0;

    //Arm
    private static double armTicks = 0; //ticks per revolution
    private static double armPower = 0;
    private static double holdingTorque = 0;
    private static double armTheta = 0;
    public static int armLimit = 0;
    public static int armDownLimit = 0;
    public static int armTarget = 0;
    public static int armPosition = 0;
    public static int armTargetPosition = 0;
    public static double armTolerance = 50;

    //Slide
    private static final double slideTicks = 1425.1; //ticks per revolution
    private static final double slideMaxLimit = 5600; //limit 5650
    private static final double slideShortLimit = 3900;
    private static double slideError = 0;
    private static double slidePower = 0;
    public static double slideLimit = slideMaxLimit;
    public static double slideTarget = 0;
    public static double slideTargetPosition = 0;
    public static double slidePosition = 0;
    public static double slideTolerance = 30;
    public static boolean previousSlideBack = false;
    public static boolean hang = false;

    public static class Nancy {
        private static boolean previousArmDown = false;
        public static double hookOpen1 = 1;
        public static double hookOpen2 = 0;
        public static double hookClosed1 = 0.35;
        public static double hookClosed2 = 0.65;

        public static void init_arm(HardwareMap hardwareMap) {
            armP = 0.005; armI = 0.0001; armD = 0.00;
            armController = new PIDController(armP, armI, armD);
            slideController = new PIDController(slideP, slideI, slideD);

            mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
            slide = hardwareMap.get(DcMotor.class, "slide");
            wrist1 = hardwareMap.get(Servo.class, "wrist1");
            wrist2 = hardwareMap.get(Servo.class, "wrist2");
            hang1 = hardwareMap.get(Servo.class, "hang1");
            hang2 = hardwareMap.get(Servo.class, "hang2");
            intake = hardwareMap.get(CRServo.class, "intake");
            intakeOther = hardwareMap.get(CRServo.class, "intake2");
            armDown = hardwareMap.get(TouchSensor.class, "bottom_limit");
            slideBack = hardwareMap.get(TouchSensor.class, "slide_limit");

            mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            intake.setPower(0);
            intakeOther.setPower(0);

            armTicks = 28 * 125;
            armTolerance = 50;
            armTarget = 0;
            armTargetPosition = 0;
            slideTarget = 0;
            slideTargetPosition = 0;
            armLimit = 1150;
            armDownLimit = -450;
        }

        public static void arm() {
            if (armTarget < armTargetPosition) {
                if ((armTargetPosition - armTarget) > 15) {
                    armTarget += 15;
                } else {
                    armTarget = armTargetPosition;
                }
            } else if (armTarget > armTargetPosition) {
                if ((armTarget - armTargetPosition) > 13) {
                    armTarget -= 13;
                } else {
                    armTarget = armTargetPosition;
                }
            }
            if (armTarget > armLimit) {
                armTarget = armLimit;
                armTargetPosition = armTarget;
            }
            if (armDown.isPressed() && armTargetPosition < 0) {
                armTargetPosition = 0;
            }
            if (armTarget < armDownLimit) {
                armTarget = armDownLimit;
                armTargetPosition = armTarget;
            }
            if (armDown.isPressed() && !previousArmDown) {
                armTarget = 0;
                mainBoom.setPower(0);
                previousArmDown = true;
            } else if (!armDown.isPressed()) {
                previousArmDown = false;
            }

            armController.setPID(armP, armI, armD);
            double pid = armController.calculate(armPosition, armTarget);
            double ticksPerDeg = armTicks / 360.0;
            armTheta = armPosition / ticksPerDeg - 28;
            holdingTorque = 0.000062 * (slidePosition + 4520.0) * Math.cos(Math.toRadians(armTheta)); //4250 is the extended arm length in ticks.
            armPower = pid + holdingTorque;
            mainBoom.setPower(armPower);
        }

        public static void arm_reset(boolean limit) {
            if (limit) {
                armTarget = armTarget - 5;
            } else {
                mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                mainBoom.setPower(0);
            }
        }

        public static void arm_loop() {
            slidePosition = slide.getCurrentPosition() * -1;
            armPosition = mainBoom.getCurrentPosition();
        }

        public static void hang_loop(Gamepad gamepad) {
            if (gamepad.dpad_down && (hang1.getPosition() <= hookOpen1 && hang2.getPosition() >= hookOpen2)) {
                hang1.setPosition(hang1.getPosition() + .013);
                hang2.setPosition(hang2.getPosition() - .013);
            } else if (gamepad.dpad_up && (hang1.getPosition() >= hookClosed1 && hang2.getPosition() <= hookClosed2)) {
                hang1.setPosition(hang1.getPosition() - .013);
                hang2.setPosition(hang2.getPosition() + .013);
            } else if (gamepad.dpad_left) {
                hang1.setPosition(hookOpen1);
                hang2.setPosition(hookOpen2);
            } else if (gamepad.dpad_right) {
                hang1.setPosition(hookClosed1);
                hang2.setPosition(hookClosed2);
            }
        }

        public static void arm_telemetry(Telemetry telemetry) {
            //Arm Telemetry
            telemetry.addData("Arm Position", armPosition);
            telemetry.addData("Arm Target", armTarget);
            telemetry.addData("Arm Target Position", armTargetPosition);
            telemetry.addData("Arm Ticks", armTicks);
            telemetry.addData("Arm Power", armPower);
            telemetry.addData("Holding Torque", holdingTorque);
            telemetry.addData("Theta", armTheta);

            //Slide Telemetry
            telemetry.addData("Slide Position", slidePosition);
            telemetry.addData("Slide Target", slideTarget);
            telemetry.addData("Slide Target Position", slideTargetPosition);
            telemetry.addData("Slide Ticks", slideTicks);
            telemetry.addData("Slide Power", slidePower);

            //Intake Telemetry
            telemetry.addData("Wrist 1", wrist1.getPosition());
            telemetry.addData("Wrist 2", wrist2.getPosition());
            telemetry.addData("Intake 1", intake.getPower());
            telemetry.addData("Intake 2", intakeOther.getPower());

            //Hang Telemetry
            telemetry.addData("Hang 1", hang1.getPosition());
            telemetry.addData("Hang 2", hang2.getPosition());

            //Switch's
            telemetry.addData("Arm Down", armDown.isPressed());
            telemetry.addData("Slide Back", slideBack.isPressed());
        }
    }

    public static class MentorBot {
        public static void init_arm(HardwareMap hardwareMap) {
            armP = 0.005; armI = 0.0001; armD = 0.00;
            armController = new PIDController(armP, armI, armD);
            slideController = new PIDController(slideP, slideI, slideD);

            mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
            slide = hardwareMap.get(DcMotor.class, "slide");
            wrist1 = hardwareMap.get(Servo.class, "wrist1");
            wrist2 = hardwareMap.get(Servo.class, "wrist2");
            intake = hardwareMap.get(CRServo.class, "intake");
            intakeOther = hardwareMap.get(CRServo.class, "intake2");
            slideBack = hardwareMap.get(TouchSensor.class, "slide_limit");

            mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slide.setDirection(DcMotor.Direction.REVERSE);

            intake.setPower(0);
            intakeOther.setPower(0);

            armTicks = 28 * 125;
            armTolerance = 20;
            armTarget = 0;
            armTargetPosition = 0;
            slideTarget = 0;
            slideTargetPosition = 0;
            armLimit = 900;
            armDownLimit = 0;
        }

        public static void arm() {
            if (armTarget < armTargetPosition) {
                if ((armTargetPosition - armTarget) > 15) {
                    armTarget += 15;
                } else {
                    armTarget = armTargetPosition;
                }
            } else if (armTarget > armTargetPosition) {
                if ((armTarget - armTargetPosition) > 13) {
                    armTarget -= 13;
                } else {
                    armTarget = armTargetPosition;
                }
            }
            if (armTarget > armLimit) {
                armTarget = armLimit;
                armTargetPosition = armTarget;
            }
            if (armTarget < armDownLimit) {
                armTarget = armDownLimit;
                armTargetPosition = armTarget;
            }

            if (Math.abs(armPosition - armTarget) > armTolerance) {
                armController.setPID(armP, armI, armD);
                armPower = armController.calculate(armPosition, armTarget);
                mainBoom.setPower(armPower);
            } else {
                mainBoom.setPower(0);
            }
        }

        public static void arm_loop() {
            slidePosition = slide.getCurrentPosition();
            armPosition = mainBoom.getCurrentPosition();
        }

        public static void arm_reset() {
            mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mainBoom.setPower(0);
        }

        public static void arm_telemetry(Telemetry telemetry) {
            //Arm Telemetry
            telemetry.addData("Arm Position", armPosition);
            telemetry.addData("Arm Target", armTarget);
            telemetry.addData("Arm Target Position", armTargetPosition);
            telemetry.addData("Arm Ticks", armTicks);
            telemetry.addData("Arm Power", armPower);

            //Slide Telemetry
            telemetry.addData("Slide Position", slidePosition);
            telemetry.addData("Slide Target", slideTarget);
            telemetry.addData("Slide Target Position", slideTargetPosition);
            telemetry.addData("Slide Ticks", slideTicks);
            telemetry.addData("Slide Power", slidePower);

            //Intake Telemetry
            telemetry.addData("Wrist 1", wrist1.getPosition());
            telemetry.addData("Wrist 2", wrist2.getPosition());
            telemetry.addData("Intake 1", intake.getPower());
            telemetry.addData("Intake 2", intakeOther.getPower());
        }
    }

    public static void slide_reset(boolean limit) {
        if (limit) {
            slide.setPower(-0.3);
        } else {
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(0);
        }
    }

    public static void slide() {
        if (slideTarget < slideTargetPosition) {
            if ((slideTargetPosition - slideTarget) > 100) {
                slideTarget += 100;
            } else {
                slideTarget = slideTargetPosition;
            }
        } else if (slideTarget > slideTargetPosition) {
            if ((slideTarget - slideTargetPosition) > 100) {
                slideTarget -= 100;
            } else {
                slideTarget = slideTargetPosition;
            }
        }
        if (slideTarget > slideLimit) {
            slideTarget = slideLimit;
            slideTargetPosition = slideTarget;
        }
        if (slideTarget < 0) {
            slideTarget = 0;
            slideTargetPosition = slideTarget;
        }
        if (slideBack.isPressed() && !previousSlideBack) {
            slideTarget = slidePosition;
            slide.setPower(0);
            previousSlideBack = true;
        } else if (!slideBack.isPressed()) {
            previousSlideBack = false;
        }

        slideError = slideTarget - slidePosition;

        if ((armPosition < 700) && !hang) {
            slideLimit = slideShortLimit;
        } else {
            slideLimit = slideMaxLimit;
        }

        if (Math.abs(slidePosition - slideTarget) > slideTolerance) {
            slideController.setPID(slideP, slideI, slideD);
            slidePower = slideController.calculate(slidePosition, slideTarget);
            slide.setPower(slidePower);
        } else {
            slide.setPower(0);
        }
    }

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
