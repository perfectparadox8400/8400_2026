package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class meepmeeptesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        // Path matching bShortTest.java
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(61.5, -14.5, Math.toRadians(180)))
                // 1. Initial drive to shooting position
                .strafeToLinearHeading(new Vector2d(55, -14.5), Math.toRadians(198))
                .strafeToLinearHeading(new Vector2d(58, -60), Math.toRadians(270))
                .waitSeconds(1.5) // SHOOT 1 (Simulated)

                .build());

        Image img = null;
        try {
            img = ImageIO.read(new File("TeamCode/src/main/res/drawable/field.png"));
        } catch(IOException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
        if (img != null) {
            meepMeep.setBackground(img);
        } else {
            meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK);
        }
        
        meepMeep.setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
