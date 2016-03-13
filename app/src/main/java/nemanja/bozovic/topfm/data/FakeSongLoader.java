package nemanja.bozovic.topfm.data;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.bozovic.nemanja.Either;

import java.io.IOException;
import java.util.List;

import nemanja.bozovic.topfm.models.Song;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FakeSongLoader
        extends AsyncTask<Void, Void, Either<FakeSongLoader.LoadingException, List<Song>>> {
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
    protected Either<LoadingException, List<Song>> doInBackground(Void... params) {
        String xml;
        try {
            long start = SystemClock.currentThreadTimeMillis();
            Response response = mOkHttpClient.newCall(mRequest).execute();
            long end = SystemClock.currentThreadTimeMillis();
            xml = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return Either.left(new LoadingException());
        }


//        SystemClock.sleep(3000);
        List<Song> songs = new SongsXMLParser(xml).getSongs();

        return Either.right(songs);
    }

    @Override
    protected void onPostExecute(Either<LoadingException, List<Song>> songs) {
        if (songs.isRight()) {
            mListener.songsLoaded(songs.getRight());
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
