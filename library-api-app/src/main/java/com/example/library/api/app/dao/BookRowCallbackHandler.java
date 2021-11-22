package com.example.library.api.app.dao;

import com.example.library.api.app.bean.Book;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookRowCallbackHandler implements RowCallbackHandler {

    private final List<Book> bookResults = new LinkedList<>();

    @Override
    public void processRow(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setTitle(resultSet.getString(1));
        book.setUsername(resultSet.getString(2));
        book.setDueDate(resultSet.getDate(3));
        bookResults.add(book);
    }

    public List<Book> getBookResults() {
        return bookResults;
    }
}
