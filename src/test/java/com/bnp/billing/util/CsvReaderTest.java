package com.bnp.billing.util;

import com.bnp.billing.exception.BillingInputException;
import com.bnp.billing.model.Item;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CsvReaderTest {
    @Test
    public void readItemsFromCsv() {
        List<Item> items = new CsvReader("ITEMDBTest1.csv").readData();
        Assert.assertEquals(3, items.size());
    }

    @Test(expected = BillingInputException.class)
    public void readBadCsvAndThrowException() {
        List<Item> items = new CsvReader("ITEMDBTest2.csv").readData();
    }
}
