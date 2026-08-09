package com.axelr.tradingposteasyvillagerscompat.mixin;

import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import fuzs.tradingpost.world.level.block.TradingPostBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Mixin(value = TradingPostBlock.class, remap = false)
public abstract class TradingPostBlockMixin {
    @Redirect(
            method = "useWithoutItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            ),
            remap = false
    )
    private List<Entity> tradingpostEasyVillagersCompat$includeTraderBlocks(
            Level level,
            Class<? extends Entity> entityClass,
            AABB bounds,
            Predicate<? super Entity> predicate
    ) {
        List<Entity> merchants = new ArrayList<>(level.getEntitiesOfClass(entityClass, bounds, predicate));

        int minX = (int) Math.floor(bounds.minX);
        int minY = (int) Math.floor(bounds.minY);
        int minZ = (int) Math.floor(bounds.minZ);
        int maxX = (int) Math.floor(bounds.maxX);
        int maxY = (int) Math.floor(bounds.maxY);
        int maxZ = (int) Math.floor(bounds.maxZ);

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                cursor.set(x, minY, z);
                if (!level.hasChunkAt(cursor)) {
                    continue;
                }

                for (int y = minY; y <= maxY; y++) {
                    cursor.set(x, y, z);
                    if (!(level.getBlockEntity(cursor) instanceof TraderTileentityBase trader)) {
                        continue;
                    }

                    EasyVillagerEntity villager = trader.getVillagerEntity();
                    if (villager == null) {
                        continue;
                    }

                    // Trading Post uses an entity's position for range checks while its menu is open.
                    villager.setPos(x + 0.5D, y + 0.5D, z + 0.5D);
                    if (predicate.test(villager)) {
                        merchants.add(villager);
                    }
                }
            }
        }

        return merchants;
    }
}
