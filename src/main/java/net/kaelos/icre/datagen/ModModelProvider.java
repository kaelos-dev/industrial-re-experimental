package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ICRE.MOD_ID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        flatItem(itemModels, ModItems.BRONZE_INGOT.get(), "ingot/bronze_ingot", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.LEAD_INGOT.get(), "ingot/lead_ingot", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.SILVER_INGOT.get(), "ingot/silver_ingot", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.STEEL_INGOT.get(), "ingot/steel_ingot", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.TIN_INGOT.get(), "ingot/tin_ingot", ModelTemplates.FLAT_ITEM);

        flatItem(itemModels, ModItems.BRONZE_PLATE.get(), "plate/bronze_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.COPPER_PLATE.get(), "plate/copper_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.GOLD_PLATE.get(), "plate/gold_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.IRON_PLATE.get(), "plate/iron_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.LAPIS_LAZULI_PLATE.get(), "plate/lapis_lazuli_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.LEAD_PLATE.get(), "plate/lead_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.OBSIDIAN_PLATE.get(), "plate/obsidian_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.STEEL_PLATE.get(), "plate/steel_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.TIN_PLATE.get(), "plate/tin_plate", ModelTemplates.FLAT_ITEM);

        flatItem(itemModels, ModItems.DENSE_BRONZE_PLATE.get(), "denseplate/dense_bronze_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_COPPER_PLATE.get(), "denseplate/dense_copper_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_GOLD_PLATE.get(), "denseplate/dense_gold_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_IRON_PLATE.get(), "denseplate/dense_iron_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_LAPIS_LAZULI_PLATE.get(), "denseplate/dense_lapis_lazuli_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_LEAD_PLATE.get(), "denseplate/dense_lead_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_OBSIDIAN_PLATE.get(), "denseplate/dense_obsidian_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_STEEL_PLATE.get(), "denseplate/dense_steel_plate", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.DENSE_TIN_PLATE.get(), "denseplate/dense_tin_plate", ModelTemplates.FLAT_ITEM);

        flatItem(itemModels, ModItems.BRONZE_CASING.get(), "casing/bronze_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.COPPER_CASING.get(), "casing/copper_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.GOLD_CASING.get(), "casing/gold_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.IRON_CASING.get(), "casing/iron_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.LEAD_CASING.get(), "casing/lead_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.STEEL_CASING.get(), "casing/steel_casing", ModelTemplates.FLAT_ITEM);
        flatItem(itemModels, ModItems.TIN_CASING.get(), "casing/tin_casing", ModelTemplates.FLAT_ITEM);
    }

    private static void flatItem(ItemModelGenerators itemModels, Item item, String texturePath, ModelTemplate template) {
        Material material = new Material(ICRE.makeId("item/" + texturePath));
        TextureMapping mapping = TextureMapping.layer0(material);
        Identifier modelId = template.create(item, mapping, itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
    }
}
