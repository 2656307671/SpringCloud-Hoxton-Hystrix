package com.szh.springcloud.service;

import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "fallback: paymentInfo_OK, (*^_^*)";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "fallback: paymentInfo_TimeOut, /(ㄒoㄒ)/~~";
    }
}
