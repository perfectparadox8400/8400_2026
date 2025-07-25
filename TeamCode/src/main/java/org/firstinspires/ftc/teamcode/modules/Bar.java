package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Robot;

public class Bar {
    public static class ArmBar implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (Robot.robotName().equals("mentorbot")) {
                armTargetPosition = 10000;
                wrist1.setPosition(0.5);
                wrist2.setPosition(0.5);
                return armPosition < 10000;
            } else if (Robot.robotName().equals("nancy")) {
                armTargetPosition = 800;
                if (armPosition >= 600) {
                    wrist1.setPosition(0.3);
                    wrist2.setPosition(0.7);
                }
            }
            return true;
        }
    }

    public static Action armBar() {
        return new ArmBar();
    }
}