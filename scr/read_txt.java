import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class read_txt {

    public static void main(String[] args) {

        String filename = "..\\fake_data\\isbn.txt";
        try {
            List list = read_file(filename);
            //list.forEach(System.out::println);
            System.out.println("Returning element: " + list.get(100));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List read_file(String fileName) throws IOException {
        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            result = lines.collect(Collectors.toList());
        }
        return result;
    }
}
