package org.firstinspires.ftc.teamcode.roadrunner;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.teamcode.Robot;

public class Params {
    public static final RobotParams CURRENT;

    static {
        switch (Robot.robotName()) {
            case "ophelia":
                CURRENT = new OpheliaParams();
                break;
            case "mentorbot":
                CURRENT = new MentorBotParams();
                break;
            case "nancy":
            default:
                CURRENT = new NancyParams();
                break;
        }
    }

    public static abstract class RobotParams {
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection;

        public double inPerTick = 1;//changed from 1

        public double lateralInPerTick = inPerTick;
        public double trackWidthTicks = 0.0;

        public double kS = 0.0;
        public double kV = 0.0;
        public double kA = 0.0;

        public double maxWheelVel = 50.0;
        public double minProfileAccel = -30.0;
        public double maxProfileAccel = 50.0;

        public double maxAngVel = Math.PI;
        public double maxAngAccel = Math.PI;

        public double axialGain = 0.0;
        public double lateralGain = 0.0;
        public double headingGain = 0.0;

        public double axialVelGain = 0.0;
        public double lateralVelGain = 0.0;
        public double headingVelGain = 0.0;
        public double par0YTicks = 0.0;
        public double par1YTicks = 1.0;
        public double perpXTicks = 0.0;
    }

    public static class NancyParams extends RobotParams {
        public NancyParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.DOWN;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

            // drive model parameters
            inPerTick = 0.00201061156101647584473610723262;
            lateralInPerTick = -0.0015513578094417196;
            trackWidthTicks = 6818.23603629532;

            // feedforward parameters (in tick units)
            kS = 1.203671153696226;
            kV = 0.0003112672491329851;
            kA = 0.000005;

            // path profile parameters (in inches)
            maxWheelVel = 100;
            minProfileAccel = -30;
            maxProfileAccel = 80;

            // turn profile parameters (in radians)
            maxAngVel = Math.PI;
            maxAngAccel = Math.PI;

            // path controller gains
            axialGain = 4.5;
            lateralGain = 5;
            headingGain = 3.7; // shared with turn

            axialVelGain = 0.0;
            lateralVelGain = 0.0;
            headingVelGain = 0.0; // shared with turn

            // Three Dead Wheels
            par0YTicks = 2801.9593888115314; // y position of the first parallel encoder (in tick units)
            par1YTicks = -3087.1335070570067; // y position of the second parallel encoder (in tick units)
            perpXTicks = 3127.2491537283527; // x position of the perpendicular encoder (in tick units)
        }
    }

    public static class OpheliaParams extends RobotParams {
        public OpheliaParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

            // drive model parameters
            inPerTick = 1;
            lateralInPerTick = inPerTick;
            trackWidthTicks = 0.0;

            // feedforward parameters (in tick units)
            kS = 0.0;
            kV = 0.0;
            kA = 0.0;

            // path profile parameters (in inches)
            maxWheelVel = 50;
            minProfileAccel = -30;
            maxProfileAccel = 50;

            // turn profile parameters (in radians)
            maxAngVel = Math.PI;
            maxAngAccel = Math.PI;

            // path controller gains
            axialGain = 0.0;
            lateralGain = 0.0;
            headingGain = 0.0; // shared with turn

            axialVelGain = 0.0;
            lateralVelGain = 0.0;
            headingVelGain = 0.0; // shared with turn

            // Three Dead Wheels
            par0YTicks = 0.0; // y position of the first parallel encoder (in tick units)
            par1YTicks = 1.0; // y position of the second parallel encoder (in tick units)
            perpXTicks = 0.0; // x position of the perpendicular encoder (in tick units)
        }
    }

    public static class MentorBotParams extends RobotParams {
        public MentorBotParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

            // drive model parameters
            inPerTick = 0.0019634792852935;
            lateralInPerTick = 0.0015725784625803187;
            trackWidthTicks = 6688.675532912596;

            // feedforward parameters (in tick units)
            kS = 0.8018814186196006;
            kV = 0.00038417994989199157;
            kA = 0.00001;

            // path profile parameters (in inches)
            maxWheelVel = 80;
            minProfileAccel = -30;
            maxProfileAccel = 80;

            // turn profile parameters (in radians)
            maxAngVel = Math.PI;
            maxAngAccel = Math.PI;

            // path controller gains
            axialGain = 4;
            lateralGain = 9;
            headingGain = 10; // shared with turn

            axialVelGain = 0.0;
            lateralVelGain = 0.0;
            headingVelGain = 0.0; // shared with turn

            // Three Dead Wheels
            par0YTicks = -3000.2831713283877; // y position of the first parallel encoder (in tick units)
            par1YTicks = 3046.458004663223; // y position of the second parallel encoder (in tick units)
            perpXTicks = 3458.801960386832; // x position of the perpendicular encoder (in tick units)
        }
    }
}
