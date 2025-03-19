package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.main.Game;

/**
 * Contains constant values used throughout the game.
 */
public class Constants {

    public static final double GRAVITY = 0.04d * Game.SCALE;
    public static final int ANIMATION_SPEED = 23;

    public static class ObjectConstants {
        public static final int BARREL = 0;
        public static final int BOX = 1;
        public static final int HEALTH_POTION = 2;
        public static final int SPIKE = 3;
        public static final int STAR = 4;

        public static final int HEALTH_POTION_VALUE = 15;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int STAR_WIDTH_DEFAULT = 16;
        public static final int STAR_HEIGHT_DEFAULT = 16;
        public static final int STAR_WIDTH = (int) (Game.SCALE * STAR_WIDTH_DEFAULT);
        public static final int STAR_HEIGHT = (int) (Game.SCALE * STAR_HEIGHT_DEFAULT);

        public static int getSpriteAmount(int object_type) {
            return switch (object_type) {
                case HEALTH_POTION, STAR-> 7;
                case BARREL, BOX -> 8;
                default -> 1;
            };
        }
    }

    public static class UI {
        public static class Buttons {
            public static final int B_WIDTH_DEFAULT = 80;
            public static final int B_HEIGHT_DEFAULT = 32;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_SIZE_DEFAULT = 32;
            public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_SIZE_DEFAULT = 32;
            public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_WIDTH_DEFAULT = 20;
            public static final int VOLUME_HEIGHT_DEFAULT = 32;
            public static final int SLIDER_WIDTH_DEFAULT = 156;
            public static final int VOLUME_WIDTH = (int) (VOLUME_WIDTH_DEFAULT * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_HEIGHT_DEFAULT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_WIDTH_DEFAULT * Game.SCALE);
        }
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int JUMP = 2;
        public static final int DOUBLEJUMP = 3;
        public static final int DEATH = 5;
        public static final int ATTACK1 = 7;
        public static final int FALL = 8;

        public static int GetSpriteAmount(int player_action) {
            return switch (player_action) {
                case IDLE, JUMP, FALL -> 4;
                case RUN, DOUBLEJUMP, DEATH, ATTACK1 -> 6;
                default -> 1;
            };
        }
    }

    public static class EnemyConstants {
        public static final int DOG = 0;
        public static final int CAT = 1;

        public static final int IDLE = 0;
        public static final int DIE = 1;
        public static final int HURT = 2;
        public static final int ATTACK = 3;
        public static final int RUN = 4;

        public static final int ENEMY_DEFAULT_WIDTH = 48;
        public static final int ENEMY_DEFAULT_HEIGHT = 48;
        public static final int ENEMY_WIDTH = (int) (ENEMY_DEFAULT_WIDTH * Game.SCALE);
        public static final int ENEMY_HEIGHT = (int) (ENEMY_DEFAULT_HEIGHT * Game.SCALE);

        public static final int DOG_DRAW_OFFSET_X = (int) (4 * Game.SCALE);
        public static final int DOG_DRAW_OFFSET_Y = (int) (17 * Game.SCALE);
        public static final int CAT_DRAW_OFFSET_X = (int) (8 * Game.SCALE);
        public static final int CAT_DRAW_OFFSET_Y = (int) (29 * Game.SCALE);

        public static int getSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case DOG, CAT -> {
                    switch (enemyState) {
                        case RUN -> {
                            return 6;
                        }
                        case HURT -> {
                            return 2;
                        }
                        case IDLE, DIE, ATTACK -> {
                            return 4;
                        }
                    }
                }
            }
            return 0;
        }

        public static int getMaxEnemyHealth(int enemyType) {
            return switch (enemyType) {
                case DOG -> 20;
                case CAT -> 10;
                default -> 1;
            };
        }

        public static int getEnemyDamage(int enemyType) {
            return switch (enemyType) {
                case DOG -> 10;
                case CAT -> 5;
                default -> 1;
            };
        }
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int RIGHT = 2;
    }
}
