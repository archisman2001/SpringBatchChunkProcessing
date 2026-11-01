package com.springbatch.ChunkProcessing.processor;

import com.springbatch.ChunkProcessing.domain.OSProduct;
import com.springbatch.ChunkProcessing.domain.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class MyProductItemProcessor implements ItemProcessor<Product, OSProduct> {

    @Override
    public @Nullable OSProduct process(Product item) throws Exception {
        System.out.println("Process() executed");

        //1. Same pojo transformation -> input and output pojo will be same <Product, Product>, just transforming the data inside it
//        Integer price=item.getProductPrice();
//        item.setProductPrice((int) (price-((0.1)*price))); //reducing price by 10%

        //2. Different pojo transformation
        OSProduct osProduct=new OSProduct();
        osProduct.setProductId(item.getProductId());
        osProduct.setProductName(item.getProductName());
        osProduct.setProductCategory(item.getProductCategory());
        osProduct.setProductPrice(item.getProductPrice());
        osProduct.setTaxPercent(item.getProductCategory().equals("Sports Accessories") ? 5 : 18);
        osProduct.setSku(item.getProductCategory().substring(0, 3) + item.getProductId());
        osProduct.setShippingRate(item.getProductPrice()<1000 ? 75 : 0);

        return osProduct;
    }
}
