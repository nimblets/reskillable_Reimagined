package net.bandit.reskillable.mixin;

import net.bandit.reskillable.common.gating.FarmerDelightSkillGate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Prevents Farmer's Delight skillet recipes from finishing when the cooked
 * result is locked behind skills.
 */
@Mixin(targets = "vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity")
public class SkilletBlockEntityMixin {

    @Inject(method = "cookAndOutputItems", at = @At("HEAD"), cancellable = true)
    private void onCookAndOutputItems(ItemStack cookingStack, Level level, CallbackInfo ci) {
        if (level == null || level.isClientSide || cookingStack.isEmpty()) {
            return;
        }

        Optional<RecipeHolder<CampfireCookingRecipe>> recipe = level.getRecipeManager().getRecipeFor(
                RecipeType.CAMPFIRE_COOKING,
                new SingleRecipeInput(cookingStack),
                level);

        if (recipe.isPresent()) {
            ItemStack resultStack = recipe.get().value().assemble(new SingleRecipeInput(cookingStack), level.registryAccess());
            BlockPos pos = ((BlockEntity) (Object) this).getBlockPos();
            if (!resultStack.isEmpty() && !FarmerDelightSkillGate.canAnyNearbyPlayerCook(level, pos, resultStack)) {
                ci.cancel();
            }
        }
    }
}