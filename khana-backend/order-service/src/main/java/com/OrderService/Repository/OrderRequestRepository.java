package com.OrderService.Repository;

import com.OrderService.Entity.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest, Long> {

     @EntityGraph(attributePaths = {"orderItems"})
     Page<OrderRequest> findAllByUserEmail(String email, Pageable pageable);

     OrderRequest findByIdempotencyKey(String idempotencyKey);
}
