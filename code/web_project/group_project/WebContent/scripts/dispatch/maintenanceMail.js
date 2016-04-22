//<%@ page language="java" contentType="text/html; charset=utf-8"%>
// 查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		query();
	}
});

// 新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		add();
	}
});

var btnEdit = new Ext.Button({
	text : "修 改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		edit();
	}
});

var btnDelete = new Ext.Button({
	text : "删除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		remove();
	}
});

var tbar = [];
addBar('<app:isPermission code = "/dispatch/maintenanceMail_query.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/dispatch/maintenanceMail_add.action">a</app:isPermission>', btnAdd);
addBar('<app:isPermission code = "/dispatch/maintenanceMail_edit.action">a</app:isPermission>', btnEdit);
addBar('<app:isPermission code = "/dispatch/maintenanceMail_delete.action">a</app:isPermission>', btnDelete);
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
	height : 100,
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
				xtype : 'trigger',
				triggerClass : 'x-form-search-trigger',
				id : 'query.departmentCode',
				fieldLabel : '<font color=red>网点代码*</font>',
				anchor : '90%',
				onTriggerClick : function() {
					var _window = new Ext.sf_dept_window({
						branchDepartmentEditable : true,
						isDispatch : true,
						callBackInput : "query.departmentCode"
					});
					_window.show(this);
				}
			} ]
		}, {
			columnWidth : .4,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [ {
				xtype : 'textfield',
				id : 'query.emailAccount',
				typeAhead : true,
				triggerAction : 'all',
				lazyRender : true,
				mode : 'local',
				fieldLabel : '维护人邮箱',
				anchor : '90%'
			} ]
		} ]
	} ]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns : [ sm, {
		header : "ID",
		dataIndex : 'id',
		hidden : true
	}, {
		header : '地区代码',
		sortable : true,
		dataIndex : 'areaCode',
		align : "center"
	}, {
		header : '分点部代码',
		sortable : true,
		dataIndex : 'divisionCode',
		align : "center"
	}, {
		header : '网点代码',
		sortable : true,
		dataIndex : 'departmentCode',
		width : 140,
		align : "center"
	}, {
		header : '维护人邮箱',
		sortable : true,
		dataIndex : 'emailAccount',
		width : 200,
		align : "center"
	} ]
});

// 数据格构建
var record = Ext.data.Record.create([ {
	name : 'areaCode',
	mapping : 'areaCode',
	type : 'string'
}, {
	name : "id",
	mapping : 'id',
	type : 'string'
}, {
	name : 'divisionCode',
	mapping : 'divisionCode',
	type : 'string'
}, {
	name : 'departmentCode',
	mapping : 'departmentCode',
	type : 'string'
}, {
	name : 'emailAccount',
	mapping : 'emailAccount',
	type : 'string'
} ]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../dispatch/maintenanceMail_query.action'
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
	tbar : pageBar
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

// 查询方法
var query = function(node) {
	if (Ext.getCmp("query.departmentCode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	store.setBaseParam("DEPARTMENT_CODE", getDepartmentCodes());// checkedDepartmentListForSearching.toString()/Ext.getCmp('query.AreaCode').getValue().split('/')[0]
	store.setBaseParam("EMAIL_ACCOUNT", Ext.getCmp('query.emailAccount').getValue());

	store.load({
		params : {
			start : 0,
			limit : 20
		}
	});
};

function getDepartmentCodes() {
	return Ext.getCmp("query.departmentCode").getValue().split("/")[0];
}

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout : "border",
		items : [ centerPanel ]
	});
});

var add = function() {
	if (Ext.getCmp("query.departmentCode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}

	Ext.Ajax.request({
		url : '../dispatch/maintenanceMail_validExists.action',
		method : 'POST',
		params : {
			DEPARTMENT_CODE : getDepartmentCodes()
		},
		success : function(res, config) {
			var data = Ext.decode(res.responseText);
			if (data.dataMap.success) {
				Ext.Msg.alert("提示", "该网点已存在维护人邮箱，请点击修改按钮修改！");
			} else {
				win.show();
				win.setTitle('新增维护人邮箱');
				Ext.getCmp('dataForm').getForm().reset();
				Ext.getCmp('win_departmentCode').setValue(getDepartmentCodes());
			}
		},
		failure : function() {
			Ext.Msg.alert("提示", "暂时无法新增，请稍后再试！");
		}
	});
}

var edit = function() {

	var records = grid.getSelectionModel().getSelections();
	var record = grid.getSelectionModel().getSelected();

	if (records.length == 1) {
		win.show();
		win.setTitle('修改信息');
		Ext.getCmp('dataForm').getForm().reset();

		Ext.getCmp('win_id').setValue(record.get('id'));
		Ext.getCmp('win_departmentCode').setValue(record.get('departmentCode'));
		Ext.getCmp('win_emailAccount').setValue(record.get('emailAccount'));
	} else {
		if (records.length == 0) {
			Ext.Msg.alert('提示', '请选择一条记录');
			return;
		}
		if (records.length > 1) {
			Ext.Msg.alert('提示', '修改时只能选择一条记录');
			return;
		}
	}
}

var win = new Ext.Window({
	layout : 'fit',
	width : 450,
	height : 200,
	closeAction : 'hide',
	modal : true,
	fbar : [ {
		text : '保存',
		ctCls : 'x-btn-over',
		handler : saveForm
	}, {
		text : '取消',
		ctCls : 'x-btn-over',
		handler : function() {
			win.hide();
		}
	} ],
	items : [ {
		id : 'dataForm',
		url : 'maintenanceMail_saveOrUpdate.action',
		xtype : 'form',
		frame : true,
		labelAlign : 'right',
		items : [ {
			layout : 'column',
			items : [ {
				columnWidth : .9,
				border : false,
				layout : 'form',
				items : [ {
					id : 'win_id',
					name : 'maintenanceMail.id',
					xtype : 'hidden'
				}, {
					xtype : 'textfield',
					readOnly : true,
					name : 'maintenanceMail.departmentCode',
					id : 'win_departmentCode',
					allowBlank : false,
					fieldLabel : '<font color=red>网点代码*</font>'
				} ]
			}, {
				layout : 'form',
				columnWidth : .9,
				items : [ {
					id : 'win_emailAccount',
					name : 'maintenanceMail.emailAccount',
					xtype : 'textarea',
					fieldLabel : '<font color=red>维护人邮箱*</font>',
					width : 250,
					maxLength : 900,
					maxLengthText : '该输入项的最大长度是300汉字',
					allowBlank : false
				} ]
			} ]
		} ]
	} ]
});

/**
 * 保存
 */
function saveForm() {
	var emailAccounts  = Ext.getCmp('win_emailAccount').getValue().split(",");
	var msg = '';
	for(var i = 0; i < emailAccounts.length; i ++){
		var j = i + 1;
		if(isEmail(emailAccounts[i])) {
			if (emailAccounts[i].indexOf('sf-express.com') == -1){
				msg = msg + '第'+j+ '个不是公司邮箱，请填写公司内部邮箱！';
			}
		}else{
			msg = msg + '第'+j+ '个邮箱格式错误，请填写正确的邮箱！';
		}
	}
	
	if ('' != msg) {
		Ext.Msg.alert('提示', msg);
	} else {
		Ext.getCmp('dataForm').getForm().submit({
			//在成功的情况下出现提示,属于用户操作不正确
			success : function(form, action) {
				if (!action.result.msg) {
					Ext.Msg.alert('提示', '操作成功');
					store.reload();
					win.hide();
				} else {
					Ext.Msg.alert('提示', action.result.msg);
				}
			},
			//超时时间5分钟
			timeout : 5 * 60,
			waitMsg : '正在执行操作...',
			waitTitle : '请稍后'
		});
	}
}

function isEmail(str) {
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	return reg.test(str);
} 

/**
 * 删除
 */
var remove = function() {
	var records = grid.getSelectionModel().getSelections();
	if (records.length == 1) {
		var record = grid.getSelectionModel().getSelected();
		Ext.Msg.confirm('提示', '您确定要删除记录吗？', function(btn) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : 'maintenanceMail_remove.action',
					params : {
						'id' : record.get('id')
					},
					success : function(response, opts) {
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.msg != null && result.msg != '') {
							Ext.Msg.alert('提示', result.msg);
						} else {
							store.reload();
							Ext.Msg.alert('提示', '操作成功');
						}
					},
				});
			}
		});
	} else {
		Ext.Msg.alert('提示', '请选择一条记录！');
	}
}