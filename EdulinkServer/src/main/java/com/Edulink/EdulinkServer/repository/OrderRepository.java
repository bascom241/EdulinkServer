package com.Edulink.EdulinkServer.repository;

import com.Edulink.EdulinkServer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order , Long> {
    Order findByReference(String reference);

     List<Order> findByClassroomOwner_UserId(Long instructorId);
}
