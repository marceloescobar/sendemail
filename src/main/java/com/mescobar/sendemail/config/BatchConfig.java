package com.mescobar.sendemail.config;

import java.util.Random;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mescobar.sendemail.batch.mapper.OrderMapper;
import com.mescobar.sendemail.batch.processor.OrderItemProcessor;
import com.mescobar.sendemail.batch.writer.OrderWriter;
import com.mescobar.sendemail.model.Order;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	 @Autowired
	   JobLauncher jobLauncher;
	 
	 private final String JOB_NAME = "emailSenderJob";
	    private final String STEP_NAME = "emailSenderStep";
	
	    @Bean(name = "emailSenderJob")
	    public Job emailSenderJob() {
	    	Random random = new Random();
	        int randomWithNextInt = random.nextInt();
	        return this.jobBuilderFactory.get(JOB_NAME+randomWithNextInt)
	                .start(emailSenderStep())
	                .build();
	    }
	    
	    @Bean
	    public Step emailSenderStep() {
	        return this.stepBuilderFactory
	                .get(STEP_NAME)
	                .<Order, Order>chunk(100)
	                .reader(activeOrderReader())
	                .processor(orderItemProcessor())
	                .writer(orderWriter())
	                .build();
	    }
	    
	    @Bean
	    public ItemProcessor<Order, Order> orderItemProcessor() {
	        return new OrderItemProcessor();
	    }
	    
	    @Bean
	    public ItemWriter<Order> orderWriter() {
	        return new OrderWriter();
	    }
	    
	    @Bean
	    public ItemReader<Order> activeOrderReader() {
	        String sql = "SELECT * FROM shipped_order WHERE status=1 and email_sent=0";
	        
	        return new JdbcCursorItemReaderBuilder<Order>()
	                .name("activeOrderReader")
	                .sql(sql)
	                .dataSource(dataSource)
	                .rowMapper(new OrderMapper())
	                .build();
	    }
	    
	
	
}
