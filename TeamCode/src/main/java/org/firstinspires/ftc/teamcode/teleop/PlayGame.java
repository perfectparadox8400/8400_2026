package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Teleop;

@TeleOp(name = "! Play Game")
public class PlayGame extends OpMode {
    @Override
    public void init() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.init(hardwareMap, telemetry);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.init(hardwareMap, telemetry);
        }
    }

    @Override
    public void start() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.start(gamepad1, gamepad2);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.start(gamepad1, gamepad2);
        }
    }

    @Override
    public void loop() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.loop(gamepad1, gamepad2);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.loop(gamepad1, gamepad2);
        }
    }
}