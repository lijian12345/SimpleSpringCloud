package cn.jian.consumer.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cn.jian.Book;
import rx.Observable;
import rx.Subscriber;

@Service
public class BookService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "error1")
    public Book test2() {
        // int i = 1 / 0;
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
    }

    @HystrixCommand(fallbackMethod = "error2")
    public Book error1(Throwable throwable) {
        System.out.println(throwable.getMessage());
        //发起某个网络请求（可能失败）
        return null;
    }

    public Book error2() {
        return new Book();
    }

    @HystrixCommand
    public Future<Book> test3() {
        return new AsyncResult<Book>() {
            @Override
            public Book invoke() {
                return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
            }
        };
    }

    @HystrixCommand
    public Observable<Book> test4() {
        return Observable.create(new Observable.OnSubscribe<Book>() {
            @Override
            public void call(Subscriber<? super Book> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    Book book =
                            restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
                    subscriber.onNext(book);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @CacheResult
    @HystrixCommand
    public Book test6(@CacheKey Integer id, String aa) {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class, id);
    }

    public Book test8(Long id) {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook6/{1}", Book.class, id);
    }

    public List<Book> test9(List<Long> ids) {
        System.out.println("test9---------" + ids + "Thread.currentThread().getName():"
                + Thread.currentThread().getName());
        Book[] books = restTemplate.getForObject("http://HELLO-SERVICE/getbook6?ids={1}",
                Book[].class, StringUtils.join(ids, ","));
        return Arrays.asList(books);
    }
    
    @HystrixCollapser(batchMethod = "test11", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")})
    public Future<Book> test10(Long id) {
        return null;
    }

    @HystrixCommand
    public List<Book> test11(List<Long> ids) {
        System.out.println("test9---------" + ids + "Thread.currentThread().getName():"
                + Thread.currentThread().getName());
        Book[] books = restTemplate.getForObject("http://HELLO-SERVICE/getbook6?ids={1}",
                Book[].class, StringUtils.join(ids, ","));
        return Arrays.asList(books);
    }
}
