package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MotorTest")
public class MotorTest extends OpMode {
    public static DcMotor right_front = null;
    public static DcMotor left_front = null;
    public static DcMotor left_back = null;
    public static DcMotor right_back = null;

    @Override
    public void init() {
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");
        telemetry.addData("Press", "Start");
        telemetry.update();
    }
    @Override
    public void loop() {
        if (gamepad1.a) {
            left_front.setPower(1);
        } else {
            left_front.setPower(0);
        }

        if (gamepad1.b) {
            right_front.setPower(1);
        } else {
            right_front.setPower(0);
        }

        if (gamepad1.x) {
            left_back.setPower(1);
        }else {
            left_back.setPower(0);
        }

        if (gamepad1.y) {
            right_back.setPower(1);
        } else {
            right_back.setPower(0);
        }

        telemetry.addData("Press", "Start");
        telemetry.update();
    }
}