package fr.mmtech.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fr.mmtech.web.rest.dto.GuessDTO;
import fr.mmtech.web.rest.dto.VideoImdbDTO;

public class IMDBUtil {
    // private Properties types;
    private final String URL = "http://www.omdbapi.com/";

    private static Logger logger = Logger.getLogger(IMDBUtil.class);

    public IMDBUtil() {
	// types = new TypeUtil().getProperties();
    }

    /**
     * Récupère une liste de propositions par IMDB correspondant au mot clé
     * 
     * @param keyword
     * @return
     * @throws IMDBException
     */
    public List<GuessDTO> getKeywordGuess(String keyword) throws Exception {
	// String response = makeRequestFromKeyword(keyword);

	// TODO Bouchon
	String response = "<root response=\"True\"><Movie Title=\"The Dark Knight\" Year=\"2008\" imdbID=\"tt0468569\" Type=\"movie\"/><Movie Title=\"The Dark Knight Rises\" Year=\"2012\" imdbID=\"tt1345836\" Type=\"movie\"/><Movie Title=\"Transformers: Dark of the Moon\" Year=\"2011\" imdbID=\"tt1399103\" Type=\"movie\"/><Movie Title=\"Thor: The Dark World\" Year=\"2013\" imdbID=\"tt1981115\" Type=\"movie\"/><Movie Title=\"Zero Dark Thirty\" Year=\"2012\" imdbID=\"tt1790885\" Type=\"movie\"/><Movie Title=\"Dark Shadows\" Year=\"2012\" imdbID=\"tt1077368\" Type=\"movie\"/><Movie Title=\"Dark City\" Year=\"1998\" imdbID=\"tt0118929\" Type=\"movie\"/><Movie Title=\"Dancer in the Dark\" Year=\"2000\" imdbID=\"tt0168629\" Type=\"movie\"/><Movie Title=\"Dark Water\" Year=\"2005\" imdbID=\"tt0382628\" Type=\"movie\"/><Movie Title=\"Dark Skies\" Year=\"2013\" imdbID=\"tt2387433\" Type=\"movie\"/></root>";

	if (response != null)
	    return buildGuessListFromKeywordResponse(response);
	else {
	    logger.debug("No response from imdb");
	    throw new Exception("Le service IMDB ne répond pas.");
	}
    }

    /**
     * Récupère les données imdb de la video en fonction de son id imdb
     * 
     * @param imdbId
     * @return
     * @throws IMDBException
     */
    public VideoImdbDTO getVideo(String imdbId) throws Exception {
	// String response = makeRequestFromId(imdbId);

	// TODO bouchon
	String response = "<root response=\"True\"><movie title=\"The Social Network\" year=\"2010\" rated=\"PG-13\" released=\"01 Oct 2010\" runtime=\"120 min\" genre=\"Biography, Drama\" director=\"David Fincher\" writer=\"Aaron Sorkin (screenplay), Ben Mezrich (book)\" actors=\"Jesse Eisenberg, Rooney Mara, Bryan Barter, Dustin Fitzsimons\" plot=\"Harvard student Mark Zuckerberg creates the social networking site that would become known as Facebook, but is later sued by two brothers who claimed he stole their idea, and the cofounder who was later squeezed out of the business.\" language=\"English, French\" country=\"USA\" awards=\"Won 3 Oscars. Another 102 wins &amp; 86 nominations.\" poster=\"\" metascore=\"95\" imdbRating=\"7.8\" imdbVotes=\"335,226\" imdbID=\"tt1285016\" type=\"movie\"/></root>";

	if (response != null)
	    return buildVideoFromIdResponse(response);
	else {
	    logger.debug("No response from imdb");
	    throw new Exception("Le service IMDB ne répond pas.");
	}
    }

    /**
     * Récupère une unique proposition de vidéo basée sur son titre
     * 
     * @param keyword
     * @return
     * @throws IMDBException
     */
    // public Video getTitleGuess(String title) throws Exception {
    // String response = makeRequestFromTitle(title);
    //
    // //TODO Bouchon
    // // String response =
    // "<root response=\"True\"><Movie Title=\"The Dark Knight\" Year=\"2008\" imdbID=\"tt0468569\" Type=\"movie\"/><Movie Title=\"The Dark Knight Rises\" Year=\"2012\" imdbID=\"tt1345836\" Type=\"movie\"/><Movie Title=\"Transformers: Dark of the Moon\" Year=\"2011\" imdbID=\"tt1399103\" Type=\"movie\"/><Movie Title=\"Thor: The Dark World\" Year=\"2013\" imdbID=\"tt1981115\" Type=\"movie\"/><Movie Title=\"Zero Dark Thirty\" Year=\"2012\" imdbID=\"tt1790885\" Type=\"movie\"/><Movie Title=\"Dark Shadows\" Year=\"2012\" imdbID=\"tt1077368\" Type=\"movie\"/><Movie Title=\"Dark City\" Year=\"1998\" imdbID=\"tt0118929\" Type=\"movie\"/><Movie Title=\"Dancer in the Dark\" Year=\"2000\" imdbID=\"tt0168629\" Type=\"movie\"/><Movie Title=\"Dark Water\" Year=\"2005\" imdbID=\"tt0382628\" Type=\"movie\"/><Movie Title=\"Dark Skies\" Year=\"2013\" imdbID=\"tt2387433\" Type=\"movie\"/></root>";
    //
    //
    // if (response != null)
    // return buildVideoFromTitleResponse(new Video(), response);
    // else {
    // logger.debug("No response from imdb");
    // throw new Exception("Le service IMDB ne répond pas.");
    // }
    // }

    /**
     * Contact imdb with an imdbId to get data for a movie
     * 
     * @param imdbId
     * @return response from imdb
     */
    private String makeRequestFromId(String imdbIdParam) {
	String url = null;
	try {
	    url = URL + "?i=" + URLEncoder.encode(imdbIdParam, "utf8") + "&r=XML";
	} catch (UnsupportedEncodingException e) {
	    logger.error(e);
	}

	return makeRequest(url);
    }

    /**
     * Contact imdb with a string to get data for a movie
     * 
     * @param titleParam
     * @return response from imdb
     */
    private String makeRequestFromKeyword(String param) {
	String url = null;
	try {
	    url = URL + "?s=" + URLEncoder.encode(param, "utf8") + "&r=XML";
	} catch (UnsupportedEncodingException e) {
	    logger.error(e);
	}

	return makeRequest(url);
    }

    /**
     * Contact imdb with a string to get data for a movie
     * 
     * @param titleParam
     * @return response from imdb
     */
    private String makeRequestFromTitle(String titleParam) {
	String url = null;
	try {
	    url = URL + "?t=" + URLEncoder.encode(titleParam, "utf8") + "&r=XML";
	} catch (UnsupportedEncodingException e) {
	    logger.error(e);
	}

	return makeRequest(url);
    }

    /**
     * Contacte l'url et lit la réponse
     * 
     * @param url
     * @return
     */
    private String makeRequest(String url) {
	URL obj;
	try {
	    obj = new URL(url);

	    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	    // optional default is GET
	    con.setRequestMethod("GET");

	    int responseCode = con.getResponseCode();
	    logger.debug("Sending 'GET' request to URL : " + url);
	    logger.debug("Response Code : " + responseCode);

	    if (responseCode == 200) {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine);
		}
		in.close();

		logger.debug(response.toString());
		return response.toString();
	    }
	} catch (MalformedURLException e) {
	    logger.error(e);
	} catch (ProtocolException e) {
	    logger.error(e);
	} catch (IOException e) {
	    logger.error(e);
	}

	return null;
    }

    /**
     * Parse une réponse IMDB pour récupérer les infos d'une video (requete sur
     * l'id)
     * 
     * @param response
     * @return
     */
    private VideoImdbDTO buildVideoFromIdResponse(String response) {
	VideoImdbDTO video = new VideoImdbDTO();
	try {
	    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(response));
	    Document doc = db.parse(is);

	    NodeList nList = doc.getElementsByTagName("movie");
	    Node nNode = nList.item(0);
	    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		Element movie = (Element) nNode;

		video.setTitle(movie.getAttribute("title"));
		video.setYear(movie.getAttribute("year"));
		video.setDuration(movie.getAttribute("runtime"));
		video.setType(movie.getAttribute("genre"));
		video.setImg(movie.getAttribute("poster"));
		video.setRate(movie.getAttribute("imdbRating"));
		video.setImdbId(movie.getAttribute("imdbID"));
		return video;
	    }
	} catch (NullPointerException ne) {
	    return null;
	} catch (Exception e) {
	    logger.error(e);
	}

	return null;
    }

    /**
     * Parse une réponse IMDB pour récupérer les infos d'une video (requete sur
     * le titre)
     * 
     * @param response
     * @return
     */
    // private Video buildVideoFromTitleResponse(Video video, String response) {
    //
    // try {
    // DocumentBuilder db =
    // DocumentBuilderFactory.newInstance().newDocumentBuilder();
    // InputSource is = new InputSource();
    // is.setCharacterStream(new StringReader(response));
    // Document doc = db.parse(is);
    //
    // NodeList nList = doc.getElementsByTagName("movie");
    // Node nNode = nList.item(0);
    // if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    // Element movie = (Element) nNode;
    //
    // video.setTitle(movie.getAttribute("title"));
    // video.setYear(Integer.decode(movie.getAttribute("year")));
    // video.setDuration(buildDuration(movie.getAttribute("runtime")));
    // video.setType(buildType(movie.getAttribute("genre")));
    // video.setImgFile(new MFile(movie.getAttribute("poster"), EFileFlag.IMG));
    // video.setRate(movie.getAttribute("imdbRating"));
    // video.setImdbId(movie.getAttribute("imdbID"));
    // return video;
    // }
    // } catch (NullPointerException ne) {
    // return null;
    // } catch (Exception e) {
    // logger.error(e);
    // }
    //
    // return null;
    // }

    /**
     * Parse une réponse IMDB pour récupérer les propositions IMDB
     * 
     * @param response
     * @return
     */
    private List<GuessDTO> buildGuessListFromKeywordResponse(String response) {
	List<GuessDTO> list = new ArrayList<GuessDTO>();

	try {
	    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(response));
	    Document doc = db.parse(is);

	    NodeList nList = doc.getElementsByTagName("Movie");
	    for (int cpt = 0; cpt < nList.getLength(); cpt++) {
		Node nNode = nList.item(cpt);
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		    Element movie = (Element) nNode;

		    GuessDTO guess = new GuessDTO(movie.getAttribute("imdbID"), movie.getAttribute("Title"), movie.getAttribute("Year"), movie.getAttribute("Type"));
		    list.add(guess);
		}
	    }
	} catch (NullPointerException ne) {
	    return null;
	} catch (Exception e) {
	    logger.error(e);
	}

	return list;
    }
}
