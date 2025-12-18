package net.fiveleafclover.soulbound.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.item.custom.BlazesteelKhopeshItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item BLAZESTEEL_KHOPESH = registerItem("blazesteel_khopesh", new BlazesteelKhopeshItem(
            ModToolMaterial.SOUL, 8, -2f, new FabricItemSettings()));

    public static final Item SOUL = registerItem("soul", new Item(new FabricItemSettings().rarity(Rarity.RARE).maxCount(1)));
    public static final Item SOUL_SHARD = registerItem("soul_shard", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item SOUL_FRAGMENT = registerItem("soul_fragment", new Item(new FabricItemSettings()));


    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(BLAZESTEEL_KHOPESH);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SoulBound.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SoulBound.LOGGER.info(SoulBound.MOD_ID + " is registering items :3");

        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
