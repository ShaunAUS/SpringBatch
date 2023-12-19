package com.example.springBatch.core.domain.accounts;

import java.util.Date;

import com.example.springBatch.core.domain.orders.Orders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Accounts {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String orderItem;
	private Date orderDate;
	private Date accountDate;

	public Accounts(Orders orders) {
		this.id = orders.getId();
		this.orderItem = orders.getOrderItem();
		this.orderDate = orders.getOrderDate();
		this.accountDate = new Date();
	}
}
