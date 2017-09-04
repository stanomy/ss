package util;

import java.util.Random;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import pojo.PushResp;
import config.Config;

/**
 * 
 * @author stanomy
 *
 */
public class ReMix {

	public static String getInfo(String[] str) throws SigarException {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		Runtime r = Runtime.getRuntime();
		StringBuilder builder = new StringBuilder();
		builder.append(r.totalMemory() / 1024L + "K ");
		builder.append(Config.Q);
		builder.append(r.freeMemory() / 1024L + "K ");
		builder.append(Config.Q);
		builder.append(mem.getTotal() / 1024L + "K av");
		builder.append(Config.Q);
		builder.append(mem.getUsed() / 1024L + "K used");
		builder.append(Config.Q);

		if (null != str && str.length > 0) {
			for (String s : str) {

				builder.append(s + "m");
				builder.append(Config.Q);
			}
		}
		builder.append(r.availableProcessors() + "cpus");

		return builder.toString();
	}

	public static String getInfo(PushResp resp) throws SigarException {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		Runtime r = Runtime.getRuntime();
		StringBuilder builder = new StringBuilder();
		builder.append(r.totalMemory() / 1024L + "K ");
		builder.append(Config.Q);
		builder.append(r.freeMemory() / 1024L + "K ");
		builder.append(Config.Q);
		if (null != resp) {
			//换算为1亿
			builder.append(new Double(resp.getS3())/10000 + " av");
		} else {
			builder.append(mem.getTotal() / 1024L + "K av");
		}
		builder.append(Config.Q);
		String s = mem.getUsed() / 1024L + resp.getName().toUpperCase()
				+ "K used";
		builder.append(s);
		builder.append(Config.Q);
		boolean isDown = false;
		if (null != resp) {
			if (resp.getS1().startsWith("-")) {
				isDown = true;
				resp.setS1(resp.getS1().replace("-", ""));
			}

			// 添加固定prefix
			builder.append(Integer.valueOf(resp.getCode().substring(3, 6))
					.toString());
			builder.append(resp.getS1() + "m");
			builder.append(Config.Q);
			builder.append(resp.getS2() + "m");
			builder.append(Config.Q);

		}

		int cpus = r.availableProcessors();
		if (isDown)
			cpus--;
		builder.append(cpus + "cpus");

		return builder.toString();
	}
}
