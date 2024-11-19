package com.example.demo.repository.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.Status;

@Mapper
@Repository
public interface MyBatisAccountRepository {

    @Insert("INSERT INTO accounts " +
            "(name, email, password, status, profile_image, furigana, age, introduction, is_deleted, role, adminstatus) " +
            "VALUES (#{name}, #{email}, #{password}, #{status}, NULL, NULL, NULL, NULL, 0, 'ROLE_ADMIN', #{adminstatus})")
    void createAdminAccount(String name, String email, String password, Status status, boolean adminstatus);

}
