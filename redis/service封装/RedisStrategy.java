package com.mytaotao.common.service;

//回调,E是方法的参数类型,T是方法的返回类型
public interface RedisStrategy<E,T> {

    T handle(E jedis);
}
