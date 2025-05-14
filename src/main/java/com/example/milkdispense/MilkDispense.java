package com.example.milkdispense;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.common.Mod;

import java.util.List;

@Mod("milkdispense")
public class MilkDispense {

    public static final String MODID = "milkdispense";

    public MilkDispense() {
        DispenseItemBehavior defaultBucketBehavior = DispenserBlock.DISPENSER_REGISTRY.get(Items.BUCKET);
        DefaultDispenseItemBehavior milkDropBehavior = new DefaultDispenseItemBehavior();

        DispenserBlock.registerBehavior(Items.BUCKET, (BlockSource source, ItemStack stack) -> {
            Level level = source.level();
            BlockPos pos = source.pos();
            Direction direction = source.state().getValue(DispenserBlock.FACING);
            BlockPos targetPos = pos.relative(direction);

            List<Cow> cows = level.getEntitiesOfClass(Cow.class, new AABB(targetPos));

            if (!cows.isEmpty()) {
                level.playSound(null, targetPos, SoundEvents.COW_MILK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, targetPos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F);

                ItemStack milkBucket = new ItemStack(Items.MILK_BUCKET);
                milkDropBehavior.dispense(source, milkBucket);

                stack.shrink(1);
                return stack;
            }

            return defaultBucketBehavior.dispense(source, stack);
        });
    }
}