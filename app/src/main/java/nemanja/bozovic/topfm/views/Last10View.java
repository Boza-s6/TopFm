package nemanja.bozovic.topfm.views;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import nemanja.bozovic.topfm.models.Song;

/**
 * View for showing last 10 songs
 */
public interface Last10View extends MvpLceView<List<Song>>{
    // MvpLceView already defines LCE methods:
    //
    // void showLoading(boolean pullToRefresh)
    // void showError(Throwable t, boolean pullToRefresh)
    // void setData(List<Country> data)
    // void showContent()
}
