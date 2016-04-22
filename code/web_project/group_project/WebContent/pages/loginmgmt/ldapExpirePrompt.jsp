<%@ page contentType="text/html; charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="app" uri="/app-tags"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${app:i18n('Authorize.ldap.passwordExpirePrompt')}</title>
<style type="text/css">
<!--
.txt1 {
	font-size: 12px;
	line-height: 200%;
}
body {
	background-color: #ece9d8;
}
-->
</style>
<script>
function retrunValue(str)
{
   window.returnValue=str;
	if(str == '1'){
		//修改密码
		window.open('https://chgpwd.sf-express.com:6498/');
	}
    window.close();
}
var obj = window.dialogArguments;
function initData(){
	if(obj != null){
		if(obj.type=='92'){
			document.getElementById('lableTip').innerHTML='${app:i18n('Authorize.ldap.passwordExpired')}';
			document.getElementById('btnModify').disabled=true;
			document.getElementById('btnLogin').style.display='none';

		}else if(obj.type=='91'){
			document.getElementById('lableTip').innerHTML='${app:i18n('Authorize.ldap.passwordWillExpire')}';
			document.getElementById('btnLogin').style.display='none';
		}else{
			document.getElementById('lableTip').innerHTML='${app:i18n('Authorize.ldap.passwordExpireDaysStart')} '+obj.value+' ${app:i18n('Authorize.ldap.passwordExpireDaysTail')}';
			document.getElementById('btnExit').style.display='none';
		}
	}
}
</script>
</head>
<base target="_self">
<body scroll="no" onload="initData()">
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="8" height="79">&nbsp;</td>
    <td width="40"><img src="${images}/icon-warning.gif" width="31" height="32" /></td>
    <td width="250"><span class="txt1" id='lableTip'></td>
  </tr>
   <tr>
    <td height="21" colspan="3" align="center">
	<input type="submit" name="button3" id="btnModify" onclick="retrunValue('1')"  value="${app:i18n('Authorize.ldap.modifyPassword')}" />
	&nbsp;&nbsp;&nbsp;&nbsp;
	 <input type="submit" name="button" id="btnLogin" onclick="retrunValue('2')" value="${app:i18n('Authorize.ldap.modifyPasswordNextTime')}" />
     <input type="submit" name="button2" id="btnExit" onclick="retrunValue('3')" value="${app:i18n('Authorize.ldap.exitSystem')}" /></td>
  </tr>
</table>
<p>&nbsp;</p>
</body>