package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class Start {
    public static class ArmStart implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (slideBack.isPressed()){
                slide_reset(false);
                if(armDown.isPressed()){
                    arm_reset(false);
                    packet.put("Resetting", "Done");
                    return false;
                } else {
                    packet.put("Resetting", "Arm");
                    arm_reset(true);
                    arm();
                }
            } else {
                packet.put("Resetting", "Slide");
                slide_reset(true);
            }
            return true;
        }
    }

    public static Action armStart() {
        return new ArmStart();
    }

    public static class ArmLoop implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            packet.put("Arm Position", armPosition);
            packet.put("Arm Target", armTargetPosition);
            packet.put("Slide Position", slidePosition);
            packet.put("Slide Target", slideTargetPosition);
            arm_loop();
            arm();
            slide();
            return true;
        }
    }

    public static Action armLoop() {
        return new ArmLoop();
    }
}
