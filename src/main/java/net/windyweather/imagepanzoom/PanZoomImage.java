/*
/** @see https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx/73328643#73328643
While this complete example illustrates zooming and panning an image using sliders, the variant below focuses on a more basic approach that may be easier to adapt to other use cases. It leverages ImageView scaling to zoom and ScrollPane mouse handing to pan.

In summary, the application constructs an ImageView from an Image read from the selected file, altering the cursor to indicate dragging with a closed hand. The ImageView is then used to construct a ScrollPane, for which the pannable property is enabled; scrolling starts out centered. A Slider controls the zoom level, and its listener alters the view's fitWidth property accordingly, while preserving the image's aspect ratio. By saving and restoring the scroll pane's scroll bar settings, zooming stays centered on the view.

*/


package net.windyweather.imagepanzoom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/** @see https://stackoverflow.com/a/73328643/230513 */
public class PanZoomImage extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        /*

         */
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        /*
            Let's start the FileChooser in the Current Working Directory
         */
        var fileChooser = new FileChooser();
        String sCWD = System.getProperty("user.dir");
        File fCWD = new File( sCWD );
        fileChooser.setInitialDirectory( fCWD );

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            System.out.println("No Image File chosen");
            return;
        }

        var image = new Image(new FileInputStream(file));
        var view = new ImageView(image);
        view.setPreserveRatio(true);

        /*
            Don't need the change of cursor. Panning now gives cross arrows
            which is fine.
         */
        if (false) {

            view.setOnMouseEntered(e -> {
                view.setCursor(Cursor.OPEN_HAND);
            });
            view.setOnMousePressed(e -> {
                view.setCursor(Cursor.CLOSED_HAND);
            });
            view.setOnMouseReleased(e -> {
                view.setCursor(Cursor.OPEN_HAND);
            });
            view.setOnMouseExited(e -> {
                view.setCursor(Cursor.DEFAULT);
            });
        }
        var sp = new ScrollPane(view);
        sp.setPannable(true);
        sp.setHvalue(0.5);
        sp.setVvalue(0.5);
        var slider = new Slider(0.2, 5.0, 1.0);
        slider.setBlockIncrement(0.1);
        /*
            Personally I hate these embedded listener methods
            They make things really hard to read and with IntelliJ inserting
            stuff for you frequently as you edit, sometimes you can get lost
            and have to mess around endlessly to get it put back together. Sigh.
         */
        slider.valueProperty().addListener((o, oldV, newV) -> {
            var x = sp.getHvalue();
            var y = sp.getVvalue();
            view.setFitWidth(image.getWidth() * newV.doubleValue());
            /*
                What happens if we don't restore x and y?
             */
            if (false) {
                sp.setHvalue(x);
                sp.setVvalue(y);
            }
        });

        var root = new BorderPane(sp);
        root.setBottom(slider);

        stage.setTitle("Image Pan Zoom");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();

    };

    public static void main(String[] args) {
        launch(args);
    }
}
