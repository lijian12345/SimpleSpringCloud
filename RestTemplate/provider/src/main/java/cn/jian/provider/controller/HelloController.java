package cn.jian.provider.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.jian.Book;
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

    @GetMapping("/hello1")
    public String hello1(@RequestParam String name) {
        return "hello " + name + "!";
    }

    @GetMapping("/hello2")
    public Book hello2(@RequestHeader String name, @RequestHeader String author,
            @RequestHeader Integer price) throws UnsupportedEncodingException {
        Book book = new Book();
        book.setName(URLDecoder.decode(name, "UTF-8"));
        book.setAuthor(URLDecoder.decode(author, "UTF-8"));
        book.setPrice(price);
        System.out.println(book);
        return book;
    }

    @GetMapping("/hello3")
    public String hello3(@RequestBody Book book) {
        return "书名为：" + book.getName() + ";作者为：" + book.getAuthor();
    }
}
