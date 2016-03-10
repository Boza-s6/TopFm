package nemanja.bozovic.topfm.presenters;

import android.os.AsyncTask;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import nemanja.bozovic.topfm.data.FakeSongLoader;
import nemanja.bozovic.topfm.models.Song;
import nemanja.bozovic.topfm.views.Last10View;


public class Last10Presenter extends MvpBasePresenter<Last10View> implements FakeSongLoader.SongLoaderListener {
    private boolean mIsPullToRefresh;
    private FakeSongLoader mFakeSongLoader;

    public void loadSongs(boolean pullToRefresh) {

        if (pullToRefresh || mFakeSongLoader == null) {
            //force load data
            //noinspection ConstantConditions
            getView().showLoading(pullToRefresh);
            mFakeSongLoader = new FakeSongLoader(this);
            mFakeSongLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        mIsPullToRefresh = pullToRefresh;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSongLoaderIfRunning();
        }
    }

    private void cancelSongLoaderIfRunning() {
        if (mFakeSongLoader != null) {
            mFakeSongLoader.cancel(true);
            mFakeSongLoader.setListener(null);
            mFakeSongLoader = null;
        }
    }

    @Override
    public void songsLoaded(List<Song> songs) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().setData(songs);
            getView().showContent();
        }
    }

    @Override
    public void onError(FakeSongLoader.LoadingException e) {
        if (isViewAttached()) {
            //noinspection ConstantConditions
            getView().setData(null);
            getView().showError(e, mIsPullToRefresh);
        }
    }
}
