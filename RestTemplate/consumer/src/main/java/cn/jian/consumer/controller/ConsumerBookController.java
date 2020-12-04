package cn.jian.consumer.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cn.jian.Book;
import cn.jian.consumer.command.BookCommand;
import cn.jian.consumer.command.BookCommand1;
import cn.jian.consumer.service.BookService;

@RestController
public class ConsumerBookController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    BookService bookService;

    @GetMapping("/test1")
    public Book test1() throws ExecutionException, InterruptedException {
        BookCommand bookCommand = new BookCommand(
                HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")),
                restTemplate);
        //同步调用
        //Book book1 = bookCommand.execute();
        //异步调用
        Future<Book> queue = bookCommand.queue();
        Book book = queue.get();
        return book;
    }

    @GetMapping("/test3")
    public Book test3() throws ExecutionException, InterruptedException {
        Future<Book> bookFuture = bookService.test3();
        //调用get方法时也可以设置超时时长
        return bookFuture.get();
    }

    @GetMapping("/test5")
    public Book test5() {
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("commandKey");
        HystrixRequestContext.initializeContext();
        BookCommand1 bc1 = new BookCommand1(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey),
                restTemplate, 1l);
        Book e1 = bc1.execute();
        HystrixRequestCache.getInstance(commandKey, HystrixConcurrencyStrategyDefault.getInstance())
                .clear(String.valueOf(1l));
        BookCommand1 bc2 = new BookCommand1(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey),
                restTemplate, 1l);
        Book e2 = bc2.execute();
        BookCommand1 bc3 = new BookCommand1(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey),
                restTemplate, 1l);
        Book e3 = bc3.execute();
        System.out.println("e1:" + e1);
        System.out.println("e2:" + e2);
        System.out.println("e3:" + e3);
        return e1;
    }

    @GetMapping("/test6")
    public Book test6() {
        HystrixRequestContext.initializeContext();
        //第一次发起请求
        Book b1 = bookService.test6(2, "");
        //参数和上次一致，使用缓存数据
        Book b2 = bookService.test6(2, "");
        //参数不一致，发起新请求
        Book b3 = bookService.test6(2, "aa");
        return b1;
    }

    @GetMapping("/book1")
    public Book book1() {
        ResponseEntity<Book> responseEntity =
                restTemplate.getForEntity("http://HELLO-SERVICE/getbook1", Book.class);
        return responseEntity.getBody();
    }

    @GetMapping("/book2")
    public Book book2() {
        Book book = restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
        return book;
    }

    @GetMapping("/book3")
    public Book book3() {
        Book book = new Book();
        book.setName("红楼梦");
        ResponseEntity<Book> responseEntity =
                restTemplate.postForEntity("http://HELLO-SERVICE/getbook2", book, Book.class);
        return responseEntity.getBody();
    }

    @GetMapping("/put")
    public void put() {
        Book book = new Book();
        book.setName("红楼梦");
        restTemplate.put("http://HELLO-SERVICE/getbook3/{1}", book, 99);
    }

    @GetMapping("/delete")
    public void delete() {
        restTemplate.delete("http://HELLO-SERVICE/getbook4/{1}", 100);
    }
}
