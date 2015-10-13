package builders;

import javax.xml.ws.Service;

public interface WebServiceBuilder {
	public Service buildWebService(String webAppName, String idAddress);
	public Object buildPort(String webAppName, String ipAddress);
}
