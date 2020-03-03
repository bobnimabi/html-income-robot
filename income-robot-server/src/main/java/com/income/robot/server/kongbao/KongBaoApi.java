package com.income.robot.server.kongbao;


import com.alibaba.fastjson.JSON;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.income.robot.server.util.HttpClientUtil;
import com.income.robot.service.dto.SenderOuterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KongBaoApi {

    @Value("${TRANSFER.EXPRESSAGE_URL}")
    private String EXPRESSAGE_URL;

    public String send(String account, String password, String logiType, SenderOuterDTO senderOuterDTO) {
        Map<String, String> parammap = new HashMap<>();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = s.format(new Date());
        parammap.put("appKey", account);
        parammap.put("orders", JSON.toJSONString(senderOuterDTO));
        parammap.put("timestamp", timestamp);
        parammap.put("logiType", logiType);
        Set<String> set = new TreeSet<>();
        for (String m : parammap.keySet())
            set.add(m);
        StringBuffer signbuffer = new StringBuffer();
        signbuffer.append(MD5(password, "16"));
        for (String param : set)
            signbuffer.append(param).append(parammap.get(param));
        signbuffer.append(MD5(password, "16"));
        parammap.put("sign", MD5(signbuffer.toString(), "32").toUpperCase());
        log.info("{}", JSON.toJSONString(parammap, true));
        return HttpClientUtil.doPost(this.EXPRESSAGE_URL, parammap);
    }

    public static String MD5(String sourceStr, String flag) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            if (flag.equals("32")) {
                System.out.println("MD5(" + sourceStr + ",32) = " + result);
                return result;
            }
            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
            return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
            return "";
        }
    }
}
