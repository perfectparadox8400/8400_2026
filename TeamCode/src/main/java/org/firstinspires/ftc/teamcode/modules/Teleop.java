package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.ColorSensor.centerLED;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.getColor;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.leftLED;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.rightLED;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.rotatePositions;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.setLEDColor;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.updateLEDs;
import static org.firstinspires.ftc.teamcode.modules.Controller.controllerChange;
import static org.firstinspires.ftc.teamcode.modules.Controller.rumble;
import static org.firstinspires.ftc.teamcode.modules.Controller.rumbleBlip;
import static org.firstinspires.ftc.teamcode.modules.Controller.rumbleWiiii;
import static org.firstinspires.ftc.teamcode.modules.Controller.speedControl;
import static org.firstinspires.ftc.teamcode.modules.Drive.DrivePower;
import static org.firstinspires.ftc.teamcode.modules.Drive.DriveSlowPower;
import static org.firstinspires.ftc.teamcode.modules.Drive.brakeMode;
import static org.firstinspires.ftc.teamcode.modules.Drive.drive;
import static org.firstinspires.ftc.teamcode.modules.Drive.driveFactor;
import static org.firstinspires.ftc.teamcode.modules.Drive.drive_telemetry;
import static org.firstinspires.ftc.teamcode.modules.Drive.fieldDrive;
import static org.firstinspires.ftc.teamcode.modules.Drive.fieldDriving;
import static org.firstinspires.ftc.teamcode.modules.Drive.imu;
import static org.firstinspires.ftc.teamcode.modules.Drive.init_drive;
import static org.firstinspires.ftc.teamcode.modules.Drive.setBrakeMode;
import static org.firstinspires.ftc.teamcode.modules.Drive.strafeFactor;
import static org.firstinspires.ftc.teamcode.modules.Drive.turnFactor;
import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntake;
import static org.firstinspires.ftc.teamcode.modules.Launcher.flyWheel;
import static org.firstinspires.ftc.teamcode.modules.Launcher.loader;
import static org.firstinspires.ftc.teamcode.modules.Launcher.turret;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.getSpinPos;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setPosition;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setRotationSpeed;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spinLeft;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spinRight;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.axon;
import static org.firstinspires.ftc.teamcode.modules.Limelight.init_limelight;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.init_colorSensor;
import static org.firstinspires.ftc.teamcode.modules.Limelight.limelight;
import static org.firstinspires.ftc.teamcode.modules.Limelight.limelightTelemetry;;
import static org.firstinspires.ftc.teamcode.modules.ColorSensor.colorSensor_telemetry;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_loop; // Import the loop
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.rotateLeft;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.rotateRight;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_telemetry;



import static java.lang.Math.abs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Teleop {
    public static Telemetry telemetry;
    public static final FtcDashboard dashboard = FtcDashboard.getInstance();


    public static class Nancy {
        private static boolean start = false;

        public static int loadPosition = 0;
        public static int launchPosition = 3;
        private static String[] spindexerSlots = new String[] {"Empty", "Empty", "Empty"};
        private static String spindexerMode = ""; // Can be "load" or "launch"
        private static boolean spindexerFull = false;




        public static double flywheelTargetVelocity = 0;
        public static boolean flywheelActive = false;
        private static ElapsedTime runTime = new ElapsedTime();
        private static ElapsedTime runTimetripleshot = new ElapsedTime();
        private static int startingShotPosition = 4;

        private static boolean tripleShotIsActive = false;

        private static boolean previousGamepad2LeftBumper = false;
        private static boolean previousGamepad2RightBumper = false;
        private static boolean previousGamepad1DpadLeft = false;
        private static boolean previousGamepad1DpadRight = false;
        private static boolean previousGamepad1DpadUp = false;
        private static boolean previousGamepad1DpadDown = false;

        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init the launcher
            Launcher.Nancy.init_launcher(hardwareMap);
            Spindexer.Nancy.init_spindexer(hardwareMap);
            // Init drive
            init_drive(hardwareMap);
            // Init Limelight
            init_limelight(hardwareMap);
            // Init Colorsensor
            init_colorSensor(hardwareMap);
            
            // Flywheel setup
            flyWheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // start telemetry
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            telemetry.addData("Press", "Start");
            telemetry.update();
//            RevBlinkinLedDriver blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
//            blinkinLedDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
        }

        public static void start(Gamepad gamepad1, Gamepad gamepad2) {
            loader.setPosition(0.325);
            start = true;
            ballIntake.setPower(1);
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
            spindexerSlots[0] = "Empty";
            spindexerSlots[1] = "Empty";
            spindexerSlots[2] = "Empty";
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Speed Control
            speedControl(gamepad1, gamepad2);

            // Hold start and release guide to disable field driving
            if ((gamepad1.start && gamepad1.guideWasReleased()) || (gamepad2.start && gamepad2.guideWasReleased())) {
                rumbleBlip(gamepad1, 2);
                rumbleBlip(gamepad2, 2);
                fieldDriving = !fieldDriving;
            }

            // Resets the internal geography
            if ((gamepad1.back && gamepad1.guideWasReleased()) || (gamepad2.back && gamepad2.guideWasReleased())) {
                rumbleBlip(gamepad1, 4);
                rumbleBlip(gamepad2, 4);
                imu.resetYaw();
            }

            // fieldDrive is the field centric driving, drive is normal driving
            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
                fieldDrive(gamepad2, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
            }

            /* -------LOADER LOGIC-------*/
            if (gamepad2.aWasPressed()) {
                loader.setPosition(0.0);
                runTime.reset();
            }
            if (runTime.milliseconds() > 300) {
                loader.setPosition(0.325);
            }


            if (gamepad2.yWasPressed()) {
                tripleShotIsActive = true;
                runTimetripleshot.reset();
            }
            if (gamepad2.bWasPressed()) {
                tripleShotIsActive = false;
                loader.setPosition(0.325);
            }
            if (tripleShotIsActive) {
                spindexerMode = "launch";
                if (runTimetripleshot.milliseconds() < 1000) {//changed from 500
                    if (getSpinPos() < 4) {
                        setPosition(5);
                    }
                    if (runTimetripleshot.milliseconds() > 500) {//changed from 300
                        loader.setPosition(0.0);
                    }
                    startingShotPosition = getSpinPos();
                }
                if (runTimetripleshot.milliseconds() > 1000 && runTimetripleshot.milliseconds() < 3000) {//changed from 600
                    loader.setPosition(0.325);

                    if (runTimetripleshot.milliseconds() > 1500) {//changed from 700
                        if (startingShotPosition == 4) {
                            setPosition(5);
                        }
                        if (startingShotPosition == 5) {
                            setPosition(6);
                        }
                        if (startingShotPosition == 6) {
                            setPosition(4);
                        }
                    }
                    if (runTimetripleshot.milliseconds() > 2000) {//changed from 1300
                        loader.setPosition(0.0);
                    }
                }
                if (runTimetripleshot.milliseconds() > 2500) {//changed from 1500
                    loader.setPosition(0.325);

                    if (runTimetripleshot.milliseconds() > 3000) {//changed from 2000
                        if (startingShotPosition == 4) {
                            setPosition(6);
                        }
                        if (startingShotPosition == 5) {
                            setPosition(4);
                        }
                        if (startingShotPosition == 6) {
                            setPosition(5);
                        }
                    }
                    if (runTimetripleshot.milliseconds() > 3500) {//changed from 2500
                        loader.setPosition(0.0);
                    }
                    if (runTimetripleshot.milliseconds() > 4000) {//changed from 2700
                        loader.setPosition(0.325);
                    }
                    if (runTimetripleshot.milliseconds() > 4500) {//changed from 3000
                        tripleShotIsActive = false;
                        setPosition(1);
                        spindexerMode = "load";
                        spindexerSlots[0] = "Empty";
                        spindexerSlots[1] = "Empty";
                        spindexerSlots[2] = "Empty";
                    }
                }

            }


            /* --------FLY WHEEL LOGIC--------*/
            if (gamepad2.xWasPressed()) {
                flywheelActive = !flywheelActive;
            }

            LLResult limelightResult = null;
            if (flywheelActive) {
                limelightResult = Limelight.limelight.getLatestResult();
                if (limelightResult != null && limelightResult.isValid()) {
                    // Formula: velocity = (m * y) + b
                    double ty = -limelightResult.getTy();
                    if(ty < 11.5){
                       flywheelTargetVelocity = (Launcher.shortM * ty) + Launcher.b;

                    } else {
//                        flywheelTargetVelocity = (Launcher.longM * ty) + Launcher.b;
                        flywheelTargetVelocity = 2300;
                    }
                } else {
                    // If no valid target is found, don't update velocity to keep it steady
                    // or default to base velocity b
                    flywheelTargetVelocity = Launcher.b;
                }
                flyWheel.setVelocity(-flywheelTargetVelocity);
            } else {
                flyWheel.setVelocity(0);
                flywheelTargetVelocity = 0;
            }

            /* -------BALL INTAKE LOGIC-------- */
            if (gamepad1.yWasPressed()) {
                if (ballIntake.getPower() > 0) {
                    ballIntake.setPower(0);
                    if (loader.getPosition() != .325)
                        loader.setPosition(.325);
                } else {
                    ballIntake.setPower(1);
                    loader.setPosition(.325);
                }
            }
            // Reverse intake
            if (gamepad1.bWasPressed()) {
                if (ballIntake.getPower() < 0) {
                    ballIntake.setPower(0);
                    if (loader.getPosition() != .325)
                        loader.setPosition(.325);
                } else {
                    ballIntake.setPower(-1);
                    loader.setPosition(.325);
                }
            }

            /*------Color Sensor LOGIC ---------- */
            if (spindexerMode.equals("load")) {
                if (!getColor().equals("unknown")) {
                    int spinIndex = loadPosition - 1;
                    if(spinIndex < 0) {
                        spinIndex = 0;
                    }
                    if(spinIndex > 2) {
                        spinIndex = 2;
                    }
                    if(spindexerSlots[spinIndex].equals("Empty")) {
                        spindexerSlots[spinIndex] = getColor();
                    }
                }
            }
//            for (String slot : spindexerSlots) {
//                spindexerFull = true;
//                if (!slot.equalsIgnoreCase("green") && !slot.equalsIgnoreCase("purple")) {
//                    spindexerFull = false;
//                    break;
//                }
//            }
//            if (spindexerFull) {
//                telemetry.addData("Full: ", "Spindexer is full");
//                ballIntake.setPower(-1);
//            } else {
//                telemetry.addData("Full: ", "No");
//            }

            // Constant LED update
            updateLEDs(spindexerSlots);


            /*------Spindexer Servo LOGIC-------- */
            if (!gamepad1.dpad_up && previousGamepad1DpadUp) {
                Launcher.b += 10;
            }
            if (!gamepad1.dpad_down && previousGamepad1DpadDown) {
                Launcher.b -= 10;
            }
            if (!gamepad1.dpad_right && previousGamepad1DpadRight) {
                Launcher.shortM += 1;
                Launcher.longM += 1;
            }
            if (!gamepad1.dpad_left && previousGamepad1DpadLeft) {
                Launcher.shortM -= 10;
                Launcher.longM -= 10;
            }
//            if (!gamepad1.dpad_up && previousGamepad1DpadUp) {
//                RTPAxon.kD += .1;
//            }
//            if (!gamepad1.dpad_down && previousGamepad1DpadDown) {
//                RTPAxon.kD -= .1;
//            }
//            if (!gamepad1.dpad_right && previousGamepad1DpadRight) {
//                RTPAxon.kP += .1;
//            }
//            if (!gamepad1.dpad_left && previousGamepad1DpadLeft) {
//                RTPAxon.kP -= .1;
//            }
            previousGamepad1DpadUp = gamepad1.dpad_up;
            previousGamepad1DpadDown = gamepad1.dpad_down;
            previousGamepad1DpadRight = gamepad1.dpad_right;
            previousGamepad1DpadLeft = gamepad1.dpad_left;


            if (!gamepad2.left_bumper && previousGamepad2LeftBumper) {
                if (loadPosition > 2)
                    loadPosition = 1;
                else loadPosition++;
                setPosition(loadPosition);
                spindexerMode = "load";
                rotatePositions(spindexerSlots);
            }

            if (!gamepad2.right_bumper && previousGamepad2RightBumper) {
                if (launchPosition > 5)
                    launchPosition = 4;
                else launchPosition++;
                setPosition(launchPosition);
                spindexerMode = "launch";
                rotatePositions(spindexerSlots);
            }
            previousGamepad2LeftBumper = gamepad2.left_bumper;
            previousGamepad2RightBumper = gamepad2.right_bumper;

            // 3. MANDATORY LOOP UPDATE (Calculates PID)
            spindexer_loop();

            /* ------- TELEMETRY --------*/
//            drive_telemetry(telemetry);
            Launcher.Nancy.launcher_telemetry(telemetry);
            spindexer_telemetry(telemetry);
            limelightTelemetry(telemetry);
            colorSensor_telemetry(telemetry, spindexerSlots, spindexerMode);
            telemetry.addData("Flywheel Target", flywheelTargetVelocity);
            telemetry.addData("Flywheel Actual", flyWheel.getVelocity());
            telemetry.addData("Launcher.B (Base velocity)", Launcher.b);
            telemetry.addData("Launcher.m (multiplier", Launcher.shortM + ", " + Launcher.longM);
            telemetry.addData("launchPosition", launchPosition);
            telemetry.addData("loadPosition", loadPosition);
            telemetry.addData("tripleshot timer", runTimetripleshot.milliseconds());
            telemetry.addData("curent spinmode", getSpinPos());
            telemetry.addData("KP Value", RTPAxon.kP);
            telemetry.addData("KD Value", RTPAxon.kD);
            telemetry.addData("Error Value", RTPAxon.lastError);

            telemetry.update();
        }
    }

    public static class MentorBot {
        private static boolean start = false;
        private static ElapsedTime runTime = new ElapsedTime();
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            Launcher.Mentorbot.init_launcher(hardwareMap);
            init_drive(hardwareMap);
            init_limelight(hardwareMap);
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            telemetry.addData("Press", "Start");
            telemetry.update();
        }

        public static void start(Gamepad gamepad1, Gamepad gamepad2) {
            loader.setPosition(.325);
            start = true;
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            speedControl(gamepad1, gamepad2);

            if (gamepad1.aWasPressed()) {
                loader.setPosition(0.0);
                runTime.reset();
            }
            if (runTime.milliseconds() > 250){
                loader.setPosition(0.325);
            }

//            if (gamepad1.xWasPressed()) {
//                if (flyWheel.getPower() == .1) {
//                    flyWheel.setPower(0);
//                } else {
//                    flyWheel.setPower(.1);
//                }
//            }
//
//            if (gamepad1.yWasPressed()) {
//                if (ballIntake.getPower() == -1) {
//                    ballIntake.setPower(0);
//                    if (loader.getPosition() != .325)
//                        loader.setPosition(.325);
//                } else {
//                    ballIntake.setPower(-1);
//                    loader.setPosition(.325);
//                }
//            }

            drive_telemetry(telemetry);
            Launcher.Mentorbot.launcher_telemetry(telemetry);
            limelightTelemetry(telemetry);

            if (gamepad1.start && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 2);
                fieldDriving = !fieldDriving;
            }

            if (gamepad1.back && gamepad1.guideWasReleased()) {
                rumbleBlip(gamepad1, 4);
                imu.resetYaw();
            }

            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
            }

            if (gamepad1.yWasReleased()) {
                rumbleBlip(gamepad1, 1);
                setBrakeMode(!brakeMode);
            }

            turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) * DriveSlowPower;
            if (gamepad2.left_bumper) {
                strafeFactor = -1.0 * DriveSlowPower; // Strafe left
            } else if (gamepad2.right_bumper) {
                strafeFactor = DriveSlowPower; // Strafe right
            } else {
                strafeFactor = 0.0;
            }
            if (gamepad2.dpad_up) {
                driveFactor = DriveSlowPower;
            } else if (gamepad2.dpad_down) {
                driveFactor = -DriveSlowPower;
            } else {
                driveFactor = 0;
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.b) {
                start = false;
                rumble(gamepad2, 500, 1, 1);
            }

            if (gamepad1.touchpadWasPressed()) {
                rumbleWiiii(gamepad1, 5000);
            }

            if (gamepad2.touchpadWasPressed()) {
                rumbleWiiii(gamepad2, 5000);
            }
            
            telemetry.update();
        }
    }
}
