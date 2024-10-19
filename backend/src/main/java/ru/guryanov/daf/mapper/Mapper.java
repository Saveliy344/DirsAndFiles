package ru.guryanov.daf.mapper;

public interface Mapper<T, D>{
    D toDTO(T t);
    T fromDTO(D d);
}
