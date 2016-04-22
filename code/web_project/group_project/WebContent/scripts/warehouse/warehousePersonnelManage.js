//<%@ page language="java" contentType="text/html; charset=utf-8"%>

// 查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		querySchedule();
	}
});

var worktypeStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : 'queryEmployeeWorkTypeByPostType_warehouseEmployee.action?'
	}),
	reader : new Ext.data.ArrayReader({
		root : 'dataMap.root'
	}, Ext.data.Record.create([ "id", "text" ]))
});

var workTypeComboBox = new Ext.form.ComboBox({
	store : worktypeStore,
	id : 'query.workType',
	anchor : '90%',
	hiddenName : 'hiddenWorkType',
	hiddenid : 'hiddenWorkType',
	displayField : 'text',
	mode : 'local',
	triggerAction : 'all',
	emptyText : '请选择...',
	forceSelection : true,
	fieldLabel : "人员类型",
	listeners : {
		blur : function() {
			if (this.getValue() == '') {
				Ext.getCmp('query.workTypeId').setValue();
			}
		},
		select : function(combo, record, index) {
			Ext.getCmp('query.workTypeId').setValue(record.data['id']);
		}
	}
});

var CONDITION_POST_NAME = 'EMP_DUTY_NAME';
var CONDITION_POST_NAME_ID = 'QUERY.EMP_DUTY_NAME';
var CONDITION_EMPLOYEE_NAME = 'EMP_NAME';
var CONDITION_EMPLOYEE_NAME_ID = 'QUERY.EMP_NAME';
var CONDITION_WORKING_STATE = 'WORKING_STATE';
var CONDITION_WORKING_STATE_ID = 'QUERY.WORKING_STATE';

var LABEL_POST_NAME = '岗位名称';
var LABEL_EMPLOYEE_NAME = '姓名';
var LABEL_WORKING_STATE = '在职状态';

var tbar = [];

addBar('<app:isPermission code = "/warehouse/query_warehouseEmployee.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/warehouse/update_warehouseEmployee.action">a</app:isPermission>', btnUpdateEmployee);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

// 顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout : 'column',
	height : 180,
	tbar : tbar,
	items : [ {
		xtype : 'fieldset',
		title : '查询条件',
		layout : "column",
		columnWidth : 1,
		style : 'margin-top:5px;',
		frame : true,
		items : [ {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				labelWidth : 120,
				labelAlign : 'right',
				layout : 'form',
				items : [ {
					xtype : "textfield",
					fieldLabel : '<font color=red>网点代码*</font>',
					id : "query.branchCode",
					anchor : '90%',
					onTriggerClick : function() {
						var _window = new Ext.sf_dept_window({
							callBackInput : "query.branchCode"
						});
						_window.show(this);
					}
				} ]
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'textfield',
				id : 'query.workNumber',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				fieldLabel : '工号',
				anchor : '90%'
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'combo',
				id : 'query.workTypeId',
				anchor : '90%',
				fieldLabel : '人员类型',
				mode : 'local',
				emptyText : '请选择...',
				forceSelection : true,
				triggerAction : 'all',
				store : workType
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 140,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'combo',
				id : 'query.is_have_commission',
				anchor : '90%',
				fieldLabel : IS_HAVE_COMMISSION,
				emptyText : '请选择...',
				mode : 'local',
				typeAhead : true,
				triggerAction : "all",
				forceSelection : true,
				store : [ [ '0', '否' ], [ '1', '是' ] ]
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'textfield',
				id : CONDITION_EMPLOYEE_NAME_ID,
				fieldLabel : LABEL_EMPLOYEE_NAME,
				anchor : '90%'
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'textfield',
				id : CONDITION_POST_NAME_ID,
				fieldLabel : LABEL_POST_NAME,
				anchor : '90%'
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'combo',
				id : CONDITION_WORKING_STATE_ID,
				anchor : '90%',
				fieldLabel : LABEL_WORKING_STATE,
				emptyText : '请选择...',
				mode : 'local',
				typeAhead : true,
				triggerAction : "all",
				forceSelection : true,
				store : [ [ '0', '全部' ], [ '1', '在职' ], [ '2', '离职' ] ]
			} ]
		} ]
	} ]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

var record_start = 0;
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns : [ sm, new Ext.grid.RowNumberer({
		header : "序号",
		width : 40,
		renderer : function(value, metadata, record, rowIndex) {
			return record_start + 1 + rowIndex;
		}
	}), {
		header : 'ID',
		hidden : true,
		sortable : true,
		dataIndex : 'EMP_ID',
		align : "center"
	}, {
		header : '地区代码',
		sortable : true,
		dataIndex : 'AREA_CODE',
		align : "center;vertical-align:middle"
	}, {
		header : '网点代码',
		sortable : true,
		dataIndex : 'DEPT_CODE',
		align : "center;vertical-align:middle"
	}, {
		header : '网点名称',
		sortable : true,
		dataIndex : 'DEPT_NAME',
		align : "center;vertical-align:middle"
	}, {
		header : '工号',
		sortable : true,
		dataIndex : 'EMP_CODE',
		align : "center;vertical-align:middle"
	}, {
		header : '姓名',
		sortable : true,
		dataIndex : 'EMP_NAME',
		align : "center;vertical-align:middle"
	}, {
		header : '人员类型',
		sortable : true,
		dataIndex : 'WORK_TYPE_NAME',
		align : "center;vertical-align:middle"
	}, {
		header : '人员类型ID',
		hidden : true,
		sortable : true,
		dataIndex : 'WORK_TYPE',
		align : "center;vertical-align:middle"
	}, {
		header : '岗位名称',
		sortable : true,
		dataIndex : 'EMP_DUTY_NAME',
		align : "center"
	}, {
		header : '入职日期',
		sortable : true,
		dataIndex : 'SF_DATE',
		align : "center;vertical-align:middle"
	}, {
		header : '在职状态',
		sortable : true,
		dataIndex : 'WORKING_TYPE',
		align : "center;vertical-align:middle"
	}, {
		header : IS_HAVE_COMMISSION,
		sortable : true,
		dataIndex : 'IS_HAVE_COMMISSION',
		width : 110,
		align : "center;vertical-align:middle"
	}, {
		header : '机动网点',
		sortable : true,
		dataIndex : 'MOBILE_NETWORK',
		width : 150,
		align : "left;vertical-align:middle"
	} ]
});

// 数据格构建
var record = Ext.data.Record.create([ {
	name : 'EMP_ID',
	mapping : 'EMP_ID',
	type : 'string'
}, {
	name : 'AREA_CODE',
	mapping : 'AREA_CODE',
	type : 'string'
}, {
	name : 'DEPT_CODE',
	mapping : 'DEPT_CODE',
	type : 'string'
}, {
	name : 'DEPT_NAME',
	mapping : 'DEPT_NAME',
	type : 'string'
}, {
	name : 'EMP_CODE',
	mapping : 'EMP_CODE',
	type : 'string'
}, {
	name : 'EMP_NAME',
	mapping : 'EMP_NAME',
	type : 'string'
}, {
	name : 'WORK_TYPE_NAME',
	mapping : 'WORK_TYPE_NAME',
	type : 'string'
}, {
	name : 'WORK_TYPE',
	mapping : 'WORK_TYPE',
	type : 'string'
}, {
	name : 'EMP_DUTY_NAME',
	mapping : 'EMP_DUTY_NAME',
	type : 'string'
}, {
	name : 'SF_DATE',
	mapping : 'SF_DATE',
	type : 'string'
}, {
	name : 'IS_HAVE_COMMISSION',
	mapping : 'IS_HAVE_COMMISSION',
	type : 'string'
}, {
	name : 'MOBILE_NETWORK',
	mapping : 'MOBILE_NETWORK',
	type : 'string'
}, {
	name : 'WORKING_TYPE',
	mapping : 'WORKING_TYPE',
	type : 'string'
} ]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../warehouse/query_warehouseEmployee.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'dataMap.root',
		totalProperty : 'dataMap.totalSize'
	}, record)
});

// 分页组件
var pageBar = new Ext.PagingToolbar({
	store : store,
	displayInfo : true,
	displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize : 20,
	emptyMsg : '未检索到数据'
});

// 表格构建
var grid = new Ext.grid.GridPanel({
	cm : cm,
	sm : sm,
	store : store,
	autoScroll : true,
	loadMask : true,
	tbar : pageBar,
	viewConfig : {
	// forceFit: true
	}
});

// 中部的Panel
var centerPanel = new Ext.Panel({
	region : 'center',
	margins : '1 1 1 0',
	items : [ topPanel, grid ],
	listeners : {
		resize : function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			grid.setWidth(adjWidth - 5);
			grid.setHeight(adjHeight - 165);
		}
	}
});

function parseDepartment(departmentCodes) {
	var codes = [];
	Ext.each(departmentCodes.split(','), function(v, i) {
		codes.push("'" + (v.split('/')[0]) + "'");
	});
	return codes.join(",");
}

// 查询方法
var querySchedule = function(node) {
	var departmentCodes = Ext.getCmp("query.branchCode").getValue();
	if (Ext.isEmpty(departmentCodes)) {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	store.setBaseParam("dept_id", parseDepartment(departmentCodes));
	store.setBaseParam("emp_code", Ext.getCmp('query.workNumber').getValue());
	store.setBaseParam("work_type", Ext.getCmp('query.workTypeId').getValue());
	store.setBaseParam("is_have_commission", Ext.getCmp('query.is_have_commission').getValue());
	store.setBaseParam(CONDITION_EMPLOYEE_NAME, Ext.getCmp(CONDITION_EMPLOYEE_NAME_ID).getValue());
	store.setBaseParam(CONDITION_POST_NAME, Ext.getCmp(CONDITION_POST_NAME_ID).getValue());
	store.setBaseParam(CONDITION_WORKING_STATE, Ext.getCmp(CONDITION_WORKING_STATE_ID).getValue());

	store.load({
		params : {
			start : 0,
			limit : 20
		}
	});
};

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout : "border",
		items : [ centerPanel ]
	});
	worktypeStore.setBaseParam("postType", "3");
	worktypeStore.load();
});
