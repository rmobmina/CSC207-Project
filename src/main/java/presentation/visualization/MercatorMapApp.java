package presentation.visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class MercatorMapApp extends JFrame {

    private final Image mapImage;
    private final Point2D mercatorPoint;

    public MercatorMapApp(String mapPath, double latitude, double longitude) {
        this.mapImage = new ImageIcon(mapPath).getImage();
        this.mercatorPoint = new MercatorAlgorithm(latitude, longitude).;
    }

}
