package cn.edu.jxau.dao;

import java.util.List;

public interface CURD<T,E> {
    
    List<T> select();
    
    T selectOne(E id);
    
    int delete(E id);
    
    int insert(T item);
    
    int update(T item);
}