package net.bandit.reskillable.mixin;

import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ReskillableMixinConfigPlugin implements IMixinConfigPlugin {

    private static final String FARMERS_DELIGHT_MOD_ID = "farmersdelight";
    private static final String[] FARMERS_DELIGHT_MIXINS = {
            "net.bandit.reskillable.mixin.CookingPotBlockEntityMixin",
            "net.bandit.reskillable.mixin.SkilletBlockEntityMixin",
            "net.bandit.reskillable.mixin.StoveBlockEntityMixin"
    };

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (String farmerDelightMixin : FARMERS_DELIGHT_MIXINS) {
            if (mixinClassName.equals(farmerDelightMixin)) {
                return FMLLoader.getLoadingModList().getModFileById(FARMERS_DELIGHT_MOD_ID) != null;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
