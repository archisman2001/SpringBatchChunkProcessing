package com.springbatch.ChunkProcessing.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    private Integer productId;
    private String productName;
    private String productCategory;

    @Min(0)
    @Max(10000)
    private Integer productPrice;
}
