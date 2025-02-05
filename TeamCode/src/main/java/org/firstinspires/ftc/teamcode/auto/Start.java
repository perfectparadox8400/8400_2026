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

    public static Runnable armLoop = new Runnable() {
        /** @noinspection InfiniteLoopStatement*/
        @Override
        public void run() {
            while (true) {
                arm_loop();
                arm();
                slide();
            }
        }
    };
    public static Thread armThread = new Thread(armLoop);
}
