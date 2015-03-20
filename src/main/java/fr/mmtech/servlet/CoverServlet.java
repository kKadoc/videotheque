package fr.mmtech.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.mmtech.repository.ConfigFieldRepository;

@SuppressWarnings("serial")
public class CoverServlet extends HttpServlet {
	
	@Autowired
	private ConfigFieldRepository configRepository;
	
	public static final String MAPPING_URL = "/cover";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
			      config.getServletContext());
	}
	
	public static interface LookupResult {
		public void respondGet(HttpServletResponse resp) throws IOException;

		public void respondHead(HttpServletResponse resp);

		public long getLastModified();
	}

	public static class Error implements LookupResult {
		protected final int statusCode;
		protected final String message;

		public Error(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public long getLastModified() {
			return -1;
		}

		public void respondGet(HttpServletResponse resp) throws IOException {
			resp.sendError(statusCode, message);
		}

		public void respondHead(HttpServletResponse resp) {
			throw new UnsupportedOperationException();
		}
	}

	public static class StaticFile implements LookupResult {
		protected final long lastModified;
		protected final String mimeType;
		protected final int contentLength;
		protected final boolean acceptsDeflate;
		protected final String path;

		public StaticFile(long lastModified, String mimeType,
				int contentLength, boolean acceptsDeflate, String path) {
			this.lastModified = lastModified;
			this.mimeType = mimeType;
			this.contentLength = contentLength;
			this.acceptsDeflate = acceptsDeflate;
			this.path = path;
		}

		public long getLastModified() {
			return lastModified;
		}

		protected boolean willDeflate() {
			return acceptsDeflate && deflatable(mimeType)
					&& contentLength >= deflateThreshold;
		}

		protected void setHeaders(HttpServletResponse resp) {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType(mimeType);
			if (contentLength >= 0 && !willDeflate())
				resp.setContentLength(contentLength);
		}

		public void respondGet(HttpServletResponse resp) throws IOException {
			setHeaders(resp);
			final OutputStream os;
			if (willDeflate()) {
				resp.setHeader("Content-Encoding", "gzip");
				os = new GZIPOutputStream(resp.getOutputStream(), bufferSize);
			} else
				os = resp.getOutputStream();

			FileInputStream fis = new FileInputStream(path);
			transferStreams(fis, os);
		}

		public void respondHead(HttpServletResponse resp) {
			if (willDeflate())
				throw new UnsupportedOperationException();
			setHeaders(resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		lookup(req).respondGet(resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		try {
			lookup(req).respondHead(resp);
		} catch (UnsupportedOperationException e) {
			super.doHead(req, resp);
		}
	}

	@Override
	protected long getLastModified(HttpServletRequest req) {
		return lookup(req).getLastModified();
	}

	protected LookupResult lookup(HttpServletRequest req) {
		LookupResult r = (LookupResult) req.getAttribute("lookupResult");
		if (r == null) {
			r = lookupNoCache(req);
			req.setAttribute("lookupResult", r);
		}
		return r;
	}

	protected LookupResult lookupNoCache(HttpServletRequest req) {
		System.out.println("DEBUG SERVICE = "+configRepository);
		String path = getPath(req);
		if (isForbidden(path))
			return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");

		path = path.replace(MAPPING_URL, "");
		path = configRepository.getPath()+path;

		final String mimeType = getMimeType(path);

		// Try as an ordinary file
		File f = new File(path);
		if (!f.isFile())
			return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
		else
			return new StaticFile(f.lastModified(), mimeType, (int) f.length(),
					acceptsDeflate(req), path);
	}

	protected String getPath(HttpServletRequest req) {
		String servletPath = req.getServletPath();
		String pathInfo = coalesce(req.getPathInfo(), "");
		return servletPath + pathInfo;
	}

	protected boolean isForbidden(String path) {
		String lpath = path.toLowerCase();
		return lpath.startsWith("/web-inf/") || lpath.startsWith("/meta-inf/");
	}

	protected String getMimeType(String path) {
		return coalesce(getServletContext().getMimeType(path),
				"application/octet-stream");
	}

	protected static boolean acceptsDeflate(HttpServletRequest req) {
		final String ae = req.getHeader("Accept-Encoding");
		return ae != null && ae.contains("gzip");
	}

	protected static boolean deflatable(String mimetype) {
		return mimetype.startsWith("text/")
				|| mimetype.equals("application/postscript")
				|| mimetype.startsWith("application/ms")
				|| mimetype.startsWith("application/vnd")
				|| mimetype.endsWith("xml");
	}

	public static <T> T coalesce(T... ts) {
		for (T t : ts)
			if (t != null)
				return t;
		return null;
	}

	protected static final int deflateThreshold = 4 * 1024;

	protected static final int bufferSize = 4 * 1024;

	protected static void transferStreams(InputStream is, OutputStream os)
			throws IOException {
		try {
			byte[] buf = new byte[bufferSize];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1)
				os.write(buf, 0, bytesRead);
		} finally {
			is.close();
			os.close();
		}
	}
}
