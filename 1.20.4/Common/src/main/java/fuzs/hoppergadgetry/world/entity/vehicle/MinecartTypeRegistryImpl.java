package fuzs.hoppergadgetry.world.entity.vehicle;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class MinecartTypeRegistryImpl implements MinecartTypeRegistry {
    private static final Map<AbstractMinecart.Type, Factory> MINECART_FACTORIES = Maps.newEnumMap(AbstractMinecart.Type.class);

    @Override
    public void register(AbstractMinecart.Type type, Factory factory) {
        Objects.requireNonNull(type, "type is null");
        Objects.requireNonNull(factory, "factory is null");
        MINECART_FACTORIES.put(type, factory);
    }

    public static Optional<AbstractMinecart> createMinecartForType(AbstractMinecart.Type type, Level level, double x, double y, double z) {
        Objects.requireNonNull(type, "type is null");
        if (MINECART_FACTORIES.containsKey(type)) {
            return Optional.of(MINECART_FACTORIES.get(type)).map(factory -> factory.create(level, x, y, z));
        } else {
            return Optional.empty();
        }
    }
}
