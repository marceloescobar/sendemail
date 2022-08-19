package com.mescobar.sendemail.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.mescobar.sendemail.model.Order;
import com.mescobar.sendemail.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderWriter implements ItemWriter<Order> {

	@Autowired
	private OrderRepository orderRepository;

	public void write(List<? extends Order> items) throws Exception {
		log.debug("item writer: {}", items.get(0));
		orderRepository.saveAllAndFlush(items);
	}

}
