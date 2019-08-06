package httpserver;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;

@Component
public class HttpServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
	@Autowired
	private ServerBootstrap serverBootstrap;
	
	@Autowired
	private InetSocketAddress tcpSocketAddress;

	
	@PostConstruct
	public void start() throws UnknownHostException{
		try {
			serverBootstrap.bind(tcpSocketAddress).sync();
			LOGGER.info("http服务器启动，地址：{}:{}",InetAddress.getLocalHost().getHostAddress(),tcpSocketAddress.getPort() );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
