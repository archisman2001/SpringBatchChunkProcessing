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
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

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
    public ItemReader<Product> jdbcCursorItemReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<Product>()
                .name("productJdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("select * from product_details order by product_id")
                .rowMapper(new BeanPropertyRowMapper<>(Product.class))
                .build();
    }

    @Bean
    public ItemReader<Product> jdbcPagingItemReader(DataSource dataSource) throws Exception {
        return new JdbcPagingItemReaderBuilder<Product>()
                .name("productJdbcPagingItemReader")
                .dataSource(dataSource)
                .selectClause("SELECT PRODUCT_ID, PRODUCT_NAME, PRODUCT_CATEGORY, PRODUCT_PRICE")
                .fromClause("FROM PRODUCT_DETAILS")
                .sortKeys(Map.of("PRODUCT_ID", Order.ASCENDING))
                .pageSize(3)
                .rowMapper(new BeanPropertyRowMapper<>(Product.class))
                .build();
    }

    @Bean
    public ItemWriter<Product> flatFileItemWriter(){
        return new FlatFileItemWriterBuilder<Product>()
                .name("productCsvWriter")
                .resource(new FileSystemResource("src/main/resources/data/product_details_output.csv"))
                .delimited()
                .delimiter(",")
                .names("productId", "productName", "productCategory", "productPrice")    //pojo variables
                .headerCallback(writer -> {
                    writer.write("PRODUCT_ID,PRODUCT_NAME,PRODUCT_CATEGORY,PRODUCT_PRICE");
                })
                .build();
    }

    @Bean
    public Step step1() throws Exception {
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

        //2. For csv/flat file and database file chunk

//        return new StepBuilder("ChunkBasedstep1", jobRepository)
//                .<Product, Product>chunk(3)
//                .reader(jdbcPagingItemReader(dataSource))
//                .writer(new ItemWriter<Product>() {
//                    @Override
//                    public void write(Chunk<? extends Product> chunk) throws Exception {
//                        System.out.println("Chunk processing started");
//                        for (Product item : chunk) {
//                            System.out.println("Item: " + item);
//                        }
//                        System.out.println("Chunk processing ended");
//                    }
//                }).build();

        //3. For Writing data to csv file

        return new StepBuilder("ChunkBasedstep1", jobRepository)
                .<Product, Product>chunk(3)
                .reader(jdbcPagingItemReader(dataSource))
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public Job firstJob() throws Exception {
        return new JobBuilder("job1", jobRepository)
                .start(step1())
                .build();
    }
}
