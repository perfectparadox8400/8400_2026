package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name = "LED Hardware Test", group = "Test")
public class LEDTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        // 1. Map the LED (Ensure "ledLeft" matches your Driver Hub config)
        ServoImplEx led = hardwareMap.get(ServoImplEx.class, "ledLeft");

        // 2. CRITICAL: Set the PWM range for goBILDA Prism Driver
        // Without this, the driver often stays dark/off.
        led.setPwmRange(new PwmControl.PwmRange(1000, 2000));

        telemetry.addData("Status", "Initialized. Press Play.");
        telemetry.update();

        waitForStart();
        int color = 0;
        while (opModeIsActive()) {
            // Test Colors based on goBILDA position values

            if (gamepad1.left_bumper){
                color+= 0.5;

            } else if (gamepad1.right_bumper){
                color-= 0.5;
            }
            telemetry.addData("Color:",color);
            if (gamepad1.a) {
                led.setPosition(0.840); // Purple
                telemetry.addData("Color", "Purple");
            } else if (gamepad1.b) {
                led.setPosition(0.389); // Green
                telemetry.addData("Color", "Green");
            } else if (gamepad1.x) {
                led.setPosition(0.480); // Blue (Standard goBILDA value)
                telemetry.addData("Color", "Blue");
            } else {
                led.setPosition(0.0);   // Off / Default pattern
                telemetry.addData("Color", "Off / Default");
            }

            telemetry.update();
        }
    }
}