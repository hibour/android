package com.dsquare.hibour.listener;

public interface MessageStateResultCallBack<T> {
  void onResultCallBack(T object, int state, Exception e);
}
