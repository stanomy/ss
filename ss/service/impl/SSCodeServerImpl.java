package service.impl;

import java.beans.Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.github.stuxuhai.jpinyin.PinyinHelper;

import config.Config;
import service.inf.SSCodeServiceInf;
import util.HttpClientUtil;

public class SSCodeServerImpl implements SSCodeServiceInf {

	private static ConcurrentHashMap<String, String> CODEMAP = new ConcurrentHashMap<String, String>();

	private Pattern rule = Pattern
			.compile("[\u4e00-\u9fa5]{3,4}\\([6|0|3]\\d{5}\\)");

	@Override
	public void initCodeService() {
		// TODO Auto-generated method stub
		String rs = "";
		try {
			rs = this.getRsFromAll_SS();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.analyzeRsToCache(rs);
	}

	// 返回所有的列表
	private String getRsFromAll_SS() throws UnsupportedEncodingException,
			IOException {
		String rsString = HttpClientUtil.doGet(Config.ALL_SS2, "GBK");
		// System.out.println(rsString);
		return rsString;
	}

	// 解析结果到cache(暂存内存，要看初始化时间，绝对是否放文件)Z
	private void analyzeRsToCache(String str) {
		if ("".equals(str))
			return;

		Matcher m = rule.matcher(str);
		while (m.find()) {

			analyzeItem(m.group(0));
			// System.out.println(m.group(0));
		}
	}

	/**
	 * style xxx(988888)
	 * 
	 * @param codeItem
	 */
	private void analyzeItem(String codeItem) {
		if (null == codeItem || "".equals(codeItem))
			return;

		// 剔除()
		codeItem = codeItem.replaceAll("[()]", "");

		// 取拼音首字母
		String key = PinyinHelper.getShortPinyin(
				codeItem.substring(0, codeItem.length() - 6)).toUpperCase();
		String value = codeItem.substring(codeItem.length() - 6,
				codeItem.length());
		// 目前取了拼首+code
		CODEMAP.put(key, value);
	}

	private void analyzeRsToFile() {
	}

	public static void main(String[] args) throws UnsupportedEncodingException,
			IOException {

		new SSCodeServerImpl().initCodeService();

	}
}
