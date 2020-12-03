package cn.jian.provider;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import cn.jian.Book;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BookController {

    @GetMapping("/getbook1")
    public Book book1() {
        return new Book("三国演义", 90, "罗贯中", "花城出版社");
    }

    @PostMapping("/getbook2")
    public Book book2(@RequestBody Book book) {
        System.out.println(book.getName());
        book.setPrice(33);
        book.setAuthor("曹雪芹");
        book.setPublisher("人民文学出版社");
        return book;
    }

    @PutMapping("/getbook3/{id}")
    public void book3(@RequestBody Book book, @PathVariable int id) {
        log.info("book:" + book);
        log.info("id:" + id);
    }

    @DeleteMapping("/getbook4/{id}")
    public void book4(@PathVariable int id) {
        log.info("id:" + id);
    }
}
