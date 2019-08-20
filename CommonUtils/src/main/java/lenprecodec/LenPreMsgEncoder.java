package lenprecodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import protocolutils.LenPreMsg;

public class LenPreMsgEncoder extends MessageToByteEncoder<LenPreMsg>{
	private static Logger logger = LoggerFactory.getLogger(LenPreMsgEncoder.class);
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	  	
	@Override
	protected void encode(ChannelHandlerContext ctx, LenPreMsg msg, ByteBuf out) throws Exception {
//		logger.info("encode msg:{}",msg);
		Object body = msg.getBody();
    	
		//4
    	out.writeInt(msg.getHeader().getId());
    	//36
    	out.writeBytes(msg.getMsgid().getBytes());
    	//4
    	int markpos = out.writerIndex();
    	out.writeBytes(LENGTH_PLACEHOLDER);
    	
    	if(body!=null){
//    		logger.info("start serialize");
    		byte[] data = SerializeUtil.serializeWithProtostuff(body);
//    		logger.info("encode to bytes:body:{}",data);
//    		logger.info("msg data length:{}",data.length);
    		out.writeBytes(data);
    		out.setInt(markpos, out.writerIndex()-markpos-4);
    	}else{
    		out.setInt(markpos, 0);
    		
    	}
    	
	}

}
