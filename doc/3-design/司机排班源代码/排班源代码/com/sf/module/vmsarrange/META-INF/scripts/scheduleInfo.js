//<%@ page language="java" contentType="text/html; charset=UTF-8"%>
// 请求有效时间十五分钟
Ext.Ajax.timeout = 15*60*1000;
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = "side";
Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
Ext.i18n = {
	search:"${app:i18n('search')}",
	add:"${app:i18n('add')}",
	update:"${app:i18n('edit')}",
	exported:"${app:i18n('export')}",
	prompt:"${app:i18n('prompt')}",
	deleted:"${app:i18n('delete')}",
	searchCondition:"${app:i18n('queryCondition')}",
	pleaseChooseRecord:"${app:i18n('selectOneRecord')}",
	validateError:"${app:i18n('validateError')}",
	exportingFile:"${app:i18n('exportingFile')}",
	cancel:"${app:i18n('canel')}",
	deptTreeTitle:"${app:i18n('deptTreeTitle')}",
	save:"${app:i18n('save')}",
	saveSuccess:"${app:i18n('saveSuccess')}",
	saving:"${app:i18n('saving')}",
	selectDepartment:"${app:i18n('selectDepartment')}",
	sfExpress:"${app:i18n('sf_express')}",
	
	upload:"${app:i18n_def('common.js.upload','导入')}",
	uploadSuccess:"${app:i18n_def('common.js.uploadSuccess','上传成功')}",
	deptCode:"${app:i18n_def('common.js.deptCode','网点代码')}",
	dataSourceAll:"${app:i18n_def('common.js.chooseAll','全部')}",
	vehicleCode:"${app:i18n_def('common.js.vehicleCode','车牌号')}",
	valid:"${app:i18n_def('scheduleArrange.js.47','有效性')}",
	valid1:"${app:i18n_def('common.js.valid1','有效')}",
	valid0:"${app:i18n_def('common.js.valid0','无效')}",
	areaName:"${app:i18n_def('common.js.areaName','所属地区')}",
	createdEmpCode:"${app:i18n_def('common.js.createdEmp','创建人')}",
	createdTm:"${app:i18n_def('common.js.createdTm','创建时间')}",
	modifiedEmpCode:"${app:i18n_def('common.js.modifiedEmp','修改人')}",
	modifiedTm:"${app:i18n_def('common.js.modifedTm','修改时间')}",
	netError:"${app:i18n_def('common.js.netError','网络状况不好,请稍后重试')}",
	deleteConfirm:"${app:i18n_def('common.js.deleteConfirm','是否删除')}",
	
	dataSource:"${app:i18n_def('scheduleInfo.js.1','数据源')}",
	dataSource1:"${app:i18n_def('scheduleInfo.js.2','手工录入')}",
	dataSource2:"${app:i18n_def('scheduleInfo.js.3','路径优化')}",
	modelBase:"${app:i18n_def('scheduleInfo.js.4','车型')}",
	lineOptimizeNo:"${app:i18n_def('scheduleInfo.js.5','路径优化线路编号')}",
	startTm:"${app:i18n_def('scheduleInfo.js.6','出车时间')}",
	endTm:"${app:i18n_def('scheduleInfo.js.7','收车时间')}",
	startDept:"${app:i18n_def('scheduleInfo.js.8','始发网点')}",
	endDept:"${app:i18n_def('scheduleInfo.js.9','目的网点')}",
	choose:"${app:i18n_def('scheduleInfo.js.10','选择')}",
	selectVehicle:"${app:i18n_def('scheduleInfo.js.11','选择车辆')}",
	selectDept:"${app:i18n_def('scheduleInfo.js.12','选择网点')}",
	typeCode:"${app:i18n_def('scheduleInfo.js.13','网点类型')}",
	ownerDept:"${app:i18n_def('scheduleInfo.js.14','归属网点')}",
	allVehicleGuide:"${app:i18n_def('scheduleInfo.js.15','提示:若想加载所有类型车辆，请于新增窗口清除选择的车型')}",
	tmError1:"${app:i18n_def('scheduleInfo.js.16','格式错误，正确格式例如:09(范围:00到23)')}",
	tmError2:"${app:i18n_def('scheduleInfo.js.17','格式错误，正确格式例如:09(范围:00到59)')}",
	deptName:"${app:i18n_def('scheduleInfo.js.18','网点名称')}",
	deleteSuccess:"${app:i18n_def('scheduleInfo.js.19','删除成功')}",
	pleaseChooseFile:"${app:i18n_def('scheduleInfo.js.20','请选择文件')}",
	pleaseChooseExcel:"${app:i18n_def('scheduleInfo.js.21','文件格式有误，只能选择excel(.xls后缀文件)')}",
	confirmUpload:"${app:i18n_def('scheduleInfo.js.22','该操作可能需要较长时间(导入期间其他用户将不能新增/修改/导入线路信息),是否导入')}",
	html1:"${app:i18n_def('scheduleInfo.js.23','注意事项')}",
	html2:"${app:i18n_def('scheduleInfo.js.24','提示信息中的行号均指excel表格的序号(数据中间必须无空行)')}",
	html3:"${app:i18n_def('scheduleInfo.js.25','提示信息中的线路重复指：出收车时间,始发网点,目的网点均相同')}",
	html4:"${app:i18n_def('scheduleInfo.js.26','请阅读模板中的注意事项及业务规则,并遵循业务规则进行操作')}",
	html5:"${app:i18n_def('scheduleInfo.js.40','请不要在数据中间或尾部留有空行')}",
	filePath:"${app:i18n_def('scheduleInfo.js.27','文件路径')}",
	templateLabel:"${app:i18n_def('scheduleInfo.js.28','导入模板')}",
	wrongInfo:"${app:i18n_def('scheduleInfo.js.29','错误信息')}",
	confirmDownload:"${app:i18n_def('scheduleInfo.js.30','导出数据过多时可能需要较长时间，是否导出')}",
	fileNameNull:"${app:i18n_def('scheduleInfo.js.31','生成的文件名称为空,请重新导出')}",
	isUsedState:"${app:i18n_def('scheduleInfo.js.32','状态')}",
	isUsed1:"${app:i18n_def('scheduleInfo.js.41','已配班')}",
	isUsed0:"${app:i18n_def('scheduleInfo.js.42','未配班')}",
	isUsed2:"${app:i18n_def('scheduleInfo.js.43','未配班(不允许修改)')}",
	isUsedReadOly:"${app:i18n_def('scheduleInfo.js.35','状态为已配班的记录不允许修改')}",
	notAllowDelete1:"${app:i18n_def('scheduleInfo.js.36','有效记录不允许删除')}",
	notAllowDelete2:"${app:i18n_def('scheduleInfo.js.37','状态为已配班的记录不允许删除')}",
	overLength:"${app:i18n_def('scheduleInfo.js.38','字段过长')}",
	vehicleDeptCode:"${app:i18n_def('scheduleInfo.js.39','所属网点')}",
	saveConfirm:"${app:i18n_def('common.js.saveConfirm','是否保存')}",
	editValid:"${app:i18n_def('scheduleArrange.js.48','修改有效性')}",
	isUsed2Alert:"${app:i18n_def('scheduleInfo.js.46','该记录已经被无效配班记录引用，不允许修改')}"
};
getErrors = function(value) {
        var errors = Ext.form.TextField.superclass.getErrors.apply(this, arguments);  
        value = Ext.isDefined(value) ? value : this.processValue(this.getRawValue());              
        if (Ext.isFunction(this.validator)) {
            var msg = this.validator(value);
            if (msg !== true) {
                errors.push(msg);
            }
        }     
        if (value.length < 1 || value === this.emptyText) {
            if (this.allowBlank) {
                return errors;
            } else {
                errors.push(this.blankText);
            }
        }     
        if (!this.allowBlank && (value.length < 1 || value === this.emptyText)) { 
            errors.push(this.blankText);
        }
       //正则替换汉字，处理汉字长度计算错误问题
        var regex = /[^\x00-\xff]/g; 
        var newValue = value.replace(regex,'***');
        if (newValue.length < this.minLength) {
            errors.push(String.format(this.minLengthText, this.minLength));
        }
        if (newValue.length > this.maxLength) {
            errors.push(String.format(this.maxLengthText, this.maxLength));
        }
        
        if (this.vtype) {
            var vt = Ext.form.VTypes;
            if(!vt[this.vtype](value, this)){
                errors.push(this.vtypeText || vt[this.vtype +'Text']);
            }
        }
        if (this.regex && !this.regex.test(value)) {
            errors.push(this.regexText);
        }
        return errors;
};
var dlg=new Dialog();
Ext.override(Ext.form.TextField, {
	initComponent : function(){
		if (this.allowBlank === false) {
			if(Ext.isEmpty(this.useDefault)){
				this.labelSeparator = '<span style="color:#FF0000;">*</span>:';
			}
		}
        Ext.form.TextField.superclass.initComponent.call(this);
        this.addEvents(
            'autosize',
            'keydown',
            'keyup',
            'keypress'
        );
    }
});
var tree = new Ext.tree.AsyncTreeNode({
	id:'0',
	text:Ext.i18n.sfExpress,
	loader:new Ext.tree.TreeLoader({
		url : "../vmsarrange/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
		dataUrl : "../vmsarrange/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
	})
});
dlg.on("beforeinit",function(){
	//网点树
	dlg.setNavigatorTitle(Ext.i18n.deptTreeTitle);
	dlg.setNavigatorRootNode(tree);
	
	//查询
	<app:isPermission code="/vmsarrange/scheduleInfo_search.action">
	dlg.addBizButton({text:Ext.i18n.search});
	</app:isPermission>
	//新增按钮
	<app:isPermission code="/vmsarrange/scheduleInfo_append.action">
	this.addBizButton({text : Ext.i18n.add});
	</app:isPermission>
	//修改按钮
	<app:isPermission code="/vmsarrange/scheduleInfo_modify.action">
	this.addBizButton({text :Ext.i18n.update});
	</app:isPermission>
	//删除
  	<app:isPermission code="/vmsarrange/scheduleInfo_remove.action">
  	this.addBizButton({text:Ext.i18n.deleted,cls:'x-btn-normal'});
  	</app:isPermission>
	//导入
	<app:isPermission code="/vmsarrange/scheduleInfo_upload.action">
	this.addBizButton({text:Ext.i18n.upload,cls:'x-btn-normal'});
  	</app:isPermission>
  	//导出
  	<app:isPermission code="/vmsarrange/scheduleInfo_download.action">
  	this.addBizButton({text:Ext.i18n.exported,cls:'x-btn-normal'});
  	</app:isPermission>
  	//置为无效
  	<app:isPermission code="/vmsarrange/arrScheduleInfo_updatevalid.action">
  	this.addBizButton({text:Ext.i18n.editValid,cls:'x-btn-normal'});
  	</app:isPermission>
				
	// 查询条件
	var queryCt = this.getQueryCt();
	queryCt.setTitle(Ext.i18n.searchCondition);
	queryCt.ownerCt.setHeight(80+(23*1));

	// 网点
	queryCt.add({
		labelAlign:"right",
		labelWidth:80,
		columnWidth:.33,
		items:[{
			xtype:"hidden",name:"deptId"
		},{
			xtype:"textfield",
			readOnly:true,
			name:"deptCode",
			allowBlank:false,
			fieldLabel:Ext.i18n.deptCode,
			width:120
		}]
	});
	//数据源
	queryCt.add({
		labelAlign:"right",
		labelWidth:80,
		columnWidth:.33,
		items:[{
			xtype:"combo",
			displayField:"text",
			valueField:"value",
			mode:"local",
			triggerAction:"all",
			hiddenName:"dataSource",
			fieldLabel:Ext.i18n.dataSource,
			editable:false,
			width:120,
			store:new Ext.data.ArrayStore({
				fields:['text','value'],
				data:[[Ext.i18n.dataSourceAll,null],[Ext.i18n.dataSource1,1],[Ext.i18n.dataSource2,2]]
			})
		}]
	});
	//车牌号
	queryCt.add({
		columnWidth:.33,
		labelWidth:80,
		labelAlign:'right',
		items:[{
			xtype:"textfield",
			fieldLabel:Ext.i18n.vehicleCode,
			name:"vehicleCode",
			width:120
		}]
	});
	//是否有效
	queryCt.add({
		columnWidth:.33,
		labelWidth:80,
		labelAlign:"right",
		items:[{
			xtype:"combo",
			hiddenName:"valid",
			width:120,
			fieldLabel:Ext.i18n.valid,
		    mode: 'local',
		    displayField:"text",
		    valueField:"value",
		    triggerAction: 'all',
		    editable:false,
		    store:new Ext.data.SimpleStore({
				fields:["text","value"],
				data:[[Ext.i18n.dataSourceAll,null],[Ext.i18n.valid1,1],[Ext.i18n.valid0,0]]
			})
		}]
	});
	//状态
	queryCt.add({
		columnWidth:.33,
		labelWidth:80,
		labelAlign:"right",
		items:[{
			xtype:"combo",
			hiddenName:"isUsed",
			width:120,
			fieldLabel:Ext.i18n.isUsedState,
		    mode: 'local',
		    displayField:"text",
		    valueField:"value",
		    triggerAction: 'all',
		    editable:false,
		    store:new Ext.data.SimpleStore({
				fields:["text","value"],
				data:[[Ext.i18n.dataSourceAll,null],[Ext.i18n.isUsed0,0],[Ext.i18n.isUsed1,1]]
			})
		}]
	});	
	// 添加Grid
	var _store = new Ext.data.JsonStore({
    	url:"scheduleInfoAction_listPage.action",
    	root:"page",
    	totalProperty:"total",
	  	fields:['id',
	  	        'areaName',
	  	        'modelBase',
	  	        'dataSource',
	  	        'vehicle',
	  	        'lineOptimizeNo',
	  	        'startTm',
	  	        'endTm',
	  	        'dept',
	  	        'startDept',
	  	        'endDept',
	  	        'valid',
	  	        'createdEmpCode',
			  	'createdTm',
			  	'modifiedEmpCode',
			  	'modifiedTm',
			  	'isUsed'],
	  	listeners:{
	  		beforeLoad:function(){
	  			this.baseParams = dlg.getQueryForm().getValues();
	  			this.baseParams['limit'] = dlg.getGrid().getTopToolbar().pageSize;
	  		}
	  	}
  	});
	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	dlg.addGrid({
		region:'center',
		xtype:'grid',
		border:false,
		loadMask: true,
		enableHdMenu:false,
		sm:sm,
		store:_store,
		tbar:new Ext.PagingToolbar({
	        pageSize: 50,
	        store: _store,
	        displayInfo: true
    	}),      
		columns:[new Ext.grid.RowNumberer(),sm,
				{header:Ext.i18n.areaName,dataIndex:"areaName",sortable:false,width:85
				},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
				},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
				},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:95,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(Ext.isEmpty(v.deptCode) && Ext.isEmpty(v.deptName)){
							return '';
						}
						if(Ext.isEmpty(v.deptCode)){
							return v.deptName;
						}
						if(Ext.isEmpty(v.deptName)){
							return v.deptCode;
						}
						return v.deptCode+"/"+v.deptName;
					}
				},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:95,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(Ext.isEmpty(v.deptCode) && Ext.isEmpty(v.deptName)){
							return '';
						}
						if(Ext.isEmpty(v.deptCode)){
							return v.deptName;
						}
						if(Ext.isEmpty(v.deptName)){
							return v.deptCode;
						}
						return v.deptCode+"/"+v.deptName;
					}
				},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:80
				},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:60,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						return v.vehicleCode;
					}
				},{header:Ext.i18n.valid,dataIndex:"valid",sortable:false,width:60,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(1 == v){
							return Ext.i18n.valid1;
						}
						if(0 == v){
							return Ext.i18n.valid0;
						}
						return v;
					}
				},{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:60,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(1 == v){
							return Ext.i18n.dataSource1;
						}
						if(2 == v){
							return Ext.i18n.dataSource2;
						}
						return v;
					}
				},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:110
				},{header:Ext.i18n.isUsedState,dataIndex:"isUsed",sortable:false,width:90,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(1 == v){
							return "<font color='red'>"+Ext.i18n.isUsed1+"</font>";
						}
						if(0 == v){
							return "<font color='green'>"+Ext.i18n.isUsed0+"</font>";
						}
						if(2 == v){
							return "<font color='green'>"+Ext.i18n.isUsed0+"</font>";
						}
						return v;
					}
				},{header:Ext.i18n.createdEmpCode,dataIndex:"createdEmpCode",sortable:false,width:50
				},{header:Ext.i18n.createdTm,dataIndex:"createdTm",sortable:false,width:120,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						return v.replace("T"," ");
					}
				},{header:Ext.i18n.modifiedEmpCode,dataIndex:"modifiedEmpCode",sortable:false,width:50
				},{header:Ext.i18n.modifiedTm,dataIndex:"modifiedTm",sortable:false,width:120,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						return v.replace("T"," ");
					}
		}]
	});
},dlg);
dlg.on("selectionchange",function(sm,node){
	if(Ext.isEmpty(node) ||(!Ext.isEmpty(node) && 0 == node.id)){
		dlg.getQueryForm().findField("deptCode").setValue(null);
		dlg.getQueryForm().findField("deptId").setValue(null);
		return;	
	}
	dlg.getQueryForm().findField("deptCode").setValue(node.attributes.deptCode);
	dlg.getQueryForm().findField("deptId").setValue(node.id);
},dlg);
//查询
dlg.on(Ext.i18n.search,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	dlg.getGrid().getStore().load();
},dlg)
//修改有效性
var validWin = new Ext.Window({
		title:Ext.i18n.editValid,
		width:400,
		height:200,
		modal:true,
		border:false,
		bodyBorder:false,
		resizable:false,
		layout:'fit',
		closeAction:'hide',
		tbar:[{text:Ext.i18n.save,cls:'x-btn-normal',minWidth:60,handler:function(){
				var form = validWin.getComponent(0).getForm();
				var sm = dlg.getGrid().getSelectionModel();
				var record = sm.getSelected();
				if(!form.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if(btn == 'yes'){
						form.submitEx({
							url:'scheduleInfoAction_updateValid.action',
							params:{
								"recordId":record.data.id
							},
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								if(!Ext.isEmpty(result.errorMsg)){
									Ext.Msg.alert(Ext.i18n.prompt,result.errorMsg);
									return false;
								}
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.saveSuccess,function(){
									validWin.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,cls:'x-btn-normal',minWidth:60,handler:function(){validWin.hide();}
		}],
		items:[{
			xtype:'form',
			frame:true,
			items:[{
				xtype:"combo",
				hiddenName:"valid",
				width:120,
				fieldLabel:Ext.i18n.valid,
			    mode: 'local',
			    displayField:"text",
			    valueField:"value",
			    triggerAction: 'all',
			    editable:false,
			    allowBlank:false,
			    forceSelection:true,
			    value:null,
			    store:new Ext.data.SimpleStore({
					fields:["text","value"],
					data:[[Ext.i18n.valid1,1],[Ext.i18n.valid0,0]]
				})
			}]
		}]
	})
//置为无效
dlg.on(Ext.i18n.editValid,function(){
	var sm = dlg.getGrid().getSelectionModel();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	if(sm.getSelections().length > 1){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	var sm = dlg.getGrid().getSelectionModel();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	validWin.show();
	var form = validWin.getComponent(0).getForm();
	form.reset();
},dlg)
//车辆选择
var vehicleSm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,header:""});
var vehicleStore = new Ext.data.JsonStore({
			url:"scheduleInfoAction_listVehiclePage.action",
			fields:["modelBase","vehicleCode","department"],
			root:"vehiclePage",
			totalProperty:"total",
			listeners:{
				'beforeload':function(){
					Ext.getCmp('vehicleGrid').getStore().baseParams = Ext.getCmp("vehicleForm").getForm().getValues();
					Ext.getCmp('vehicleGrid').getStore().baseParams['limit'] = Ext.getCmp('vehicleGrid').getTopToolbar().pageSize;
				}
			}
		});
var vehicleWin = new Ext.Window({
	title:Ext.i18n.selectVehicle,
	width:500,
	height:390,
	closeAction:"hided",
	modal:true,
	resizable:false,
	hided:function(){
		Ext.getCmp("vehicleForm").getForm().reset();
		vehicleSm.clearSelections();
		vehicleWin.hide();
	},
	tbar:[{
		text:Ext.i18n.choose,minWidth:60,cls:'x-btn-normal',
		handler:function(){
			if(!vehicleSm.hasSelection()){
				Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
				return false;
			}
			var record = vehicleSm.getSelected();
			var formName = Ext.getCmp("vehicleForm").getForm().findField("formName").getValue();
			var form = Ext.getCmp(formName).getForm();
			form.findField("entity.vehicle.vehicleCode").setValue(record.data.vehicleCode);
			//设置车型为所选车辆车型
			form.findField("entity.modelBase").setValue(record.data.modelBase);
			Ext.getCmp("vehicleForm").getForm().reset();
			vehicleSm.clearSelections();
			vehicleWin.hided();
		}
	},{
		text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
		handler:function(){
			Ext.getCmp("vehicleForm").getForm().reset();
			vehicleSm.clearSelections();
			vehicleWin.hided();
		}
	}],
	bbar:[{xtype:"label",text:Ext.i18n.allVehicleGuide}],
	layout:"border",
	items:[{
		region:"north",
		id:"vehicleForm",
		height:20*2+10,
		frame:true,
		xtype:"form",
		items:[{xtype:"hidden",name:"formName"},
			{xtype:"hidden",name:"modelBase"},{
			xtype:"trigger",
			triggerClass : 'x-form-search-trigger',
			fieldLabel:Ext.i18n.vehicleCode,
			name:"vehicleCode",
			onTriggerClick : function() {
				Ext.getCmp('vehicleGrid').getStore().load();
			}
		}]
	},{
		region:"center",
		xtype:"grid",
		id:"vehicleGrid",
		sm:vehicleSm,
		tbar:new Ext.PagingToolbar({
	        pageSize: 10,
	        store: vehicleStore,
	        displayInfo: true
    	}), 
		store:vehicleStore,
		loadMask:true,
		listeners:{
			'dblclick':function(){
				if(!vehicleSm.hasSelection()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
					return false;
				}
				var record = vehicleSm.getSelected();
				var formName = Ext.getCmp("vehicleForm").getForm().findField("formName").getValue();
				var form = Ext.getCmp(formName).getForm();
				form.findField("entity.vehicle.vehicleCode").setValue(record.data.vehicleCode);
				//设置车型为所选车辆车型
				form.findField("entity.modelBase").setValue(record.data.modelBase);
				Ext.getCmp("vehicleForm").getForm().reset();
				vehicleSm.clearSelections();
				vehicleWin.hided();
			}
		},
		columns:[vehicleSm,
			{header:Ext.i18n.vehicleCode,dataIndex:"vehicleCode",sortable:false,width:130},
			{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:80},
			{header:Ext.i18n.vehicleDeptCode,dataIndex:"department",sortable:false,width:225,
				rendererCall:function(v){
					if(Ext.isEmpty(v)){
						return '';
					}
					if(Ext.isEmpty(v.deptCode) && Ext.isEmpty(v.deptName)){
						return '';
					}
					if(Ext.isEmpty(v.deptCode)){
						return v.deptName;
					}
					if(Ext.isEmpty(v.deptName)){
						return v.deptCode;
					}
					return v.deptCode+"/"+v.deptName;
				}
			}
		]
	}]
})
//网点选择
var deptSm = new Ext.grid.CheckboxSelectionModel({singleSelect:true,header:""});
//需要动态修改url:1.有权限控制scheduleInfoAction_listUserDeptPage 2.无权限控制scheduleInfoAction_listDeptPage
var deptStore = new Ext.data.JsonStore({
			url:"scheduleInfoAction_listDeptPage.action",
			fields:["id","deptCode","deptName","typeCode","areaCode"],
			root:"deptPage",
			totalProperty:"total",
			listeners:{
				'beforeload':function(){
					Ext.getCmp('deptGrid').getStore().baseParams = Ext.getCmp("deptForm").getForm().getValues();
					Ext.getCmp('deptGrid').getStore().baseParams['limit'] = Ext.getCmp('deptGrid').getTopToolbar().pageSize;
				}
			}
		});
var deptWin = new Ext.Window({
	title:Ext.i18n.selectDept,
	width:500,
	height:390,
	closeAction:"hided",
	modal:true,
	resizable:false,
	hided:function(){
		Ext.getCmp("deptForm").getForm().reset();
		deptSm.clearSelections();
		deptWin.hide();
	},
	tbar:[{
		text:Ext.i18n.choose,minWidth:60,cls:'x-btn-normal',
		handler:function(){
			if(!deptSm.hasSelection()){
				Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
				return false;
			}
			var deptForm = Ext.getCmp("deptForm").getForm();
			var formName = Ext.getCmp("deptForm").getForm().findField("formName").getValue();
			var form = Ext.getCmp(formName).getForm();
			form.findField(deptForm.findField("deptCodeField").getValue()).setValue(deptSm.getSelected().data.deptCode);
			deptSm.clearSelections();
			deptWin.hided();
		}
	},{
		text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
		handler:function(){
			Ext.getCmp("deptForm").getForm().reset();
			deptSm.clearSelections();
			deptWin.hided();
		}
	}],
	layout:"border",
	items:[{
		region:"north",
		id:"deptForm",
		xtype:"form",
		height:20*2+10,
		frame:true,
		items:[{xtype:"hidden",name:"formName"},
			{xtype:"hidden",name:"deptCodeField"},{
			xtype:"trigger",
			triggerClass : 'x-form-search-trigger',
			fieldLabel:Ext.i18n.deptCode,
			name:"deptCode",
			onTriggerClick : function() {
				Ext.getCmp('deptGrid').getStore().load();
			}
		}]
	},{
		region:"center",
		xtype:"grid",
		id:"deptGrid",
		sm:deptSm,
		tbar:new Ext.PagingToolbar({
	        pageSize: 10,
	        store: deptStore,
	        displayInfo: true
    	}), 
		store:deptStore,
		loadMask:true,
		autoExpandColumn:"autoDeptName",
		listeners:{
			'dblclick':function(){
				if(!deptSm.hasSelection()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
					return false;
				}
				var deptForm = Ext.getCmp("deptForm").getForm();
				var formName = Ext.getCmp("deptForm").getForm().findField("formName").getValue();
				var form = Ext.getCmp(formName).getForm();
				form.findField(deptForm.findField("deptCodeField").getValue()).setValue(deptSm.getSelected().data.deptCode);
				deptSm.clearSelections();
				deptWin.hided();
			}
		},
		columns:[deptSm,
			{header:Ext.i18n.areaName,dataIndex:"areaCode"},
			{header:Ext.i18n.deptCode,dataIndex:"deptCode"},
			{header:Ext.i18n.deptName,dataIndex:"deptName",id:"autoDeptName"},
			{header:Ext.i18n.typeCode,dataIndex:"typeCode"}
		]
	}]
})
//新增
dlg.on(Ext.i18n.add,function(){
	var addWin = dlg.createModalDialog();
	addWin.add({xtype:"panel",
		title:Ext.i18n.add,
		tools:[{id:'close',
			handler:function(){
				addWin.getForm().reset();
				addWin.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var addForm = Ext.getCmp("addForm").getForm();
				if(!addForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if('yes' == btn){
						var startTm = addForm.findField("startTm1").getValue()+":"+addForm.findField("startTm2").getValue();
						var endTm = addForm.findField("endTm1").getValue()+":"+addForm.findField("endTm2").getValue();
						addForm.submitEx({
							url:"scheduleInfoAction_saveEntity.action",
							params:{
								'entity.startTm':startTm,
								'entity.endTm':endTm
							},
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								if(!Ext.isEmpty(result.errorMsg)){
									Ext.Msg.alert(Ext.i18n.prompt,result.errorMsg);
									return false;
								}
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.saveSuccess,function(){
									addForm.reset();
									addWin.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				addWin.getForm().reset();
				addWin.hide();
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"addForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				items:[{
					xtype:"hidden",
					name:"entity.dataSource",
					value:1
				},{
					xtype:"textfield",
					readOnly:true,
					width:130,
					fieldLabel:Ext.i18n.dataSource,
					value:Ext.i18n.dataSource1
				}]
			},{
				items:[{
					xtype:"combo",
					hiddenName:"entity.valid",
					width:130,
					fieldLabel:Ext.i18n.valid,
				    mode: 'local',
				    displayField:"text",
				    valueField:"value",
				    triggerAction: 'all',
				    editable:false,
				    allowBlank:false,
				    value:1,
				    store:new Ext.data.SimpleStore({
						fields:["text","value"],
						data:[[Ext.i18n.valid1,1],[Ext.i18n.valid0,0]]
					})
				}]
			}/*,{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.vehicleCode,
					name:"entity.vehicle.vehicleCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:16,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						vehicleWin.show();
						var vehicleForm = Ext.getCmp("vehicleForm").getForm();
						var addForm = Ext.getCmp("addForm").getForm();
						vehicleForm.findField("formName").setValue("addForm");
						//选择了车型，则只加载所选车型对应的车辆
						vehicleForm.findField("modelBase").setValue(null);
						if(!Ext.isEmpty(addForm.findField("entity.modelBase").getValue())){
							vehicleForm.findField("modelBase").setValue(addForm.findField("entity.modelBase").getValue());
						}
						vehicleStore.load();
					}
				}]
			}*/,{
				layout:"column",
				defaults:{
					xtype:'panel',
					layout:"form",
					labelAlign:"right",
					labelWidth:120
				},
				items:[{
					columnWidth:.52,
					items:[{
						xtype:"textfield",
						fieldLabel:Ext.i18n.startTm,
						name:"startTm1",
						allowBlank:false,
						width:50,
						regex:/^(([0-1]{1}[0-9]{1})|(2[0-3]{1}))$/,
						regexText:Ext.i18n.tmError1,
						minLength:2,
						maxLength:2
					}]
				},{	labelWidth:5,
					columnWidth:.48,
					items:[{
						xtype:"textfield",
						fieldLabel:":",
						labelSeparator:"",
						useDefault:true,
						name:"startTm2",
						allowBlank:false,
						width:50,
						regex:/^([0-5]{1}[0-9]{1})$/,
						regexText:Ext.i18n.tmError2,
						minLength:2,
						maxLength:2
					}]
				}]
			},{
				layout:"column",
				defaults:{
					xtype:'panel',
					layout:"form",
					labelAlign:"right",
					labelWidth:120
				},
				items:[{
					columnWidth:.52,
					items:[{
						xtype:"textfield",
						fieldLabel:Ext.i18n.endTm,
						name:"endTm1",
						allowBlank:false,
						width:50,
						regex:/^(([0-1]{1}[0-9]{1})|(2[0-3]{1}))$/,
						regexText:Ext.i18n.tmError1,
						minLength:2,
						maxLength:2
					}]
				},{
					labelWidth:5,
					columnWidth:.48,
					items:[{
						xtype:"textfield",
						fieldLabel:":",
						name:"endTm2",
						allowBlank:false,
						labelSeparator:"",
						useDefault:true,
						width:50,
						regex:/^([0-5]{1}[0-9]{1})$/,
						regexText:Ext.i18n.tmError2,
						minLength:2,
						maxLength:2
					}]
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.startDept,
					name:"entity.startDept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("addForm");
						deptForm.findField("deptCodeField").setValue("entity.startDept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listDeptPage.action", true);
						deptStore.load();
					}
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.endDept,
					name:"entity.endDept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("addForm");
						deptForm.findField("deptCodeField").setValue("entity.endDept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listDeptPage.action", true);
						deptStore.load();
					}
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.ownerDept,
					name:"entity.dept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("addForm");
						deptForm.findField("deptCodeField").setValue("entity.dept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listUserDeptPage.action", true);
						deptStore.load();
					}
				}]
			}]
		}]
	})
	addWin.show();
},dlg);
//修改
dlg.on(Ext.i18n.update,function(){
	var sm = dlg.getGrid().getSelectionModel();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	if(sm.getSelections().length > 1){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	var record = sm.getSelected();
	if(1 == record.data.isUsed){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.isUsedReadOly);
		return false;
	}
	if(2 == record.data.isUsed){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.isUsed2Alert);
		return false;
	}
	var updateWin = dlg.createModalDialog();
	updateWin.add({xtype:"panel",
		title:Ext.i18n.update,
		tools:[{id:'close',
			handler:function(){
				updateWin.getForm().reset();
				updateWin.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var updateForm = Ext.getCmp("updateForm").getForm();
				if(!updateForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if('yes' == btn){
						var startTm = updateForm.findField("startTm1").getValue()+":"+updateForm.findField("startTm2").getValue();
						var endTm = updateForm.findField("endTm1").getValue()+":"+updateForm.findField("endTm2").getValue();
						updateForm.submitEx({
							url:"scheduleInfoAction_updateEntity.action",
							params:{
								'entity.startTm':startTm,
								'entity.endTm':endTm
							},
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								if(!Ext.isEmpty(result.errorMsg)){
									Ext.Msg.alert(Ext.i18n.prompt,result.errorMsg);
									return false;
								}
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.saveSuccess,function(){
									updateForm.reset();
									updateWin.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				updateWin.getForm().reset();
				updateWin.hide();
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"updateForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				items:[{
					xtype:"hidden",name:"entity.id"
				},{
					xtype:"textfield",
					readOnly:true,
					width:130,
					fieldLabel:Ext.i18n.dataSource,
					name:"dataSourceTxt"
				}]
			},{
				items:[{
					xtype:"textfield",
					readOnly:true,
					width:130,
					fieldLabel:Ext.i18n.lineOptimizeNo,
					name:"lineOptimizeNo",
					disabled:true
				}]
			},{
				items:[{
					xtype:"combo",
					fieldLabel:Ext.i18n.modelBase,
					displayField:"modelBase",
					valueField:"modelBase",
					triggerAction:"all",
					hiddenName:"entity.modelBase",
					mode:"remote",
					typeAhead:true,
					minChars:1,
					queryParam:"modelBase",
					forceSelection:true,
					allowBlank:false,
					disabled:true,
					width:130,
					listeners:{
						'beforequery': function(qe){
							delete qe.combo.lastQuery;
						}
					},
					store:new Ext.data.JsonStore({
						url:"scheduleInfoAction_listModelBase.action",
						fields:["modelBase"],
						root:"brand"
					})
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.vehicleCode,
					name:"entity.vehicle.vehicleCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:16,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						if(this.disabled){
							return false;
						}
						vehicleWin.show();
						var vehicleForm = Ext.getCmp("vehicleForm").getForm();
						var updateForm = Ext.getCmp("updateForm").getForm();
						vehicleForm.findField("formName").setValue("updateForm");
						//选择了车型，则只加载所选车型对应的车辆
						if(!Ext.isEmpty(updateForm.findField("entity.modelBase").getValue())){
							vehicleForm.findField("modelBase").setValue(updateForm.findField("entity.modelBase").getValue());
						}
						vehicleStore.load();
					}
				}]
			},{
				layout:"column",
				defaults:{
					xtype:'panel',
					layout:"form",
					labelAlign:"right",
					labelWidth:120
				},
				items:[{
					columnWidth:.52,
					items:[{
						xtype:"textfield",
						fieldLabel:Ext.i18n.startTm,
						name:"startTm1",
						allowBlank:false,
						readOnly:true,
						width:50,
						regex:/^(([0-1]{1}[0-9]{1})|(2[0-3]{1}))$/,
						regexText:Ext.i18n.tmError1,
						minLength:2,
						maxLength:2
					}]
				},{	labelWidth:5,
					columnWidth:.48,
					items:[{
						xtype:"textfield",
						fieldLabel:":",
						labelSeparator:"",
						useDefault:true,
						name:"startTm2",
						allowBlank:false,
						readOnly:true,
						width:50,
						regex:/^([0-5]{1}[0-9]{1})$/,
						regexText:Ext.i18n.tmError2,
						minLength:2,
						maxLength:2
					}]
				}]
			},{
				layout:"column",
				defaults:{
					xtype:'panel',
					layout:"form",
					labelAlign:"right",
					labelWidth:120
				},
				items:[{
					columnWidth:.52,
					items:[{
						xtype:"textfield",
						fieldLabel:Ext.i18n.endTm,
						name:"endTm1",
						allowBlank:false,
						readOnly:true,
						width:50,
						regex:/^(([0-1]{1}[0-9]{1})|(2[0-3]{1}))$/,
						regexText:Ext.i18n.tmError1,
						minLength:2,
						maxLength:2
					}]
				},{
					labelWidth:5,
					columnWidth:.48,
					items:[{
						xtype:"textfield",
						fieldLabel:":",
						name:"endTm2",
						allowBlank:false,
						labelSeparator:"",
						useDefault:true,
						readOnly:true,
						width:50,
						regex:/^([0-5]{1}[0-9]{1})$/,
						regexText:Ext.i18n.tmError2,
						minLength:2,
						maxLength:2
					}]
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.startDept,
					name:"entity.startDept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						if(this.readOnly){
							return false;
						}
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("updateForm");
						deptForm.findField("deptCodeField").setValue("entity.startDept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listDeptPage.action", true);
						deptStore.load();
					}
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.endDept,
					name:"entity.endDept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						if(this.readOnly){
							return false;
						}
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("updateForm");
						deptForm.findField("deptCodeField").setValue("entity.endDept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listDeptPage.action", true);
						deptStore.load();
					}
				}]
			},{
				items:[{
					xtype:"combo",
					hiddenName:"entity.valid",
					width:130,
					fieldLabel:Ext.i18n.valid,
				    mode: 'local',
				    displayField:"text",
				    valueField:"value",
				    triggerAction: 'all',
				    editable:false,
				    allowBlank:false,
				    value:1,
				    store:new Ext.data.SimpleStore({
						fields:["text","value"],
						data:[[Ext.i18n.valid1,1],[Ext.i18n.valid0,0]]
					})
				}]
			},{
				items:[{
					xtype:"trigger",
					triggerClass : 'x-form-search-trigger',
					fieldLabel:Ext.i18n.ownerDept,
					name:"entity.dept.deptCode",
					allowBlank:false,
					width:130,
					getErrors:getErrors,
					maxLength:30,
					maxLengthText:Ext.i18n.overLength,
					onTriggerClick : function() {
						deptWin.show();
						var deptForm = Ext.getCmp("deptForm").getForm();
						deptForm.findField("formName").setValue("updateForm");
						deptForm.findField("deptCodeField").setValue("entity.dept.deptCode");
						deptStore.proxy.setUrl("scheduleInfoAction_listUserDeptPage.action", true);
						deptStore.load();
					}
				}]
			}]
		}]
	})
	updateWin.show();
	var updateForm = Ext.getCmp("updateForm").getForm();
	//设置可修改值
	if(2 == record.data.dataSource){
		//设置出车网点、收车网点只读、车牌号可编辑
		updateForm.findField("entity.startDept.deptCode").setReadOnly(true);
		updateForm.findField("entity.endDept.deptCode").setReadOnly(true);
		updateForm.findField("entity.vehicle.vehicleCode").setDisabled(false);
		//设置出车网点、收车网点不可编辑
		updateForm.findField("entity.startDept.deptCode").setEditable(false);
		updateForm.findField("entity.endDept.deptCode").setEditable(false);
	}else{
		//设置出车网点、收车网点可编辑、车牌号不可用
		updateForm.findField("entity.startDept.deptCode").setReadOnly(false);
		updateForm.findField("entity.endDept.deptCode").setReadOnly(false);
		updateForm.findField("entity.vehicle.vehicleCode").setDisabled(true);
		//设置出车网点、收车网点可编辑
		updateForm.findField("entity.startDept.deptCode").setEditable(true);
		updateForm.findField("entity.endDept.deptCode").setEditable(true);
	}
	//设值
	updateForm.findField("entity.modelBase").setValue(record.data.modelBase);
	if(!Ext.isEmpty(record.data.startTm)){
		updateForm.findField("startTm1").setValue(record.data.startTm.substring(0,2));
		updateForm.findField("startTm2").setValue(record.data.startTm.substring(3,5));
	}
	if(!Ext.isEmpty(record.data.endTm)){
		updateForm.findField("endTm1").setValue(record.data.endTm.substring(0,2));
		updateForm.findField("endTm2").setValue(record.data.endTm.substring(3,5));
	}
	if(!Ext.isEmpty(record.data.startDept)){
		updateForm.findField("entity.startDept.deptCode").setValue(record.data.startDept.deptCode);
	}
	if(!Ext.isEmpty(record.data.endDept)){
		updateForm.findField("entity.endDept.deptCode").setValue(record.data.endDept.deptCode);
	}
	updateForm.findField("entity.valid").setValue(record.data.valid);
	if(1 == record.data.valid){
		updateForm.findField("entity.valid").setRawValue(Ext.i18n.valid1);
	}else{
		updateForm.findField("entity.valid").setRawValue(Ext.i18n.valid0);
	}
	updateForm.findField("entity.id").setValue(record.data.id);
	if(2 == record.data.dataSource){
		updateForm.findField("dataSourceTxt").setValue(Ext.i18n.dataSource2);
	}else{
		updateForm.findField("dataSourceTxt").setValue(Ext.i18n.dataSource1);
	}
	updateForm.findField("lineOptimizeNo").setValue(record.data.lineOptimizeNo);
	if(!Ext.isEmpty(record.data.dept)){
		updateForm.findField("entity.dept.deptCode").setValue(record.data.dept.deptCode);
	}
	if(!Ext.isEmpty(record.data.vehicle)){
		updateForm.findField("entity.vehicle.vehicleCode").setValue(record.data.vehicle.vehicleCode);
	}
},dlg)
//删除
dlg.on(Ext.i18n.deleted,function(){
	var sm = dlg.getGrid().getSelectionModel();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	var s = sm.getSelections();
	var ids = [];
	for(var i=0;i<s.length;i++){
		if(s[i].get("valid") == 1){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.notAllowDelete1);
			return false;
		}
		if(s[i].get("isUsed") == 1){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.notAllowDelete2);
			return false;
		}
		ids.push(s[i].get("id"));
	}
	Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.deleteConfirm,function(btn){
		if("yes" == btn){
			Ext.Ajax.requestEx({
				url:"scheduleInfoAction_deleteByIds.action",
				params:{
					"ids":ids
				},
				successCallback:function(result){
					if(Ext.isEmpty(result)){
						Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
						return false;
					}
					if(!Ext.isEmpty(result.errorMsg)){
						Ext.Msg.alert(Ext.i18n.prompt,result.errorMsg);
						return false;
					}
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.deleteSuccess,function(){
						dlg.getGrid().getStore().reload();
					});
				}
			})
		}
	})
},dlg)
//导入
dlg.on(Ext.i18n.upload,function(){
	var win = this.createModalDialog();
	win.add({xtype:"form",
		fileUpload:true,
		title:Ext.i18n.upload,
		tools:[{id:'close',handler:function(){win.hide();}}],
		tbar:[{text:Ext.i18n.save,cls:'x-btn-normal',minWidth:60,handler:function(){
				var form = win.getComponent(0).getForm();
				var fileName = form.findField("uploadFile").getValue();
				if(Ext.isEmpty(fileName)){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseFile);
					return false;
				}
				if(!/.xls$/.test(fileName)){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseExcel);
					return false;
				}
				form.getEl().dom.enctype = 'multipart/form-data';
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmUpload,function(btn){
					if(btn == 'yes'){
						form.submitEx({
							url:'scheduleInfoAction_saveFile.action',
							params:{
								fileName:fileName
							},
							fileUpload:true,
							timeout:60*30,
							method:"post",
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								if(!Ext.isEmpty(result.errorMsg)){
									var msgStr = "";
									var msgs = new Array();
									msgs = result.errorMsg.replace(/;$/,"").split(";");
									for(var i = 0; i < msgs.length; i++) {
										msgStr = msgStr+"<p>"+msgs[i]+"</p>";
									}
									var winInfo = new Ext.Window({
										title:Ext.i18n.wrongInfo,
										width:780,
										height:400,
										layout:'fit',
										modal:true,
										autoScroll:false,
										items:[{xtype:'panel',frame:true,autoScroll:true,html:msgStr}]
									});
									winInfo.show();
									return false;
								}
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.uploadSuccess,function(){
									win.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,cls:'x-btn-normal',minWidth:60,handler:function(){win.hide();}
		}],
		items:[{xtype:"panel",
				layout:"form",
				height:480-24*2,
				frame:true,
				labelWidth:80,
				labelAlign:"right",
				fileUpload:true,
				items:[{xtype:'panel',html:"<div>"+Ext.i18n.html1+"</div>"
						+"<div style='text-indent:2em;font-weight:normal;color:red;padding-left:1em;'>1.&nbsp;&nbsp;&nbsp;&nbsp;"+Ext.i18n.html2+"</div>"
						+"<div style='text-indent:2em;font-weight:normal;color:red;padding-left:1em;'>2.&nbsp;&nbsp;&nbsp;&nbsp;"+Ext.i18n.html3+"</div>"
						+"<div style='text-indent:2em;font-weight:normal;color:red;padding-left:1em;'>3.&nbsp;&nbsp;&nbsp;&nbsp;"+Ext.i18n.html5+"</div>"
						+"<div style='text-indent:2em;font-weight:normal;color:red;padding-left:1em;'>4.&nbsp;&nbsp;&nbsp;&nbsp;"+Ext.i18n.html4+"</div>"
						,style:'padding-left:4em;line-height:21px;'
					},{xtype:"textfield",
						height:21,
						width:604,
						inputType:"file",
						name:"uploadFile",
						labelStyle:"margin-top:4px;",
						fieldLabel:Ext.i18n.filePath
					},{xtype:"panel",
							html:Ext.i18n.templateLabel+':&nbsp;&nbsp;&nbsp;&nbsp;<a style="text-decoration:none;color:blue;" ' +
									'href="../pages/vmsarrange/template/scheduleInfo.xls" target="blank">scheduleInfo.xls</a>',
							style:'margin-left:2.5em;width:100%;height:23px;line-height:23px;'
					}]
			}]
	});
	win.show();
},dlg);
//导出
dlg.on(Ext.i18n.exported,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmDownload,function(button,text){
		if(button == "yes"){
			dlg.getQueryForm().submitEx({
				url:"scheduleInfoAction_listReport.action",
				timeout:60*30,
				successCallback:function(result){
					if(Ext.isEmpty(result)){
						Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
						return false;
					}
					if(!Ext.isEmpty(result.errorMsg)){
						Ext.Msg.alert(Ext.i18n.prompt,result.errorMsg);
						return false;
					}
					if(Ext.isEmpty(result.fileName)){
						Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.fileNameNull);
						return false;
					}
					window.location = "../vmsarrange/scheduleInfoAction_downloadFile.action?fileName="+encodeURI(result.fileName);
				}
			})
		}
	})
},dlg);