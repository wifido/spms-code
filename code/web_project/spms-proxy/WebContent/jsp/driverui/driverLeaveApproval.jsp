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
<script type="text/javascript">
	var leave = 1;
	var agree = 2;
	var applyPopupStr = ['', '', '同意请假申请', '驳回请假申请'];
	
	var firstPage = 1;

	$(document).ready(function() {
		loadLeaveData(firstPage, '');

		$("#submitApproval").click(function() {
			submitApproval();
		});

		$("#leaveUl").on("listviewbeforefilter", function(e, data) {
			var searchString  = $(data.input).val();
			
			$("div [data-role=navbar] a").removeClass('ui-btn-active');
			$("#leaveBtnFirstPage").addClass('ui-btn-active');
			
			loadLeaveData(firstPage, searchString);
		});

		$("#leaveBtnFirstPage").click(function() {
			var searchString  = $('input[data-type=search]').val();
			loadLeaveData(firstPage, searchString);
		});
		
		$("#leaveBtnLastPage").click(function() {
			var totalPage = parseInt($("#leaveTotalPage").val());
			var searchString  = $('input[data-type=search]').val();
			loadLeaveData(totalPage,searchString);
		});

		$("#leaveBtnPrevPage").click(function() {
			var currentPage = parseInt($("#leaveCurrentPage").val());
			if (currentPage == firstPage) {
				return false;
			}
			currentPage = currentPage - 1;
			var searchString  = $('input[data-type=search]').val();
			loadLeaveData(currentPage,searchString);
		});

		$("#leaveBtnNextPage").click(function() {
			var currentPage = parseInt($("#leaveCurrentPage").val());
			var totalPage = parseInt($("#leaveTotalPage").val());
			if (currentPage == totalPage) {
				return false;
			}
			currentPage = currentPage + 1;
			var searchString  = $('input[data-type=search]').val();
			loadLeaveData(currentPage,searchString);
		});
	});

	function loadLeaveData(pageIndex,searchString) {
		$("#leaveUl").empty();
		$.mobile.showPageLoadingMsg();
		var pageSize = 5;
		$.getJSON("queryPending.action", {
			applyType : 1,
			start : (pageIndex - 1) * pageSize,
			limit : pageSize,
			searchString : searchString,
			employeeCode : $("#employeeCode").val()
		}, function(json) {
			var result = jQuery.parseJSON(json.resultMap.root);
			$.mobile.hidePageLoadingMsg();
			var totalPage = parseInt($("#leaveTotalPage").val());
			var totalSize = result.resultMap.totalSize;
			var pageCount = Math.ceil(totalSize / pageSize);
			if (totalPage != pageCount) {
				$("#leaveTotalPage").val(pageCount);
				$("#leaveCurrentPage").val(firstPage);
				$("#leaveBtnFirstPage").addClass('ui-btn-active');
			} else {
				$("#leaveCurrentPage").val(pageIndex);
			}
			$.each(result.resultMap.root, function(i, v) {
				$("#leaveUl").append(buildLeaveList(v));
			});
			$("#leaveUl").listview("refresh");
		});
	}

	function buildLeaveList(scheduling) {
		var divHtml = [ '<li data-icon="gear"><a href="#popupMenu" data-rel="popup" onclick="setInitData(' + scheduling.APPLY_ID
				+ ')" data-role="button" data-inline="true" ><table><tr>' + '<td>员工工号：' + scheduling.APPLY_EMPLOYEE_CODE + '</td>'
				+ '<td style="padding-left:15px">排班日期：' + scheduling.DAY_OF_MONTH + '</td></tr><tr>' + '<td>排班班次：' + scheduling.OLD_CONFIG_CODE + '</td>'
				+ '<td style="padding-left:15px">请假原因：' + scheduling.APPLY_INFO + '</td></tr></table></a></li>' ]
		return divHtml.join('');
	}

	function setInitData(applyId) {
		$("#applyPopup p").html("");
		$("#approverInfo").val("");
		$("#applyId").val(applyId);
	}

	function submitApproval() {
		$.post("approval.action", {
			applyId : $("#applyId").val(),
			overruleInfo : $("#approverInfo").val(),
			status : $("#approvalStatus").val(),
			employeeCode : $("#employeeCode").val()
		}, function(json) {
			if (json.resultMap.success) {
				$("#applyPopup p").html(json.resultMap.msg);
				loadLeaveData(1,'');
				return false;
			}
			$("#applyPopup p").html("系统异常！");
		
		});
	}

	function setStatus(approvalStatus) {
		$("#approvalStatus").val(approvalStatus);
		$("#applyPopup h3").html(applyPopupStr[approvalStatus]);
	}
</script>
</head>
<body>
	<div data-role="page" id="page_LeavePending" data-add-back-btn="true">
		<div data-role="header" data-mini="true" data-theme="a">
			<a href="toDriverLeaderPage.action?employeeCode=<s:property value="employeeCode" />" data-role="button" data-mini="true" data-icon="arrow-l">返回</a>
			<h1>请假待审批</h1>
		</div>

		<div data-role="content" data-theme="c" data-mini="true">
		<input type="hidden" id="employeeCode" value="<s:property value="employeeCode" />">
		
			<input type="hidden" name="leaveTotalPage" id="leaveTotalPage" /> <input
				type="hidden" name="leaveCurrentPage" id="leaveCurrentPage" />
			<div data-role="navbar">
				<ul>
					<li><a href="#" id="leaveBtnFirstPage" data-mini="true" data-theme="a">首页</a></li>
					<li><a href="#" id="leaveBtnPrevPage" data-mini="true" data-theme="a">上一页</a></li>
					<li><a href="#" id="leaveBtnNextPage" data-mini="true" data-theme="a">下一页</a></li>
					<li><a href="#" id="leaveBtnLastPage" data-mini="true" data-theme="a">尾页</a></li>
				</ul>
			</div>

			<div>
				</br>
				<ul id="leaveUl" data-role="listview" data-inset="true"
					data-filter="true" data-filter-placeholder="请输入配班代码、时间。。。"
					data-filter-theme="c">
				</ul>
			</div>
		</div>

		<div data-role="popup" id="popupMenu" data-theme="c">
			<ul data-role="listview" data-inset="true" style="min-width: 210px;"
				data-theme="c">
				<li data-role="divider" data-theme="c"></li>
				<li><a href="#applyPopup" data-rel="popup"
					onclick="javaScript:setStatus(2);" data-position-to="window"
					data-inline="true" data-theme="c" data-transition="pop">同意</a></li>
				<li><a href="#applyPopup" data-rel="popup"
					onclick="javaScript:setStatus(3);" data-position-to="window"
					data-inline="true" data-theme="c" data-transition="pop">驳回</a></li>
			</ul>
		</div>

		<div data-role="popup" id="applyPopup" data-theme="c"
			style="width: 250px" class="ui-corner-all">
			<div style="padding: 10px 20px;">
				<h3></h3>
				<textarea rows="3" cols="1" id="approverInfo" name="approverInfo"
					value="" placeholder="审批原因,非必填"></textarea>
				<input type="hidden" name="applyId" id="applyId" /> <input
					type="hidden" name="applyType" id="applyType" /> <input
					type="hidden" name="approvalStatus" id="approvalStatus" />
				<p></p>
				<a href="#" data-role="button" data-mini="true" data-inline="true"
					id="submitApproval" data-theme="x" >提交</a> <a href="#" data-role="button"
					data-mini="true" data-inline="true" id="returnApproval" data-rel="back" data-theme="y">返回</a>
			</div>
		</div>

		<div data-role="footer" data-theme="c">
			<h1></h1>
		</div>
	</div>
</body>
</html>