package fr.mmtech.service.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CleanerUtil {

    private String root;
    private String garbage;

    public CleanerUtil(String root, String garbage) {
	this.root = root;
	this.garbage = garbage;
    }

    public static void main(String[] args) {
	CleanerUtil t = new CleanerUtil("C:\\tmp", "C:\\tmp2");
	try {
	    t.clearAppDir();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void clearAppDir() throws IOException {

	FileVisitor<Path> fileProcessor = new ProcessFile();
	Files.walkFileTree(Paths.get(root), fileProcessor);

    }

    private final class ProcessFile extends SimpleFileVisitor<Path> {
	@Override
	public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
	    System.out.println("file:" + aFile);
	    System.out.println(root);
	    String name = aFile.toString().substring(root.length());
	    System.out.println(name);
	    return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path aDir, IOException exc) throws IOException {
	    System.out.println("directory:" + aDir);

	    java.io.File dir = aDir.toFile();
	    if (dir.list().length == 0) {
		System.out.println("empty");
		// dir.delete();
	    }
	    return FileVisitResult.CONTINUE;

	}
    }

}
