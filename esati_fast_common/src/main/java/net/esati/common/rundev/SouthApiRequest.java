package net.esati.common.rundev;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.esati.common.http.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gao Zhenfeng
 * @email jobs-gao@foxmail.com
 */
public class SouthApiRequest {

    public static void main(String[] args) {
//        本地测试地址
//        String loginUrl = "http://172.17.0.56:8099/alarm/login";
//        String dataUrl = "http://172.17.0.56:8099/alarm/data/add";

//        unicom 地址
//        String loginUrl = "http://223.167.110.245:28005/alarm/login";
//        String dataUrl = "http://223.167.110.245:28005/alarm/data/add";

//        unicom 地址
        String loginUrl = "http://30.1.1.12:8005/alarm/login";
        String dataUrl = "http://30.1.1.12:8005/alarm/data/add";

        HttpClientUtil client = new HttpClientUtil();

//        本地测试参数
//        String loginStr ="{\"username\":\"xiaogao\",\"password\":\"Gzf3511_!\"}";
//        String dataStr ="{\"sti\":\"2019-08-21 11:37:02\",\"rt\":\"2019-05-21 11:37:02\",\"di\":\"10011\",\"vd\":\"unicom\",\"ft\":\"27\",\"st\":\"1\",\"dynamic\":\"data\"}";

        for(int i =0;i<10;i++){

            String loginStr ="{\"username\":\"yangansl\",\"password\":\"Yangansl_01\"}";
            String dataStr ="{\n" +
                    "\t\"di\": \"FFFFFFFFFFFFFFFFFFFFFFFF\",\n" +
                    "\t\"vd\": \"zt\",\n" +
                    "\t\"dynamic\": \"data\",\n" +
                    "\t\"rt\": \"2029-08-21 00:00:"+i+"\",\n" +
                    "\t\"sti\": \"2019-08-21 16:56:07\",\n" +
                    "\t\"ft\": 5,\n" +
                    "\t\"st\": 3,\n" +
                    "\t\"prp\": null,\n" +
                    "\t\"li\": null,\n" +
                    "\t\"pi\": {\n" +
                    "\t\t\"alarmState\": 0,\n" +
                    "\t\t\"alarmContent\": \"氨氮超标报警test\",\n" +
                    "\t\t\"alarmType\": 2\n" +
                    "\t}\n" +
                    "}";
            //发送登录请求
            String loginResult = client.postByJson(loginUrl,loginStr,String.class);
//            System.out.println("login request ,responseBody:"+loginResult.toString());
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(loginResult);

            String sid = jsonObject.get("sid").toString();
//            System.out.println("login success , sid: "+sid);

            Map map = new HashMap();
            map.put("Cookie","sessionId="+sid);
            //发送登录请求
            String dataResult = client.postByJson(dataUrl,dataStr,map,String.class);
            System.out.println("["+i+"]th send success ,responseBody:"+dataResult.toString());

        }


    }

}
