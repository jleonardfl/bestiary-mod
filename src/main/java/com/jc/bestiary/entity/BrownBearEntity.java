package com.jc.bestiary.entity;

import com.jc.bestiary.Bestiary;
import com.jc.bestiary.registry.BestiaryEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class BrownBearEntity extends TamableAnimal implements GeoEntity {
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.brown_bear.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.brown_bear.walk");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public BrownBearEntity(EntityType<? extends BrownBearEntity> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new MoveControl(this);
        //this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.GRAVITY, 1.0D)
                .add(Attributes.SAFE_FALL_DISTANCE, 3D)
                .add(Attributes.STEP_HEIGHT, 1.5D);
    }

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
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, this::isFood, false));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.5F));
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
}
