package com.almasb.zeph

/**
 * Holds various constants.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
object Config {

    val tileSize = 64
    val mapWidth = 20
    val mapHeight = 20

    // CHARACTER VALUES

    /**
     * When regeneration happens, in seconds.
     */
    val REGEN_INTERVAL = 2.0

    /**
     * How slow can a character attack, i.e. 1 attack in **n** seconds.
     */
    val SLOWEST_ATTACK_INTERVAL = 3.0

    val MAX_LEVEL_BASE = 100
    val MAX_LEVEL_STAT = 100
    val MAX_LEVEL_JOB = 60
    val MAX_LEVEL_SKILL = 10
    val MAX_ATTRIBUTE = 100
    val ATTRIBUTE_POINTS_PER_LEVEL = 3

    /**
     * By what value should experience needed for next level
     * increase per level
     */
    val EXP_NEEDED_INC_BASE = 1.75f;
    val EXP_NEEDED_INC_STAT = 1.5f;
    val EXP_NEEDED_INC_JOB  = 2.25f;

    val EXP_NEEDED_FOR_LEVEL2 = 10;

    // GAMEPLAY

    val STARTING_MONEY = 100

    val MAX_INVENTORY_SIZE = 30
}