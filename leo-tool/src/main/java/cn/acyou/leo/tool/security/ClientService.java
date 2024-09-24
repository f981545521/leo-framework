package cn.acyou.leo.tool.security;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author youfang
 * @version [1.0.0, 2024/9/24 9:18]
 **/
@Slf4j
@Service
public class ClientService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.info("loadClientByClientId  根据clientId查找用户" + clientId);
        if (clientId.startsWith("client_")) {
            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(clientId);
            clientDetails.setClientSecret("123456");
            Set<SimpleGrantedAuthority> collect = Sets.newLinkedHashSet("order:get", "ROLE_AUDITOR").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            clientDetails.setAuthorities(collect);
            clientDetails.setAuthorizedGrantTypes(Lists.newArrayList("authorization_code", "client_credentials", "password", "refresh_token"));
            clientDetails.setScope(Lists.newArrayList("all"));
            clientDetails.setRegisteredRedirectUri(Sets.newLinkedHashSet("http://www.baidu.com"));
            clientDetails.setAccessTokenValiditySeconds(3600);
            clientDetails.setRefreshTokenValiditySeconds(864000);
            return clientDetails;
        }
        throw new UsernameNotFoundException("client 不存在");
    }
}
