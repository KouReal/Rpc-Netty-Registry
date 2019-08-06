package RpcServer;

import TokenUtils.TokenCache;
import TokenUtils.TokenFactory;

public class RpcTackFactory {
	private TokenCache tokenCache;
	private ServiceHolder serviceHolder;
	private TokenFactory tokenFactory;
	public TokenCache getTokenCache() {
		return tokenCache;
	}
	public void setTokenCache(TokenCache tokenCache) {
		this.tokenCache = tokenCache;
	}
	public ServiceHolder getServiceHolder() {
		return serviceHolder;
	}
	public void setServiceHolder(ServiceHolder serviceHolder) {
		this.serviceHolder = serviceHolder;
	}
	public TokenFactory getTokenFactory() {
		return tokenFactory;
	}
	public void setTokenFactory(TokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}
	public RpcTackFactory(TokenCache tokenCache, ServiceHolder serviceHolder, TokenFactory tokenFactory) {
		super();
		this.tokenCache = tokenCache;
		this.serviceHolder = serviceHolder;
		this.tokenFactory = tokenFactory;
	}
	
}
