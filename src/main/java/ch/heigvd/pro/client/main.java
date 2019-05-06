package ch.heigvd.pro.client;

import ch.heigvd.pro.client.crypto.Crypto;
import ch.heigvd.pro.client.file.*;
import ch.heigvd.pro.client.gui.GUIManager;
import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;

import javax.crypto.BadPaddingException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
/*
        FileDriver test = new FileDriver();
        File passwordDB = new File("");
        Safe safeTest = new Safe();
        Scanner filenameInput = new Scanner(System.in);
        Scanner passwordInput = new Scanner(System.in);

        while (true) {
            System.out.println("1-Create an entry");
            System.out.println("2-Modify an entry");
            System.out.println("3-Show entries");
            System.out.println("4-Save");
            System.out.println("5-Create a database");
            System.out.println("6-Load an existing database");
            System.out.println("7-Exit");

            Scanner menuInput = new Scanner(System.in);

            switch (menuInput.nextInt()) {
                case 1:
                    Scanner userInput = new Scanner(System.in);

                    System.out.println("Enter an entry name :");
                    char[] entryName = userInput.nextLine().toCharArray();

                    System.out.println("Enter a username :");
                    char[] username = userInput.nextLine().toCharArray();

                    System.out.println("Enter a target :");
                    char[] target = userInput.nextLine().toCharArray();

                    System.out.println("Enter a notes :");
                    char[] notes = userInput.nextLine().toCharArray();

                    System.out.println("Enter a password :");
                    char[] clearPassword = userInput.nextLine().toCharArray();

                    System.out.println("Enter a folder name :");
                    String folderName = userInput.nextLine();

                    Entry newEntry = new Entry(0, entryName, username, target, clearPassword, notes, new Date());
                    newEntry.encryptEntry(safeTest.getSafePassword());

                    List<Entry> entryList = new ArrayList<>();
                    entryList.add(newEntry);

                    Folder newFolder = new Folder(folderName, entryList);
                    safeTest.addFolder(newFolder);

                    break;
                case 2:

                    break;
                case 3:
                    for (Folder f : safeTest.getFolderList()) {
                        for (Entry e : f.getEntrylist()) {
                            try {
                                e.decryptEntry(safeTest.getSafePassword());
                            } catch (BadPaddingException e1) {
                                e1.printStackTrace();
                            }

                            System.out.println("======= ENTRY =======");
                            System.out.print("Username : ");
                            System.out.println(e.getUsername());
                            System.out.print("Target : ");
                            System.out.println(e.getTarget());
                            System.out.print("Notes : ");
                            System.out.println(e.getNotes());

                            System.out.print("Password : ");
                            System.out.println(e.getClearPassword());

                            System.out.println();
                            System.out.println();

                            e.encryptEntry(safeTest.getSafePassword());
                        }
                    }

                    break;
                case 4:
                    test.saveSafe(safeTest, passwordDB);

                    break;
                case 5:
                    System.out.println("Enter the name of your new password database file :");

                    String newFilename = filenameInput.nextLine();
                    passwordDB = new File(newFilename + ".json");
                    passwordDB.createNewFile();

                    System.out.println("Enter your password :");
                    safeTest.setSafePassword(passwordInput.nextLine().toCharArray());

                    break;
                case 6:
                    System.out.println("Enter the name of your password file :");

                    String filename = filenameInput.nextLine();
                    passwordDB = new File(filename + ".json");
                    safeTest = test.loadSafe(passwordDB);

                    boolean goodPassword = false;

                    do {
                        System.out.println("Enter your password :");
                        safeTest.setSafePassword(passwordInput.nextLine().toCharArray());

                        try {
                            safeTest.decryptPassword();
                            goodPassword = true;
                            safeTest.encryptPassword();

                        } catch (BadPaddingException e) {
                            System.out.println("Error, wrong password !");
                        }
                    } while (!goodPassword);

                    break;
                case 7:
                    return;
                default:
                    break;
            }
        }
*/
        GUIManager newGUIMan = new GUIManager();
        newGUIMan.start();

    }
}
