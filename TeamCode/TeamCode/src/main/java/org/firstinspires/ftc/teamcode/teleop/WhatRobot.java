package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name = "WhatRobot")
public class WhatRobot extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Robot", Robot.robotName());
        telemetry.update();
    }
    @Override
    public void loop() {
        telemetry.addData("Robot", Robot.robotName());
        telemetry.update();
    }
}