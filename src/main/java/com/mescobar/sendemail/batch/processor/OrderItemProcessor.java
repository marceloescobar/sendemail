package com.mescobar.sendemail.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.mescobar.sendemail.model.Order;
import com.mescobar.sendemail.service.EmailSenderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderItemProcessor implements ItemProcessor<Order, Order> {

	@Autowired
	private EmailSenderService emailSenderService;
	
	@Override
	public Order process(Order item) throws Exception {
		 log.debug("processor: {}", item);
	        try {
	        	emailSenderService.sendEmail(item.getEmail(), "Your Order has been shipped!", "Thank you for shopping with us");
	        	item.setEmailSent(true);
	        	
	        } catch (Exception sendFailedException) {
	            log.debug("error: {}", sendFailedException.getMessage());
	        }
	        return item;
	}

}
