module net.windyweather.imagepanzoom {
    requires javafx.controls;
    requires javafx.fxml;


    opens net.windyweather.imagepanzoom to javafx.fxml;
    exports net.windyweather.imagepanzoom;
}