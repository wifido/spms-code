// <%@ page language="java" contentType="text/html; charset=utf-8"%>
	var leave = 1; 			  // 请假状态
	var change_the_shift = 2; // 调班状态
	
	var unapproved = 1;       // 审批中
	var approval = 2;		  // 审批通过
	var reject = 3;			  // 驳回
	var revocation = 4;		  // 撤销
	
	var pageno = 1;			  // 页码
	var pageSize = 10;		  // 页面条数
	var totalSize = 0;		  // 总记录数
	var totalPage = 0;		  // 总页数
	
	var optionType = true;		  // 选择类型：true 构建请假审批页面路径、false 构建调班审批页面路径(默认true)
	
	$(document).ready(function() {
		$("#prevWeekBtn").click(function() {
			loadPrevWeekData();
		});
		$("#currentWeekBtn").click(function() {
			loadCurrentWeekData();
		});
		$("#nextWeekBtn").click(function() {
			loadNextWeekData();
		});
		
		$('#submitLeaveBtn').bind('click', function() {
			var formData = $('#leaveForm').serialize();
			
			if ($('#leaveForm textarea[id=applyInfo]').val() == "") {
				$("#leaveForm #notification").html("请假原因不能为空！");
				return false;
			}
			$.ajax({
				type : "POST",
				url : "toLeave.action",
				cache : false,
				async : false,
				data : formData,
				success : onSuccess
			});
			
			return false;
		});
		
		$("#autocomplete_Leave").on("listviewbeforefilter", function(e, data) {
			pageno = 1;
			$('#optionApprover div[data-role=navbar] a').removeClass('ui-btn-active');
			$("#homePage").addClass('ui-btn-active');
			queryApprover($(data.input).val());
		});

		$("#autocomplete").on("listviewbeforefilter", function(e, data) {
			$('#selectConfigCode div[data-role=navbar] a').removeClass('ui-btn-active');
			$("#btnFirstPage").addClass('ui-btn-active');
			
			var currentPage =  $("#currentPage").val();
			var searchString  = $(data.input).val();
			loadLineConfigData(currentPage,searchString);
		});
		
		$("#btnFirstPage").click(function() {
			var searchString  = $('input[data-type=search]').val();
			loadLineConfigData(1,searchString);
		});
		
		$("#btnLastPage").click(function() {
			var totalPage = parseInt($("#totalPage").val());
			var searchString  = $('input[data-type=search]').val();
			loadLineConfigData(totalPage,searchString);
		});
		
		$("#btnPrevPage").click(function() {
			var searchString  = $('input[data-type=search]').val();
			var currentPage =  parseInt($("#currentPage").val());
			if(currentPage==1){
				return false;
			}
			currentPage = currentPage -1 ;
			loadLineConfigData(currentPage,searchString);
		});
		
		$("#btnNextPage").click(function() {
			var currentPage =  parseInt($("#currentPage").val());
			var totalPage = parseInt($("#totalPage").val());
			if(currentPage == totalPage){
				return false;
			}
			currentPage = currentPage + 1;
			var searchString  = $('input[data-type=search]').val();
			loadLineConfigData(currentPage,searchString);
		});
		
		$('#btnConfirmRevokeLeave').bind('click', function() {
			revokeLeave();
		});
		
		$('#btnConfirmRevokeExchangeScheduling').bind('click', function() {
			revokeExchangeScheduling();
		});
		
		if ($("#empDutyName").val() =='司机组长'){
			$("#page_index div[data-role=header] a").attr('data-rel','back');
			$("#page_index div[data-role=header] a").css('display','block');
		}
		
		$('#homePage').on('click', function(){
	 		pageno = 1;
	 		queryApprover($('#optionApprover input[data-type=search]').val());
	 	});
	 	
	 	$('#pageUp').on('click', function(){
	 		if (pageno == 1)
	 			return false;
	 		
	 		pageno += -1; 
	 		queryApprover($('#optionApprover input[data-type=search]').val());
	 	});
	 	
	 	$('#pageDown').on('click', function(){
	 		if (pageno == totalPage)
	 			return false;
	 		
	 		pageno += 1;	
	 		queryApprover($('#optionApprover input[data-type=search]').val());
	 	});
	 	
	 	$('#trailerPage').on('click', function(){
	 		pageno = Math.ceil(totalSize / pageSize);
	 		queryApprover($('#optionApprover input[data-type=search]').val());
	 	});
	});
	
	function queryApprover(searchString) {
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		$.ajax({
			type:'POST',
			url: 'queryTheApprover.action',
			async: false,
			data: {
				start : (pageno - 1) * pageSize,
				limit : pageno * pageSize,
				departmentCode : $('#optionApprover_departmentCode').val()/*$("#optionApprover input[name=departmentCode]").val()*/,
				searchString : searchString
			},
			success : function(resp) {
				totalSize = resp.resultMap.totalSize;
				totalPage = Math.ceil(totalSize / pageSize);
				
				var $ul = $('#autocomplete_Leave');
				$ul.html("<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>");
				$ul.listview("refresh");
				$.mobile.hidePageLoadingMsg();
				var html = '';
				$.each(resp.resultMap.root,function(i, row){
					html += buildTheApproverData(row);
				});
				$ul.html(html);
				$ul.listview("refresh");
				$ul.trigger("updatelayout");
			}
		});
	}
	
	function buildTheApproverData(row) {
		var html = '';
		if (optionType) {
			html += '<li><a href="#addLeave?dayOfMonth=' + $("#optionApprover_dayOfMonth").val()
					+ '&employeeCode=' + $("#optionApprover_employeeCode").val()
					+ '&departmentCode=' + $("#optionApprover_departmentCode").val()
					+ '&configureCode=' + $("#optionApprover_configureCode").val()
					+ '&approver=' + row.EMP_CODE + '" data-rel="dialog">';
			html += '<p>姓名:&nbsp;' + row.EMP_NAME
					+ '&nbsp;&nbsp;&nbsp; 员工工号: ' + row.EMP_CODE
					+ '&nbsp;&nbsp;&nbsp; 职位:&nbsp;'
					+ row.EMP_DUTY_NAME + '</p>';
			html += '</a></li>';
			
			return html;
		}

		html += '<li><a href="#commitExchangeSchedulingPage?approver=' 
				+ row.EMP_CODE + '" data-rel="dialog">';
		html += '<p>姓名:&nbsp;' + row.EMP_NAME
				+ '&nbsp;&nbsp;&nbsp; 员工工号: ' + row.EMP_CODE
				+ '&nbsp;&nbsp;&nbsp; 职位:&nbsp;'
				+ row.EMP_DUTY_NAME + '</p>';
		html += '</a></li>';
		
		return html;
	}
	
	$(document).on("pageshow", "#optionApprover", function(index, data) {
		pageno = 1;
		queryApprover("");
		$("#homePage").addClass('ui-btn-active');
	});
	
	$(document).on("pagebeforecreate", "#page_myScheduling", function() {
		loadCurrentWeekData();
		$("#currentWeekBtn").addClass('ui-btn-active');
	});

	$(document).on("pageshow", "#commitExchangeSchedulingPage", function() {
		$("#btnCommitExchangeScheduling").button("enable");
		$("#commitExchangeSchedulingPage div[data-role=header] a").removeClass("ui-disabled");
		$("#cancleCommitBtn span span").text('取消');
	});

	$(document).on("pageshow", "#selectConfigCode", function() {
		loadLineConfigData(1,'');
		$("#btnFirstPage").addClass('ui-btn-active');
	});

	$(document).on("pagebeforechange", function(e, data) {
		var url = data.toPage;
		if (typeof url == "string") {
			var pageIdOfSelectConfigCode = '#selectConfigCode';
			var pageIdOfCommitExchangeSchedulingPage = '#commitExchangeSchedulingPage';
			var pageIdOfAddLeave = '#addLeave';
			var pageIdOfOptionApprover = "#optionApprover";
			
			if (url.indexOf(pageIdOfSelectConfigCode) != -1) {
				optionType = false;
				var params = parseUrl(url, pageIdOfSelectConfigCode);
				var dayOfMonth = params['dayOfMonth'];
				var oldSchedulingCode = params['oldSchedulingCode'];
				
				$('#employeeCode').val(params['employeeCode']);
				
				if(params['departmentCode']!= null){
					$('#optionApprover_departmentCode').val(params['departmentCode']);
				}
				
				if(dayOfMonth!= null){
					$('#applyRecordDayOfMonth').val(dayOfMonth);
				}
				if(oldSchedulingCode!= null){
					$('#oldSchedulingCode').val(oldSchedulingCode);
				}
			}
			if (url.indexOf(pageIdOfCommitExchangeSchedulingPage) != -1) {
				var params = parseUrl(url, pageIdOfCommitExchangeSchedulingPage);
				$('#commitExchangeSchedulingPage #approver').val(params['approver']);
			}
			
			if (url.indexOf(pageIdOfOptionApprover) != -1) {
				var params = parseUrl(url, pageIdOfOptionApprover);
				if(params['newSchedulingCode']!= null) {
					$("#newSchedulingCode").val(params['newSchedulingCode']);
				}
				
				if (params['employeeCode'] != null) {
					$('#exchangeSchedulingForm input[id=employeeCode]').val(params['employeeCode']);
				}
			}
			
			if (url.indexOf(pageIdOfAddLeave) != -1) {
				var params = parseUrl(url, pageIdOfAddLeave);
				
				$("#leaveForm #departmentCode").val(params['departmentCode']);
				$("#leaveForm #oldConfigCode").val(params['configureCode']);
				$("#leaveForm #dayOfMonth").val(params['dayOfMonth']);
				$("#leaveForm #approver").val(params['approver']);
				
				$('#submitLeaveBtn').removeClass("ui-disabled");
				$('#notification').text("");
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
	
	function loadLineConfigData(pageIndex, searchStr) {
		var dayOfMonth = $('#applyRecordDayOfMonth').val();
		var html = '';
		var $ul = $("#autocomplete");
		$ul.html("<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>");
		$ul.listview("refresh");
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		var pageSize = 10;
		$.ajax({
			url : "queryLineConfigueForExchangeScheduling.action",
			type : "POST",
			dataType : "json",
			data : {
				CODE : searchStr,
				start : (pageIndex - 1) * pageSize,
				limit : pageSize,
				dayOfMonth : dayOfMonth,
				employeeCode : $('#applyEmployeeCode').val(),
				currentCode : $('#oldSchedulingCode').val()
			},
			success : function(data, textStatus) {
				$.mobile.hidePageLoadingMsg();
				var totalPage = parseInt($("#totalPage").val());
				var totalSize = data.resultMap.totalSize;
				var pageCount = Math.ceil(totalSize / pageSize);
				if (totalPage != pageCount) {
					$("#totalPage").val(pageCount);
					$("#currentPage").val(1);
				} else {
					$("#currentPage").val(pageIndex);
				}
				
				if ($('#oldSchedulingCode').val() != "休") {
					html += '<li><a href="#optionApprover?newSchedulingCode=休&employeeCode='+$('#applyEmployeeCode').val()+'" data-rel="dialog">';
					html += '<h4>休</h4>';
					html += '</a></li>';
				}
				
				$.each(data.resultMap.root, function(i, val) {
					
					html += '<li><a href="#optionApprover?newSchedulingCode=' + 

					(val) + '&employeeCode='+$('#applyEmployeeCode').val()+'" data-rel="dialog">';
					html += '<h4>' + formatLineConfiguerCode(val) + '</h4>';
					if (null == val.SOURCE_CODE) {
						html += '<p>机动班     发车时间:(' + val.START_TIME + ')  收车时间:(' + val.END_TIME + ')</p>';
					} else {
						html += '<p>发车:' + val.SOURCE_CODE + ' (' + val.START_TIME + ') 收车：' + val.DESTINATION_CODE + '(' + val.END_TIME + ')</p>';
					}
					html += '</a></li>';
				});
				
				
				$ul.html(html);
				$ul.listview("refresh");
				$ul.trigger("updatelayout");

			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$.mobile.hidePageLoadingMsg();
			}
		});
	}

	function formatLineConfiguerCode(lineConfiguer) {
		return lineConfiguer.DEPT_CODE + '-' + lineConfiguer.CODE;
	}

	function loadData(year_week) {
		var empCode=  $('#applyEmployeeCode').val();
		var datacontent = $("#page_myScheduling div[data-role=content]");
		var collapsibleSet = $("#page_myScheduling div[data-role=content] div[data-role=collapsible-set]");
		collapsibleSet.children().remove();
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		$.getJSON("myScheduling.action", {
			yearWeek : year_week,
			employeeCode : empCode
		}, function(json) {
			$.mobile.hidePageLoadingMsg();
			$('#page_myScheduling div[data-role=content] div[data-role=collapsible-set]').html('');
			if (json.resultMap.root.length < 1) {
				$("#btnConfirmScheduling").addClass("ui-disabled");
				collapsibleSet.append('<div data-role="collapsible" data-mini="true" data-theme="c" data-content-theme="c"><h4>没有排班数据</h4></div>');
				datacontent.trigger('create');
				return false;
			}
			if (1 == json.resultMap.root[0].confirmStatus) {
				$("#btnConfirmScheduling").addClass("ui-disabled");
			} else {
				$("#btnConfirmScheduling").removeClass("ui-disabled");
			}
			$.each(json.resultMap.root, function(i, v) {
				collapsibleSet.append(buildList(v));
			});
			datacontent.trigger('create');
			$.each(json.resultMap.root, function(i, v) {
				initLeaveButton(v);
				initExchangeButton(v);
				setLeaveButtonDisabled(v);
				setExchangeSchedulingButtonDisabled(v);
			});
		});
		$("#page_myScheduling div[data-role=header] h1").html('我的排班 (' + year_week.split("-")[1] + '周)');
	}

	function loadCurrentWeekData() {
		var currentWeek = $("#currentWeek").val();
		loadData(currentWeek);
		$("#yearWeek").val(currentWeek);
		$("#currentWeekBtn").addClass('ui-btn-active');
	}

	function loadPrevWeekData() {
		var yearWeek = $("#yearWeek").val();
		var yearWeekAsDate = moment(yearWeek, "YYYY-WW");
		var currentWeek = $("#currentWeek").val();
		var currentWeekAsDate = moment(currentWeek, "YYYY-WW");
		if (moment(yearWeek, "YYYY-WW").add(3, 'weeks').isBefore(currentWeekAsDate)) {
			$("#popupCloseRight p").html("只显示当前周往前四周的排班数据！");
			$("#popupCloseRight").popup("open");
			return false;
		}
		yearWeekAsDate = yearWeekAsDate.subtract(1, 'weeks');
		yearWeek = yearWeekAsDate.format("YYYY-WW");
		loadData(yearWeek);
		$("#yearWeek").val(yearWeek);
	}

	function loadNextWeekData() {
		var yearWeek = $("#yearWeek").val();
		var yearWeekAsDate = moment(yearWeek, "YYYY-WW");
		var currentWeek = $("#currentWeek").val();
		var currentWeekAsDate = moment(currentWeek, "YYYY-WW");
		if (moment(yearWeek, "YYYY-WW").isAfter(currentWeekAsDate)) {
			$("#popupCloseRight p").html("只显示当前周往后一周的排班数据！");
			$("#popupCloseRight").popup("open");
			return false;
		}
		yearWeekAsDate = yearWeekAsDate.add(1, 'weeks');
		yearWeek = yearWeekAsDate.format("YYYY-WW");
		loadData(yearWeek);
		$("#yearWeek").val(yearWeek);
	}

	function confirmScheduling() {
		var year_week = $("#yearWeek").val();
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		$.getJSON("confirmScheduling.action", {
			yearWeek : year_week,
			employeeCode : $("#id_employeeCode").val()
		}, function(json) {
			$.mobile.hidePageLoadingMsg();

			if ('success' == json.resultMap.root) {
				$("#popupCloseRight p").html("确认成功！");
				$("#popupCloseRight").popup("open");
				$("#btnConfirmScheduling").addClass("ui-disabled");
			} else {
				$("#popupCloseRight p").html("确认失败！");
				$("#popupCloseRight").popup("open");
			}
		});
	}

	function buildList(scheduling) {
		var divHtml = [
				'<div data-role="collapsible" data-collapsed="true" data-mini="true" data-theme="c" data-content-theme="c">',
				'<h4 class="showTitle_'+scheduling.dayOfMonth+'">' + scheduling.dayOfMonth + '班次:' + scheduling.configureCode + '<span class="AppState ui-li-count ui-btn-corner-all">'
						+ buildApply(scheduling) + '</span>' + '</h4>','<ul id="mySchedulingListview" data-role="listview" >',buildDetail(scheduling),'<li><p>',buildOperationButton(scheduling),
				'</p></li>','	</ul>','</div>'];
		return divHtml.join('');
	}
	
	function initLeaveButton(scheduling){
		var btnLeave = $('.leave_'+scheduling.dayOfMonth+'');
		var btnRevokeLeave = $('.revokeLeave_'+scheduling.dayOfMonth+'');
		if(leave==scheduling.applyType&&unapproved==scheduling.status){
			btnLeave.hide();
			btnRevokeLeave.show();
			return false;
		} else if(leave==scheduling.applyType&&approval==scheduling.status) {
			btnLeave.addClass('ui-disabled');
			btnRevokeLeave.hide();
			return false;
		}
		btnLeave.show();
		btnRevokeLeave.hide();
	}
	
	function initExchangeButton(scheduling){
		var btnExchangeScheduling =  $('.exchangeScheduling_'+scheduling.dayOfMonth+'');
		var btnRevokeExchangeScheduling =  $('.revokeExchangeScheduling_'+scheduling.dayOfMonth+'');
		if(change_the_shift==scheduling.applyType&&unapproved==scheduling.status){
			btnExchangeScheduling.hide();
			btnRevokeExchangeScheduling.show();
			return false;
		}
		btnExchangeScheduling.show();
		btnRevokeExchangeScheduling.hide();
	}
	
	function setLeaveButtonDisabled(scheduling) {
		var btnLeave = $('.leave_' + scheduling.dayOfMonth + '');
		var btnRevokeLeave = $('.revokeLeave_' + scheduling.dayOfMonth + '');
		var btnExchangeScheduling =  $('.exchangeScheduling_'+scheduling.dayOfMonth+'');
		var btnRevokeExchangeScheduling =  $('.revokeExchangeScheduling_'+scheduling.dayOfMonth+'');
		if (isAfterSchedulingTime(moment(scheduling.dayOfMonth, 'YYYYMMDD')) || '休' == scheduling.configureCode) {
			btnLeave.addClass('ui-disabled');
			btnRevokeLeave.addClass('ui-disabled');
			return false;
		}
		if (leave == scheduling.applyType && unapproved == scheduling.status) {
			btnExchangeScheduling.addClass('ui-disabled');
			btnRevokeExchangeScheduling.addClass('ui-disabled');
			return false;
		}
	}

	function setExchangeSchedulingButtonDisabled(scheduling) {
		var btnLeave = $('.leave_' + scheduling.dayOfMonth + '');
		var btnRevokeLeave = $('.revokeLeave_' + scheduling.dayOfMonth + '');
		var btnExchangeScheduling = $('.exchangeScheduling_' + scheduling.dayOfMonth + '');
		var btnRevokeExchangeScheduling = $('.revokeExchangeScheduling_' + scheduling.dayOfMonth + '');
		if (isAfterSchedulingTime(moment(scheduling.dayOfMonth, 'YYYYMMDD'))) {
			btnExchangeScheduling.addClass('ui-disabled');
			btnRevokeExchangeScheduling.addClass('ui-disabled');
			return false;
		}
		if (change_the_shift == scheduling.applyType && unapproved == scheduling.status) {
			btnLeave.addClass('ui-disabled');
			btnRevokeLeave.addClass('ui-disabled');
			return false;
		}
	}

	function isAfterSchedulingTime(schedulingTime) {
		var currentTime = moment().add(-1, 'days');;
		return currentTime.isAfter(schedulingTime);
	}

	function buildApply(scheduling) {
		if (null == scheduling.applyType) {
			return '';
		}
		var applyType = '请假';
		if (change_the_shift == scheduling.applyType) {
			applyType = '调班';
		}
		var applyStatus = '审批中';
		if (approval == scheduling.status) {
			applyStatus = '审批通过';
		}
		if (reject == scheduling.status) {
			applyStatus = '驳回';
		}
		if (revocation == scheduling.status) {
			applyStatus = '已撤销';
		}
		return '<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">' + applyType + '-' + applyStatus + '</span>';
	}

	function buildOperationButton(scheduling) {
		return buildLeaveButton(scheduling) + buildRevokeLeaveButton(scheduling) + buildExchangeScheduling(scheduling) + buildRevokeExchangeScheduling(scheduling);
	}
	
	function setApproverParams(dayOfMonth, departmentCode, configureCode) {
		optionType = true;
		$('#optionApprover_dayOfMonth').val(dayOfMonth);
		$('#optionApprover_departmentCode').val(departmentCode);
		$('#optionApprover_configureCode').val(configureCode);
	}

	function buildLeaveButton(scheduling) {
		return '<a href="#optionApprover" onclick="setApproverParams(\''+scheduling.dayOfMonth+'\',\''
			+scheduling.departmentCode+'\',\''+scheduling.configureCode+'\')" class="leave_' 
			+ scheduling.dayOfMonth + '" data-rel="dialog" data-mini="true" data-role="button" data-inline="true">请假</a>';
	}
	
	function buildRevokeLeaveButton(scheduling) {
		return '<a href="#" onclick="setRevokeLeaveFormValue(' + scheduling.dayOfMonth + ',\''+scheduling.employeeCode+'\')" class="revokeLeave_' + scheduling.dayOfMonth
				+ '"  data-role="button" data-inline="true" data-mini="true" >撤销请假</a>';
	}

	function buildExchangeScheduling(scheduling) {
		
	return '<a href="#selectConfigCode?oldSchedulingCode='
				+ scheduling.configureCode
				+ '&dayOfMonth='
				+ scheduling.dayOfMonth
				+ '&employeeCode='
				+ scheduling.employeeCode
				+ '&departmentCode='+
				  scheduling.departmentCode
				+ '" class="exchangeScheduling_'
				+ scheduling.dayOfMonth
				+ '" data-rel="dialog" data-mini="true" data-role="button" data-inline="true">调班</a>';
	}

	function buildRevokeExchangeScheduling(scheduling) {
		return '<a href="#" onclick="setRevokeExchangeSchedulingFormValue('
				+ scheduling.dayOfMonth
				+ ',\''+scheduling.employeeCode+'\')" class="revokeExchangeScheduling_'
				+ scheduling.dayOfMonth
				+ '" data-role="button" data-inline="true" data-mini="true" >撤回</a>';
	}

	function buildDetail(scheduling) {
		var html = [];
		if (null == scheduling.lines) {
			return '';
		}
		$.each(scheduling.lines, function(i, v) {
			if (null == v.SOURCE_CODE) {
				html.push('<li><p><b>机动配班</b></p></li>');
			} else {
				html.push('<li><p><b>始发网点:' + v.SOURCE_CODE + '--- 目的网点:'
						+ v.DESTINATION_CODE + '</b></p></li>');
			}
			html.push('<li><p><b>出车时间:' + v.START_TIME + '--- 收车时间:'
					+ v.END_TIME + '</b></p></li>');
		});
		return html.join('');
	}

	function setRevokeLeaveFormValue(dayOfMonth,employeeCode) {
		$("#revokeLeaveForm input[name=employeeCode]").val(employeeCode);
		$("#revokeLeaveWindow input[name=dayOfMonth]").val(dayOfMonth);
		
		$("#revokeLeaveWindow").popup("open");
	}

	function setRevokeExchangeSchedulingFormValue(dayOfMonth, employeeCode) {
		$("#revokeExchangeSchedulingWindow input[name=dayOfMonth]").val(
				dayOfMonth);
		$('#revokeExchangeSchedulingWindow input[name=employeeCode]').val(employeeCode);
		
		$("#revokeExchangeSchedulingWindow").popup("open");
	}

	function onSuccess(data, status) {
		if (data['success']) {
			$('#notification').text("请假成功！");
			var dayOfMonth = $("#dayOfMonth").val();
			$('h4.showTitle_' + dayOfMonth + '').find("span.AppState")
					.html('<span class="ui-li-count ui-btn-corner-all ui-btn-up-c">请假-审批中</span>');
			var btnLeave = $('.leave_' + dayOfMonth + '');
			var btnRevokeLeave = $('.revokeLeave_' + dayOfMonth + '');
			var btnExchangeScheduling = $('.exchangeScheduling_' + dayOfMonth + '');
			btnLeave.hide();
			btnRevokeLeave.removeClass("ui-disabled");
			btnRevokeLeave.show();
			btnExchangeScheduling.addClass("ui-disabled");
			$('#submitLeaveBtn').addClass("ui-disabled");
			setTimeout(function() {
				window.history.go(-2);
			}, 500);
			return;
		}
		$('#notification').text("程序异常，请稍候再试！");
		setTimeout(function() {
			window.history.go(-2);
		}, 500);
	}

	function applyExchangeScheduling() {
		if ($("#oldSchedulingCode").val() == $("#newSchedulingCode").val()) {
			$("#popupCloseRight_1 p").html("调班前代码跟调班后代码一致！");
			$("#popupCloseRight_1").popup("open");
			setTimeout(function() {
				window.history.go(-3);
			}, 1000);
			return false;
		}

		if ($("#newSchedulingCode").val() == '') {
			$("#popupCloseRight_1 p").html("新配班代码不能为空！");
			$("#popupCloseRight_1").popup("open");
			return false;
		}

		var formData = $('#exchangeSchedulingForm').serialize();
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		$.ajax({
				type : "POST",
				url : "exchangeScheduling.action",
				cache : false,
				data : formData,
				success : function(data, textStatus) {
					$.mobile.hidePageLoadingMsg();
					if(data.success == undefined){
						$("#popupCloseRight_1 p").html("提交失败！");
						$("#popupCloseRight_1").popup("open");
						return false;
					}
					$("#btnCommitExchangeScheduling").button('disable');
					$("#commitExchangeSchedulingPage div[data-role=header] a").addClass("ui-disabled");
					$("#cancleCommitBtn span span").text('返回');
					$("#popupCloseRight_1 p").html("调班申请已提交！");
					$("#popupCloseRight_1").popup("open");

					var dayOfMonth = $("#applyRecordDayOfMonth").val();
					var btnLeave = $('.leave_' + dayOfMonth + '');
					var btnExchangeScheduling = $('.exchangeScheduling_'
							+ dayOfMonth + '');
					var btnRevokeExchangeScheduling = $('.revokeExchangeScheduling_'
							+ dayOfMonth + '');

					btnLeave.addClass("ui-disabled");
					btnExchangeScheduling.hide();
					btnRevokeExchangeScheduling.show();

					$('h4.showTitle_' + dayOfMonth + '')
							.find("span.AppState")
							.html(
									'<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">调班-审批中</span>');
					setTimeout(function() {
						window.history.go(-4);
					}, 1000);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.mobile.hidePageLoadingMsg();
				}
			});
	}

	function revokeLeave() {
		var formData = $('#revokeLeaveForm').serialize();
		$.mobile.showPageLoadingMsg("b", "加载中", false);
		$.ajax({
				type : "POST",
				url : "revokeLeave.action",
				cache : false,
				data : formData,
				success : function(data, textStatus) {
					$.mobile.hidePageLoadingMsg();
					if(data.success == undefined){
						$("#popupCloseRight p").html("撤销失败！");
						$("#popupCloseRight").popup("open");
						return false;
					}
					var dayOfMonth = $(
							"#revokeLeaveWindow input[name=dayOfMonth]")
							.val();
					var btnLeave = $('.leave_' + dayOfMonth + '');
					var btnRevokeLeave = $('.revokeLeave_' + dayOfMonth
							+ '');
					var btnExchangeScheduling = $('.exchangeScheduling_'
							+ dayOfMonth + '');
					btnLeave.show();
					btnRevokeLeave.hide();
					btnExchangeScheduling.removeClass("ui-disabled");
					$('h4.showTitle_' + dayOfMonth + '')
							.find("span.AppState")
							.html(
									'<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">请假-已撤销</span>');
					$("#popupCloseRight p").html("撤销成功！");
					$("#popupCloseRight").popup("open");
					setTimeout(function() {
						history.back();
					}, 500);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.mobile.hidePageLoadingMsg();
					$("#popupCloseRight p").html("撤销失败！");
					$("#popupCloseRight").popup("open");
				}
			});
	}

	function revokeExchangeScheduling() {
		var formData = $('#revokeExchangeSchedulingForm').serialize();
		$.mobile.showPageLoadingMsg("b", "加载中", false);
	
		$.ajax({
				type : "POST",
				url : "revokeExchangeScheduling.action",
				cache : false,
				data : formData,
				success : function(data, textStatus) {
					$.mobile.hidePageLoadingMsg();
					if(data.success == undefined){
						$("#popupCloseRight p").html("撤销失败！");
						$("#popupCloseRight").popup("open");
						return false;
					}
					var dayOfMonth = $(
							"#revokeExchangeSchedulingWindow input[name=dayOfMonth]")
							.val();
					var btnLeave = $('.leave_' + dayOfMonth + '');
					var btnExchangeScheduling = $('.exchangeScheduling_'
							+ dayOfMonth + '');
					var btnRevokeExchangeScheduling = $('.revokeExchangeScheduling_'
							+ dayOfMonth + '');
					btnExchangeScheduling.show();
					btnRevokeExchangeScheduling.hide();
					
					if (data.resultMap.configureInfor.oldConfigCode != "休") {
						btnLeave.removeClass("ui-disabled");
					}
					
					$('h4.showTitle_' + dayOfMonth + '')
							.find("span.AppState")
							.html(
									'<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">调班-已撤销</span>');
					$("#popupCloseRight p").html("撤销成功！");
					$("#popupCloseRight").popup("open");
					setTimeout(function() {
						history.back();
					}, 500);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.mobile.hidePageLoadingMsg();
					$("#popupCloseRight p").html("撤销失败！");
					$("#popupCloseRight").popup("open");
				}
			});
	}
