package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;

//import org.rowlandhall.meepmeep.MeepMeep;
//import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
//import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class meepmeeptesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
//Pose2d start = new Pose2d(-49,-49, 0.942478);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(61.5,14.5, 3.142))

                .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                .strafeToLinearHeading(new Vector2d(61.5, 62.5), Math.toRadians(90))

                // pickup

                .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                .waitSeconds(1)
                //shoot

                .strafeToLinearHeading(new Vector2d(35, 20.5), Math.toRadians(90))

                .strafeToLinearHeading(new Vector2d(35, 62.5), Math.toRadians(90))

                // pickup

                .strafeToLinearHeading(new Vector2d(55, 14.5), Math.toRadians(159))

                .waitSeconds(1)
                //shoot

                .lineToX(35)

                .build());

        Image img = null;
        try {
            img = ImageIO.read(new File("TeamCode/src/main/res/drawable/field.png"));
        } catch(IOException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
        if (img != null) {
            meepMeep.setBackground(img);
        }
        meepMeep
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}