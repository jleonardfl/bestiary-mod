package com.jc.bestiary.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class ShadeGoal extends Goal {
    private final Level ilevel;
    private final PathfinderMob imob;
    private final double ispeed;

    public ShadeGoal(PathfinderMob mob, double speedModifier) {
        super();

        this.ilevel = mob.level();
        this.imob = mob;
        this.ispeed = speedModifier;
    }

    @Override
    public boolean canUse() {
        boolean result = false;

        if(ilevel.isDay() && ilevel.canSeeSky(imob.blockPosition())){
            // find somewhere to run
            result = findShelter();
        }

        return result;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.imob.getNavigation().isDone();
    }

    @Override
    public void start() {
        // ?
    }

    public boolean findShelter() {
        boolean is_shaded_and_reachable = false;
        for (int i = 0; i < 8; i++) {
            // Pick a block within a 16x6x16 space (centered on the mob?)
            BlockPos candidate = new BlockPos(
                    imob.getBlockX() + imob.getRandom().nextInt(16) - 8,
                    imob.getBlockY() + imob.getRandom().nextInt(6) - 3,
                    imob.getBlockZ() + imob.getRandom().nextInt(16) - 8);

            // Is the block in shade?
            boolean is_shaded = (!ilevel.canSeeSky(new BlockPos(candidate)));
            boolean is_reachable =  imob.getNavigation().createPath(candidate, 0) != null;

            if (is_shaded && is_reachable) {
                // Move there
                this.imob.getNavigation().moveTo(
                        candidate.getX(),
                        candidate.getY(),
                        candidate.getZ(),
                        this.ispeed);
            }
        }

        return is_shaded_and_reachable;
    }

}
