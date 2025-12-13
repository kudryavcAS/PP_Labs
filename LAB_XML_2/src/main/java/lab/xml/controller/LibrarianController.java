package lab.xml.controller;

import lab.xml.model.Book;
import lab.xml.repository.BookXmlRepository;
import lab.xml.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final BookXmlRepository bookRepository;
    private final UserRepository userRepository;

    public LibrarianController(BookXmlRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/books")
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
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
    public String checkoutBook(@RequestParam("id") int id) {
        bookRepository.checkoutBook(id);
        return "redirect:/librarian/books";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "librarian/users";
    }
}