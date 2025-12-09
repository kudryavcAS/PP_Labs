module org.example.pp_sr_2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pp_sr_2 to javafx.fxml;
    exports org.example.pp_sr_2;
}