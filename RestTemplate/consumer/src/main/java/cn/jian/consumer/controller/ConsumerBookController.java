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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import cn.jian.Book;
import cn.jian.consumer.command.BookCollapseCommand;
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

    @RequestMapping("/test7")
    @ResponseBody
    public void test7() throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        BookCollapseCommand bc1 = new BookCollapseCommand(bookService, 1l);
        BookCollapseCommand bc2 = new BookCollapseCommand(bookService, 2l);
        BookCollapseCommand bc3 = new BookCollapseCommand(bookService, 3l);
        BookCollapseCommand bc4 = new BookCollapseCommand(bookService, 4l);
        Future<Book> q1 = bc1.queue();
        Future<Book> q2 = bc2.queue();
        Future<Book> q3 = bc3.queue();
        Book book1 = q1.get();
        Book book2 = q2.get();
        Book book3 = q3.get();
        Thread.sleep(3000);
        Future<Book> q4 = bc4.queue();
        Book book4 = q4.get();
        System.out.println("book1>>>" + book1);
        System.out.println("book2>>>" + book2);
        System.out.println("book3>>>" + book3);
        System.out.println("book4>>>" + book4);
        context.close();
    }

    @RequestMapping("/test8")
    @ResponseBody
    public void test8() throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        Future<Book> f1 = bookService.test10(1l);
        Future<Book> f2 = bookService.test10(2l);
        Future<Book> f3 = bookService.test10(3l);
        Book b1 = f1.get();
        Book b2 = f2.get();
        Book b3 = f3.get();
        Thread.sleep(3000);
        Future<Book> f4 = bookService.test10(4l);
        Book b4 = f4.get();
        System.out.println("b1>>>" + b1);
        System.out.println("b2>>>" + b2);
        System.out.println("b3>>>" + b3);
        System.out.println("b4>>>" + b4);
        context.close();
    }
}
