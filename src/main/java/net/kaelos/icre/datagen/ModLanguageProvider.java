package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
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

        add(ModItems.BRONZE_INGOT.get(), "Bronze Ingot", "Бронзовый слиток", "Бронзовий злиток");
        add(ModItems.LEAD_INGOT.get(), "Lead Ingot", "Свинцовый слиток", "Свинцевий злиток");
        add(ModItems.SILVER_INGOT.get(), "Silver Ingot", "Серебряный слиток", "Срібний злиток");
        add(ModItems.STEEL_INGOT.get(), "Steel Ingot", "Стальной слиток", "Сталевий злиток");
        add(ModItems.TIN_INGOT.get(), "Tin Ingot", "Оловянный слиток", "Олов'яний злиток");

        add(ModItems.BRONZE_PLATE.get(), "Bronze Plate", "Бронзовая пластина", "Бронзова пластина");
        add(ModItems.COPPER_PLATE.get(), "Copper Plate", "Медная пластина", "Мідна пластина");
        add(ModItems.GOLD_PLATE.get(), "Gold Plate", "Золотая пластина", "Золота пластина");
        add(ModItems.IRON_PLATE.get(), "Iron Plate", "Железная пластина", "Залізна пластина");
        add(ModItems.LAPIS_LAZULI_PLATE.get(), "Lapis Lazuli Plate", "Лазуритовая пластина", "Лазуритова пластина");
        add(ModItems.LEAD_PLATE.get(), "Lead Plate", "Свинцовая пластина", "Свинцева пластина");
        add(ModItems.OBSIDIAN_PLATE.get(), "Obsidian Plate", "Обсидиановая пластина", "Обсидіанова пластина");
        add(ModItems.STEEL_PLATE.get(), "Steel Plate", "Стальная пластина", "Сталева пластина");
        add(ModItems.TIN_PLATE.get(), "Tin Plate", "Оловянная пластина", "Олов'яна пластина");

        add(ModItems.DENSE_BRONZE_PLATE.get(), "Dense Bronze Plate", "Сжатая бронзовая пластина", "Стиснута бронзова пластина");
        add(ModItems.DENSE_COPPER_PLATE.get(), "Dense Copper Plate", "Сжатая медная пластина", "Стиснута мідна пластина");
        add(ModItems.DENSE_GOLD_PLATE.get(), "Dense Gold Plate", "Сжатая золотая пластина", "Стиснута золота пластина");
        add(ModItems.DENSE_IRON_PLATE.get(), "Dense Iron Plate", "Сжатая железная пластина", "Стиснута залізна пластина");
        add(ModItems.DENSE_LAPIS_LAZULI_PLATE.get(), "Dense Lapis Lazuli Plate", "Сжатая лазуритовая пластина", "Стиснута лазуритова пластина");
        add(ModItems.DENSE_LEAD_PLATE.get(), "Dense Lead Plate", "Сжатая свинцовая пластина", "Стиснута свинцева пластина");
        add(ModItems.DENSE_OBSIDIAN_PLATE.get(), "Dense Obsidian Plate", "Сжатая обсидиановая пластина", "Стиснута обсидіанова пластина");
        add(ModItems.DENSE_STEEL_PLATE.get(), "Dense Steel Plate", "Сжатая стальная пластина", "Стиснута сталева пластина");
        add(ModItems.DENSE_TIN_PLATE.get(), "Dense Tin Plate", "Сжатая оловянная пластина", "Стиснута олов'яна пластина");

        add(ModItems.BRONZE_CASING.get(), "Bronze Casing", "Бронзовая оболочка", "Бронзова оболонка");
        add(ModItems.COPPER_CASING.get(), "Copper Casing", "Медная оболочка", "Мідна оболонка");
        add(ModItems.GOLD_CASING.get(), "Gold Casing", "Золотая оболочка", "Золота оболонка");
        add(ModItems.IRON_CASING.get(), "Iron Casing", "Железная оболочка", "Залізна оболонка");
        add(ModItems.LEAD_CASING.get(), "Lead Casing", "Свинцовая оболочка", "Свинцева оболонка");
        add(ModItems.STEEL_CASING.get(), "Steel Casing", "Стальная оболочка", "Сталева оболонка");
        add(ModItems.TIN_CASING.get(), "Tin Casing", "Оловянная оболочка", "Олов'яна оболонка");
    }

    private void add(Item item, String en, String ru, String uk) {
        add(item, pick(en, ru, uk));
    }

    private void add(Block block, String en, String ru, String uk) {
        add(block, pick(en, ru, uk));
    }

    private String pick(String en, String ru, String uk) {
        return switch (locale) {
            case EN_US -> en;
            case RU_RU -> ru;
            case UK_UA -> uk;
        };
    }
}
