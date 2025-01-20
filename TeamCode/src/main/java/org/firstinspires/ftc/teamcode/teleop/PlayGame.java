package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {
    public boolean toggleWrist;
    public boolean goUp;
    public boolean goDown;
    public boolean goX;
    public boolean goY;
    public boolean allowArmInput;
    public boolean continuousIntakeToggle;
    public boolean lastContinuousIntakeToggle;
    public enum IntakeDirection {
        IN,
        OUT
    };
    public IntakeDirection continousIntakeDirection;
    public boolean start;

    @Override
    public void init() {
        //Init Arm
        init_arm(hardwareMap);
        //Init Wheels
        init_drive(hardwareMap);

        toggleWrist = false;
        goUp = false;
        goDown = false;
        goX = false;
        goY = false;
        continuousIntakeToggle = false;
        lastContinuousIntakeToggle = false;
        continousIntakeDirection = IntakeDirection.IN;

        armTarget = 0;
        slideTarget = 0;
        allowArmInput = false;
    }

    @Override
    public void start() {
        start = true;
    }

    @Override
    public void loop() {
        if (start) {
            slideZeroing = true;
            if (!slideBack.isPressed()) {
                slide_reset(true);
                telemetry.addData("Slide is zeroing", "");
            } else if (slideBack.isPressed()) {
                slide_reset(false);
            }
            armZeroing = true;
            if (!armDown.isPressed()) {
                arm_reset(true);
                if (!(armPosition <= armTarget)) {
                    telemetry.addData("Arm is zeroing", armPosition);
                }
            } else if (slideBack.isPressed()) {
                arm_reset(false);
            }
            if (armHome && slideIsBack) {
                start = false;
                allowArmInput = true;
            }
        }

        //Arm Loop
        arm_loop();

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
        if (gamepad2.guide) {
            start = false;
            allowArmInput = true;
            armZeroing = false;
            slideZeroing = false;
        }
        if (allowArmInput) {
            if (gamepad2.left_bumper) {
                slideTarget = Math.max(slideTarget - 35, 0);
            } else if (gamepad2.right_bumper) {
                slideTarget = Math.min(slideTarget + 35, slideLimit);
            }
            if (gamepad2.back) {
                isBack = true;
                slideTarget = 0;
                wrist1.setPosition(0);
                wrist2.setPosition(1);
                allowArmInput = false;
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
                if (wrist1.getPosition() != 0) {
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
                armTarget = 900;
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
            }
            double armInput = -gamepad2.left_stick_y;
            if (gamepad2.left_stick_button) {
                armTarget = 1000;
                slideTarget = 2000;
                goDown = true;
            } else if (Math.abs(armInput) > 0.05) {
                armTarget += (int) (armInput * 10);
            }
            if (gamepad2.right_stick_button) {
                if (continuousIntakeToggle == lastContinuousIntakeToggle) {
                    continuousIntakeToggle = !continuousIntakeToggle;
                }
            } else{
                lastContinuousIntakeToggle = continuousIntakeToggle;
            }
            if (gamepad2.right_stick_y > 0.2){
                continousIntakeDirection = IntakeDirection.IN;
            } else if (gamepad2.right_stick_y < -0.2){
                continousIntakeDirection = IntakeDirection.OUT;
            }
            if (continuousIntakeToggle && continousIntakeDirection == IntakeDirection.IN) {
                intake.setPower(-1);
                intakeOther.setPower(1);
            } else if (continuousIntakeToggle && continousIntakeDirection == IntakeDirection.OUT) {
                intake.setPower(1);
                intakeOther.setPower(-1);
            } else {
                intake.setPower(0);
                intakeOther.setPower(0);
            }
            if (gamepad2.x) {
                goX = true;
                slideTarget = 0;
                wrist1.setPosition(0.4);
                wrist2.setPosition(0.6);
            } else if (gamepad2.y) {
                goY = true;
                armTarget = 1100;
                wrist1.setPosition(0.7);
                wrist2.setPosition(0.3);
            }
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
            armTarget = 85;
            goUp = false;
        }

        //Go Down
        if (goDown && Math.abs(armPosition - armTarget) <= armTolerance) {
            //TODO:
            // slideTargetPosition = 0;
            // isBack = true;
            goDown = false;
        }

        //Go X
        if (goX && (slidePosition <= 1500)) {
            armTarget = 200;
            goX = false;
        }

        //Go Y
        if (goY && Math.abs(armPosition - armTarget) <= armTolerance) {
            slideTarget = slideLimit;
            armTarget = 1200;
            goY = false;
        }

        //Go Home
        if (isBack && Math.abs(slidePosition) <= slideTolerance) {
            armTarget = 0;
        }
        if (isBack && slideBack.isPressed() && armDown.isPressed()) {
            isBack = false;
            arm_reset(false);
            slide_reset(false);
            allowArmInput = true;
        }

        //Slide Power
        slide();

        //Arm Power
        arm();

        //Drive Power
        drive(gamepad1);

        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

        telemetry.addData("Allow Arm Input", allowArmInput);

        telemetry.update();
    }
}