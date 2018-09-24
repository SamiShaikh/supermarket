package com.bnp.billing.util;

import com.bnp.billing.exception.BillingInputException;
import com.bnp.billing.service.BillingService;
import com.bnp.billing.serviceImpl.BillingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This is the main driving class.
 * Initialises input and does validation on item codes
 * Generates Bill
 */

public class BillGenerator {
    public static final Logger LOGGER = LoggerFactory.getLogger(BillGenerator.class);
    private BillingService billingService;
    private List<String> billingItems = new ArrayList<>();
    private Map<String, Integer> itemQtyMap = new HashMap<>();

    BillGenerator(BillingService billingService) {
        this.billingService = billingService;
    }

    public static void main(String args[]) {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl());
        billGenerator.initialiseInput(args[0]);
        billGenerator.validateItems();
        Map<String, Double> billingData = billGenerator.generateBill();
        LOGGER.info("Billing Basket : ");
        billingData.forEach(
                (key, value) -> LOGGER.info(String.format("Item : %s, Price : %.2f", key, value))
        );
        LOGGER.info(String.format("Total : %.2f", billingData.entrySet().stream().mapToDouble(entry -> entry.getValue()).sum()));
    }

    public void validateItems() {
        billingService.validateItems(billingItems);
    }

    public Map<String, Double> generateBill() {
        return billingService.deriveBill(itemQtyMap);
    }

    public void initialiseInput(String arg) {
        Pattern pattern = Pattern.compile("[A-Z]+\\.[1-9]+[0-9]*([ ][A-Z]+\\.[1-9]+[0-9]*)*");
        Pattern individualPattern = Pattern.compile("[A-Z]+\\.[1-9]+[0-9]*");
        if (!pattern.matcher(arg).matches()) {
            throw new BillingInputException("Input should be of format <ItemCode>.<Qty>");
        }
        Matcher m = individualPattern.matcher(arg);
        while (m.find()) {
            String itemWithQty = m.group(0);
            String[] splits = itemWithQty.split("\\.");
            billingItems.add(splits[0]);
            if (itemQtyMap.get(splits[0]) != null) {
                itemQtyMap.put(splits[0], itemQtyMap.get(splits[0]) + Integer.parseInt(splits[1]));
            } else {
                itemQtyMap.put(splits[0], Integer.parseInt(splits[1]));
            }
        }
    }
}
