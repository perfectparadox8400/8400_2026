package com.perfectparadox.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(40, 40, Math.PI, Math.PI, 12.64)
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
                .strafeToConstantHeading(new Vector2d(58.5,40))
                .turnTo(Math.toRadians(270))

                .lineToY(35)

                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45))

                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(58.5,40))
                .turnTo(Math.toRadians(90))
                .lineToY(12)
                .strafeToConstantHeading(new Vector2d(62.5,12))
                .turnTo(Math.toRadians(90))
                .lineToY(50)
                .lineToY(12)
                .strafeToConstantHeading(new Vector2d(48,12))
                .turnTo(Math.toRadians(75))
                .lineToY(55)
                .lineToY(45)
                .build()); //Put Code above this

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

/*
                .lineToY(52)
                .turnTo(Math.toRadians(0))
                .lineToX(52.5)
                .turnTo(Math.toRadians(45))
                .lineToX(54)
                .waitSeconds(1)
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(58.5,40))
                .turnTo(Math.toRadians(270))
                .lineToY(35)
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45))
                .waitSeconds(1)
                .turnTo(Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(48.5,40))
                .turnTo(Math.toRadians(270))
                .lineToY(35)
                .strafeToConstantHeading(new Vector2d(54,52.5))
                .turnTo(Math.toRadians(45))
                .waitSeconds(1)
                .turnTo(Math.toRadians(270))
                .lineToY(10)
                .turnTo(Math.toRadians(180))
                .lineToX(25)
 */