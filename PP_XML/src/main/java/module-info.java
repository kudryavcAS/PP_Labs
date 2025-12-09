module lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml; // Для работы с XML (DOM парсер)

    opens lab.controller to javafx.fxml;
    opens lab.model to javafx.base;

    exports lab;
}