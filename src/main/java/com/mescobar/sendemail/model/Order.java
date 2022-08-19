package com.mescobar.sendemail.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity(name = "shipped_order")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long orderId;
	private String firstName;
	private String lastName;
	private String email;
	private String cost;
	private String itemId;
	private String itemName;
	private Date shipDate;
	private boolean status;
	private boolean emailSent;
}
