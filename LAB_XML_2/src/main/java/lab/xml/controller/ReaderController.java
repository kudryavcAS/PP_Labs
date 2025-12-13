package lab.xml.controller;

import lab.xml.model.Book;
import lab.xml.repository.BookXmlRepository;
import lab.xml.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reader")
public class ReaderController {

    private final BookXmlRepository bookRepository;
    private final UserRepository userRepository;

    public ReaderController(BookXmlRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/books")
    public String listBooks(Model model,
                            @RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "type", required = false) String type) {
        List<Book> books = bookRepository.findAll();

        if (search != null && !search.isEmpty()) {
            books = books.stream().filter(b -> {
                if ("author".equals(type)) return b.getAuthor().toLowerCase().contains(search.toLowerCase());
                if ("year".equals(type)) return String.valueOf(b.getYear()).contains(search);
                if ("category".equals(type)) return b.getCategory().toLowerCase().contains(search.toLowerCase());
                return true;
            }).collect(Collectors.toList());
        }

        model.addAttribute("books", books);
        return "reader/books";
    }

    @GetMapping("/account")
    public String account(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("user", userRepository.findByUsername(userDetails.getUsername()));
        return "reader/account";
    }
}