// <%@ page language="java" contentType="text/html; charset=utf-8"%>
var mustMark = '<font color=red>*</font>';
var COL_END_TIME = '收车时间';
var validTimeFormat = /^((0\d{1}|1\d{1}|2[0-3])([0-5]\d{1})|2400)$/;

function addMustMark(fieldLabel) {
	return fieldLabel + mustMark;
}

var addFormPanelForMobileNetWork = new Ext.form.FormPanel({
	id: 'addFormPanelForMobileNetWork',
	width: 450,
	height: 200,
	frame: true,
	items: [
		{
		    columnWidth: 1,
		    layout: 'column',
		    labelWidth: 70,
		    items: [
		        {
		        	columnWidth: .5,
		        	layout: 'form',
		        	labelAlign: 'right',
		        	items: [
		                {
		                    xtype: 'textfield',
		                    fieldLabel: '网点代码<font color=red>*</font>',
		                    allowBlank: false,
		                    name: '_departmentCode',
		                    id: '_departmentCode',
		                    readOnly: true,
		                    anchor: '90%'
		                },
						{
							xtype: 'textfield',
							fieldLabel: '出车时间<lable style="color: red">*</lable>',
							allowBlank: false,
							name: '_startTime',
							id: '_startTime',
							maxLength: 4,
							regex: validTimeFormat,
							validator: function(value) {
								if (!validTimeFormat.test(value)) {
									return '输入值非法';
								}
								return true;
							},
							anchor: '90%'
						},
		                {
							xtype: 'datefield',
							fieldLabel: '月份<font color=red>*</font>',
							format: 'Y-m',
							editable: false,
							allowBlank: false,
							plugins: 'monthPickerPlugin',
							readOnly: true,
							name: '_yearMonth',
							id: '_yearMonth',
							anchor: '90%',
							listeners: {
								'select' : function() {
									setClassCode();
								}
							}
						}
		        	]
		        },
		        {
		        	columnWidth: .5,
		        	layout: 'form',
		        	labelAlign: 'right',
		        	items: [
		                {
		                    xtype: 'textfield',
		                    fieldLabel: '班次代码<font color=red>*</font>',
		                    allowBlank: false,
		                    name: '_classCode',
		                    id: '_classCode',
		                    readOnly: true,
		                    anchor: '90%'
		                },
		                {
							xtype: 'textfield',
							fieldLabel: addMustMark(COL_END_TIME),
							allowBlank: false,
							name: '_endTime',
							id: '_endTime',
							maxLength: 4,
							regex: validTimeFormat,
							validator: function(value) {
								
								if (!validTimeFormat.test(value)) {
									return '输入值非法';
								}
								return true;
							},
							anchor: '90%'
						},
		                {
							xtype: 'combo',
							fieldLabel: '有效性<lable style="color: red">*</lable>',
							id: '_validState',
							mode: 'local',
							triggerAction: "all",
							border: false,
							hiddenName: 'validState',
							forceSelection: true,
							allowBlank: false,
							store: [['1','有效'],['0','无效']],
							name: '_validState',
		                	anchor: '90%'
		                },
		                {
		                	xtype: 'textfield',
		                	hidden: true,
		                	name: 'configureId'
		                }
		        	]
		        }
		    ]
		}
	]
});

var saveMobileNetWorkBtn = new Ext.Button({
	text: '保存',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		if (!addFormPanelForMobileNetWork.getForm().isValid()) {
			return;
		}
		if('0000' == addFormPanelForMobileNetWork.getForm().findField('_endTime').getValue()){
			Ext.Msg.alert('提示', '收车时间不能为00:00 !');
			return;
		}
		if ('2400' == addFormPanelForMobileNetWork.getForm().findField('_startTime').getValue()) {
			addFormPanelForMobileNetWork.getForm().findField('_startTime').setValue('2359');
		}
		if ('2400' == addFormPanelForMobileNetWork.getForm().findField('_endTime').getValue()) {
			addFormPanelForMobileNetWork.getForm().findField('_endTime').setValue('2359');
		}
		if (addFormPanelForMobileNetWork.getForm().findField('_startTime').getValue() == addFormPanelForMobileNetWork.getForm().findField('_endTime').getValue()) {
			Ext.Msg.alert('提示', '出车时间不能等于收车时间');
			return;
		}
		
		Ext.Ajax.request({
			url: "../driver/validClassesCode.action",
			params: {
				'departmentCode': Ext.getCmp('_classCode').getValue().split('-')[0],
				'code': Ext.getCmp('_classCode').getValue().split('-')[1],
				'yearMonth': Ext.util.Format.date(Ext.getCmp('_yearMonth').getValue(), "Y-m")
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
				addFormPanelForMobileNetWork.getForm().submit({
					waitMsg: '正在提交数据...',
					waitTitle: '提示',
					method: "POST",
					timeout: 30000,
					url: '../driver/addMobileNetwork_lineConfigure.action',
					success: function() {
						Ext.Msg.alert('提示', '操作成功！');
						addFormPanelForMobileNetWork.getForm().reset();
						addPanel.hide();
					},
					failure: function(form, action) {
						Ext.Msg.alert('提示', '操作失败！保存发生异常！');
					}
				});
			}
		});
	}
});

var addPanel = new Ext.Window({
    title: '新增机动配班',
	plain: true,
	width: 450,
	height: 200,
	modal: true,
	resizable: false,
	closeAction: 'hide',
	plain: true,
	items: [addFormPanelForMobileNetWork],
	buttons: [saveMobileNetWorkBtn]
});

function validDepartmentIsNull(departmentCode) {
	return departmentCode == '';
}

function isAreaCode(departmentCode) {
	var flag = false;
	Ext.Ajax.request({
		url: "checkDepartment_scheduling.action",
		async: false,
		params: {
			departmentCode: departmentCode.split('/')[0]
		},
		success: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			if (result.success) {
				flag = true;
			}
		},
		failure: function(response) {
		}
	});
	return flag;
}

var addMobileNetwork = new Ext.Button({
	text: '新增机动配班',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp('query.deptId').getValue();
		
		if (validDepartmentIsNull(departmentCode)) {
			Ext.Msg.alert('提示', '请选择网点信息');
			return;
		}
		if (isAreaCode(departmentCode)) {
			Ext.Msg.alert('提示', "只能在区部以下网点新增排班！");
			return;
		}
		
		if (validModifyMonthBeforeCurrentMonth()) {
			Ext.Msg.alert('提示', '当月之前的配班不能进行新增操作');
			return;
		}
		
		addPanel.setTitle('新增机动配班');
		addPanel.show();
		
		addFormPanelForMobileNetWork.getForm().reset();
		addFormPanelForMobileNetWork.getForm().setValues({
			_departmentCode: departmentCode,
			_yearMonth: Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m"),
			_validState: 1
		});
		setClassCode();
	}
});

function setClassCode() {
	var department = Ext.getCmp('query.deptId').getValue();
	
	Ext.Ajax.request({
		url: "queryClassesCode.action",
		params: {
			'departmentCode': department.split('/')[0],
			'type': '0',
			'yearMonth': Ext.util.Format.date(Ext.getCmp('_yearMonth').getValue(), "Y-m")
		},
		success: function(response) {
			var result = Ext.decode(response.responseText)
			if (Ext.isEmpty(result.classesCode)) {
				Ext.Msg.alert(PROMPT, '程序发生异常,请重新获取班次代码！');
				return;
			}
			if (!Ext.isEmpty(addFormPanelForMobileNetWork) && addFormPanelForMobileNetWork.isVisible()) {
				addFormPanelForMobileNetWork.getForm().findField('_classCode').setValue(result.classesCode);
			}
		}
	});
}
