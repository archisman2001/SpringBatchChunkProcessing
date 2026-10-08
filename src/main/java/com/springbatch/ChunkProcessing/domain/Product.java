package com.springbatch.ChunkProcessing.domain;

import lombok.Data;

@Data
public class Product {
    private Integer productId;
    private String productName;
    private String productCategory;
    private Integer productPrice;
}
