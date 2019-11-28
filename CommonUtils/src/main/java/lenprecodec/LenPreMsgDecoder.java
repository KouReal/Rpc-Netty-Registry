package lenprecodec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocolutils.Header;
import protocolutils.LenPreMsg;
import protocolutils.ProtocolMap;

public class LenPreMsgDecoder extends ByteToMessageDecoder{
	private static Logger logger = LoggerFactory.getLogger(LenPreMsgDecoder.class);
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//		logger.info("decode: readinx:{},readable:{},writeidx:{}",in.readerIndex(),in.readableBytes(),in.writerIndex());
		if(in.readableBytes()<LenPreMsg.BASE_PRE_LEN){
//			logger.info("readable too little");
			return ;
		}
		int beginindex;
//		int protocol_id;
		Header header;
		while(true){
			beginindex = in.readerIndex();
			in.markReaderIndex();
//			logger.info("before readInt:,readerindex:{},readable:{}",in.readerIndex(),in.readableBytes());
//			logger.info("protocolid:{}",protocol_id);
			header = ProtocolMap.getheader(in.readInt());
			if(header != null){
				break;
			}
			in.resetReaderIndex();
			in.readByte();
			if(in.readableBytes()<LenPreMsg.BASE_PRE_LEN){
				return;
			}
		}
		ByteBuf msgid_buf = in.readBytes(36);
		byte[] msgid_bytes = new byte[36];
		msgid_buf.readBytes(msgid_bytes);
		String msgid = new String(msgid_bytes);
		
		int len = in.readInt();
//		logger.info("read len:{}",len);
		
		if(len==0){
			//heartbeat
			out.add(new LenPreMsg(header, msgid, 0, null));
			return ;
		}
//		logger.info("after read length,readablebytes:{}",in.readableBytes());
		if(in.readableBytes()<len){
			in.readerIndex(beginindex);
			return ;
		}
		byte[] data = new byte[len];
		in.readBytes(data);
		Class<?> protocol_cls = ProtocolMap.getclass(header);
//		logger.info("start serialize,cls:{}",protocol_cls);
		Object obj = (Object) SerializeUtil.deserializeWithProtostuff(data, protocol_cls);
		logger.info("decoder get obj:{}",obj);
		out.add(new LenPreMsg(header,msgid, len, obj));
		
		
	}
	

}
