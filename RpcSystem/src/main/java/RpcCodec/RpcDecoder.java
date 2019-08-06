package RpcCodec;

import java.util.List;


import MessageUtils.Header;
import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RpcDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		//datalength:body所占的字节数
		int datalength = in.readInt();
		byte headtype = in.readByte();
		Header header = new Header(datalength, headtype);
		if(datalength>0){
			byte[] body = new byte[datalength];
			in.readBytes(body);
			
			Class cls = Header.RPC_REQUEST == headtype ? RpcRequest.class : RpcResponse.class;
			Object obj = (Object) SerializeUtil.deserializeWithProtostuff(body, cls);
			out.add(new RpcMessage(header, obj));
		}else{
			out.add(new RpcMessage(header, null));
		}
	}
	
}
