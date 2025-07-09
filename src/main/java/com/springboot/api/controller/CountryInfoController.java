package com.springboot.api.controller;

import com.springboot.api.service.CountryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class CountryInfoController {

    private final CountryInfoService countryInfoService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> getAllInfo(@RequestParam String country) {
        return countryInfoService.getCountryInfo(country, 10)
                .map(ResponseEntity::ok);
    }
}
