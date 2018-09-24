package com.bnp.billing.util;

import com.bnp.billing.exception.BillingInputException;
import com.bnp.billing.repo.ItemRepository;
import com.bnp.billing.serviceImpl.BillingServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class BillGeneratorTest {

    @Test
    public void testAFixedInputWithNoDeals() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest3.csv"))));
        billGenerator.initialiseInput("W.9 A.6");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(2, itemQtyMap.size());
        Assert.assertEquals(9.0, itemQtyMap.get("Melon"), 0);
        Assert.assertEquals(6.0, itemQtyMap.get("Apple"), 0);
    }

    @Test
    public void testAFixedInputWithMixOfDealsAndNoDeals() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.9 A.6 O.9");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(3, itemQtyMap.size());
        Assert.assertEquals(0.60, itemQtyMap.get("Apple"), 0.001);
        Assert.assertEquals(4.80, itemQtyMap.get("Melon"), 0.001);
        Assert.assertEquals(4.50, itemQtyMap.get("Oranges"), 0.001);
    }

    @Test
    public void testAFixedInputWithADealButQtyBoughtNotQualifying() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.2 A.1 O.9");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(3, itemQtyMap.size());
        Assert.assertEquals(0.20, itemQtyMap.get("Apple"), 0.001);
        Assert.assertEquals(1.60, itemQtyMap.get("Melon"), 0.001);
        Assert.assertEquals(4.50, itemQtyMap.get("Oranges"), 0.001);

    }

    @Test
    public void testAFixedInputWithADealButQtyBoughtNotExactMultiple() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.5 A.9 O.9");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(3, itemQtyMap.size());
        Assert.assertEquals(1.00, itemQtyMap.get("Apple"), 0.001);
        Assert.assertEquals(3.20, itemQtyMap.get("Melon"), 0.001);
        Assert.assertEquals(4.50, itemQtyMap.get("Oranges"), 0.001);
    }

    @Test
    public void testAFixedInputWithADealAndQtyBoughtExactMultiple() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.6 A.10 O.9");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(3, itemQtyMap.size());
        Assert.assertEquals(1.00, itemQtyMap.get("Apple"), 0.001);
        Assert.assertEquals(3.20, itemQtyMap.get("Melon"), 0.001);
        Assert.assertEquals(4.50, itemQtyMap.get("Oranges"), 0.001);
    }

    @Test(expected = BillingInputException.class)
    public void testABadInput() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.6 A.10 J.9");
        billGenerator.validateItems();
    }

    @Test
    public void testASpreadInput() {
        BillGenerator billGenerator = new BillGenerator(new BillingServiceImpl(new ItemRepository(new CsvReader("ITEMDBTest1.csv"))));
        billGenerator.initialiseInput("W.2 A.9 O.9 A.2 W.5 W.3");
        Map<String, Double> itemQtyMap = billGenerator.generateBill();
        Assert.assertEquals(3, itemQtyMap.size());
        Assert.assertEquals(1.20, itemQtyMap.get("Apple"), 0.001);
        Assert.assertEquals(5.60, itemQtyMap.get("Melon"), 0.001);
        Assert.assertEquals(4.50, itemQtyMap.get("Oranges"), 0.001);
    }
}
