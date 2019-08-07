package RegistryCodec;

import java.util.List;


import MessageUtils.Header;
import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import configutils.NormalConfig;
import configutils.ServiceConfig;
import configutils.ServiceRegist;
import configutils.TokenConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RegistryDecoder extends ByteToMessageDecoder{

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
			Class cls = null;
			if(Header.REGISTRY_NORMALCONFIG == headtype){
				cls = NormalConfig.class;
			}else if(Header.REGISTRY_TOKENCONFIG == headtype){
				cls = TokenConfig.class;
			}else{
				cls = ServiceRegist.class;
			}
			
			//Class cls = Header.REGISTRY_NORMALCONFIG == headtype ? NormalConfig.class : TokenConfig.class;
			Object obj = (Object) SerializeUtil.deserializeWithProtostuff(body, cls);
			out.add(new RpcMessage(header, obj));
		}else{
			out.add(new RpcMessage(header, null));
		}
	}
	
}
