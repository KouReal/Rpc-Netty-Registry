package RpcServer;

import org.springframework.stereotype.Component;

import TokenUtils.TokenCache;
import TokenUtils.TokenFactory;

@Component
public class RpcTackFactory {
	private TokenCache tokenCache;
	private ServiceHolder serviceHolder;
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

	public RpcTackFactory(TokenCache tokenCache, ServiceHolder serviceHolder) {
		super();
		this.tokenCache = tokenCache;
		this.serviceHolder = serviceHolder;
	}
	
}
