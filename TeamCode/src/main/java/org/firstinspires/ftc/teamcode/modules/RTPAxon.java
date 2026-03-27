package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

public class RTPAxon {

    private CRServo servo;
    private DcMotorEx encoder;

    private static final double COUNTS_PER_REV = 8192.0;

    // PID Coefficients
    // Low P to prevent oscillation (gyrating)
    public double kP = .6;
    public double kI = 0.000;
    public double kD = .6;
    public double kFF = 5;

    private double targetAngle = 0.0;
    private double currentAngle = 0.0;
    private double lastError = 0.0;
    private double integralSum = 0.0;

    private double maxPower = 100.0;
    // Deadband: If we are within 5 degrees, stop moving.
    // This stops the servo from hunting back and forth endlessly.
    private double deadband = 2.0;

    public RTPAxon(CRServo servo, DcMotorEx encoder) {
        this.servo = servo;
        this.encoder = encoder;
        this.encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Set initial target to current position so it doesn't snap violently on start
        this.targetAngle = this.getAbsolutePosition();
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
        power = power + kFF * Math.signum(power);

        // Deadband check
        if (Math.abs(error) < deadband) {
            power = 0;
        }

        power = Range.clip(power, -maxPower, maxPower);

        // DIRECTION CHECK:
        // If the servo spins AWAY from the target continuously, remove the negative sign below.
        servo.setPower(power / 100);
    }

    public double getAbsolutePosition() {
        double degrees = (encoder.getCurrentPosition() / COUNTS_PER_REV) * 360.0;
        degrees = degrees % 360.0;
        if (degrees < 0) degrees += 360.0;
        return degrees;
    }

    public void setTargetAngle(double angle) {
        this.targetAngle = angle;
        // Normalize target to 0-360
//		while (this.targetAngle > 360) this.targetAngle -= 360;
//		while (this.targetAngle < 0)   this.targetAngle += 360;
    }

    public void changeTargetRotation(double delta) {
        setTargetAngle(this.targetAngle + delta);
    }

    // Getters for Telemetry
    public double getTargetAngle()  { return targetAngle; }
    public double getCurrentAngle() { return currentAngle; }
    public double getRawCounts()    { return encoder.getCurrentPosition(); }

    // Add this to the bottom of RTPAxon.java

}