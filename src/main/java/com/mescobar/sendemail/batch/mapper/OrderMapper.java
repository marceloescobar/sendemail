package com.mescobar.sendemail.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.mescobar.sendemail.model.Order;

public class OrderMapper implements RowMapper<Order> {

	@Override
	public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
		return Order.builder().orderId(rs.getLong("order_id")).firstName(rs.getString("first_name"))
				.lastName(rs.getString("last_name")).email(rs.getString("email")).cost(rs.getString("cost"))
				.itemId(rs.getString("item_id")).itemName(rs.getString("item_name")).shipDate(rs.getDate("ship_date"))
				.status(rs.getBoolean("status")).emailSent(rs.getBoolean("email_sent")).build();
	}
}
