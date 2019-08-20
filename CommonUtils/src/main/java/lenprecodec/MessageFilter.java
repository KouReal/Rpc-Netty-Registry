package lenprecodec;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocolutils.Header;
import protocolutils.LenPreMsg;

@ChannelHandler.Sharable
public class MessageFilter extends SimpleChannelInboundHandler<LenPreMsg>{
	private Logger logger = LoggerFactory.getLogger(MessageFilter.class);
	private List<Header> protocol_whitelist = new ArrayList<>();
	public MessageFilter(List<Header> list) {
		this.protocol_whitelist = list;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
//		logger.info("messagefilter get msg:{}",msg);
		if(protocol_whitelist.size()==0){
			logger.info("白名单为空，拒绝所有消息进入");
		}
		Header header = msg.getHeader();
//		Integer id = msg.getProtocol();
		if(protocol_whitelist.contains(header)){
			logger.debug("允许消息类型:{}进入",header);
			ctx.fireChannelRead(msg);
		}else{
			logger.info("拒绝消息类型:{}进入",header);
		}
	}

}
