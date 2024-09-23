package cn.acyou.leo.tool.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2020/6/29]
 **/
@Service
public class UserService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername  根据用户名查找用户" + username);
        Set<String> authoritySet = new HashSet<>();
        List<String> roleCodeList = new ArrayList<>();

        authoritySet.add("demo:list");
        roleCodeList.add("ROLE_ADMIN");
        roleCodeList.add("ROLE_AUDITOR");

        authoritySet.addAll(roleCodeList);
        Set<SimpleGrantedAuthority> collect = authoritySet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return User.builder()
                .username(username)//用户名
                .password("123")//密码
                .disabled(false)//帐户是否可用             DisabledException
                .accountExpired(false)//帐户是否过期       AccountExpiredException
                .accountLocked(false)//帐户是否被锁定       LockedException
                .authorities(collect)//权限列表
                .build();
    }
}
