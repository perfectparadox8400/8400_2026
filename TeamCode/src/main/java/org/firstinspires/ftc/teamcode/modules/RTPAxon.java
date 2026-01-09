package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

public class RTPAxon {

    private CRServo servo;
    public AnalogInput encoder;

    // PID Coefficients
    // Low P to prevent oscillation (gyrating)
    private double kP = 0.01;
    private double kI = 0.000;
    private double kD = 0.01;

    private double targetAngle = 0.0;
    private double currentAngle = 0.0;
    private double lastError = 0.0;
    private double integralSum = 0.0;

    private double maxPower = 1.0;
    // Deadband: If we are within 5 degrees, stop moving.
    // This stops the servo from hunting back and forth endlessly.
    private double deadband = 2.0;

    public RTPAxon(CRServo servo, AnalogInput encoder) {
        this.servo = servo;
        this.encoder = encoder;
        // Set initial target to current position so it doesn't snap violently on start
        this.targetAngle = getAbsolutePosition();
    }

    public void update() {
        currentAngle = getAbsolutePosition();
        double error = targetAngle - currentAngle;

        // Shortest Path Logic:
        // Handles the "rollover" (e.g., going from 359 to 1 degree)
        while (error > 180)  error -= 360;
        while (error < -180) error += 360;

        // PID Calculation
        double pOut = kP * error;

        // I term
        if (Math.abs(error) < 10) {
            integralSum += error;
        } else {
            integralSum = 0;
        }
        double iOut = kI * integralSum;

        // D term
        double derivative = error - lastError;
        double dOut = kD * derivative;
        lastError = error;

        double power = pOut + iOut + dOut;

        // Deadband check
        if (Math.abs(error) < deadband) {
            power = 0;
        }

        power = Range.clip(power, -maxPower, maxPower);

        // DIRECTION CHECK:
        // If the servo spins AWAY from the target continuously, remove the negative sign below.
        servo.setPower(power);
    }

    // --- THE FIX IS HERE ---
    public double getAbsolutePosition() {
        // We hardcode 3.3V because using encoder.getMaxVoltage() can sometimes
        // return 0.0 or fluctuate, causing massive math spikes that make the servo gyrate.
        // Axon encoders on the REV Hub reliably map 0V-3.3V to 0-360 degrees.
        return (encoder.getVoltage() / 3.3) * 360.0;
    }

    public void setTargetAngle(double angle) {
        this.targetAngle = angle;
        // Normalize target to 0-360
        while (this.targetAngle > 360) this.targetAngle -= 360;
        while (this.targetAngle < 0)   this.targetAngle += 360;
    }

    public void changeTargetRotation(double delta) {
        setTargetAngle(targetAngle + delta);
    }

    // Getters for Telemetry
    public double getTargetAngle() { return targetAngle; }
    public double getCurrentAngle() { return currentAngle; }
    public double getRawVoltage() { return encoder.getVoltage(); }
    // Add this to the bottom of RTPAxon.java

}


