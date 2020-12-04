package cn.jian.consumer.command;

import java.util.List;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import cn.jian.Book;
import cn.jian.consumer.service.BookService;

public class BookBatchCommand extends HystrixCommand<List<Book>> {
    private List<Long> ids;
    private BookService bookService;

    public BookBatchCommand(List<Long> ids, BookService bookService) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CollapsingGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CollapsingKey")));
        this.ids = ids;
        this.bookService = bookService;
    }

    @Override
    protected List<Book> run() throws Exception {
        return bookService.test9(ids);
    }
}
