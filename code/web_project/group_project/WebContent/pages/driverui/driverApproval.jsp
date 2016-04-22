<%@taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<head>
<script src="../scripts/common/moment.min.js"></script>
<link rel="stylesheet"
	href="../jqueryMobile/jquery.mobile-1.3.2.min.css">
<script src="../jqueryMobile/jquery-1.8.3.min.js"></script>
<script src="../jqueryMobile/jquery.mobile-1.3.2.min.js"></script>
</head>
<body>
	<div data-role="page" id="page_index">
		<div data-role="header" data-theme="c">
			<h1>司机组长排班管理</h1>
		</div>

		<div data-role="content" data-theme="c">
			<div data-role="collapsible" data-mini="true" data-collapsed="false" data-theme="c" data-content-theme="c">
				<h4>
					司机组长：
					<s:property value="employee.empName" />
					工号：
					<s:property value="employee.empCode" />
				</h4>
				<ul data-role="listview">
					<li><a href="leaveForward.action?employeeCode=<s:property value="employee.empCode" />" data-ajax="false" data-transition="slide">请假待审批<span class="ui-li-count"><s:property value="resultMap.leaveCount" /></span></a></li>
					<li><a href="transferForward.action?employeeCode=<s:property value="employee.empCode" />" data-ajax="false" data-transition="slide" >调班待审批<span class="ui-li-count"><s:property value="resultMap.exchangeCount" /></span></a></li>
					<li><a href="toLeaderMyScheduledPage.action?employeeCode=<s:property value="employee.empCode" />"  data-ajax="false" data-transition="slide" >我的排班管理</a></li>
				</ul>
			</div>
		</div>

		<div data-role="footer" data-theme="c">
			<h1>页脚文本</h1>
		</div>
	</div>
	
</body>
</html>