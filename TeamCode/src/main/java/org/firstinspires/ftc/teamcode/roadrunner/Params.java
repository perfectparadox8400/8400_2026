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

        public double inPerTick;
        public double lateralInPerTick;
        public double trackWidthTicks;

        public double kS;
        public double kV;
        public double kA;

        public double maxWheelVel;
        public double minProfileAccel;
        public double maxProfileAccel;

        public double maxAngVel;
        public double maxAngAccel;

        public double axialGain;
        public double lateralGain;
        public double headingGain;

        public double axialVelGain;
        public double lateralVelGain;
        public double headingVelGain;
        public double par0YTicks;
        public double par1YTicks;
        public double perpXTicks;
    }

    public static class NancyParams extends RobotParams {
        public NancyParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

            // drive model parameters
            inPerTick = 0.0019876368984914;
            lateralInPerTick = 0.0009019917292751737;
            trackWidthTicks = 6068.751343230888;

            // feedforward parameters (in tick units)
            kS = 1.2413263606256049;
            kV = 0.00025279672456487;
            kA = 0.00001;

            // path profile parameters (in inches)
            maxWheelVel = 100;
            minProfileAccel = -30;
            maxProfileAccel = 80;

            // turn profile parameters (in radians)
            maxAngVel = Math.PI;
            maxAngAccel = Math.PI;

            // path controller gains
            axialGain = 4.5;
            lateralGain = 8.0;
            headingGain = 15.0; // shared with turn

            axialVelGain = 0.0;
            lateralVelGain = 0.0;
            headingVelGain = 0.0; // shared with turn

            // Three Dead Wheels
            par0YTicks = 3202.635760339295; // y position of the first parallel encoder (in tick units)
            par1YTicks = -3245.0016237683703; // y position of the second parallel encoder (in tick units)
            perpXTicks = -1937.055067651397; // x position of the perpendicular encoder (in tick units)
        }
    }

    public static class OpheliaParams extends RobotParams {
        public OpheliaParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

            // drive model parameters
            inPerTick = 0.0;
            lateralInPerTick = 0.0;
            trackWidthTicks = 0.0;

            // feedforward parameters (in tick units)
            kS = 0.0;
            kV = 0.0;
            kA = 0.0;

            // path profile parameters (in inches)
            maxWheelVel = 0;
            minProfileAccel = 0;
            maxProfileAccel = 0;

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
            par1YTicks = 0.0; // y position of the second parallel encoder (in tick units)
            perpXTicks = 0.0; // x position of the perpendicular encoder (in tick units)
        }
    }

    public static class MentorBotParams extends RobotParams {
        public MentorBotParams() {
            // IMU orientation
            logoFacingDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
            usbFacingDirection = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;

            // drive model parameters
            inPerTick = 0.0019876368984914;
            lateralInPerTick = 0.0009019917292751737;
            trackWidthTicks = 6068.751343230888;

            // feedforward parameters (in tick units)
            kS = 1.2413263606256049;
            kV = 0.00025279672456487;
            kA = 0.00001;

            // path profile parameters (in inches)
            maxWheelVel = 100;
            minProfileAccel = -30;
            maxProfileAccel = 80;

            // turn profile parameters (in radians)
            maxAngVel = Math.PI;
            maxAngAccel = Math.PI;

            // path controller gains
            axialGain = 4.5;
            lateralGain = 8.0;
            headingGain = 15.0; // shared with turn

            axialVelGain = 0.0;
            lateralVelGain = 0.0;
            headingVelGain = 0.0; // shared with turn

            // Three Dead Wheels
            par0YTicks = 3202.635760339295; // y position of the first parallel encoder (in tick units)
            par1YTicks = -3245.0016237683703; // y position of the second parallel encoder (in tick units)
            perpXTicks = -1937.055067651397; // x position of the perpendicular encoder (in tick units)
        }
    }
}
