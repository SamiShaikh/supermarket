package com.bnp.billing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Item {
    /*
     * For high precision like currencies, use BigDecimal instead of double and initialise with string
     */

    private String itemCode;
    private String itemName;
    private Double price;
    private String deal;
}
