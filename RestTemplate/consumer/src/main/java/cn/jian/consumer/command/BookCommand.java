package cn.jian.consumer.command;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;
import cn.jian.Book;

public class BookCommand extends HystrixCommand<Book> {
    private RestTemplate restTemplate;

    public BookCommand(Setter setter, RestTemplate restTemplate) {
        super(setter);
        this.restTemplate = restTemplate;
    }

    @Override
    protected Book run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
    }

    @Override
    protected Book getFallback() {
        return new Book("宋诗选注", 88, "钱钟书", "三联书店");
    }
}
