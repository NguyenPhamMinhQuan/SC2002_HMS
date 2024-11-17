import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling text-based file I/O operations, such as reading and writing
 * CSV-like files, and converting objects to and from text representations using reflection.
 */
public class TextDB {

    private static final String SEPARATOR = ",";
    private static final String DATA_DIR = "data/";

    /**
     * Ensures that the directory where data files are stored exists.
     * If the directory does not exist, it is created.
     */
    private static void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    /**
     * Reads all lines from a file in the data directory.
     *
     * @param filename The name of the file to read.
     * @return A list of strings, each representing a line from the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static List<String> readLines(String filename) throws IOException {
        ensureDataDirectoryExists();
        List<String> lines = new ArrayList<>();
        File file = new File(DATA_DIR + filename);

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

    /**
     * Writes a list of lines to a file in the data directory.
     *
     * @param filename The name of the file to write to.
     * @param lines    The list of strings to write, each as a separate line.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void writeLines(String filename, List<String> lines) throws IOException {
        ensureDataDirectoryExists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + filename))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    /**
     * Converts an object to a CSV-like string using reflection.
     * Each field of the object is included as a value in the resulting string.
     *
     * @param <T> The type of the object.
     * @param obj The object to convert to text.
     * @return A string containing the object's field values, separated by commas.
     * @throws IllegalAccessException If a field of the object is inaccessible.
     */
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

    /**
     * Saves a list of objects to a file in the data directory.
     * Each object is converted to a CSV-like string and saved as a line in the file.
     *
     * @param <T>        The type of the objects.
     * @param filename   The name of the file to save to.
     * @param objectList The list of objects to save.
     * @throws IOException           If an I/O error occurs while writing to the file.
     * @throws IllegalAccessException If a field of an object is inaccessible.
     */
    public static <T> void saveObjects(String filename, List<T> objectList) throws IOException, IllegalAccessException {
        List<String> lines = new ArrayList<>();
        for (T obj : objectList) {
            lines.add(objectToText(obj));
        }
        writeLines(filename, lines);
    }

    /**
     * Converts a line of CSV-like text into an object using reflection.
     * Each value in the line is assigned to a corresponding field in the object.
     *
     * @param <T>   The type of the object to create.
     * @param line  The CSV-like text representing the object.
     * @param clazz The class of the object to create.
     * @return An object populated with values from the CSV-like text.
     * @throws Exception If an error occurs during object creation or field assignment.
     */
    public static <T> T textToObject(String line, Class<T> clazz) throws Exception {
        String[] values = line.split(SEPARATOR);
        T obj = clazz.getDeclaredConstructor().newInstance();
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

    /**
     * Loads a list of objects from a file in the data directory.
     * Each line in the file is converted to an object using reflection.
     *
     * @param <T>      The type of the objects to load.
     * @param filename The name of the file to read from.
     * @param clazz    The class of the objects to create.
     * @return A list of objects created from the lines in the file.
     * @throws Exception If an error occurs during file reading or object creation.
     */
    public static <T> List<T> loadObjects(String filename, Class<T> clazz) throws Exception {
        List<T> objectList = new ArrayList<>();
        List<String> lines = readLines(filename);

        for (String line : lines) {
            T obj = textToObject(line, clazz);
            objectList.add(obj);
        }

        return objectList;
    }
}
