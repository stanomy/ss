package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class HttpClientUtil {

	/**
	 * 发起http get请求
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String doGet(String url,String charset) throws UnsupportedEncodingException,
			IOException {
		URLConnection urlConn = new URL(url).openConnection();
		urlConn.setConnectTimeout(30000);// 30s超时
		urlConn.setReadTimeout(30000);// 30s
		StringBuffer rs = new StringBuffer();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlConn.getInputStream(), charset))) {
			for (String line; (line = reader.readLine()) != null;) {

				rs.append(line);
			}
		}
		return rs.toString();
	}
}
