package fr.mmtech.service.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mmtech.repository.FileRepository;
import fr.mmtech.service.VideoService;

public class CleanerUtil {

    private String root;
    private String garbage;

    private FileRepository fileRepository;

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    public CleanerUtil(String root, String garbage, FileRepository fileRepository) {
	this.root = root;
	this.garbage = garbage;
	this.fileRepository = fileRepository;
    }

    public void clearAppDir() throws IOException {

	FileVisitor<Path> fileProcessor = new ProcessFile();
	Files.walkFileTree(Paths.get(root), fileProcessor);
    }

    private final class ProcessFile extends SimpleFileVisitor<Path> {
	@Override
	public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
	    log.debug("file:" + aFile);
	    String name = aFile.toString().substring(root.length());
	    if (fileRepository.findAllByPath(name).isEmpty()) {
		// on ne récupère que le nom du fichier
		if (name.contains(String.valueOf(java.io.File.separatorChar))) {
		    name = name.substring(name.lastIndexOf(java.io.File.separatorChar));
		}

		java.io.File f = aFile.toFile();
		log.debug("moving " + name + " to " + garbage + java.io.File.separatorChar + name);
		if (!f.renameTo(new java.io.File(garbage + java.io.File.separatorChar + name))) {
		    log.debug("moving failed");
		}
	    }
	    return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path aDir, IOException exc) throws IOException {
	    log.debug("directory:" + aDir);

	    java.io.File dir = aDir.toFile();
	    if (dir.list().length == 0) {
		log.debug("empty -> deleting");
		if (!dir.delete()) {
		    log.debug("deleting failed");
		}
	    }
	    return FileVisitResult.CONTINUE;
	}
    }

}
