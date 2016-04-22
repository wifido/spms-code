//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var employeeCode;
var employeeName;
var monthId;
var workType;
var departmentCode;
var deptName;
var years = getCurrentYear();
var selectBtn;
var editLine = false;
var isValid = true;
var maxDayOfMonth;
var resp;

function sfAlert(messge) {
	var showMsg = Ext.Msg.show({
		title : '提示',
		msg : messge
	});
	setTimeout(function() {
		showMsg.hide();
	}, 2000);
}

// 数据格构建
var record2 = Ext.data.Record.create([ {
	name : 'DAY_OF_MONTH',
	mapping : 'DAY_OF_MONTH',
	type : 'string'
}, {
	name : 'BEGIN_TIME',
	mapping : 'BEGIN_TIME',
	type : 'string'
}, {
	name : 'END_TIME',
	mapping : 'END_TIME',
	type : 'string'
}, {
	name : 'DEPARTMENT_CODE',
	mapping : 'DEPARTMENT_CODE',
	type : 'string'
}, {
	name : 'MONTH_ID',
	mapping : 'MONTH_ID',
	type : 'string'
}, {
	name : 'EMPLOYEE_CODE',
	mapping : 'EMPLOYEE_CODE',
	type : 'string'
}, {
	name : 'CREATED_EMPLOYEE_CODE',
	mapping : 'CREATED_EMPLOYEE_CODE',
	type : 'string'
}, {
	name : 'YEARS',
	mapping : 'YEARS',
	type : 'string'
}, {
	name : 'WORK_TYPE',
	mapping : 'WORK_TYPE',
	type : 'string'
}, {
	name : 'ISEDIT',
	mapping : 'ISEDIT',
	type : 'string'
}, {
	name : 'CROSS_DAY_TYPE',
	mapping : 'CROSS_DAY_TYPE',
	type : 'string'
}, {
	name : 'ACROSS_NAME',
	mapping : 'ACROSS_NAME',
	type : 'string'
}, {
	name : 'ACROSS_ID',
	mapping : 'ACROSS_ID',
	type : 'string'
} ]);

// 构建数据存储Store
var st2 = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../dispatch/querySingleShedulingInfo.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'dataMap.root',
		totalProperty : 'totalSize'
	}, record2),
	sortInfo : {
		field : 'BEGIN_TIME',
		direction : "ASC"
	}
});

//
var sm2 = new Ext.grid.CheckboxSelectionModel({
	listeners : {
		selectionchange : function(sm) {
			if (sm.getCount()) {
				grid2.removeButton.enable();
			} else {
				grid2.removeButton.disable();
			}
		}
	}
});

var storeAcross = [ [ '否' ], [ '是' ] ];

var cm2 = new Ext.grid.ColumnModel([ sm2, {
	header : "日期",
	width : 100,
	dataIndex : 'DAY_OF_MONTH'
}, {
	header : "开始时间",
	width : 100,
	dataIndex : 'BEGIN_TIME',
	editor : new Ext.form.TextField({
		allowBlank : false,
		selectOnFocus : true,
		validator : function(value) {
			var re = /^((0\d{1}|1\d{1}|2[0-3])([0-5]\d{1})|2400)$/;
			if (!re.test(value)) {
				return '格式有误！范围：时（00-24）分（00-59）';
			}
			editLine = true;
			return true;
		}
	})
}, {
	header : "结束时间",
	width : 100,
	dataIndex : 'END_TIME',
	editor : new Ext.form.TextField({
		allowBlank : false,
		selectOnFocus : true,
		validator : function(value) {
			var re = /^((0\d{1}|1\d{1}|2[0-3])([0-5]\d{1})|2400)$/;
			if (!re.test(value) || parseInt(value) > 2400) {
				return '格式有误！范围：时（00-24）分（00-59）';
			}
			editLine = true;
			return true;
		}
	})
}, {
	header : '是否跨天',
	hidden : false,
	width : 100,
	dataIndex : 'ACROSS_NAME',
	id : 'ACROSS_NAME',
	editor : new Ext.form.ComboBox({
		name : 'checkupperson',
		forceSelection : true,
		listWidth : 200,
		store : new Ext.data.SimpleStore({
			fields : [ 'text' ],
			data : storeAcross
		}),
		valueField : 'text',
		displayField : 'text',
		typeAhead : true,
		mode : 'local',
		triggerAction : 'all',
		selectOnFocus : true,// 用户不能自己输入,只能选择列表中有的记录
		allowBlank : false
	})
}, {
	header : "WORK_TYPE",
	hidden : true,
	width : 120,
	dataIndex : 'WORK_TYPE'
}, {
	header : "DEPARTMENT_CODE",
	hidden : true,
	width : 120,
	dataIndex : 'DEPARTMENT_CODE'
}, {
	header : "MONTH_ID",
	hidden : true,
	width : 120,
	dataIndex : 'MONTH_ID'
}, {
	header : "EMPLOYEE_CODE",
	hidden : true,
	width : 120,
	dataIndex : 'EMPLOYEE_CODE'
}, {
	header : "CREATED_EMPLOYEE_CODE",
	hidden : true,
	width : 120,
	dataIndex : 'CREATED_EMPLOYEE_CODE'
}, {
	header : "YEARS",
	hidden : true,
	width : 120,
	dataIndex : 'YEARS'
}, {
	header : "ISEDIT",
	hidden : true,
	width : 120,
	dataIndex : 'ISEDIT'
} ]);

var grid2 = new Ext.grid.EditorGridPanel({
	store : st2,
	cm : cm2,
	sm : sm2,
	id : 'editor-grid',
	width : 480,
	height : 300,
	frame : true,
	buttonAlign : 'center',
	tbar : [ {
		text : '添加',
		id : 'addBtn',
		tooltip : 'Add a new row',
		icon : '../ext-3.4.0/resources/images/default/dd/drop-add.gif',
		handler : function() {
			if (editLine) {
				Ext.Msg.alert('提示', '请先保存当前操作数据后，再添加操作！');
				return false;
			}
			var Plant = grid2.getStore().recordType;
			if (grid2.getStore().getCount() == 0) {
				var p = new Plant({
					DAY_OF_MONTH : selectBtn.getId(),
					BEGIN_TIME : '0000',
					DEPARTMENT_CODE : departmentCode,
					MONTH_ID : monthId.substr(4, 6),
					EMPLOYEE_CODE : employeeCode,
					CREATED_EMPLOYEE_CODE : employeeCode,
					YEARS : monthId.substr(0, 4),
					END_TIME : '0000',
					WORK_TYPE : '',
					ISEDIT : '1',
					ACROSS_NAME : '否'
				});
			} else {
				var insertRecord = grid2.getStore().getAt(grid2.getStore().getCount() - 1);
				var p = new Plant({
					DAY_OF_MONTH : insertRecord.data['DAY_OF_MONTH'],
					BEGIN_TIME : '0000',
					DEPARTMENT_CODE : insertRecord.data['DEPARTMENT_CODE'],
					MONTH_ID : insertRecord.data['MONTH_ID'],
					EMPLOYEE_CODE : insertRecord.data['EMPLOYEE_CODE'],
					CREATED_EMPLOYEE_CODE : insertRecord.data['CREATED_EMPLOYEE_CODE'],
					YEARS : insertRecord.data['YEARS'],
					END_TIME : '0000',
					WORK_TYPE : insertRecord.data['WORK_TYPE'],
					ISEDIT : '1',
					ACROSS_NAME : '否'
				});
			}
			grid2.stopEditing();
			st2.insert(0, p);
			grid2.startEditing(0, 2);
			editLine = true;
		}
	}, '-', {
		text : '删除',
		id : 'removeBtn',
		tooltip : 'Remove the selected item',
		icon : '../ext-3.4.0/resources/images/default/dd/delete.gif',
		ref : '../removeButton',
		disabled : true,
		handler : function() {
			var selectedRows = grid2.getSelectionModel().getSelections();
			Ext.each(selectedRows, function(item) {
				if (item.data["ISEDIT"] == "1") {
					editLine = false;
				}
				st2.remove(item);
			});
		}
	}, '-', {
		text : '保存',
		id : 'saveButton',
		tooltip : 'save the selected item',
		icon : '../ext-3.4.0/resources/images/default/dd/accept.png',
		handler : function() {
			isValid = true;
			var json = [];
			var rowCount = st2.getCount();
			var workTimetotal = 0;
			grid2.getStore().each(function(verifyLine, i) {
				// 是否存在编辑的数据标识 1 为存在编辑行 0反之
				var editLine = verifyLine.data["ISEDIT"];
				var currentBeginTime = verifyLine.data["BEGIN_TIME"];
				var currentEndTime = verifyLine.data["END_TIME"];
				var currentAcrossDayLogo = verifyLine.data["ACROSS_NAME"];

				if ((i + 1) < rowCount) {
					grid2.getStore().each(function(compareData, j) {
						var byCompareBeginTime = compareData.data["BEGIN_TIME"];
						var byCompareEndTime = compareData.data["END_TIME"];
						var byCompareAcrossDayLogo = compareData.data["ACROSS_NAME"];
						var notCurrentAcrossDayLogo = currentAcrossDayLogo != '是';
						var notCompareAcrossDayLogo = byCompareAcrossDayLogo != '是';

						if (byCompareBeginTime == byCompareEndTime) {
							setTimeErrorMsg();
							return;
						}
						if (i != j) {
							// 如果都不是跨天的数据
							if (notCompareAcrossDayLogo && notCurrentAcrossDayLogo) {
								var validationDataLegal = byCompareEndTime < currentBeginTime || byCompareBeginTime > currentEndTime;
								if (!validationDataLegal) {
									setErrorMsg();
									return;
								}
							}

							// 如果当前编辑的数据是跨天,并且被对比的数据也是垮天的
							if (!notCompareAcrossDayLogo && !notCurrentAcrossDayLogo) {
								setErrorMsg();
								return;
							}

							// 如果当前编辑的是垮天,被对比的数据不是跨天的
							if (notCompareAcrossDayLogo && !notCurrentAcrossDayLogo) {
								var isCurrentCorrectTime = byCompareEndTime < currentBeginTime;
								if (!isCurrentCorrectTime) {
									setErrorMsg();
									return;
								}
							}

							if (!notCompareAcrossDayLogo && notCurrentAcrossDayLogo) {
								var isNotCurrentCorrectTime = byCompareBeginTime > currentEndTime;
								if (!isNotCurrentCorrectTime) {
									setErrorMsg();
									return;
								}
							}
						}
					});
				}

				if (currentBeginTime == currentEndTime) {
					setTimeErrorMsg();
					return;
				}
				var re = /^((0\d{1}|1\d{1}|2[0-3])([0-5]\d{1})|2400)$/;
				if (!re.test(currentBeginTime)) {
					Ext.Msg.alert('提示', '格式有误！范围：时（00-24）分（00-59）');
					grid2.startEditing(i, 2);
					isValid = false;
					return;
				}
				if (!re.test(currentEndTime)) {
					Ext.Msg.alert('提示', '格式有误！范围：时（00-24）分（00-59）');
					grid2.startEditing(i, 3);
					isValid = false;
					return;
				}
				json.push(verifyLine.data);
			});

			// 验证失败 返回
			if (!isValid) {
				return;
			}

			// // 当非全日制时
			if (isValid) {
				Ext.Ajax.request({
					url : '../dispatch/saveScheduling.action',
					params : {
						data : Ext.util.JSON.encode(json),
						MONTH_ID : monthId,
						EMPLOYEE_CODE : employeeCode,
						DAY_OF_MONTH : monthId + selectBtn.getId(),
						DEPARTMENT_CODE : departmentCode
					},
					method : 'POST',
					success : function(response, options) {
						var btnText = buildButtonText(json[0], false);

						if (json.length > 1) {
							buildButtonText(json[0], true);
						}
						selectBtn.setText(btnText);
						Ext.Msg.alert('提示', '修改成功！');

						// 保存成功时，修改编辑状态为 可编辑。
						editLine = false;

						// 刷新数据，防止下次验证时。数据未刷新
						refreshDataForValidation();

						// 保存成功，刷新当前所有排班数据
						refreshCurrentSchedulingData();
					},
					failure : function(response) {
						Ext.Msg.alert('提示', '数据更新失败，请稍后再试！');
					}
				});
			}
		}
	} ]
});

// 升序
function ascendingOrder(arrayList) {
	arrayList.sort(function(a, b) {
		return a > b ? 1 : -1
	});
}

function refreshCurrentSchedulingData() {
	st2.load({
		params : {
			MONTH_ID : monthId,
			EMPLOYEE_CODE : employeeCode,
			DAY_OF_MONTH : monthId + selectBtn.getId(),
			DEPARTMENT_CODE : departmentCode
		},
		callback : function(records, options, success) {
		},
		scope : store
	});
}

function refreshDataForValidation() {
	Ext.Ajax.request({
		url : '../dispatch/querySingleShedulingInfo.action',
		params : {
			MONTH_ID : monthId,
			EMPLOYEE_CODE : employeeCode,
			DEPARTMENT_CODE : departmentCode
		},
		success : function(response) {
			resp = Ext.util.JSON.decode(response.responseText);
		}
	});
}

function setTimeErrorMsg() {
	Ext.Msg.alert('提示', '排班时间不能大于等于24小时！');
	isValid = false;
}

function setErrorMsg() {
	Ext.Msg.alert('提示', '时间段存在交叉数据，请先确认后保存！');
	isValid = false;
}

var baiscInfoPane = new Ext.Panel({
	id : 'baiscInfoPane',
	frame : true,
	region : 'north',
	title : '基本信息',
	height : 100,
	html : ' '
});

var schedulingListPanel = new Ext.Panel({
	id : 'schedulingListPanel',
	frame : true,
	title : '排班明细',
	region : 'west',
	autoScroll : true,
	height : 400,
	width : 190
});

var editPanel = new Ext.Panel({
	frame : true,
	region : 'center',
	height : 400,
	width : 500,
	items : [ grid2 ]
});

var _wd = new Ext.Window({
	title : '修改排班信息',
	width : 700,
	height : 500,
	closeAction : 'hide',
	layout : 'border',
	items : [ baiscInfoPane, schedulingListPanel, editPanel ],
	buttons : [ {
		text : "关闭",
		handler : function() {
			_wd.hide();
		}
	} ]
});

var btnEditHoursWork = new Ext.Button({
	text : "修改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		editLine = false;
		if (Ext.getCmp("query.AreaCode").getValue() == "") {
			var records = grid.getSelectionModel().getSelections();
			if (records.length > 1) {
				Ext.Msg.alert("提示", "只能选择一条数据!");
				return;
			}
			if (records.length < 1) {
				Ext.Msg.alert("提示", "请能选择一条数据!");
				return;
			}
		}else{
			Ext.Ajax.request({
				async: false,
				url: '../dispatch/dispatch_queryPermissions.action',
				params: {'DEPARTMENT_CODE':getDepartmentCodes()},
				success: function(res, config) {
						var returnObj = Ext.decode(res.responseText);
						if (returnObj.dataMap.totalSize == 0){
							Ext.Msg.alert("提示", "当前用户没有该网点的权限！");	
							return;
						}else{
							var records = grid.getSelectionModel().getSelections();
							if (records.length > 1) {
								Ext.Msg.alert("提示", "只能选择一条数据!");
								return;
							}
							if (records.length < 1) {
								Ext.Msg.alert("提示", "请能选择一条数据!");
								return;
							}
							var record = grid.getSelectionModel().getSelected();
							var flag = false;
							Ext.Ajax.request({
								url: "../dispatch/dispatch_queryIsBeOverdue.action",
								async: false,
								params: {
									MONTH_ID : record.data['monthId']
								},
								success: function(response) {
									var result = Ext.util.JSON.decode(response.responseText);
									if (result.success) {
										Ext.Msg.alert("提示", "已逾期，不能修改该月排班");
										flag = true;
									}
								},
								failure: function(response) {
								}
							});
							if(flag)
								return;

							_wd.show();
							workType = record.data['workType'];

							grid2.getTopToolbar().items.itemAt(0).show();
							grid2.getTopToolbar().items.itemAt(1).show();
							grid2.getTopToolbar().items.itemAt(2).show();
							grid2.getTopToolbar().items.itemAt(3).show();
							// if("6"==workType){
							// grid2.getTopToolbar().items.itemAt(0).hide();
							// grid2.getTopToolbar().items.itemAt(1).hide();
							// grid2.getTopToolbar().items.itemAt(2).hide();
							// grid2.getTopToolbar().items.itemAt(3).hide();
							// }
							employeeCode = record.data['employeeCode'];
							employeeName = record.data['employeeName'];
							monthId = record.data['monthId'];
							departmentCode = record.data['departmentCode'];
							deptName = record.data['deptName'];

							baiscInfoPane.body.update('<table align="center" style="font-size:12px;line-height:2">' + '<tr><td width="40%">员工工号:' + employeeCode + ' &nbsp; '
									+ employeeName + '</td><td width="40%">当前所属排班年月：' + monthId + '月</td></tr>' + '<tr><td width="40%">所属网点: ' + departmentCode + '/' + deptName
									+ '</td><td width="40%"></td></tr>' + '</table>');

							schedulingListPanel.setTitle(monthId + '月份排班明细');

							bindSchedulingListPanelData();

							st2.load({
								params : {
									MONTH_ID : monthId,
									EMPLOYEE_CODE : employeeCode,
									DAY_OF_MONTH : monthId + '01',
									DEPARTMENT_CODE : departmentCode
								},
								callback : function(records, options, success) {
									// 初始化默认选中第一天
									selectBtn = Ext.getCmp("01");
									selectBtn.addClass('x-btn-over');

									// 当非全日制时，隐藏跨天列
									if (workType != "A") {
										grid2.getColumnModel().setHidden(4, true);
									} else {
										grid2.getColumnModel().setHidden(4, false);
									}
								},
								scope : store
							});
						}
				}
			});	
		}	
	}

});

// 组装button
function assembleButton(data) {
	maxDayOfMonth = getCountDays(parseInt(monthId.substring(0, monthId.length-2)),parseInt(monthId.substring(monthId.length - 2, monthId.length)));
	for ( var i = 1; i <= maxDayOfMonth; i++) {
		var flag = false;
		var btnText;
		var day = i;
		if (i < 10) {
			day = '0' + i;
		}
		var count = 0;
		Ext.each(data.dataMap.root, function(v) {
			if (v.DAY_OF_MONTH == day) {
				flag = true;
				count = count + 1;
				if (count <= 1) {
					btnText = buildButtonText(v, false);
				} else {
					btnText = buildButtonText(v, true);
					createBtn(day, btnText);
					return false;
				}
				createBtn(day, btnText);
			}
		});
		if (!flag) {
			btnText = '<span style="font-size:15px;"><b>' + day + '号&nbsp;&nbsp;(no Data)</b><span>';
			createBtn(day, btnText);
		}
	}
	schedulingListPanel.doLayout();
}

// 创建button对象
function createBtn(btnId, btnText) {
	var bt = new Ext.Button({
		id : btnId,
		text : btnText,
		width : 160,
		height : 40,
		handleMouseEvents : false,
		style : {
			marginBottom : '3px'
		},
		handler : function() {
			editLine = false;
			if (selectBtn != null) {
				selectBtn.removeClass('x-btn-over');
			}
			this.addClass('x-btn-over');
			selectBtn = this;

			var buttonId = this.getId();
			st2.load({
				params : {
					MONTH_ID : monthId,
					EMPLOYEE_CODE : employeeCode,
					DAY_OF_MONTH : monthId + buttonId,
					DEPARTMENT_CODE : departmentCode
				},
				callback : function(records, options, success) {
					// Ext.Msg.alert('info', '加载完毕');
				},
				scope : store
			});
		}
	});
	schedulingListPanel.add(bt);
}

function buildButtonText(item, isMany) {
	if (item == null) {
		btnText = '<span style="font-size:15px;"><b>' + selectBtn.getId() + '号&nbsp;&nbsp;(休)</b><span>';
		return btnText;
	}
	if (null == item.BEGIN_TIME || null == item.END_TIME) {
		btnText = '<span style="font-size:15px;"><b>' + item.DAY_OF_MONTH + '号&nbsp;&nbsp;(休)</b><span>';
		return btnText;
	}
	var btnText = '<span style="font-size:15px;"><b>' + item.DAY_OF_MONTH + '号 (' + item.BEGIN_TIME + ':' + item.END_TIME + ')</b><span>';
	if (isMany || grid2.getStore().getCount() > 1) {
		btnText = '<span style="font-size:15px;"><b>' + item.DAY_OF_MONTH + '号 (' + item.BEGIN_TIME + ':' + item.END_TIME + ')....</b><span>';
	}
	return btnText;
}

// 排班明细绑定数据
function bindSchedulingListPanelData() {
	schedulingListPanel.removeAll();
	Ext.Ajax.request({
		url : '../dispatch/querySingleShedulingInfo.action',
		params : {
			MONTH_ID : monthId,
			EMPLOYEE_CODE : employeeCode,
			DEPARTMENT_CODE : departmentCode
		},
		success : function(response) {
			resp = Ext.util.JSON.decode(response.responseText);
			assembleButton(resp);
		}
	});
}

function getCurrentYear() {
	var date = new Date();
	var year = date.getFullYear(); // 2014
	// var month = date.getMonth()+1; //9
	// var day = date.getDate(); //27
	return year;
}

function getCountDays(yars,month) {
	var d = new Date(yars,month,0);
	/* 返回当月的天数 */
	return d.getDate();
}
