package nemanja.bozovic.topfm.data;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nemanja.bozovic.topfm.models.Song;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FakeSongLoader extends AsyncTask<Void, Void, List<Song>> {
    public interface SongLoaderListener {
        void songsLoaded(List<Song> songs);

        void onError(LoadingException e);
    }
    private static int NUM = 0;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    Request mRequest = new Request.Builder().url("http://spider.intellex.rs/TopFM/onair").build();
    private SongLoaderListener mListener;

    public FakeSongLoader(SongLoaderListener listener) {
        mListener = listener;
    }

    @Override
    protected List<Song> doInBackground(Void... params) {
        String xml;
        try {
            long start = SystemClock.currentThreadTimeMillis();
            Response response = mOkHttpClient.newCall(mRequest).execute();
            long end = SystemClock.currentThreadTimeMillis();
            xml = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


//        SystemClock.sleep(3000);
        List<Song> songs = new SongsXMLParser(xml).getSongs();

        return songs;
    }

    @Override
    protected void onPostExecute(List<Song> songs) {
        if (songs != null) {
            mListener.songsLoaded(songs);
        } else {
            mListener.onError(new LoadingException());
        }
    }

    public void setListener(SongLoaderListener listener) {
        mListener = listener;
    }

    public class LoadingException extends Exception {
    }
}
