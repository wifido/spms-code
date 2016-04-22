<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="app" uri="/app-tags"%>
<html>
<head>
	<style>
		* {
			padding:0;
			margin:0;
		}
		
		a {
			color:#0000FF;
		}
		
		body {
			text-align:center;
			position:relative;
			height:100%;
			background-color: #DFE8F6;
		}
		
		table {
			height:100%;
			margin:0 auto;
		}
		
		tr {
			height:100%;
			
		}
		
		td {
			height:100%;
			font-size:12px;
		}

		#content {
			color:#FF0000;
			font-size:14px;
		}
		
		#link {
			padding-top:20px;
			text-align:center;
		}
	</style>
</head>
<body>
	<table>
		<tr>
			<td>
				<div id="content">${app:i18n_def('reportDownload.prompt.system.exception','系统出现异常,请与管理员联系')}</div>
				<div id="link"><a href="javascript:void(0);" onclick="history.back();return false;" target="_self">${app:i18n_def('downloadFailure.jsp.1','返回至操作界面')}</a></div>
			</td>
		</tr>
	</table>
</body>
</html>