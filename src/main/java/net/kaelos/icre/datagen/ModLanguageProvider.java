package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public enum Locale {
        EN_US("en_us"),
        RU_RU("ru_ru"),
        UK_UA("uk_ua");

        public final String code;

        Locale(String code) {
            this.code = code;
        }
    }

    private final Locale locale;

    public ModLanguageProvider(PackOutput output, Locale locale) {
        super(output, ICRE.MOD_ID, locale.code);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        add("creativetab." + ICRE.MOD_ID + ".resources_tab", "IC-RE: Resources");
        add("creativetab." + ICRE.MOD_ID + ".agriculture_tab", "IC-RE: Agriculture");
        add("creativetab." + ICRE.MOD_ID + ".tools_tab", "IC-RE: Tools");
        add("creativetab." + ICRE.MOD_ID + ".components_tab", "IC-RE: Components");
        add("creativetab." + ICRE.MOD_ID + ".machines_tab", "IC-RE: Machines");

        // Ingots
        add(ModItems.BRONZE_INGOT.get(), "Bronze Ingot", "Бронзовый слиток", "Бронзовий злиток");
        add(ModItems.LEAD_INGOT.get(), "Lead Ingot", "Свинцовый слиток", "Свинцевий злиток");
        add(ModItems.SILVER_INGOT.get(), "Silver Ingot", "Серебряный слиток", "Срібний злиток");
        add(ModItems.STEEL_INGOT.get(), "Steel Ingot", "Стальной слиток", "Сталевий злиток");
        add(ModItems.TIN_INGOT.get(), "Tin Ingot", "Оловянный слиток", "Олов'яний злиток");

        // Plates
        add(ModItems.BRONZE_PLATE.get(), "Bronze Plate", "Бронзовая пластина", "Бронзова пластина");
        add(ModItems.COPPER_PLATE.get(), "Copper Plate", "Медная пластина", "Мідна пластина");
        add(ModItems.GOLD_PLATE.get(), "Gold Plate", "Золотая пластина", "Золота пластина");
        add(ModItems.IRON_PLATE.get(), "Iron Plate", "Железная пластина", "Залізна пластина");
        add(ModItems.LAPIS_LAZULI_PLATE.get(), "Lapis Lazuli Plate", "Лазуритовая пластина", "Лазуритова пластина");
        add(ModItems.LEAD_PLATE.get(), "Lead Plate", "Свинцовая пластина", "Свинцева пластина");
        add(ModItems.OBSIDIAN_PLATE.get(), "Obsidian Plate", "Обсидиановая пластина", "Обсидіанова пластина");
        add(ModItems.STEEL_PLATE.get(), "Steel Plate", "Стальная пластина", "Сталева пластина");
        add(ModItems.TIN_PLATE.get(), "Tin Plate", "Оловянная пластина", "Олов'яна пластина");

        // Dense Plates
        add(ModItems.DENSE_BRONZE_PLATE.get(), "Dense Bronze Plate", "Сжатая бронзовая пластина", "Стиснута бронзова пластина");
        add(ModItems.DENSE_COPPER_PLATE.get(), "Dense Copper Plate", "Сжатая медная пластина", "Стиснута мідна пластина");
        add(ModItems.DENSE_GOLD_PLATE.get(), "Dense Gold Plate", "Сжатая золотая пластина", "Стиснута золота пластина");
        add(ModItems.DENSE_IRON_PLATE.get(), "Dense Iron Plate", "Сжатая железная пластина", "Стиснута залізна пластина");
        add(ModItems.DENSE_LAPIS_LAZULI_PLATE.get(), "Dense Lapis Lazuli Plate", "Сжатая лазуритовая пластина", "Стиснута лазуритова пластина");
        add(ModItems.DENSE_LEAD_PLATE.get(), "Dense Lead Plate", "Сжатая свинцовая пластина", "Стиснута свинцева пластина");
        add(ModItems.DENSE_OBSIDIAN_PLATE.get(), "Dense Obsidian Plate", "Сжатая обсидиановая пластина", "Стиснута обсидіанова пластина");
        add(ModItems.DENSE_STEEL_PLATE.get(), "Dense Steel Plate", "Сжатая стальная пластина", "Стиснута сталева пластина");
        add(ModItems.DENSE_TIN_PLATE.get(), "Dense Tin Plate", "Сжатая оловянная пластина", "Стиснута олов'яна пластина");

        // Casings
        add(ModItems.BRONZE_CASING.get(), "Bronze Casing", "Бронзовая оболочка", "Бронзова оболонка");
        add(ModItems.COPPER_CASING.get(), "Copper Casing", "Медная оболочка", "Мідна оболонка");
        add(ModItems.GOLD_CASING.get(), "Gold Casing", "Золотая оболочка", "Золота оболонка");
        add(ModItems.IRON_CASING.get(), "Iron Casing", "Железная оболочка", "Залізна оболонка");
        add(ModItems.LEAD_CASING.get(), "Lead Casing", "Свинцовая оболочка", "Свинцева оболонка");
        add(ModItems.STEEL_CASING.get(), "Steel Casing", "Стальная оболочка", "Сталева оболонка");
        add(ModItems.TIN_CASING.get(), "Tin Casing", "Оловянная оболочка", "Олов'яна оболонка");

        // Dusts
        add(ModItems.BRONZE_DUST.get(), "Bronze Dust", "Бронзовая пыль", "Бронзовий пил");
        add(ModItems.COPPER_DUST.get(), "Copper Dust", "Медная пыль", "Мідний пил");
        add(ModItems.GOLD_DUST.get(), "Gold Dust", "Золотая пыль", "Золотий пил");
        add(ModItems.IRON_DUST.get(), "Iron Dust", "Железная пыль", "Залізний пил");
        add(ModItems.LAPIS_LAZULI_DUST.get(), "Lapis Lazuli Dust", "Лазуритовая пыль", "Лазуритовий пил");
        add(ModItems.LEAD_DUST.get(), "Lead Dust", "Свинцовая пыль", "Свинцевий пил");
        add(ModItems.SILVER_DUST.get(), "Silver Dust", "Серебряная пыль", "Срібний пил");
        add(ModItems.TIN_DUST.get(), "Tin Dust", "Оловянная пыль", "Олов'яний пил");
        add(ModItems.HYDRATED_TIN_DUST.get(), "Hydrated Tin Dust", "Увлажнённая оловянная пыль", "Зволожений олов'яний пил");
        add(ModItems.DIAMOND_DUST.get(), "Diamond Dust", "Алмазная пыль", "Діамантовий пил");
        add(ModItems.COAL_DUST.get(), "Coal Dust", "Угольная пыль", "Вугільний пил");
        add(ModItems.HYDRATED_COAL_DUST.get(), "Hydrated Coal Dust", "Увлажнённая угольная пыль", "Зволожений вугільний пил");
        add(ModItems.OBSIDIAN_DUST.get(), "Obsidian Dust", "Обсидиановая пыль", "Обсидіановий пил");
        add(ModItems.ENERGIUM_DUST.get(), "Energium Dust", "Энергетическая пыль", "Енергетичний пил");
        add(ModItems.CLAY_DUST.get(), "Clay Dust", "Глиняная пыль", "Глиняний пил");
        add(ModItems.LITHIUM_DUST.get(), "Lithium Dust", "Литиевая пыль", "Літієвий пил");
        add(ModItems.SILICON_DIOXIDE_DUST.get(), "Silicon Dioxide Dust", "Пыль диоксида кремния", "Пил діоксиду кремнію");
        add(ModItems.SULFUR_DUST.get(), "Sulfur Dust", "Серная пыль", "Сірчаний пил");
        add(ModItems.NETHERRACK_DUST.get(), "Netherrack Dust", "Пыль незерака", "Пил незераку");
        add(ModItems.ENDER_PEARL_DUST.get(), "Ender Pearl Dust", "Пыль жемчуга Эндера", "Пил перлини Ендеру");
        add(ModItems.ENDER_EYE_DUST.get(), "Ender Eye Dust", "Пыль ока Эндера", "Пил ока Ендеру");
        add(ModItems.EMERALD_DUST.get(), "Emerald Dust", "Изумрудная пыль", "Смарагдовий пил");
        add(ModItems.MILK_POWDER.get(), "Milk Powder", "Сухое молоко", "Сухе молоко");

        // Ores
        add(ModBlocks.LEAD_ORE.get(), "Lead Ore", "Свинцовая руда", "Свинцева руда");
        add(ModBlocks.DEEPSLATE_LEAD_ORE.get(), "Deepslate Lead Ore", "Глубинная свинцовая руда", "Глибинна свинцева руда");
        add(ModBlocks.TIN_ORE.get(), "Tin Ore", "Оловянная руда", "Олов'яна руда");
        add(ModBlocks.DEEPSLATE_TIN_ORE.get(), "Deepslate Tin Ore", "Глубинная оловянная руда", "Глибинна олов'яна руда");
        add(ModBlocks.DEEPSLATE_URANIUM_ORE.get(), "Deepslate Uranium Ore", "Глубинная урановая руда", "Глибинна уранова руда");

        // Raws
        add(ModItems.RAW_LEAD.get(), "Raw Lead", "Глыба свинца", "Брила свинцю");
        add(ModItems.RAW_TIN.get(), "Raw Tin", "Глыба олова", "Брила олова");
        add(ModItems.RAW_URANIUM.get(), "Raw Uranium", "Глыба урана", "Брила урану");

        // Rubber Tree
        add(ModBlocks.RUBBER_LOG.get(), "Rubber Log", "Бревно гевеи", "Колода гевеї");
        add(ModBlocks.STRIPPED_RUBBER_LOG.get(), "Stripped Rubber Log", "Обтёсанное бревно гевеи", "Обтесана колода гевеї");
        add(ModBlocks.RUBBER_WOOD.get(), "Rubber Wood", "Древесина гевеи", "Деревина гевеї");
        add(ModBlocks.STRIPPED_RUBBER_WOOD.get(), "Stripped Rubber Wood", "Обтёсанная древесина гевеи", "Обтесана деревина гевеї");
        add(ModBlocks.RUBBER_LEAVES.get(), "Rubber Leaves", "Листва гевеи", "Листя гевеї");
        add(ModBlocks.RUBBER_SAPLING.get(), "Rubber Sapling", "Саженец гевеи", "Саджанець гевеї");
        add(ModBlocks.RUBBER_PLANKS.get(), "Rubber Planks", "Доски из гевеи", "Дошки з гевеї");

        // Components
        add(ModItems.LATEX.get(), "Latex", "Каучук", "Каучук");
        add(ModItems.RUBBER.get(), "Rubber", "Резина", "Гума");

        // Tools
        add(ModItems.TREETAP.get(), "Treetap", "Краник", "Краник");
        add(ModItems.WRENCH.get(), "Wrench", "Гаечный ключ", "Гайковий ключ");
        add(ModItems.FORGE_HAMMER.get(), "Forge Hammer", "Кузнечный молот", "Ковальський молот");
        add(ModItems.CUTTER.get(), "Cutter", "Кусачки", "Кусачки");

        // Cables
        add(ModBlocks.TIN_CABLE.get(), "Tin Cable", "Оловянный кабель", "Олов'яний кабель");
        add(ModBlocks.TIN_CABLE_1X.get(), "Insulated Tin Cable", "Изолированный оловянный кабель", "Ізольований олов'яний кабель");
        add(ModBlocks.COPPER_CABLE.get(), "Copper Cable", "Медный кабель", "Мідний кабель");
        add(ModBlocks.COPPER_CABLE_1X.get(), "Insulated Copper Cable", "Изолированный медный кабель", "Ізольований мідний кабель");
        add(ModBlocks.GOLD_CABLE.get(), "Gold Cable", "Золотой кабель", "Золотий кабель");
        add(ModBlocks.GOLD_CABLE_1X.get(), "Insulated Gold Cable", "Изолированный золотой кабель", "Ізольований золотий кабель");
        add(ModBlocks.GOLD_CABLE_2X.get(), "2x Insulated Gold Cable", "2x изолированный золотой кабель", "2x ізольований золотий кабель");
        add(ModBlocks.IRON_CABLE.get(), "HV Cable", "Высоковольтный кабель", "Високовольтний кабель");
        add(ModBlocks.IRON_CABLE_1X.get(), "Insulated HV Cable", "Изолированный высоковольтный кабель", "Ізольований високовольтний кабель");
        add(ModBlocks.IRON_CABLE_2X.get(), "2x Insulated HV Cable", "2x изолированный высоковольтный кабель", "2x ізольований високовольтний кабель");
        add(ModBlocks.IRON_CABLE_4X.get(), "4x Insulated HV Cable", "4x изолированный высоковольтный кабель", "4x ізольований високовольтний кабель");
        add(ModBlocks.GLASS_FIBRE_CABLE.get(), "Glass Fibre Cable", "Стекловолоконный кабель", "Скловолоконний кабель");

        // Machine Casings
        add(ModBlocks.BASIC_MACHINE_CASING.get(), "Basic Machine Casing", "Базовый корпус механизма", "Базовий корпус механізму");

        // Generators & Machines
        add(ModBlocks.GENERATOR.get(), "Generator", "Генератор", "Генератор");
        add(ModBlocks.MACERATOR.get(), "Macerator", "Дробитель", "Подрібнювач");
        add(ModBlocks.METAL_FORMER.get(), "Metal Former", "Металлоформовочный механизм", "Металоформувальний механізм");
        add(ModBlocks.ELECTRIC_FURNACE.get(), "Electric Furnace", "Электропечь", "Електропіч");
        add(ModBlocks.COMPRESSOR.get(), "Compressor", "Компрессор", "Компресор");

        // Tooltips
        add("tooltip." + ICRE.MOD_ID + ".durability", pick("Durability: ", "Прочность: ", "Міцність: "));

        // GUIs
        add("gui." + ICRE.MOD_ID + ".metal_former.mode.rolling", "Rolling", "Прокатка", "Прокатка");
        add("gui." + ICRE.MOD_ID + ".metal_former.mode.cutting", "Cutting", "Резка", "Різання");
        add("gui." + ICRE.MOD_ID + ".metal_former.mode.extruding", "Extruding", "Выдавливание", "Видавлювання");
    }

    private void add(Item item, String en, String ru, String uk) {
        add(item, pick(en, ru, uk));
    }

    private void add(Block block, String en, String ru, String uk) {
        add(block, pick(en, ru, uk));
    }

    private void add(String name, String en, String ru, String uk) {
        add(name, pick(en, ru, uk));
    }

    private String pick(String en, String ru, String uk) {
        return switch (locale) {
            case EN_US -> en;
            case RU_RU -> ru;
            case UK_UA -> uk;
        };
    }
}
