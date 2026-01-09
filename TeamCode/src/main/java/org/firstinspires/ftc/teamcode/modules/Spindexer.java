package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Spindexer {

    // The single static instance of the axon helper
    public static RTPAxon axon;

    // A static class to handle initialization and operation, similar to your other modules
    public static class Nancy {

        public static void init_spindexer(HardwareMap hardwareMap) {
            // Get hardware from config
            // MAKE SURE CONFIG NAMES MATCH: "spindexerServo" and "spindexerEncoder"
            CRServo servo = hardwareMap.get(CRServo.class, "spindexerServo");
            AnalogInput encoder = hardwareMap.get(AnalogInput.class, "spindexerEncoder");

            // Initialize the RTPAxon wrapper
            axon = new RTPAxon(servo, encoder);
        }

        public static void spindexer_loop() {
            // This MUST be called every loop cycle
            if (axon != null) {
                axon.update();
            }
        }

        public static void rotateLeft() {
            if (axon != null) axon.changeTargetRotation(-45);
        }

        public static void rotateRight() {
            if (axon != null) axon.changeTargetRotation(45);
        }

        public static void spindexer_telemetry(Telemetry telemetry) {
            // Inside spindexer_telemetry
            if (axon != null) {
                telemetry.addData("Axon Target", "%.1f", axon.getTargetAngle());
                telemetry.addData("Axon Current", "%.1f", axon.getCurrentAngle());
                telemetry.addData("Axon Raw Volts", "%.3f", axon.getRawVoltage());

            }

        }
    }
}
