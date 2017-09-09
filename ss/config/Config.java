package config;

public class Config {
	public final static String Q = "\t";
	public final static String SPLIT = ";";
	public final static String URLSTRING = "http://qt.gtimg.cn/q=s_";
	public static final Integer LIMIT = 10;
	public static final Integer FACTOR = 3;
	//沪A,深A,沪B,深B
	public static final String[] ALL_SS={
		"http://stock.gtimg.cn/data/view/rank.php?t=rankash/chr&p=1&o=0&l=4000&v=list_data",
		"http://stock.gtimg.cn/data/view/rank.php?t=rankasz/chr&p=1&o=0&l=4000&v=list_data",
		"http://stock.gtimg.cn/data/view/rank.php?t=rankbsh/chr&p=1&o=0&l=4000&v=list_data",
		"http://stock.gtimg.cn/data/view/rank.php?t=rankbsz/chr&p=1&o=0&l=4000&v=list_data"
		};
	
	public static final String ALL_SS2="http://quote.eastmoney.com/stocklist.html";
	

}
