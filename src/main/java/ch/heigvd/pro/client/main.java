package ch.heigvd.pro.client;

import ch.heigvd.pro.client.file.FileDriver;

import java.io.File;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        System.out.println("Enter the name of your password file :");

        FileDriver test = new FileDriver();
        Scanner filenameInput = new Scanner(System.in);
        String filename = filenameInput.nextLine();

        File passwordDB = new File(filename + ".json");
        Safe safeTest = new Safe();

        while (true) {
            System.out.println("1-Load another file");
            System.out.println("2-Create an entry");
            System.out.println("3-Modify an entry");
            System.out.println("4-Show entries");
            System.out.println("5-Save");
            System.out.println("6-Load");

            Scanner menuInput = new Scanner(System.in);

            switch (menuInput.nextInt()) {
                case 1:
                    break;
                case 2:
                    System.out.println("Enter a username :");

                    Scanner usernameInput = new Scanner(System.in);
                    String username = usernameInput.nextLine();
                    String target = usernameInput.nextLine();
                    Password password = new Password(usernameInput.nextLine());
                    String email = usernameInput.nextLine();
                    Entry newEntry = new Entry(username, target, password, email, null);

                    safeTest.addEntry(newEntry);
                    break;
                case 3:
                    break;
                case 4:
                    for (Entry e : safeTest.getEntryList()) {
                        System.out.println("Username : " + e.getUsername());
                    }
                    break;
                case 5:
                    test.saveSafe(safeTest, passwordDB);
                    break;
                case 6:
                    safeTest = test.loadSafe(passwordDB);
                    break;
                default:
                    break;
            }

        }
    }
}
