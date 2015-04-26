package builders;

import javax.xml.ws.Service;

public interface WebServiceBuilder {
	public Service buildWebService(String webAppName);
	public Object buildPort(String webAppName);
}
