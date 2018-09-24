package com.bnp.billing.util;

import com.bnp.billing.exception.BillingInputException;
import com.bnp.billing.model.Item;
import com.bnp.billing.repo.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Repository for Pricing and deal information
 */

public class CsvReader implements Reader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReader.class);

    public static final String DELIMITER = ",";

    private final String inputFileName;

    public CsvReader(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public CsvReader() {
        this.inputFileName = "ITEMDB.csv";
    }

    @Override
    public List<Item> readData() {
        List<Item> inputList;
        URL resource = this.getClass().getResource("/" + this.inputFileName);
        File inputFile;
        InputStream inputFileStream;
        BufferedReader br = null;
        try {
            inputFile = new File(resource.toURI().getPath());
            inputFileStream = new FileInputStream(inputFile);
            br = new BufferedReader(new InputStreamReader(inputFileStream));
            inputList = br.lines().map(
                    line -> {
                        String[] params = line.split(DELIMITER);
                        return Item.builder().itemCode(params[0])
                                .itemName(params[1])
                                .price(new Double(params[2]))
                                .deal(params[3]).build();
                    }
            ).collect(Collectors.toList());
        } catch(Exception e) {
            throw new BillingInputException("Error reading input");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch(Exception e) {
                    LOGGER.error("Error closing file", e);
                }
            }
        }
        return inputList ;
    }
}
