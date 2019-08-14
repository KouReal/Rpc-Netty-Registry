package RegistryCodec;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.RegistryMessage;
import MessageUtils.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToByteEncoder;

public class RegistryEncoder extends MessageToByteEncoder<RegistryMessage> {
	private static Logger logger = LoggerFactory.getLogger(RegistryEncoder.class);

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    public void encode(ChannelHandlerContext ctx, RegistryMessage in, ByteBuf out) throws Exception {
//    	logger.info("{} encoding:{}",Thread.currentThread(),in);
    	
    	Object body = in.getBody();
    	int markpos = out.writerIndex();
    	out.writeBytes(LENGTH_PLACEHOLDER);
    	out.writeByte(in.getHeader().getType());
    	if(body!=null){
    		byte[] data = SerializeUtil.serializeWithProtostuff(body);
    		out.writeBytes(data);
    		out.setInt(markpos, out.writerIndex()-markpos-5);
    	}else{
    		out.setInt(markpos, 0);
    	}
    }
}
