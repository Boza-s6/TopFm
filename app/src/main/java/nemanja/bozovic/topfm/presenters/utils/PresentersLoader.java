package nemanja.bozovic.topfm.presenters.utils;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import nemanja.bozovic.topfm.utils.Factory;


public class PresentersLoader<T extends MvpPresenter> extends Loader<T> {
    private static final String TAG = "PresentersLoader";
    private T mPresenter;
    private PresenterFactory<T> mPresenterFactory;

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad: ");
        mPresenter = mPresenterFactory.create();
        deliverResult(mPresenter);
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset: ");
        mPresenter.detachView(false);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        if (mPresenter != null) {
            deliverResult(mPresenter);
        } else {
            forceLoad();
        }
    }

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public PresentersLoader(Context context, PresenterFactory<T> presenterFactory) {
        super(context);
        mPresenterFactory = presenterFactory;
    }
}
