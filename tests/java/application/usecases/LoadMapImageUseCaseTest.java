//package application.usecases;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//
//public class LoadMapImageUseCaseTest {
//
//    @Test
//    public void testLoadMapImageSuccess() throws IOException {
//        // Create a temporary image file
//        File tempFile = File.createTempFile("testImage", ".png");
//        tempFile.deleteOnExit();
//
//        // Write an empty image to the file
//        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
//        ImageIO.write(image, "png", tempFile);
//
//        // Execute the use case
//        LoadMapImageUseCase useCase = new LoadMapImageUseCase();
//        BufferedImage result = (BufferedImage) useCase.execute(tempFile.getAbsolutePath());
//
//        // Assert the result
//        assertNotNull(result);
//        assertEquals(10, result.getWidth());
//        assertEquals(10, result.getHeight());
//    }
//
//    @Test
//    public void testLoadMapImageFailure() {
//        // Use a non-existent file path
//        String invalidPath = "invalidImagePath.png";
//
//        // Execute the use case
//        LoadMapImageUseCase useCase = new LoadMapImageUseCase();
//
//        try {
//            useCase.execute(invalidPath);
//            fail("Expected IOException to be thrown");
//        } catch (IOException e) {
//            assertTrue(e.getMessage().contains("Can't read input file") || e.getMessage().contains("No such file"));
//        }
//    }
//}
