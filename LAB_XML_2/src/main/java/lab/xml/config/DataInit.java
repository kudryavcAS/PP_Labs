package lab.xml.config;

import lab.xml.model.User;
import lab.xml.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final UserService userService;

    public DataInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123");
            userService.saveLibrarian(admin);
            System.out.println(">>> Пользователь admin/admin создан автоматически!");
        }
    }
}