package com.polarbookshop.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {

    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get() // GET 메소드
                .uri(BOOKS_ROOT_API + isbn) // /books/{isbn}
                .retrieve()     // 응답을 받는다.
                .bodyToMono(Book.class) //     받은 객체를 Mono<Book>으로 리턴
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty()) // 404를 받으면 빈객체를 리턴한다.
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100))) // GET 요청에 대해 3초의 타임아웃을 설정한다. 폴백으로 빈 Mono 객체를 리턴
                .onErrorResume(Exception.class, exception -> Mono.empty());  // 3회의 재시도 동안 오류가 발생하면 예외를 포착하고 빈 객체를 반환한다.
    }

}
