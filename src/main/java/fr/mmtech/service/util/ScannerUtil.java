package fr.mmtech.service.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mmtech.service.VideoService;

public class ScannerUtil {

    private String root;
    private List<String> listFiles;

    private static final List<String> extensions = Arrays.asList("avi", "mp4");

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    public ScannerUtil(String root, List<String> listFiles) {
	this.root = root;
	this.listFiles = listFiles;
    }

    public void scan() throws IOException {
	FileVisitor<Path> fileProcessor = new ProcessFile();
	Files.walkFileTree(Paths.get(root), fileProcessor);
    }

    private final class ProcessFile extends SimpleFileVisitor<Path> {
	@Override
	public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
	    log.debug("file:" + aFile);
	    String path = aFile.toString();
	    if (path.indexOf(".") != -1) {
		String extension = path.substring(path.lastIndexOf(".") + 1);

		if (extensions.contains(extension)) {
		    log.debug("video file");

		    listFiles.add(path);
		}
	    }

	    return FileVisitResult.CONTINUE;
	}
    }

}
