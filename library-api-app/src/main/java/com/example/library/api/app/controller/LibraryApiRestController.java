package com.example.library.api.app.controller;

import com.example.library.api.app.bean.Book;
import com.example.library.api.app.bean.CheckoutRequest;
import com.example.library.api.app.bean.CheckoutResponse;
import com.example.library.api.app.dao.BookDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
public class LibraryApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryApiRestController.class);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final BookDAO bookDAO;

    public LibraryApiRestController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping(path = "/books/available",produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_USER")
    public List<String> availableBooks() {
        return bookDAO.getAvailableBooks();
    }

    private String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userDetails.getUsername();
        }
        return null;
    }

    @GetMapping(path = "/books/checkedOut",produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public List<Book> getCheckedOutBooks() {
        return bookDAO.getCheckedOutBooks();
    }

    @GetMapping(path = "/books/overdue",produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public List<Book> getOverdueBooks() {
        return bookDAO.getOverdueBooks();
    }

    @PostMapping(path = "/books/checkout",produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_USER")
    @ResponseBody
    public CheckoutResponse checkoutBook(@RequestBody CheckoutRequest request,
                                         Authentication authentication) {
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setMessage("OK");
        Optional<Book> book = bookDAO.getBookForTitle(request.getTitle());
        book.ifPresentOrElse(
                bookPresent -> {
                    if(bookPresent.getUsername() == null) {
                        performCheckoutPresent(checkoutResponse,authentication,
                                bookPresent);
                    } else {
                        checkoutResponse.setMessage(
                                String.format("Title is already checked out, Due date is %s",
                                        dateFormat.format(bookPresent.getDueDate()))
                        );
                    }
                },
                () -> checkoutResponse.setMessage("Title of Book not recognised")
        );
        return checkoutResponse;
    }

    private void performCheckoutPresent(CheckoutResponse checkoutResponse,Authentication authentication,
                                        Book book) {
        String username = getUsername(authentication);
        if(username != null) {
            checkoutResponse.setDueDate(bookDAO.performCheckout(book,username));
        } else {
            checkoutResponse.setMessage("Unable to obtain Username from Session");
        }
    }
}
