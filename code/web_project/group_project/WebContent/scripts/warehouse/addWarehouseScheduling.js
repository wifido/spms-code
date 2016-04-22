//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var add_queryClassStore = new Ext.data.JsonStore({
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

var add_queryClassComboBox = new Ext.form.ComboBox({
	store: add_queryClassStore,
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
		this.value = null;
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

var month_day;

var searchBtn = new Ext.Button({
	width: 60,
	cls: "x-btn-normal",
	pressed: true,
	text: '查询',
	handler: function() {
		searchEmployee();
	}
});

function searchEmployee() {
	var departmentCodes = Ext.getCmp("query.branchCode").getValue();
	emp_store.setBaseParam("departmentCode", departmentCodes.split(',')[0].split('/')[0]);
	emp_store.setBaseParam("month", Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Ym'));
	emp_store.setBaseParam("EMP_CODE", Ext.getCmp('query_emp_code').getValue());
	emp_store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
}

var searchConditionPanel = new Ext.Panel({
	layout: 'column',
	frame: true,
	heigth: 50,
	tbar: [searchBtn],
	items: [{
		xtype: 'panel',
		layout: 'form',
		columnWidth: .5,
		labelAlign: 'right',
		items: [{
			xtype: 'textfield',
			id: 'departmentCode',
			name: 'departmentCode',
			readOnly: true,
			fieldLabel: "网点代码",
			anchor: '90%'
		}]
	},{
		xtype: 'panel',
		layout: 'form',
		columnWidth: .5,
		labelAlign: 'right',
		items: [{
			xtype: 'textfield',
			id: 'query_emp_code',
			name: 'empcodes',
			triggerAction: "all",
			fieldLabel: '员工工号',
			anchor: '90%'
		}]
	}]
});

var emp_sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: true,
	listeners: {
		'rowselect': function(sm, rowIndex, record) {
			var schedulingMonth = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y/m');
			var firstDayOfSchedulingMonth = moment(schedulingMonth + '/01');
			dp2.setValue(firstDayOfSchedulingMonth.toDate());
			dp2.setMinDate(getMinDate(record, firstDayOfSchedulingMonth).toDate());
			dp2.setMaxDate(getMaxDate(record, firstDayOfSchedulingMonth).toDate());
		}
	}
});

function getMaxDate(record, firstDayOfSchedulingMonth) {
	var lastDayOfSchedulingMonth = firstDayOfSchedulingMonth.endOf('month');

	var dimissionDate = record.data['DIMISSION_DT'];
	if (!Ext.isEmpty(dimissionDate) && moment(dimissionDate).isBefore(lastDayOfSchedulingMonth)) {
		return moment(dimissionDate).subtract(1, 'days');
	}

	return lastDayOfSchedulingMonth;
}

function getMinDate(record, firstDayOfSchedulingMonth) {
	var formatString = 'YYYYMMDD';
	var dateArray = [firstDayOfSchedulingMonth.format(formatString)];
	var dateOfTransferDepartment = record.data['DATE_FROM'];

	if (!Ext.isEmpty(dateOfTransferDepartment)) {
		dateArray.push(moment(dateOfTransferDepartment).format(formatString));
	}
	var entryDate = record.data['SF_DATE'];
	if (!Ext.isEmpty(entryDate)) {
		dateArray.push(moment(entryDate).format(formatString));
	}
	var dateOfTransferPost = record.data['TRANSFER_DATE'];
	if (!Ext.isEmpty(dateOfTransferPost)) {
		dateArray.push(moment(dateOfTransferPost).format(formatString));
	}
	dateArray.sort();
	dateArray.reverse();
	return moment(dateArray[0], formatString);
}

var emp_store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../warehouse/queryNotSchedulingStaff_warehouseScheduling.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'dataMap.root',
		totalProperty: 'dataMap.totalSize'
	}, new Ext.data.Record.create([{
		name: 'DEPT_CODE',
		mapping: 'DEPT_CODE',
		type: 'string'
	},{
		name: 'EMP_CODE',
		mapping: 'EMP_CODE',
		type: 'string'
	},{
		name: 'EMP_NAME',
		mapping: 'EMP_NAME',
		type: 'string'
	},{
		name: 'WORK_TYPE_NAME',
		mapping: 'WORK_TYPE_NAME',
		type: 'string'
	},{
		name: 'DEPTTYPE',
		mapping: 'DEPTTYPE',
		type: 'string'
	},{
		name: 'DIMISSION_DT',
		mapping: 'DIMISSION_DT',
		type: 'string'
	},{
		name: 'SF_DATE',
		mapping: 'SF_DATE',
		type: 'string'
	},{
		name: 'TRANSFER_DATE',
		mapping: 'TRANSFER_DATE',
		type: 'string'
	},{
		name: 'DATE_FROM',
		mapping: 'DATE_FROM',
		type: 'string'
	}]))
});

var emp_grid = new Ext.Panel({
	frame: true,
	heigth: 450,
	items: [{
		xtype: 'grid',
		id: 'emp_grid',
		layout: 'form',
		store: emp_store,
		loadMask: true,
		sm: emp_sm,
		height: 325,
		tbar: new Ext.PagingToolbar({
			pageSize: 20,
			store: emp_store,
			displayInfo: true,
			displayMsg: "当前显示 {0} - {1} 总记录数目 {2}",
			emptyMsg: "未检索到数据"
		}),
		columns: [emp_sm,{
			header: '网点代码',
			dataIndex: 'DEPT_CODE',
			width: 80,
			align: 'center'
		},{
			header: '工号',
			dataIndex: 'EMP_CODE',
			width: 80,
			align: 'center'
		},{
			header: '姓名',
			dataIndex: 'EMP_NAME',
			width: 80,
			align: 'center'
		},{
			header: '人员类型',
			dataIndex: 'WORK_TYPE_NAME',
			width: 100,
			align: 'center'
		},{
			header: '网点类型',
			hidden: true,
			dataIndex: 'DEPTTYPE',
			align: 'center'
		},{
			header: '离职日期',
			dataIndex: 'DIMISSION_DT',
			align: 'center',
			renderer: function(value, metaData, record) {
				if (Ext.isEmpty(value)) {
					return value;
				}
				metaData.attr = 'style="background-color:yellow;"';
				return moment(value).format("YYYY-MM-DD");
			}
		},{
			header: '转岗日期',
			width: 90,
			dataIndex: 'TRANSFER_DATE',
			align: 'center'
		},{
			header: '转网日期',
			width: 90,
			dataIndex: 'DATE_FROM',
			align: 'center'
		}]
	}]
});

var dp2 = new Ext.ux.DatePicker({
	id: 'add_datepicker_update',
	style: 'margin-left:17px;',
	format: 'Y-m-d',
	showToday: false,
	listeners: {
		render: function() {

		}
	}
});

var westPanel = new Ext.Panel({
	region: 'west',
	title: '未排班人员信息',
	width: 500,
	items: [searchConditionPanel,emp_grid]
});

var eastPanel = new Ext.Panel({
	region: 'center',
	title: '设置排班数据',
	layout: 'form',
	width: 250,
	items: [{
		layout: 'column',
		xtype: 'fieldset',
		title: '日期选择',
		items: [dp2]
	},{
		layout: 'column',
		xtype: 'fieldset',
		title: '班别代码',
		items: [add_queryClassComboBox]
	}]
});

var addWindow = new Ext.Window({
	id: 'formAddWin',
	width: 750,
	height: 500,
	modal: true,
	border: true,
	bodyBorder: false,
	title: '新增排班',
	layout: 'border',
	closeAction: 'hide',
	items: [westPanel,eastPanel],
	buttons: [{
		text: "保存",
		handler: function() {
			validateSchedulingDateAndSave();
		}
	},{
		text: "取消",
		handler: function() {
			addWindow.hide();
			add_queryClassComboBox.setValue("");
		}
	}]
});

function schedulingCodeExist(schedulingCodes, classStore) {
	var codeArray = schedulingCodes.split(',');
	for ( var i = 0; i < codeArray.length; i++) {
		var code = codeArray[i];
		if ('休' != code) {
			var flag = true;
			classStore.each(function(record) {
				if (code == record.get('text')) {
					flag = false;
				}
			});
			if (flag) {
				return false;
			}
		}
	}
	return true;
}

function validateSchedulingDateAndSave() {
	var schedulingDays = Ext.getCmp('add_datepicker_update').getDays();
	var records = Ext.getCmp('emp_grid').getSelectionModel().getSelections();
	if (records.length < 1) {
		Ext.Msg.alert('提示', '请能选择一条数据!');
		return;
	}

	if (records.length > 1) {
		Ext.Msg.alert('提示', '只能选择一条数据!');
		return;
	}

	if (schedulingDays.length < 1) {
		Ext.Msg.alert('提示', '请选择排班日期!');
		return;
	}

	var schedulingCodes = add_queryClassComboBox.getValue();
	if (Ext.isEmpty(schedulingCodes)) {
		Ext.Msg.alert('提示', '请选择班别!');
		return;
	}
	schedulingCodes = schedulingCodes.replace(/，/g, ',');

	if (schedulingCodes.length > 1 && schedulingCodes.indexOf('休') != -1) {
		Ext.Msg.alert("提示", "选择多个班别时不能包含 '休' !");
		return false;
	}

	if (includeRestClass(schedulingCodes)) {
		Ext.Msg.alert("提示", "选择多个班别时不能包含 '休' !");
		return false;
	}

	if (classMoreThanThree(schedulingCodes)) {
		Ext.Msg.alert("提示", "班别代码不能超过3个!");
		return false;
	}

	if (!schedulingCodeExist(schedulingCodes, add_queryClassStore)) {
		Ext.Msg.alert("提示", "班别代码不存在！");
		return false;
	}

	if (!validClassExpiredDate(schedulingDays, schedulingCodes, add_queryClassStore)) {
		return false;
	}

	var department_Code = Ext.getCmp('departmentCode').getValue().split('/')[0];

	var empCode = records[0].data['EMP_CODE'];

	var month = schedulingDays[0].split("-")[0] + "-" + schedulingDays[0].split("-")[1];

	var myMask = new Ext.LoadMask(addWindow.getEl(), {
		msg: "正在新增..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: "addScheduling_warehouseScheduling.action",
		params: {
			empCode: empCode,
			deptCode: department_Code,
			month: month,
			schedulingDays: schedulingDays.join(","),
			schedulingCodes: schedulingCodes
		},
		success: function(response) {
			myMask.hide();
			result = Ext.util.JSON.decode(response.responseText);
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert("提示", "新增成功!");
				addWindow.hide();
				querySchedule();
			} else {
				Ext.Msg.alert("提示", "新增失败!" + result.error);
			}
		},
		failure: function(response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);// 就可以取出来。如果是数组，那么很简单
			if (Ext.isEmpty(result.error)) {
				Ext.Msg.alert('提示', "新增失败！服务器错误！");
				return;
			}
			Ext.Msg.alert("提示", "新增失败!" + result.error);
		}
	});

}

// 当参数
function includeRestClass(schedulingCodes) {
	return schedulingCodes.length > 1 && schedulingCodes.indexOf('休') != -1;
}

function classMoreThanThree(schedulingCodes) {
	return schedulingCodes.split(",").length > 3;
}

var btnAddScheduling = new Ext.Button({
	text: "新增",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCodes = Ext.getCmp("query.branchCode").getValue();
		if (Ext.isEmpty(departmentCodes)) {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		if (Ext.getCmp("query.monthId").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择月排班月份！");
			return;
		}

		var flag = false;
		Ext.Ajax.request({
			url: "queryIsBeOverdue_warehouseScheduling.action",
			async: false,
			params: {
				MONTH_ID: Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m')
			},
			success: function(response) {
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.success) {
					Ext.Msg.alert("提示", "已逾期，不能新增该月排班");
					flag = true;
				}
			},
			failure: function(response) {
			}
		});
		if (flag)
			return;

		Ext.Ajax.request({
			url : '../warehouse/validateAuthority.action',
			method : 'POST',
			params : {
				dept_code : departmentCodes
			},
			success : function(res, config) {
				var data = Ext.decode(res.responseText);
				if (data.dataMap.success == false) {
					Ext.Msg.alert("提示", "暂无该网点权限！");
				} else {
					addWindow.show();
					Ext.getCmp('departmentCode').setValue(departmentCodes.split(",")[0]);
					searchEmployee();
					add_queryClassStore.setBaseParam("departmentCode", departmentCodes.split(",")[0].split("/")[0]);
					add_queryClassStore.load();

					var schedulingMonth = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m');
					var firstDayOfMonth = schedulingMonth.replace('-', '/') + '/01';
					new Date(moment(firstDayOfMonth).format("YYYY/MM/DD"));
					dp2.setValue(new Date(firstDayOfMonth));
					dp2.setMinDate(new Date(firstDayOfMonth));
					dp2.setMaxDate(new Date(firstDayOfMonth).getLastDateOfMonth());
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});