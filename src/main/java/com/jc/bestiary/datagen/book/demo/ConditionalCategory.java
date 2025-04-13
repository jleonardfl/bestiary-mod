// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.jc.bestiary.datagen.book.demo;

import com.jc.bestiary.datagen.book.demo.formatting.AlwaysLockedEntry;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.SingleBookSubProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import net.minecraft.world.item.Items;

public class ConditionalCategory extends CategoryProvider {
    public static final String ID = "conditional";

    public ConditionalCategory(SingleBookSubProvider parent) {
        super(parent);
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
                "_____________________",
                "_____________________",
                "__________l__________",
                "_____________________",
                "_____________________"
        };
    }

    @Override
    protected void generateEntries() {
        var alwaysLockedEntry = this.add(new AlwaysLockedEntry(this).generate('l'));
    }

    @Override
    protected String categoryName() {
        return "Conditional Category";
    }

    @Override
    protected BookIconModel categoryIcon() {
        return BookIconModel.create(Items.CHEST);
    }

    @Override
    public String categoryId() {
        return ID;
    }
}
