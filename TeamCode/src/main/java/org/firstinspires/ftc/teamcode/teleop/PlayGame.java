package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;
import static org.firstinspires.ftc.teamcode.modules.Drive.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {
    public boolean goX;
    public boolean goY;
    public boolean start;

    @Override
    public void init() {
        //Init Arm
        init_arm(hardwareMap);
        //Init Wheels
        init_drive(hardwareMap);
    }

    @Override
    public void start() {
        start = true;
    }

    @Override
    public void loop() {
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

        //Drive Power
        drive(gamepad1);

        drive_telemetry(telemetry);
        arm_telemetry(telemetry);

        telemetry.update();

        if (gamepad2.guide) {
            start = false;
        }

        if(start) {
            wrist1.setPosition(0.15);
            wrist2.setPosition(0.85);
         if (slideBack.isPressed()){
             slide_reset(false);

             if(armDown.isPressed()){
                 arm_reset(false);
                 start = false;
             } else {
                 arm_reset(true);
                 arm();
             }
         } else {
             slide_reset(true);
         }
         return;
        }

        //Controller2 Buttons

            /* ------ SLIDE LOGIC ------ */
            if (gamepad2.left_bumper) {
                //slideTarget = Math.max(slideTarget - 35, 0);
                slideTarget = slideTarget - 35;
            } else if (gamepad2.right_bumper) {
                //slideTarget = Math.min(slideTarget + 35, slideLimit);
                slideTarget = slideTarget + 35;
            }

            /* ------ RESET ------ */
            if (gamepad2.back) {
                start = true;
            }

            /* ------WRIST LOGIC------ */
            if (gamepad2.dpad_up) {
                wrist1.setPosition(wrist1.getPosition() + .007);
                wrist2.setPosition(wrist2.getPosition() - .007);
            }
            if (gamepad2.dpad_down) {
                wrist1.setPosition(wrist1.getPosition() - .007);
                wrist2.setPosition(wrist2.getPosition() + .007);
            }

            /* ------ MAINBOOM LOGIC ------ */
            if (gamepad2.left_stick_y > 0.2){
                armTarget = armTarget - 10;
            } else if (gamepad2.left_stick_y < -0.2){
                armTarget = armTarget + 10;
            }

            /* ------ INTAKE LOGIC ------ */
            if (gamepad2.right_stick_y > 0.2){
                intake.setPower(-1);
                intakeOther.setPower(1);
           } else if (gamepad2.right_stick_y < -0.2){
                intake.setPower(1);
                intakeOther.setPower(-1);
            } else {
                intake.setPower(0);
                intakeOther.setPower(0);
            }

            /* ------ PRESETS ------ */
            if (gamepad2.x) {
                goX = true;
                slideTarget = 0;
                wrist1.setPosition(0.4);
                wrist2.setPosition(0.6);
            }

            if (gamepad2.y) {
                goY = true;
                armTarget = 1100;
                wrist1.setPosition(0.7);
                wrist2.setPosition(0.3);
            }
        //}

        //turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) / DriveSlowPower;
        //strafeFactor = 0.0;
        //if (gamepad2.dpad_left) {
          //  strafeFactor = -1.0 / DriveSlowPower; // Strafe left
        //} else if (gamepad2.dpad_right) {
          //  strafeFactor = 1.0 / DriveSlowPower; // Strafe right
        //}

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

        //Slide Power
        slide();

        //Arm Power
        arm();
    }
}