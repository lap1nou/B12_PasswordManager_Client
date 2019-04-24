package ch.heigvd.pro.client.file;


import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileDriverTest {
    /*
    @Test
    public void saveJsonTest() {
        FileDriver testFileDriver = new FileDriver();
        String toCompare = "{\"entryList\":[{\"id\":0,\"username\":\"User1\",\"target\":\"http://google.ch\",\"password\":{\"id\":0,\"strength\":0.0,\"password\":\"1234\"},\"email\":\"user1@gmail.com\"},{\"id\":1,\"username\":\"User2\",\"target\":\"http://google.com\",\"password\":{\"id\":1,\"strength\":0.0,\"password\":\"12345\"},\"email\":\"user2@gmail.com\"}]}";
        Entry entry1 = new Entry("User1", "http://google.ch", new Password("1234"), "user1@gmail.com", null);
        Entry entry2 = new Entry("User2", "http://google.com", new Password("12345"), "user2@gmail.com", null);
        Safe safeTest = new Safe();

        safeTest.addEntry(entry1);
        safeTest.addEntry(entry2);

        testFileDriver.saveSafe(safeTest, new File("test.json"));
        try {
            BufferedReader fr = new BufferedReader(new FileReader(new File("test.json")));
            String result = fr.readLine();
            assertEquals(toCompare, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}