package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate") // 后面加("newName")给bean重命名，从而强制容器返回bean
public class AlphaDaoHibernateImpl implements AlphaDao {

    @Override
    public String select() {
        return "Hibernate";
    }
}
