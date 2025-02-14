package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Drive {
    public static DcMotor right_front = null;
    public static DcMotor left_front = null;
    public static DcMotor left_back = null;
    public static DcMotor right_back = null;

    public static double DrivePower = 2;
    public static double DriveSlowPower = 3;
    public static double turnFactor = 0;
    public static double strafeFactor = 0.0;

    public static void init_drive(HardwareMap hardwareMap) {
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");
    }

    public static void drive(Gamepad gamepad) {
        left_front.setPower(-gamepad.left_stick_y/DrivePower + gamepad.left_stick_x/DrivePower + gamepad.right_stick_x/DrivePower + turnFactor + strafeFactor);
        right_front.setPower(-gamepad.left_stick_y/DrivePower - gamepad.left_stick_x/DrivePower - gamepad.right_stick_x/DrivePower - turnFactor - strafeFactor);
        left_back.setPower(-gamepad.left_stick_y/DrivePower - gamepad.left_stick_x/DrivePower + gamepad.right_stick_x/DrivePower + turnFactor - strafeFactor);
        right_back.setPower(-gamepad.left_stick_y/DrivePower + gamepad.left_stick_x/DrivePower - gamepad.right_stick_x/DrivePower - turnFactor + strafeFactor);
    }

    public static void drive_telemetry(Telemetry telemetry) {
        //Wheel Telemetry
        telemetry.addData("LF POW", left_front.getPower());
        telemetry.addData("LB POW", left_back.getPower());
        telemetry.addData("RF POW", right_front.getPower());
        telemetry.addData("RB POW", right_back.getPower());
    }
}
