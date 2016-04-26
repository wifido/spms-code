// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var ID = 'ID';

var HEADER_INPUT_TYPE = '数据源';
var INPUT_TYPE = "INPUT_TYPE";

var HEADER_OPTIMIZE_LINE_CODE = '路径优化线路编号';
var OPTIMIZE_LINE_CODE = "OPTIMIZE_LINE_CODE";

var HEADER_VEHICLE_TYPE = '车型';
var VEHICLE_TYPE = "VEHICLE_TYPE";

var HEADER_START_TIME = '出车时间';
var START_TIME = 'START_TIME';

var HEADER_END_TIME = '收车时间';
var END_TIME = 'END_TIME';

var HEADER_SOURCE_CODE = '始发网点';
var SOURCE_CODE = "SOURCE_CODE";
var SOURCE_NAME = "SOURCE_NAME";

var HEADER_DESTINATION_CODE = '目的网点';
var DESTINATION_CODE = "DESTINATION_CODE";
var DESTINATION_NAME = "DESTINATION_NAME";

var HEADER_BELONG_ZONE_CODE = '归属网点';
var BELONG_ZONE_CODE = "BELONG_ZONE_CODE";
var BELONG_NAME = "BELONG_NAME";

var HEADER_VEHICLE_NUMBER = '车牌号';
var VEHICLE_NUMBER = "VEHICLE_NUMBER";

var HEADER_VALID_STATUS = '状态';
var VALID_STATUS = "VALID_STATUS";

var VALUE_INVALID = '无效';
var VALUE_VALID = '有效';

var LABEL_INPUT_BY_HAND = '手工录入';
var LABEL_INPUT_BY_OPTIMIZE = '路径优化';

var LABEL_DEPARTMENT_CODE = '网点代码';
var ENTITY_DEPARTMENT_CODE = "ENTITY_DEPARTMENT_CODE";

var LABEL_ARRANGE_CODE = '班次代码';
var ENTITY_ARRANGE_CODE = "ENTITY_ARRANGE_CODE";

var LABEL_VALID_STATE = '有效性';
var ENTITY_VALID_STATE = "ENTITY_VALID_STATE";

var HEADER_CONFIGURE_ID = "配班ID";
var CONFIGURE_ID = "CONFIGURE_ID";

var YEAR_MONTH = "YEAR_MONTH";

var HEADER_CONFIGURE_LINE_ID = "配班线路ID";
var CONFIGURE_LINE_ID = 'CONFIGURE_LINE_ID';

var ENTITY_DEPARTMENT_TYPE = 'CONFIGURE_TYPE';
var LABEL_DEPARTMENT_TYPE = '配班类型';

var ENTITY_CONFIGURE_LINES = 'CONFIGURE_LINES';
var LABEL_OPTION_CONFIGURE_LINES = '已选的配班线路ID';

var ADD_ENTITY_START = "ADD_ENTITY_START";

var QUERY_LINE_PATH = '../driver/queryLine.action';

var buttonTop = [{
	text : '保存',
	pressed : true,
	minWidth : 60,
	handler : function() {
		var lineId = [];
		configureLineStore.each(function(record) {
			lineId.push(record.data[CONFIGURE_LINE_ID]);
		});

		if (lineId.length == 0) {
			Ext.Msg.alert('提示', '已选线路不能为空！');
			return;
		}
		
		Ext.Ajax.request({
			url: "../driver/validClassesCode.action",
			params: {
				'departmentCode': addFormPanel.getForm().findField(ENTITY_ARRANGE_CODE).getValue().split('-')[0],
				'code': addFormPanel.getForm().findField(ENTITY_ARRANGE_CODE).getValue().split('-')[1],
				'yearMonth': Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m")
			},
			success: function(response) {
				var result = Ext.decode(response.responseText)
				if (Ext.isEmpty(result.existClassCode)) {
					Ext.Msg.alert('提示', '操作失败！保存发生异常！');
					return;
				} 

				if (result.existClassCode > 0) {
					Ext.Msg.alert(PROMPT, '班次代码已存在,请重新获取班次代码！');
					return;
				}
				
				addFormPanel.getForm().findField(ENTITY_CONFIGURE_LINES).setValue(lineId.toString());
				addFormPanel.getForm().submit({
					waitMsg : '正在提交数据',
					waitTitle : '提示',
					method : "POST",
					timeout : 30000,
					url : '../driver/addConfigureClassesInformation.action',
					success : function(form, action) {
						Ext.Msg.alert('提示', '新增成功！');
						addFormPanel.getForm().reset();
						allLinePanel.store.removeAll();
						configureLineGridPanel.store.removeAll();
						addConfigureWindow.hide();

						store.load({
							params : {
								start : 0,
								limit : 20
							}
						});
					},
					failure : function(form, action) {
						var msg = '新增失败!';
						if (!Ext.isEmpty(action.result.error)) {
							msg = action.result.error;
						}
						Ext.Msg.alert('提示', '新增失败 !' + msg);
					}
				});
			}
		});
		
	}
},'&nbsp',{
	text : '重新获取班次代码',
	minWidth : 60,
	pressed : true,
	cls : 'x-btn-normal',
	handler : function() {
		setClassesCode(Ext.getCmp(ID_PARAMETER_DEPARTMENT).getValue().split("/")[0]);
	}
}];

var buttonCenter = [{
	xtype : 'label',
	style : 'font-weight:bold;font-size:12;color:#15428b;',
	text : '已选的线路'
},'-',{
	xtype : 'label',
	style : 'color:#15428b;',
	text : '注意：已选线路若被修改，此处将仍显示修改前的线路信息'
},'->',{
	text : '车牌号',
	hidden : true,
	minWidth : 60,
	cls : 'x-btn-normal',
	pressed : true,
	handler : function() {
	}
},'&nbsp',{
	text : '去掉选中线路',
	minWidth : 60,
	cls : 'x-btn-normal',
	pressed : true,
	handler : function() {
		removeChooseWithClass();
	}
}];

function removeChooseWithClass() {
	var configureLineSm = configureLineGridPanel.getSelectionModel();
	var store = configureLineGridPanel.getStore();

	if (!configureLineSm.hasSelection()) {
		Ext.Msg.alert(PROMPT, '请先选择需要删除的线路,至少一条！');
		return;
	}

	store.remove(configureLineSm.getSelections());

	setChooseBackgroundColor();
}
var configureLineSm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

function setClassesCode(department) {
	if (department == null) {
		department = Ext.getCmp(ID_PARAMETER_DEPARTMENT).getValue().split("/")[0];
	}

	Ext.Ajax.request({
		url : "queryClassesCode.action",
		params : {
			'departmentCode' : department,
			'type' : '1',
			'yearMonth' : Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m")
		},
		success : function(response) {
			var result = Ext.decode(response.responseText);
			if (Ext.isEmpty(result.classesCode)) {
				Ext.Msg.alert(PROMPT, '程序发生异常,请重新获取班次代码！');
				return;
			}
			if (!Ext.isEmpty(addFormPanel) && addFormPanel.isVisible()) {
				addFormPanel.getForm().findField(ENTITY_ARRANGE_CODE).setValue(result.classesCode);
			}
		}
	});
}

var configureLineRecord = Ext.data.Record.create([{
	name : CONFIGURE_ID,
	mapping : CONFIGURE_ID,
	type : 'int'
},{
	name : CONFIGURE_LINE_ID,
	mapping : CONFIGURE_LINE_ID,
	type : 'int'
},{
	name : OPTIMIZE_LINE_CODE,
	mapping : OPTIMIZE_LINE_CODE,
	type : 'string'
},{
	name : VEHICLE_TYPE,
	mapping : VEHICLE_TYPE,
	type : 'string'
},{
	name : START_TIME,
	mapping : START_TIME,
	type : 'string'
},{
	name : END_TIME,
	mapping : END_TIME,
	type : 'string'
},{
	name : SOURCE_CODE,
	mapping : SOURCE_CODE,
	type : 'string'
},{
	name : DESTINATION_CODE,
	mapping : DESTINATION_CODE,
	type : 'string'
},{
	name : BELONG_ZONE_CODE,
	mapping : BELONG_ZONE_CODE,
	type : 'string'
},{
	name : VEHICLE_NUMBER,
	mapping : VEHICLE_NUMBER,
	type : 'string'
}]);

var configureLineStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../driver/queryLine.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, configureLineRecord)
});

var configureLineColumn = new Ext.grid.ColumnModel({
	columns : [configureLineSm,{
		header : HEADER_INPUT_TYPE,
		dataIndex : INPUT_TYPE,
		sortable : false,
		width : 55,
		rendererCall : function(value) {
			if (0 == value) {
				return LABEL_INPUT_BY_HAND;
			}
			if (1 == value) {
				return LABEL_INPUT_BY_OPTIMIZE;
			}
			return value;
		}
	},{
		header : HEADER_OPTIMIZE_LINE_CODE,
		dataIndex : OPTIMIZE_LINE_CODE,
		sortable : false,
		width : 105
	},{
		header : HEADER_CONFIGURE_ID,
		dataIndex : CONFIGURE_ID,
		sortable : false,
		width : 105,
		hidden : true
	},{
		header : HEADER_CONFIGURE_LINE_ID,
		dataIndex : CONFIGURE_LINE_ID,
		sortable : false,
		width : 105,
		hidden : true
	},{
		header : HEADER_VEHICLE_TYPE,
		dataIndex : VEHICLE_TYPE,
		sortable : false,
		width : 70
	},{
		header : HEADER_START_TIME,
		dataIndex : START_TIME,
		sortable : false,
		width : 60
	},{
		header : HEADER_END_TIME,
		dataIndex : END_TIME,
		sortable : false,
		width : 60
	},{
		header : HEADER_SOURCE_CODE,
		dataIndex : SOURCE_CODE,
		sortable : false,
		width : 85
	},{
		header : HEADER_DESTINATION_CODE,
		dataIndex : DESTINATION_CODE,
		sortable : false,
		width : 85
	},{
		header : HEADER_BELONG_ZONE_CODE,
		dataIndex : BELONG_ZONE_CODE,
		sortable : false,
		width : 85
	},{
		header : HEADER_VEHICLE_NUMBER,
		dataIndex : VEHICLE_NUMBER,
		sortable : false,
		width : 65
	},{
		header : HEADER_VALID_STATUS,
		dataIndex : VALID_STATUS,
		sortable : false,
		width : 100,
		rendererCall : function(value) {
			if (1 == value) {
				return VALUE_VALID;
			}
			if (0 == value) {
				return VALUE_INVALID;
			}
			return value;
		}
	}]
});

var configureLineGridPanel = new Ext.grid.GridPanel({
	region : "center",
	tbar : [buttonCenter],
	xtype : 'grid',
	height : 130,
	id : "configureLineGridPanel",
	enableLocking : true,
	border : false,
	style : "border: 1px solid #8db2e3;",
	enableHdMenu : false,
	forceFit : true,
	sm : configureLineSm,
	enableColumnResize : false,
	listeners : {
		dblclick : removeChooseWithClass
	},
	store : configureLineStore,
	cm : configureLineColumn
});

var allRecordTop = Ext.data.Record.create([{
	name : ID,
	mapping : ID,
	type : 'int'
},{
	name : OPTIMIZE_LINE_CODE,
	mapping : OPTIMIZE_LINE_CODE,
	type : 'string'
},{
	name : VEHICLE_TYPE,
	mapping : VEHICLE_TYPE,
	type : 'string'
},{
	name : START_TIME,
	mapping : START_TIME,
	type : 'string'
},{
	name : END_TIME,
	mapping : END_TIME,
	type : 'string'
},{
	name : SOURCE_CODE,
	mapping : SOURCE_CODE,
	type : 'string'
},{
	name : SOURCE_NAME,
	mapping : SOURCE_NAME,
	type : 'string'
},{
	name : DESTINATION_CODE,
	mapping : DESTINATION_CODE,
	type : 'string'
},{
	name : DESTINATION_NAME,
	mapping : DESTINATION_NAME,
	type : 'string'
},{
	name : BELONG_ZONE_CODE,
	mapping : BELONG_ZONE_CODE,
	type : 'string'
},{
	name : BELONG_NAME,
	mapping : BELONG_NAME,
	type : 'string'
},{
	name : VEHICLE_NUMBER,
	mapping : VEHICLE_NUMBER,
	type : 'string'
},{
	name : INPUT_TYPE,
	mapping : INPUT_TYPE,
	type : 'string'
},{
	name : VALID_STATUS,
	mapping : VALID_STATUS,
	type : 'string'
}]);

var allLineStore = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : QUERY_LINE_PATH
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, allRecordTop),
	listeners : {
		beforeLoad : function() {
			var startTime = Ext.getCmp(ADD_ENTITY_START).getValue();
			this.baseParams['startTime'] = startTime;
			this.baseParams['departmentId'] = getSelectedDepartmentNode().id;
			this.baseParams['inputType'] = "0";
			this.baseParams['validStatus'] = "1";
			this.baseParams['limit'] = allLinePanel.getTopToolbar().getComponent(2).pageSize;
		},
		load : function() {
			setChooseBackgroundColor();
		}
	}
});

var allLineSm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

var allLineButton = [{
	xtype : 'label',
	style : 'font-weight:bold;font-size:12;color:#15428b;',
	text : '所有的线路'
},'-',new Ext.PagingToolbar({
	pageSize : 10,
	store : allLineStore,
	displayInfo : true
}),'->',{
	xtype : "trigger",
	triggerClass : 'x-form-search-trigger',
	emptyText : '请输入搜索条件',
	id : ADD_ENTITY_START,
	onTriggerClick : function() {
		allLineStore.load({
			params : {
				start : 0,
				limit : 10
			},
			scope : allLineStore
		});
	}
},'-',{
	text : '选为配班线路',
	minWidth : 60,
	cls : 'x-btn-normal',
	pressed : true,
	handler : function() {
		chooseWithClass();
	}
}];

Ext.HashMap = function() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	};

	/** 取 * */
	this.get = function(key) {
		return this.containsKey(key) ? entry[key] : null;
	};

	/** 删除 * */
	this.remove = function(key) {
		if (this.containsKey(key) && (delete entry[key])) {
			size--;
		}
	};

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	};

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for ( var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	};

	/** 所有 Value * */
	this.values = function() {
		var values = new Array();
		for ( var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	};

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array();
		for ( var prop in entry) {
			keys.push(prop);
		}
		return keys;
	};

	/** Map Size * */
	this.size = function() {
		return size;
	};

	/* 清空 */
	this.clear = function() {
		size = 0;
		entry = new Object();
	};
}

function validSourceCodeDestinationCodeSame(optionLine, configureGridPanel) {
	if (configureGridPanel.getStore().getCount() > 0) {
		var lastLine = configureGridPanel.getStore().getAt(configureGridPanel.getStore().getCount() - 1);
		var firstLine = optionLine.getSelections()[0];

		if (lastLine.data.DESTINATION_CODE != firstLine.data.SOURCE_CODE) {
			return false;
		}
	}

	for ( var index = 0; index < optionLine.getCount(); index++) {
		if (optionLine.getCount() - 1 == index) {
			return true;
		}
		var currentLine = optionLine.getSelections()[index];
		var nextLine = optionLine.getSelections()[index + 1];

		if (currentLine.data.DESTINATION_CODE != nextLine.data.SOURCE_CODE) {
			return false;
		}
	}

	return true;
}

function chooseWithClass() {
	var optionLine = allLinePanel.getSelectionModel();
	var store = configureLineGridPanel.getStore();

	if (!optionLine.hasSelection()) {
		Ext.Msg.alert(PROMPT, '请先选择需要添加的线路,至少一条！');
		return;
	}

	if (!validSourceCodeDestinationCodeSame(optionLine, configureLineGridPanel)) {
		Ext.Msg.alert('提示', '目的网点必须和始发网点相同');
		return;
	}

	var inValid = false;

	// 获取新选中的数据
	var data = [];
	Ext.each(optionLine.getSelections(), function(item) {
		store.each(function(configureLine) {
			if (item.data.START_TIME == configureLine.data.START_TIME) {
				Ext.Msg.alert('提示', '不能同时选择两笔出车时间相同的数据！');
				inValid = true;
				return;
			}

			if (item.data.END_TIME == configureLine.data.END_TIME) {
				Ext.Msg.alert('提示', '不能同时选择两笔收车时间相同的数据！');
				inValid = true;
				return;
			}

			if (item.data.ID == configureLine.data.CONFIGURE_LINE_ID && item.data.SOURCE_CODE == configureLine.data.SOURCE_CODE) {
				Ext.Msg.alert('提示', '不能同时选择两笔线路相同的数据！');
				inValid = true;
				return;
			}
		});

		if (inValid) {
			return;
		}

		var plant = new store.recordType({
			// CONFIGURE_ID: item.data.id,
			CONFIGURE_LINE_ID : item.data.ID,
			OPTIMIZE_LINE_CODE : item.data.OPTIMIZE_LINE_CODE,
			VEHICLE_TYPE : item.data.VEHICLE_TYPE,
			START_TIME : item.data.START_TIME,
			END_TIME : item.data.END_TIME,
			SOURCE_CODE : item.data.SOURCE_CODE,
			SOURCE_NAME : item.data.SOURCE_NAME,
			DESTINATION_CODE : item.data.DESTINATION_CODE,
			DESTINATION_NAME : item.data.DESTINATION_NAME,
			BELONG_ZONE_CODE : item.data.BELONG_ZONE_CODE,
			BELONG_NAME : item.data.BELONG_NAME,
			VEHICLE_NUMBER : item.data.VEHICLE_NUMBER,
			INPUT_TYPE : item.data.INPUT_TYPE,
			VALID_STATUS : item.data.VALID_STATUS
		});
		this.push(plant);
	}, data);

	if (inValid) {
		return;
	}

//	sortAscending(data);

	store.insert(store.getCount(), data);

	optionLine.clearSelections();

	setChooseBackgroundColor();
}

function setChooseBackgroundColor() {
	var optionLineMap = new Ext.HashMap();

	configureLineGridPanel.getStore().each(function(configureLine) {
		var key = configureLine.data.CONFIGURE_LINE_ID + "_" + configureLine.data.BELONG_ZONE_CODE;
		optionLineMap.put(key);
	});

	for ( var i = 0; i < allLinePanel.getStore().getCount(); i++) {
		allLinePanel.getView().getRow(i).style.backgroundColor = "#FFFFFF";
		allLinePanel.getView().getRow(i).title = '';
	}

	allLinePanel.getStore().each(function(item, i) {
		var key = item.data.ID + "_" + item.data.BELONG_ZONE_CODE;
		if (optionLineMap.containsKey(key)) {
			optionLineMap.remove(key);
			allLinePanel.getView().getRow(i).style.backgroundColor = '#FF8888';
			allLinePanel.getView().getRow(i).title = '数据已经存在';
		}
	});
}

function sortAscending(data) {
	data.sort(function(record1, record2) {
		if (record1.data.START_TIME > record2.data.START_TIME) {
			return 1;
		}
		return -1;
	});
}

function removeAllConfigureLine(store) {
	if (store.getCount() > 0) {
		store.removeAll();
	}
}

function getConfigureLineOldData(store, data) {
	// 获取旧数据
	if (store.getCount() > 0) {
		var oldData = store.getRange();
		Ext.each(oldData, function(item) {
			this.push(item);
		}, data);
	}
}

/**
 * 重写Grid.Column.renderer 每列悬停时提示内容信息
 */
Ext.override(Ext.grid.Column, {
	renderer : function(value, metadata, record, rowIdx, colIdx, ds) {
		if (this.rendererCall) {
			var ret = this.rendererCall(value, metadata, record, rowIdx, colIdx, ds);
			return '<div ext:qtitle="' + this.header + '" ext:qtip="' + (ret == null ? "" : ret) + '">' + (ret == null ? "" : ret) + '</div>';
		} else {
			return '<div ext:qtitle="' + this.header + '" ext:qtip="' + (value == null ? "" : value) + '">' + (value == null ? "" : value) + '</div>';
		}
	}
});

var allLineColumn = new Ext.grid.ColumnModel({
	columns : [allLineSm,{
		header : HEADER_INPUT_TYPE,
		dataIndex : INPUT_TYPE,
		sortable : false,
		width : 55,
		rendererCall : function(value) {
			if (0 == value) {
				return LABEL_INPUT_BY_HAND;
			}
			if (1 == value) {
				return LABEL_INPUT_BY_OPTIMIZE;
			}
			return value;
		}
	},{
		header : 'ID',
		dataIndex : "ID",
		sortable : false,
		width : 105,
		hidden : true
	},{
		header : HEADER_OPTIMIZE_LINE_CODE,
		dataIndex : OPTIMIZE_LINE_CODE,
		sortable : false,
		width : 105
	},{
		header : HEADER_VEHICLE_TYPE,
		dataIndex : VEHICLE_TYPE,
		sortable : false,
		width : 70
	},{
		header : HEADER_START_TIME,
		dataIndex : START_TIME,
		sortable : false,
		width : 60
	},{
		header : HEADER_END_TIME,
		dataIndex : END_TIME,
		sortable : false,
		width : 60
	},{
		header : HEADER_SOURCE_CODE,
		dataIndex : SOURCE_CODE,
		sortable : false,
		width : 130,
		rendererCall : function(value, obj, record) {
			return value + "/" + record.data[SOURCE_NAME];
		}
	},{
		header : HEADER_DESTINATION_CODE,
		dataIndex : DESTINATION_CODE,
		sortable : false,
		width : 85,
		rendererCall : function(value, obj, record) {
			return value + "/" + record.data[DESTINATION_NAME];
		}
	},{
		header : HEADER_BELONG_ZONE_CODE,
		dataIndex : BELONG_ZONE_CODE,
		sortable : false,
		width : 85,
		rendererCall : function(value, obj, record) {
			return value + "/" + record.data[BELONG_NAME];
		}
	},{
		header : HEADER_VEHICLE_NUMBER,
		dataIndex : VEHICLE_NUMBER,
		sortable : false,
		width : 65
	},{
		header : HEADER_VALID_STATUS,
		dataIndex : VALID_STATUS,
		sortable : false,
		width : 50,
		rendererCall : function(value) {
			if (1 == value) {
				return VALUE_VALID;
			}
			if (0 == value) {
				return VALUE_INVALID;
			}
			return value;
		}
	}]
});

var allLinePanel = new Ext.grid.GridPanel({
	region : 'south',
	height : 240,
	xtype : 'grid',
	id : "allLinePanel",
	border : false,
	style : "border-bottom: 1px solid #8db2e3;border-left: 1px solid #8db2e3;border-right: 1px solid #8db2e3;",
	loadMask : true,
	enableHdMenu : false,
	enableColumnResize : false,
	sm : allLineSm,
	store : allLineStore,
	listeners : {
		dblclick : chooseWithClass
	},
	tbar : [allLineButton],
	cm : allLineColumn
});

var addFormPanel = new Ext.form.FormPanel({
	layout : "column",
	xtype : "form",
	id : "addFormPanel",
	frame : true,
	defaults : {
		xtype : 'panel',
		columnWidth : .5,
		layout : "form",
		labelAlign : "right",
		labelWidth : 60
	},
	items : [{
		columnWidth : .25,
		items : [{
			xtype : "textfield",
			fieldLabel : LABEL_DEPARTMENT_CODE,
			readOnly : true,
			name : ENTITY_DEPARTMENT_CODE,
			width : 100
		},{
			xtype : 'hidden',
			fieldLabel : LABEL_DEPARTMENT_TYPE,
			name : ENTITY_DEPARTMENT_TYPE
		},{
			xtype : 'hidden',
			fieldLabel : LABEL_OPTION_CONFIGURE_LINES,
			name : ENTITY_CONFIGURE_LINES
		}]
	},{
		columnWidth : .25,
		items : [{
			xtype : "textfield",
			fieldLabel : LABEL_ARRANGE_CODE,
			name : ENTITY_ARRANGE_CODE,
			readOnly : true,
			maxLength : 50,
			width : 100
		}]
	},{
		columnWidth : .25,
		items : [{
			xtype : "combo",
			hiddenName : ENTITY_VALID_STATE,
			width : 100,
			fieldLabel : LABEL_VALID_STATE,
			mode : 'local',
			displayField : "text",
			valueField : "value",
			triggerAction : 'all',
			editable : false,
			allowBlank : false,
			value : 1,
			store : new Ext.data.SimpleStore({
				fields : ["text","value"],
				data : [[VALUE_VALID,1],[VALUE_INVALID,0]]
			})
		}]
	},{
		columnWidth : .25,
		items : [{
			xtype : "textfield",
			fieldLabel : '<font color=red>月份*</font>',
			name : YEAR_MONTH,
			readOnly : true,
			maxLength : 50,
			width : 100
		}]
	},{
		columnWidth : 1,
		xtype : "fieldset",
		style : "padding-left:10px;padding-right:10px;padding-top:10px;",
		height : 400,
		title : '线路明细数据',
		layout : "border",
		items : [configureLineGridPanel,allLinePanel]
	}]
});

var addConfigurePanel = new Ext.Panel({
	layout : 'fit',
	tbar : buttonTop,
	items : [addFormPanel]
});

var addConfigureWindow = new Ext.Window({
	title : '新增配班信息',
	height : 490,
	width : 880,
	layout : 'fit',
	closeAction : 'hide',
	tools : [{
		id : 'close',
		handler : function() {
			addFormPanel.getForm().reset();

			addConfigureWindow.hide();

			configureLineGridPanel.store.removeAll();

			allLinePanel.store.removeAll();
		}
	}],
	items : [addConfigurePanel]
});

function isAreaCode(departmentCode) {
	var flag = false;
	Ext.Ajax.request({
		url : "checkDepartment_scheduling.action",
		async : false,
		params : {
			departmentCode : departmentCode
		},
		success : function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			if (result.success) {
				flag = true;
			}
		},
		failure : function(response) {
		}
	});
	return flag;
}

var addButton = new Ext.Button({
	text : '新增',
	cls : 'x-btn-normal',
	pressed : true,
	minWidth : 60,
	handler : function() {
		var departmentCode = Ext.getCmp(ID_PARAMETER_DEPARTMENT).getValue();
		if (Ext.isEmpty(departmentCode)) {
			Ext.Msg.alert('提示', '请先选择网点！');
			return;
		}
		departmentCode = departmentCode.split("/")[0];
		if (isAreaCode(departmentCode)) {
			Ext.Msg.alert('提示', "只能在区部以下网点新增配班！");
			return;
		}

		if (validModifyMonthBeforeCurrentMonth()) {
			Ext.Msg.alert('提示', '当月之前的配班不能进行新增操作');
			return;
		}

		addConfigureWindow.show();
		addFormPanel.getForm().findField(ENTITY_DEPARTMENT_CODE).setValue(departmentCode);
		addFormPanel.getForm().findField(ENTITY_DEPARTMENT_TYPE).setValue(1);
		addFormPanel.getForm().findField(YEAR_MONTH).setValue(Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m"));

		setClassesCode();
		allLineStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}
});

