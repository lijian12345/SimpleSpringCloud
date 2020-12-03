package cn.jian.provider.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HelloController {
    @Autowired
    private DiscoveryClient client;

    @GetMapping("/hello")
    public String hello() {
        List<ServiceInstance> instances = client.getInstances("hello-service");
        for (int i = 0; i < instances.size(); i++) {
            ServiceInstance instance = instances.get(i);
            log.info(
                    "/hello,host:" + instance.getHost() + ",serivce_id:" + instance.getServiceId());
        }
        return "hello";
    }

    @GetMapping("/sayhello")
    public String sayHello(String name) {
        return "hello " + name;
    }
}
