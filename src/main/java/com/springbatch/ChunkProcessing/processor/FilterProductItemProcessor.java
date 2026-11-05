package com.springbatch.ChunkProcessing.processor;

import com.springbatch.ChunkProcessing.domain.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public @Nullable Product process(Product item) throws Exception {
        System.out.println("filterProductItemProcessor() executed");
        if(item.getProductPrice()>100)
            return item;
        return null;
    }
}
