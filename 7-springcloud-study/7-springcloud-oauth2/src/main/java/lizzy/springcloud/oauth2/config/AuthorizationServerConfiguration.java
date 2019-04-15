package lizzy.springcloud.oauth2.config;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import lizzy.springcloud.oauth2.service.MyUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
	oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//把client的信息保存在内存中
		clients.inMemory() 
			.withClient("client") // client_id
			.secret("secret") // client_secret
			.authorizedGrantTypes("authorization_code") // 该client允许的授权类型
			.scopes("app"); // 允许的授权范围
	}
	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// @formatter:off
		endpoints.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
			//加入JWT
			.accessTokenConverter(accessTokenConverter());
	}
	
	/**
	 * jwt配置
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				String username = authentication.getName();
				final Map<String, Object> additionalInformation  = new HashMap<>();
				additionalInformation.put("user_name", username);
				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
				OAuth2AccessToken token = super.enhance(accessToken, authentication);
				return token;
			}
		};
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("lizzy_key.jks"), "123456".toCharArray()).getKeyPair("lizzy_key");
		converter.setKeyPair(keyPair);
		return converter;
	}
	
}
