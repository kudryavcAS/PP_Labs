package lab.xml.controller;

import lab.xml.model.Book;
import lab.xml.model.IssuedBook;
import lab.xml.model.User;
import lab.xml.repository.BookXmlRepository;
import lab.xml.repository.IssuedBookRepository;
import lab.xml.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final BookXmlRepository bookRepository;
    private final UserRepository userRepository;
    private final IssuedBookRepository issuedBookRepository;

    public LibrarianController(BookXmlRepository bookRepository, UserRepository userRepository, IssuedBookRepository issuedBookRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "librarian/books";
    }

    @GetMapping("/book/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "librarian/add_book";
    }

    @PostMapping("/book/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/librarian/books";
    }

    @PostMapping("/book/update_price")
    public String updatePrice(@RequestParam("id") int id, @RequestParam("price") double price) {
        bookRepository.updatePrice(id, price);
        return "redirect:/librarian/books";
    }

    @PostMapping("/book/checkout")
    public String checkoutBook(@RequestParam("bookId") int bookId, @RequestParam("userId") Long userId) {
        bookRepository.checkoutBook(bookId);

        User user = userRepository.findById(userId).orElseThrow();
        IssuedBook issuedBook = new IssuedBook(user, bookId);
        issuedBookRepository.save(issuedBook);

        return "redirect:/librarian/books";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        List<Book> allBooks = bookRepository.findAll();

        Map<User, List<String>> userBooksMap = new HashMap<>();

        for (User user : users) {
            List<Integer> bookIds = issuedBookRepository.findByUser(user).stream()
                    .map(IssuedBook::getBookId)
                    .toList();

            List<String> bookTitles = allBooks.stream()
                    .filter(b -> bookIds.contains(b.getId()))
                    .map(Book::getTitle)
                    .collect(Collectors.toList());

            userBooksMap.put(user, bookTitles);
        }

        model.addAttribute("userBooksMap", userBooksMap);
        return "librarian/users";
    }
}