package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.modules.Controller.controllerChange;
import static org.firstinspires.ftc.teamcode.modules.Drive.init_drive;
import static org.firstinspires.ftc.teamcode.modules.Drive.setPower;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MotorTest")
public class MotorTest extends OpMode {
    @Override
    public void init() {
        init_drive(hardwareMap);
        telemetry.addData("Press", "Start");
        telemetry.update();
    }
    @Override
    public void loop() {
        double power;
        if (gamepad1.right_bumper) {
            power = -1;
        } else {
            power = 1;
        }

        if (gamepad1.y) {
            setPower(1* power, "right_front");
        } else {
            setPower(0, "right_front");
        }

        if (gamepad1.x) {
            setPower(1* power, "left_front");
        } else {
            setPower(0, "left_front");
        }

        if (gamepad1.b && controllerChange(gamepad1, gamepad2)) {
            setPower(1* power, "right_back");
        }else {
            setPower(0, "right_back");
        }

        if (gamepad1.a && controllerChange(gamepad1, gamepad2)) {
            setPower(1* power, "left_back");
        } else {
            setPower(0, "left_back");
        }

        telemetry.addLine(" Xbox/PS4 Button - Start");
        telemetry.addLine("   X / ▢  -  Front Left");
        telemetry.addLine("   Y / Δ  -  Front Right");
        telemetry.addLine("   B / O  -  Rear  Right");
        telemetry.addLine("   A / X  -  Rear  Left");
        telemetry.update();
    }
}