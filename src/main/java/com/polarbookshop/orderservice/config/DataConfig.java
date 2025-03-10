package com.polarbookshop.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration // 이 클래스가 스프링 설정을 위한 클래스임을 나타낸다.
@EnableR2dbcAuditing // persistence entity에 대해 auditing을 활성화한다.
public class DataConfig {
}
