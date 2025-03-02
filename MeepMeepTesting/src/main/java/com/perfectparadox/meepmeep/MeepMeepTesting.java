package com.perfectparadox.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Dimension;
import java.awt.Toolkit;

public class MeepMeepTesting {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        MeepMeep meepMeep = new MeepMeep((int) screenSize.getHeight() - 200);

        //Start Pose
        Pose2d start = new Pose2d(-16, 63, Math.toRadians(270));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(100, 80, Math.PI, Math.PI, 12.64)
                .setDimensions(16, 18)
                .setColorScheme(PPColor.perfectParadox)
                .setStartPose(start)
                .build();

        //Center of bot is w = 7.5 & h = = 9
        myBot.runAction(myBot.getDrive().actionBuilder(start) //Put Code below this
                .lineToY(52)

                .turnTo(Math.toRadians(0))
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54)

                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5,45))
                .turnTo(Math.toRadians(270))
                .lineToY(44)

                .lineToY(36)

                .strafeToConstantHeading(new Vector2d(52.5,52))
                .turnTo(Math.toRadians(45))
                .lineToX(54)

                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(47,45))
                .turnTo(Math.toRadians(270))
                .lineToY(44)

                .lineToY(36)

                .strafeToConstantHeading(new Vector2d(52.5,52))
                .turnTo(Math.toRadians(45))
                .lineToX(54)

                .lineToX(50)
                .turnTo(Math.toRadians(290))

                .build()); //Put Code above this

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}