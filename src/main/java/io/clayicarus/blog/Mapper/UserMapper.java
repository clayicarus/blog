package io.clayicarus.blog.Mapper;

import io.clayicarus.blog.DataBase.DB_User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into users(name, account_id, token, gmt_create, gmt_modified) values(#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(DB_User user);
}
