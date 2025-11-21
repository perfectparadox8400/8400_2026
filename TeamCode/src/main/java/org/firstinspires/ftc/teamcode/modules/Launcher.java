package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Launcher {
    //loader will input artifacts into the launcher
    public static Servo loader = null;
    public static class Nancy {
        public static void init_launcher(HardwareMap hardwareMap) { // This defines which Servo goes to which port, we need to apply this on the robot servos.
            loader = hardwareMap.get(Servo.class, "loader");
        }
        public static void launcher_telemetry(Telemetry telemetry) {
            telemetry.addData("loader", loader.getPosition()); // This stores where the loader is set to.
        }

        public static class puncher{
            public static class Home{
                public static double loader = 0.15;
            }
        }
    }
}

