package org.firstinspires.ftc.teamcode.modules;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm {
    public static CRServo intake = null;
    public static CRServo intakeOther = null;
    public static Servo wrist1 = null;
    public static Servo wrist2 = null;

    public static DcMotor mainBoom = null;
    public static DcMotor slide = null;

    public static TouchSensor armDown = null;
    public static TouchSensor armUp = null;
    public static TouchSensor slideBack = null;

    //PID For Arm
    public static PIDController controller;
    public static final double fStart = 0.15;
    public static final double fEnd = 0.5;
    public static double p = 0.01, i = 0.01, d = 0.0005;
    public static double f = fStart;

    //Arm
    public static final double armTicks = 28 * 125; //ticks per revolution
    public static final int armLimit = 1200;
    public static int armTarget = 0;
    public static double armTargetPosition = 0;
    public static int armPosition;
    public static double armSpeed = 10;
    public static double armTolerance = 50;
    public static boolean armHome = false;
    public static boolean armZeroing = false;

    public static boolean previousArmDown = true;

    //Slide
    public static final double slideTicks = 1425.1; //ticks per revolution
    public static final double slideMaxLimit = 5600; //limit 5650
    public static final double slideShortLimit = 3900;
    public static double slideLimit = slideMaxLimit;
    public static double slideTarget = 0;
    public static double slidePosition;
    public static double slideTolerance = 5;
    public static double slideError = 0;
    public static boolean slideIsBack = false;
    public static boolean slideZeroing = false;
    public static boolean previousSlideBack = true;

    //Home
//    public static boolean isBack;

    public static void init_arm(HardwareMap hardwareMap) {
        controller = new PIDController(p, i, d);

        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        wrist2 = hardwareMap.get(Servo.class, "wrist2");
        intake = hardwareMap.get(CRServo.class, "intake");
        intakeOther = hardwareMap.get(CRServo.class, "intake2");
        armDown = hardwareMap.get(TouchSensor.class, "bottom_limit");
        armUp = hardwareMap.get(TouchSensor.class, "arm_limit");
        slideBack = hardwareMap.get(TouchSensor.class, "slide_limit");

        mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mainBoom.setDirection(DcMotorSimple.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setDirection(DcMotorSimple.Direction.REVERSE);

        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        intake.setPower(0);
        intakeOther.setPower(0);
//        isBack = false;
    }

    public static void arm_reset(boolean limit) {
        if (limit) {
            armTarget = armTarget - 5;
        } else {
            armTarget = 0;
            mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            mainBoom.setDirection(DcMotorSimple.Direction.REVERSE);
            armHome = true;
            armZeroing = false;
        }
    }

    public static void slide_reset(boolean limit) {
        if (limit) {
            slide.setPower(-0.3);
        } else {
            slideZeroing = false;
            slideTarget = 0;
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            slide.setDirection(DcMotorSimple.Direction.REVERSE);
            slideIsBack = true;
        }
    }

    public static void arm_loop() {
        slidePosition = slide.getCurrentPosition();
        armPosition = mainBoom.getCurrentPosition();
    }

    public static void slide() {
        if (slideTarget > slideLimit) {
            slideTarget = slideLimit;
        }
        if (slideTarget < 0){
            slideTarget = 0;
        }
        if (slideBack.isPressed() && !previousSlideBack) {
            slidePosition = 0;
            slideTarget = 0;
            previousSlideBack = true;
        } else if (!slideBack.isPressed()) {
            previousSlideBack = false;
        }

//        if (slideBack.isPressed() && slideTarget <= slidePosition) {
//            slideTarget = armPosition;
//        }
        slideError = slideTarget - slidePosition;
        if (armPosition > 900) {
            slideLimit = slideMaxLimit;
        } else {
            slideLimit = slideShortLimit;
        }
        //if (!slideZeroing) {
            if (Math.abs(slidePosition - slideTarget) > slideTolerance) {
                double error = slideTarget - slidePosition; // Calculate the error
                double proportionalPower = Math.min(1.0, Math.max(0.2, Math.abs(error) / 500.0)); // Scale power based on error
                if (slidePosition < slideTarget) {
                    slide.setPower(proportionalPower); // Move out with scaled power
                } else if (slidePosition > slideTarget) {
                    slide.setPower(-proportionalPower); // Move in with scaled power
                } else {
                    slide.setPower(0); // Safety stop if limits are exceeded
                }
            } else {
                slide.setPower(0);
            }
        //}
    }
    public static void arm() {
        if (armTarget > armLimit) {
            armTarget = armLimit;
        }
        if (armTarget < 0){
            armTarget = 0;
        }
        if (armDown.isPressed() && !previousArmDown) {
            armPosition = 0;
            armTarget = 0;
            previousArmDown = true;
        } else if (!armDown.isPressed()) {
            previousArmDown = false;
        }
//        if (armUp.isPressed() && armTarget >= armPosition) {
//            armTarget = armPosition + 30;
//        }
        f = fStart + (slidePosition / slideLimit) * (fEnd - fStart);
        f = Math.max(fStart, Math.min(fEnd, f));
        controller.setPID(p, i, d);
        //double pid = controller.calculate(armPosition, armTargetPosition);
        double pid = controller.calculate(armPosition, armTarget);
        double ticksPerDeg = armTicks / 360.0;
        //double ff = Math.cos(Math.toRadians(armTargetPosition / ticksPerDeg)) * f;
        double ff = Math.cos(Math.toRadians(armTarget / ticksPerDeg)) * f;
        double power = pid + ff;
        //int negative;
        //if (armZeroing) {
         //   negative = armTarget;
        //} else {
         //   negative = 0;
        //}
        //armTarget = Math.max(negative, Math.min(armLimit, armTarget));
//        if (Math.abs(armTarget - armTargetPosition) > armSpeed) {
//            armTargetPosition += Math.signum(armTarget - armTargetPosition) * armSpeed;
//        } else {
//            armTargetPosition = armTarget;
//        }
        mainBoom.setPower(power);
    }
    public static void arm_telemetry(Telemetry telemetry) {
        //Arm Telemetry
        telemetry.addData("Arm Position", armPosition);
        telemetry.addData("Arm Target", armTarget);
        telemetry.addData("Arm Ticks", armTicks);

        //Slide Telemetry
        telemetry.addData("Slide Position", slidePosition);
        telemetry.addData("Slide Target", slideTarget);
        telemetry.addData("Slide Ticks", slideTicks);
        telemetry.addData("F Value", f);

        //Intake Telemetry
        telemetry.addData("Wrist 1", wrist1.getPosition());
        telemetry.addData("Wrist 2", wrist2.getPosition());

        //Switch's
        telemetry.addData("Arm Down", armDown.isPressed());
        telemetry.addData("Arm Up", armUp.isPressed());
        telemetry.addData("Slide Back", slideBack.isPressed());
    }
}
