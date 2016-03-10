package nemanja.bozovic.topfm.models;

/**
 * Song model
 * TODO Add picure
 */
public class Song {
    private String mAuthor, mSongName;

    public Song(String author, String songName) {
        mAuthor = author;
        mSongName = songName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSongName() {
        return mSongName;
    }
}
