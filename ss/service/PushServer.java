package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * @author stanomy
 *
 */
public class PushServer {

	// 请求
	private static Map<String, URL> producerMap = new ConcurrentHashMap<String, URL>();
	// 响应结果
	private static Map<String, List<PushResp>> consumerMap = new ConcurrentHashMap<String, List<PushResp>>();
	// 显示
	private static Map<String, PushResp> showMap = new ConcurrentHashMap<String, PushResp>();

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

	public void json(String rs) {
		if (null != rs && !rs.isEmpty()) {
			String[] rss = rs.split("~");
			if (null != rss && rss.length > 6) {

				if (consumerMap.containsKey(rss[2])) {
					List<PushResp> resp = consumerMap.get(rss[2]);
					final PushResp pushResp = new PushResp(rss[2], rss[5],
							rss[3]);
					pushResp.setS1(rss[5]);
					pushResp.setS2(rss[3]);
					// s1和上一次不等才进行保存
					if (!resp.get(resp.size() - 1).getS1()
							.equals(pushResp.getS1())) {
						resp.add(pushResp);
						consumerMap.put(pushResp.getCode(), resp);
						showMap.put(pushResp.getCode(), pushResp);
					}

				} else {
					final PushResp resp = new PushResp(rss[2], rss[5], rss[3]);

					consumerMap.put(resp.getCode(), new LinkedList<PushResp>() {
						{
							add(resp);
						}
					});
					showMap.put(resp.getCode(), resp);
				}
			}
		}
	}

	public void show() throws SigarException {
		Iterator<Entry<String, PushResp>> iterator = showMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, PushResp> entry = iterator.next();
			// last one
			String s = ReMix.getInfo(entry.getValue());
			p(s);
			// urlConn.setAllowUserInteraction(false);
			// urlConn.setDoOutput(true);
		}
		showMap.clear();// 每次清理
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

					producerMap.put(str, new URL(Config.URLSTRING
							+ getPrefix(str)));

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

	/**
	 * get prefix from code
	 * 
	 * @param code
	 * @return
	 */
	public String getPrefix(String code) {
		if (null != code) {
			if (code.startsWith("6"))
				return "sh" + code;
			else
				return "sz" + code;
		}
		return "";
	}
}
