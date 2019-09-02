package net.esati.common.rundev;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @author jobs-gao@foxmail.com
 * 重复发送测试
 */
public class SouthApiRequestResend {

    public static  String alarmLoginUrl="http://30.1.1.12:8005//alarm/login";
    public static  String alarmUpUrl="http://30.1.1.12:8005//alarm/data/add";

    public static void main(String[] args) {
        for (int i = 0; i <10 ; i++) {


            System.out.println("***************************临时服务烟感上报开始***************************");

            String loginStr = "{\"username\":\"yangansl\",\"password\":\"Yangansl_01\"}";
            StringBuffer successSN = new StringBuffer("");
            StringBuffer failSN = new StringBuffer("");
            Map<String, String> heads = new HashMap<String, String>();
            heads.put("Content-Type", "application/json;charset=UTF-8");
            String content = postJson(loginStr, SouthApiRequestResend.alarmLoginUrl, heads);
            if (StringUtils.isNotBlank(content)) {
                JSONObject resultco = JSONObject.fromObject(content);
                if (resultco.getBoolean("success")) {
                    String sid = resultco.getString("sid");

                    System.out.println("http request ok sid :" + sid);

                    String eve = "{\n" +
                            "\t\"di\": \"FFFFFFFFFFFFFFFFFFFFFFFF\",\n" +
                            "\t\"vd\": \"zt\",\n" +
                            "\t\"dynamic\": \"data\",\n" +
                            "\t\"rt\": \"2029-08-21 00:00:9"+i+"\",\n" +
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

                    Map<String, String> heads2 = new HashMap<String, String>();
                    heads.put("Content-Type", "application/json;charset=UTF-8");
                    heads.put("Cookie", "sessionId=" + sid);
                    String content2 = postJson(eve, alarmUpUrl, heads);
                    System.out.println("第【"+i+"】次上报:south api rec" + content2.toString());
                }
            }
        }
    }



    public static <JSONObject> String postJson(String obj, String alarmUrl, Map<String,String> heads) {
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            //  System.out.println(obj);

            HttpPost httpPost = new HttpPost(alarmUrl);
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            if(heads!=null) {
                Set<Map.Entry<String, String>> entries = heads.entrySet();
                Iterator<Map.Entry<String, String>> iteratorMap = entries.iterator();
                while (iteratorMap.hasNext()){
                    Map.Entry<String, String> next = iteratorMap.next();
                    httpPost.addHeader(next.getKey(),next.getValue());
                }
            }
            // 解决中文乱码问题
            StringEntity stringEntity = new StringEntity(obj, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");

            httpPost.setEntity(stringEntity);

            // CloseableHttpResponse response =
            // httpclient.execute(httpPost);

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {//
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {

                        HttpEntity entity = response.getEntity();

                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            return responseBody;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }


}
