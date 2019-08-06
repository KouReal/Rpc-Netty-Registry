package configutils;

public class NormalConfig {
	//rpcclient默认重连次数/连接超时时间(毫秒）
	private int DEFAULT_RECONNECT_TRY;
	private int DEFAULT_CONNECT_TIMEOUT;
	
	//Token最大生命期（毫秒）
	private int TOKEN_LIFE;
	
	//rpcserver绑定的端口
	private int RPCSERVER_PORT;
	
	//针对rpc连接的读写空闲时间(毫秒）
	//客户端：写空闲
	//服务端:读空闲
	private int CLIENT_WRITE_IDLE;
	private int SERVER_READ_IDLE;
	
	public NormalConfig(int dEFAULT_RECONNECT_TRY, int dEFAULT_CONNECT_TIMEOUT, int tOKEN_LIFE, int rPCSERVER_PORT,
			int cLIENT_WRITE_IDLE, int sERVER_READ_IDLE) {
		super();
		DEFAULT_RECONNECT_TRY = dEFAULT_RECONNECT_TRY;
		DEFAULT_CONNECT_TIMEOUT = dEFAULT_CONNECT_TIMEOUT;
		TOKEN_LIFE = tOKEN_LIFE;
		RPCSERVER_PORT = rPCSERVER_PORT;
		CLIENT_WRITE_IDLE = cLIENT_WRITE_IDLE;
		SERVER_READ_IDLE = sERVER_READ_IDLE;
	}
	public int getDEFAULT_RECONNECT_TRY() {
		return DEFAULT_RECONNECT_TRY;
	}
	public void setDEFAULT_RECONNECT_TRY(int dEFAULT_RECONNECT_TRY) {
		DEFAULT_RECONNECT_TRY = dEFAULT_RECONNECT_TRY;
	}
	public int getDEFAULT_CONNECT_TIMEOUT() {
		return DEFAULT_CONNECT_TIMEOUT;
	}
	public void setDEFAULT_CONNECT_TIMEOUT(int dEFAULT_CONNECT_TIMEOUT) {
		DEFAULT_CONNECT_TIMEOUT = dEFAULT_CONNECT_TIMEOUT;
	}
	public int getTOKEN_LIFE() {
		return TOKEN_LIFE;
	}
	public void setTOKEN_LIFE(int tOKEN_LIFE) {
		TOKEN_LIFE = tOKEN_LIFE;
	}
	public int getRPCSERVER_PORT() {
		return RPCSERVER_PORT;
	}
	public void setRPCSERVER_PORT(int rPCSERVER_PORT) {
		RPCSERVER_PORT = rPCSERVER_PORT;
	}
	public int getCLIENT_WRITE_IDLE() {
		return CLIENT_WRITE_IDLE;
	}
	public void setCLIENT_WRITE_IDLE(int cLIENT_WRITE_IDLE) {
		CLIENT_WRITE_IDLE = cLIENT_WRITE_IDLE;
	}
	public int getSERVER_READ_IDLE() {
		return SERVER_READ_IDLE;
	}
	public void setSERVER_READ_IDLE(int sERVER_READ_IDLE) {
		SERVER_READ_IDLE = sERVER_READ_IDLE;
	}
	

}
