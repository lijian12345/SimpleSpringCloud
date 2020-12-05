package cn.jian.feignconsumer.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.jian.Book;
import cn.jian.feignconsumer.service.HelloService2;

@RestController
public class FeignConsumerController {
    @Autowired
    HelloService2 helloService2;

    @GetMapping("/hello1")
    public String hello1() {
        return helloService2.hello("张三");
    }

    @GetMapping("/hello2")
    public Book hello2() throws UnsupportedEncodingException {
        Book book = helloService2.hello(URLEncoder.encode("三国演义", "UTF-8"),
                URLEncoder.encode("罗贯中", "UTF-8"), 33);
        System.out.println(book);
        return book;
    }

    @GetMapping("/hello3")
    public String hello3() {
        Book book = new Book();
        book.setName("红楼梦");
        book.setPrice(44);
        book.setAuthor("曹雪芹");
        return helloService2.hello(book);
    }
}
