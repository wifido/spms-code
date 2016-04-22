//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var schedulingEmpCode;
var schedulingDeptCode;
var schedulingMonth;
var IS_HAVE_COMMISSION = '是否参与计提计算';
var modifyHistorySchedulingPermission = '<app:isPermission code = "/schedulingMgt/modify_warehouse_historty_scheduling.action">a</app:isPermission>';

var queryClassStore = new Ext.data.JsonStore({
	root: 'dataMap.root',
	fields: [{
		name: 'text',
		mapping: 'SCHEDULE_CODE'
	},{
		name: 'disAbleTime',
		mapping: 'DISABLE_DT'
	},{
		name: 'enable_dt',
		mapping: 'ENABLE_DT'
	}],
	url: 'queryClassByDeptId_warehouseClass.action'
}, []);

var queryClassComboBox = new Ext.form.ComboBox({
	store: queryClassStore,
	displayField: 'text',
	valueField: 'text',
	typeAhead: false,
	width: 150,
	fieldLabel: '班别代码',
	tpl: '<tpl for="."><div class="x-combo-list-item"><span><input type="checkbox"' + '{[values.check?"checked":""]} value="{[values.text]}" /></span><span >{text}</span></div></tpl>',
	triggerAction: 'all',
	emptyText: '请选择',
	selectOnFocus: true,
	onSelect: function(record, index) {
		if (this.fireEvent('beforeselect', this, record, index) != false) {
			record.set('check', !record.get('check'));
			var str = [];// 页面显示的值
			var strvalue = [];// 传入后台的值
			this.store.each(function(rc) {
				if (rc.get('check')) {
					str.push(rc.get('text'));
					strvalue.push(rc.get('text'));
				}
			});
			this.setValue(str.join());
			this.value = strvalue.join();// 赋值
			this.fireEvent('select', this, record, index);
		}
	},
	listeners: {
		expand: function(value) {// 监听下拉事件
			this.store.each(function(rc) {
				if (value.value == rc.get('text')) {
					rc.set('check', true);// 选中
				}
			});
		}
	}
});

var updateSchedulingFormPanel = new Ext.FormPanel({
	labelAlign: 'left',
	buttonAlign: 'right',
	frame: true,
	layout: 'column',
	monitorValid: true,
	items: [{
		layout: 'form',
		style: {
			paddingLeft: '5px'
		},
		defaults: {
			width: 100
		},
		labelWidth: 60,
		columnWidth: .35,
		items: [{
			id: '_DEPT_CODE',
			xtype: "label",
			fieldLabel: "网点代码",
			text: ''

		},{
			id: '_EMP_NAME',
			xtype: "label",
			fieldLabel: "姓名",
			text: ''
		},{
			id: '_WORK_TYPE',
			xtype: "label",
			fieldLabel: "人员类型",
			text: ''
		}]
	},{
		layout: 'form',
		style: {
			paddingLeft: '10px'
		},
		defaults: {
			width: 100
		},
		labelWidth: 80,
		columnWidth: .35,
		items: [{
			id: '_DYNAMIC_DEPT_CODE',
			xtype: "label",
			fieldLabel: "机动网点",
			text: ''
		},{
			id: '_EMP_CODE',
			readOnly: true,
			xtype: "label",
			fieldLabel: "工号",
			text: ''
		},{
			xtype: "label",
			id: '_IS_HAVE_COMMISSION',
			labelStyle: 'width:100px; margin-Top: 0',
			fieldLabel: IS_HAVE_COMMISSION,
			text: ''
		}]
	},{
		layout: 'form',
		style: {
			paddingLeft: '5px'
		},
		defaults: {
			width: 100
		},
		labelWidth: 60,
		columnWidth: .3,
		items: [{
			id: '_DIMISSION_DT',
			xtype: "label",
			fieldLabel: "离职日期",
			style: "background-color:yellow;",
			text: ''
		}]
	}]
});

var editForm = new Ext.FormPanel({
	id: 'editForm',
	labelAlign: 'left',
	buttonAlign: 'right',
	frame: true,
	layout: 'column',
	monitorValid: true,
	items: [{
		layout: 'form',
		labelWidth: 100,
		defaults: {
			width: 120
		},
		items: [queryClassComboBox]
	}]
});

var baiscInfoPane = new Ext.Panel({
	frame: true,
	region: 'north',
	title: '员工基本信息',
	height: 130,
	items: [updateSchedulingFormPanel]
});

var dp = new Ext.ux.DatePicker({
	id: 'datepicker_update',
	style: 'margin-left:17px;',
	format: 'Y-m-d',
	// minDate :new Date('2014/12/01'),
	// maxDate :new Date('2014/12/01').getLastDateOfMonth(),
	// maxDate : todayDt.add(Date.MONTH,1).getLastDateOfMonth(),
	// minDate : todayDt,
	showToday: false,
	listeners: {
		render: function() {

		}
	}
});

var schedulingListPanel = new Ext.Panel({
	id: 'schedulingListPanel',
	frame: true,
	title: '日期选择',
	region: 'west',
	autoScroll: true,
	height: 350,
	width: 250,
	items: [dp]
});

var editPanel = new Ext.Panel({
	frame: true,
	region: 'center',
	height: 350,
	width: 450,
	items: [editForm]
});

var _wd = new Ext.Window({
	title: '修改仓管排班信息',
	width: 700,
	height: 450,
	closeAction: 'hide',
	layout: 'border',
	items: [baiscInfoPane,schedulingListPanel,editPanel],
	buttons: [{
		text: "修改",
		handler: function() {
			var schedulingDays = Ext.getCmp('datepicker_update').getDays();
			if (schedulingDays.length < 1) {
				Ext.Msg.alert("提示", "请选择排班日期!");
				return false;
			}
			var schedulingCodes = queryClassComboBox.getValue();
			if (Ext.isEmpty(schedulingCodes)) {
				Ext.Msg.alert("提示", "请选择排班别代码!");
				return false;
			}
			if (schedulingCodes.length > 1 && schedulingCodes.indexOf('休') != -1) {
				Ext.Msg.alert("提示", "选择多个班别时不能包含 '休' !");
				return false;
			}
			if (schedulingCodes.split(",").length > 3) {
				Ext.Msg.alert("提示", "排班班别不能超过3个!");
				return false;
			}
			if (!schedulingCodeExist(schedulingCodes, queryClassStore)) {
				Ext.Msg.alert("提示", "班别代码不存在！");
				return false;
			}
			if (!validClassExpiredDate(schedulingDays, schedulingCodes, queryClassStore)) {
				return false;
			}
			var departmentCode = Ext.getCmp("query.branchCode").getValue();
			var dynamicDeptIds = [ departmentCode ];
			Ext.Ajax.request({
				url : '../warehouse/validateAuthority.action',
				method : 'POST',
				params : {
					dept_code : dynamicDeptIds
				},
				success : function(res, config) {
					var data = Ext.decode(res.responseText);
					if (data.dataMap.success == false) {
						Ext.Msg.alert("提示", "暂无该网点权限！");
					}else {
						savaSchedulingData(schedulingCodes, schedulingDays.join(','));
					}
				},
				failure : function() {
					Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
				}
			});
		}
	},{
		text: "取消",
		handler: function() {
			_wd.hide();
			queryClassComboBox.setValue("");
		}
	}]
});

function validClassExpiredDate(schedulingDays, schedulingCodes, store) {
	var lastOfSelectedDay = moment(schedulingDays[schedulingDays.length - 1], 'YYYY-MM-DD');
	var firstOfSelectedDay = moment(schedulingDays[0], 'YYYY-MM-DD');
	var expiredDate = moment().add(10000, 'days');
	var classCode = '';
	var validFaild = false;
	var enableDate = moment().add(10000, 'days');
	var enableClassCode = '';
	store.each(function(rc) {
		var schedulingCode = rc.get('text');
		var disAbleTime = rc.get('disAbleTime');
		var enableTime = rc.get('enable_dt');
		Ext.each(schedulingCodes.split(','), function(v, i) {
			if (v == schedulingCode && !Ext.isEmpty(disAbleTime)) {
				if (moment(disAbleTime).isBefore(expiredDate)) {
					expiredDate = moment(disAbleTime);
					classCode = schedulingCode;
				}
			}

			if (v == schedulingCode && !Ext.isEmpty(enableTime)) {
				if (firstOfSelectedDay.isBefore(moment(enableTime))) {
					enableDate = moment(enableTime);
					enableClassCode = classCode;
					validFaild = true;
					return false;
				}
			}
		});

		if (validFaild)
			return;
	});
	if (lastOfSelectedDay.add(1, 'days').isAfter(expiredDate)) {
		Ext.Msg.alert("提示", '排班日期冲突！班别代码 ' + classCode + ' 将在 ' + expiredDate.format('YYYY-MM-DD') + ' 失效！');
		return false;
	}
	if (validFaild) {
		Ext.Msg.alert("提示", '排班日期冲突！班别代码 ' + enableClassCode + ' 将在 ' + enableDate.format('YYYY-MM-DD') + ' 生效！');
		return false;
	}

	return true;
}

function savaSchedulingData(schedulingCodes, schedulingDays) {
	var myMask = new Ext.LoadMask(_wd.getEl(), {
		msg: "正在修改..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: "update_warehouseScheduling.action",
		params: {
			empCode: schedulingEmpCode,
			deptCode: schedulingDeptCode,
			month: schedulingMonth,
			schedulingDays: schedulingDays,
			schedulingCodes: schedulingCodes
		},

		success: function(response) {
			myMask.hide();
			result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "修改成功!");
				_wd.hide();
				querySchedule();
				queryClassComboBox.setValue("");
			} else {
				Ext.Msg.alert("提示", "修改失败!" + result.error);
			}
		},
		failure: function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);// 就可以取出来。如果是数组，那么很简单
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert('提示', "新增失败！服务器错误！");
				return;
			}
			Ext.Msg.alert("提示", "修改失败!" + result.error);
		}
	});
}

function validSchedulingData(schedulingCodes, schedulingDays) {
	Ext.Ajax.request({
		url: "valid_warehouseScheduling.action",
		params: {
			empCode: schedulingEmpCode,
			deptCode: schedulingDeptCode,
			schedulingDays: schedulingDays
		},
		success: function(response) {
			resp = Ext.util.JSON.decode(response.responseText);
			if (!Ext.isEmpty(resp.dataMap.msg)) {
				Ext.Msg.alert("提示", resp.dataMap.msg + "号已在其他部门排班!");
				return false;
			}
			savaSchedulingData(schedulingCodes, schedulingDays);
		}
	});
}

var btnUpdateScheduling = new Ext.Button({
	text: "修改",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length > 1) {
			Ext.Msg.alert("提示", "只能选择一条数据!");
			return false;
		}
		if (records.length < 1) {
			Ext.Msg.alert("提示", "请能选择一条数据!");
			return false;
		}
		var record = grid.getSelectionModel().getSelected();

		var flag = false;
		Ext.Ajax.request({
			url: "queryIsBeOverdue_warehouseScheduling.action",
			async: false,
			params: {
				MONTH_ID: record.data['monthId'].substring(0, 4) + "-" + record.data['monthId'].substring(4, 6)
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

		if (flag)
			return;
		var departmentCode = Ext.getCmp("query.branchCode").getValue();
		var dynamicDeptIds = [ departmentCode ];
		Ext.Ajax.request({
			url : '../warehouse/validateAuthority.action',
			method : 'POST',
			params : {
				dept_code : dynamicDeptIds
			},
			success : function(res, config) {
				var data = Ext.decode(res.responseText);
				if (data.dataMap.success == false) {
					Ext.Msg.alert("提示", "暂无该网点权限！");
				} else {
					schedulingEmpCode = record.data['employeeCode'];
					schedulingMonth = record.data['monthId'];
					_wd.show();
					queryClassComboBox.clearValue();
					requestEmployeeInfo();
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});

function requestEmployeeInfo() {
	Ext.Ajax.request({
		url: "queryEmployeeInfo_warehouseScheduling.action",
		async: true,
		params: {
			empCode: schedulingEmpCode
		},
		success: function(response) {
			resp = Ext.util.JSON.decode(response.responseText);
			Ext.getCmp("_EMP_CODE").setText(schedulingEmpCode);
			Ext.getCmp("_EMP_NAME").setText(resp.dataMap.EMP_NAME);
			Ext.getCmp("_WORK_TYPE").setText(resp.dataMap.WORK_TYPE);
			Ext.getCmp("_DEPT_CODE").setText(resp.dataMap.DEPT_CODE);
			if (Ext.isEmpty(resp.dataMap.DYNAMIC_DEPTS)) {
				Ext.getCmp("_DYNAMIC_DEPT_CODE").setText('');
			} else {
				Ext.getCmp("_DYNAMIC_DEPT_CODE").setText(resp.dataMap.DYNAMIC_DEPTS);
			}
			if ('1' == resp.dataMap.IS_HAVE_COMMISSION) {
				Ext.getCmp("_IS_HAVE_COMMISSION").setText('是');
			} else {
				Ext.getCmp("_IS_HAVE_COMMISSION").setText('否');
			}
			if (!Ext.isEmpty(resp.dataMap.DIMISSION_DT)) {
				Ext.getCmp("_DIMISSION_DT").setText(moment(resp.dataMap.DIMISSION_DT).format('YYYY-MM-DD'));
			} else {
				Ext.getCmp("_DIMISSION_DT").setText('');
			}
			schedulingDeptCode = Ext.getCmp('query.branchCode').getValue().split('/')[0];
			if (resp.dataMap.DEPT_CODE != schedulingDeptCode && (Ext.isEmpty(resp.dataMap.DYNAMIC_DEPTS) || resp.dataMap.DYNAMIC_DEPTS.indexOf(schedulingDeptCode) < 0)) {
				schedulingDeptCode = resp.dataMap.DEPT_CODE;
			}
			queryClassStore.setBaseParam("departmentCode", schedulingDeptCode);
			queryClassStore.load();

			setDatePickerValue(resp.dataMap);
		}
	});
}

function setDatePickerValue(record) {
	var formatStr='YYYY/MM/DD';
	var firstDayOfSchedulingMonth = moment(schedulingMonth, 'YYYYMM');
	new Date(firstDayOfSchedulingMonth.format(formatStr));
	dp.setValue(firstDayOfSchedulingMonth.toDate());
	var today = new Date(moment().add(1, 'days').format(formatStr));
	var minDate = getMinDay(record);
	 if (!minDate.isAfter(firstDayOfSchedulingMonth)) {
		 minDate = firstDayOfSchedulingMonth;
	 }
	
	//如果有修改权限，或者 修改当前月之后的数据
	if(!Ext.isEmpty(modifyHistorySchedulingPermission)||moment(firstDayOfSchedulingMonth).isAfter(moment())){
		dp.setMinDate(new Date(minDate.format(formatStr)));
	}else{
		dp.setMinDate(new Date(moment().add(0, 'days').format(formatStr)));
	}
	var maxDate = getMaxDay(record, firstDayOfSchedulingMonth);
	dp.setMaxDate(new Date(maxDate.format(formatStr)));
}

function formatMoment(mom) {
	var formatString = 'YYYY/MM/DD';
	var dateString = mom.format(formatString);
	return moment(dateString, formatString);
}

function getMaxDay(record, firstDayOfSchedulingMonth) {
	var lastDayOfSchedulingMonth = firstDayOfSchedulingMonth.endOf('month');
	var dimissionDate = record.DIMISSION_DT;
	if (!Ext.isEmpty(dimissionDate) && moment(dimissionDate).isBefore(lastDayOfSchedulingMonth)) {
		return moment(dimissionDate).subtract(1, 'days');
	}
	return lastDayOfSchedulingMonth;
}

function getMinDay(record) {
	var formatString = 'YYYYMMDD';
	var dateArray = [];
	var entryDate = record.SF_DATE;
	if (!Ext.isEmpty(entryDate)) {
		dateArray.push(moment(entryDate).format(formatString));
	}
	var dateOfTransferPost = record.TRANSFER_DATE;
	if (!Ext.isEmpty(dateOfTransferPost)) {
		dateArray.push(moment(dateOfTransferPost).format(formatString));
	}
	var dateOfTransferDepartment = record.DATE_FROM;
	if (!Ext.isEmpty(dateOfTransferDepartment)) {
		dateArray.push(moment(dateOfTransferDepartment).format(formatString));
	}
	dateArray.sort();
	dateArray.reverse();
	return moment(dateArray[0], formatString);
}
