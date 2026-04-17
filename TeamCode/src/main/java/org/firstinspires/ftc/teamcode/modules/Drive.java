package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

/** @noinspection unused*/
public class Drive {
    public static DcMotor right_front = null;
    public static DcMotor left_front = null;
    public static DcMotor left_back = null;
    public static DcMotor right_back = null;
    public static IMU imu = null;

    public static double DrivePower = 1;
    public static double DriveSlowPower = 0.6;
    public static double turnFactor = 0;
    public static double strafeFactor = 0.0;
    public static double driveFactor = 0.0;

    public static boolean driving = false;
    public static boolean brakeMode = false;
    public static boolean fieldDriving = false;

    public static void init_drive(HardwareMap hardwareMap) {
        right_front = hardwareMap.get(DcMotor.class, "right_front");
        left_front = hardwareMap.get(DcMotor.class, "left_front");
        left_back = hardwareMap.get(DcMotor.class, "left_back");
        right_back = hardwareMap.get(DcMotor.class, "right_back");
        imu = hardwareMap.get(IMU.class, "imu");
        ImuOrientationOnRobot imuPose;
        if (Robot.robotName().equals("ophelia")) {
            right_back.setDirection(DcMotor.Direction.REVERSE);
            left_back.setDirection(DcMotor.Direction.REVERSE);
            brakeMode = true;
            fieldDriving = true;
            imuPose = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
        } else if (Robot.robotName().equals("mentorbot")) {
            left_front.setDirection(DcMotor.Direction.REVERSE);
            left_back.setDirection(DcMotor.Direction.REVERSE);
            brakeMode = true;
            fieldDriving = false;
            imuPose = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);
        } else {
//            right_back.setDirection(DcMotor.Direction.REVERSE);
            left_back.setDirection(DcMotor.Direction.REVERSE);
//            right_front.setDirection(DcMotor.Direction.REVERSE);
            left_front.setDirection(DcMotor.Direction.REVERSE);
            brakeMode = true;
            imuPose = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
        }
        IMU.Parameters parameters = new IMU.Parameters(imuPose);
        imu.initialize(parameters);
        setBrakeMode(brakeMode);
    }

    public static boolean motorsBusy() {
        return left_front.getPower() != 0 || right_front.getPower() != 0 || left_back.getPower() != 0 || right_back.getPower() != 0;
    }

    public static void drive(Gamepad gamepad, double maxSpeed) {
        driving = motorsBusy();
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = -gamepad.left_stick_y + gamepad.left_stick_x + gamepad.right_stick_x + turnFactor + strafeFactor + driveFactor;
        double frontRightPower = -gamepad.left_stick_y - gamepad.left_stick_x - gamepad.right_stick_x - turnFactor - strafeFactor + driveFactor;
        double backRightPower = -gamepad.left_stick_y + gamepad.left_stick_x - gamepad.right_stick_x - turnFactor + strafeFactor + driveFactor;
        double backLeftPower = -gamepad.left_stick_y - gamepad.left_stick_x + gamepad.right_stick_x + turnFactor - strafeFactor + driveFactor;

        double maxPower = 1.0;

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        left_front.setPower(maxSpeed * (frontLeftPower / maxPower));
        right_front.setPower(maxSpeed * (frontRightPower / maxPower));
        left_back.setPower(maxSpeed * (backLeftPower / maxPower));
        right_back.setPower(maxSpeed * (backRightPower / maxPower));
    }

    public static void fieldDrive(Gamepad gamepad, double maxSpeed) {
        driving = motorsBusy();
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = gamepad.right_stick_x;
        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = ((rotY + rotX + rx) / denominator) + turnFactor + strafeFactor + driveFactor;
        double backLeftPower = ((rotY - rotX + rx) / denominator) + turnFactor - strafeFactor + driveFactor;
        double frontRightPower = ((rotY - rotX - rx) / denominator) - turnFactor - strafeFactor + driveFactor;
        double backRightPower = ((rotY + rotX - rx) / denominator)  - turnFactor + strafeFactor + driveFactor;

        double maxPower = 1.0;

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        left_front.setPower(maxSpeed * (frontLeftPower / maxPower));
        right_front.setPower(maxSpeed * (frontRightPower / maxPower));
        left_back.setPower(maxSpeed * (backLeftPower / maxPower));
        right_back.setPower(maxSpeed * (backRightPower / maxPower));
    }

    public static void setPower(double power, String motor) {
        switch (motor) {
            case "left_front":
                left_front.setPower(power);
                break;
            case "right_front":
                right_front.setPower(power);
                break;
            case "left_back":
                left_back.setPower(power);
                break;
            case "right_back":
                right_back.setPower(power);
                break;
        }
    }

    public static void setBrakeMode(boolean enabled) {
        if (enabled) {
            left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            brakeMode = true;
        } else {
            right_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            left_back.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            brakeMode = false;
        }
    }

    public static void brake(boolean brake) {
        if (brake) {
            setBrakeMode(true);
            left_front.setPower(0);
            right_front.setPower(0);
            left_back.setPower(0);
            right_back.setPower(0);
        } else {
            setBrakeMode(brakeMode);
        }
    }

    public static void auto_drive(MecanumDrive drive, FtcDashboard dashboard) {
        TelemetryPacket packet = new TelemetryPacket();
        Canvas c = packet.fieldOverlay();
        c.setStroke("#3F51B5");
        Drawing.drawRobot(c, drive.localizer.getPose());
        dashboard.sendTelemetryPacket(packet);
    }

    public static void drive_telemetry(Telemetry telemetry) {
        //Wheel Telemetry
//        telemetry.addData("LF POW", left_front.getPower());
//        telemetry.addData("LB POW", left_back.getPower());
//        telemetry.addData("RF POW", right_front.getPower());
//        telemetry.addData("RB POW", right_back.getPower());
//        telemetry.addData("Driving", driving);
//        telemetry.addData("Field Drive", fieldDriving);
//        telemetry.addData("Brake Mode", brakeMode);
    }

    public static void auto_telemetry(Telemetry telemetry, MecanumDrive drive) {
        drive.updatePoseEstimate();
        Pose2d myPose = drive.localizer.getPose();
        telemetry.addData("x", myPose.position.x);
        telemetry.addData("y", myPose.position.y);
        telemetry.addData("heading", myPose.heading.real);
    }
}
