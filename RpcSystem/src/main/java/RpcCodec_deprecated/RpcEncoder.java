package RpcCodec_deprecated;
/*package RpcCodec;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {
	private Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    public void encode(ChannelHandlerContext ctx, RpcMessage in, ByteBuf out) throws Exception {
    	
    	Object body = in.getBody();
    	int markpos = out.writerIndex();
//    	logger.info("encode::markpos:{}",markpos);
    	out.writeBytes(LENGTH_PLACEHOLDER);
    	out.writeByte(in.getHeader().getType());
    	logger.info("encode ,,temp writerindex:{}",out.writerIndex());
    	if(body!=null){
    		byte[] data = SerializeUtil.serializeWithProtostuff(body);
    		logger.info("data length:{}",data.length);
    		out.writeBytes(data);
    		out.setInt(markpos, out.writerIndex()-markpos-5);
    		logger.info("encode::datalength:{},writerindex:{}",out.writerIndex()-markpos-5,out.writerIndex());
    	}else{
    		out.setInt(markpos, 0);
    	}
    }
}
*/