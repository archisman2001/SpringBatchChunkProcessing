package com.springbatch.ChunkProcessing.listener;

import com.springbatch.ChunkProcessing.domain.OSProduct;
import com.springbatch.ChunkProcessing.domain.Product;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;

import java.io.FileWriter;
import java.io.IOException;

public class MySkipListener implements SkipListener<Product, OSProduct> {
    @Override
    public void onSkipInRead(Throwable t) {
        if(t instanceof FlatFileParseException){
            try {
                System.out.println("Skipped item :-");
                System.out.println(((FlatFileParseException) t).getInput());
                writeToFile(((FlatFileParseException) t).getInput());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onSkipInWrite(OSProduct item, Throwable t) {
        SkipListener.super.onSkipInWrite(item, t);
    }

    @Override
    public void onSkipInProcess(Product item, Throwable t) {
        System.out.println("Skipped item :-");
        System.out.println(item);
        try {
            writeToFile(item.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String data) throws IOException {
        FileWriter fileWriter=new FileWriter("src/main/resources/rejected/product_details_rejected.txt", true);
        fileWriter.write(data+"\n");
        fileWriter.close();
    }
}
