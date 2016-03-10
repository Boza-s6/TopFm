package nemanja.bozovic.topfm.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nemanja.bozovic.topfm.R;
import nemanja.bozovic.topfm.adapters.SongsAdapter;
import nemanja.bozovic.topfm.models.Song;
import nemanja.bozovic.topfm.presenters.Last10Presenter;


public class Last10Fragment
        extends MvpLceViewStateFragment<SwipeRefreshLayout, List<Song>, Last10View, Last10Presenter>
        implements Last10View, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "Last10Fragment";

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private SongsAdapter mSongsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.last10fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        contentView.setOnRefreshListener(this);

        mSongsAdapter = new SongsAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongsAdapter);
        loadData(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "ERROR";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().detachView(true);
    }

    @NonNull
    @Override
    public LceViewState<List<Song>, Last10View> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        //handled by super class
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override
    public List<Song> getData() {
        return mSongsAdapter == null ? null : mSongsAdapter.getData();
    }

    @Override
    public void setData(List<Song> data) {
        mSongsAdapter.setSongs(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadSongs(pullToRefresh);
    }

    @NonNull
    @Override
    public Last10Presenter createPresenter() {
        return new Last10Presenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadSongs(true);
    }
}
