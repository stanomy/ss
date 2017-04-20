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
import java.util.Random;
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

				final PushResp pushResp = new PushResp(rss[2], rss[5], rss[3]);

				if (consumerMap.containsKey(pushResp.getCode())) {
					List<PushResp> resp = consumerMap.get(rss[2]);
					pushResp.setS1(rss[5]);
					pushResp.setS2(rss[3]);
					// 相同校验，不同则加入显示
					if (!checkFactor(resp)) {
						showMap.put(pushResp.getCode(), pushResp);
					}
					resp.add(pushResp);
					consumerMap.put(pushResp.getCode(), resp);
				} else {
					// 初始化
					consumerMap.put(pushResp.getCode(),
							new LinkedList<PushResp>() {
								{
									add(pushResp);
								}
							});
					showMap.put(pushResp.getCode(), pushResp);
				}
			}
		}
	}

	private boolean checkFactor(List<PushResp> pList) {
		boolean isSame = true;

		if (null == pList || pList.isEmpty() || pList.size() <= Config.FACTOR) {

			return !isSame;
		}

		int sum = pList.size();

		int i = 1;
		while (i <= Config.FACTOR && isSame) {
			// 从尾部开始比较，比较factor次
			if (pList.get(sum - 1).getS1()
					.equals(pList.get(sum - 1 - i).getS1())) {
				isSame = isSame & true;
			} else {
				isSame = false;
			}
			i++;
		}
		return isSame;
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
			 * 解析配置，生成账户信息here,进行系数放大读取(0-100)，反正为空不存
			 */
			for (int i = 0; i < 100; i++) {
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

	public void clearShow() {
		showMap.clear();
	}

	public void clearResp() {
		consumerMap.clear();

	}
}
