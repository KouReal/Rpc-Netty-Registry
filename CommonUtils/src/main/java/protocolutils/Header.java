package protocolutils;

public enum Header{
	rpc_request(0x1234a001),
	rpc_response(0x1234a002),
    reg_addservice(0x1234a003),  
	reg_normalconfig(0x1234a004),
	reg_discover(0x1234a005),    	
	reg_tokenconfig(0x1234a006),
    heart_beat(0x1234a007);  
	
    private final int id;
    private Header(int id) {
    	this.id = id;
    }
	
	
	public static Header getHeader(int id){
		for (Header header: Header.values()) {
			if(header.getId() == id){
				return header;
			}
		}
		return null;
	}
	

	public int getId() {
		return id;
	}
}

/*public class Header {
	public static final int rpc_request = 0x1234a001;
	public static final int rpc_response = 0x1234a002;
	public static final int reg_addservice = 0x1234a003;
	public static final int reg_normalconfig = 0x1234a004;
	public static final int reg_discover = 0x1234a005;
	public static final int reg_tokenconfig = 0x1234a006;
	public static final int heart_beat = 0x1234a007;
}
*/