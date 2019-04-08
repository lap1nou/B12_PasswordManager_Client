package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.Safe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Source : https://www.tutorialspoint.com/gson/gson_object_serialization.htm
public class FileDriver implements IStorePasswordDriver {
    /**
     * Load JSON from a file and return the Safe object of the JSON data.
     *
     * @param file the file to read from
     * @return a Safe object, unserialized from the JSON file
     */
    public Safe loadSafe(File file) {
        try {
            FileReader fr = new FileReader(file);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            return parser.fromJson(fr, Safe.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Save a Safe object into a JSON format.
     *
     * @param safe the Safe object to save
     * @param file the file where to save the Safe object
     */
    public void saveSafe(Safe safe, File file) {
        try {
            FileWriter fw = new FileWriter(file, false);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            fw.write(parser.toJson(safe));
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
