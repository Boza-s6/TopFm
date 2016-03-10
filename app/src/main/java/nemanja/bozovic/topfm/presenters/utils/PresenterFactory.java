package nemanja.bozovic.topfm.presenters.utils;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import nemanja.bozovic.topfm.utils.Factory;

public interface PresenterFactory<T extends MvpPresenter> extends Factory<T> {
}
