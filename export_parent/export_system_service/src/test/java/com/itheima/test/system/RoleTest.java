package com.itheima.test.system;


import com.itheima.service.system.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-*.xml")
public class RoleTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void testFindTreeJson() {
        List<Map> list = roleService.findTreeJson("4028a1cd4ee2d9d6014ee2df4c6a0001");
        for (Map map : list) {
            System.out.println(map);
        }
    }
}
