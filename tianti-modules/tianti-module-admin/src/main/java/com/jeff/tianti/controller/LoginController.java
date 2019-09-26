package com.jeff.tianti.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.jeff.tianti.common.util.Md5Util;
import com.jeff.tianti.org.entity.Resource;
import com.jeff.tianti.org.entity.Role;
import com.jeff.tianti.org.entity.User;
import com.jeff.tianti.org.service.RoleService;
import com.jeff.tianti.org.service.UserService;
import com.jeff.tianti.user.UserInfoResp;
import com.jeff.tianti.util.HttpUtils;
import com.jeff.tianti.util.OauthConfig;
import com.jeff.tianti.util.TokenData;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeff.tianti.util.WebHelper;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.jeff.tianti.util.HttpUtils.sendGet;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

    @Autowired
    OauthConfig oauthConfig;

	@RequestMapping("/login")
	public String login(){
		return "login";
	}

//	@RequestMapping("/zdlogin")
//	public String zdlogin(@RequestParam String token,
//                          HttpServletResponse response){
//	    String ss ="";
//		return "zdLogin";
//	}

    /**
     * 单点登录
     *
     * @return
     * @throws UnknownHostException
     */
    @RequestMapping("/zdlogin")
    public void zdloginCheck(@RequestParam String token,
                             HttpServletResponse response) {

        String url = oauthConfig.oauthIndexUrl;
        Map<String, String> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", oauthConfig.clientId);
        params.put("client_secret", oauthConfig.clientSecret);
        params.put("redirect_uri", oauthConfig.redirectUri);
        params.put("scope", "select");
        params.put("oauth_user_token", token);
        try {
//            String aaa = HttpRUtils.sendGet(url,params);
            String redUrl = oauthConfig.oauthIndexUrl;
            redUrl += "?response_type=code&client_id=" + oauthConfig.clientId + "&client_secret=" + oauthConfig.clientSecret + "&redirect_uri=" + oauthConfig.redirectUri + "&scope=select&oauth_user_token=" + token;
            response.sendRedirect(redUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUserInfo(String accessToken) {
        String url = oauthConfig.oauthUserinfoUrl;
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        String userInfo = "";
        try {
            userInfo = sendGet(url, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    @RequestMapping("/oauth/redirect")
    public String getToken(@RequestParam String code,
                           @RequestParam(required = false, defaultValue = "") String state,
                           HttpServletRequest req,
                           HttpServletResponse response) {

        String url = oauthConfig.oauthTokenUrl;
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("code", code);

        params.put("client_id", oauthConfig.clientId);
        params.put("client_secret", oauthConfig.clientSecret);
        params.put("redirect_uri", oauthConfig.redirectUri);

        String userInfo = "";
        try {
            String token = sendGet(url, params);

            TokenData tokenData = JSON.parseObject(token, TokenData.class);
            userInfo = getUserInfo(tokenData.getAccess_token());

            UserInfoResp user = JSON.parseObject(userInfo, UserInfoResp.class);

            req.getSession().setAttribute("userId",user.getUserInfo().getUserId());
            req.getSession().setAttribute("userType",user.getUserInfo().getUserType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "zdLogin";
    }

	@RequestMapping("/token")
	public String token(HttpServletRequest request, HttpServletResponse response){
        //从3.0获取用户信息
        String userUrl = oauthConfig.demiaUrl + "/api/userAPIInfo";
        Map<String, String> userMap = new HashMap<>();
        userMap.put("userId", request.getSession().getAttribute("userId")+"");
        userMap.put("userType", request.getSession().getAttribute("userType")+"");
        userMap.put("productId", "");
        String list = "";
        try {
            list = sendGet(userUrl, userMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

//		String requestUrl="http://localhost:8080/ischool/oauth/authorize/chkToken1";
//		Map<String, String> map=new HashMap<>();
//		map.put("access_token",request.getParameter("access_token"));
		try {
//			String list= HttpUtils.sendPost(requestUrl,map);
			JSONObject json=  new JSONObject(list);
			String list1=json.get("userInfo").toString();
			JSONObject json1=  new JSONObject(list1);
			String userName=json1.get("username").toString();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username", userName);
			List<User> userList = this.userService.findUsers(params);
			String password=null;
			if(userList.size()==0){
				String id =json1.get("userId").toString();
				String username =json1.get("username").toString();
				String icon =json1.get("imageUrl").toString();
				password = Md5Util.generatePassword("123456");
				String realName = json1.get("fullName").toString();
				String[] roleIds = new String[]{"402881e457f075530157f0791e2f0000"};
				User user = new User();
				user.setPassword(password);
				user.setIcon(StringUtils.trim(icon));
				user.setUsername(StringUtils.trim(username));
				user.setRealName(StringUtils.trim(realName));
				user.setId(id);
				user.setStatus(User.STATUS_YES);
				Set<Role> set = new HashSet<Role>();
				if(roleIds != null){
					for(String roleId : roleIds){
						Role role = roleService.find(roleId);
						if(role != null){
							set.add(role);
						}
					}
				}
				user.setRoles(set);
				user.setType(0);
				userService.save(user);
			}else{
				password=userList.get(0).getPassword();
			}
			boolean rememberMe = false;
			UsernamePasswordToken token = new UsernamePasswordToken(userName, password, rememberMe);

			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			//跳转第一个菜单

			List<Resource> hasResource = (List<Resource>) request.getSession().getAttribute(WebHelper.SESSION_MENU_RESOURCE);
			if(hasResource != null && !hasResource.isEmpty()){
				for(Resource resource : hasResource){
					List<Resource> chResources = resource.getChildren();
					if(StringUtils.isNotBlank(resource.getUrl()) && (chResources == null || chResources.isEmpty())){
						return "redirect:" + resource.getUrl();
					}
					if(chResources != null && !chResources.isEmpty()){
						for(Resource chRes : chResources){
							if(StringUtils.isNotBlank(chRes.getUrl())){
								String  url=chRes.getUrl();
								return "redirect:" + chRes.getUrl();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/list";
	}

	@RequestMapping("/do_login")
	public String doLogin(HttpServletRequest request, Model model){

		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");

		boolean rememberMe = false;

		String md5Pwd = Md5Util.generatePassword(pwd);

		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, md5Pwd, rememberMe);

			Subject subject = SecurityUtils.getSubject();

			subject.login(token);

			//跳转第一个菜单
			List<Resource> hasResource = (List<Resource>) request.getSession().getAttribute(WebHelper.SESSION_MENU_RESOURCE);
			if(hasResource != null && !hasResource.isEmpty()){
				for(Resource resource : hasResource){

					List<Resource> chResources = resource.getChildren();
					if(StringUtils.isNotBlank(resource.getUrl()) && (chResources == null || chResources.isEmpty())){
						return "redirect:" + resource.getUrl();
					}
					if(chResources != null && !chResources.isEmpty()){
						for(Resource chRes : chResources){
							if(StringUtils.isNotBlank(chRes.getUrl())){
								return "redirect:" + chRes.getUrl();
							}
						}
					}
				}
			}

			return "redirect:/user/list";
		} catch (LockedAccountException lae) {
//			lae.printStackTrace();
			model.addAttribute("msg", "账号已被禁用");
		} catch (AuthenticationException ae) {
//			ae.printStackTrace();
			model.addAttribute("msg", "账号或密码错误");
		} catch (Exception e) {
//			e.printStackTrace();
			model.addAttribute("msg", "登录异常");
		}

		return "login";
	}

	@RequestMapping("/login_out")
	public String loginOut(HttpServletRequest request){

		Subject subject = SecurityUtils.getSubject();
		subject.logout();

		return "redirect:/login";
	}

}
