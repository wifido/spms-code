// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var lineConfigureId;
var KEY_ENTITY_START = 'ENTITY_START';

var buttonTop_modify = [{
	text : '保存',
	pressed : true,
	minWidth : 60,
	handler : function() {
		var lineId = [];
		configureLineStore_modify.each(function(record) {
			lineId.push(record.data[ID]);
		});

		if (lineId.length == 0) {
			Ext.Msg.alert('提示', '已选线路不能为空！');
			return;
		}

		addFormPanel_modify.getForm().findField(ENTITY_CONFIGURE_LINES).setValue(lineId.toString());
		addFormPanel_modify.getForm().submit({
			waitMsg : '正在提交数据',
			waitTitle : '提示',
			method : "POST",
			timeout : 30000,
			url : '../driver/update_lineConfigure.action',
			params : {
				LINE_CONFIGURE_ID : lineConfigureId
			},
			success : function(form, action) {
				Ext.Msg.alert('提示', '修改成功！');
				addFormPanel_modify.getForm().reset();
				allLinePanel_modify.store.removeAll();
				configureLineGridPanel_modify.store.removeAll();
				addConfigureWindow_modify.hide();

				store.load({
					params : {
						start : 0,
						limit : 20
					}
				});
			},
			failure : function(form, action) {
				var msg = '修改失败！服务器出错！';
				if (!Ext.isEmpty(action.result.error)) {
					msg = action.result.error;
				}
				Ext.Msg.alert('提示', '修改失败 !' + msg);
			}
		});
	}
},'&nbsp',{
	text : '取消',
	minWidth : 60,
	pressed : true,
	cls : 'x-btn-normal',
	handler : function() {
		addConfigureWindow_modify.hide();
	}
}];

var buttonCenter_modify = [{
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
		removeChooseWithClass_modify();
	}
}];

// 自定义数组删除
Array.prototype.remove = function (dx) {
	if (isNaN(dx) || dx > this.length) {
		return false;
	}
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != this[dx]) {
			this[n++] = this[i]
		}
	}
	this.length -= 1
}

function removeChooseWithClass_modify() {
	var configureLineSm_modify = configureLineGridPanel_modify.getSelectionModel();
	var store = configureLineGridPanel_modify.getStore();

	if (!configureLineSm_modify.hasSelection()) {
		Ext.Msg.alert(PROMPT, '请先选择需要删除的线路,至少一条！');
		return;
	}
	var selectLine = configureLineSm_modify.getSelections();
	
	var copyStore = new Array();

	store.each(function(item, index){
		copyStore[copyStore.length] = store.getAt(index);
	});
	
	var deleIndex = new Array();
	for (var i = 0; i < copyStore.length; i++) {
		Ext.each(selectLine, function(item){
			if (copyStore[i].data.ID == item.data.ID) {
				deleIndex[deleIndex.length] = i;
			}
		});
	}

	for (var i = 0; i < deleIndex.length; i ++) {
		copyStore.splice(deleIndex[i]-i, 1);
	}
	
	for (var i = 0; i < copyStore.length; i++) {
		if (i >= copyStore.length - 1)
			break;
		var firstLine = copyStore[i];
		var nextLine = copyStore[i + 1];
		
		if (firstLine.data.DESTINATION_CODE != nextLine.data.SOURCE_CODE) {
			Ext.Msg.alert('提示', '上一条目的网点必须和下一条始发网点相同');
			return;
		}
	}
	store.remove(configureLineSm_modify.getSelections());

	setChooseBackgroundColor_modify();
}
var configureLineSm_modify = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

var configureLineRecord_modify = Ext.data.Record.create([{
	name : ID,
	mapping : ID,
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
},{
	name : INPUT_TYPE,
	mapping : INPUT_TYPE,
	type : 'string'
},{
	name : VALID_STATUS,
	mapping : VALID_STATUS,
	type : 'string'
}]);

var configureLineStore_modify = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../driver/queryLineByLineConfigureId.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, configureLineRecord_modify)
});

var configureLineColumn_modify = new Ext.grid.ColumnModel({
	columns : [configureLineSm_modify,{
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

var configureLineGridPanel_modify = new Ext.grid.GridPanel({
	region : "center",
	tbar : [buttonCenter_modify],
	xtype : 'grid',
	height : 135,
	enableLocking : true,
	border : false,
	style : "border: 1px solid #8db2e3;",
	enableHdMenu : false,
	forceFit : true,
	sm : configureLineSm_modify,
	enableColumnResize : false,
	listeners : {
		dblclick : removeChooseWithClass_modify
	},
	store : configureLineStore_modify,
	cm : configureLineColumn_modify
});

var allRecordTop_modify = Ext.data.Record.create([{
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

var allLineStore_modify = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : QUERY_LINE_PATH
	}),
	reader : new Ext.data.JsonReader({
		root : 'resultMap.root',
		totalProperty : 'resultMap.totalSize'
	}, allRecordTop_modify),
	listeners : {
		beforeLoad : function() {
			var startTime = Ext.getCmp(KEY_ENTITY_START).getValue();
			this.baseParams['startTime'] = startTime;
			this.baseParams['inputType'] = "0";
			this.baseParams['validStatus'] = "1";
			this.baseParams['limit'] = allLinePanel_modify.getTopToolbar().getComponent(2).pageSize;
		},
		load : function() {
			setChooseBackgroundColor_modify();
		}
	}
});

var allLineSm_modify = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

var allLineButton_modify = [{
	xtype : 'label',
	style : 'font-weight:bold;font-size:12;color:#15428b;',
	text : '所有的线路'
},'-',new Ext.PagingToolbar({
	pageSize : 10,
	store : allLineStore_modify,
	displayInfo : true
}),'->',{
	xtype : "trigger",
	triggerClass : 'x-form-search-trigger',
	emptyText : '请输入出车时间',
	id : KEY_ENTITY_START,
	onTriggerClick : function() {
		allLineStore_modify.load({
			params : {
				start : 0,
				limit : 10
			},
			scope : allLineStore_modify
		});
	}
},'-',{
	text : '选为配班线路',
	minWidth : 60,
	cls : 'x-btn-normal',
	pressed : true,
	handler : function() {
		chooseWithClass_modify();
	}
}];

function chooseWithClass_modify() {
	var optionLine = allLinePanel_modify.getSelectionModel();
	var store = configureLineGridPanel_modify.getStore();

	if (!optionLine.hasSelection()) {
		Ext.Msg.alert(PROMPT, '请先选择需要添加的线路,至少一条！');
		return;
	}

	if (!validSourceCodeDestinationCodeSame(optionLine, configureLineGridPanel_modify)) {
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

			if (item.data.ID == configureLine.data.ID && item.data.SOURCE_CODE == configureLine.data.SOURCE_CODE) {
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
			ID : item.data.ID,
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

	sortAscending(data);

	store.insert(store.getCount(), data);

	optionLine.clearSelections();

	setChooseBackgroundColor_modify();
}

function setChooseBackgroundColor_modify() {
	var optionLineMap = new Ext.HashMap();

	configureLineGridPanel_modify.getStore().each(function(configureLine) {
		var key = configureLine.data.ID + "_" + configureLine.data.BELONG_ZONE_CODE;
		optionLineMap.put(key);
	});

	for ( var i = 0; i < allLinePanel_modify.getStore().getCount(); i++) {
		allLinePanel_modify.getView().getRow(i).style.backgroundColor = "#FFFFFF";
		allLinePanel_modify.getView().getRow(i).title = '';
	}

	allLinePanel_modify.getStore().each(function(item, i) {
		var key = item.data.ID + "_" + item.data.BELONG_ZONE_CODE;
		if (optionLineMap.containsKey(key)) {
			optionLineMap.remove(key);
			allLinePanel_modify.getView().getRow(i).style.backgroundColor = '#FF8888';
			allLinePanel_modify.getView().getRow(i).title = '数据已经存在';
		}
	});
}

var allLineColumn_modify = new Ext.grid.ColumnModel({
	columns : [allLineSm_modify,{
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

var allLinePanel_modify = new Ext.grid.GridPanel({
	region : 'south',
	height : 240,
	xtype : 'grid',
	border : false,
	style : "border-bottom: 1px solid #8db2e3;border-left: 1px solid #8db2e3;border-right: 1px solid #8db2e3;",
	loadMask : true,
	enableHdMenu : false,
	enableColumnResize : false,
	sm : allLineSm_modify,
	store : allLineStore_modify,
	listeners : {
		dblclick : chooseWithClass_modify
	},
	tbar : [allLineButton_modify],
	cm : allLineColumn_modify
});

var addFormPanel_modify = new Ext.form.FormPanel({
	layout : "column",
	xtype : "form",
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
		items : [configureLineGridPanel_modify,allLinePanel_modify]
	}]
});

var addConfigurePanel_modify = new Ext.Panel({
	layout : 'fit',
	tbar : buttonTop_modify,
	items : [addFormPanel_modify]
});

var addConfigureWindow_modify = new Ext.Window({
	title : '修改配班信息',
	height : 490,
	width : 880,
	layout : 'fit',
	closeAction : 'hide',
	tools : [{
		id : 'close',
		handler : function() {
			addFormPanel_modify.getForm().reset();

			addConfigureWindow_modify.hide();

			configureLineGridPanel_modify.store.removeAll();

			allLinePanel_modify.store.removeAll();
		}
	}],
	items : [addConfigurePanel_modify]
});

function validModifyMonthBeforeCurrentMonth() {
	return moment().startOf('month').isAfter(moment(Ext.util.Format.date(Ext.getCmp('query.month').getValue(), 'Y-m'), 'YYYY-MM'));
}

function validYeayMonthAfterCurrentTime(yeayMonth) {
	return moment(yeayMonth, 'YYYY-MM').startOf('month').isBefore(moment());
}

function validSelectIsNot(selectionRows) {
	return !selectionRows.hasSelection();
}

function checkSelectMultipleLines(selectionRows) {
	return selectionRows.getSelections().length > 1;
}

function validFail(selectionRows) {
	if (validSelectIsNot(selectionRows)) {
		Ext.Msg.alert('提示', '请选择一条记录！');
		return true;
	}

	if (checkSelectMultipleLines(selectionRows)) {
		Ext.Msg.alert('提示', '只能选择一条记录！');
		return true;
	}

	/*if (validYeayMonthAfterCurrentTime(selectionRows.getSelected().data.MONTH)) {
		Ext.Msg.alert('提示', '当月及之前的配班不能进行修改操作');
		return true;
	}*/

	if (selectionRows.getSelected().data.TYPE == 0) {
		Ext.Msg.alert('提示', '只能修改正常配班，机动配班需要通过修改机动配班按钮实现！');
		return true;
	}

	if (!Ext.isEmpty(selectionRows.getSelected().data.CONFIGURE_CODE)) {
		Ext.Msg.alert('提示', '已经排班的不能修改！');
		return true;
	}
}

function loadConfigureLine(record) {
	configureLineStore_modify.load({
		params : {
			lineConfigureId : record.data.ID,
			start : 0,
			limit : 10
		}
	});
}

function loadLineSetQueryParameterByRecord(record) {
	departmentCode = record.data.DEPT_CODE;
	lineConfigureId = record.data.ID;

	addFormPanel_modify.getForm().findField(ENTITY_DEPARTMENT_CODE).setValue(departmentCode);
	addFormPanel_modify.getForm().findField(YEAR_MONTH).setValue(Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m"));
	addFormPanel_modify.getForm().findField(ENTITY_ARRANGE_CODE).setValue(departmentCode + '-' + record.data.CODE);
	allLineStore_modify.baseParams['departmentCode'] = departmentCode;

	allLineStore_modify.load({
		params : {
			start : 0,
			limit : 10
		}
	});
}

var moidfyButton = new Ext.Button({
	text : '修改',
	cls : 'x-btn-normal',
	pressed : true,
	minWidth : 60,
	handler : function() {
		if (validFail(grid.getSelectionModel())) {
			return;
		}
		addConfigureWindow_modify.show();

		loadConfigureLine(grid.getSelectionModel().getSelected());

		setChooseBackgroundColor_modify();

		loadLineSetQueryParameterByRecord(grid.getSelectionModel().getSelected());
	}
});