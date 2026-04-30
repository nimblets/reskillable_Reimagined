package net.bandit.reskillable.mixin;

import net.bandit.reskillable.common.gating.FarmerDelightSkillGate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents Farmer's Delight cooking pot recipes from completing when the produced
 * item is locked behind skills.
 */
@Mixin(targets = "vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity")
public class CookingPotBlockEntityMixin {

    @Inject(method = "processCooking", at = @At("HEAD"), cancellable = true)
    private void onProcessCooking(RecipeHolder<?> recipeHolder, @Coerce Object cookingPot, CallbackInfoReturnable<Boolean> cir) {
        if (!(cookingPot instanceof BlockEntity blockEntity)) {
            return;
        }

        Level level = blockEntity.getLevel();
        if (level == null || level.isClientSide) {
            return;
        }

        try {
            ItemStack resultStack = resolveCookingPotResult(recipeHolder, level.registryAccess());
            if (!resultStack.isEmpty() && !FarmerDelightSkillGate.canAnyNearbyPlayerCook(level, blockEntity.getBlockPos(), resultStack)) {
                cir.setReturnValue(false);
            }
        } catch (Exception ignored) {
            // If the gate cannot resolve the recipe safely, leave vanilla behavior alone.
        }
    }

    private static ItemStack resolveCookingPotResult(RecipeHolder<?> recipeHolder, HolderLookup.Provider registryAccess) throws ReflectiveOperationException {
        Object recipe = recipeHolder.value();
        return (ItemStack) recipe.getClass().getMethod("getResultItem", HolderLookup.Provider.class).invoke(recipe, registryAccess);
    }
}
