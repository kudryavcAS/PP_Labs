package lab.xml.repository;

import lab.xml.model.Book;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookXmlRepository {

    private final String FILE_PATH = "data/library.xml";

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return books;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("book");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    books.add(new Book(
                            Integer.parseInt(elem.getAttribute("id")),
                            getTagValue("title", elem),
                            getTagValue("author", elem),
                            Integer.parseInt(getTagValue("year", elem)),
                            Double.parseDouble(getTagValue("price", elem)),
                            getTagValue("category", elem),
                            Integer.parseInt(elem.getAttribute("total")),
                            Integer.parseInt(elem.getAttribute("available"))
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public void save(Book book) {
        List<Book> books = findAll();
        int maxId = books.stream().mapToInt(Book::getId).max().orElse(0);
        book.setId(maxId + 1);
        book.setAvailable(book.getTotal());
        books.add(book);
        writeToXml(books);
    }

    public void updatePrice(int id, double newPrice) {
        List<Book> books = findAll();
        for (Book b : books) {
            if (b.getId() == id) {
                b.setPrice(newPrice);
                break;
            }
        }
        writeToXml(books);
    }

    public void checkoutBook(int id) {
        List<Book> books = findAll();
        for (Book b : books) {
            if (b.getId() == id && b.getAvailable() > 0) {
                b.setAvailable(b.getAvailable() - 1);
                break;
            }
        }
        writeToXml(books);
    }

    private void writeToXml(List<Book> books) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("library");
            doc.appendChild(rootElement);

            for (Book book : books) {
                Element bookElem = doc.createElement("book");
                bookElem.setAttribute("id", String.valueOf(book.getId()));
                bookElem.setAttribute("total", String.valueOf(book.getTotal()));
                bookElem.setAttribute("available", String.valueOf(book.getAvailable()));

                appendChild(doc, bookElem, "title", book.getTitle());
                appendChild(doc, bookElem, "author", book.getAuthor());
                appendChild(doc, bookElem, "year", String.valueOf(book.getYear()));
                appendChild(doc, bookElem, "price", String.valueOf(book.getPrice()));
                appendChild(doc, bookElem, "category", book.getCategory());

                rootElement.appendChild(bookElem);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendChild(Document doc, Element parent, String tagName, String text) {
        Element el = doc.createElement(tagName);
        el.appendChild(doc.createTextNode(text));
        parent.appendChild(el);
    }

    private String getTagValue(String tag, Element element) {
        NodeList nl = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node n = nl.item(0);
        return n.getNodeValue();
    }
}