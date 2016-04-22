// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var treePanel = new Ext.tree.TreePanel(
		{
			region : "west",
			margins : "1 1 1 1",
			width : 245,
			title : "网点信息",
			collapsible : true,
			autoScroll : true,
			root : new Ext.tree.AsyncTreeNode(
					{
						id : '0',
						text : '顺丰速运',
						loader : new Ext.tree.TreeLoader(
								{
									dataUrl : "../common/getSfDeptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
								})
					}),
			listeners : {
				beforeclick : function(node, e) {
					if (hasNotSelectedDepartment(node)) {
						return;
					}
					Ext.getCmp("query.departmentCode").setValue(node.text);
				}
			}
		});

function hasNotSelectedDepartment(node) {
	return node == null || node.id == 0;
}

function formatDate(date) {
	return date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate();
}

var exportButton = new Ext.Button({
	text : "导出",
	pressed : true,
	cls : "x-btn-normal",
	minWidth : 60,
	handler : function() {
		var data = validataForm();
		if (!data.flag) {
			return;
		}
		console.log(data);
		var messageDialog = new Ext.LoadMask(Ext.getBody(), {
			msg : "正在导出..."
		});
		messageDialog.show();

		Ext.Ajax.request({
			url : "../driver/exportReport.action",
			method : "POST",
			params : data,
			success : function(response) {
				messageDialog.hide();

				var obj = Ext.decode(response.responseText);
				console.log(obj);

				if (obj.exprotResult.isSuccessed) {
					window.location = "../common/downloadReportFile.action?"
							+ encodeURI(encodeURI(obj.exprotResult.filePath));
				} else {
					Ext.Msg.alert("错误：导出报表失败。", (obj.exprotResult.message));
				}
			}
		});
	}
});

var operationButton = [];
addBar("<app:isPermission code = '/driver/export.action'>a</app:isPermission>",
		exportButton);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		operationButton.push("-");
		operationButton.push(button);
	}
}

function validataForm() {
	var data = new Object();
	var temp = codeInput.getCode();
	if (temp.flag == true) {
		var code = temp.data;
		data.departmentCode = code;
	} else {
		Ext.Msg.alert("错误", (temp.msg));
		data.flag = false;
		return data;
	}

	temp = myTimePanel.getTimePeriod();
	data.startTime = temp.startTime.format("YYYY-MM-DD");
	data.endTime = temp.endTime.format("YYYY-MM-DD");

	temp = errorTypeInput.getErrorType();
	if (temp.flag == true) {
		data.errorType = temp.data;
	} else {
		Ext.Msg.alert("错误", temp.msg);
		data.flag = false;
		return data;
	}
	data.flag = true;
	return data;
}

function validate(obj) {
	if (typeof (obj) == "undefined") {
		obj = this;
	}
	var result = new Object();
	result.flag = obj.validate();
	result.data = obj.getValue();
	result.msg = obj.getActiveError();
	return result;
}

var codeInput = new Ext.form.TextField({
	xtype : "textField",
	fieldLabel : "网点代码<font color=red>*</font>",
	readOnly : true,
	id : "query.departmentCode",
	anchor : "90%",
	allowBlank : false,
	blankText : "请选择需要导出的网点",
	getCode : function() {
		var result = validate(codeInput);
		if (result.data != "") {
			var code = result.data.substring(0, result.data.indexOf("/"));
			result.data = code;
			if (code == null || code == "") {
				result.flag = false;
				result.msg = "请选择需要导出的网点";
			}
		}
		return result;
	}
});

var startTimeInput = new Ext.form.DateField({
	xtype : "datefield",
	allowBlank : false,
	fieldLabel : "开始时间<font color=red>*</font>",
	id : "query.startTime",
	blankText : "请选择开始时间",
	anchor : "90%",
	getStartTime : validate
});

var endTimeInput = new Ext.form.DateField({
	xtype : "datefield",
	allowBlank : false,
	fieldLabel : "结束时间<font color=red>*</font>",
	id : "query.endTime",
	blankText : "请选择结束时间",
	anchor : "90%",
	getEndTime : validate
});

var monthInput = new Ext.form.DateField({
	xtype : 'datefield',
	fieldLabel : '月份<font color=red>*</font>',
	id : 'query.yeaMonth',
	format : 'Y-m',
	editable : false,
	value : new Date,
	plugins : 'monthPickerPlugin',
	anchor : '90%',
	getEndTime : validate,
	getDate : function() {
		return date = moment(this.getValue());
	}
});

function dp() {
	WdatePicker({
		dateFmt : 'yyyy年-WW周   MM-dd',
		isShowWeek : true,
		isShowToday : false,
		isShowClear : false,
		errDealMode : 8,
		onpicked : function(dp) {
			$dp.$('selectedDay').value = $dp.cal.getP('y', 'yyyy') + "-"
					+ $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd');
			$dp.$('weekOfYear').value = $dp.cal.getP('y', 'yyyy') + "-"
					+ $dp.cal.getP('W', 'W');
		}
	});
}

var sm_week = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});

var weekInput = new Ext.form.Label(
		{
			xtype : 'label',
			id : 'query.yearWeek',
			fieldLabel : '周数<font color=red>*</font>',
			editable : false,
			html : '<input type="text" class="Wdate" readonly = true onclick="dp();"  id="weekPicker" /><input type="hidden" size="2" id="selectedDay" name="selectedDay" /><input type="hidden" size="2" id="weekOfYear" name="weekOfYear" />',
			anchor : '90%',
			getDate : function() {
				var date = moment(Ext.get("weekOfYear").getValue(), "YYYY-WW");
				return date;
			}
		});

var queryTypeArray = {
	"item" : [ [ 1, "按月" ], [ 2, "按周" ] ],
	"text" : [ "months", "weeks" ]
};

var queryTypeInput = new Ext.form.ComboBox({
	mode : "local",
	allowBlank : false,
	fieldLabel : "查询类型<font color=red>*</font>",
	autoHeight : true,
	blankText : "请选择查询类型",
	typeAhead : true,
	triggerAction : "all",
	value : '1',
	editable : false,
	forceSelection : true,

	anchor : "90%",
	store : queryTypeArray.item,
	listeners : {
		"select" : function(combo, record, index) {
			var type = (record.get("field1"));
			myTimePanel.select(type);
		}
	}
});

var timeInputPanel = new Ext.Panel({
	columnWidth : .4,
	layout : "form",
	items : [ queryTypeInput, monthInput, weekInput ]
});

function timePanel(monthInputExt, weekInputExt) {
	this.queryType;
	this.monthInputExt = monthInputExt;
	this.weekInputExt = weekInputExt;
	this.selected;
	this.typeArray = queryTypeArray.text;
	this.selectMonth = function() {
		this.monthInputExt.show();
		this.weekInputExt.hide();
		this.queryType = 1;
		this.selected = this.monthInputExt;
	}
	this.selectWeek = function() {
		this.monthInputExt.hide();
		this.weekInputExt.show();
		this.queryType = 2;
		this.selected = this.weekInputExt;
	}
	this.select = function(queryType) {
		if (queryType == 1) {
			return this.selectMonth();
		} else if (queryType == 2) {
			return this.selectWeek();
		}
	}
	this.getTimePeriod = function() {
		var date = moment(this.selected.getDate());
		var result = new Object();
		date.startOf(this.typeArray[this.queryType - 1]);
		result.startTime = moment(date);
		date.endOf(this.typeArray[this.queryType - 1]);
		result.endTime = moment(date);
		return result;
	}
}

var allType = [ 0, 1, 2, 3, 4 ];
var errorArray = [ [ allType, "全部" ], [ 1, "排班未出勤" ], [ 2, "出勤未排班" ],
		[ 3, "出勤线路少于排班线路" ], [ 4, "出勤线路超出排班线路" ] ];
var errorTypeInput = new Ext.form.ComboBox({
	mode : "local",
	allowBlank : false,
	fieldLabel : "异常类型<font color=red>*</font>",
	autoHeight : true,
	blankText : "请选择异常类型",
	typeAhead : true,
	triggerAction : "all",
	editable : false,
	forceSelection : true,
	value : allType,
	anchor : "90%",
	store : errorArray,
	valueField : 'id',
	displayField : 'text',
	getErrorType : validate
});

var errorTypePanel = new Ext.Panel({
	columnWidth : .4,
	layout : "form",
	items : [ errorTypeInput ]
});

var codePanel = new Ext.Panel({
	columnWidth : .4,
	layout : "form",
	items : [ codeInput, errorTypeInput ]
});

var conditionPanel = new Ext.Panel({
	region : "north",
	layout : "column",
	/* height: 120, */
	frame : true,
	tbar : operationButton,
	items : [ {
		xtype : "fieldset",
		style : "margin-top:5px;",
		title : "查询条件",
		columnWidth : 1,
		frame : true,
		layout : "column",
		items : [ codePanel, timeInputPanel ]
	} ]
});

var centerPanel = new Ext.Panel({
	region : "center",
	items : [ conditionPanel ],
});

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout : "border",
		items : [ treePanel, centerPanel ]
	});

	moment.locale("fr", {
		week : {
			dow : 1
		}
	});
	var year = moment().year();
	var weeks = moment().isoWeek();
	Ext.get('weekOfYear').set({
		value : year + '-' + weeks
	});
	Ext.get('selectedDay').set({
		value : moment().format('YYYY-MM-DD')
	});
	Ext.get('weekPicker').set({
		value : year + '年-' + weeks + '周'
	});
	myTimePanel = new timePanel(monthInput, weekInput);
	myTimePanel.select(queryTypeInput.getValue());
});