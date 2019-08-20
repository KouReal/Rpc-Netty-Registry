package protocolutils;

import java.io.Serializable;

import annotationutils.MyMessage;

@MyMessage("reg_addservice")
public class RegService implements Serializable{
		private String servicename;
		private String addr;
		
		public RegService(String servicename, String addr) {
			this.servicename = servicename;
			this.addr = addr;
		}
		public String getServicename() {
			return servicename;
		}
		public void setServicename(String servicename) {
			this.servicename = servicename;
		}
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		@Override
		public String toString() {
			return "ServiceRegist [servicename=" + servicename + ", addr=" + addr + "]";
		}
}

