
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * //rsw=v_s_sh601212="1~xxxx~601212~15.59~0.32~2.10~2844191~435087~~1087.09";
 * 
 * @author yy
 *
 */
public class PushServer {

	private String urlString = "http://qt.gtimg.cn/q=";
	private static String Q = "\t";
	private static String SPLIT = ";";

	public void push(String str) throws IOException, SigarException {

		URL url = new URL(str);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				url.openStream(), "UTF-8"))) {
			for (String line; (line = reader.readLine()) != null;) {

				this.getInfo(this.json(line));
			}
		}
	}

	public String[] json(String rs) {
		StringBuilder t = null;
		if (null != rs && !rs.isEmpty()) {
			String[] rss = rs.split("~");
			if (null != rss && rss.length > 6) {
				t = new StringBuilder();
				t.append(rss[5] + SPLIT);
				t.append(rss[3] + SPLIT);
			}
		}
		return (null == t) ? null : t.toString().split(SPLIT);
	}

	public void getInfo(String[] str) throws SigarException {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		Runtime r = Runtime.getRuntime();
		StringBuilder builder = new StringBuilder();
		builder.append(r.totalMemory() / 1024L + "K ");
		builder.append(Q);
		builder.append(r.freeMemory() / 1024L + "K ");
		builder.append(Q);
		builder.append(mem.getTotal() / 1024L + "K av");
		builder.append(Q);
		builder.append(mem.getUsed() / 1024L + "K used");
		builder.append(Q);
		if (null != str && str.length > 0) {
			for (String s : str) {
				builder.append(s + "%");
				builder.append(Q);
			}
		}

		builder.append(r.availableProcessors() + "cpus");

		System.out.println(builder.toString());
	}

	public void getSFromConfigFile() {
		Properties properties = PropertyConfig.getInstance().getConfig("D:\\other\\s.properties",
				"/config/s.properties", "ss");
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
					this.push(urlString + str);
				} else
					continue;
			}
//			System.out.println("从配置文件读取了" + properties.size() + "个信息。");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
