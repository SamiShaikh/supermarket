package com.bnp.billing.repo;

import com.bnp.billing.model.Item;

import java.util.List;

/*
 * Item Repo to read item info
 */
public class ItemRepository {
    private final Reader reader;

    public ItemRepository(Reader reader) {
        this.reader = reader;
    }

    public List<Item> getAllItems() {
        return this.reader.readData();
    }
}
