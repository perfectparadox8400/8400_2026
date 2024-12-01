package org.firstinspires.ftc.teamcode.teleop;

import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {
    public CRServo intake = null;
    public Servo wrist1 = null;
    public Servo wrist2 = null;

    public DcMotor mainBoom = null;
    public DcMotor slide = null;

    private PIDController controller;

    //WHEELS
    public DcMotor right_front = null;
    public DcMotor left_front = null;
    public DcMotor left_back = null;
    public DcMotor right_back = null;

    public final double fStart = 0.15;
    public final double fEnd = 0.5;

    public double p = 0.01, i = 0.01, d = 0.0005;
    public double f = fStart;

    public int target = 0;
    public double RAMP_RATE = 10;
    public double currentTarget = 0;

    public final double mainBoomTicks = 28 * 125; //ticks per revolution
    public final double slideTicks = 1425.1; //ticks per revolution
    public final double slideLimit = 5600; //limit 5650
    public int armLimit = 1250;
    public double armTolerance = 50;
    public double slidePosition = 0;
    public double slideTargetPosition = 0;
    public double slideTolerance = 5;
    public double slideError = 0;
    public double DrivePower = 2.5;
    public double DriveSlowPower = 3;
    public double turnFactor;
    public double strafeFactor;
    public boolean toggleWrist;
    public boolean isBack;
    public boolean goUp;
    public boolean goDown;
    public boolean goX;
    public boolean goY;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);

        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        wrist2 = hardwareMap.get(Servo.class, "wrist2");
        intake = hardwareMap.get(CRServo.class, "intake");

        mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        wrist1.setPosition(0);
        wrist2.setPosition(1);
        intake.setPower(0);

        //WHEELS
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");

        right_back.setDirection(DcMotorSimple.Direction.REVERSE);
        left_front.setDirection(DcMotorSimple.Direction.REVERSE);
        slide.setDirection(DcMotorSimple.Direction.REVERSE);

        toggleWrist = false;
        isBack = false;
        goUp = false;
        goDown = false;
        goX = false;
        goY = false;
    }

    @Override
    public void loop() {
        //Arm
        slidePosition = slide.getCurrentPosition();
        int armPos = mainBoom.getCurrentPosition();
        slideError = slideTargetPosition - slidePosition;
        if (Math.abs(slidePosition - slideTargetPosition) > slideTolerance) {
            double error = slideTargetPosition - slidePosition; // Calculate the error
            double proportionalPower = Math.min(1.0, Math.max(0.2, Math.abs(error) / 500.0)); // Scale power based on error

            if (slidePosition < slideTargetPosition && slidePosition <= slideLimit) {
                slide.setPower(proportionalPower); // Move out with scaled power
            } else if (slidePosition > slideTargetPosition && slidePosition >= 0) {
                slide.setPower(-proportionalPower); // Move in with scaled power
            } else {
                slide.setPower(0); // Safety stop if limits are exceeded
            }
        } else {
            slide.setPower(0); // Stop within tolerance
        }

        //Controller Button
        boolean change = !gamepad1.start && !gamepad2.start;

        //Controller1 Buttons
        DrivePower = 2;
        if ((change && gamepad1.b) || gamepad1.right_bumper){
            DrivePower = 1;
        }
        if ((change && gamepad1.a) || gamepad1.left_bumper){
            DrivePower = DriveSlowPower;
        }

        //Controller2 Buttons
        if (gamepad2.left_bumper) {
            slideTargetPosition = Math.max(slideTargetPosition - 35, 0);
        } else if (gamepad2.right_bumper) {
            slideTargetPosition = Math.min(slideTargetPosition + 35, slideLimit);
        }
        if (gamepad2.back) {
            isBack = true;
            slideTargetPosition = 0;
            wrist1.setPosition(0);
            wrist2.setPosition(1);
        }
        if (gamepad2.dpad_up) {
            wrist1.setPosition(wrist1.getPosition() + .005);
            wrist2.setPosition(wrist2.getPosition() - .005);
        }
        if (gamepad2.dpad_down) {
            wrist1.setPosition(wrist1.getPosition() - .005);
            wrist2.setPosition(wrist2.getPosition() + .005);
        }
        if (change && gamepad2.a && !toggleWrist) {
            if(wrist1.getPosition() != 0) {
                wrist1.setPosition(0);
                wrist2.setPosition(1);
            } else {
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
            }
            toggleWrist = true;
        } else if (!gamepad2.a) {
            toggleWrist = false;
        }
        if (change && gamepad2.b) {
           target = 900;
           wrist1.setPosition(0.5);
           wrist2.setPosition(0.5);
        }
        double armInput = -gamepad2.left_stick_y;
        if (gamepad2.left_stick_button) {
            target = 1000;
            slideTargetPosition = 2000;
            goDown = true;
        } else if (Math.abs(armInput) > 0.05) {
            target += (int) (armInput * 10);
        }
        if (gamepad2.right_stick_button) {
            slideTargetPosition = 80;
            goUp = true;
        } else if (gamepad2.right_stick_x > 0.2) {
            //TODO
            //spin wrist right
        } else if (gamepad2.right_stick_x < -0.2) {
            //TODO
            //spin wrist left
        } else if (gamepad2.right_stick_y > 0.2) {
            intake.setPower(1);
        } else if (gamepad2.right_stick_y < -0.2) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }
        if (gamepad2.x) {
            goX = true;
            slideTargetPosition = 0;
            wrist1.setPosition(0.4);
            wrist2.setPosition(0.6);
        } else if (gamepad2.y) {
            goY = true;
            slideTargetPosition = 1500;
            target = 1100;
            wrist1.setPosition(0.7);
            wrist2.setPosition(0.3);
        }
        turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) / DriveSlowPower;
        strafeFactor = 0.0;
        if (gamepad2.dpad_left) {
            strafeFactor = -1.0 / DriveSlowPower; // Strafe left
        } else if (gamepad2.dpad_right) {
            strafeFactor = 1.0 / DriveSlowPower; // Strafe right
        }

        //Go Up
        if (goUp) {
            target = 85;
            goUp = false;
        }

        //Go Down
        if (goDown && Math.abs(armPos - target) <= armTolerance) {
            //TODO
            //slideTargetPosition = 0;
            //isBack = true;
            goDown = false;
        }

        //Go X
        if (goX && (slidePosition <= 1500)) {
            target = 200;
            goX = false;
        }

        //Go Y
        if (goY && Math.abs(armPos - target) <= armTolerance) {
            slideTargetPosition = slideLimit;
            target = 1200;
            goY = false;
        }

        //Go Home
        if (isBack && Math.abs(slidePosition) <= slideTolerance) {
            target = 0;
            isBack = false;
        }

        //Arm Power
        f = fStart + (slidePosition / slideLimit) * (fEnd - fStart);
        f = Math.max(fStart, Math.min(fEnd, f));
        controller.setPID(p, i, d);
        double pid = controller.calculate(armPos, currentTarget);
        double ticksPerDeg = mainBoomTicks / 360.0;
        double ff = Math.cos(Math.toRadians(currentTarget / ticksPerDeg)) * f;
        double power = pid + ff;
        target = Math.max(0, Math.min(armLimit, target));
        if (Math.abs(target - currentTarget) > RAMP_RATE) {
            currentTarget += Math.signum(target - currentTarget) * RAMP_RATE;
        } else {
            currentTarget = target;
        }
        mainBoom.setPower(power);

        //Drive Power
        left_front.setPower(-gamepad1.left_stick_y/DrivePower + gamepad1.left_stick_x/DrivePower + gamepad1.right_stick_x/DrivePower + turnFactor + strafeFactor);
        right_front.setPower(-gamepad1.left_stick_y/DrivePower - gamepad1.left_stick_x/DrivePower - gamepad1.right_stick_x/DrivePower - turnFactor - strafeFactor);
        left_back.setPower(-gamepad1.left_stick_y/DrivePower - gamepad1.left_stick_x/DrivePower + gamepad1.right_stick_x/DrivePower + turnFactor - strafeFactor);
        right_back.setPower(-gamepad1.left_stick_y/DrivePower + gamepad1.left_stick_x/DrivePower - gamepad1.right_stick_x/DrivePower - turnFactor + strafeFactor);

        //Wheel Telemetry
        telemetry.addData("LF POW", left_front.getPower());
        telemetry.addData("LB POW", left_back.getPower());
        telemetry.addData("RF POW", right_front.getPower());
        telemetry.addData("RB POW", right_back.getPower());

        //Arm Telemetry
        telemetry.addData("Main Boom Position", armPos);
        telemetry.addData("Main Boom Target", target);
        telemetry.addData("Slide Position", slidePosition);
        telemetry.addData("Slide Target", slideTargetPosition);
        telemetry.addData("F Value", f);

        //Intake Telemetry
        telemetry.addData("Wrist 1", wrist1.getPosition());
        telemetry.addData("Wrist 2", wrist2.getPosition());
        telemetry.update();
    }
}