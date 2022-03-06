package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import com.teammetallurgy.aquaculture.entity.SpectralWaterArrowEntity;
import com.teammetallurgy.aquaculture.entity.TurtleLandEntity;
import com.teammetallurgy.aquaculture.entity.WaterArrowEntity;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import com.teammetallurgy.aquaculture.misc.AquaConfig.Helper;
import com.teammetallurgy.aquaculture.misc.BiomeDictionaryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquaEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_DEFERRED = DeferredRegister.create(ForgeRegistries.ENTITIES, Aquaculture.MOD_ID);
    private static final List<String> MOB_NAMES = Lists.newArrayList();
    public static final RegistryObject<EntityType<AquaFishingBobberEntity>> BOBBER = register("bobber", () -> EntityType.Builder.<AquaFishingBobberEntity>createNothing(MobCategory.MISC)
            .noSave()
            .noSummon()
            .sized(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(5)
            .setCustomClientFactory(AquaFishingBobberEntity::new));
    public static final RegistryObject<EntityType<WaterArrowEntity>> WATER_ARROW = register("water_arrow", () -> EntityType.Builder.<WaterArrowEntity>of(WaterArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setCustomClientFactory(WaterArrowEntity::new));
    public static final RegistryObject<EntityType<SpectralWaterArrowEntity>> SPECTRAL_WATER_ARROW = register("spectral_water_arrow", () -> EntityType.Builder.<SpectralWaterArrowEntity>of(SpectralWaterArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .setCustomClientFactory(SpectralWaterArrowEntity::new));
    public static final RegistryObject<EntityType<TurtleLandEntity>> BOX_TURTLE = registerMob("box_turtle", 1, 2, 10, BiomeDictionary.Type.SWAMP, null, 0x7F8439, 0x5D612A,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.25F));
    public static final RegistryObject<EntityType<TurtleLandEntity>> ARRAU_TURTLE = registerMob("arrau_turtle", 1, 2, 8, BiomeDictionary.Type.JUNGLE, null, 0x71857A, 0x4F6258,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.25F));
    public static final RegistryObject<EntityType<TurtleLandEntity>> STARSHELL_TURTLE = registerMob("starshell_turtle", 1, 2, 5, BiomeDictionaryHelper.TWILIGHT, null, 0xDCE2E5, 0x464645,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.25F));

    private static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, int min, int max, int weight, BiomeDictionary.Type include, @Nullable BiomeDictionary.Type exclude, int eggPrimary, int eggSecondary, Supplier<EntityType.Builder<T>> builder) {
        return registerMob(name, min, max, weight, eggPrimary, eggSecondary, Collections.singletonList(String.valueOf(include == null ? "" : include)), Collections.singletonList(String.valueOf(exclude == null ? "" : exclude)), builder);
    }

    private static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, int min, int max, int weight, int eggPrimary, int eggSecondary, List<? extends String> include, List<? extends String> exclude, Supplier<EntityType.Builder<T>> builder) {
        RegistryObject<EntityType<T>> entityType = register(name, builder);
        AquaItems.register(() -> new ForgeSpawnEggItem(entityType, eggPrimary, eggSecondary, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)), name + "_spawn_egg");
        new AquaConfig.Spawn(AquaConfig.BUILDER, name, min, max, weight, include, exclude);
        MOB_NAMES.add(name);
        return entityType;
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        ResourceLocation location = new ResourceLocation(Aquaculture.MOD_ID, name);
        return ENTITY_DEFERRED.register(name, () -> builder.get().build(location.toString()));
    }

    public static void setSpawnPlacement() {
        SpawnPlacements.register(BOX_TURTLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules);
        SpawnPlacements.register(ARRAU_TURTLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules);
        SpawnPlacements.register(STARSHELL_TURTLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules);
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BOX_TURTLE.get(), TurtleLandEntity.createAttributes().build());
        event.put(ARRAU_TURTLE.get(), TurtleLandEntity.createAttributes().build());
        event.put(STARSHELL_TURTLE.get(), TurtleLandEntity.createAttributes().build());
    }

    public static void addEntitySpawns(BiomeLoadingEvent event) {
        for (String name : MOB_NAMES) {
            String subCategory = Helper.getSubConfig(AquaConfig.Spawn.SPAWN_OPTIONS, name);
            BiomeDictionaryHelper.addSpawn(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(Aquaculture.MOD_ID, name)), Helper.get(subCategory, "min"), Helper.get(subCategory, "max"), Helper.get(subCategory, "weight"), Helper.get(subCategory, "include"), Helper.get(subCategory, "exclude"), event);
        }
    }
}