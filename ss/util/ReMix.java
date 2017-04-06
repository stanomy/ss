package util;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import pojo.PushResp;
import config.Config;

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
		builder.append(mem.getTotal() / 1024L + "K av");
		builder.append(Config.Q);
		builder.append(mem.getUsed() / 1024L + "K used");
		builder.append(Config.Q);
		if (null != resp) {

			builder.append(resp.getS1() + "m");
			builder.append(Config.Q);
			builder.append(resp.getS2() + "m");
			builder.append(Config.Q);

		}

		builder.append(r.availableProcessors() + "cpus");

		return builder.toString();
	}
}
