package nemanja.bozovic.topfm.data;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import nemanja.bozovic.topfm.models.Song;


public class FakeSongLoader2 extends AsyncTaskLoader<List<Song>> {

    private List<Song> mData;

    public FakeSongLoader2(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            //use cached result
            deliverResult(mData);
        } else {
            // we have no data, start loading
            forceLoad();
        }
    }

    @Override
    public void deliverResult(List<Song> data) {
        // this is on main thread
        mData = data;
        super.deliverResult(data);
    }

    @Override
    public List<Song> loadInBackground() {
        SystemClock.sleep(3000);
        List<Song> songs = new ArrayList<>(100);

        for (int i = 0; i < 100; i++) {
            songs.add(new Song("Neko" + i, "Nesto" + i));
            if (isLoadInBackgroundCanceled()) {
                break;
            }
        }

        return songs;
    }


}
