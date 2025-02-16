package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Bar {
    public static class ArmBar implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            armTargetPosition = 800;
            if (armPosition >= 600) {
                wrist1.setPosition(0.9);
                wrist2.setPosition(0.1);
            }
            return true;
        }
    }

    public static Action armBar() {
        return new ArmBar();
    }
}