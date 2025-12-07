package lab.service;

import lab.model.Book;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private static final String XML_PATH = "data/library.xml";
    private static final String XSD_PATH = "data/library.xsd";

    // Метод для валидации и загрузки
    public List<Book> loadBooks() throws Exception {
        File xmlFile = new File(XML_PATH);
        File xsdFile = new File(XSD_PATH);

        // 1. Валидация XSD
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsdFile);
        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(getDocument(xmlFile))); // Валидируем

        // 2. Парсинг
        return parseBooks(xmlFile);
    }

    private Document getDocument(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(file);
    }

    private List<Book> parseBooks(File file) throws Exception {
        List<Book> books = new ArrayList<>();
        Document doc = getDocument(file);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("book");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                int id = Integer.parseInt(elem.getAttribute("id"));
                int total = Integer.parseInt(elem.getAttribute("total"));
                int available = Integer.parseInt(elem.getAttribute("available"));

                String title = getTagValue("title", elem);
                String author = getTagValue("author", elem);
                int year = Integer.parseInt(getTagValue("year", elem));
                double price = Double.parseDouble(getTagValue("price", elem));
                String category = getTagValue("category", elem);

                books.add(new Book(id, title, author, year, price, category, total, available));
            }
        }
        return books;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nl = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node n = nl.item(0);
        return n.getNodeValue();
    }

    // Сохранение изменений обратно в XML
    public void saveBooks(List<Book> books) {
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

            // Запись в файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(XML_PATH));
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
}