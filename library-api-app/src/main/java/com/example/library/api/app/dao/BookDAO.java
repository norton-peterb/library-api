package com.example.library.api.app.dao;

import com.example.library.api.app.bean.Book;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    List<String> getAvailableBooks();
    Optional<Book> getBookForTitle(String title);
    Date performCheckout(Book book, String username);
    List<Book> getCheckedOutBooks();
    List<Book> getOverdueBooks();
}
