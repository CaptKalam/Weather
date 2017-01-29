package com.paszylk.marcin.weather;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
