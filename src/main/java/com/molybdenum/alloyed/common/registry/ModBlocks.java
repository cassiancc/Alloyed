package com.molybdenum.alloyed.common.registry;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.compat.create.CreateCompat;
import com.molybdenum.alloyed.common.content.blocks.BronzeBellBlock;
import com.molybdenum.alloyed.common.content.blocks.ForgeBlock;
import com.molybdenum.alloyed.common.content.blocks.SteelDoorBlock;
import com.molybdenum.alloyed.common.content.blocks.WeatheringRotatedPillarBlock;
import com.molybdenum.alloyed.common.util.Platform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;


import java.util.List;
import java.util.function.Function;

import static com.molybdenum.alloyed.Alloyed.MOD_ID;

@SuppressWarnings({"unused"})
public class ModBlocks {
    // BRONZE
    public static final List<BlockEntry<? extends Block>> BRONZE_BLOCK = registerBronzeSet("bronze_block", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final List<BlockEntry<? extends Block>> EXPOSED_BRONZE_BLOCK = registerBronzeSet("exposed_bronze_block", WeatheringCopper.WeatherState.EXPOSED);
    public static final List<BlockEntry<? extends Block>> WEATHERED_BRONZE_BLOCK = registerBronzeSet("weathered_bronze_block", WeatheringCopper.WeatherState.WEATHERED);
    public static final List<BlockEntry<? extends Block>> OXIDIZED_BRONZE_BLOCK = registerBronzeSet("oxidized_bronze_block", WeatheringCopper.WeatherState.OXIDIZED);


    public static final List<BlockEntry<? extends Block>> CUT_BRONZE = registerCutBronzeSet("cut_bronze", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final List<BlockEntry<? extends Block>> CUT_EXPOSED_BRONZE = registerCutBronzeSet("cut_exposed_bronze", WeatheringCopper.WeatherState.EXPOSED);
    public static final List<BlockEntry<? extends Block>> CUT_WEATHERED_BRONZE = registerCutBronzeSet("cut_weathered_bronze", WeatheringCopper.WeatherState.WEATHERED);
    public static final List<BlockEntry<? extends Block>> CUT_OXIDIZED_BRONZE = registerCutBronzeSet("cut_oxidized_bronze", WeatheringCopper.WeatherState.OXIDIZED);

    public static final List<BlockEntry<? extends Block>> BRONZE_PILLAR = registerBronzePillarSet("bronze_pillar", WeatheringCopper.WeatherState.UNAFFECTED);
    public static final List<BlockEntry<? extends Block>> EXPOSED_BRONZE_PILLAR = registerBronzePillarSet("exposed_bronze_pillar", WeatheringCopper.WeatherState.EXPOSED);
    public static final List<BlockEntry<? extends Block>> WEATHERED_BRONZE_PILLAR = registerBronzePillarSet("weathered_bronze_pillar", WeatheringCopper.WeatherState.WEATHERED);
    public static final List<BlockEntry<? extends Block>> OXIDIZED_BRONZE_PILLAR = registerBronzePillarSet("oxidized_bronze_pillar", WeatheringCopper.WeatherState.OXIDIZED);


    public static final BlockEntry<BronzeBellBlock> BRONZE_BELL = registerBlock("bronze_bell", BronzeBellBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()
            .sound(SoundType.ANVIL));



    // STEEL

    public static final BlockEntry<Block> STEEL_BLOCK = registerBlock("steel_block", Block::new, ModBlocks.steelProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));


    public static final BlockEntry<SteelDoorBlock> STEEL_DOOR =
            steelDoorBlock(false, null);

    public static final BlockEntry<SteelDoorBlock> LOCKED_STEEL_DOOR =
            steelDoorBlock(true, STEEL_DOOR);

    public static final BlockEntry<Block> STEEL_SHEET_METAL = registerBlock("steel_sheet_metal",Block::new, Blocks.IRON_BLOCK);

    public static final BlockEntry<StairBlock> STEEL_SHEET_STAIRS = registerBlock("steel_sheet_stairs", properties ->
            new StairBlock(Blocks.BRICK_STAIRS.defaultBlockState(), properties));

    public static final BlockEntry<SlabBlock> STEEL_SHEET_SLAB = registerBlock("steel_sheet_slab", SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));

    public static final BlockEntry<IronBarsBlock> STEEL_BARS = registerBlock("steel_bars", IronBarsBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS));

    public static final BlockEntry<TrapDoorBlock> STEEL_TRAPDOOR = registerBlock("steel_trapdoor", properties -> new TrapDoorBlock(ModBlockSetTypes.STEEL, properties), BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR));

    public static final BlockEntry<FenceBlock> STEEL_MESH_FENCE = registerBlock("steel_mesh_fence", FenceBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.CHAIN));
    public static final BlockEntry<ForgeBlock> FORGE = registerBlock("forge", ForgeBlock::new);


    public static void register() {
        Alloyed.LOGGER.debug("Registering ModBlocks!");
    }

    public static BlockBehaviour.@NotNull Properties steelProperties(BlockBehaviour.Properties properties) {
        return properties.sound(SoundType.NETHERITE_BLOCK).strength(5, 14).mapColor(MapColor.COLOR_GRAY);
    }

    public static BlockBehaviour.@NotNull Properties steelProperties() {
        return steelProperties(BlockBehaviour.Properties.of());
    }

    public static BlockBehaviour.@NotNull Properties bronzeProperties(BlockBehaviour.Properties properties) {
        return properties.sound(SoundType.COPPER).strength(3, 6).mapColor(MapColor.COLOR_ORANGE);
    }

    public static BlockBehaviour.@NotNull Properties bronzeProperties() {
        return bronzeProperties(BlockBehaviour.Properties.of());
    }

    public static void fixBronzeBlocks() {
    }

    private static List<BlockEntry<? extends Block>> registerBronzePillarSet(String id, WeatheringCopper.WeatherState state) {
        if (Platform.isLoaded("create")) {
            return CreateCompat.registerBronzePillarSet(id, state);
        } else {
            var block = registerBlock(id, (properties)-> new WeatheringRotatedPillarBlock(state, properties), ModBlocks.bronzeProperties());
            var waxedBlock = registerBlock("waxed_"+id, RotatedPillarBlock::new, ModBlocks.bronzeProperties());
            Platform.addWaxable(block.get(), waxedBlock.get());
            return List.of(block, waxedBlock);
        }
    }

    private static List<BlockEntry<? extends Block>> registerBronzeSet(String id, WeatheringCopper.WeatherState state) {
        var block = registerCutBronze(id, state);
        var waxedBlock = registerBlock("waxed_"+id,(Block::new), Blocks.CUT_COPPER);
        Platform.addWeathering(block.get(), waxedBlock.get());
		switch (state) {
			case WEATHERED ->
					Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("weathered_", "exposed_"))), block.get());
			case EXPOSED ->
					Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("exposed_", ""))), block.get());
			case OXIDIZED ->
					Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("oxidized_", "weathered_"))), block.get());
		}
        return List.of(block, waxedBlock);
    }

    private static List<BlockEntry<? extends Block>> registerCutBronzeSet(String id, WeatheringCopper.WeatherState state) {
        var block = registerCutBronze(id, state);
        var stairs = registerCutBronzeStairs(id, state);
        var slab = registerCutBronzeSlab(id, state);
        var waxedBlock = registerBlock("waxed_"+id,(Block::new), Blocks.CUT_COPPER);
        var waxedStairs = registerBlock("waxed_"+id+"_stairs", properties ->
                new WeatheringCopperStairBlock(state, Blocks.BRICK_STAIRS.defaultBlockState(), properties), Blocks.CUT_COPPER);
        var waxedSlab = registerBlock("waxed_"+id+"_slab", SlabBlock::new, Blocks.CUT_COPPER);
        Platform.addWaxable(block.get(), waxedBlock.get());
        Platform.addWaxable(stairs.get(), waxedStairs.get());
        Platform.addWaxable(slab.get(), waxedSlab.get());
		switch (state) {
			case WEATHERED -> {
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("weathered_", "exposed_"))), block.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(stairs.getId().getPath().replace("weathered_", "exposed_"))), stairs.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(slab.getId().getPath().replace("weathered_", "exposed_"))), slab.get());
			}
			case EXPOSED -> {
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("exposed_", ""))), block.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(stairs.getId().getPath().replace("exposed_", ""))), stairs.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(slab.getId().getPath().replace("exposed_", ""))), slab.get());
			}
			case OXIDIZED -> {
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(id.replace("oxidized_", "weathered_"))), block.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(stairs.getId().getPath().replace("oxidized_", "weathered_"))), stairs.get());
				Platform.addWeathering(BuiltInRegistries.BLOCK.getValue(Alloyed.asResource(slab.getId().getPath().replace("oxidized_", "weathered_"))), slab.get());
			}
		}
        return List.of(block, stairs, slab, waxedBlock, waxedStairs, waxedSlab);
    }

    private static BlockEntry<? extends Block> registerCutBronze(String id, WeatheringCopper.WeatherState state) {
        return registerBlock(id,(properties -> new WeatheringCopperFullBlock(state, properties)), Blocks.CUT_COPPER);
    }

    private static BlockEntry<? extends SlabBlock> registerCutBronzeSlab(String id, WeatheringCopper.WeatherState state) {
        return registerBlock(id+"_slab", (p)-> new WeatheringCopperSlabBlock(state, p), Blocks.CUT_COPPER);
    }

    private static BlockEntry<WeatheringCopperStairBlock> registerCutBronzeStairs(String id, WeatheringCopper.WeatherState state) {
        return registerBlock(id+"_stairs", properties ->
                new WeatheringCopperStairBlock(state, Blocks.BRICK_STAIRS.defaultBlockState(), properties), Blocks.CUT_COPPER);
    }

    private static BlockEntry<SteelDoorBlock> steelDoorBlock(boolean locked, BlockEntry<SteelDoorBlock> normalDoor) {
        String path = "block/" + (locked ? "locked_" : "") + "steel_door/";
        String name = (locked ? "locked_" : "") + "steel_door";

        return registerBlock(name, properties -> new SteelDoorBlock(properties, locked), BlockBehaviour.Properties.of().noOcclusion()
                .sound(SoundType.METAL)
                .strength(5)
                .requiresCorrectToolForDrops());
    }

    public static <T extends Block> BlockEntry<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings) {
        return registerBlock(id, factory, settings, true);
    }

    public static <T extends Block> BlockEntry<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings, boolean b) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, id));
        T block = factory.apply(settings.setId(key));
        var entry = Registry.register(BuiltInRegistries.BLOCK, key, block);
        BlockEntry<T> tBlockEntry = new BlockEntry<>(key.identifier(), entry);
        if (b)
            ModItems.registerBlockItem(tBlockEntry);
        return tBlockEntry;
    }


    public static <T extends Block> BlockEntry<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> factory) {
        return registerBlock(id, factory, BlockBehaviour.Properties.of());
    }

    public static <T extends Block> BlockEntry<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> factory, Block block) {
        return registerBlock(id, factory, BlockBehaviour.Properties.ofFullCopy(block));
    }
}
