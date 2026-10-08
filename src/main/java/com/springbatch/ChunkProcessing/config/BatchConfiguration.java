package com.springbatch.ChunkProcessing.config;

import com.springbatch.ChunkProcessing.domain.Product;
import com.springbatch.ChunkProcessing.reader.ProductNameItemReader;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

//	@Autowired
//	public JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<String> itemReader(){
        List<String> productList=new ArrayList<>();
        productList.add("Product 1");
        productList.add("Product 2");
        productList.add("Product 3");
        productList.add("Product 4");
        productList.add("Product 5");
        productList.add("Product 6");
        productList.add("Product 7");
        productList.add("Product 8");
        return new ProductNameItemReader(productList);
    }

    @Bean
    public ItemReader<Product> flatFileItemReader(){
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("/data/product_details.csv"))
                .delimited()
                    .names("product_id", "product_name", "product_category", "product_price")
                .targetType(Product.class)
                .linesToSkip(1) //to skip header names in csv file
                .build();
    }

    @Bean
    public Step step1(){
        //1. For List of string based chunk

//        return new StepBuilder("ChunkBasedstep1", jobRepository)
//                .<String, String>chunk(3)
//                .reader(itemReader())
//                .writer(new ItemWriter<String>() {
//                    @Override
//                    public void write(Chunk<? extends String> chunk) throws Exception {
//                        System.out.println("Chunk processing started");
//                        for (String item : chunk) {
//                            System.out.println("Item: " + item);
//                        }
//                        System.out.println("Chunk processing ended");
//                    }
//                }).build();

        //2. For csv/flat file chunk

        return new StepBuilder("ChunkBasedstep1", jobRepository)
                .<Product, Product>chunk(3)
                .reader(flatFileItemReader())
                .writer(new ItemWriter<Product>() {
                    @Override
                    public void write(Chunk<? extends Product> chunk) throws Exception {
                        System.out.println("Chunk processing started");
                        for (Product item : chunk) {
                            System.out.println("Item: " + item);
                        }
                        System.out.println("Chunk processing ended");
                    }
                }).build();
    }

    @Bean
    public Job firstJob(){
        return new JobBuilder("job1", jobRepository)
                .start(step1())
                .build();
    }
}
