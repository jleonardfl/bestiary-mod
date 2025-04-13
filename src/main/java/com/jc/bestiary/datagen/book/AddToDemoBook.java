/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.jc.bestiary.datagen.book;

import com.jc.bestiary.datagen.book.addtodemo.AddToGettingStartedCategory;
import com.jc.bestiary.datagen.book.addtodemo.NewCategory;
import com.klikli_dev.modonomicon.api.datagen.AddToBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import net.minecraft.resources.ResourceLocation;

public class AddToDemoBook extends AddToBookSubProvider {

    public static final ResourceLocation TARGET_BOOK_ID = ResourceLocation.fromNamespaceAndPath("theurgy", "the_hermetica");

    public AddToDemoBook(ModonomiconLanguageProvider lang) {
        super(TARGET_BOOK_ID, lang);
    }

    @Override
    protected void registerDefaultMacros() {
        //currently no macros
    }

    @Override
    protected void generateCategories() {
        //add stuff to an existing category
        var addToGettingStartedCategory = this.add(new AddToGettingStartedCategory(this).generate());

        //add a new category
        this.currentSortIndex(50); //ensure the new category is sorted after the existing ones
        var newCategory = this.add(new NewCategory(this).generate());
    }
}
