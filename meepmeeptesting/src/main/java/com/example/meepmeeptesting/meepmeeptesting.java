package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class meepmeeptesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(61, 15, (-1.57*2)))
                        .forward(50)
                        .turn(Math.toRadians(-90))
                        .forward(100)
                        .turn(Math.toRadians(-90))
                        .forward(100)
                        .turn(Math.toRadians(-90))
                        .forward(100)
                        .turn(Math.toRadians(-90))
                        .forward(50)
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