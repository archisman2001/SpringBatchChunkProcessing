package com.springbatch.ChunkProcessing.reader;


import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class ProductNameItemReader implements ItemReader<String> {
    Iterator<String> productListIterator;

    public ProductNameItemReader(List<String> productList){
        this.productListIterator=productList.iterator();
    }
    @Override
    public @Nullable String read() throws Exception {
        return this.productListIterator.hasNext() ? this.productListIterator.next() : null;
    }
}
