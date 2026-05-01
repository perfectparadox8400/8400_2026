package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Spindexer {

    // The single static instance of the axon helper
    public static RTPAxon axon;
    private static int spinmode = 0;

    // A static class to handle initialization and operation, similar to your other modules
    public static class Nancy {
        public static void setRotationSpeed(double speed) {
            if (axon != null) axon.changeTargetRotation(speed);
        }

        public static void init_spindexer(HardwareMap hardwareMap) {
            // Get hardware from config
            // MAKE SURE CONFIG NAMES MATCH: "spindexerServo" and "spindexerEncoder"
            CRServo servo = hardwareMap.get(CRServo.class, "spindexerServo");
            AnalogInput encoder = hardwareMap.get(AnalogInput.class, "spindexerEncoder");
            
            // secondaryEncoder is now a DcMotorEx for ABI Quadrature mode
            DcMotorEx secondaryEncoder = hardwareMap.get(DcMotorEx.class, "left_back");

            // Initialize the RTPAxon wrapper
            axon = new RTPAxon(servo, encoder, secondaryEncoder);
        }

        public static void spindexer_loop() {
            // This MUST be called every loop cycle
            if (axon != null) {
                axon.update();
            }
        }



        public static void setTargetRotation(double rot){
//            if (axon != null) axon.changeTargetRotation(-axon.getAbsolutePosition()); //TODO This may be an issue, if the getAbsolutePosition wouldn't work.
            if (axon != null){ axon.changeTargetRotation(rot);}
        }

        public static void rotateLeft() {
            if (axon != null) axon.changeTargetRotation(-30);
        }

        public static void rotateRight() {
            if (axon != null) axon.changeTargetRotation(30);
        }

        public static void spinRight() {
            setRotationSpeed(-5);
        }

        public static void spinLeft() {
            setRotationSpeed(5);
        }

        public static int getSpinPos() {
            return spinmode;
        }
//        public static double spinoffset = -60;
        public static void setPosition(int position) {
            spinmode = position;
            if (axon != null) {
                switch (position) {
                    /* This function is called using setPosition(x);
                    * Below, it specifies the degrees to set the position to based on what it is.
                    * For example, if the position is 1, it sets it to 60 degrees.*/
                    case 4: setTargetRotation(57);
                            break;
                    case 5: setTargetRotation(177);
                        break;
                    case 6: setTargetRotation(297);
                        break;
                    case 1: setTargetRotation(353);
                        break;
                    case 2: setTargetRotation(116);
                        break;
                    case 3: setTargetRotation(235);
//                        break;


//                    case 4: setTargetRotation(351);
//                        break;
//                    case 5: setTargetRotation(110);
//                        break;
//                    case 6: setTargetRotation(225);
//                        break;
//                    case 1: setTargetRotation(284);
//                        break;
//                    case 2: setTargetRotation(50);
//                        break;
//                    case 3: setTargetRotation(172);
                        break;
                }
            }
        }

        public static void spindexer_telemetry(Telemetry telemetry) {
            // Inside spindexer_telemetry
            if (axon != null) {
                telemetry.addData("Axon Target", "%.1f", axon.getTargetAngle());
                telemetry.addData("Axon Current", "%.1f", axon.getCurrentAngle());
                telemetry.addData("Axon Raw Volts", "%.3f", axon.getRawVoltage());
                
                if (axon.secondaryEncoder != null) {
                    telemetry.addData("Secondary Angle (Quad)", "%.1f", axon.getSecondaryAbsolutePosition());
                    telemetry.addData("Secondary Ticks", "%.0f", axon.getSecondaryTicks());
                }
            }
        }
    }
}
