package com.jc.bestiary.entity;

import com.jc.bestiary.registry.BestiaryEntities;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BrownBearEntity extends TamableAnimal implements GeoEntity, ISemiAquaticHunter {
    //protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.brown_bear.idle");
    //protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.brown_bear.walk");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private boolean isHunting = false;

    public BrownBearEntity(EntityType<? extends BrownBearEntity> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new BearMoveControl(this);
        //this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.SAFE_FALL_DISTANCE, 3D)
                .add(Attributes.STEP_HEIGHT, 1.5D);

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        var mainController = new AnimationController<>(this, "mainController", 0, this::animPredicate);
        controllers.add(mainController);
    }

    private <T extends GeoAnimatable> PlayState animPredicate(AnimationState<T> tAnimationState) {

//        if (this.swinging) {
//            return tAnimationState.setAndContinue(RawAnimation.begin().thenPlay("attack"));
//        }

        return tAnimationState.setAndContinue(tAnimationState.isMoving() ? RawAnimation.begin().thenPlay("animation.brown_bear.walk") : RawAnimation.begin().thenPlay("animation.brown_bear.idle"));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatIfNotHuntingGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, this::isFood, false));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(10, new RandomStrollGoal(this, 1.0, 30));

        this.targetSelector.addGoal(5, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(6, new HurtByTargetGoal(this, Creeper.class)); // put creeper here bc it wants a class, i think to ignore a damage type
        this.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>(this, Salmon.class, true, entity -> true /*entity instanceof Salmon*/));

    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FISHES) || stack.getItem() == Items.HONEYCOMB;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        BrownBearEntity offspring = BestiaryEntities.BROWN_BEAR.get().create(level);

        UUID uuid = this.getOwnerUUID();
        if (uuid != null) {
            offspring.setOwnerUUID(uuid);
            offspring.setTame(true, false);
        }

        return offspring;
    }

    public boolean getHunting() {
        return isHunting;
    }

    public void setHunting(boolean isHunting) {
        this.isHunting = isHunting;
    }

    public boolean targetIsInWater() {
        if (this.getTarget() != null) {
            LogUtils.getLogger().debug("Bear targeting {}", this.getTarget());
            return this.getTarget().isInWater();
        }
        return false;
    }

    // Technically, this method isn't safe!
    public boolean shouldHunt() {
        return getHunting() && targetIsInWater();
    }

    // this is the same as Drowned.DrownedMoveControl
    static class BearMoveControl extends MoveControl {
        private final BrownBearEntity bear;

        public BearMoveControl(BrownBearEntity bear) {
        super(bear);
        this.bear = bear;
    }

        @Override
        public void tick() {
        LivingEntity livingentity = this.bear.getTarget();
        if (this.bear.shouldHunt() && this.bear.isInWater()) {
            if (livingentity != null && livingentity.getY() > this.bear.getY()) {
                this.bear.setDeltaMovement(this.bear.getDeltaMovement().add(0.0, 0.002, 0.0));
            }

            if (this.operation != MoveControl.Operation.MOVE_TO || this.bear.getNavigation().isDone()) {
                this.bear.setSpeed(0.0F);
                return;
            }

            double d0 = this.wantedX - this.bear.getX();
            double d1 = this.wantedY - this.bear.getY();
            double d2 = this.wantedZ - this.bear.getZ();
            double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            d1 /= d3;
            float f = (float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F;
            this.bear.setYRot(this.rotlerp(this.bear.getYRot(), f, 90.0F));
            this.bear.yBodyRot = this.bear.getYRot();
            float f1 = (float)(this.speedModifier * this.bear.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float f2 = Mth.lerp(0.125F, this.bear.getSpeed(), f1);
            this.bear.setSpeed(f2);
            this.bear.setDeltaMovement(this.bear.getDeltaMovement().add((double)f2 * d0 * 0.005, (double)f2 * d1 * 0.1, (double)f2 * d2 * 0.005));
        } else {
            if (!this.bear.onGround()) {
                this.bear.setDeltaMovement(this.bear.getDeltaMovement().add(0.0, -0.008, 0.0));
            }

            super.tick();
        }
    }
    }

    public static class FloatIfNotHuntingGoal extends FloatGoal {
        Mob imob;
        public FloatIfNotHuntingGoal(Mob mob) {
            super(mob);
            this.imob = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && ((this.imob.getAirSupply() < (this.imob.getMaxAirSupply() * 0.20)) || this.imob.getTarget() == null);
        }

    }

}
