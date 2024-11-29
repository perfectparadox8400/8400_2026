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
    public final double slideLimit = -5550; //limit -5650
    public double slidePosition = 0;
    public double DrivePower = 2.5;
    public double DriveSlowPower = 3;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);

        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");

        mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        wrist1.setPosition(0);
        wrist2.setPosition(1);

        //WHEELS
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");

        right_back.setDirection(DcMotorSimple.Direction.REVERSE);
        left_front.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //Arm Boom & Slide
        double position = slide.getCurrentPosition();
        if (gamepad2.left_bumper && (position <= 0)) {
            slide.setPower(1);
            slidePosition = position;
        } else if (gamepad2.right_bumper && (position >= slideLimit)) {
            slide.setPower(-1);
            slidePosition = position;
        } else {
            if ((slidePosition > position) && (position <= 0)) {
                slide.setPower(0.0001);
            } else if ((slidePosition < position) && (position >= slideLimit)) {
                slide.setPower(-0.0001);
            } else {
                slide.setPower(0);
            }
        }

        double joystickInput = -gamepad2.left_stick_y;
        if (Math.abs(joystickInput) > 0.1) { // Dead zone
            target += (int) (joystickInput * 10);
        }

        target = Math.max(0, Math.min(1150, target));

        // Smooth target transition
        if (Math.abs(target - currentTarget) > RAMP_RATE) {
            currentTarget += Math.signum(target - currentTarget) * RAMP_RATE;
        } else {
            currentTarget = target;
        }

        f = fStart + (position / slideLimit) * (fEnd - fStart);

        f = Math.max(fStart, Math.min(fEnd, f));

        controller.setPID(p, i, d);
        int armPos = mainBoom.getCurrentPosition();
        double pid = controller.calculate(armPos, currentTarget);
        double ticksPerDeg = mainBoomTicks / 360.0;
        double ff = Math.cos(Math.toRadians(currentTarget / ticksPerDeg)) * f;
        double power = pid + ff;
        mainBoom.setPower(power);

        //Controller1 Buttons
        DrivePower = 2.5;
        if (gamepad1.b){
            DrivePower = 1;
        }

        //Controller2 Buttons
        if (gamepad2.dpad_up) {
            wrist1.setPosition(wrist1.getPosition() + .005);
            wrist2.setPosition(wrist2.getPosition() - .005);
        }
        if (gamepad2.dpad_down) {
            wrist1.setPosition(wrist1.getPosition() - .005);
            wrist2.setPosition(wrist2.getPosition() + .005);
        }
        if (gamepad2.a) {
            intake.setPower(1);
        } else if (gamepad2.b) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }

        //Drive
        left_front.setPower(-gamepad1.left_stick_y/DrivePower + gamepad1.left_stick_x/DrivePower + gamepad1.right_stick_x/DrivePower);
        right_front.setPower(-gamepad1.left_stick_y/DrivePower - gamepad1.left_stick_x/DrivePower - gamepad1.right_stick_x/DrivePower);
        left_back.setPower(-gamepad1.left_stick_y/DrivePower - gamepad1.left_stick_x/DrivePower + gamepad1.right_stick_x/DrivePower);
        right_back.setPower(-gamepad1.left_stick_y/DrivePower + gamepad1.left_stick_x/DrivePower - gamepad1.right_stick_x/DrivePower);

        //Wheel Telemetry
        telemetry.addData("LF POW", left_front.getPower());
        telemetry.addData("LB POW", left_back.getPower());
        telemetry.addData("RF POW", right_front.getPower());
        telemetry.addData("RB POW", right_back.getPower());

        //Arm Telemetry
        telemetry.addData("Main Boom Position", armPos);
        telemetry.addData("Main Boom Target", target);
        telemetry.addData("Slide Position", position);
        telemetry.addData("F Value", f);

        //Intake Telemetry
        telemetry.addData("Wrist 1", wrist1.getPosition());
        telemetry.addData("Wrist 2", wrist2.getPosition());
        telemetry.update();
    }
}