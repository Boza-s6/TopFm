package nemanja.bozovic.topfm.data;

import android.support.annotation.Nullable;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nemanja.bozovic.topfm.models.Song;


public class SongsXMLParser {

    static final String TAG_PREVIOUS = "previous";
    static final String TAG_SONG = "song";
    static final String TAG_AUTHOR = "artist";
    static final String TAG_TITLE = "title";

    private Document mDocument;

    public SongsXMLParser(String xml) {
        mDocument = getDocument(xml);
    }

    /**
     * @return <code>null</code> on error
     */
    @Nullable
    private Document getDocument(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            return db.parse(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Song> getSongs() {

        NodeList previous = mDocument.getElementsByTagName(TAG_PREVIOUS); // should be one
        List<Song> songs = new ArrayList<>(previous.getLength());

        NodeList songsTags = previous.item(0).getChildNodes();

        for (int j = 0; j < songsTags.getLength(); j++) {
            NodeList songTag = songsTags.item(j).getChildNodes();

            for (int i = 0; i < songTag.getLength(); i++) {

                Node child = songsTags.item(j);
                NodeList sList = child.getChildNodes();
                for (int k = 0; k < sList.getLength(); k++) {
                    Node c = sList.item(k);
                    String author = null, name = null;
                    String nodeName = c.getNodeName();
                    switch (nodeName) {
                        case TAG_AUTHOR:
                            author = child.getTextContent();
                        case TAG_TITLE:
                            name = child.getTextContent();
                            break;
                        default:
                            continue;
                    }
                    songs.add(new Song(author.trim(), name.trim()));
                    break;
                }

            }
        }
        return songs;
    }
}
