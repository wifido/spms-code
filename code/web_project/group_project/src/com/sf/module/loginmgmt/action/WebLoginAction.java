package com.sf.module.loginmgmt.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.sf.framework.core.domain.IModule;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.framework.server.core.context.ApplicationContext;
import com.sf.framework.server.core.context.UserContext;
import com.sf.framework.server.core.security.ModuleContext;
import com.sf.module.authorization.domain.User;
import com.sf.module.loginmgmt.biz.ILoginBiz;
import com.sf.module.loginmgmt.domain.AuthResult;
import com.sf.module.loginmgmt.domain.AuthResultType;
import com.sf.monitor.Monitor;

public class WebLoginAction
  extends BaseAction
{
  private static final long serialVersionUID = 4233455390628430818L;
  private String language;
  private String country;
  private String variant;
  
  public void setLanguage(String language)
  {
    if (language.matches("\\w+")) {
      this.language = language;
    }
  }
  
  public void setCountry(String country)
  {
    if (country.matches("\\w+")) {
      this.country = country;
    }
  }
  
  public void setVariant(String variant)
  {
    if (variant.matches("\\w+")) {
      this.variant = variant;
    }
  }
  
  private Locale reqLocale = null;
  private ILoginBiz loginBiz;
  private String username;
  private String password;
  private String app;
  private String appName;
  private boolean error;
  private int errorInt;
  private Object[] errorMsgOrKeys;
  private Object[] errorParams;
  
  private Locale getCookieLocale()
  {
    Locale result = null;
    Cookie[] cookies = ServletActionContext.getRequest().getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ((cookie != null) && ("locale".equalsIgnoreCase(cookie.getName())) && 
          (cookie.getValue() != null) && (cookie.getValue().length() > 0))
        {
          String[] tokens = cookie.getValue().split("_");
          if (tokens == null) {
            break;
          }
          if (tokens.length >= 3)
          {
            result = new Locale(tokens[0], tokens[1], tokens[2]);
            break;
          }
          if (tokens.length >= 2)
          {
            result = new Locale(tokens[0], tokens[1]);
            break;
          }
          if (tokens.length < 1) {
            break;
          }
          result = new Locale(tokens[0]);
          

          break;
        }
      }
    }
    return result;
  }
  
  public String index()
  {
    if ((this.language != null) && (this.language.length() > 0)) {
      if ((this.country != null) && (this.country.length() > 0) && (this.variant != null) && (this.variant.length() > 0)) {
        this.reqLocale = new Locale(this.language, this.country, this.variant);
      } else if ((this.country != null) && (this.country.length() > 0)) {
        this.reqLocale = new Locale(this.language, this.country);
      } else {
        this.reqLocale = new Locale(this.language);
      }
    }
    if (this.reqLocale == null) {
      this.reqLocale = getCookieLocale();
    }
    if (this.reqLocale == null) {
      this.reqLocale = Locale.SIMPLIFIED_CHINESE;
    }
    UserContext.getContext().setUserLocale(this.reqLocale);
    String appCode = ApplicationContext.getContext().getApplication().getName();
    IModule moduleTmp = ModuleContext.getModuleByCode(appCode);
    if (moduleTmp != null) {
      this.appName = moduleTmp.getName();
    }
   String url= ServletActionContext.getRequest().getRequestURI();
   if(url.equals("/spms/loginmgmt/index.action")){	   
	 String request="/spms/loginmgmt/frame.action";
	   try {
		ServletActionContext.getResponse().sendRedirect(request);
	} catch (IOException e) {		
		e.printStackTrace();
	}
   }
    return "success";
  }
  
  public String login()
  {
    
    if ((this.app == null) || (this.app.length() == 0)) {
      this.app = ApplicationContext.getContext().getApplication().getName();
    }
    try
    {
      AuthResult authResultObj = this.loginBiz.login(this.username, this.password);
      AuthResultType authResultType = authResultObj.getAuthResultType();
      if (AuthResultType.SUCCESS != authResultType)
      {
        setErrorInt(authResultType.getCode());
        setErrorParams(authResultObj.getParams());
        setErrorMsgOrKeys(authResultType.getMsgKeys());
      }
      return "success";
    }
    finally
    {
      Monitor.endAction();
    }
  }
  
  public String logout()
  {
    User user = (User)getCurrentUser();
    if (user != null) {
      this.loginBiz.logout(user);
    }
    HttpSession session = ServletActionContext.getRequest().getSession();
    session.removeAttribute("_const_cas_assertion_");
    session.setAttribute("_is_logout_", "1");
    
    return "success";
  }
  
  public void setLoginBiz(ILoginBiz loginBiz)
  {
    this.loginBiz = loginBiz;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = (username == null ? null : username.toLowerCase());
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getApp()
  {
    return this.app;
  }
  
  public void setApp(String app)
  {
    this.app = app;
  }
  
  public String getAppName()
  {
    return this.appName;
  }
  
  public void setAppName(String appName)
  {
    this.appName = appName;
  }
  
  public boolean isError()
  {
    return this.error;
  }
  
  public void setError(boolean error)
  {
    this.error = error;
  }
  
  public int getErrorInt()
  {
    return this.errorInt;
  }
  
  public void setErrorInt(int errorInt)
  {
    this.errorInt = errorInt;
  }
  
  public Object[] getErrorMsgOrKeys()
  {
    return this.errorMsgOrKeys;
  }
  
  public String[] getMappedErrorMsgs()
  {
    Object[] keys = getErrorMsgOrKeys();
    if (keys == null) {
      return null;
    }
    String[] msgs = new String[keys.length];
    for (int i = 0; i < keys.length; i++)
    {
      String key = String.valueOf(keys[i]);
      msgs[i] = super.getMessageSource().getMessage(key, key);
    }
    return msgs;
  }
  
  public void setErrorMsgOrKeys(Object[] errorMsgOrKeys)
  {
    this.errorMsgOrKeys = errorMsgOrKeys;
  }
  
  public Object[] getErrorParams()
  {
    return this.errorParams;
  }
  
  public void setErrorParams(Object[] errorParams)
  {
    this.errorParams = errorParams;
  }
  
  public boolean isCurrentApplication()
  {
    return ApplicationContext.getContext().getApplication().getName().equals(this.app);
  }
  
  public Map<String, String> getVersion()
  {
    Map<String, String> version = new HashMap();
    Properties props = ApplicationContext.getContext().getSystemConfig();
    version.put("versionType", props.getProperty("version.type"));
    version.put("versionNumber", props.getProperty("version.number"));
    return version;
  }
  
  public String getLogoutRedirectUrl()
  {
    String logoutUrl = ApplicationContext.getContext().getSystemConfig().getProperty("logoutUrl");
    if (StringUtils.isEmpty(logoutUrl)) {
      return "index.action";
    }
    try
    {
      Locale locale = UserContext.getContext().getLocale();
      return logoutUrl + "&language=" + locale.getLanguage() + "&country=" + locale.getCountry() + "&variant=" + 
        locale.getVariant();
    }
    catch (Exception localException) {}
    return logoutUrl;
  }
  
  public String getLanguage()
  {
    return this.reqLocale == null ? null : this.reqLocale.getLanguage();
  }
  
  public String getCountry()
  {
    return this.reqLocale == null ? null : this.reqLocale.getCountry();
  }
  
  public String getVariant()
  {
    return this.reqLocale == null ? null : this.reqLocale.getVariant();
  }
}
