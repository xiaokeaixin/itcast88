package com.itheima.web.shiro;

import com.itheima.commons.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义密码比较器
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     * 比较密码：
     * 明文密码和密文密码对比
     * 结论：永远不会一样
     * 所以：
     * 要么秘闻解密（不能解密）
     * 要么明文加密
     * <p>
     * 此方法要做的事情
     * 1.取出明文
     * 2.取出密文
     * 3.把明文加密
     * 4.把密文比较
     *
     * @param token 令牌中存着明文
     * @param info  认证信息中存着密文
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.取出明文密码
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String email = uToken.getUsername();
        String password = new String(uToken.getPassword());
        //2.取出密文密码
        String dbPassword = (String) info.getCredentials();
        //3.把明文密码加密
        String md5Password = Encrypt.md5(password, email);
        //4.比较两个密码，并返回比较结果，返回flase时抛出异常，true表示验证成功
        return md5Password.equals(dbPassword);
    }
}
