# ImagePanZoom
/** @see https://stackoverflow.com/questions/48687994/zooming-an-image-in-imageview-javafx/73328643#73328643

While this complete example illustrates zooming and panning an image using sliders, the variant below focuses on a more basic approach that may be easier to adapt to other use cases. It leverages ImageView scaling to zoom and ScrollPane mouse handing to pan.

In summary, the application constructs an ImageView from an Image read from the selected file, altering the cursor to indicate dragging with a closed hand. The ImageView is then used to construct a ScrollPane, for which the pannable property is enabled; scrolling starts out centered. A Slider controls the zoom level, and its listener alters the view's fitWidth property accordingly, while preserving the image's aspect ratio. By saving and restoring the scroll pane's scroll bar settings, zooming stays centered on the view.

Problem reported here:

https://stackoverflow.com/questions/79543365/javafx-imageview-scrollpane-example-doesnt-work-help-please

The problem is that zooming in, while using the scroll wheel causes the corners of the image to be unreachable via panning. The goal is to be able to zoom into the corners to examine details of the image all over the image.

Oddly, using the slider at the bottom of the window to zoom preserves the ability to pan throughout the image.

So the remaining goal is to enhance the SetOnScroll method and its event handler to preserve the ability to pan to the edges and corners of the image after zooming in.

