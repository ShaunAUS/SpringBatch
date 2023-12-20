package com.example.springBatch.core.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
