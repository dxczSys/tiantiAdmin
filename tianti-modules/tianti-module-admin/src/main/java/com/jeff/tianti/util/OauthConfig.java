package com.jeff.tianti.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 〈功能简述〉
 * 〈功能详细描述〉
 *
 * @Author zhaoyifan
 * @Date 2019/8/26 14:12
 * @Since [产品/模块版本]
 * @Description TODO
 * @Version 3.0
 */
@Component
public class OauthConfig {

    @PostConstruct
    public void readConfig() {
        loadProps();
    }

    public String clientId;

    public String clientSecret;

    public String oauthIndexUrl;

    public String oauthTokenUrl;

    public String oauthUserinfoUrl;

    public String redirectUri;

    public String demiaUrl;

    public String imgPathUrl;

    public String savePathUrl;

    public String fileUploadUrl;

    private void loadProps(){
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = OauthConfig.class.getClassLoader().getResourceAsStream("oauth.properties");
            props.load(in);

            clientId = props.getProperty("clientId");
            clientSecret = props.getProperty("clientSecret");
            oauthIndexUrl = props.getProperty("oauthIndexUrl");
            oauthTokenUrl = props.getProperty("oauthTokenUrl");
            redirectUri = props.getProperty("redirectUri");
            oauthUserinfoUrl = props.getProperty("oauthUserinfoUrl");

            demiaUrl = props.getProperty("demiaUrl");

            imgPathUrl = props.getProperty("imgPathUrl");
            savePathUrl = props.getProperty("savePathUrl");

            fileUploadUrl = props.getProperty("fileUploadUrl");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
