package com.perfectparadox.meepmeep;

import com.noahbres.meepmeep.core.colorscheme.ColorScheme;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class PPColor {
    public static Color BOT_BODY_COLOR = new Color(128, 0, 128);        // Purple
    public static Color BOT_WHEEL_COLOR = new Color(64, 0, 64);         // Dark Purple
    public static Color BOT_DIRECTION_COLOR = new Color(0, 0, 0);
    public static Color AXIS_COLOR = new Color(255, 0, 0);             // Red
    public static ColorScheme perfectParadox = new ColorScheme() {
        @NotNull
        @Override
        public Color getUI_MAIN_BG() {
            return new Color(30, 30, 30); // Dark Gray for UI background
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_TEXT_COLOR() {
            return new Color(255, 255, 255); // White
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_SLIDER_FG() {
            return new Color(121, 5, 121); // Blue
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_SLIDER_BG() {
            return new Color(100, 100, 100); // Gray
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_MARKER_COLOR() {
            return new Color(255, 165, 0); // Orange
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_TURN_COLOR() {
            return new Color(0, 255, 255); // Cyan
        }

        @NotNull
        @Override
        public Color getTRAJECTORY_PATH_COLOR() {
            return new Color(0, 255, 0);
        }

        @Override
        public double getAXIS_HOVER_OPACITY() {
            return 0.5; // 50% transparency when hovering
        }

        @Override
        public double getAXIS_NORMAL_OPACITY() {
            return 1.0; // Fully opaque when not hovering
        }

        @NotNull
        @Override
        public Color getAXIS_Y_COLOR() {
            return AXIS_COLOR;
        }

        @NotNull
        @Override
        public Color getAXIS_X_COLOR() {
            return AXIS_COLOR;
        }

        @NotNull
        @Override
        public Color getBOT_DIRECTION_COLOR() {
            return BOT_DIRECTION_COLOR;
        }

        @NotNull
        @Override
        public Color getBOT_WHEEL_COLOR() {
            return BOT_WHEEL_COLOR;
        }

        @NotNull
        @Override
        public Color getBOT_BODY_COLOR() {
            return BOT_BODY_COLOR;
        }

        @Override
        public boolean isDark() {
            return true; // Dark theme
        }
    };
}
