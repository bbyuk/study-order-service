package com.polarbookshop.orderservice.order.web;

import com.polarbookshop.orderservice.order.domain.Order;
import com.polarbookshop.orderservice.order.domain.OrderService;
import com.polarbookshop.orderservice.order.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class) // OrderController를 대상으로 한 스프링 웹플럭스 컴포넌트에 집중하는 테스트 클래스임을 나타낸다.
class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private OrderService orderService;

    @Test
    @DisplayName("책이 이용가능하지 않을 경우 주문을 reject")
    void whenBookNotAvailableThenRejectOrder() {
        var orderRequest = new OrderRequest("1234567890", 3);
        var expectedOrder = OrderService.buildRejectedOrder(orderRequest.isbn(), orderRequest.quantity());

        given(orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity()))
                .willReturn(Mono.just(expectedOrder));  // OrderService 모의 빈이 어떻게 작동해야 하는지 지정한다.

        webClient
                .post()
                .uri("/orders/")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful() // 주문이 성공적으로 생성될 것을 예상한다.
                .expectBody(Order.class).value(actualOrder -> {
                    Assertions.assertThat(actualOrder).isNotNull();
                    Assertions.assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
                });

    }

}