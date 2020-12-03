package cn.jian.consumer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class HelloController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HelloService helloService;

    @GetMapping("/consumer")
    public String helloController() {
        return helloService.hello();
    }

    @GetMapping("/gethello")
    public String getHello() {
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class);
        String body = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();
        StringBuffer result = new StringBuffer();
        result.append("responseEntity.getBody()：").append(body).append("<hr>")
                .append("responseEntity.getStatusCode()：").append(statusCode).append("<hr>")
                .append("responseEntity.getStatusCodeValue()：").append(statusCodeValue)
                .append("<hr>").append("responseEntity.getHeaders()：").append(headers)
                .append("<hr>");
        return result.toString();
    }

    @GetMapping("/sayhello")
    public String sayHello() {
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity("http://HELLO-SERVICE/sayhello?name={1}", String.class, "张三");
        return responseEntity.getBody();
    }

    @GetMapping("/sayhello2")
    public String sayHello2() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "李四");
        ResponseEntity<String> responseEntity = restTemplate
                .getForEntity("http://HELLO-SERVICE/sayhello?name={name}", String.class, map);
        return responseEntity.getBody();
    }

    @GetMapping("/sayhello3")
    public String sayHello3() {
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString("http://HELLO-SERVICE/sayhello?name={name}")
                        .build().expand("王五").encode();
        URI uri = uriComponents.toUri();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        return responseEntity.getBody();
    }
}
