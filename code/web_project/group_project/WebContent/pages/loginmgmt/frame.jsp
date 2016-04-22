<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ext" uri="/ext-tags"%>
<%@ taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ext:ui title="${app:i18n_def(app:concat(app.code, '.name'), app.name)} - ${app:i18n_def(app:concat('version.', version.versionType), version.versionType)} - ${version.versionNumber}">
	<ext:toolbar var="menuBar" enableOverflow="true">
		<ext:items>
			<s:iterator value="menus">
				<ext:toolbarSeparator />
				<ext:toolbarButton var="menu_${codeVar}" id="${code}" text="${app:i18n_menu_def(app:concat(code, '.name'), name)}" handler="function(item){${url == '' || children != null ? 'return;' : ''}openMenu(item, '..${url}', '${app:i18n_menu_def(app:concat(code, '.desc'), description)}', '${helpUrl}');}">
					<s:if test="%{children != null && children.size() > 0}">
					<ext:menu>
						<ext:items>
							<s:iterator value="%{children}">
								<s:if test="%{children == null || children.size() == 0}">
									<ext:toolbarButton var="menu_${codeVar}" id="${code}" text="${app:i18n_menu_def(app:concat(code, '.name'), name)}" handler="function(item){${url == '' ? 'return;' : ''}openMenu(item, '..${url}', '${app:i18n_menu_def(app:concat(code, '.desc'), description)}', '${helpUrl}');}" />
								</s:if>
								<s:else>
									<ext:toolbarButton var="menu_${codeVar}" id="${code}" text="${app:i18n_menu_def(app:concat(code, '.name'), name)}" handler="function(item){${url == '' || children != null ? 'return;' : ''}openMenu(item, '..${url}', '${app:i18n_menu_def(app:concat(code, '.desc'), description)}', '${helpUrl}');}">
										<ext:menu>
											<ext:items>
												<s:iterator value="%{children}">
													<ext:toolbarButton var="menu_${codeVar}" id="${code}" text="${app:i18n_menu_def(app:concat(code, '.name'), name)}" handler="function(item){${url == '' ? 'return;' : ''}openMenu(item, '..${url}', '${app:i18n_menu_def(app:concat(code, '.desc'), description)}', '${helpUrl}');}" />
												</s:iterator>
											</ext:items>
										</ext:menu>
									</ext:toolbarButton>
								</s:else>
							</s:iterator>
						</ext:items>
					</ext:menu>
					</s:if>
				</ext:toolbarButton>
			</s:iterator>
			<ext:toolbarSeparator />
			<ext:toolbarSplitor/>
			<ext:toolbarButton text="${app:i18n('tools')}" cls="x-btn-text-icon" icon="${images}/frame/gears.png" >
				<ext:menu showSeparator="false">
					<ext:items>
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/home.gif" text="${app:i18n('go.back.home')}" handler="function(){tabs.setActiveTab('home');}" />
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/close.gif" text="${app:i18n('close.all.tab')}" handler="function(){closeAllTab();}" />
						<s:if test="%{app.helpUrl != null}">
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/help.gif" text="${app:i18n('help')}" handler="function() {showHelp('${app.helpUrl}');}" />
						</s:if>
						<s:if test="%{faqUrl != null && faqUrl.length() > 0}">
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/faq.gif" text="${app:i18n('faq')}" handler="function() {window.open('${faqUrl}', '_blank', 'height=600,width=800,resizable=yes,scrollbars=yes');}" />
						</s:if>
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/version.gif" text="${app:i18n('version')}" handler="function(){versionWin.show();}" />
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/user.gif" text="${app:i18n('user')}" handler="function(){userWin.show();}" />
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/password.gif" text="${app:i18n('change.password')}" handler="function(){changePasswordWin.show();}" />
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/accordian.gif" text="${app:i18n('change.theme')}" handler="function(){pageStyle();}" />
						<ext:toolbarButton cls="x-btn-text-icon" icon="${images}/frame/exit.gif" text="${app:i18n('logout')}" handler="function(){logout();}" />
					</ext:items>				
				</ext:menu>
			</ext:toolbarButton>
			<ext:toolBarSpacer />		
		</ext:items>
	</ext:toolbar>
	<ext:viewport var="framePane" layout="border">
		<ext:items>
			<ext:panel region="center" border="0" layout="border" tbar="menuBar">
				<ext:items>
					<ext:tabPanel var="tabs" region="center" border="false" autoScroll="false" enableTabScroll="true" activeTab="home" activeItem="home">
						<ext:items>
							<ext:panel id="home" title="${app:i18n('home')}" layout="border">
								<ext:items>
									<ext:panel id="homemain" border="false" region="center" border="false">
										<ext:html>
											<iframe src='${homeUrl}' width='100%' height='100%' frameborder='0'></iframe>
										</ext:html>
									</ext:panel>
									<ext:panel id="homebar" region="north" layout="border" height="25" border="false" cls="x-toolbar">
										<ext:items>
											<ext:panel id="homemsg" region="center" border="false">
												<ext:html>
													<table border='0' class='x-toolbar' cellpadding='0' cellspacing='0' width='100%' height="25"><tr><td width='24' align='center'><img src='${images}/frame/info.gif' border='0' height='16' width='16'/></td><td>${app:i18n_def(app:concat(app.code, '.desc'), app.description)}</td></tr></table>
												</ext:html>
											</ext:panel>
											<ext:panel id="homebtn" region="east" width="22" border="false">
												<ext:items>
													<ext:button id="homehelp" cls="x-btn-icon x-toolbar" style="padding:0" icon="${images}/frame/help.gif" tooltip="${app:i18n('help')}" handler="function() {showHelp('${app.helpUrl}');}"></ext:button>
												</ext:items>
											</ext:panel>
										</ext:items>
									</ext:panel>
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:tabPanel>
				</ext:items>
			</ext:panel>
		</ext:items>
	</ext:viewport>
	<ext:window var="userWin" title="${app:i18n('user')}" closeAction="hide" width="300" height="200" layout="border" modal="true" frame="true">
		<ext:items>
			<ext:panel region="center" border="false" frame="true">
				<ext:html>
					<table border="0" width="100%" class="x-form-field" style="font-size:15">
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td style="border-bottom:1px dashed #BEBEBE" align="right" width="120">${app:i18n('user.name')}&nbsp;:&nbsp;&nbsp;</td>
							<td style="border-bottom:1px dashed #BEBEBE">${user.username}</td>
						</tr>
						<s:if test="%{user.employee != null}">
							<tr>
								<td style="border-bottom:1px dashed #BEBEBE" align="right">${app:i18n('user.employee.name')}&nbsp;:&nbsp;&nbsp;</td>
								<td style="border-bottom:1px dashed #BEBEBE">${user.employee.name}</td>
							</tr>
							<tr style="border-bottom:1px dashed #BEBEBE">
								<td style="border-bottom:1px dashed #BEBEBE" align="right">${app:i18n('user.employee.duty.name')}&nbsp;:&nbsp;&nbsp;</td>
								<td style="border-bottom:1px dashed #BEBEBE">${user.employee.empDutyName}</td>
							</tr>
						</s:if>
						<s:if test="%{user.dept != null}">
							<tr>
								<td style="border-bottom:1px dashed #BEBEBE" align="right">${app:i18n('user.employee.department.name')}&nbsp;:&nbsp;&nbsp;</td>
								<td style="border-bottom:1px dashed #BEBEBE">${user.dept.deptCode} / ${user.dept.deptName}</td>
							</tr>
						</s:if>
					</table>
				</ext:html>
			</ext:panel>
		</ext:items>
	</ext:window>
	<ext:window var="versionWin" title="${app:i18n('version')}" closeAction="hide" width="345" height="355" layout="border" modal="true" frame="true">
		<ext:items>
			<ext:panel region="center" border="false" frame="true">
				<ext:html>
					<table border="0" class="x-form-field" style="font-size:14">
						<tr>
							<td colspan="2"><a href="http://www.sf-express.com" target="_blank"><img style="background-color: black" src="${images}/frame/sf_logo.png" alt="SF-Express"/></a></td>
						</tr>
						<tr>
							<td colspan="2" style="font-size:20">${app:i18n_def(app:concat(app.code, '.name'), app.name)}</td>
						</tr>
						<tr>
							<td colspan="2"><hr/></td>
						</tr>
						<tr>
							<td align="right" width="120">${app:i18n('version.type')}&nbsp;:&nbsp;&nbsp;</td>
							<td>${app:i18n_def(app:concat('version.', version.versionType), version.versionType)}(${version.versionType})</td>
						</tr>
						<tr>
							<td align="right">${app:i18n('version.number')}&nbsp;:&nbsp;&nbsp;</td>
							<td>${version.versionNumber}</td>
						</tr>
						<tr>
							<td align="right">${app:i18n('version.release.date')}&nbsp;:&nbsp;&nbsp;</td>
							<td>${version.releaseDate}</td>
						</tr>
						<tr>
							<td align="right">Framework&nbsp;:&nbsp;&nbsp;</td>
							<td>${version.framework}</td>
						</tr>						
						<tr>
							<td colspan="2"><hr/></td>
						</tr>
						<tr>
							<td colspan="2">Copyright (c) 2013, S.F. Express Inc. All rights reserved.</td>
						</tr>
						<tr>
							<td colspan="2"><br/><a href="../${version.changeLog}" target="_blank">${app:i18n('version.change.log')}</a></td>
						</tr>
					</table>
				</ext:html>
			</ext:panel>
		</ext:items>
	</ext:window>
	<ext:window var="changePasswordWin" title="${app:i18n('change.password')}" closeAction="hide" width="300" height="240" layout="border" modal="true" frame="true">
		<ext:items>
			<ext:panel region="center" layout="border" border="false">
				<ext:tbar>
					<ext:button text="${app:i18n('save')}"  handler="saveChangePwd"></ext:button>
					<ext:button text="${app:i18n('reset')}" handler="function() {changePasswordForm.getForm().reset();}"></ext:button>
				</ext:tbar>
				<ext:items>
					<ext:formPanel var="changePasswordForm" region="center" frame="true" border="false">
						<ext:submitAction name="savePassword" url="change_password.action" success="changePasswordSuccess" failure="changePasswordFailure" />
						<ext:items>
							<ext:textField allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('old.password'))}"
							maxLength="20" maxLengthText="${app:i18n_arg2('error.maxLength',app:i18n('old.password'),20)}"
							minLength="6" minLengthText="${app:i18n_arg2('error.minLength',app:i18n('old.password'),8)}"
							 fieldLabel="${app:i18n('old.password')}" name="oldPassword" inputType="password" var="oldPasswordVar"></ext:textField>
							<ext:textField allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('new.password'))}"
							maxLength="20" maxLengthText="${app:i18n_arg2('error.maxLength',app:i18n('new.password'),20)}"
							minLength="6" minLengthText="${app:i18n_arg2('error.minLength',app:i18n('new.password'),8)}" fieldLabel="${app:i18n('new.password')}" name="newPassword" inputType="password" var="newPasswordVar"></ext:textField>
							<ext:textField allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('comfirm.new.password'))}"
							maxLength="20" maxLengthText="${app:i18n_arg2('error.maxLength',app:i18n('comfirm.new.password'),20)}"
							minLength="6" minLengthText="${app:i18n_arg2('error.minLength',app:i18n('comfirm.new.password'),8)}" fieldLabel="${app:i18n('comfirm.new.password')}" name="newPasswordConfirm" inputType="password" var="newPasswordConfirmVar"></ext:textField>
						</ext:items>
					</ext:formPanel>
				</ext:items>
			</ext:panel>
		</ext:items>
	</ext:window>
    <ext:window var="switchStyleWin" title="${app:i18n('change.theme')}" closeAction="hide" width="220" height="120" layout="fit" modal="true" plain="true">
        <ext:items>
            <ext:formPanel region="center" frame="true" layout="border" >
                <ext:items>
                    <ext:radioGroup region="center" id="styleRadio" autoWidth="false" columns="[160,160,160]">
                        <ext:items>
                            <ext:radio boxLabel="${app:i18n('theme.option.blue')}" name="theme" inputValue=""/>
                            <ext:radio boxLabel="${app:i18n('theme.option.access')}" name="theme" inputValue="xtheme-access" width="150"/>
                            <ext:radio boxLabel="${app:i18n('theme.option.gray')}" name="theme" inputValue="xtheme-gray"/>
                        </ext:items>
                    </ext:radioGroup>
                </ext:items>
            </ext:formPanel>
        </ext:items>
    </ext:window>	
	<ext:script>
    function saveChangePwd() {
		if(changePasswordForm.getForm().isValid()) {
			var oldPasswordVarValue = oldPasswordVar.getValue();
			var newPasswordVarValue = newPasswordVar.getValue();
			var newPasswordConfirmVarValue = newPasswordConfirmVar.getValue();
			if (validatePassword(oldPasswordVarValue, newPasswordVarValue, newPasswordConfirmVarValue))
				changePasswordForm.savePassword();
		}
    }

	// 检验密码表单数据
	function validatePassword(oldPasswordVarValue, newPasswordVarValue, newPasswordConfirmVarValue) {
		if(! checkPassword(oldPasswordVarValue, '${app:i18n('old.password')}')) {
			return false;
		}
		if(! checkPassword(newPasswordVarValue, '${app:i18n('new.password')}')) {
			return false;
		}
		if(! checkPassword(newPasswordConfirmVarValue, '${app:i18n('comfirm.new.password')}')) {
			return false;
		}
		if(oldPasswordVarValue == newPasswordVarValue){
			Ext.MessageBox.alert('${app:i18n('ts')}', '${app:i18n('error.equ')}');
			return false;
		}
		if(newPasswordVarValue != newPasswordConfirmVarValue) {
			Ext.MessageBox.alert('${app:i18n('ts')}', '${app:i18n('error.notEqu')}');
			return false;
		}
		return true;
	}

	// 检验密码，包括长度为八位，必需为数据与字母混合等
	function checkPassword(password, name) {
	    if(password == null || password == ''){
	        Ext.MessageBox.alert('${app:i18n('ts')}', name + '${app:i18n('error.checkPasswordEmpty')}');
	        return false;
	    }
	    if(password.length < 8){
	        Ext.MessageBox.alert('${app:i18n('ts')}', name + '${app:i18n('error.checkPasswordLength')}');
	        return false;
	    }
        var pwdPtn = /^[\x21-\x7e]*$/;
       	if(!pwdPtn.test(password)){
       	    Ext.MessageBox.alert('${app:i18n('ts')}', name + '${app:i18n('error.checkPasswordBuild')}');
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
	        Ext.MessageBox.alert('${app:i18n('ts')}', name + '${app:i18n('error.checkPasswordBuild')}');
			return false;
	    }
	    return true;
	}

	function changePasswordSuccess() {
		changePasswordWin.hide();
		changePasswordForm.getForm().reset();
		Ext.MessageBox.alert("${app:i18n('change.password.success.title')}", "${app:i18n('change.password.success.message')}");
	}

	function changePasswordFailure() {
		Ext.MessageBox.alert("${app:i18n('change.password.failure.title')}", "${app:i18n('change.password.failure.message')}");
	}

    function pageStyle(){
        switchStyleWin.show();
        var styleRadioGroup = Ext.getCmp('styleRadio');
        var ra = styleRadioGroup.items.items;
        for(var idx in ra){
            if(ra[idx].inputValue == '${style}'){
                ra[idx].setValue(true);
                break;
            }
        }
        styleRadioGroup.on("change", switchStyle);
    }

    function switchStyle(g, t){
        var newStyle = t.inputValue;
        if(newStyle == '${style}') return;
        var wb = Ext.Msg.wait("${app:i18n('waitingMessage')}");
        Ext.Ajax.request({
            url:'switchStyle.action',
            params:{
                style:t.inputValue
            },
            callback:function(o,s,r){
                wb.hide();
                switchStyleWin.hide();
                if(s){
                    window.location='frame.action';
                }else
                    Ext.MessageBox.alert("${app:i18n('prompt')}","${app:i18n('change.theme.fail')}");
            }
        });
    }

    function closeAllTab() {
        var ts = tabs.findBy(function() {
            if (this.id != 'home' 
                && this.id != 'homemain' 
                && this.id != 'homemain' 
                && this.id != 'homebar' 
                && this.id != 'homemsg'
                && this.id != 'homebtn' 
                && this.id != 'homehelp') {
                return true;
            }
        });
        for (i = 0; i < ts.length; i++) {
            tabs.remove(ts[i]);
        }
    }

	function showHelp(helpUrl) {
		if (helpUrl && helpUrl.length > 0) {
			window.open('${helpUrlPrefix}' + helpUrl, '_blank', 'height=600,width=800,resizable=yes,scrollbars=yes');
		}
	}

	function logoutConfirmed(s) {
		if (s == 'yes') {
			window.location.href='logout.action';
		}
	}
	function logout() {
		Ext.MessageBox.confirm("${app:i18n('logout')}", "${app:i18n('logout.confirm')}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", logoutConfirmed);
	}

	function openMenu(item, url, desc, helpUrl) {
	    if(url != null && url != ''){
			openPage(item.id, item.text, desc, helpUrl, url, true);
	    }
	}

	function openTab(id, text, desc, helpUrl, url) {
		if(url != null && url != ''){
			openPage(id, text, desc, helpUrl, url, true);
		}
	}

	function refreshTab(id, text, desc, helpUrl, url) {
		refreshPage(id, text, desc, helpUrl, url, true);
	}

	function openPage(id, text, desc, helpUrl, url, closable) {
		var tab = tabs.getItem(id);
		if (typeof(tab) == "undefined" || tab == null) {
			tabs.add(createTabPane(id, text, desc, helpUrl, url, closable)).show();
        } else {
        	tabs.setActiveTab(tab);
        }
	}

	function refreshPage(id, text, desc, helpUrl, url, closable) {
		var tab = tabs.getItem(id);
		if (typeof(tab) != "undefined" && tab != null) {
			tabs.remove(tab);
        }
        tabs.add(createTabPane(id, text, desc, helpUrl, url, closable)).show();
	}

	function createTabPane(id, text, desc, helpUrl, url, closable) {
	    var wrapUrl = url;
	    if(window.wrapParam == undefined){
	       window.wrapParam = Math.random();
	    }
        if(wrapUrl.indexOf('.action?')!= -1){
            wrapUrl += "&_dd=" + window.wrapParam;
        }else{
            wrapUrl += "?_dd=" + window.wrapParam;
        }
		var tabPane = new Ext.Panel({
				id: id,
				title: text,
				border: false,
				closable: closable,
				layout: "border",
				items: [
					new Ext.Panel({
						region: "north",
						cls: "x-toolbar",
						height: 25,
						border: false,
						layout: "border",
						items: [
							new Ext.Panel({
								region: "east",
								width: 44,
								border: false,
								layout: "border",
								items: [
									new Ext.Panel({
										region: "west",
										width: 22,
										border: false,
										layout: "form",
										items: [
											new Ext.Button({
												cls: "x-btn-icon x-toolbar",
												style:"padding:0",
												icon: "${images}/frame/new.gif",
												tooltip: "${app:i18n('open.with.new.tab')}",
												handler: function(){openPage(id + '1', text, desc, helpUrl, url, closable);}
											})
										]
									}),
									new Ext.Panel({
										region: "center",
										border: false,
										layout: "form",
										items: [
											new Ext.Button({
												cls: "x-btn-icon x-toolbar",
												style:"padding:0",
												icon: "${images}/frame/help.gif",
												tooltip: "${app:i18n('help')}",
												handler: function(){showHelp(helpUrl);}
											})
										]
									})
								]
							}),
							new Ext.Panel({
								region: "center",
								border: false,
								html: "<table border='0' class='x-toolbar' cellpadding='0' cellspacing='0' width='100%' height='25' style='background-color: #FFDF8C;'><tr><td width='24' align='center'><img src='${images}/frame/info.gif' border='0' height='16' width='16'/></td><td>" + desc + "</td></tr></table>"
			        		})
						]
			        }),
			        new Ext.Panel({
						region: "center",
						border: false,
			            html: "<iframe name='frame_" + id + "' src='" + wrapUrl + "' width='100%' height='100%' frameborder='0'></iframe>"
			        })
	        	]
	        });
		return tabPane;
	}

	var mask = null;

	// 传入的参数为不被遮罩的元素
	function showModal(win) {
		if (mask == null) {
			if (win && win.el && win.el.dom) {
				mask = framePane.container.createChild({cls:"ext-el-mask"}, win.el.dom);
			} else {
				userWin.show();
				userWin.hide();
				mask = framePane.container.createChild({cls:"ext-el-mask"}, userWin.el.dom);
			}
		}
		Ext.getBody().addClass("x-body-masked");
		mask.setSize(Ext.lib.Dom.getViewWidth(true), Ext.lib.Dom.getViewHeight(true));
		mask.show();
	}

	function hideModal() {
		mask.hide();
		Ext.getBody().removeClass("x-body-masked");
	}

	function requestBlank() {
		Ext.Ajax.request({
			url: 'blank.action',
			method: 'GET',
			disableCaching: true,
			success: function (result, request) {
				if (result.responseText.indexOf('okokokokokok') == -1) {
					window.location.href='logout.action';
				}
			}
		});
	}

	try{
		setInterval("requestBlank()", ${sessionTimeout});
	} catch(e) {
	}
	</ext:script>
</ext:ui>