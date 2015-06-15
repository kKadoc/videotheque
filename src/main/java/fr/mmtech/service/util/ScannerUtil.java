package fr.mmtech.service.util;

import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mmtech.config.Consts;
import fr.mmtech.domain.ScanResult;
import fr.mmtech.service.VideoService;

public class ScannerUtil {

    private String root;
    private List<ScanResult> listFiles;

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    public ScannerUtil(String root, List<ScanResult> listFiles) {
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

		if (Consts.extensionsVideo.contains(extension)) {
		    log.debug(path + " video file");
		    ScanResult sr = new ScanResult(path, null);

		    // si on est dans un sous répertoire du root
		    if (path.replaceAll(root, "").indexOf(java.io.File.separatorChar) != -1) {
			// on cherche un sous titre
			java.io.File[] listSubs = aFile.toFile().getParentFile().listFiles(new FilenameFilter() {
			    public boolean accept(java.io.File file, String fileName) {
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
				return Consts.extensionsSub.contains(extension);
			    }
			});

			// par defaut on utilise le premier
			if (listSubs.length > 0) {
			    sr.setSubPath(listSubs[0].getAbsolutePath());
			}
		    }

		    listFiles.add(sr);
		}
	    }

	    return FileVisitResult.CONTINUE;
	}
    }

}
