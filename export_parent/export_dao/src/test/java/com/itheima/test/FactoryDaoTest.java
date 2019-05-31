package com.itheima.test;

import com.itheima.dao.cargo.FactoryDao;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class FactoryDaoTest {

    @Autowired
    private FactoryDao factoryDao;

    @Test
    public void testFindOne() {
        Factory factory = factoryDao.selectByPrimaryKey("1");
        System.out.println(factory);
    }

    /**
     *  select * from co_factory
     *  where  这里的内容是靠criteria对象补全的
     *  group by   having
     *  order by   这里的内容是靠exmaple对象补全的
     */
    @Test
    public void testFindAll() {
        //1.创建例子 特例查询
        FactoryExample factoryExample = new FactoryExample();
        //2.创建例子中的条件对象
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        //3.设置查询条件
        criteria.andCtypeEqualTo("货物");
        //4.加入排序实用的example对象
        factoryExample.setOrderByClause("create_time desc");
        List<Factory> factories = factoryDao.selectByExample(factoryExample);
        /*for (Factory factory : factories) {
            System.out.println(factory);
        }*/
        //System.out.println(factories.size());
        System.out.println(factories.get(0).getFactoryName());
    }
}
