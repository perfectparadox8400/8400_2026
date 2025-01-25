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
    public static PIDController armController;
    public static PIDController slideController;
    //public static final double fStart = 0.15;
    //public static final double fEnd = 0.5;
    public static double p = 0.005, i = 0.00, d = 0.00;

    public static double slideP = 0.08, slideI = 0.00, slideD = 0.00;
    //public static double f = fStart;

    //Arm
    public static final double armTicks = 28 * 125; //ticks per revolution
    public static final int armLimit = 1200;
    public static int armTarget = 0;
    public static int armPosition = 0;

    public static double armPower = 0;
    public static double holdingTorque = 0;
    //public static double armSpeed = 10;
    public static double armTolerance = 50;

    public static boolean previousArmDown = false;

    public static double theta = 0;

    //Slide
    public static final double slideTicks = 1425.1; //ticks per revolution
    public static final double slideMaxLimit = 5600; //limit 5650
    public static final double slideShortLimit = 3900;
    public static double slideLimit = slideMaxLimit;
    public static double slideTarget = 0;
    public static double slidePosition;
    public static double slideTolerance = 5;
    public static double slideError = 0;
    public static boolean previousSlideBack = false;

    public static void init_arm(HardwareMap hardwareMap) {
        armController = new PIDController(p, i, d);
        slideController = new PIDController(slideP, slideI, slideD);

        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        wrist2 = hardwareMap.get(Servo.class, "wrist2");
        intake = hardwareMap.get(CRServo.class, "intake");
        intakeOther = hardwareMap.get(CRServo.class, "intake2");
        armDown = hardwareMap.get(TouchSensor.class, "bottom_limit");
        armUp = hardwareMap.get(TouchSensor.class, "arm_limit");
        slideBack = hardwareMap.get(TouchSensor.class, "slide_limit");

        mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mainBoom.setDirection(DcMotorSimple.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setDirection(DcMotorSimple.Direction.REVERSE);

        wrist1.setPosition(0.15);
        wrist2.setPosition(0.85);
        intake.setPower(0);
        intakeOther.setPower(0);
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

    public static void slide_reset(boolean limit) {
        if (limit) {
            slide.setPower(-0.3);
        } else {
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(0);
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
            slideTarget = slidePosition;
            slide.setPower(0);
            previousSlideBack = true;
            return;
        } else if (!slideBack.isPressed()) {
            previousSlideBack = false;
        }

        slideError = slideTarget - slidePosition;
        if (armPosition > 900) {
            slideLimit = slideMaxLimit;
        } else {
            slideLimit = slideShortLimit;
        }

            if (Math.abs(slidePosition - slideTarget) > slideTolerance) {
                slideController.setPID(slideP, slideI, slideD);
                double slidePower = armController.calculate(slidePosition, slideTarget);
                slide.setPower(slidePower);
            } else {
                slide.setPower(0);
            }
    }
    public static void arm() {
        if (armTarget > armLimit) {
            armTarget = armLimit;
        }
        if (armTarget < 0){
            armTarget = 0;
        }
        if (armDown.isPressed() && !previousArmDown) {
            armTarget = armPosition;
            mainBoom.setPower(0);
            previousArmDown = true;
            return;
        } else if (!armDown.isPressed()) {
            previousArmDown = false;
        }

        //f = fStart + (slidePosition / slideLimit) * (fEnd - fStart);
        //f = Math.max(fStart, Math.min(fEnd, f));
        armController.setPID(p, i, d);
        double pid = armController.calculate(armPosition, armTarget);
        double ticksPerDeg = armTicks / 360.0;
        theta = armPosition / ticksPerDeg - 28;
        holdingTorque = 0.000062 * (slidePosition + 4520.0) * Math.cos(Math.toRadians(theta)); //4250 is the contracted arm length in ticks.
        armPower = pid + holdingTorque;

        mainBoom.setPower(armPower);
    }
    public static void arm_telemetry(Telemetry telemetry) {
        //Arm Telemetry
        telemetry.addData("Arm Position", armPosition);
        telemetry.addData("Arm Target", armTarget);
        telemetry.addData("Arm Ticks", armTicks);
        telemetry.addData("Arm Power", armPower);

        //Slide Telemetry
        telemetry.addData("Slide Position", slidePosition);
        telemetry.addData("Slide Target", slideTarget);
        telemetry.addData("Slide Ticks", slideTicks);
        //telemetry.addData("F Value", f);

        //Intake Telemetry
        telemetry.addData("Wrist 1", wrist1.getPosition());
        telemetry.addData("Wrist 2", wrist2.getPosition());

        //Switch's
        telemetry.addData("Arm Down", armDown.isPressed());
        telemetry.addData("Arm Up", armUp.isPressed());
        telemetry.addData("Slide Back", slideBack.isPressed());
        telemetry.addData("Holding Torque", holdingTorque);
        telemetry.addData("Theta", theta);
    }
}
