package com.bnp.billing.serviceImpl;

import com.bnp.billing.exception.BillingInputException;
import com.bnp.billing.model.Item;
import com.bnp.billing.repo.ItemRepository;
import com.bnp.billing.service.BillingService;
import com.bnp.billing.util.CsvReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BillingServiceImpl implements BillingService {
    private final ItemRepository itemRepository;

    public BillingServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public BillingServiceImpl() {
        this.itemRepository = new ItemRepository(new CsvReader());
    }

    @Override
    public Map<String, Double> deriveBill(Map<String, Integer> items) {
        Map<String, Double> bill = new HashMap<>();
        Map<String, Item> itemMap = itemRepository.getAllItems().stream()
                .collect(Collectors.toMap(Item::getItemCode, Function.identity()));
        items.forEach((key, value) -> {
            Item item = itemMap.get(key);
            Integer qty = value;
            String deal = item.getDeal();
            Double price = item.getPrice() * qty;
            if (deal != null && deal.length() > 0) {
                String[] splits = deal.split("/");
                Integer getQty = Integer.parseInt(splits[0]);
                Integer forQty = Integer.parseInt(splits[1]);
                if (qty > getQty && !getQty.equals(forQty)) {
                    int discountedQty = ((qty/getQty) * forQty) + (qty % getQty);
                    price = item.getPrice() * discountedQty;
                }
            }
            bill.put(item.getItemName(), price);
        });
        return bill;
    }

    @Override
    public void validateItems(List<String> itemList) {
        Map<String, Item> itemMap = itemRepository.getAllItems().stream()
                .collect(Collectors.toMap(Item::getItemCode, Function.identity()));
        itemList.forEach(item -> {
            if (itemMap.get(item) == null) {
                throw new BillingInputException(String.format(" Bad item %s", item));
            }
        });
    }

}
