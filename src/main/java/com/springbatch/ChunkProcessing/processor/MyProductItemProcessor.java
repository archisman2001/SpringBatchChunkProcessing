package com.springbatch.ChunkProcessing.processor;

import com.springbatch.ChunkProcessing.domain.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class MyProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public @Nullable Product process(Product item) throws Exception {
        System.out.println("Process() executed");
        Integer price=item.getProductPrice();
        item.setProductPrice((int) (price-((0.1)*price))); //reducing price by 10%
        return item;
    }
}
