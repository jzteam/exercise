package cn.jzteam.module.fastjson;

import cn.jzteam.test.Person;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.HashMap;
import java.util.Map;

public class TestFastJson {
	
	public static void main(String[] args) {
		
		
//		for(int i = 0;i<50;i++){
//			int num = new Random().nextInt(100) * 100;
//			System.out.println("第"+i+"次：num="+num);
//			Map<String, Object> data = makeData(num);
//			test1(data);
//			test2(data);
//		}
		test4();
	}

	private static void test4(){
		final Person person = new Person();
		person.setAge(1);
		person.setName("jzteam");
		final Object json = JSON.toJSON(person);
		System.out.println(json.getClass().getSimpleName());
		System.out.println(json);
	}

	public static void test3(){
		final JSONObject kycInfo = JSON.parseObject("{\"unitNo\":\"\",\"storeyNo\":\"广安路9号院\",\"city\":\"丰台区\",\"companyName\":\"ZHONG LIANG BAO\",\"directors\":\"[123,345]\",\"formerCompanyName\":\"ZHONG LIANG BAO\",\"dateOfIncorporation\":1353859200000,\"companyNumber\":\"91110106057321892B\",\"shareMes\":\"[]\",\"roadAddress\":\"广安路9号院\",\"provice\":\"北京市\",\"localLanguageCompanyName\":\"中量宝（北京）科技有限公司\",\"postCode\":\"\"}");
		final JSONArray directorsId = kycInfo.getJSONArray("unitNo");
		System.out.println(directorsId);
	}
	
	private static void test2(Map<String,Object> data){
		long start = System.currentTimeMillis();
		
		String jsonString = JSONObject.toJSONString(data);
        String orgStr = outputToOrgStr1(jsonString);
        Object bodyNew = JSONObject.parse(orgStr);
        String result = JSON.toJSONString(bodyNew);
//        System.out.println(result);
        
        System.out.println("advive方案耗时："+(System.currentTimeMillis() - start));
        System.out.println();
	}
	
	// filter
	private static void test1(Map<String,Object> data){
		ValueFilter valueFilter = new ValueFilter() {
			@Override
			public Object process(Object o, String propertyName, Object propertyValue) {
				
				// 只对字符串类型的值进行处理
				if (propertyValue instanceof String) {
					return outputToOrgStr(propertyValue.toString());
				}
				return propertyValue;
			}
		};
		
		long start = System.currentTimeMillis();
		String jsonString = JSON.toJSONString(data, valueFilter);
//		System.out.println(jsonString);
		System.out.println("filter方案耗时："+(System.currentTimeMillis() - start));
		System.out.println();
	}
	
	private static Map<String,Object> makeData(int num){
		Map<String,Object> map = new HashMap<>();
		for(int i = 0;i<num;i++){
			map.put("test"+i, i+"fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;fadfsdfdsfffffffffffsfdsfsdfdsfedfadfcdsfadsfdfdfdfeq+++jz&quot;xss&quot; jz&#40;last&#41;&#92;r&#92;n");
		}
		return map;
	}
	
	public static String outputToOrgStr(String str) {
        if (str == null) { return null; }
        str = replaceStr(str, "&lt;", "<");
        str = replaceStr(str, "&gt;", ">");
        str = replaceStr(str, "&#47;", "/");
        str = replaceStr(str, "&#92;", "\\");
        str = replaceStr(str, "&nbsp;", " ");
        str = replaceStr(str, "&acute;", "'");
        str = replaceStr(str, "&quot;", "\"");
        // str = replaceStr(str, "&#46;",".");
        str = replaceStr(str, "&#40;", "(");
        str = replaceStr(str, "&#41;", ")");

        str = replaceStr(str, "<br/>", "\r\n");
        // str = replaceStr(str, "&cedil;",".");
        // str = replaceStr(str, " ","\t" );
        return str;
    }
	
	public static String outputToOrgStr1(String str) {
        if (str == null) { return null; }
        str = replaceStr(str, "&lt;", "<");
        str = replaceStr(str, "&gt;", ">");
        str = replaceStr(str, "&#47;", "/");
        str = replaceStr(str, "&#92;", "\\\\");
        str = replaceStr(str, "&nbsp;", " ");
        str = replaceStr(str, "&acute;", "'");
        str = replaceStr(str, "&quot;", "\\\"");
        // str = replaceStr(str, "&#46;",".");
        str = replaceStr(str, "&#40;", "(");
        str = replaceStr(str, "&#41;", ")");

        str = replaceStr(str, "<br/>", "\\r\\n");
        // str = replaceStr(str, "&cedil;",".");
        // str = replaceStr(str, " ","\t" );
        return str;
    }
	
	 public static String replaceStr(String source, String oldString, String newString) {
	        StringBuffer output = new StringBuffer();

	        int lengthOfSource = source.length(); // 源字符串长度
	        int lengthOfOld = oldString.length(); // 老字符串长度

	        int posStart = 0; // 开始搜索位置
	        int pos; // 搜索到老字符串的位置

	        String lower_s = source.toLowerCase(); // 不区分大小写
	        String lower_o = oldString.toLowerCase();

	        while ((pos = lower_s.indexOf(lower_o, posStart)) >= 0) {
	            output.append(source.substring(posStart, pos));

	            output.append(newString);
	            posStart = pos + lengthOfOld;
	        }

	        if (posStart < lengthOfSource) {
	            output.append(source.substring(posStart));
	        }

	        return output.toString();
	    }


}
