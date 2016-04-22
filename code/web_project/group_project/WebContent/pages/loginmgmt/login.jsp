<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ext" uri="/ext-tags"%>
<%@ taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ext:ui styles="${styles}/login.css"
	title="${app:i18n_def('title', appName)} - ${app:i18n_def(app:concat('version.', version.versionType), version.versionType)} - ${version.versionNumber}">
	<ext:viewport layout="table" layoutConfig="{column: 1}" listeners="{afterlayout:afterUILayout}">
		<ext:items>
			<ext:formPanel id="mainPanel" frame="false" width="1002" height="578" bodyCssClass="loginbg" border="0" 
				bodyStyle="background:transparent;">
				<ext:items>
					<ext:panel bodyStyle="background:transparent;" border="0" frame="false" region="center" height="578" layout="table" layoutConfig="{column: 2}">
						<ext:items>
							<ext:panel bodyStyle="background:transparent;" border="0" frame="false" height="578" width="512" layout="absolute"
								style="background-image:url(${images}/login/logn_title.jpg);background-repeat: no-repeat;background-position:top right;">
								<ext:items>
									<ext:label y="300" width="500" text="${app:i18n_def('title',appName)}" style="font-size:60px;color:white;text-align:center;"/>
									<ext:label y="510" width="500" text="顺丰速运 版权所有 Copyright © 2014 sf-express.com Inc. All rights reserved." style="font-size:12px;color:#999;text-align:center;"/>
								</ext:items>
							</ext:panel>
							<ext:panel bodyStyle="background:transparent;" border="0" frame="false" height="578" width="490" cls="logn_lb" layout="absolute">
								<ext:items>
									<ext:panel bodyStyle="background:transparent;" border="0" frame="false" x="79" y="106" width="380" height="338" layout="absolute" border="0">
										<ext:items>
											<ext:label y="0" height="50" cls="f14b red_t" html="${app:i18n('login.panel.title')}" />
											<ext:label y="58" height="16" cls="f12w" html="${app:i18n('username')}" />
											<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="76" height="65" layout="table" layoutConfig="{column: 2}">
												<ext:items>
													<ext:panel height="37" width="10" cls="logn_lbg" bodyStyle="background:transparent;" border="0" frame="false" html=""/>
													<ext:panel height="37" bodyStyle="background:transparent;" border="0" frame="false" cls="logn_inbg" width="360" layout="absolute">
														<ext:items>
															<ext:textField x="0" y="4" height="30" width="350" allowBlank="false" listeners="{specialkey:focus}"
																fieldClass="inp1" id="username" name="username" minLength="3" maxLength="20"
																regex="/^[\x21-\x7e]{1,30}$/" />
														</ext:items>
													</ext:panel>
												</ext:items>
											</ext:panel>
											<ext:label y="141" height="16" cls="f12w" text="${app:i18n('password')}"/>
											<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="159" height="65" layout="table" layoutConfig="{column: 2}">
												<ext:items>
													<ext:panel height="37" width="10" cls="logn_lbg" bodyStyle="background:transparent;" border="0" frame="false" html=""/>
													<ext:panel bodyStyle="background:transparent;" border="0" frame="false" cls="logn_inbg" width="360" height="37" layout="absolute">
														<ext:items>
															<ext:textField x="0" y="4" height="30" width="350" allowBlank="false" listeners="{specialkey:enterKey}"
																fieldClass="inp1" id="password" name="password" inputType="password" minLength="1"
																regex="/^[\x21-\x7e]{1,30}$/" />
														</ext:items>
													</ext:panel>
												</ext:items>
											</ext:panel>
											<ext:comboBox y="269" width="100" height="24" name="lang" id="lang" triggerAction="all" editable="false" mode="local" 
												valueField="key" displayField="value" data="['zh_CN','简体中文'],['zh_TW','繁體中文'],['en_US','English']" />
											<ext:label height="46" width="157" x="213" y="256" >
												<ext:html>
												<a href="javascript:loginFunc()" class="logn_btn f18 logn_lt lh46"><b>${app:i18n('login.btn.text')}</b></a>
												</ext:html>
											</ext:label>
										</ext:items>
									</ext:panel>
									<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="426" height="152" width="490" layout="absolute"
										style="background-image:url(${images}/login/logn_lbg2.jpg);background-repeat: no-repeat;background-position:bottom right;">
										<ext:items>
											<ext:label x="366" y="37" ><ext:html><img src="${images}/login/logo.jpg"/></ext:html></ext:label>
										</ext:items>
									</ext:panel>									
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:panel>
				</ext:items>
			</ext:formPanel>
		</ext:items>
	</ext:viewport>
	<ext:script>
    function afterUILayout() {
    	var ccc = document.getElementsByTagName('table');
    	ccc[0].align = 'center';
        var localeVal = getLocaleVal();
        var cbxLang = Ext.getCmp('lang');
        cbxLang.setValue(localeVal);
        cbxLang.on("select", switchLocale);
        Ext.getCmp('username').focus(false, 200);
    }
    
    function getLocaleVal(){
    	var thePagelocale = null;
	<s:if test="%{language != null}">
		<s:if test="%{country != null}">
		thePagelocale = "${language}_${country}";
		</s:if>
	</s:if>
    	return (thePagelocale == null || thePagelocale == undefined) ? 'zh_CN' : thePagelocale.replace(/-/, '_');
    }
        
    function switchLocale(cbx, rec, i) {
        var localeVal = getLocaleVal();
        var newVal = cbx.getValue();
        if (newVal != '' && newVal != undefined && newVal != localeVal) {
            var lc = newVal.split('_');
            window.location = "switch_locale.action?language=" + lc[0] + "&country=" + lc[1];
        }
    }
    
    function loginFunc() {
        var form = Ext.getCmp('mainPanel').getForm();
        if (form.isValid()) {
            var username = Ext.getCmp('username').getValue();
            var password = Ext.getCmp('password').getValue();
            var wb = Ext.Msg.wait("${app:i18n_def('waitingMessage','Please wait...')}");
            Ext.Ajax.request({
                url : 'login.action',
                params : {
                    username : username,
                    password : password
                },
                callback : function(o, s, r) {
                    wb.hide();
                    if (s) {
                        var resp = Ext.decode(r.responseText);
                        var msg = resp.errorInt;
                        if (msg != '0') {
                            if (msg == '90' || msg == '91' || msg == '92') {
                                var passValue = new Object();
                                passValue.type = msg;
                                passValue.value = resp.errorParams[0];
                                var str = showModalDialog('ldapPass.action', passValue,
                                        'dialogWidth:320px;dialogHeight:170px;center:yes;help:no;location:no;status:no;resizable:no;');
                                if (str != undefined) {
                                    if (str == '2') {
                                        window.location.href = 'loginSuccess.action';
                                    }// 修改密码 退出系统
                                    else {
                                        window.location.href = 'logout.action';
                                    }
                                }
                            } else if(msg == '11'){
                                window.location.href = 'password.action?expiry=true&redirect=loginSuccess.action';
                            }else {
                                var mappedMsgs = resp.mappedErrorMsgs || ["${app:i18n('prompt.exception')}"];
                                failure(msg, mappedMsgs);
                            }
                        } else {
                            Ext.Msg.wait("${app:i18n_def('waitingMessage','Please wait...')}");
                            window.location = 'loginSuccess.action';
                        }
                    } else {
			            if (Ext.encode(r.status) == 0) {
			                Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('prompt.connection.exception')}");
			            } else {
			                Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('login.timeout')}");
			            }                    
                        Ext.getCmp('password').setValue('');
                    }
                }
            });
        } else {
            Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('login.form.invalid')}", focus);
        }
    }
    
    function failure(msg, mappedMsgs) {
        Ext.MessageBox.alert("${app:i18n('prompt')}", mappedMsgs[0]);
        Ext.getCmp('password').setValue('');
    }
    
    function focus(t, e) {
        var username = Ext.getCmp('username').getValue();
        if (!username) {
            Ext.getCmp('username').focus();
            return;
        }
        if (typeof e != 'object' || e.getKey() == e.ENTER)
            Ext.getCmp('password').focus();
    }
    
    function enterKey(t, e) {
        if (e.getKey() == e.ENTER && t.getValue())
            loginFunc();
    }
	</ext:script>
</ext:ui>