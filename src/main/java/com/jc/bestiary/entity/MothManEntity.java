package com.jc.bestiary.entity;

import com.jc.bestiary.entity.ai.goal.ShadeGoal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MothManEntity extends PathfinderMob implements GeoEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.mothman.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.mothman.walk");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public MothManEntity(EntityType<? extends MothManEntity> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new MoveControl(this);
        //this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D)
                .add(Attributes.GRAVITY, 0.5D)
                .add(Attributes.SAFE_FALL_DISTANCE, 16D)
                .add(Attributes.STEP_HEIGHT, 1.5D)
                .add(Attributes.JUMP_STRENGTH, 2.0D)
                .add(Attributes.SCALE, 0.5D);
    }

//    @Override
//    public void aiStep(){
//        super.aiStep();
//        if(this.level().isClientSide){
//            // DebugUtils.renderPath(this,) can render ai pathfinding with debugutils mod, but I'd need to add it to gradle
//        }
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, state -> state.setAndContinue(IDLE_ANIM)));
        controllers.add(new AnimationController<>(this, "walk", 5, state -> state.setAndContinue(WALK_ANIM)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ShadeGoal(this, 1.5D));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.0D, 0.5));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 20.0F));
        //this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5D, 20));

        //this.goalSelector.addGoal(6, new WaterAvoidingRandomFlyingGoal(this, 0.5D));
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }
}
