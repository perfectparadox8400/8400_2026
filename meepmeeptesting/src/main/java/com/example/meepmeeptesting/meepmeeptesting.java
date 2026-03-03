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
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-55.5,-45.5, 0.942478))


                .strafeToLinearHeading(new Vector2d(-30, -14.5), Math.toRadians(234))

                //SHOOT

                .strafeToLinearHeading(new Vector2d(-11.5, -30), Math.toRadians(270))

                .strafeToLinearHeading(new Vector2d(-11.5, -55), Math.toRadians(270))

                //PICKUP

                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-2, -56.5, Math.toRadians(180)), Math.toRadians(360))

                .strafeToLinearHeading(new Vector2d(-30, -14.5), Math.toRadians(234))

                //SHOOT

                .strafeToLinearHeading(new Vector2d(12.5, -30), Math.toRadians(270))

                .strafeToLinearHeading(new Vector2d(12.5, -55), Math.toRadians(270))

                //PICKUP

                .setReversed(true)

                .splineToLinearHeading(new Pose2d(-30, -14.5, Math.toRadians(234)), Math.toRadians(180))

                //SHOOT

                .strafeToLinearHeading(new Vector2d(-9, -30), Math.toRadians(0))



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