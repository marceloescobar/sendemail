package com.mescobar.sendemail.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mescobar.sendemail.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	 Optional<Order> findByStatus(boolean status);
	    List<Order> findByEmailSentAndStatus(boolean emailSent, boolean status);
	    long countByStatus(boolean status);
	    long countByEmailSent(boolean emailSent);
}
