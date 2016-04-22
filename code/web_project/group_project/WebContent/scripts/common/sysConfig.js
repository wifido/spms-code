//<%@ page language="java" contentType="text/html; charset=utf-8"%>
//延时30分钟(Ext.form.submit.timeout超时设置是秒; Ext.Ajax.request.timeout 超时设置是毫秒；)
Ext.Ajax.timeout = 1000 * 60 * 30;  
Ext.form.Action.timeout = 15 * 60;
Ext.form.BasicForm.timeout = 15 * 60;
Ext.form.Action.Load.timeout = 15 * 60;
Ext.form.Action.Submit.timeout = 15 * 60;
Ext.QuickTips.init();
Ext.BLANK_IMAGE_URL='../ext-3.4.0/resources/images/default/s.gif';


/**
 * 重写Grid.Column.renderer
 * 每列悬停时提示内容信息
 */
Ext.override(Ext.grid.Column,{
	renderer : function(value,metadata,record,rowIdx,colIdx,ds){
		if( this.rendererCall){
			var ret = this.rendererCall(value,metadata,record,rowIdx,colIdx,ds);
			return '<div ext:qtitle="' +this.header 
				+ '" ext:qtip="' + (ret == null ? "" : ret) + '">'+ (ret == null ? "" : ret) +'</div>';
		}else{
			return '<div ext:qtitle="' +this.header 
				+ '" ext:qtip="' + (value == null ? "" : value) + '">'+ (value == null ? "" : value) +'</div>';
		}
    }
});

//最大长度计算汉字处理函数
var maxLengthOverride = function (value){   
	if(this.allowBlank == false){ 
		if( Ext.isEmpty(value,false) ){   
			this.markInvalid(String.format(this.blankText,value));   
			return false;   
		}
	}   
	var maxLen = this.maxLength;   
	var maxLenText = this.maxLengthText;   
	var len ; 
	if(maxLen != null && maxLen != 'undefined' && maxLen > 0 ){    
	   var regex = /[^\x00-\xff]/g;  
	   var repalceValue = value.replace(regex,'***');
		len = repalceValue.length;
	}
	
	if(len > maxLen){   
		this.markInvalid(String.format(maxLenText ,value));   
		return false;   
	}
	return true;   
}

var checkboxselection = new Ext.grid.CheckboxSelectionModel({singleSelect: false});

Ext.onReady(function(){
	
	new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'center',
			layout: 'border',
			defaults: {
				ctCls: 'x-btn-over'	
			},
			tbar : new Ext.Toolbar({
				defaults : {
					ctCls: 'x-btn-over'
				},
				items:['-'
				, {
					width: 60,
					text: '查询',
					handler: onSearch
				}
				, {
					width: 60,
					text: '新增',
					handler: addForm
				}
				, {
					width: 60,
					text: '修改',
					handler: editForm
				}
				, {
					width: 60,
					text: '删除',
					handler: removeForm
				}]
			}),
			items: [queryPanel, listPanel]
		}]
	});
});

//查询条件面板
var queryPanel = new Ext.form.FormPanel({
	
	id: 'queryForm',
	region: 'north',
	autoHeight: true,
	frame: true,
	items: [{
		xtype: 'fieldset',
		title: '查询条件',
		items: [{
				layout: 'column',
				items: [{
					layout: 'form',
					columnWidth: .5,
					labelWidth: 80,
					items: [{
						name: 'sysConfig.keyName',
						xtype: 'textfield',
						fieldLabel: 'KEY名称',
					    width : 130
					}]
				}, {
					layout: 'form',
					columnWidth: .5,
					labelWidth: 80,
					items: [{
						name: 'sysConfig.keyDesc',
						xtype: 'textfield',
						fieldLabel: 'KEY描述',
					    width : 130
					}]
				}, {
					layout: 'form',
					columnWidth: 1,
					labelWidth: 80,
					items: [{
						html: '<font color=red>注意事项：</br>' +
												'1.非管理员请勿维护记录！</font>'
					}]
				}]
		}]
	}]
});
	
//数据源
var store = new Ext.data.JsonStore({
	autoDestroy: true,
	url: 'sysConfig_pageView.action',
	root: 'results',
	totalProperty: 'total',
	fields: ['id', 'keyName', 'keyValue', 'keyDesc', 'createdTm', 'modifiedTm']
});

//显示列表面板
var listPanel = new Ext.grid.GridPanel({
	
	region: 'center',
	loadMask: true,
	tbar: {
		xtype: 'paging',
		store: store,
		displayInfo: true,
		pageSize: 50,
		displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
		emptyMsg : '未检索到数据',
		autoWidth : true
	},
	store: store,
	sm: checkboxselection,
	columns: [new Ext.grid.RowNumberer(), 
	checkboxselection,
	{header: 'KEY名称', dataIndex: 'keyName', align: 'left', width: 200},
	{header: 'KEY值', dataIndex: 'keyValue', align: 'left', width: 300},
	{header: 'KEY描述', dataIndex: 'keyDesc', align: 'left', width: 300},
	{header: '创建时间', dataIndex: 'createdTm', align: 'left', width: 150, rendererCall: transformInformDate},
	{header: '修改时间', dataIndex: 'modifiedTm', align: 'left', width: 150, rendererCall: transformInformDate}]
});

//弹出新增和编辑窗口
var win = new Ext.Window({
	
	layout: 'fit',
	width: 600,
	height: 300,
	closeAction: 'hide',
	modal: true,
	fbar: [{
		text: '保存',
		ctCls: 'x-btn-over',
		handler: saveForm
	}, {
		text: '取消',
		ctCls: 'x-btn-over',
		handler: function() {
			win.hide();
		}
	}],
	items: [{
		id: 'dataForm',
		url: 'sysConfig_saveOrUpdate.action',
		xtype: 'form',
		frame: true,
		labelAlign: 'right',
		items: [{
			layout: 'column',
			items: [{
				layout: 'form',
				columnWidth: .9,
				items: [
			{
				id: 'win_id',
				name: 'sysConfig.id',
				xtype: 'hidden'
			}, {	
					id: 'win_keyName',
					name: 'sysConfig.keyName',
					xtype: 'textfield',
					fieldLabel: 'KEY名称',
					width: 300,
					maxLength: 90,
					maxLengthText: '该输入项的最大长度是30汉字',
					validateValue: maxLengthOverride,
					allowBlank: false 
				}]
			}, {
				layout: 'form',
				columnWidth: .9,
				items: [{
					id: 'win_keyValue',
					name: 'sysConfig.keyValue',
					xtype: 'textarea',
					fieldLabel: 'KEY值',
					width : 300,
					maxLength: 1500,
					maxLengthText: '该输入项的最大长度是500汉字',
					validateValue: maxLengthOverride,
					allowBlank: false 
				}]
			}, {
				layout: 'form',
				columnWidth: .9,
				items: [{
					id: 'win_keyDesc',
					name: 'sysConfig.keyDesc',
					xtype: 'textarea',
					fieldLabel: 'KEY描述',
					width: 300,
					maxLength: 900,
					maxLengthText: '该输入项的最大长度是300汉字',
					validateValue: maxLengthOverride
				}]
			}]
		}]
	}]
});

/**
 * 查询
 */
function onSearch(){
	
	if(!Ext.getCmp('queryForm').getForm().isValid()) {
		Ext.Msg.alert('提示', '请输入正确查询条件');
		return;
	}
	  
	store.baseParams = Ext.getCmp('queryForm').getForm().getValues();
   	store.load();
}

/**
 * 弹出新增界面
 */
function addForm() {
	
	win.show();
	win.setTitle('新增信息');
	Ext.getCmp('dataForm').getForm().reset();
	setReadOnlyF(false);
}

/**
 * 弹出编辑界面
 */
function editForm() {
	
	var records = listPanel.getSelectionModel().getSelections();
	var record = listPanel.getSelectionModel().getSelected();
	
	if(records.length == 1){
		
		win.show();
		win.setTitle('修改信息');
	    Ext.getCmp('dataForm').getForm().reset();
	    
	    Ext.getCmp('win_id').setValue(record.get('id'));
	    Ext.getCmp('win_keyName').setValue(record.get('keyName'));
	    Ext.getCmp('win_keyValue').setValue(record.get('keyValue'));
	    Ext.getCmp('win_keyDesc').setValue(record.get('keyDesc'));
	    setReadOnlyF(true);
	} else {
		if(records.length == 0 ) {
			Ext.Msg.alert('提示', '请选择一条记录');
			return;
		} 
		if(records.length > 1) {
			Ext.Msg.alert('提示', '修改时只能选择一条记录');
			return;
		} 
	}
}


/**
 * 保存
 */
function saveForm() {

	Ext.getCmp('dataForm').getForm().submit({
		  //在成功的情况下出现提示,属于用户操作不正确
		  success: function(form, action) {
			  if(!action.result.msg){
				  Ext.Msg.alert('提示', '操作成功');
				  store.reload();
				  win.hide(); 
			  }else{
				  Ext.Msg.alert('提示', action.result.msg);
			  }
		  },
		  //超时时间5分钟
		  timeout : 5*60,
		  //在失败情况下出现提示，属于系统错误
		  failure: formFailure,
		  waitMsg: '正在执行操作...',
		  waitTitle: '请稍后'
	  });
}	

/**
 * 设置只读
 * @param {} flag
 */
function setReadOnlyF(flag) {
	
	Ext.getCmp('win_keyName').setReadOnly(flag);
}

/**
 * 删除
 */
function removeForm() {
	
	var records = listPanel.getSelectionModel().getSelections();
	var ids = [];
	
	if(records.length > 0) {
		
		Ext.Msg.confirm('提示', '您确定要删除记录!', function (btn){
			if(btn == 'yes') {
				for(var i = 0; i < records.length; i ++){
					ids.push(records[i].get('id'));
				}
				Ext.Ajax.request({
				   url: 'sysConfig_remove.action',
				   params: {'ids': ids},
				   success: function(response, opts) {
				   	  var result = Ext.util.JSON.decode(response.responseText);
				   	  if(result.msg != null && result.msg != '') {
				   	    Ext.Msg.alert('提示', result.msg);
				   	  } else {
				   	  	store.reload();
				      	Ext.Msg.alert('提示', '操作成功');
				   	  }
				   },
				   failure: ajaxRequestFailure
				});
			}
		});
	} else {
		Ext.Msg.alert('提示', '请选择记录');
	}
}

/**
 * 数组转换
 * @param {} v
 * @param {} fieldArray 数组
 * @return {}
 */
function transformStroe(v, fieldArray) {
	
	if(Ext.isEmpty(v, false)) {
		return '';
	}	    
	var vText = v;
	Ext.each(fieldArray, function(item){
		if(v == item[0]){
			vText = item[1];
			return ;
		}
	});	    
	return vText;
}

/**
 * 转换报修时间
 * @param {} v 报修时间
 * @return {}
 */
function transformInformDate(v) {
	if(v != null && v != '') v = v.toString().replace('T', ' ');
	return v;
}

/**
 * 表单异常处理
 * @param {} form
 * @param {} action
 */
function formFailure(form, action) {
	var msg = '';
	if(action && action.result && action.result.error){
		//数据库连接不上的时候会报此错误
		Ext.Msg.alert('提示', '系统出现了异常,请与管理员联系!');
	} else {
		if (action.failureType == Ext.form.Action.CONNECT_FAILURE) {
			if (action.response.status == 0) {
				//应用服务器没有启动的时候会报此错误
				msg = '连接失败,无法连接到服务器!';
			} else if (action.response.status == -1) {
				//一般服务器处理繁忙的时候会报此错误
				msg = '服务器处理超时!';
			} else {
				msg = String.format('错误代码:{0}, 错误描述:{1}', action.response.status, action.response.statusText);
			}
		} else if (action.failureType === Ext.form.Action.CLIENT_INVALID) {
			msg = '请将表单填写正确!';
		}
		Ext.Msg.alert('提示', msg);
	 }
}	
/**
 * ajax请求异常处理
 * @param {} response
 */
function ajaxRequestFailure(response) {
	if (response.status == 0) {
	    Ext.Msg.alert('提示', '无法连接到服务器，请检查网络是否正常');
	} else if (response.status == -1) {
	    Ext.Msg.alert('提示', '服务器处理超时，请稍后再试');
	} else {
		Ext.Msg.alert('提示', '系统出现异常,请与管理员联系');
	}
};