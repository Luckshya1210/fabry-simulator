module test.test {
    requires javafx.controls;
    requires javafx.fxml;
            
                        
    opens fabry to javafx.fxml;
    exports fabry;
}