package com.example.technical_specification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("api/markPrice")
@RequiredArgsConstructor
public class MarkPriceController {

    private final WebClient webClient;

    @Value("${application.binance.api.host}")
    private String HOST;

    @GetMapping()
    public Mono<ResponseEntity<String>> getMarkPrice(@RequestParam(name = "symbol", required = false) String symbol) {
        String url = UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host(HOST)
                .path("/fapi/v1/premiumIndex")
                .queryParamIfPresent("symbol", Optional.ofNullable(symbol))
                .build()
                .toUriString();


        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.empty())
                .toEntity(String.class);
    }
}
