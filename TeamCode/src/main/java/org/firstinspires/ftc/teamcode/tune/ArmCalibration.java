package org.firstinspires.ftc.teamcode.tune;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name = "Arm Calibration")
public class ArmCalibration extends OpMode {
    private PIDController controller;

    public static final double fStart = 0.15;
    public static final double fEnd = 0.5;

    public static double p = 0.01, i = 0.01, d = 0.0005;
    public static double f = fStart;

    public static int target = 0;
    private double currentTarget = 0;
    public static double RAMP_RATE = 10;

    public DcMotor mainBoom = null;
    public DcMotor slide = null;

    private final double mainBoomTicks = 28 * 125; //ticks per revolution
    private final double slideTicks = 1425.1; //ticks per revolution
    private final double slideLimit = -5550; //limit -5650
    private double slidePosition = 0;

    @Override
    public void init() {
        controller = new PIDController(p, i, d);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");

        mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mainBoom.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mainBoom.setDirection(DcMotorSimple.Direction.REVERSE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
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

        telemetry.addData("Main Boom Position", armPos);
        telemetry.addData("Main Boom Target", target);
        telemetry.addData("Slide Position", position);
        telemetry.addData("F Value", f);
        telemetry.update();
    }
}