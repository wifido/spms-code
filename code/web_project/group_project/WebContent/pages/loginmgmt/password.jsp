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
									<ext:label y="510" width="500" text="顺丰速运 版权所有 Copyright © 2013 sf-express.com Inc. All rights reserved." style="font-size:12px;color:#999;text-align:center;"/>
								</ext:items>
							</ext:panel>
							<ext:panel bodyStyle="background:transparent;" border="0" frame="false" height="578" width="490" cls="logn_lb" layout="absolute">
								<ext:items>
							        <ext:hidden type="hidden" name="expiry" value="${expiry}"/>
							        <ext:hidden type="hidden" name="userId" value="${userId}"/>
							        <ext:hidden type="hidden" name="redirect" value="${redirect}"/>								
									<ext:panel bodyStyle="background:transparent;" border="0" frame="false" x="79" y="106" width="380" height="338" layout="absolute" border="0">
										<ext:items>
											<ext:label y="0" height="50" cls="f14b red_t" html="${app:i18n('password.expiry')}" />
											<ext:label y="58" height="16" cls="f12w" html="${app:i18n('old.password')}" />
											<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="76" height="65" layout="table" layoutConfig="{column: 2}">
												<ext:items>
													<ext:label height="37" width="10" ><ext:html><img src="${images}/login/input_1_l.jpg"/></ext:html></ext:label>
													<ext:panel bodyStyle="background:transparent;" border="0" frame="false" cls="logn_inbg" width="360" height="37" layout="absolute">
														<ext:items>
															<ext:textField x="0" y="4" height="30" width="350" allowBlank="false" listeners="{specialkey:enterKey}"
																fieldClass="inp1" id="oldPassword" name="oldPassword" inputType="password" minLength="8"
																regex="/^[\x21-\x7e]{1,30}$/" regexText="${app:i18n('error.checkPasswordBuild')}"/>
														</ext:items>
													</ext:panel>
												</ext:items>
											</ext:panel>
											<ext:label y="121" height="16" cls="f12w" html="${app:i18n('new.password')}" />
											<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="139" height="65" layout="table" layoutConfig="{column: 2}">
												<ext:items>
													<ext:label height="37" width="10" ><ext:html><img src="${images}/login/input_1_l.jpg"/></ext:html></ext:label>
													<ext:panel bodyStyle="background:transparent;" border="0" frame="false" cls="logn_inbg" width="360" height="37" layout="absolute">
														<ext:items>
															<ext:textField x="0" y="4" height="30" width="350" allowBlank="false" listeners="{specialkey:enterKey}"
																fieldClass="inp1" id="newPassword" name="newPassword" inputType="password" minLength="8"
																regex="/^[\x21-\x7e]{1,30}$/" regexText="${app:i18n('error.checkPasswordBuild')}"/>
														</ext:items>
													</ext:panel>
												</ext:items>
											</ext:panel>
											<ext:label y="184" height="16" cls="f12w" text="${app:i18n('comfirm.new.password')}" />
											<ext:panel bodyStyle="background:transparent;" border="0" frame="false" y="202" height="65" layout="table" layoutConfig="{column: 2}">
												<ext:items>
													<ext:label height="37" width="10" ><ext:html><img src="${images}/login/input_1_l.jpg"/></ext:html></ext:label>
													<ext:panel bodyStyle="background:transparent;" border="0" frame="false" cls="logn_inbg" width="360" height="37" layout="absolute">
														<ext:items>
															<ext:textField x="0" y="4" height="30" width="350" allowBlank="false" listeners="{specialkey:enterKey}"
																fieldClass="inp1" id="confirmPassword" name="confirmPassword" inputType="password" minLength="8"
																regex="/^[\x21-\x7e]{1,30}$/" regexText="${app:i18n('error.checkPasswordBuild')}"/>
														</ext:items>
													</ext:panel>
												</ext:items>
											</ext:panel>	
											<ext:label y="269">
												<ext:html>
													<s:if test="%{!expiry && redirect != null && redirect != ''}">
														<a href="${redirect}" class="f12w f12w_lk">${app:i18n('return')}</a>&nbsp;&nbsp;&nbsp;&nbsp;
													</s:if>
													<app:isLogin>
														<a href="logout.action" class="f12w f12w_lk">${app:i18n('logout')}</a>
													</app:isLogin>
												</ext:html>
											</ext:label>	
											<ext:label y="256" x="213" width="157" height="46">
												<ext:html>
													<a href="javascript:changePassword()" class="logn_btn f18 logn_lt lh46"><b>${app:i18n('change.password')}</b></a>
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
    	Ext.getCmp('oldPassword').focus(false, 200);
    }
    
	function validatePassword(oldPasswordVarValue, newPasswordVarValue, newPasswordConfirmVarValue) {
		if(! checkPassword(newPasswordVarValue, '${app:i18n('new.password')}')) {
			return false;
		}
		if(! checkPassword(newPasswordConfirmVarValue, '${app:i18n('comfirm.new.password')}')) {
			return false;
		}
		if(oldPasswordVarValue == newPasswordVarValue){
			Ext.MessageBox.alert("${app:i18n('prompt')}", '${app:i18n('error.equ')}');
			return false;
		}
		if(newPasswordVarValue != newPasswordConfirmVarValue) {
			Ext.MessageBox.alert("${app:i18n('prompt')}", '${app:i18n('error.notEqu')}');
			return false;
		}
		return true;
	}
	
	// 检验密码，包括长度为八位，必需为数据与字母混合等
	function checkPassword(password, fieldname) {
	    if(password == null || password == ''){
	        Ext.MessageBox.alert("${app:i18n('prompt')}",  fieldname + '${app:i18n('error.checkPasswordEmpty')}');
	        return false;
	    }
	    if(password.length < 8){
	        Ext.MessageBox.alert("${app:i18n('prompt')}",  fieldname + '${app:i18n('error.checkPasswordLength')}');
	        return false;
	    }
	    var pwdPtn = /^[\x21-\x7e]*$/;
		if(!pwdPtn.test(password)){
		    Ext.MessageBox.alert("${app:i18n('prompt')}",  fieldname + '${app:i18n('error.checkPasswordBuild')}');
		    return false;
		}
	    //判断是否数字和英文组成
	    var numFlag = false;
	    var charFlag = false;
	    for(var i =0; i < password.length; i++){
	        if(password.charAt(i) >= '0' && password.charAt(i) <= '9'){
	            numFlag = true;
	        }
	        if( ( password.charAt(i) >= 'A' && password.charAt(i) <= 'Z' ) || ( password.charAt(i) >= 'a' && password.charAt(i) <= 'z' )){
	            charFlag = true;
	        }
	    }
	    if(! charFlag || ! numFlag ) {
	        Ext.MessageBox.alert("${app:i18n('prompt')}", fieldname + '${app:i18n('error.checkPasswordBuild')}');
			return false;
	    }
	    return true;
	}
	
    function changePassword() {
        var form = Ext.getCmp('mainPanel').getForm();
        if (form.isValid()) {
            var op = Ext.getCmp('oldPassword').getValue();
            var np = Ext.getCmp('newPassword').getValue();
            var cp = Ext.getCmp('confirmPassword').getValue();
            if(validatePassword(op, np, cp)){
            	form.submit({
			        url : "submit_password.action",
			        method : "post",
			        waitMsg : "${app:i18n_def('waitingMessage','Please wait...')}",
			        failure : submitPwdFailed,
			        success : submitPwdSucc
			    });
            }
        } else {
            Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('validateError')}", focus);
        }
    }
    
    function submitPwdSucc(form, action){
        if (action.result.error == true) {
            Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('change.password.failure.message')}", function() {
                Ext.getCmp('oldPassword').focus();
            });
        } else {
            window.location.href = '${redirect}';
        }
    }
    
    function submitPwdFailed(form, action){
        if (action.failureType === Ext.form.Action.CONNECT_FAILURE) {
            Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('prompt.connection.exception')}");
        } else if (action.failureType === Ext.form.Action.CLIENT_INVALID) {
            Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('validateError')}", focus);
        } else {
        	Ext.MessageBox.alert("${app:i18n('prompt')}", "${app:i18n('prompt.exception')}");
        }
    }
    
    function focus(t, e) {
    	if(e != undefined && typeof e == 'object' && e.getKey() != e.ENTER){
    		return;
    	}
        var valid = Ext.getCmp('oldPassword').isValid();
        if (!valid) {
            Ext.getCmp('oldPassword').focus();
            return;
        }
        valid = Ext.getCmp('newPassword').isValid();
        if (!valid) {
            Ext.getCmp('newPassword').focus();
            return;
        }else{
        	Ext.getCmp('confirmPassword').focus();
        }
    }
    
    function enterKey(t, e) {
        var form = Ext.getCmp('mainPanel').getForm();
        if (t.id == "confirmPassword" && e.getKey() == e.ENTER && form.isValid()) {
        	changePassword();
        } else {
        	focus(t, e);
        }
    }
	</ext:script>
</ext:ui>