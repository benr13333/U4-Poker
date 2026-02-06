import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Tools {


    public static int countLines(Path filePath)
    {
        try (var lines = Files.lines(filePath))
        {
            return (int) lines.count(); //cast because this returns a long and i dont wanna have to go back and change my arrays to longs[]
        }
        catch (IOException e)
        {
            return -1;
        }
    }


}
