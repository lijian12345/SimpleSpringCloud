package cn.jian.consumer;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringCloudApplication
public class ConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HystrixCommandAspect hystrixCommandAspect() {
		return new HystrixCommandAspect();
	}
}
