package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RTPAxon {

    private CRServo servo;
    public AnalogInput encoder;
    public DcMotorEx secondaryEncoder;

    // PID Coefficients
    public static double kP = 0.4;
    public static double kI = 0.000;
    public static double kD = 0.05;
    public static double kFF = 7.0;

    private double targetAngle = 0.0;
    private double currentAngle = 0.0;
    private double lastAngle = 0.0;
    public static double lastError = 0.0;
    private double integralSum = 0.0;
    
    private ElapsedTime timer = new ElapsedTime();
    private double lastTime = 0;
    
    // Low-pass filter for the derivative term
    private double filteredVelocity = 0;
    // Higher gain = less lag, but more noise. 0.8 is a good balance for Axon.
    public static double gain = 0.8;

    private double maxPower = 100.0;
    private double deadband = 1.0;

    public RTPAxon(CRServo servo, AnalogInput encoder) {
        this.servo = servo;
        this.encoder = encoder;
        this.targetAngle = getAbsolutePosition();
        this.lastAngle = targetAngle;
        timer.reset();
    }

    public RTPAxon(CRServo servo, AnalogInput encoder, DcMotorEx secondaryEncoder) {
        this.servo = servo;
        this.encoder = encoder;
        this.secondaryEncoder = secondaryEncoder;
        
        if (this.secondaryEncoder != null) {
            this.secondaryEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            this.secondaryEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        
        this.targetAngle = getAbsolutePosition();
        this.lastAngle = targetAngle;
        timer.reset();
    }

    public void update() {
        double currentTime = timer.seconds();
        double deltaTime = currentTime - lastTime;
        if (deltaTime <= 0 || deltaTime > 0.1) deltaTime = 0.01; 

        currentAngle = getAbsolutePosition();
        double error = targetAngle - currentAngle;

        // Shortest Path Logic
        while (error > 180)  error -= 360;
        while (error < -180) error += 360;

        // P Term
        double pOut = kP * error;

        // I Term
        if (Math.abs(error) < 10) {
            integralSum += (error * deltaTime);
        } else {
            integralSum = 0;
        }
        double iOut = kI * integralSum;

        // D Term with Low-Pass Filter
        double angleChange = currentAngle - lastAngle;
        while (angleChange > 180) angleChange -= 360;
        while (angleChange < -180) angleChange += 360;
        
        double rawVelocity = angleChange / deltaTime;
        // Faster filter to minimize lag-induced oscillation
        filteredVelocity = (gain * rawVelocity) + ((1 - gain) * filteredVelocity);
        double dOut = -kD * filteredVelocity;

        // Tapered Feedforward
        // Changed divisor to 3.0 so it stays at full power until 3 degrees away
        double ffScale = Range.clip(Math.abs(error) / 3.0, 0, 1);
        double ffOut = kFF * Math.signum(error) * ffScale;
        
        // Combine Terms
        double power = pOut + iOut + dOut + ffOut;

        // Deadband check
        if (Math.abs(error) < deadband) {
            power = 0;
            integralSum = 0;
        }

        power = Range.clip(power, -maxPower, maxPower);
        servo.setPower(power/100);
        
        // Save state for next loop
        lastError = error;
        lastAngle = currentAngle;
        lastTime = currentTime;
    }


    public double getAbsolutePosition() {
        return (encoder.getVoltage() / 3.3) * 360.0;
    }

    public double getSecondaryAbsolutePosition() {
        if (secondaryEncoder == null) return -1.0;
        double ticks = secondaryEncoder.getCurrentPosition();
        double angle = (ticks / 8192.0) * 360.0;
        return ((angle % 360.0) + 360.0) % 360.0;
    }

    public void setTargetAngle(double angle) {
        this.targetAngle = angle;
    }

    public void changeTargetRotation(double delta) {
        setTargetAngle(delta);
    }

    public double getTargetAngle() { return targetAngle; }
    public double getCurrentAngle() { return currentAngle; }
    public double getRawVoltage() { return encoder.getVoltage(); }
    
    public double getSecondaryTicks() {
        if (secondaryEncoder == null) return -1.0;
        return secondaryEncoder.getCurrentPosition();
    }

}
