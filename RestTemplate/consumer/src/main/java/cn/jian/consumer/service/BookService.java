package cn.jian.consumer.service;

import java.util.concurrent.Future;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
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

    @HystrixCommand
    public Book test2() {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
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
}
