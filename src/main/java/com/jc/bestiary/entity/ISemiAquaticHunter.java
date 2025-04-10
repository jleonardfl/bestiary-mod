package com.jc.bestiary.entity;

public interface ISemiAquaticHunter {
    boolean getHunting();
    void setHunting(boolean isHunting);

    boolean targetIsInWater();
    boolean shouldHunt();
}
