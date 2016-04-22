<%@taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="../jqueryMobile/jquery.mobile.structure-1.3.2.min.css">
<link rel="stylesheet" href="../jqueryMobile/jquery.mobile-1.3.2.min.css">
<script src="../jqueryMobile/jquery-1.8.3.min.js"></script>
<script src="../jqueryMobile/jquery.mobile-1.3.2.min.js"></script>
<style type="text/css">
	#table-column-toggle tr td{
		line-height: 40px
	}
	#table-column-toggle tr td a{
		/* margin-left: 40px */
		float: right;
	} 
</style>
<script type="text/javascript">
	var pageno = 1;
	var pageSize = 5;
	var totalPage = 0;

	$(document).ready(function() {
		$('#homePage').on('click', function() {
			pageno = 1;
			loadExchangeScheduling();
		});

		$('#pageUp').on('click', function() {
			if (pageno == 1)
				return false;
			pageno += -1;
			loadExchangeScheduling();
		});

		$('#pageDown').on('click', function() {
			if (pageno == totalPage)
				return false;
			pageno += 1;
			loadExchangeScheduling();
		});

		$('#trailerPage').on('click', function() {
			pageno = totalPage;
			loadExchangeScheduling();
		});
	});

	$(document).on("pagebeforecreate", "#myExchangeScheduling", function() {
		loadExchangeScheduling();
	});

	function loadExchangeScheduling() {
		$.ajax({
			type : 'POST',
			async: false,
			url : 'myExchangScheduling.action',
			data : {
				pageIndex : pageno-1,
				pageSize : pageSize,
				employeeCode : $('#applyEmployeeCode').val()
			},
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function() {
				$.mobile.hidePageLoadingMsg();
			},
			success : function(resp) {
				totalPage = parseInt(resp.resultMap.totalSize);
				var $tbody = $('#table-column-toggle tbody');
				$tbody.html('');
				var html = '';
				$.each(resp.resultMap.root,function(i, row){
						html += '<tr>';
							html += '<td>';
							html += row.dayOfMonth;
							html += '</td>';
							html += '<td>';
							html += approverStatus(row.status);
							html += '<a href="#myExchangeSchedulingDetail?newConfigCode='+row.newConfigCode+'&oldConfigCode='+row.oldConfigCode+'&APPORVE_TIME='+row.apporveTime+'&DAY_OF_MONTH='+row.dayOfMonth+'&APPLY_INFO='+row.applyInfo+'&APPROVER='+row.approver+'&APPROVER_INFO='+row.approverInfo+'&STATUS='+row.status+'"  data-rel="dialog" data-role="button"  data-icon="bars" data-iconpos="notext">详细信息</a>';
							html += '</td>';
							html += '</tr>';
				});
				$tbody.html(html);
				$tbody.trigger("updatelayout");
				$tbody.trigger("create") ;
			}
		});
	}

	function approverStatus(status) {
		var applyStatus = '审批中';
		if (2 == status) {
			applyStatus = '审批通过';
		}
		if (3 == status) {
			applyStatus = '驳回';
		}
		if (4 == status) {
			applyStatus = '已撤销';
		}
		return applyStatus;
	}
	
	$(document).on("pagebeforechange", function(e, data) {
		var url = data.toPage;
		if (typeof url == "string") {
			var pageIdOfMyLeaveDetailInfo = '#myExchangeSchedulingDetail';
			if (url.indexOf(pageIdOfMyLeaveDetailInfo) != -1) {
				var params = parseUrl(url, pageIdOfMyLeaveDetailInfo);
				$("#detail_day_of_month").html(params['DAY_OF_MONTH']);
				$("#detail_apply_info").html(isNotEmpty(params['APPLY_INFO']));
				$("#detail_status").html(approverStatus(params['STATUS']));
				$("#detail_approver").html(isNotEmpty(params['APPROVER']));
				$("#detail_approval_info").html(isNotEmpty(params['APPROVER_INFO']));
				$("#detail_approval_time").html(isNotEmpty(params['APPORVE_TIME']));
				$("#detail_old_configCode").html(isNotEmpty(params['oldConfigCode']));
				$("#detail_new_configCode").html(isNotEmpty(params['newConfigCode']));
			}
		}
	});

	function parseUrl(url, pageId) {
		var paramStr = url.substr(url.indexOf(pageId) + pageId.length + 1);
		var params = new Object();
		var strs = paramStr.split("&");
		for ( var i = 0; i < strs.length; i++) {
			params[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
		return params;
	}

	function isNotEmpty(val) {
		if(val==null||val=='null'){
			return '';
		}
		return val;
	}


	
</script>

</head>
<body>
	<div data-role="page" id="myExchangeScheduling">
		<div data-role="header" data-theme="c">
			<h1>我的调班</h1>
			<a href="#page_index" data-rel="back" data-icon="arrow-l" data-min="true">返回</a>
			<input type="hidden" id="applyEmployeeCode" name="applyRecord.applyEmployeeCode"
							value="<s:property value="employee.empCode" />">
		</div>

		<div data-role="content">
			<div data-role="navbar" data-grid="c">
				<ul>
					<li><a href="#" class="ui-btn-active" data-mini="true" id="homePage">首页</a></li>
					<li><a href="#" data-mini="true" id="pageUp">上一页</a></li>
					<li><a href="#" data-mini="true" id="pageDown">下一页</a></li>
					<li><a href="#" data-mini="true" id="trailerPage">尾页</a></li>
				</ul>
			</div>
			
			<table data-role="table" id="table-column-toggle" data-mode="" class="ui-responsive table-stroke">
				<thead>
					<tr>
						<th>调班日期</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>

		</div>

		<div data-role="footer" data-theme="c">
			<h1>请联系管理员</h1>
		</div>
	</div>
	
	<div data-role="page" id="myExchangeSchedulingDetail">
		<div data-role="header" data-theme="c">
			<h1>调班详情页面</h1>
			<a href="#page_index" data-rel="back" data-icon="arrow-l"
				data-min="true">返回</a> <input type="hidden" id="applyEmployeeCode"
				name="applyRecord.applyEmployeeCode"
				value="<s:property value="employee.empCode" />">
		</div>

		<div data-role="content">
			<div data-role="navbar">
				<ul>
					<li>调班日期</li>
					<li id="detail_day_of_month"></li>
					<li>调班原因</li>
					<li id="detail_apply_info"></li>
					<li>状态</li>
					<li id="detail_status"></li>
					<li>调班前配班代码</li>
					<li id="detail_old_configCode"></li>
					<li>调班后配班代码</li>
					<li id="detail_new_configCode"></li>
					<li>审批人</li>
					<li id="detail_approver"></li>
					<li>审批备注</li>
					<li id="detail_approval_info"></li>
					<li>审批时间</li>
					<li id="detail_approval_time"></li>
				</ul>
			</div>
		</div>

	</div>
	
</body>
</html>