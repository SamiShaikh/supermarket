package com.bnp.billing.repo;

import com.bnp.billing.model.Item;

import java.util.List;

public interface Reader {

    List<Item> readData();
}
