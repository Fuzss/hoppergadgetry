package fuzs.hoppergadgetry.world.entity.vehicle;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public interface MinecartTypeRegistry {
    MinecartTypeRegistry INSTANCE = new MinecartTypeRegistryImpl();

    void register(AbstractMinecart.Type type, Factory factory);

    interface Factory {

        AbstractMinecart create(Level level, double x, double y, double z);
    }
}
