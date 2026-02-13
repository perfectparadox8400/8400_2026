package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.modules.Launcher;
import org.firstinspires.ftc.teamcode.modules.Limelight;
import org.firstinspires.ftc.teamcode.modules.Teleop;

import java.util.List;

@TeleOp(name = "! Blue Play Game")
public class BluePlayGame extends OpMode {
    private double turretPosition = 0.5;
    private boolean autoAim = true;
    private double Kp = 0.001;

    @Override
    public void init() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.init(hardwareMap, telemetry);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.init(hardwareMap, telemetry);
        }
        telemetry.addData("Robot Name", Robot.robotName());
        telemetry.update();
    }

    @Override
    public void start() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.start(gamepad1, gamepad2);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.start(gamepad1, gamepad2);
        }
        turretPosition = 0.5;
        autoAim = true;
    }

    @Override
    public void loop() {
        if (Robot.robotName().equals("mentorbot")) {
            Teleop.MentorBot.loop(gamepad1, gamepad2);
        } else if (Robot.robotName().equals("nancy")) {
            Teleop.Nancy.loop(gamepad1, gamepad2);
            
            // Integrated Turret Logic for Nancy
            handleTurret();
            
            telemetry.update();
        }
    }

    private void handleTurret() {
        // Toggle auto-aim master switch (X=ON, Y=OFF) on gamepad1 for testing
        // Note: You might want to move these to specific buttons if they conflict with PlayGame
        if (gamepad1.x) {
            if (autoAim == true){
                autoAim = false;
            } else {
                autoAim = true;
            }
        }

        // Manual control via triggers always overrides auto-aim
        // Using gamepad2 triggers as defined in Teleop.Nancy.loop
        if (gamepad2.left_trigger > 0.1) {
            turretPosition -= 0.005;
        } else if (gamepad2.right_trigger > 0.1) {
            turretPosition += 0.005;
        } else if (autoAim) {
            LLResult result = Limelight.limelight.getLatestResult();
            if (result != null && result.isValid()) {
                // Look for AprilTag ID 20 specifically
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducials) {
                    if (fr.getFiducialId() == 20) {
                        double tx = fr.getTargetXDegrees();
                        turretPosition -= tx * Kp;
                        break;
                    }
                }
            }
        }

        turretPosition = Range.clip(turretPosition, 0, 1);
        Launcher.turret.setPosition(turretPosition);

        telemetry.addData("Auto Aim", autoAim ? "ON" : "OFF");
        telemetry.addData("Turret Position", turretPosition);
    }
}
