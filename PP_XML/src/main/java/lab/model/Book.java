package lab.model;

import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final IntegerProperty year;
    private final DoubleProperty price;
    private final StringProperty category;
    private final IntegerProperty total;
    private final IntegerProperty available;

    public Book(int id, String title, String author, int year, double price, String category, int total, int available) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.total = new SimpleIntegerProperty(total);
        this.available = new SimpleIntegerProperty(available);
    }

    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public int getYear() { return year.get(); }
    public double getPrice() { return price.get(); }
    public String getCategory() { return category.get(); }
    public int getTotal() { return total.get(); }
    public int getAvailable() { return available.get(); }

    public void setPrice(double price) { this.price.set(price); }
    public void setAvailable(int available) { this.available.set(available); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public IntegerProperty yearProperty() { return year; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty categoryProperty() { return category; }
    public IntegerProperty totalProperty() { return total; }
    public IntegerProperty availableProperty() { return available; }
}