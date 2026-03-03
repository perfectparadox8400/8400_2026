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
        ServoImplEx led1 = hardwareMap.get(ServoImplEx.class, "ledCenter");
        ServoImplEx led2 = hardwareMap.get(ServoImplEx.class, "ledRight");


        // 2. CRITICAL: Set the PWM range for goBILDA Prism Driver
        // Without this, the driver often sta  ys dark/off.
        led.setPwmRange(new PwmControl.PwmRange(1000, 2000));

        telemetry.addData("Status", "Initialized. Press Play.");
        telemetry.update();

        waitForStart();
        double color = 0;
        double bcolor  = 0;
        while (opModeIsActive()) {
            // Test Colors based on goBILDA position values

            if (gamepad1.left_bumper){
                color += 0.0005;
                bcolor = color;
            } else if (gamepad1.right_bumper){
                color-= 0.0005;
                bcolor = color;
            }
            telemetry.addData("Color:",color);
            telemetry.addData("B-Color:",bcolor);
            if (gamepad1.a) {
                color = 0.722; // Purple
                bcolor = color + 0.142;
                telemetry.addData("Color", "Purple");
            } else if (gamepad1.b) {
                color = 0.500; // Green
                bcolor = color;
                telemetry.addData("Color", "Green");
            } else if (gamepad1.x){
                color = 0;
                bcolor = color;
                telemetry.addData("Color", "Off / Default");
            }
            led.setPosition(color);
            led1.setPosition(color);
            led2.setPosition(color);
            telemetry.update();
        }
    }
}