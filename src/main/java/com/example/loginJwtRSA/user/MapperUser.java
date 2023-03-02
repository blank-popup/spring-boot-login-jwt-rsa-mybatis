package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperUser {
    List<ResponseUser> getAll();
    ResponseUser get(RequestUser requestUser);
    int create(RequestUser requestUser);
    int modify(RequestUser requestUser);
    int remove(RequestUser requestUser);
}
