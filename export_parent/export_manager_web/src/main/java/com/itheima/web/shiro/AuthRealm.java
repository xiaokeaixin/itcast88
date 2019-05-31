package com.itheima.web.shiro;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义reaml域
 * 此时就已经明确了里面应该包含的方法
 *      认证：用户名和密码的校验
 *      授权：获取用户的权限和每次访问的权限鉴定（鉴权）
 */
public class AuthRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    /**
     * 授权的方法 可以配合标签实现鉴权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.取出认证成功的用户
        User user = (User) principals.getPrimaryPrincipal();
        //2.查询该用户所具备的功能
        List<Module> moduleList = userService.finUserMenus(user.getId());
        Set<String> moduleSet = new HashSet<>();
        for (Module module : moduleList) {
            moduleSet.add(module.getName());
        }
        //3.创建返回值对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //4.按照返回值对象要求填充数据
        info.setStringPermissions(moduleSet);//set集合就是用户可以操作的模块，集合中的元素都是string类型的存的是模块的名称
        //5.返回
        return info;
    }

    /**
     * 认证的方法
     *      使用用户名和密码去数据库查询
     * @param token
     * @return 如果此方法返回null，则subject就会抛出异常
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.把参数转成usernamepasswordToken
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        //2.获取邮箱和密码
        String email = uToken.getUsername();
        String password = new String(uToken.getPassword());
        //3.使用邮箱去数据库查询
        User user = userService.findByEmail(email);
        //4.当User对象不为null时，我们按照返回值要求创建对象并返回
        if (user != null) {
            //此处按照当前方法的返回值要求即可
            //创建返回值对象
            //使用该对象创建时的构造函数嗲用自定义密码比较器
            //构造函数需要使用三参：第一个参数：是从数据库查询出来的用户对象 第二个参数：密文密码 第三个参数：Realm域的名称
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            return info;
        }
        //此处直接返回null
        return null;
    }
}
