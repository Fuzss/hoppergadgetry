package fuzs.hoppergadgetry.mixin;

import fuzs.hoppergadgetry.init.MinecartTypeRegistryImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
abstract class AbstractMinecartMixin extends Entity {

    public AbstractMinecartMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "createMinecart", at = @At("HEAD"), cancellable = true)
    private static void createMinecart(Level level, double x, double y, double z, AbstractMinecart.Type type, CallbackInfoReturnable<AbstractMinecart> callback) {
        MinecartTypeRegistryImpl.createMinecartForType(type, level, x, y, z).ifPresent(callback::setReturnValue);
    }
}
