package RpcCodec;


import MessageUtils.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    @Override
    public void encode(ChannelHandlerContext ctx, RpcMessage in, ByteBuf out) throws Exception {
    	
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
