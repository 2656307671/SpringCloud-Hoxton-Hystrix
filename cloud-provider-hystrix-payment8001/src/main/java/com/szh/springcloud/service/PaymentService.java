package com.szh.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class PaymentService {

    /**
     * 正常访问，一切OK
     */
    public String paymentInfo_OK(Integer id) {
        return "线程池: " + Thread.currentThread().getName() + "paymentInfo_OK,id: " +
                id + "\t" + "O(∩_∩)O";
    }

    /**
     * 服务降级
     * 超时访问，演示降级
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",
                    commandProperties = {
                        @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="5000")
                    })
    public String paymentInfo_TimeOut(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池:" + Thread.currentThread().getName() + "paymentInfo_TimeOut,id: " +
                id + "\t" + "O(∩_∩)O，耗费5秒";
    }
    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池:" + Thread.currentThread().getName() + "paymentInfo_TimeOutHandler,id: " +
                id + "\t" + "o(╥﹏╥)o";
    }

    /**
     * 服务熔断
     */
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",
                    commandProperties = {
                        @HystrixProperty(name = "circuitBreaker.enabled",value = "true"), //是否开启断路器
                        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"), //请求次数
                        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), //时间窗口期
                        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸
                    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("******id 不能是负数");
        }
        String serialNumber = IdUtil.simpleUUID(); //等价于UUID.randomUUID().toString();
        return Thread.currentThread().getName()+"\t"+"调用成功，流水号: " + serialNumber;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~   id: " +id;
    }


}
