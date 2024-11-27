package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

@TeleOp(name = "! Play Game")
public class PlayGame extends LinearOpMode {
    private CRServo intake;
    //wrist 1
    private Servo wrist1;
    //wrist 2
    private Servo wrist2;

    public DcMotor mainBoom = null;
    public DcMotor slide = null;

    //WHEELS
    public DcMotor right_front = null;
    public DcMotor left_front = null;
    public DcMotor left_back = null;
    public DcMotor right_back = null;
    
    @Override
    public void runOpMode() {
        mainBoom = hardwareMap.get(DcMotor.class, "main_arm");
        slide = hardwareMap.get(DcMotor.class, "slide");
        wrist1 = hardwareMap.get(Servo.class, "wrist1");
        wrist2 = hardwareMap.get(Servo.class, "wrist2");
        intake = hardwareMap.get(CRServo.class, "intake");

        mainBoom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mainBoom.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mainBoom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double angle;
        double position;
        //Power to hold Main Boom when Jib Boom is at 90 Degrees
        double c1 = .001;

        double c1Tmp;

        double Pa = 0;

        double holdTorque;

        wrist1.setPosition(0);
        wrist2.setPosition(1);
        double mainBoomTicks = 28 * 125; //ticks per revolution
        double slideTicks = 28 * 125;
        double slidePosition = 0;
        double slideLimit = -590; //Max -590

        //WHEELS
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");

        double power;

        // Reverse one of the drive motors.
        right_back.setDirection(DcMotorSimple.Direction.REVERSE);
        left_front.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //ARM
                angle = mainBoom.getCurrentPosition() * 360/mainBoomTicks;
                position = slide.getCurrentPosition() * 360/slideTicks;
                angle += 40;

                if (gamepad2.dpad_up) {
                    wrist1.setPosition(wrist1.getPosition() + .005);
                    wrist2.setPosition(wrist2.getPosition() - .005);
                }
                if (gamepad2.dpad_down) {
                    wrist1.setPosition(wrist1.getPosition() - .005);
                    wrist2.setPosition(wrist2.getPosition() + .005);
                }

                c1Tmp = c1;

                holdTorque = c1Tmp * cos(toRadians(angle));

                Pa = (-gamepad2.left_stick_y * 1);
                if (Pa < 0) {
                    Pa -= holdTorque;
                } else {
                    Pa += holdTorque;
                }

                if (gamepad2.a) {
                    intake.setPower(1);
                } else if (gamepad2.b) {
                    intake.setPower(-1);
                } else {
                    intake.setPower(0);
                }

                if (gamepad2.left_bumper && (position <= 0)) {
                    slide.setPower(1);
                    slidePosition = position;
                } else if (gamepad2.right_bumper && (position >= slideLimit)) { //max -592
                    slide.setPower(-1);
                    slidePosition = position;
                } else {
                    if ((slidePosition > position) && (position <= 0)) {
                        slide.setPower(0.001);
                    } else if ((slidePosition < position) && (position >= slideLimit)) {
                        slide.setPower(-0.001);
                    } else {
                        slide.setPower(0);
                    }
                }

                //WHEELS
                power = 2.5;
                if (gamepad1.b){
                    power = 0.5;
                }
                if (gamepad1.y) {
                    power = 1;
                }
                mainBoom.setPower(Pa);

                left_front.setPower(-gamepad1.left_stick_y/power + gamepad1.left_stick_x/power + gamepad1.right_stick_x/power);
                right_front.setPower(-gamepad1.left_stick_y/power - gamepad1.left_stick_x/power - gamepad1.right_stick_x/power);
                left_back.setPower(-gamepad1.left_stick_y/power - gamepad1.left_stick_x/power + gamepad1.right_stick_x/power);
                right_back.setPower(-gamepad1.left_stick_y/power + gamepad1.left_stick_x/power - gamepad1.right_stick_x/power);

                //Wheel Telemetry
                telemetry.addData("LF POW", left_front.getPower());
                telemetry.addData("LB POW", left_back.getPower());
                telemetry.addData("RF POW", right_front.getPower());
                telemetry.addData("RB POW", right_back.getPower());

                //Arm Telemetry
                telemetry.addData("Arm Position", mainBoom.getCurrentPosition());
                telemetry.addData("Main Boom Power", Pa);
                telemetry.addData("Main Boom Angle", angle);
                telemetry.addData("Jib Boom Position", position);
                telemetry.addData("Wrist 1", wrist1.getPosition());
                telemetry.addData("Wrist 2", wrist2.getPosition());
                telemetry.update();
            }
        }
    }
}