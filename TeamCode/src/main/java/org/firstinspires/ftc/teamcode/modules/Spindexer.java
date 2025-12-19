//Info for this module comes from https://github.com/The-Robotics-Catalyst-Foundation/FIRST-Opensource/tree/main/FTC/RTPAxon

package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Teleop.telemetry;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Spindexer {
    public static CRServo spindexerServo = null;
    public static AnalogInput spindexerEncoder = null;
    public static RTPAxon axon = null;
    public static class Nancy {

        public static void init_spindexer(HardwareMap hardwareMap) { // This defines which Servo goes to which port, we need to apply this on the robot servos.
            spindexerServo = hardwareMap.get(CRServo.class, "spindexerServo");
            spindexerEncoder = hardwareMap.get(AnalogInput.class, "Spinencoder");
            axon = new RTPAxon(spindexerServo, spindexerEncoder);
            axon.setMaxPower(0.5);  // Limit max power to 50%
            axon.setPidCoeffs(0.02, 0.0005, 0.0025);  // Set PID coefficients
        }

            //@Override
            public void spindexerLoop() {
                axon.update(); // Must be called every loop

                telemetry.addData("Servo Position", axon.getCurrentAngle());
                telemetry.addData("Total Rotation", axon.getTotalRotation());
                telemetry.addData("Target Rotation", axon.getTargetRotation());
                telemetry.update();
            }
        }

        // calls the data



    public static class Mentorbot {
        public static void init_spindexer(HardwareMap hardwareMap) { // This defines which Servo goes to which port, we need to apply this on the robot servos.
            spindexerServo = hardwareMap.get(CRServo.class, "spindexerServo");
            spindexerEncoder = hardwareMap.get(AnalogInput.class, "Spinencoder");
            axon = new RTPAxon(spindexerServo, spindexerEncoder);
            axon.setMaxPower(0.5);  // Limit max power to 50%
            axon.setPidCoeffs(0.02, 0.0005, 0.0025);  // Set PID coefficients
        }

        //@Override
        public void spindexerLoop() {
            axon.update(); // Must be called every loop

            telemetry.addData("Servo Position", axon.getCurrentAngle());
            telemetry.addData("Total Rotation", axon.getTotalRotation());
            telemetry.addData("Target Rotation", axon.getTargetRotation());
            telemetry.update();



        }
    }
}

