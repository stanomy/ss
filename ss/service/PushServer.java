package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.hyperic.sigar.SigarException;
import pojo.PushResp;
import config.Config;
import util.PropertyConfig;
import util.ReMix;

/**
 * //rsw=v_s_sh601212="1~xxxx~601212~15.59~0.32~2.10~2844191~435087~~1087.09";
 * 
 * @author yy
 *
 */
public class PushServer {

	private static Map<String, URL> producerMap = new ConcurrentHashMap<String, URL>();
	private static Map<String, PushResp> consumerMap = new ConcurrentHashMap<String, PushResp>(
			);

	public PushServer() {
		super();
		this.getSFromConfigFile();
	}

	public void push() throws IOException, SigarException {

		Iterator<Entry<String, URL>> iterator = producerMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, URL> entry = iterator.next();
			this.doPush(entry.getValue());

			// urlConn.setAllowUserInteraction(false);
			// urlConn.setDoOutput(true);
		}
	}

	private void doPush(URL url) throws IOException, SigarException {

		URLConnection urlConn = url.openConnection();
		urlConn.setConnectTimeout(15000);// 15s超时
		urlConn.setReadTimeout(15000);// 15s
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlConn.getInputStream(), "UTF-8"))) {
			for (String line; (line = reader.readLine()) != null;) {

				this.json(line);
			}
		}
	}

	private void p(String str) {
		System.out.println(str);
	}

	public String[] json(String rs) {
		StringBuilder t = null;
		if (null != rs && !rs.isEmpty()) {
			String[] rss = rs.split("~");
			if (null != rss && rss.length > 6) {

				if (consumerMap.containsKey(rss[2])) {
					PushResp resp = consumerMap.get(rss[2]);
					resp.setS1(rss[5]);
					resp.setS2(rss[3]);
					consumerMap.put(resp.getCode(), resp);
				} else {
					PushResp resp = new PushResp();
					// t = new StringBuilder();
					resp.setS1(rss[5]);
					resp.setS2(rss[3]);
					resp.setCode(rss[2]);
					consumerMap.put(resp.getCode(), resp);
				}

				// t.append(rss[5] + Config.SPLIT);
				// t.append(rss[3] + Config.SPLIT);
			}
		}
		return (null == t) ? null : t.toString().split(Config.SPLIT);
	}

	public void show() throws SigarException {
		Iterator<Entry<String, PushResp>> iterator = consumerMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, PushResp> entry = iterator.next();
			String s = ReMix.getInfo(entry.getValue());
			p(s);
			// urlConn.setAllowUserInteraction(false);
			// urlConn.setDoOutput(true);
		}
	}

	public void getSFromConfigFile() {
		Properties properties = PropertyConfig.getInstance().getConfig(
				"D:\\other\\s.properties", "/config/s.properties", "ss");
		try {

			if (null == properties) {
				return;
			}
			String str = null;

			/**
			 * 解析配置，生成账户信息here
			 */
			for (int i = 0; i < properties.size(); i++) {
				str = properties.getProperty("code" + i);
				if (null != str && !"".equals(str)) {

					producerMap.put(str, new URL(Config.URLSTRING + str));

				} else
					continue;
			}
			// System.out.println("从配置文件读取了" + properties.size() + "个信息。");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
