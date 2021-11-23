package com.example.library.api.app.dao;

import com.example.library.api.app.bean.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.*;

@Component
public class BookDAOImpl implements BookDAO {

    private static final String SQL_GET_AVAILABLE_BOOKS =
            "SELECT title FROM book WHERE user_login_id IS NULL";
    private static final String SQL_GET_BOOK_FOR_TITLE =
            "SELECT b.title,ul.username,b.due_date FROM book b " +
                    "LEFT JOIN user_login ul ON b.user_login_id = ul.id " +
                    "WHERE b.title = ?";
    private static final String SQL_GET_USER_ID =
            "SELECT id FROM user_login WHERE username = ?";
    private static final String SQL_APPLY_CHECKOUT =
            "UPDATE book SET user_login_id = ?,due_date = ? WHERE title = ?";
    private static final String SQL_GET_CHECKED_OUT_BOOKS =
            "SELECT b.title,username,b.due_date FROM book b " +
                    "JOIN user_login ul ON b.user_login_id = ul.id";
    private static final String SQL_GET_OVERDUE_BOOK =
            "SELECT b.title,username,b.due_date FROM book b " +
                    "JOIN user_login ul ON b.user_login_id = ul.id " +
                    "WHERE b.due_date > ?";

    private final JdbcTemplate jdbcTemplate;

    public BookDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Date getNextDayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        return calendar.getTime();
    }

    @Override
    public List<String> getAvailableBooks() {
        List<String> titles = new LinkedList<>();
        jdbcTemplate.query(SQL_GET_AVAILABLE_BOOKS,
                resultSet -> {
                    titles.add(resultSet.getString("title"));
                });
        return titles;
    }

    @Override
    public Optional<Book> getBookForTitle(String title) {
        ResultHolder<Book> resultHolder = new ResultHolder<>();
        jdbcTemplate.query(SQL_GET_BOOK_FOR_TITLE,new Object[]{title},
                new int[]{Types.VARCHAR},resultSet -> {
                    Book book = new Book();
                    book.setTitle(resultSet.getString(1));
                    book.setUsername(resultSet.getString(2));
                    book.setDueDate(resultSet.getDate(3));
                    resultHolder.setResult(book);
                });
        return resultHolder.getResultOptional();
    }

    @Override
    @Transactional
    public Date performCheckout(Book book, String username) {
        ResultHolder<Long> id = new ResultHolder<>();
        jdbcTemplate.query(SQL_GET_USER_ID,new Object[]{username},
                new int[]{Types.VARCHAR},resultSet -> {
                    id.setResult(resultSet.getLong(1));
                });
        Optional<Long> idOptional = id.getResultOptional();
        if(idOptional.isEmpty()) {
            throw new RuntimeException("Unable to find record for user " + username);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,10);
            Date dueDate = calendar.getTime();
            jdbcTemplate.update(SQL_APPLY_CHECKOUT,
                    idOptional.get(),dueDate,book.getTitle());
            return dueDate;
        }
    }

    @Override
    public List<Book> getCheckedOutBooks() {
        BookRowCallbackHandler rowCallbackHandler = new BookRowCallbackHandler();
        jdbcTemplate.query(SQL_GET_CHECKED_OUT_BOOKS,rowCallbackHandler);
        return rowCallbackHandler.getBookResults();
    }

    @Override
    public List<Book> getOverdueBooks() {
        Date cutoffTime = getNextDayDate();
        BookRowCallbackHandler rowCallbackHandler = new BookRowCallbackHandler();
        jdbcTemplate.query(SQL_GET_OVERDUE_BOOK,new Object[]{cutoffTime},
                new int[]{Types.DATE},rowCallbackHandler);
        return rowCallbackHandler.getBookResults();
    }
}
