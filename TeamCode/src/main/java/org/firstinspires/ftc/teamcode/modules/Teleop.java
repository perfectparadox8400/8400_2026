package org.firstinspires.ftc.teamcode.modules;

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
import static org.firstinspires.ftc.teamcode.modules.Launcher.ballIntakeRear;
import static org.firstinspires.ftc.teamcode.modules.Launcher.flyWheel;
import static org.firstinspires.ftc.teamcode.modules.Launcher.loader;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setPosition;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.setRotationSpeed;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spinLeft;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spinRight;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.axon;
import static org.firstinspires.ftc.teamcode.modules.Limelight.limelight;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_loop; // Import the loop
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.rotateLeft;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.rotateRight;
import static org.firstinspires.ftc.teamcode.modules.Spindexer.Nancy.spindexer_telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Teleop {
    public static Telemetry telemetry;
    public static final FtcDashboard dashboard = FtcDashboard.getInstance();

    public static class Nancy {
        private static boolean start = false;

        public static int loadPosition = 0;
        public static int launchPosition = 3;
        private static ElapsedTime runTime = new ElapsedTime();

        private static boolean previousGamepad2LeftBumper = false;
        private static boolean previousGamepad2RightBumper = false;
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init the launcher
            Launcher.Nancy.init_launcher(hardwareMap);
            Spindexer.Nancy.init_spindexer(hardwareMap);
            // Init drive
            init_drive(hardwareMap);
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
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Launcher Loop
            // Launcher.Nancy.launcher_loop();

            /* -------CONTROLLER1 BUTTONS-------- */

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


            // Moves everything back to default position
//            if (gamepad1.guideWasPressed() || gamepad2.guideWasPressed()) {
//                // TODO: Put logic to reset to default positions
//                rumble(gamepad2, 750, 1, 1);
//            }

            // fieldDrive is the field centric driving, drive is normal driving
            // Toggles between the two.
            if (fieldDriving) {
                fieldDrive(gamepad1, DrivePower);
                fieldDrive(gamepad2, DrivePower);
            } else {
                drive(gamepad1, DrivePower);
//                drive(gamepad2, DrivePower);
            }

//            // Turns off breakMode
//            if (gamepad1.yWasReleased()){
//                rumbleBlip(gamepad1, 1);
//                setBrakeMode(!brakeMode);
//            }

            /* -------LOADER LOGIC-------*/
            if (gamepad2.aWasPressed()) {
                loader.setPosition(0.0);
                runTime.reset();
            }
            if (runTime.milliseconds() > 500){
                loader.setPosition(0.325);
            }

            /* --------FLY WHEEL LOGIC--------*/
            if (gamepad2.xWasPressed()) {
                if (flyWheel.getPower() < 0) {
                    flyWheel.setPower(0);
                } else {
                    flyWheel.setPower(-1);
                }
            }
            /* -------BALL INTAKE LOGIC-------- */
            if (gamepad1.yWasPressed()) {
                if (ballIntake.getPower() < 0) {
                    ballIntake.setPower(0);
                    ballIntakeRear.setPower(0);
                    if (loader.getPosition() != .325)
                        loader.setPosition(.325);
                } else {
                    ballIntake.setPower(-0.75);
                    ballIntakeRear.setPower(-1);
                    loader.setPosition(.325);
                }
            }

            /*------Spindexer Servo LOGIC-------- */
            // 2. BUTTON LOGIC (Change Targets)



            if (!gamepad2.left_bumper && previousGamepad2LeftBumper) {
                if (loadPosition > 2)
                    loadPosition = 1;
                else loadPosition++;
                setPosition(loadPosition);
            }
            if (!gamepad2.right_bumper && previousGamepad2RightBumper) {
                if (launchPosition > 5)
                    launchPosition = 4;
                else launchPosition++;
                setPosition(launchPosition);
            }
            previousGamepad2LeftBumper = gamepad2.left_bumper;
            previousGamepad2RightBumper = gamepad2.right_bumper;


            // 3. MANDATORY LOOP UPDATE (Calculates PID)
            spindexer_loop();
            //These variables are for setting the
            //axon.setTargetRotation(90);    // Move to 90 degrees absolute
            //axon.changeTargetRotation(45); // Move 45 degrees from current position

            /* ------- TELEMETRY --------*/
            drive_telemetry(telemetry);
            Launcher.Nancy.launcher_telemetry(telemetry);
            spindexer_telemetry(telemetry);
//            telemetry.addData("Start", start);
//            telemetry.addData("loader", loader);
//            telemetry.addData("flyWheel", flyWheel);
//            telemetry.addData("ballIntake", ballIntake);
            telemetry.addData("loadPosition", loadPosition);
            telemetry.addData("launchPosition", launchPosition);
            telemetry.update();

            // This code runs the slow-strafe and allows strafing on controller 2
//            turnFactor = (gamepad2.right_trigger - gamepad2.left_trigger) * DriveSlowPower;
//            if (gamepad2.dpad_left) {
//                strafeFactor = -1.0 * DrivePower; // Strafe left
//            } else if (gamepad2.dpad_right) {
//                strafeFactor = DrivePower; // Strafe right
//            } else {
//                strafeFactor = 0.0;
//            }

        }
    }



    public static class MentorBot {
        private static boolean start = false;
        private static ElapsedTime runTime = new ElapsedTime();
        public static void init(HardwareMap hardwareMap, Telemetry telemetryOld) {
            // Init Arm
            Launcher.Mentorbot.init_launcher(hardwareMap);
            // Init Wheels
            init_drive(hardwareMap);
            telemetry = new MultipleTelemetry(telemetryOld, dashboard.getTelemetry());
            telemetry.addData("Press", "Start");
            telemetry.update();
        }

        public static void start(Gamepad gamepad1, Gamepad gamepad2) {

            start = true;
//            Arm.MentorBot.armOverride = false;
//            wrist1.setPosition(0);
//            wrist2.setPosition(1);
            loader.setPosition(.325);
            start = true;
//            flyWheel.setPower(1); // 1 is full power
//            ballIntake.setPower(-1);
            rumbleBlip(gamepad1, 1);
            rumbleBlip(gamepad2, 2);
        }

        public static void loop(Gamepad gamepad1, Gamepad gamepad2) {
            // Arm Loop

            // Controller1 Buttons
            // Speed Control
            speedControl(gamepad1, gamepad2);

            if (gamepad1.aWasPressed()) {
                    loader.setPosition(0.15);
                    runTime.reset();
            }
            if (runTime.milliseconds() > 250){
                loader.setPosition(0.325);
            }
//                if (loader.getPosition() <= 0.1) {
//                    loader.setPosition(0.325);
//                }

            if (gamepad1.xWasPressed()) {
                if (flyWheel.getPower() == .1) {
                    flyWheel.setPower(0);
                } else {
                    flyWheel.setPower(.1);
                }
            }

            if (gamepad1.yWasPressed()) {
                if (ballIntake.getPower() == -1) {
                    ballIntake.setPower(0);
                    ballIntakeRear.setPower(0);
                    if (loader.getPosition() != .325)
                        loader.setPosition(.325);
                } else {
                    ballIntake.setPower(-1);
                    ballIntakeRear.setPower(-1);
                    loader.setPosition(.325);
                }
            }

            //Telemetry
            drive_telemetry(telemetry);
            Launcher.Mentorbot.launcher_telemetry(telemetry);

            telemetry.addData("Start", start);
//            telemetry.addData("goA", goA);
//            telemetry.addData("goX", goX);
//            telemetry.addData("goY", goY);
//            telemetry.addData("loader", loader);
//            telemetry.addData("flyWheel", flyWheel);
//            telemetry.addData("ballIntake", ballIntake);
            telemetry.update();
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

            //Telemetry
            drive_telemetry(telemetry);

            telemetry.addData("Start", start);
            telemetry.update();

//            if(start) {
//                if (slideBack.isPressed()){
//                    slide_reset(false);
//                    Arm.MentorBot.arm_reset();
//                    start = false;
//                } else {
//                    slide_reset(true);
//                }
//                return;
//            }

            // Controller2 Buttons
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

            /* ------ SLIDE LOGIC ------ */
//            slideTargetPosition += (-gamepad2.left_stick_y * 55);

            /* ------ RESET ------ */
//            if (gamepad2.backWasPressed()) {
//                goY = false;
//                goX = false;
//                goA = false;
//                wrist1.setPosition(0.05);
//                wrist2.setPosition(0.95);
//                slideTargetPosition = 0;
//                goHome = true;
//                rumble(gamepad2, 500, 0.6, 0.6);
//            }

            /* ------WRIST LOGIC------ */
//            if (gamepad2.left_stick_x > 0.01) {
//                wrist1.setPosition(wrist1.getPosition() + .012);
//                wrist2.setPosition(wrist2.getPosition() - .012);
//            } else if (gamepad2.left_stick_x < -0.01) {
//                wrist1.setPosition(wrist1.getPosition() - .012);
//                wrist2.setPosition(wrist2.getPosition() + .012);
//            }

            /* ------ MAIN BOOM LOGIC ------ */
//            if (gamepad2.right_stick_y > 0.01 || gamepad2.right_stick_y < -0.01){
//                armTargetPosition += (int) (22.5 * gamepad2.right_stick_y);
//            }

//            if (gamepad2.guideWasPressed()) {
//                Arm.MentorBot.armOverride = true;
//            } else if (gamepad2.guideWasReleased()) {
//                armTargetPosition = armPosition;
//                Arm.MentorBot.arm_reset();
//                Arm.MentorBot.armOverride = false;
//            }

            /* ------ INTAKE LOGIC ------ */
//            if (gamepad2.right_stick_x > 0.5){
//                intake.setPower(-1);
//                intakeOther.setPower(1);
//            } else if (gamepad2.right_stick_x < -0.5){
//                intake.setPower(1);
//                intakeOther.setPower(-1);
//            } else if (gamepad2.dpad_right){
//                intake.setPower(-1);
//                intakeOther.setPower(-1);
//            } else if (gamepad2.dpad_left){
//                intake.setPower(1);
//                intakeOther.setPower(1);
//            } else {
//                intake.setPower(0);
//                intakeOther.setPower(0);
//            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.b) {
                start = false;
//                armTargetPosition = armPosition;
//                slideTargetPosition = slidePosition;
                rumble(gamepad2, 500, 1, 1);
            }

            if (controllerChange(gamepad1, gamepad2) && gamepad2.a) {
//                armTargetPosition = 1100;
//                wrist1.setPosition(0.3);
//                wrist2.setPosition(0.7);
                rumbleBlip(gamepad2, 1);
            }

            /* ------ PRESETS ------ */
//            if (gamepad2.xWasPressed()) {
//                goX = true;
//                goY = false;
//                goA = false;
//                goHome = false;
//                slideTargetPosition = 0;
//                wrist1.setPosition(0.6);
//                wrist2.setPosition(0.4);
//                rumbleBlip(gamepad2, 1);
//            }

//            if (gamepad2.yWasPressed()) {
//                goY = true;
//                goX = false;
//                goHome = false;
//                goA = false;
//                slideTargetPosition = 0;
//                wrist1.setPosition(0.1);
//                wrist2.setPosition(0.9);
//                rumbleBlip(gamepad2, 1);
//            }
//
//            if (goHome && (slidePosition <= 2000)) {
//                slideTargetPosition = 0;
//                armTargetPosition = 0;
//            }
//
//            if (goHome && (armPosition <= armTolerance)) {
//                goHome = false;
//                rumble(gamepad2, 500, 0.8, 0.8);
//            }
//
//            //Go X
//            if (goX && (slidePosition <= 2000)) {
//                armTargetPosition = 200;
//                if (armPosition <= 200) {
//                    slideTargetPosition = 1350;
//                    wrist1.setPosition(0.82);
//                    wrist2.setPosition(0.18);
//                    goX = false;
//                }
//            }
//
//            //Go A
//            if (goA && (armPosition >= 900)) {
//                slideTargetPosition = slideLimit;
//                wrist1.setPosition(0.75);
//                wrist2.setPosition(0.25);
//                goA = false;
//            }
//
//            //Go Y
//            if (goY && (slidePosition <= 400)) {
//                armTargetPosition = 250;
//                goY = false;
//            }
//
//            //Slide Power
//            slide();

            //Arm Power
//            Arm.MentorBot.arm();

            if (gamepad1.touchpadWasPressed()) {
                rumbleWiiii(gamepad1, 5000);
            }

            if (gamepad2.touchpadWasPressed()) {
                rumbleWiiii(gamepad2, 5000);
            }
        }
    }
}
