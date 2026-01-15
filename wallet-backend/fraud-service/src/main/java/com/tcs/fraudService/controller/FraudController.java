package com.tcs.fraudService.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.fraudService.dto.FraudDto;
import com.tcs.fraudService.service.FraudService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fraud")
@RequiredArgsConstructor
public class FraudController {

    private final FraudService fraudService;

    @PostMapping("/check")
    public FraudDto checkFraud(@RequestBody FraudDto dto) {
        return fraudService.check(dto);
    }
}
