package org.firstinspires.ftc.teamcode.tune;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Config
@TeleOp(name = "Arm Calibration")
public class ArmCalibration extends OpMode {

    @Override
    public void init() {
        init_arm(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {
        arm_loop();
        if (gamepad2.left_bumper) {
            slideTarget = Math.max(slideTarget - 35, 0);
        } else if (gamepad2.right_bumper) {
            slideTarget = Math.min(slideTarget + 35, slideLimit);
        }
        if (gamepad2.back) {
            isBack = true;
            slideTarget = 0;
        }
        double armInput = -gamepad2.left_stick_y;
        if (Math.abs(armInput) > 0.05) {
            armTarget += (int) (armInput * 10);
        }
        if (isBack && Math.abs(slidePosition) <= slideTolerance) {
            armTarget = 0;
            isBack = false;
        }
        slide();
        arm();
        arm_telemetry(telemetry);
        telemetry.update();
    }
}