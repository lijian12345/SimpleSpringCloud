package cn.jian.provider.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.jian.Book;
import cn.jian.helloserviceapi.service.HelloService;

@RestController
public class BookController2 implements HelloService {
    @Override
    public String hello(@RequestParam("name") String name) {
        return "hello " + name + "!";
    }

    @Override
    public Book hello(@RequestHeader("name") String name, @RequestHeader("author") String author,
            @RequestHeader("price") Integer price) throws UnsupportedEncodingException {
        Book book = new Book();
        book.setName(URLDecoder.decode(name, "UTF-8"));
        book.setAuthor(URLDecoder.decode(author, "UTF-8"));
        book.setPrice(price);
        System.out.println(book);
        return book;
    }

    @Override
    public String hello(@RequestBody Book book) {
        return "书名为：" + book.getName() + ";作者为：" + book.getAuthor();
    }
}
