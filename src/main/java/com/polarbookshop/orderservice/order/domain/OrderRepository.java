package com.polarbookshop.orderservice.order.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    // CRUD 연산을 제공하는 리액티브 리포지토리가 관리할 엔티티의 타입과 해당 엔티티의 기본키 타입을 지정하고 상속받는다.
}
