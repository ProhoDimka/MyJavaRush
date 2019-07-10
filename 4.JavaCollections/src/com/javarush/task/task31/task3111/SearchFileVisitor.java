package com.javarush.task.task31.task3111;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {
    private String partOfName;
    private String partOfContent;
    private int minSize;
    private int maxSize;
    private List<Path> foundFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        byte[] content = Files.readAllBytes(file); // размер файла: content.length
        String strContent = new String(content);
        boolean isFounded = true;
        if (partOfName != null)
            //isFounded = file.getFileName().toString().matches(".*" + partOfName + ".*");
            isFounded = file.getFileName().toString().contains(partOfName);
        if (partOfContent != null && isFounded)
            //isFounded = strContent.matches(".*" + partOfContent + ".*");
            isFounded = strContent.contains(partOfContent);
        if (maxSize != 0 && minSize != 0 && isFounded)
            isFounded = (long) content.length < maxSize && (long) content.length > minSize;
        else if (maxSize != 0 && isFounded)
            isFounded = (long) content.length < maxSize;
        else if (minSize != 0 && isFounded)
            isFounded = (long) content.length > minSize;
        if (isFounded)
            foundFiles.add(file);
        /*System.out.println("Для файла " + file.getFileName() +
                " проверяем что в названии есть подстрока " + partOfName +
                " результат:" + file.getFileName().toString().matches(".*" + partOfName + ".*"));
        System.out.println("Для файла " + file.getFileName() +
                " проверяем что содержит подстроку " + partOfContent +
                " результат:" + strContent.contains(partOfContent));*/
        //System.out.println(strContent);
        return super.visitFile(file, attrs);
    }

    public List<Path> getFoundFiles() {
        return foundFiles;
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
