package org.firstinspires.ftc.teamcode.auto;

import static org.firstinspires.ftc.teamcode.modules.Arm.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

/** @noinspection FieldMayBeFinal*/
public class Start {
    private static boolean runningLoop = false;
    public static class ArmStart implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            runningLoop = false;
            if (slideBack.isPressed()){
                slide_reset(false);
                if(armDown.isPressed()){
                    arm_reset(false);
                    packet.put("Resetting", "Done");
                    armThread.start();
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

    private static Runnable armLoop = new Runnable() {
        private boolean running = true;

        @Override
        public void run() {
            runningLoop = true;
            long startTime = System.currentTimeMillis();
            while (running) {
                if (System.currentTimeMillis() - startTime >= 30500) {
                    //running = false;
                }
                arm_loop();
                arm();
                slide();
            }
        }
    };
    private static Thread armThread = new Thread(armLoop);
}
