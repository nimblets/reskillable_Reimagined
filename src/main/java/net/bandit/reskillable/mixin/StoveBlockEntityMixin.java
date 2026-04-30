package net.bandit.reskillable.mixin;

import net.bandit.reskillable.common.gating.FarmerDelightSkillGate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Prevents Farmer's Delight stove recipes from completing when the cooked
 * result is locked behind skills.
 */
@Mixin(targets = "vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity")
public abstract class StoveBlockEntityMixin {

    @Shadow
    public abstract ItemStackHandler getItems();

    @Shadow
    public abstract Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> getCookingRecipe(ItemStack itemStack);

    @Inject(method = "cookAndOutputItems", at = @At("HEAD"), cancellable = true)
    private void onCookAndOutputItems(CallbackInfo ci) {
        BlockEntity blockEntity = (BlockEntity) (Object) this;
        Level level = blockEntity.getLevel();
        if (level == null || level.isClientSide) {
            return;
        }

        BlockPos pos = blockEntity.getBlockPos();
        for (int slot = 0; slot < getItems().getSlots(); slot++) {
            ItemStack ingredient = getItems().getStackInSlot(slot);
            if (ingredient.isEmpty()) {
                continue;
            }

            Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> recipe = getCookingRecipe(ingredient);
            if (recipe.isEmpty()) {
                continue;
            }

            ItemStack resultStack = recipe.get().value().assemble(new SingleRecipeInput(ingredient), level.registryAccess());
            if (!resultStack.isEmpty() && !FarmerDelightSkillGate.canAnyNearbyPlayerCook(level, pos, resultStack)) {
                ci.cancel();
                return;
            }
        }
    }
}