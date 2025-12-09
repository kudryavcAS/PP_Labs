module lab.sortvizualization.pp_sort_vizualization {
    requires javafx.controls;
    requires javafx.fxml;


    opens lab.sortvizualization to javafx.fxml;
    exports lab.sortvizualization;
}