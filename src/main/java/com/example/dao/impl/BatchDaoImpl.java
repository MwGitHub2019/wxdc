package com.example.dao.impl;

import com.example.dao.BatchDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void batchInsert(List<T> list) {

        int size = list.size();
        //循环存入缓存区
        for (int i = 0; i < size ; i++) {
            em.persist(list.get(i));
            //每一百条写入一次数据库 如果不足一百条 就直接将全部数据写入数据库
            if (i % 100 == 0 || i == size - 1){

                em.flush();
                em.clear();
            }
        }
    }
}
