package net.bandit.reskillable.common.gating;

import net.bandit.reskillable.common.capabilities.SkillModel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

public final class FarmerDelightSkillGate {

    private static final double SEARCH_RADIUS = 32.0D;

    private FarmerDelightSkillGate() {
    }

    public static boolean canAnyNearbyPlayerCook(net.minecraft.world.level.Level level, BlockPos pos, ItemStack resultStack) {
        if (!(level instanceof ServerLevel serverLevel) || resultStack.isEmpty()) {
            return true;
        }

        AABB searchArea = new AABB(pos).inflate(SEARCH_RADIUS);
        List<ServerPlayer> nearbyPlayers = serverLevel.getEntitiesOfClass(ServerPlayer.class, searchArea);
        if (nearbyPlayers.isEmpty()) {
            return false;
        }

        for (ServerPlayer player : nearbyPlayers) {
            if (SkillModel.get(player).canCraftItem(player, resultStack)) {
                return true;
            }
        }

        return false;
    }
}