package cn.jian.provider;

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
    public String index() {
        List<ServiceInstance> instances = client.getInstances("hello-service");
        for (int i = 0; i < instances.size(); i++) {
            log.info("/hello,host:" + instances.get(i).getHost() + ",service_id:"
                    + instances.get(i).getServiceId());
        }
        return "Hello World";
    }
}
