package cn.jian.provider.controller;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/getbook5/{id}")
    public Book book5(@PathVariable("id") Integer id) {
        System.out.println(">>>>>>>>/getbook5/{id}");
        if (id == 1) {
            return new Book("《李自成》", 55, "姚雪垠", "人民文学出版社");
        } else if (id == 2) {
            return new Book("中国文学简史", 33, "林庚", "清华大学出版社");
        }
        return new Book("文学改良刍议", 33, "胡适", "无");
    }

    @GetMapping("/getbook6")
    public List<Book> book6(String ids) {
        System.out.println("ids>>>>>>>>>>>>>>>>>>>>>" + ids);
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("《李自成》", 55, "姚雪垠", "人民文学出版社"));
        books.add(new Book("中国文学简史", 33, "林庚", "清华大学出版社"));
        books.add(new Book("文学改良刍议", 33, "胡适", "无"));
        books.add(new Book("ids", 22, "helloworld", "haha"));
        return books;
    }

    @GetMapping("/getbook6/{id}")
    public Book book61(@PathVariable Integer id) {
        Book book = new Book("《李自成》2", 55, "姚雪垠2", "人民文学出版社2");
        return book;
    }
}
