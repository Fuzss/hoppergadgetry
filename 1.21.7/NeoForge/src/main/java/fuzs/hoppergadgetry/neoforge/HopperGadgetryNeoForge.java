package fuzs.hoppergadgetry.neoforge;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.data.ModBlockLootProvider;
import fuzs.hoppergadgetry.data.ModBlockTagProvider;
import fuzs.hoppergadgetry.data.ModRecipeProvider;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.items.VanillaHopperItemHandler;
import org.jetbrains.annotations.Nullable;

@Mod(HopperGadgetry.MOD_ID)
public class HopperGadgetryNeoForge {

    public HopperGadgetryNeoForge() {
        ModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetry::new);
        registerCapabilities();
        DataProviderHelper.registerDataProviders(HopperGadgetry.MOD_ID,
                ModBlockLootProvider::new,
                ModRecipeProvider::new,
                ModBlockTagProvider::new);
    }

    private static void registerCapabilities() {
        NeoForgeCapabilityHelper.registerBlockEntity((HopperBlockEntity blockEntity, @Nullable Direction direction) -> {
            return new VanillaHopperItemHandler(blockEntity);
        }, ModRegistry.GRATED_HOPPER_BLOCK_ENTITY_TYPE, ModRegistry.DUCT_BLOCK_ENTITY_TYPE);
        NeoForgeCapabilityHelper.registerEntityContainer(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE);
    }
}
