<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<head>
<link rel="stylesheet" href="../scripts/jqueryMobile/jquery.mobile-1.3.2.min.css">
<link rel="stylesheet" href="../scripts/jqueryMobile/theme_x-y.css">
<script src="../scripts/common/moment.min.js"></script>
<script src="../scripts/jqueryMobile/jquery-1.8.3.min.js"></script>
<script src="../scripts/jqueryMobile/jquery.mobile-1.3.2.min.js"></script>
<script src="../scripts/driverui/driverApp.js"></script>
</head>
<body>
	<div data-role="page" id="page_index">
		<div data-role="header" data-theme="a">
			<h1>司机排班管理</h1>
			<input id="yearWeek" name="yearWeek" type="hidden" value="<s:property value="yearWeek" />">
			<input id="currentWeek" name="currentWeek" type="hidden" value="<s:property value="yearWeek" />">
		</div>

		<div data-role="content" data-theme="c">
			<div data-role="collapsible" data-mini="true" data-collapsed="false" data-theme="c" data-content-theme="c">
				<h4>
					驾驶员：
					<s:property value="#attr.employee.empName" />
					工号：
					<s:property value="#attr.employee.empCode" />
					<input type="hidden"  id ="empDutyName" value = "<s:property value="#attr.employee.empDutyName" />"/>
					<input type="hidden"  id ="id_employeeCode" value = "<s:property value="#attr.employee.empCode" />"/>
				</h4>
				<ul data-role="listview">
					<li><a href="#page_myScheduling" id = 'MY' data-transition="slide">我的排班</a></li>
					<li><a href="toMyLeave.action?employeeCode=<s:property value="#attr.employee.empCode" />" id="myLeaveA" data-ajax="false">我的请假</a></li>
					<li><a href="toMyExchangeScheduling.action?employeeCode=<s:property value="#attr.employee.empCode" />" data-ajax="false">我的调班</a></li>
					<s:if test="#attr.employee.empDutyName.indexOf(\"司机组长\") !=-1">
						<li><a href="toDriverLeaderPage.action?employeeCode=<s:property value="#attr.employee.empCode" />" data-ajax="false">我的待审批</a></li>
					</s:if>
				</ul>
			</div>
		</div>

		<div data-role="footer" data-theme="c">
			<h1></h1>
		</div>
	</div>

	<div data-role="page" id="page_myScheduling" data-add-back-btn="true">
		<div data-role="header" data-mini="true" data-theme="a">
			<a href="#page_index" data-role="button" data-mini="true" data-icon="arrow-l">返回</a>
			<h1>我的排班</h1>
		</div>
		<div data-role="content" data-theme="c">
			<div data-role="navbar">
				<ul>
					<li><a href="#" id="prevWeekBtn" data-mini="true" data-theme="a">前一周</a></li>
					<li><a href="#" id="currentWeekBtn" data-mini="true" data-theme="a">当前周</a></li>
					<li><a href="#" id="nextWeekBtn" data-mini="true" data-theme="a">下一周</a></li>
				</ul>
			</div>

			<div data-role="collapsible-set" data-content-theme="c"></div>

			<div data-role="popup" id="popupCloseRight" class="ui-content" style="max-width: 280px">
				<a href="#" data-rel="back" data-role="button" data-theme="c" data-icon="delete" data-iconpos="notext"
					class="ui-btn-right">Close</a>
				<p></p>
			</div>

			<div data-role="popup" id="revokeLeaveWindow" data-overlay-theme="c" data-theme="c" data-dismissible="false"
				style="max-width: 400px;" class="ui-corner-all">
				<form id="revokeLeaveForm">
					<div data-role="content" data-theme="c" class="ui-corner-bottom ui-content">
						<input type="hidden" name="dayOfMonth">
						<input type="hidden" name="employeeCode">
						<h3 class="ui-title">你确定要撤销请假吗?</h3>
						<a href="#" data-role="button" data-inline="true" data-rel="back"  data-theme="x">取消</a> <a href="#"
							id="btnConfirmRevokeLeave" data-role="button" data-inline="true" data-rel="back" data-transition="flow"
							data-theme="x">撤销</a>
					</div>
				</form>
			</div>

			<div data-role="popup" id="revokeExchangeSchedulingWindow" data-overlay-theme="c" data-theme="c"
				data-dismissible="false" style="max-width: 400px;" class="ui-corner-all">
				<form id="revokeExchangeSchedulingForm">
					<div data-role="content" data-theme="c" class="ui-corner-bottom ui-content">
						<input type="hidden" name="dayOfMonth">
						<input type="hidden" name="employeeCode">
						<h3 class="ui-title">你确定要撤回调班吗?</h3>
						<a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="x">取消</a> <a href="#"
							id="btnConfirmRevokeExchangeScheduling" data-role="button" data-inline="true" data-rel="back" data-transition="flow" data-theme="x">撤回</a>
					</div>
				</form>
			</div>

			<div data-role="footer" data-theme="c"  data-position="fixed" >
				<a href="javaScript:confirmScheduling()" data-role="button"   id="btnConfirmScheduling"
					data-icon="check" data-theme="x">提交确认</a>
			</div>
		</div>
	</div>

	<div data-role="page" id="selectConfigCode" data-close-btn="none">
		<div data-role="header" data-mini="true" data-theme="a">
			<h1>调班>选择配班</h1>
			<a href="#" data-role="button" data-mini="true" data-icon="arrow-l" data-rel="back">取消</a>
		</div>
		<div data-role="content" data-theme="c" data-mini="true">
			<input type="hidden" name="totalPage" id="totalPage" />
			<input type="hidden" name="currentPage" id="currentPage" />
			<div data-role="navbar">
				<ul>
					<li><a href="#" id="btnFirstPage" data-mini="true" data-theme="a">首页</a></li>
					<li><a href="#" id="btnPrevPage" data-mini="true" data-theme="a">上一页</a></li>
					<li><a href="#" id="btnNextPage" data-mini="true" data-theme="a">下一页</a></li>
					<li><a href="#" id="btnLastPage" data-mini="true" data-theme="a">尾页</a></li>
				</ul>
			</div>

			<div>
				</br>
				<ul id="autocomplete" data-role="listview" data-inset="true" data-filter="true"
					data-filter-placeholder="请输入配班代码、时间、网点查询。。。。" data-filter-theme="c">
				</ul>
			</div>
		</div>
	</div>

	<div data-role="page" id="commitExchangeSchedulingPage" data-close-btn="none">
		<div data-role="header" data-mini="true" data-theme="a">
			<h1>调班>提交申请</h1>
			<a href="#" data-role="button" data-mini="true" data-icon="arrow-l" data-rel="back">上一步</a>
		</div>
		<div data-role="content" data-theme="c" data-mini="true">
			<div data-role="popup" id="popupCloseRight_1" class="ui-content" style="max-width: 280px">
				<a href="#" data-rel="back" data-role="button" data-theme="c" data-icon="delete" data-iconpos="notext"
					class="ui-btn-right">Close</a>
				<p></p>
			</div>
			<form id="exchangeSchedulingForm">
				<b> 调班前配班代码： </b>
				<input id="oldSchedulingCode" name="applyRecord.oldConfigCode" value="" readOnly="true" />
				<input type="hidden" id="employeeCode" name="applyRecord.applyEmployeeCode"/>
				<b> 调班后配班代码： </b>
				<input id="newSchedulingCode" name="applyRecord.newConfigCode" value="" readOnly="true">
				<b> 审批人： </b>
				<input id="approver" name="applyRecord.approver" value="" readOnly="true">
				<b> 备注： </b>
				<textarea rows="3" cols="1" id="exchangeSchedulingApplyInfo" name="applyRecord.applyInfo" value="" placeholder="调班原因,非必填"></textarea>
				<input type="button" data-inline="true" id="btnCommitExchangeScheduling" onclick="applyExchangeScheduling()"
					value="提交"  data-theme="x"/>
				<input id="applyRecordDayOfMonth" type="hidden" name="applyRecord.dayOfMonth" />
				<a href="javaScript:window.history.go(-3)" data-role="button" data-inline="true" id="cancleCommitBtn" data-theme="x">取消</a>
			</form>
		</div>
	</div>
	
	<input type="hidden" name="departmentCode" id="optionApprover_departmentCode"/> 
	<input type="hidden" name="configureCode" id="optionApprover_configureCode"/>
	<input type="hidden" name="dayOfMonth" id="optionApprover_dayOfMonth"/>
	
	<div data-role="page" id="optionApprover" data-close-btn="none">
		<div data-role="header" data-mini="true" data-theme="a">
			<h1>选择审批人</h1>
			<a href="#" data-role="button" data-mini="true" data-icon="arrow-l" data-rel="back">取消</a>
		</div>
		<div data-role="content" data-theme="c" data-mini="true">
			<div data-role="navbar">
				<ul>
					<li><a href="#" id="homePage" data-mini="true" data-theme="a">首页</a></li>
					<li><a href="#" id="pageUp" data-mini="true" data-theme="a">上一页</a></li>
					<li><a href="#" id="pageDown" data-mini="true" data-theme="a">下一页</a></li>
					<li><a href="#" id="trailerPage" data-mini="true" data-theme="a">尾页</a></li>
				</ul>
			</div>

			<div>
				</br>
				<ul id="autocomplete_Leave" data-role="listview" data-inset="true" data-filter="true"
					data-filter-placeholder="请输入员工姓名、员工工号查询。。。。" data-filter-theme="c">
				</ul>
			</div>
		</div>
	</div>
	
	<div data-role="page" id="addLeave" data-close-btn="none">
		<div data-role="header" data-theme="c">
			<h3>我的请假</h3>
			<a href="#" data-role="button" data-mini="true" data-icon="arrow-l" data-rel="back">上一步</a>
		</div>
		<div data-role="content">
			<form id="leaveForm">
				<div style="padding: 10px 20px;">
					<input type="hidden" id="dayOfMonth" name="applyRecord.dayOfMonth">
					<input type="hidden" id="applyEmployeeCode" name="applyRecord.applyEmployeeCode"
						value="<s:property value="employee.empCode" />">
					<label for="text-basic">审批人:</label>	
					<input type="text" id="approver" name="applyRecord.approver" readonly="readonly">
					<input type="hidden" id="departmentCode" name="applyRecord.departmentCode">
					<input type="hidden" id="applyType" name="applyRecord.applyType" value="1">
					<input type="hidden" id="status" name="applyRecord.status" value="1">
					<input type="hidden" id="oldConfigCode" name="applyRecord.oldConfigCode" value="1">

					<textarea rows="3" cols="1" id="applyInfo" name="applyRecord.applyInfo" value="" placeholder="请假原因,必填"></textarea>
					<h3 id="notification"></h3>
					<a href="javaScript:window.history.go(-2)" data-role="button" data-inline="true" id="cancleCommitBtn" data-theme="x">关闭</a>
					<a href="#" data-role="button" data-inline="true" id="submitLeaveBtn" data-theme="x" >提交</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>