/*package RegistryCodec_deprecated;

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
//			logger.info("decode get bytes:data:{}",body);
			Class<?> cls = null;
			
			switch (headtype) {
			case (Header.REGISTRY_NORMALCONFIG):
				cls = NormalConfig.class;
				break;
			case (Header.REGISTRY_TOKEN):
				cls = Token.class;
				break;
			case (Header.REGISTRY_SERVICE):
				cls = ServiceRegist.class;
				break;
			case (Header.REGISTRY_DISCOVER):
				cls = String.class;
				break;
			case (Header.REGISTRY_DISCOVER_REPLY):
				cls = String.class;
				break;
			default:
				break;
			}
			Header.HEART_BEAT_REQUEST
			if(Header.REGISTRY_NORMALCONFIG == headtype){
				cls = NormalConfig.class;
			}else if(Header.REGISTRY_TOKEN == headtype){
				cls = Token.class;
			}else if(Header.REGISTRY_SERVICE == headtype){
				cls = ServiceRegist.class;
			}else if(Header.)
			
			//Class cls = Header.REGISTRY_NORMALCONFIG == headtype ? NormalConfig.class : TokenConfig.class;
			Object obj = (Object) SerializeUtil.deserializeWithProtostuff(body, cls);
//			logger.info("decode result obj:type:{}, str:{}",obj.getClass(),obj);
			out.add(new RegistryMessage(header, obj));
		}else{
			out.add(new RegistryMessage(header, null));
		}
	}
	
}
*/