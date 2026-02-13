package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.modules.Limelight;

import java.util.List;

@TeleOp(name = "Turret Only Testing")
public class TurretOnly extends OpMode {
    private double turretPosition = 0.5;
    private boolean autoAim = true;

    // Adjust this value to tune sensitivity.
    // Smaller = smoother/slower, Larger = faster/snappier
    private double Kp = 0.001;

    @Override
    public void init() {
        Launcher.Nancy.init_launcher(hardwareMap);
        Limelight.init_limelight(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        turretPosition = 0.5;
        Launcher.turret.setPosition(turretPosition);
        autoAim = true;
    }

    @Override
    public void loop() {
        // Toggle auto-aim master switch (X=ON, Y=OFF)
        if (gamepad1.x) {
            autoAim = true;
        } else if (gamepad1.y) {
            autoAim = false;
        }

        // Manual control via triggers always overrides auto-aim
        if (gamepad1.left_trigger > 0.1) {
            turretPosition -= 0.005;
        } else if (gamepad1.right_trigger > 0.1) {
            turretPosition += 0.005;
        } else if (autoAim) {
            LLResult result = Limelight.limelight.getLatestResult();
            if (result != null && result.isValid()) {
                // Look for AprilTag ID 20 specifically
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducials) {
                    if (fr.getFiducialId() == 20) {
                        double tx = fr.getTargetXDegrees();
                        // Inverted the sign to -= to correct the direction
                        // and used the smaller Kp for smoother tracking
                        turretPosition -= tx * Kp;
                        break;
                    }
                }
            }
        }

        turretPosition = Range.clip(turretPosition, 0, 1);
        Launcher.turret.setPosition(turretPosition);

        telemetry.addData("Auto Aim", autoAim ? "ON (X)" : "OFF (Y)");
        telemetry.addData("Kp (Sensitivity)", Kp);
        Limelight.limelightTelemetry(telemetry);
        telemetry.addData("Turret Position", turretPosition);
        telemetry.update();
    }
}
