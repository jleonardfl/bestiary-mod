/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.jc.bestiary.datagen.book;

import com.jc.bestiary.datagen.book.demo.ConditionalCategory;
import com.jc.bestiary.datagen.book.demo.FeaturesCategory;
import com.jc.bestiary.datagen.book.demo.FormattingCategory;
import com.jc.bestiary.datagen.book.demo.IndexModeCategory;
import com.jc.bestiary.datagen.book.demo.features.ConditionRootEntry;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCommandModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import net.minecraft.resources.ResourceLocation;

public class DemoBook extends SingleBookSubProvider {

    public static final String ID = "demo";

    public DemoBook(String modid, ModonomiconLanguageProvider lang) {
        super(ID, modid, lang);
    }

    @Override
    protected BookModel additionalSetup(BookModel book) {
        //if we want to handle a second language in here we can add book-related (not category or entry or page) translations here
        //this.add(this.lang("ru_ru"), this.context().bookName(), "Демонстрационная книга");
        //this.add(lang("ru_ru"), this.context().bookTooltip(), "Книга для демонстрации и тестирования функций \"Модономикона\".");

        var commandEntryCommand = BookCommandModel.create(this.modLoc("test_command"), "/give @s minecraft:apple 1")
                .withPermissionLevel(2)
                .withSuccessMessage("modonomicon.command.test_command.success");
        this.add(commandEntryCommand.getSuccessMessage(), "You got an apple, because reading is cool!");

        var commandEntryLinkCommand = BookCommandModel.create(this.modLoc("test_command2"), "/give @s minecraft:wheat 1")
                .withPermissionLevel(2)
                .withSuccessMessage("modonomicon.command.test_command2.success");
        this.add(commandEntryLinkCommand.getSuccessMessage(), "You got wheat, because clicking is cool!");

        return book.withModel(ResourceLocation.parse("modonomicon:modonomicon_green"))
                .withBookTextOffsetX(5)
                .withBookTextOffsetY(0) //no top offset
                .withBookTextOffsetWidth(-5)
                .withCommand(commandEntryCommand)
                .withCommand(commandEntryLinkCommand)
                .withAllowOpenBooksWithInvalidLinks(true);
    }

    @Override
    protected void registerDefaultMacros() {
        //currently no macros
    }

    @Override
    protected void generateCategories() {
        //for the two big categories we use the category provider
        var featuresCategory = this.add(new FeaturesCategory(this).generate());
        var formattingCategory = this.add(new FormattingCategory(this).generate());

        var conditionalCategory = this.add(new ConditionalCategory(this).generate())
                .withCondition(this.condition().entryRead(this.modLoc(FeaturesCategory.ID, ConditionRootEntry.ID)));

        var indexModeCategory = this.add(new IndexModeCategory(this).generate());
    }

    @Override
    protected String bookName() {
        return "Demo Book";
    }

    @Override
    protected String bookTooltip() {
        return "A book to showcase & test Modonomicon features.";
    }
}
