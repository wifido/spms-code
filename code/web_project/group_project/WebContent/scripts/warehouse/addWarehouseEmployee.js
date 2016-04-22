//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var IS_HAVE_COMMISSION = '是否同步考勤到计提系统';
var EMPLOYEE_DEPARTMENT_CODE = 'employeeDepartmentCode';
var EMPLOYEE_AREA_CODE = 'employeeAreaCode';

function isRepeat(arr) {
	var hash = {};
	for ( var i in arr) {
		if (hash[arr[i]]) {
			return true;
		}
		hash[arr[i]] = true;
	}
	return false;
}

function updateEmployeeValidData() {
	var departmentCode = Ext.getCmp("DEPT_CODE").getValue();
	var dynamicDeptIds = [ departmentCode ];
	var errorMsg;
	Ext.select("input[name^=DYNAMIC_DEPT_CODE]").each(
			function(v, i) {
				if (null != v.getValue() && '' != v.getValue()) {
					var dynamicDepartmentCode = v.getValue().split('/')[0];
					var url = '../warehouse/validDynamicDepartmentCode.action?employeeDepartmentCode=' + departmentCode + '&dynamicDepartmentCode='
							+ dynamicDepartmentCode;

					var obj;
					dynamicDeptIds.push(dynamicDepartmentCode);

					if (window.ActiveXObject) {
						obj = new ActiveXObject('Microsoft.XMLHTTP');
					} else if (window.XMLHttpRequest) {
						obj = new XMLHttpRequest();
					}

					obj.open('post', url, false);
					obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
					obj.send(null);

					var response = Ext.decode(obj.responseText);
					if (response.dataMap["success"] == "false") {
						v.addClass("x-form-invalid");
						errorMsg = "网点代码" + dynamicDepartmentCode + "无效,该网点在系统中不存在！";
						return;
					}
				}
			});

	if (errorMsg != null) {
		Ext.Msg.alert('提示', errorMsg);
		return false;
	}

	if (isRepeat(dynamicDeptIds)) {
		Ext.Msg.alert('提示', '网点代码或机动网点代码不能重复!');
		return false;
	}
	dynamicDeptIds.remove(0);
	return true;
}

var formPanel = new Ext.FormPanel({
	id : 'addEmployeeForm',
	labelAlign : 'left',
	buttonAlign : 'right',
	frame : true,
	layout : 'column',
	monitorValid : true,
	items : [ {
		layout : 'form',
		style : {
			paddingLeft : '5px'
		},
		defaults : {
			width : 100
		},
		columnWidth : .4,
		items : [ {
			name : 'DEPT_CODE',
			id : 'DEPT_CODE',
			xtype : "textfield",
			disabled : true,
			fieldLabel : "网点代码",
			editable : false
		}, {
			name : EMPLOYEE_DEPARTMENT_CODE,
			id : EMPLOYEE_DEPARTMENT_CODE,
			xtype : "textfield",
			hidden : true
		}, {
			name : EMPLOYEE_AREA_CODE,
			id : EMPLOYEE_AREA_CODE,
			xtype : "textfield",
			hidden : true
		}, {
			name : 'EMP_NAME',
			id : 'EMP_NAME',
			xtype : "textfield",
			disabled : true,
			fieldLabel : "姓名"
		}, {
			name : 'EMP_ID',
			id : 'EMP_ID',
			xtype : "hidden"
		}, {
			name : 'EMP_CODE',
			id : 'EMP_CODE',
			readOnly : true,
			xtype : "textfield",
			fieldLabel : "工号"
		}, {
			name : 'EMP_DUTY_NAME',
			id : 'EMP_DUTY_NAME',
			xtype : "textfield",
			disabled : true,
			readOnly : true,
			fieldLabel : "岗位"
		}, {
			xtype : "datefield",
			name : 'SF_DATE',
			id : 'SF_DATE',
			editable : false,
			disabled : true,
			format : 'Y-m-d',
			fieldLabel : "入职日期"
		} ]
	}, {
		layout : 'form',
		style : {
			paddingLeft : '10px'
		},
		defaults : {
			width : 100
		},
		columnWidth : .45,
		items : [ {
			name : 'DYNAMIC_DEPT_CODE',
			id : 'DYNAMIC_DEPT_CODE',
			xtype : "textfield",
			fieldLabel : "机动网点",
			onTriggerClick : function() {
				var _window = new Ext.sf_dept_window({
					callBackInput : 'DYNAMIC_DEPT_CODE'
				});
				_window.show(this);
			}
		}, {
			xtype : "checkbox",
			name : 'IS_HAVE_COMMISSION',
			id : 'IS_HAVE_COMMISSION',
			labelStyle : 'width:100px; margin-Top: 0',
			fieldLabel : IS_HAVE_COMMISSION
		} ]
	}, {
		layout : 'form',
		columnWidth : .05,
		defaults : {
			width : 25
		},
		items : [ {
			xtype : 'button',
			id : 'addDynamicEmployeeBtn',
			icon : '../ext-3.4.0/resources/images/default/dd/drop-add.gif',
			handler : function() {
				addDept();
			}
		} ]

	}, {
		layout : 'form',
		columnWidth : .05,
		items : [ {
			xtype : 'button',
			id : 'deleteAllBtn',
			icon : '../ext-3.4.0/resources/images/default/dd/delete.gif',
			handler : function() {
				Ext.getCmp("DYNAMIC_DEPT_CODE").setValue('');
			}
		} ]

	} ]
});

function addDept(deptText, deptId) {
	var length = Ext.getCmp("addDynamicEmployeeBtn").ownerCt.items.length;
	if (length > 3) {
		Ext.Msg.alert('提示', '机动网点最多添加四个！');
		return false;
	}

	var dynamicDeptPanel = Ext.getCmp("DYNAMIC_DEPT_CODE").ownerCt;
	var rand = parseInt(10000 * Math.random());
	var inputName = 'DYNAMIC_DEPT_CODE_' + rand;
	var _textfield = new Ext.form.TextField({
		name : inputName,
		id : inputName,
		xtype : "dynamicTextFiled",
		onTriggerClick : function() {
			var _window = new Ext.sf_dept_window({
				callBackInput : inputName
			});
			_window.show(this);
		}
	});
	Ext.getCmp(inputName).setValue(deptText);
	dynamicDeptPanel.insert(1, _textfield);
	dynamicDeptPanel.doLayout();

	var addDynamicDeptBtnPanel = Ext.getCmp("addDynamicEmployeeBtn").ownerCt;
	var delBtn = new Ext.Button({
		xtype : "delteButton",
		style : {
			marginTop : '4px'
		},
		icon : '../ext-3.4.0/resources/images/default/dd/delete.gif',
		handler : function() {
			addDynamicDeptBtnPanel.remove(this);
			dynamicDeptPanel.remove(Ext.getCmp("DYNAMIC_DEPT_CODE_" + rand));

		}
	});

	addDynamicDeptBtnPanel.insert(1, delBtn);
	addDynamicDeptBtnPanel.doLayout();

}

var baiscInfoPane = new Ext.Panel({
	frame : true,
	region : 'center',
	items : [ formPanel ]
});

var add_window = new Ext.Window({
	title : '人员信息修改',
	width : 580,
	height : 250,
	closeAction : 'hide',
	layout : 'border',
	items : [ baiscInfoPane ],
	buttons : [ {
		text : "保存",
		handler : function() {
			updateEmployee();
		}
	}, {
		text : "取消",
		handler : function() {
			add_window.hide();
		}
	} ]
});

function updateEmployee() {

	// alert(Ext.getCmp('addEmployeeForm').form.getValues());
	if (!updateEmployeeValidData()) {
		return false;
	}

	/*
	 * Ext.Ajax.request({ timeout : 30000, url :
	 * 'update_warehouseEmployee.action', params :
	 * Ext.getCmp('addEmployeeForm').form.getValues(), success :
	 * function(response, config) { var rep = Ext.decode(response.responseText);
	 * if (Ext.isEmpty(rep.msg)) { Ext.Msg.alert('提示', '修改成功！'); store.load({
	 * params : { start : 0, limit : 20 } }); add_window.hide(); } else {
	 * Ext.Msg.alert('提示', rep.msg); } }, failure: function(resp,opts) { var
	 * respText = Ext.util.JSON.decode(resp.responseText); Ext.Msg.alert('错误',
	 * respText.error); } });
	 */
	var is_have_commission = Ext.getCmp("IS_HAVE_COMMISSION").getValue();
	if (is_have_commission) {
		is_have_commission = '1';
	} else {
		is_have_commission = '0';
	}
	Ext.getCmp('addEmployeeForm').form.submit({
		url : 'update_warehouseEmployee.action',
		waitMsg : '正在提交数据',
		waitTitle : '提示',
		method : "POST",
		timeout : 30000,
		params : {
			IS_HAVE_COMMISSION : is_have_commission
		},
		success : function(form, action) {
			Ext.Msg.alert('提示', "修改成功！");
			store.load({
				params : {
					start : 0,
					limit : 20
				}
			});
			add_window.hide();
		},
		failure : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);// 就可以取出来。如果是数组，那么很简单
			Ext.Msg.alert('提示', "修改失败！" + result.error);
		}
	});

}

var btnUpdateEmployee = new Ext.Button({
	text : "人员信息修改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length > 1) {
			Ext.Msg.alert("提示", "只能选择一条数据!");
			return false;
		}
		if (records.length < 1) {
			Ext.Msg.alert("提示", "请能选择一条数据!");
			return false;
		}
		var departmentCode = Ext.getCmp("query.branchCode").getValue();
		Ext.Ajax.request({
			url : '../warehouse/validateAuthority.action',
			method : 'POST',
			params : {
				dept_code : departmentCode
			},
			success : function(res, config) {
				var data = Ext.decode(res.responseText);
				if (data.dataMap.success == false) {
					Ext.Msg.alert("提示", "暂无该网点权限！");
				} else {
					add_window.show();
					var record = grid.getSelectionModel().getSelected();
					Ext.getCmp("EMP_ID").setValue(record.data['EMP_ID']);
					Ext.getCmp("EMP_CODE").setValue(record.data['EMP_CODE']);
					Ext.getCmp("EMP_NAME").setValue(record.data['EMP_NAME']);
					Ext.getCmp("SF_DATE").setValue(record.data['SF_DATE']);
					Ext.getCmp("EMP_DUTY_NAME").setValue(record.data['EMP_DUTY_NAME']);
					Ext.getCmp("DEPT_CODE").setValue(record.data['DEPT_CODE']);
					Ext.getCmp(EMPLOYEE_DEPARTMENT_CODE).setValue(record.data['DEPT_CODE']);
					Ext.getCmp(EMPLOYEE_AREA_CODE).setValue(record.data['AREA_CODE']);

					if (record.data['IS_HAVE_COMMISSION'] == '是') {
						document.getElementById("IS_HAVE_COMMISSION").checked = true;
					}
					if (record.data['IS_HAVE_COMMISSION'] == '否') {
						document.getElementById("IS_HAVE_COMMISSION").checked = false;
					}

					var dynamicDeptIds = record.data['MOBILE_NETWORK'].split(",");

					initDynamicDeptData(dynamicDeptIds);
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});

function initDynamicDeptData(dynamicDeptIds) {
	Ext.getCmp("DYNAMIC_DEPT_CODE").setValue(dynamicDeptIds[0]);

	var dynamicDeptPanel = Ext.getCmp("DYNAMIC_DEPT_CODE").ownerCt;
	Ext.each(dynamicDeptPanel.find('xtype', 'dynamicTextFiled'), function(v, i) {
		dynamicDeptPanel.remove(v);
	});
	dynamicDeptPanel.doLayout();

	var addDynamicDeptBtnPanel = Ext.getCmp("addDynamicEmployeeBtn").ownerCt;
	Ext.each(addDynamicDeptBtnPanel.find('xtype', 'delteButton'), function(v, i) {
		addDynamicDeptBtnPanel.remove(v);
	});
	addDynamicDeptBtnPanel.doLayout();

	if (dynamicDeptIds.length > 1) {
		for ( var i = 1; i < dynamicDeptIds.length; i++) {
			addDept(dynamicDeptIds[i], '');
		}
	}

}
