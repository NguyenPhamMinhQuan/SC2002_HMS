import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TextDatabase {

    private static final String SEPARATOR = ","; // CSV-like separator
    private static final String DATA_DIR = "data/";

    // Ensure the data directory exists
    private static void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir(); // Create the directory if it doesn't exist
        }
    }

    // Read lines from a text file
    public static List<String> readLines(String filename) throws IOException {
        ensureDataDirectoryExists(); // Ensure data directory exists before reading
        List<String> lines = new ArrayList<>();
        File file = new File(DATA_DIR + filename); // Save to data/ directory

        if (!file.exists()) {
            file.createNewFile();
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // Write lines to a text file
    public static void writeLines(String filename, List<String> lines) throws IOException {
        ensureDataDirectoryExists(); // Ensure data directory exists before writing
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + filename))) { // Save to data/ directory
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    // Convert any object to a CSV-like string using reflection
    public static <T> String objectToText(T obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (sb.length() > 0) {
                sb.append(SEPARATOR);
            }
            sb.append(value);
        }

        return sb.toString();
    }

    // Save objects to a text file
    public static <T> void saveObjects(String filename, List<T> objectList) throws IOException, IllegalAccessException {
        List<String> lines = new ArrayList<>();

        for (T obj : objectList) {
            lines.add(objectToText(obj));
        }

        writeLines(filename, lines);
    }

    // Convert a line of CSV text back to an object using reflection
    public static <T> T textToObject(String line, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        String[] values = line.split(SEPARATOR);
        T obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            Class<?> fieldType = field.getType();
            String value = values[i].trim();

            if (fieldType == int.class || fieldType == Integer.class) {
                field.set(obj, Integer.parseInt(value));
            } else if (fieldType == double.class || fieldType == Double.class) {
                field.set(obj, Double.parseDouble(value));
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                field.set(obj, Boolean.parseBoolean(value));
            } else {
                field.set(obj, value);
            }
        }

        return obj;
    }

    // Load objects from a file
    public static <T> List<T> loadObjects(String filename, Class<T> clazz) throws IOException, InstantiationException, IllegalAccessException {
        List<T> objectList = new ArrayList<>();
        List<String> lines = readLines(filename);

        for (String line : lines) {
            T obj = textToObject(line, clazz);
            objectList.add(obj);
        }

        return objectList;
    }
}
