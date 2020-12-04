package cn.jian.consumer.command;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;
import cn.jian.Book;

public class BookCommand1 extends HystrixCommand<Book> {
    private RestTemplate restTemplate;
    private Long id;

    public BookCommand1(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }


    @Override
    protected Book run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class, id);
    }

    @Override
    protected Book getFallback() {
        Throwable executionException = getExecutionException();
        System.out.println(executionException.getMessage());
        return new Book("宋诗选注", 88, "钱钟书", "三联书店");
    }


    @Override
    protected String getCacheKey() {
        return String.valueOf(id);}
}
