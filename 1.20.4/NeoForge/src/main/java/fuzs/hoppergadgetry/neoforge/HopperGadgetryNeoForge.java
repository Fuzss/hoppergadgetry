package fuzs.hoppergadgetry.neoforge;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.data.ModBlockLootProvider;
import fuzs.hoppergadgetry.data.ModBlockTagProvider;
import fuzs.hoppergadgetry.data.ModRecipeProvider;
import fuzs.hoppergadgetry.data.client.ModLanguageProvider;
import fuzs.hoppergadgetry.data.client.ModModelProvider;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelperV2;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.VanillaHopperItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

@Mod(HopperGadgetry.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HopperGadgetryNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetry::new);
        registerCapabilities();
        DataProviderHelper.registerDataProviders(HopperGadgetry.MOD_ID,
                ModBlockLootProvider::new,
                ModRecipeProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new,
                ModBlockTagProvider::new
        );
    }

    private static void registerCapabilities() {
        NeoForgeCapabilityHelperV2.registerBlockEntity((HopperBlockEntity blockEntity, @Nullable Direction direction) -> {
            return new VanillaHopperItemHandler(blockEntity);
        }, ModRegistry.GRATED_HOPPER_BLOCK_ENTITY_TYPE, ModRegistry.DUCT_BLOCK_ENTITY_TYPE);
        registerEntityContainer(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE);
    }

    @Deprecated(forRemoval = true)
    @SafeVarargs
    public static <T extends Entity & Container> void registerEntityContainer(Holder<? extends EntityType<? extends T>>... entityTypes) {
        NeoForgeCapabilityHelperV2.register((RegisterCapabilitiesEvent evt, EntityType<? extends T> entityType) -> {
            evt.registerEntity(Capabilities.ItemHandler.ENTITY, entityType, (T entity, Void aVoid) -> {
                return new InvWrapper(entity);
            });
            evt.registerEntity(Capabilities.ItemHandler.ENTITY_AUTOMATION, entityType, (T entity, @Nullable Direction direction) -> {
                return new InvWrapper(entity);
            });
        }, entityTypes);
    }
}
