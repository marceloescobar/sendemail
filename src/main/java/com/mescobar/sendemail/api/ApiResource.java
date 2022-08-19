package com.mescobar.sendemail.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mescobar.sendemail.model.Order;
import com.mescobar.sendemail.repository.OrderRepository;
import com.mescobar.sendemail.service.EmailSenderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping(path = "api/v1/orders")
@Slf4j
public class ApiResource {
	
	 @Autowired
	    private EmailSenderService emailService;
	 
	    @Autowired
	    private JobLauncher jobLauncher;
	    
	    @Autowired
	    private OrderRepository orderRepository;
	    
	    @Autowired
	    @Qualifier("emailSenderJob")
	    private Job emailSenderJob;
	    

	  @GetMapping("/testEnvioEmail")
	    public ResponseEntity<ResponseMessage> getAllOrders() {
	        try {
	        	
	            emailService.sendEmail("hello@world.com", "This is the message", "Thank you for registering with us");
	            
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("failed to send email"));
	        }
	        
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("email sent"));
	    }
	  
	  
	  @GetMapping("/count")
	    public ResponseEntity<OrderResponse> getTotalOrder() {
	        long ordersCount = orderRepository.count();

	        OrderResponse response = new OrderResponse();
	        response.setMessage("success");
	        HashMap<String, Long> count = new HashMap<>();
	        count.put("total", ordersCount);
	        response.setResponse(count);

	        return  ResponseEntity.status(HttpStatus.OK).body(response);
	    }
	  
	  @GetMapping("/email-sent")
	    public ResponseEntity<OrderResponse> getEmailsSent() {
	        long ordersCount = orderRepository.countByEmailSent(true);

	        OrderResponse response = new OrderResponse();
	        response.setMessage("success");
	        HashMap<String, Long> count = new HashMap<>();
	        count.put("total", ordersCount);
	        response.setResponse(count);

	        return  ResponseEntity.status(HttpStatus.OK).body(response);
	    }
	  
	  @PostMapping("/send/notification")
	    public ResponseEntity<ResponseMessage> sendEmails() {
	        Random random = new Random();
	        int randomWithNextInt = random.nextInt();

	        JobParameter param = new JobParameter(String.valueOf(randomWithNextInt));
	        JobParameters jobParameters = new JobParametersBuilder().addParameter("unique", param).toJobParameters();
	        List<Order> emailNotSentOrders = orderRepository.findByEmailSentAndStatus(false, true);

	        if (emailNotSentOrders.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Nothing to send"));
	        }

	        try {
	        	
	            final JobExecution jobExecution = jobLauncher.run(emailSenderJob, jobParameters);
	                Date create = jobExecution.getStartTime();
	                Date end = jobExecution.getEndTime();
	                int diff = end.getSeconds() - create.getSeconds();
	                log.debug("difference = {}", diff);
	                
	                TimeUnit.SECONDS.sleep(diff);
	                
	                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("success"));
	                
	        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobParametersInvalidException |InterruptedException | JobRestartException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
	        }
	    }

}
