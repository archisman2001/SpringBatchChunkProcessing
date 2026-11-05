package com.springbatch.ChunkProcessing.domain;

import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.batch.infrastructure.item.validator.Validator;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

public class ProductValidator implements Validator<Product> {
    List<String> validProductCategories = Arrays.asList("Mobile Phones", "Tablets", "Televisions", "Sports Accessories");
    @Override
    public void validate(Product value) throws ValidationException {
        System.out.println("ProductValidator() executed");
        if(!validProductCategories.contains(value.getProductCategory())){
            throw new ValidationException("Invalid Product Category");
        }

        if(value.getProductPrice()>100000){
            throw new ValidationException("Invalid product price");
        }
    }
}
