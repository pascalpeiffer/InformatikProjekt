package de.missiontakedown.io.file;

import de.missiontakedown.serializable.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * @author Pascal
 */
public class FileManager {

    /**
     * Deserializing from File
     * Can only be used for smaller Files (<<code>2GB</code>)
     * @param serializable Serializable object
     * @param path File Path
     * @throws IOException if an I/O error occurs reading from the stream
     * @see Files#readAllBytes(Path) 
     */
    public static void deserializeFromFile(Serializable serializable, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String jsonString = new String(bytes, StandardCharsets.UTF_8);

        JSONObject json;

        try {
            json = new JSONObject(jsonString);
        }catch (JSONException ignored) {
            json = new JSONObject();
        }

        serializable.deserialize(json);
    }

    /**
     *
     * @param serializable Serializable object
     * @param path File Path
     * @throws IOException if an I/O error occurs writing to or creating the file
     * @see Files#write(Path, byte[], OpenOption...) 
     */
    public static void serializeToFile(Serializable serializable, String path) throws IOException {
        JSONObject json = serializable.serialize();
        String jsonString = json.toString();
        Files.write(Paths.get(path), jsonString.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
