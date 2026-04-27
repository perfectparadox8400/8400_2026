package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.modules.Spindexer;
import org.firstinspires.ftc.teamcode.modules.RTPAxon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@TeleOp(name = "Spindexer Test", group = "Test")
public class SpindexerTest extends OpMode {

    private boolean prevLB = false;
    private boolean prevRB = false;
    private int currentPos = 1;

    private boolean prevUp = false;
    private boolean prevDown = false;
    private boolean prevLeft = false;
    private boolean prevRight = false;

    private PrintWriter logWriter;
    private long startTime;

    @Override
    public void init() {
        Spindexer.Nancy.init_spindexer(hardwareMap);
        telemetry.addData("Status", "Spindexer Initialized");

        // Initialize logging
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String filename = "SpindexerLog_" + timestamp + ".csv";
            File logFile = new File(AppUtil.FIRST_FOLDER, filename);
            logWriter = new PrintWriter(new FileWriter(logFile));
            
            // Header for CSV
            logWriter.println("TimeOffset_ms,TargetSlot,TargetAngle,CurrentAngle,SecondaryAngle,PID_kP,PID_kD,PID_kFF,Error");
            startTime = System.currentTimeMillis();
            telemetry.addData("Log", "Writing to " + filename);
        } catch (IOException e) {
            telemetry.addData("Log", "Error: " + e.getMessage());
        }
    }

    @Override
    public void loop() {
        // --- Position Control ---
        // Left Bumper: Cycle Down (1-6)
        if (gamepad2.left_bumper && !prevLB) {
            currentPos--;
            if (currentPos < 1) currentPos = 6;
            Spindexer.Nancy.setPosition(currentPos);
        }
        // Right Bumper: Cycle Up (1-6)
        if (gamepad2.right_bumper && !prevRB) {
            currentPos++;
            if (currentPos > 6) currentPos = 1;
            Spindexer.Nancy.setPosition(currentPos);
        }
        prevLB = gamepad2.left_bumper;
        prevRB = gamepad2.right_bumper;

        // --- PID Tuning (Gamepad 2 D-Pad) ---
        // D-Pad Up/Down: Tune kP
        if (gamepad2.dpad_up && !prevUp) {
            RTPAxon.kP += 0.05;
        }
        if (gamepad2.dpad_down && !prevDown) {
            RTPAxon.kP -= 0.05;
        }
        // D-Pad Left/Right: Tune kD
        if (gamepad2.dpad_right && !prevRight) {
            RTPAxon.kD += 0.1;
        }
        if (gamepad2.dpad_left && !prevLeft) {
            RTPAxon.kD -= 0.1;
        }
        prevUp = gamepad2.dpad_up;
        prevDown = gamepad2.dpad_down;
        prevLeft = gamepad2.dpad_left;
        prevRight = gamepad2.dpad_right;

        // X Button: Small bump to kFF
        if (gamepad2.x) {
            RTPAxon.kFF += 0.1;
        } else if (gamepad2.y) {
            RTPAxon.kFF -= 0.1;
        }

        // --- Update Spindexer ---
        Spindexer.Nancy.spindexer_loop();

        // --- Logging Data ---
        if (logWriter != null && Spindexer.axon != null) {
            long timeOffset = System.currentTimeMillis() - startTime;
            logWriter.printf(Locale.getDefault(), "%d,%d,%.2f,%.2f,%.2f,%.3f,%.3f,%.3f,%.2f\n",
                    timeOffset,
                    currentPos,
                    Spindexer.axon.getTargetAngle(),
                    Spindexer.axon.getCurrentAngle(),
                    Spindexer.axon.getSecondaryAbsolutePosition(),
                    RTPAxon.kP,
                    RTPAxon.kD,
                    RTPAxon.kFF,
                    Spindexer.axon.lastError
            );
        }

        // --- Telemetry ---
        telemetry.addData("--- Spindexer Controls ---", "");
        telemetry.addData("Bumpers", "Switch Positions (1-6)");
        telemetry.addData("Dpad Up/Down", "Tune kP (current: %.2f)", RTPAxon.kP);
        telemetry.addData("Dpad Left/Right", "Tune kD (current: %.2f)", RTPAxon.kD);
        telemetry.addData("Y/X", "Tune kFF (current: %.2f)", RTPAxon.kFF);
        telemetry.addLine();
        telemetry.addData("Current Target Slot", currentPos);
        
        Spindexer.Nancy.spindexer_telemetry(telemetry);
        
        if (Spindexer.axon != null) {
            telemetry.addData("PID Error", "%.2f", Spindexer.axon.lastError);
        }
        
        telemetry.update();
    }

    @Override
    public void stop() {
        if (logWriter != null) {
            logWriter.close();
            logWriter = null;
        }
    }
}
