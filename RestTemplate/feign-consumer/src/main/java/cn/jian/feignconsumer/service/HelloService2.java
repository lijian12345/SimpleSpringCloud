package cn.jian.feignconsumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import cn.jian.helloserviceapi.service.HelloService;

@FeignClient("hello-service")
public interface HelloService2 extends HelloService {
}
