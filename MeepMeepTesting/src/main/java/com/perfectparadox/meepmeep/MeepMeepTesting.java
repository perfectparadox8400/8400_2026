package com.perfectparadox.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(700);

        Pose2d start = new Pose2d(31, 63, Math.toRadians(270));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 50, Math.PI, Math.PI, 12)
                .setDimensions(15, 18)
                .setColorScheme(PPColor.perfectParadox)
                .setStartPose(start)
                .build();

        //Center of bot is w = 7.5 & h = = 9
        myBot.runAction(myBot.getDrive().actionBuilder(start) //Put Code below this
                .lineToY(50)
                .turnTo(Math.toRadians(0))
                .lineToX(50)
                .turnTo(Math.toRadians(45))
                .lineToX(53)
                .build()); //Put Code above this

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}