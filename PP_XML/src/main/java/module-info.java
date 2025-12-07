module lab.library.pp_xml {
    requires javafx.controls;
    requires javafx.fxml;


    opens lab.library.pp_xml to javafx.fxml;
    exports lab.library.pp_xml;
}