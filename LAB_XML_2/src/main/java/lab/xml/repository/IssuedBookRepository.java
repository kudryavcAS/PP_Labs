package lab.xml.repository;

import lab.xml.model.IssuedBook;
import lab.xml.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {
    List<IssuedBook> findByUser(User user);
}