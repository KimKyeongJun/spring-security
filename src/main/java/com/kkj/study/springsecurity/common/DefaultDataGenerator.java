package com.kkj.study.springsecurity.common;

import com.kkj.study.springsecurity.account.Account;
import com.kkj.study.springsecurity.account.AccountService;
import com.kkj.study.springsecurity.book.Book;
import com.kkj.study.springsecurity.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataGenerator implements ApplicationRunner {

    @Autowired
    AccountService accountService;
    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Account user = createUser("user");
        Account user1 = createUser("user1");

        createBook("spring", user1);

    }

    private void createBook(String bookName , Account user1) {
        Book book = new Book();
        book.setTitle(bookName);
        book.setAuthor(user1);
        bookRepository.save(book);
    }

    private Account createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("123");
        account.setRole("USER");
        return accountService.createNew(account);
    }
}
