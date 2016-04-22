//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var employeePanel = new Ext.Panel({
	region : 'north',
	height : 160,
	layout : 'column',
	frame : true,
	items : [{
		layout : 'form',
		labelAlign : 'right',
		columnWidth : .3,
		items : [{
			xtype : 'label',
			fieldLabel : '地区代码',
			id : 'Area_Code',
			style : 'position:relative;top:2px;'
		},{
			xtype : 'label',
			fieldLabel : '员工名称',
			id : 'Employee_Name',
			style : 'position: relative; top: 2px;'
		},{
			xtype : 'label',
			fieldLabel : '创建人',
			id : 'Create_Employee',
			style : 'position: relative; top: 2px'
		},{
			xtype : 'hidden',
			id : 'hidden_Area_Code'
		}]
	},{
		layout : 'form',
		labelAlign : 'right',
		columnWidth : .3,
		items : [{
			xtype : 'label',
			fieldLabel : '网点代码',
			id : 'Department_Code',
			style : 'position:relative;top:2px;'
		},{
			xtype : 'label',
			fieldLabel : '员工工号',
			id : 'Employee_Code',
			style : 'position:relative;top:2px;'
		},{
			xtype : 'label',
			fieldLabel : '创建时间',
			id : 'Create_Time',
			style : 'position: relative; top: 2px'
		}]
	},{
		layout : 'form',
		labelAlign : 'right',
		columnWidth : .3,
		items : [{
			xtype : 'label',
			fieldLabel : '排班周数',
			id : 'Year_Week',
			style : 'position:relative;top:2px;'
		},{
			xtype : 'label',
			fieldLabel : '计划休息总天数',
			id : 'Total_Rest_Days',
			style : 'position:relative;top:2px'
		},{
			xtype : 'label',
			fieldLabel : '修改人',
			id : 'Modify_Employee',
			style : 'position: relative; top: 2px'
		}]
	}]
});

var configure_Record = new Ext.data.Record.create([{
	name : 'ID',
	mapping : 'ID',
	type : 'string'
},{
	name : 'CODE',
	mapping : 'CODE',
	type : 'string'
},{
	name : 'START_TIME',
	mapping : 'START_TIME',
	type : 'string'
},{
	name : 'END_TIME',
	mapping : 'END_TIME',
	type : 'string'
},{
	name : 'SOURCE_CODE',
	mapping : 'SOURCE_CODE',
	type : 'string'
},{
	name : 'SOURCE_NAME',
	mapping : 'SOURCE_NAME',
	type : 'string'
},{
	name : 'DESTINATION_CODE',
	mapping : 'DESTINATION_CODE',
	type : 'string'
},{
	name : 'DESTINATION_NAME',
	mapping : 'DESTINATION_NAME',
	type : 'string'
},{
	name : 'DEPT_CODE',
	mapping : 'DEPT_CODE',
	type : 'string'
},{
	name : 'MONTH',
	mapping : 'MONTH',
	type : 'string'
}]);

var configure_Store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../driver/query_lineConfigure.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, configure_Record),
	listeners : {
		load : function(obj, record, options) {
			if (options.params.start == 0) {
				var restRecord = new record_lineConfigure({
					"CODE" : "休"
				});
				configure_Store.add(restRecord);
			}
		}
	}
});

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true
});

var columnModel = new Ext.grid.ColumnModel({
	columns : [sm,{
		header : '配班代码',
		dataIndex : 'CODE',
		width : 120,
		sortable : true,
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			if ('休' == value) {
				return value;
			}
			if (record.get("SOURCE_CODE"))
				return record.get("DEPT_CODE") + "-" + value;
			return "机动"+record.get("DEPT_CODE") + "-" + value;
		}
	},{
		header : '月份',
		dataIndex : 'MONTH',
		width : 70,
		sortable : true
	},{
		header : '出车时间',
		dataIndex : 'START_TIME',
		width : 70,
		sortable : true
	},{
		header : '收车时间',
		dataIndex : 'END_TIME',
		width : 70,
		sortable : true
	},{
		header : '始发网点',
		dataIndex : 'SOURCE_CODE',
		width : 80,
		sortable : true
	},{
		header : '目的网点',
		dataIndex : 'DESTINATION_CODE',
		width : 80,
		sortable : true
	}]
});

var configurePageBar = new Ext.PagingToolbar({
	store : configure_Store,
	displayInfo : true,
	displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize : 20,
	emptyMsg : '未检索到数据'
});

var configureGridPanel = new Ext.grid.GridPanel({
	sm : sm,
	cm : columnModel,
	store : configure_Store,
	autoScroll : true,
	loadMark : true,
	height : 250,
	tbar : configurePageBar,
	width : 530
});

var configureQueryPanel = new Ext.Panel({
	layout : 'column',
	frame : true,
	items : [{
		xtype : 'fieldset',
		title : '查询条件',
		labelAlign : 'right',
		columnWidth : 1,
		layout : 'column',
		items : [{
			layout : 'form',
			columnWidth : .5,
			items : [{
				xtype : 'textfield',
				fieldLabel : '配班代码',
				id : 'Configure_Code',
				width : 100
			},{
				xtype : 'textfield',
				fieldLabel : '网点ID',
				hidden : true,
				id : 'Department_Id',
				width : 100
			}]
		},{
			layout : 'form',
			columnWidth : .5,
			items : [{
				xtype : 'textfield',
				fieldLabel : '月份',
				width : 100,
				readOnly : true,
				id : 'Configure_Month'
			}]
		}]
	}],
	tbar : [{
		text : '查询',
		minWidth : 60,
		pressed : true,
		handler : function() {
			search_LineConfigure();
		}
	},'&nbsp',{
		text : '保存',
		minWidth : 60,
		pressed : true,
		handler : function() {
			var schedulingDays = Ext.getCmp('add_date_picker_update').getDays();
			var employeeCode = Ext.getCmp('Employee_Code').text;
			var departmentCode = Ext.getCmp('Department_Code').text;
			var yearWeek = Ext.get('weekOfYear').getValue();

			var record = resultGrid_week.getSelectionModel().getSelected();
			var schedulingType = record.data['schedulingType'];

			var selectRow = configureGridPanel.getSelectionModel().getSelections();
			if (selectRow == 0) {
				Ext.Msg.alert('提示', '请选择配班代码!');
				return;
			}

			if (selectRow > 1) {
				Ext.Msg.alert('提示', '只能选择一个配班代码!');
				return;
			}

			if (schedulingDays == 0) {
				Ext.Msg.alert('提示', '请选择排班日期!');
				return;
			}

			var selectedLineConfigure = configureGridPanel.getSelectionModel().getSelected();
			var configureCode = selectedLineConfigure.data['CODE'];
			if ('休' != configureCode) {
				configureCode = selectedLineConfigure.data['DEPT_CODE'] + '-' + configureCode;
			}

			var myMask = new Ext.LoadMask(driverWindow.getEl(), {
				msg : "正在修改..."
			});
			myMask.show();

			Ext.Ajax.request({
				url : "updateDriverScheduling_scheduling.action",
				params : {
					employeeCode : employeeCode,
					departmentCode : departmentCode,
					yearWeek : yearWeek,
					configureCode : configureCode,
					schedulingDays : schedulingDays.join(","),
					schedulingType : schedulingType
				},
				success : function(response) {
					myMask.hide();
					result = Ext.util.JSON.decode(response.responseText);
					if (Ext.isEmpty(result.resultMap.errorMessage)) {
						Ext.Msg.alert("提示", "修改成功!");
						driverWindow.hide();
						queryDriverScheduling();
						return;
					}
					Ext.Msg.alert("提示", "修改失败!" + result.resultMap.errorMessage);
				},
				failure : function(response) {
					myMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);// 就可以取出来。如果是数组，那么很简单
					if (Ext.isEmpty(result.error)) {
						Ext.Msg.alert('提示', "修改失败！服务器错误！");
						return;
					}
					Ext.Msg.alert("提示", "修改失败!" + result.error);
				}
			});
		}
	}

	]
});

function search_LineConfigure() {
	var yearMonth = Ext.getCmp('Configure_Month').getValue();
	var configureCode = Ext.getCmp('Configure_Code').getValue();
	configure_Store.setBaseParam('MONTH', yearMonth);
	configure_Store.setBaseParam('CODE', configureCode);
	configure_Store.setBaseParam('DEPT_CODE', Ext.getCmp('hidden_Area_Code').getValue());
	configure_Store.setBaseParam('VALID_STATUS', 1);

	configure_Store.removeAll();
	configure_Store.load({
		add : true,
		params : {
			start : 0,
			limit : 20
		}
	});
}

var pickerDate = new Ext.ux.DatePicker({
	id : 'add_date_picker_update',
	style : 'margin-left:11px;margin-top:20px',
	format : 'Y-m-d',
	showToday : false,
	listeners : {
		render : function() {

		}
	}
});

var bottomPanel = new Ext.Panel({
	layout : 'border',
	region : 'center',
	items : [{
		title : '选择配班数据',
		xtype : 'panel',
		region : 'west',
		width : 530,
		items : [configureQueryPanel,configureGridPanel]

	},{
		xtype : 'panel',
		title : '选择日历',
		region : 'center',
		items : [pickerDate]
	}]
});

var topPanel = new Ext.Panel({
	title : '人员信息',
	region : 'north',
	height : 110,
	items : [employeePanel]
});

var formPanel = new Ext.form.FormPanel({
	layout : 'border',
	width : 750,
	height : 500,
	items : [topPanel,bottomPanel]
});

var driverWindow = new Ext.Window({
	title : '修改排班',
	width : 750,
	height : 500,
	closeAction : 'hide',
	items : [formPanel],
	tools : [{
		id : 'close',
		handler : function() {
			pickerDate.clearAllFun();
			driverWindow.hide();
		}
	}]
});

var updateDriverSchedulingButton = new Ext.Button({
	text : '修改',
	pressed : true,
	cls : 'x-btn-normal',
	minWidth : 60,
	handler : function() {
		var queryType = Ext.getCmp("query.queryType").getValue();
		if ("1" != queryType) {
			Ext.Msg.alert('提示', "只有查询方式为'按周'查询时才支持修改排班！");
			return;
		}
		var checkedRow = resultGrid_week.getSelectionModel().getSelections();
		var prevWeekMonday = moment().subtract(5, 'weeks').startOf('isoWeek');
		var selectWeekSunday = moment(Ext.get("selectedDay").getValue()).endOf('isoWeek');
		
		if (selectWeekSunday.isBefore(prevWeekMonday)) {
			Ext.Msg.alert('提示', '只能修改前五周及之后的排班数据！');
			return;
		}

		if (checkedRow.length == 0) {
			Ext.Msg.alert('提示', '请至少选择一条数据!');
			return;
		}

		if (checkedRow.length > 1) {
			Ext.Msg.alert('提示', '只能选择一条排班数据!');
			return;
		}

		var record = resultGrid_week.getSelectionModel().getSelected();
		var schedulingType = record.data['schedulingType'];
		if (schedulingType == 0) {
			/*------------------预排班处理------------------*/
		}

		driverWindow.show();
		setSchedulingParameter(record);
		search_LineConfigure();
	}
});

var setSchedulingParameter = function(record) {
	Ext.getCmp('Area_Code').setText(record.data['areaCode']);
	Ext.getCmp('hidden_Area_Code').setValue(record.data['areaCode']);
	Ext.getCmp('Employee_Name').setText(record.data['employeeName']);
	Ext.getCmp('Create_Employee').setText(record.data['creator']);
	// Ext.getCmp('Modify_Time').setText(new
	// Date(record.data['modifiedTime']).format('Y-m-d'));
	Ext.getCmp('Department_Code').setText(record.data['departmentCode']);
	Ext.getCmp('Employee_Code').setText(record.data['employeeCode']);
	Ext.getCmp('Create_Time').setText(record.data['createdTimeStr']);
	Ext.getCmp('Year_Week').setText(record.data['yearWeek']);
	Ext.getCmp('Total_Rest_Days').setText(record.data['totalRestDays']);
	Ext.getCmp('Modify_Employee').setText(record.data['modifier']);
	Ext.getCmp('Department_Id').setValue(record.data['departmentId']);
	Ext.getCmp('Configure_Month').setValue(moment(Ext.get("selectedDay").getValue()).format("YYYY-MM"));
	Ext.getCmp('Configure_Code').setValue('');

	var yearWeek = record.data['yearWeek'];
	var selectedDay = Ext.get("selectedDay").getValue();
	var monday = moment().year(yearWeek.split("-")[0]).isoWeek(yearWeek.split("-")[1]).isoWeekday(1);
	var sunday = moment().year(yearWeek.split("-")[0]).isoWeek(yearWeek.split("-")[1]).isoWeekday(7);
	var firstDayOfMonth = moment(selectedDay).startOf('month');
	var lastDayOfMonth = moment(selectedDay).endOf('month');
	if (monday.isBefore(firstDayOfMonth)) {
		monday = firstDayOfMonth;
	}
	if (sunday.isAfter(lastDayOfMonth)) {
		sunday = lastDayOfMonth;
	}

	pickerDate.setValue(moment(selectedDay).toDate());
	pickerDate.setMinDate(monday.toDate());
	pickerDate.setMaxDate(sunday.toDate());
};