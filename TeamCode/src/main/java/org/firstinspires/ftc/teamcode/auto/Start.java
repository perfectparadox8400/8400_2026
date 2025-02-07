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
                    ArmLoop.startThread();
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

    public static class ArmLoop {
        private static Thread armThread;
        private static volatile boolean running = false;

        public static void startThread() {
            stopThread(); // Ensure any running thread is stopped before starting a new one

            armThread = new Thread(() -> {
                running = true;
                long startTime = System.currentTimeMillis();

                while (running) {
                    if (System.currentTimeMillis() - startTime >= 30500) {
                        break; // Stop after 30.5 seconds
                    }
                    arm_loop();
                    arm();
                    slide();
                }
                running = false;
            });

            armThread.start();
        }

        public static void stopThread() {
            if (armThread != null && armThread.isAlive()) {
                running = false;
                try {
                    armThread.join(); // Wait for the thread to finish before starting a new one
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
