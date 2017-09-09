package service.inf;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 维护SS code的服务
 * @author stanomy
 *
 */
public interface SSCodeServiceInf {

	public void initCodeService();
	
	public Map<String, String> getCodeMap();
}
