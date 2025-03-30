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
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/** @see https://stackoverflow.com/a/73328643/230513 */
public class PanZoomImage extends Application {

    static double dZoomScale = 1.0;

    public static void printSysOut( String str ) {
        System.out.println(str);
    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        /*

         */
        printSysOut("Working Directory = " + System.getProperty("user.dir"));

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
            if (true) {
                sp.setHvalue(x);
                sp.setVvalue(y);
            }
        });


        /*
            Now set up a scroll wheel based zoom
         */


        view.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        event.consume();
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();
                    /*
                        Don't zoom forever. Just ignore it after
                        a while.
                     */
                        double dScale = dZoomScale;
                        if (deltaY > 0.0 && dScale > 10.0) {
                            printSysOut("Don't scale too big");
                            //event.consume();
                            return;
                        } else if (deltaY < 0.0 && dScale < 0.20) {
                            printSysOut("Don't scale too small");
                            //event.consume();
                            return;
                        }

                        if (deltaY < 0) {
                            zoomFactor = 0.95;
                        }

                        var x = sp.getHvalue();
                        var y = sp.getVvalue();
            /*
                What happens if we don't restore x and y?
             */
                        /*
                            Let's zoom image with setFitWidth rather than setScale
                            but save our zoom so we can report it
                         */
                        dZoomScale = dZoomScale * zoomFactor;
                        slider.setValue( dZoomScale);
                        view.setFitWidth(image.getWidth() * zoomFactor);

                        String scaleReport = String.format("ImageView scale factor %.3f", view.getScaleX() * dZoomScale);



                        if (false) {
                            view.setScaleX(view.getScaleX() * zoomFactor);
                            view.setScaleY(view.getScaleY() * zoomFactor);
                            //String scaleReport = String.format("ImageView scale factors [%.3f, %.3f]", view.getScaleX(), view.getScaleY());
                        }

                        printSysOut(scaleReport);
                        /*
                            Lets try this here and see if that fixes the pan after zoom
                         */
                        x = sp.getHvalue();
                        y = sp.getVvalue();

                        /*
                            ********************************************************************
                            *************** The following statement appears to have made it work
                            * Now wheel zooming preserves panning to the corners
                            ********************************************************************
                         */
                        view.setFitWidth(image.getWidth() * dZoomScale);
            /*
                What happens if we don't restore x and y?
             */
                        if (true) {
                            sp.setHvalue(x);
                            sp.setVvalue(y);
                        }
                        /*
                            we already did this above
                         */
                        if ( false ) {
                            view.setFitWidth(image.getWidth() * zoomFactor);
                            if (true) {
                                sp.setHvalue(x);
                                sp.setVvalue(y);
                            }
                        }
                    }
                }
        );

        /*
            Carry on with the rest of start
         */
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
