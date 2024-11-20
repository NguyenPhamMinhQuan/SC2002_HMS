package HMS.Repositories;

import java.util.List;

public interface LoadandSaveInterface<T> {
    /**
     * Loads data from a file and returns it as a list of objects.
     * @return a list of objects loaded from the file.
     */
    List<T> loadData();

    /**
     * Saves a list of objects to a file.
     * @param data the list of objects to save.
     */
    void saveData(List<T> data);
}