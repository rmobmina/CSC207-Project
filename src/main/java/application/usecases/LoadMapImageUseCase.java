package application.usecases;

import utils.UseCaseInteractor;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Use case for loading map images.
 */
public class LoadMapImageUseCase extends UseCaseInteractor {

    /**
     * Loads the map image from the specified file path.
     *
     * @param mapFilePath The file path of the map image.
     * @return The loaded Image object.
     * @throws IOException If an error occurs while reading the file.
     */
    public Image execute(String mapFilePath) throws IOException {
        return ImageIO.read(new File(mapFilePath));
    }
}
