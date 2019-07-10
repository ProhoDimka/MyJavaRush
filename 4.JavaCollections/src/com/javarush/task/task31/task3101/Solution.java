package com.javarush.task.task31.task3101;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        File catalog = new File(args[0]);
        File targetFile = new File(args[1]);
        File targetNewFileName = new File(targetFile.getParent() + "/" + "allFilesContent.txt");

        // Получаем список файлов в текущем каталоге и подкаталогах
        List<File> listOfFiles = new ArrayList<>();
        if (catalog.isDirectory())
            listOfFiles = getAllFiles(catalog);
        else if (catalog.isFile())
            listOfFiles = getAllFiles(catalog.getParentFile());

        /* Удаляем из списка файлы
        1. не *.txt
        2. размером больше 50 Байт
        3. если файл равен файлу из второго аргумента
         */
        Iterator<File> fileIterator = listOfFiles.iterator();
        while (fileIterator.hasNext()) {
            File curFile = fileIterator.next();
            System.out.print(String.format("файл - %s размер файла: %d Байт",
                    curFile.getAbsoluteFile(), curFile.length()));
            if (!curFile.getName().matches(".+\\.txt$")) {
                fileIterator.remove();
                System.out.print(" --- был удален из списка.");
            } else if (curFile.length() > 50) {
                fileIterator.remove();
                System.out.print(" --- был удален из списка.");
            } else if (curFile.getAbsoluteFile().equals(targetFile)) {
                fileIterator.remove();
                System.out.print(" --- был удален из списка.");
            } else if (curFile.getAbsoluteFile().equals(targetNewFileName)) {
                fileIterator.remove();
                System.out.print(" --- был удален из списка.");
            }

            System.out.println();
        }

        System.out.println("---Список после чистки---");
        for (File file : listOfFiles) {
            System.out.println(file.getAbsoluteFile());
        }

        // Соритруем только по имени файла метдом пузырька
        for (int i = listOfFiles.size() - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (listOfFiles.get(j).getName().compareToIgnoreCase(listOfFiles.get(j + 1).getName()) > 0) {
                    File tmpFile = listOfFiles.get(j + 1);
                    listOfFiles.remove(j + 1);
                    listOfFiles.add(j + 1, listOfFiles.get(j));
                    listOfFiles.remove(j);
                    listOfFiles.add(j, tmpFile);
                }
            }
        }

        System.out.println("---Список после сортировки---");
        for (File file : listOfFiles) {
            System.out.println(file.getAbsoluteFile());
        }

        // удаляем allFilesContent.txt, если он существует
        if (FileUtils.isExist(targetNewFileName))
            FileUtils.deleteFile(targetNewFileName);

        // переименовываем resultFileAbsolutePath в allFilesContent.txt
        FileUtils.renameFile(targetFile, targetNewFileName);

        // записать каждый файл из списка в файл allFilesContent.txt согласно условию

        // открываем поток записи в файл allFilesContent.txt
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(targetNewFileName), 200);

        for (int i = 0; i < listOfFiles.size(); i++) {
            // открываем поток чтения каждого файла из списка
            BufferedReader fileReader = new BufferedReader(new FileReader(listOfFiles.get(i)), 51);
            while (fileReader.ready()) {
                fileWriter.write(fileReader.readLine());
                if (fileReader.ready())
                    fileWriter.newLine();
            }
            ///////////////
            fileWriter.write("\n");
            // закрываем поток чтения каждого файла из списка
            fileReader.close();
        }
        // закрываем поток записи в файл allFilesContent.txt
        fileWriter.close();
    }

    private static void getAllFiles(File path, List<File> listOfFiles) {
        if (path.isDirectory()) {
            List<File> tmpListOfFiles = new ArrayList<>();
            for (File curFile : path.listFiles()) {
                getAllFiles(curFile, tmpListOfFiles);
            }
            listOfFiles.addAll(tmpListOfFiles);
        } else
            listOfFiles.add(path);
    }

    private static List<File> getAllFiles(File path) {
        List<File> listOfFiles = new ArrayList<>();
        if (path.isDirectory()) {
            List<File> tmpListOfFiles = new ArrayList<>();
            for (File curFile : path.listFiles()) {
                getAllFiles(curFile, tmpListOfFiles);
            }
            listOfFiles.addAll(tmpListOfFiles);
        } else
            listOfFiles.add(path);
        return listOfFiles;
    }
}
