package protocolutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotationutils.MyMessage;
import exceptionutils.ProtocolException;

public class ProtocolMap {
	private static Logger logger = LoggerFactory.getLogger(ProtocolMap.class);
//	private static Map<Integer, String> pmap_name = new HashMap<>();
//	private static Map<Integer,Class<?>> pmap = new HashMap<>();
	private static Map<Integer, Header> idmap = new HashMap<>();
	private static Map<String, Header> namemap = new HashMap<>();
	private static Map<Header, Class<?>> clzmap = new HashMap<>();
	
	private static void setmap() throws ProtocolException{
		for (Header header : Header.values()) {
			idmap.put(header.getId(), header);
			namemap.put(header.name(), header);
		}
		Reflections reflections = new Reflections("protocolutils");
		Set<Class<?>> msg_cls_set = reflections.getTypesAnnotatedWith(MyMessage.class);
		logger.info("class sets:{}",msg_cls_set);
		String name;
		Header header;
		for (Class<?> msg_cls : msg_cls_set) {
			MyMessage anno = msg_cls.getAnnotation(MyMessage.class);
			name = anno.value();
			header = namemap.get(name);
			if(header == null){
				throw new ProtocolException("协议class:"+msg_cls+"的MyMessage注解值:【"+name+"】对应的协议未定义");
			}
			clzmap.put(header, msg_cls);
		}
	}
	public static Header getheader(Integer id) throws ProtocolException{
		if(idmap.isEmpty()){
			setmap();
		}
		return idmap.get(id);
	}
	public static Class<?> getclass(Header header) throws ProtocolException{
		if(clzmap.isEmpty()){
			setmap();
		}
		return clzmap.get(header);
	}
}
