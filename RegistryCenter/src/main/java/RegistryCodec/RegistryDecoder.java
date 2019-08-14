package RegistryCodec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessageUtils.Header;
import MessageUtils.RegistryMessage;
import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import TokenUtils.Token;
import configutils.NormalConfig;
import configutils.ServiceRegist;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RegistryDecoder extends ByteToMessageDecoder{
	private static Logger logger = LoggerFactory.getLogger(RegistryDecoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		//datalength:body所占的字节数
//		logger.info("{} decoding:{}",Thread.currentThread(),in.toString());
		int datalength = in.readInt();
		byte headtype = in.readByte();
		Header header = new Header(datalength, headtype);
		if(datalength>0){
			byte[] body = new byte[datalength];
			in.readBytes(body);
			Class cls = null;
			if(Header.REGISTRY_NORMALCONFIG == headtype){
				cls = NormalConfig.class;
			}else if(Header.REGISTRY_TOKEN == headtype){
				cls = Token.class;
			}else{
				cls = ServiceRegist.class;
			}
			
			//Class cls = Header.REGISTRY_NORMALCONFIG == headtype ? NormalConfig.class : TokenConfig.class;
			Object obj = (Object) SerializeUtil.deserializeWithProtostuff(body, cls);
			out.add(new RegistryMessage(header, obj));
		}else{
			out.add(new RegistryMessage(header, null));
		}
	}
	
}
