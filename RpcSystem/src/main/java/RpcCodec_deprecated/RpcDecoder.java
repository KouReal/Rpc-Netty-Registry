package RpcCodec_deprecated;
/*package RpcCodec;

import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.StaticApplicationContext;

import MessageUtils.Header;
import MessageUtils.RpcMessage;
import MessageUtils.RpcRequest;
import MessageUtils.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RpcDecoder extends ByteToMessageDecoder{
	private Logger logger = LoggerFactory.getLogger(RpcDecoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		// TODO Auto-generated method stub
		//datalength:body所占的字节数
		int datalength = in.readInt();
		byte headtype = in.readByte();
		logger.info("rpcdecoder get buf:, datalength:{},headtype:{},readerindex:{},writerindex:{}",datalength,headtype,in.readerIndex(),in.writerIndex());
		Header header = new Header(datalength, headtype);
		if(datalength>0){
			byte[] body = new byte[datalength];
			in.readBytes(body);
			
			Class<?> cls = Header.RPC_REQUEST == headtype ? RpcRequest.class : RpcResponse.class;
			Object obj = (Object) SerializeUtil.deserializeWithProtostuff(body, cls);
			out.add(new RpcMessage(header, obj));
		}else{
			out.add(new RpcMessage(header, null));
		}
	}
	
}
*/