//<%@ page language="java" contentType="text/html; charset=UTF-8"%>
// 请求有效时间十五分钟
Ext.Ajax.timeout = 15*60*1000;
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = "side";
Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
Ext.i18n = {
	search:"${app:i18n('search')}",
	add:"${app:i18n_def('scheduleArrange.js.34','配班')}",
	update:"${app:i18n('edit')}",
	exported:"${app:i18n('export')}",
	prompt:"${app:i18n('prompt')}",
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
	dataSourceAll:"${app:i18n_def('common.js.chooseAll','全部')}",
	deptCode:"${app:i18n_def('common.js.deptCode','网点代码')}",
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
	
	dataSource:"${app:i18n_def('scheduleInfo.js.1','数据源')}",
	dataSource1:"${app:i18n_def('scheduleInfo.js.2','手工录入')}",
	dataSource2:"${app:i18n_def('scheduleInfo.js.3','路径优化')}",
	modelBase:"${app:i18n_def('scheduleInfo.js.4','车型')}",
	lineOptimizeNo:"${app:i18n_def('scheduleInfo.js.5','路径优化线路编号')}",
	startTm:"${app:i18n_def('scheduleInfo.js.6','出车时间')}",
	endTm:"${app:i18n_def('scheduleInfo.js.7','收车时间')}",
	startDept:"${app:i18n_def('scheduleInfo.js.8','始发网点')}",
	endDept:"${app:i18n_def('scheduleInfo.js.9','目的网点')}",
	ownerDept:"${app:i18n_def('scheduleInfo.js.14','归属网点')}",
	tmError1:"${app:i18n_def('scheduleInfo.js.16','格式错误，正确格式例如:09(范围:00到23)')}",
	tmError2:"${app:i18n_def('scheduleInfo.js.17','格式错误，正确格式例如:09(范围:00到59)')}",
	pleaseChooseFile:"${app:i18n_def('scheduleInfo.js.20','请选择文件')}",
	pleaseChooseExcel:"${app:i18n_def('scheduleInfo.js.21','文件格式有误，只能选择excel(.xls后缀文件)')}",
	confirmUpload:"${app:i18n_def('scheduleArrange.js.1','该操作可能需要较长时间,是否导入')}",
	html1:"${app:i18n_def('scheduleInfo.js.23','注意事项')}",
	html2:"${app:i18n_def('scheduleArrange.js.2','请认真填写序号列的值，提示信息将以您填写的序号作为提示的行号')}",
	html3:"${app:i18n_def('scheduleArrange.js.3','提示信息中的线路明细重复指：出收车时间,始发网点,目的网点均相同')}",
	html5:"${app:i18n_def('scheduleInfo.js.40','请不要在数据中间或尾部留有空行')}",
	html4:"${app:i18n_def('scheduleInfo.js.26','请阅读模板中的注意事项及业务规则,并遵循业务规则进行操作')}",
	filePath:"${app:i18n_def('scheduleInfo.js.27','文件路径')}",
	templateLabel:"${app:i18n_def('scheduleInfo.js.28','导入模板')}",
	wrongInfo:"${app:i18n_def('scheduleInfo.js.29','错误信息')}",
	confirmDownload:"${app:i18n_def('scheduleInfo.js.30','导出数据过多时可能需要较长时间，是否导出')}",
	fileNameNull:"${app:i18n_def('scheduleInfo.js.31','生成的文件名称为空,请重新导出')}",
	arrangeNo:"${app:i18n_def('scheduleArrange.js.4','班次代码')}",
	infos:"${app:i18n_def('scheduleArrange.js.5','线路明细数据')}",
	choosedInfo:"${app:i18n_def('scheduleArrange.js.6','已选的线路')}",
	allInfo:"${app:i18n_def('scheduleArrange.js.7','所有的线路')}",
	cancelChoosed:"${app:i18n_def('scheduleArrange.js.8','去掉选中线路')}",
	confirmChoosed:"${app:i18n_def('scheduleArrange.js.9','选为配班线路')}",
	pleaseEnterStartTm:"${app:i18n_def('scheduleArrange.js.10','请输入出车时间')}",
	hasRepeat:"${app:i18n_def('scheduleArrange.js.11','不能同时选择两笔出车时间相同的记录')}",
	choosedNull:"${app:i18n_def('scheduleArrange.js.12','已选线路不能为空')}",
	detail:"${app:i18n_def('scheduleArrange.js.13','查看明细')}",
	validState:"${app:i18n_def('scheduleArrange.js.14','状态')}",
	refreshArrangeNo:"${app:i18n_def('scheduleArrange.js.15','重新获取班次代码')}",
	add1:"${app:i18n_def('scheduleArrange.js.16','机动班新增')}",
	arrangeNo1:"${app:i18n_def('scheduleArrange.js.17','机动班次代码')}",
	startTmTxt:"${app:i18n_def('scheduleArrange.js.18','上班时间')}",
	endTmTxt:"${app:i18n_def('scheduleArrange.js.19','下班时间')}",
	isUsedState:"${app:i18n_def('scheduleInfo.js.32','状态')}",
	isUsed1:"${app:i18n_def('scheduleArrange.js.23','已排班')}",
	isUsed0:"${app:i18n_def('scheduleArrange.js.24','未排班')}",
	isUsedReadOly:"${app:i18n_def('scheduleArrange.js.36','状态为已排班的记录不允许修改')}",
	alertInfo:"${app:i18n_def('scheduleArrange.js.20','(注意:已选线路若被修改，此处将仍显示修改前的线路信息)')}",
	pleaseChooseArr:"${app:i18n_def('scheduleArrange.js.21','请选择一条非机动班记录')}",
	pleaseChooseArr2:"${app:i18n_def('scheduleArrange.js.22','机动班不可查看明细，请选择一条非机动班记录')}",
	saveConfirm:"${app:i18n_def('common.js.saveConfirm','是否保存')}",
	editValid:"${app:i18n_def('scheduleArrange.js.48','修改有效性')}",
	chooseVehicle:"${app:i18n_def('scheduleArrange.js.26','车牌号')}",
	chooseVehicleAlert:"${app:i18n_def('scheduleArrange.js.27','已选线路车牌号不能为空，请点击“车牌号”按钮选择车牌号')}",
	choose:"${app:i18n_def('scheduleInfo.js.10','选择')}",
	pleaseChooseInput:"${app:i18n_def('scheduleArrange.js.28','只能选择数据源为手工录入的记录')}",
	confirmUpdate:"${app:i18n_def('scheduleArrange.js.29','是否保存(注意：修改为无效时，系统将清除今天之后该班次的排班信息)')}",
	saveConfirmBefore:"${app:i18n_def('scheduleArrange.js.30','1、修改后本班次变为无效；2、明天起已引用此配班的班次清空')}",
	saveConfirmNew:"${app:i18n_def('scheduleArrange.js.33','本班次为新班次(注意班次代码已经重新生成)，是否保存？')}",
	morethan16:"${app:i18n_def('scheduleArrange.js.35','出勤时长超过16个小时，存在疲劳驾驶风险，是否继续保存？')}"
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
Ext.override(Dialog,{
	createModalDialog:function(){
		var _win = Ext.getCmp("Dialog-win");
		if( Ext.isEmpty(_win,false) ){
			_win = new Ext.Window({
				id:"Dialog-win",
				width:800,
				height:550,
				modal:true,
				border:false,
				bodyBorder:false,
				closable:false,
				resizable:false,
				layout:'fit',
				closeAction:'hide',
				getForm:function(){
					var cmpCt = this.getComponent(0);
					if( /form/gi.test(cmpCt.getXType()) ){
						return cmpCt.getForm();
					}	
					return cmpCt.getComponent(0).getForm();
				},
				listeners:{hide:function(){
					_win.items.each(function(item,i){
						_win.remove(_win.getComponent(i),true);
					});
				}}
			});
		}
		return _win;	
	}
})
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
	<app:isPermission code="/vmsarrange/scheduleArrange_search.action">
	dlg.addBizButton({text:Ext.i18n.search});
	</app:isPermission>
	//查看明细
	<app:isPermission code="/vmsarrange/scheduleArrange_detail.action">
	this.addBizButton({text : Ext.i18n.detail});
	</app:isPermission>
	//新增按钮
	<app:isPermission code="/vmsarrange/scheduleArrange_append.action">
	this.addBizButton({text : Ext.i18n.add});
	</app:isPermission>
	//修改按钮
	<app:isPermission code="/vmsarrange/scheduleArrange_modify.action">
	this.addBizButton({text :Ext.i18n.update});
	</app:isPermission>
	//导入
	<app:isPermission code="/vmsarrange/scheduleArrange_upload.action">
	this.addBizButton({text:Ext.i18n.upload,cls:'x-btn-normal'});
  	</app:isPermission>
  	//导出
  	<app:isPermission code="/vmsarrange/scheduleArrange_download.action">
  	this.addBizButton({text:Ext.i18n.exported,cls:'x-btn-normal'});
  	</app:isPermission>
	//机动班新增按钮
	<app:isPermission code="/vmsarrange/scheduleArrange_appendSpecial.action">
	this.addBizButton({text : Ext.i18n.add1});
	</app:isPermission>	
	//置为无效
  	<app:isPermission code="/vmsarrange/arrScheduleArrange_updatevalid.action">
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
		columnWidth:.5,
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
	//班次代码
	queryCt.add({
		columnWidth:.5,
		labelWidth:80,
		labelAlign:'right',
		items:[{
			xtype:"textfield",
			fieldLabel:Ext.i18n.arrangeNo,
			name:"arrangeNo",
			width:120
		}]
	});
	//是否有效
	queryCt.add({
		columnWidth:.5,
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
		columnWidth:.5,
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
    	url:"scheduleArrangeAction_listPage.action",
    	root:"page",
    	totalProperty:"total",
	  	fields:['id',
	  	        'arrangeNo',
	  	        'valid',
	  	        'startTm',
	  	        'endTm',
	  	        'dept',
	  	        'startDept',
	  	        'endDept',
	  	        'createdEmpCode',
			  	'createdTm',
			  	'modifiedEmpCode',
			  	'modifiedTm',
			  	'arrangeType',
			  	'scheduleArrangeInfos',
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
				{header:Ext.i18n.arrangeNo,dataIndex:"arrangeNo",sortable:false,width:110
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
				},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
				},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
				},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
				},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
				},{header:Ext.i18n.isUsedState,dataIndex:"isUsed",sortable:false,width:60,
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
//选择网点
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
				var records = sm.getSelections();
				var ids = [];
				Ext.each(records,function(item){
					this.push(item.data.id);
				},ids);
				if(!form.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmUpdate,function(btn){
					if(btn == 'yes'){
						form.submitEx({
							url:'scheduleArrangeAction_updateValid.action',
							timeout:2*60,
							params:{
								recordIds:ids
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
			    value:null,
			    forceSelection:true,
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
	validWin.show();
	var form = validWin.getComponent(0).getForm();
	form.reset();
},dlg)
var addSm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
var addStore = new Ext.data.JsonStore({
	url:"scheduleInfoAction_listPageForArrange.action",
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
		  	'modifiedTm'],
  	listeners:{
  		beforeLoad:function(){
  			var startTm = Ext.getCmp("searchString").getValue();
  			var grid = Ext.getCmp("arrangeGrid");
  			var recordId = Ext.getCmp("winForm").getForm().findField("recordId").getValue();
  			var deptId = dlg.getQueryForm().findField("deptId").getValue();
  			if(!Ext.isEmpty(Ext.getCmp("updateDeptId")) 
  				&& !Ext.isEmpty(Ext.getCmp("updateDeptId").getValue())){
  				deptId = Ext.getCmp("updateDeptId").getValue();
  			}
  			this.baseParams['deptId'] = deptId;
  			this.baseParams['recordId'] = recordId;
  			this.baseParams['startTm'] = startTm;
  			this.baseParams['limit'] = grid.getTopToolbar().getComponent(2).pageSize;
  		},
  		load:function(){
  			setChoosedRed();
  		}
  	}
});
var addAllSm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//去掉选中班次
function removeChoose(){
	var sm = Ext.getCmp("choosedGrid").getSelectionModel();
	var store = Ext.getCmp("choosedGrid").getStore();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	store.remove(sm.getSelections());
	setChoosedRed();
}
//选为配班班次
function chooseArrange(){
	var sm = Ext.getCmp("arrangeGrid").getSelectionModel();
	var grid = Ext.getCmp("choosedGrid");
	var store = grid.getStore();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	//获取新选中的数据
	var data = [];
	Ext.each(sm.getSelections(),function(item){
		var plant = new store.recordType({
				'id':item.data.id,
	  	        'dataSource':item.data.dataSource,
	  	        'lineOptimizeNo':item.data.lineOptimizeNo,
	  	        'modelBase':item.data.modelBase,
	  	        'startTm':item.data.startTm,
	  	        'endTm':item.data.endTm,
	  	        'startDept':item.data.startDept,
	  	        'endDept':item.data.endDept,
	  	        'dept':item.data.dept,
	  	        'vehicle':item.data.vehicle,
	  	        'valid':item.data.valid
			});
		this.push(plant);
	},data);
	//获取旧数据
	if(store.getCount()>0){
		var oldData = store.getRange();
		Ext.each(oldData,function(item){
			this.push(item);
		},data);
	}
	//排序后重新插入到已选择列表
	var flg = false;
	data.sort(function(a1,a2){
		if(Ext.isEmpty(a1) || Ext.isEmpty(a2)
			|| Ext.isEmpty(a1.data) || Ext.isEmpty(a2.data)
			|| Ext.isEmpty(a1.data.startTm) || Ext.isEmpty(a2.data.startTm)){
			return 0;
		}
		if(!/^[0-9]{2}:[0-9]{2}$/.test(a1.data.startTm)){
			return 0;
		}
		if(!/^[0-9]{2}:[0-9]{2}$/.test(a2.data.startTm)){
			return 0;
		}
		var a1part1 = eval(a1.data.startTm.substring(0,2));
		var a1part2 = eval(a1.data.startTm.substring(3,5));
		var a2part1 = eval(a2.data.startTm.substring(0,2));
		var a2part2 = eval(a2.data.startTm.substring(3,5));
		if(a1part1 > a2part1){
			return 1;
		}
		if(a1part1 < a2part1){
			return -1;
		}
		if(a1part2 > a2part2){
			return 1;
		}
		if(a1part2 < a2part2){
			return -1;
		}
		//如果有重复数据则提示
		flg = true;
		return 0;
	})
	//出车时间重复则提示
	if(flg){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.hasRepeat);
		return false;
	}
	if(store.getCount()>0){
		store.removeAll();
	}
    store.insert(0,data);
    sm.clearSelections();
    setChoosedRed();
}
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
function chooseVehicleFn(){
	if(!vehicleSm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	var record = vehicleSm.getSelected();
	var sm = Ext.getCmp("choosedGrid").getSelectionModel().getSelections();
	Ext.each(sm,function(item){
		item.set('vehicle',{'vehicleCode':record.data.vehicleCode});
	})
	vehicleWin.hided();
}
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
		handler:chooseVehicleFn
	},{
		text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
		handler:function(){
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
		items:[{xtype:"hidden",name:"formName"},{
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
		listeners: {
			'dblclick': chooseVehicleFn 
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
//新增
dlg.on(Ext.i18n.add,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	var addWin = dlg.createModalDialog();
	addWin.add({xtype:"panel",
		title:Ext.i18n.add,
		tools:[{id:'close',
			handler:function(){
				addWin.getForm().reset();
				Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
				Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
				addWin.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var addForm = addWin.getForm();
				if(!addForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				var store = Ext.getCmp("choosedGrid").getStore();
				var vView = Ext.getCmp("choosedGrid").getView(); 
				if(store.getCount()<1){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.choosedNull);
					return false;
				}
				//检验数据
				var isValid = false;
				//清空已有的错误提示
				for(var i=0;i<store.getCount();i++ ){
					vView.getRow(i).title="";
				}
				for(var i=0;i<store.getCount();i++ ){
					var item = store.getAt(i);
					//车牌号校验-手工录入
					if(item.get("dataSource") == 1 && Ext.isEmpty(item.get("vehicle"))){
						vView.getCell(i,1).style.color="red";
						vView.getRow(i).title=Ext.i18n.chooseVehicleAlert;
				   		isValid = true;
				   		break;
					}
				}
				if(isValid){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.chooseVehicleAlert);
					return false;	
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if('yes' == btn){
						var params = {};
						Ext.each(store.getRange(),function(item,i){
							params['entity.infoArrangeList['+i+"].id"] = item.data.id;
							params['entity.infoArrangeList['+i+"].startTm"] = item.data.startTm;
							params['entity.infoArrangeList['+i+"].endTm"] = item.data.endTm;
							params['entity.infoArrangeList['+i+"].vehicle.vehicleCode"] = item.data.vehicle.vehicleCode;
							if(!Ext.isEmpty(item.data.dept)){
								params['entity.infoArrangeList['+i+"].dept.id"] = item.data.dept.id;
							}
						});
						//校验是否超过16个小时
						addForm.submitEx({
							url:"scheduleArrangeAction_listCheckEntity.action",
							timeout:2*60,
							params:params,
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								//超过16个小时则提示
								if(!Ext.isEmpty(result.errorMsg) && result.errorMsg!="ok"){
									Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.morethan16,function(btn){
										if('yes' == btn){
											addForm.submitEx({
												url:"scheduleArrangeAction_saveEntity.action",
												timeout:2*60,
												params:params,
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
														Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
														Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
														addWin.hide();
														dlg.getGrid().getStore().reload();
													});
												}
											})
										}
									})
								}else{
									//没有超过16个小时则保存
									addForm.submitEx({
										url:"scheduleArrangeAction_saveEntity.action",
										timeout:2*60,
										params:params,
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
												Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
												Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
												addWin.hide();
												dlg.getGrid().getStore().reload();
											});
										}
									})
								}
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				addWin.getForm().reset();
				Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
				Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
				addWin.hide();
			}
		},{text:Ext.i18n.refreshArrangeNo,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				Ext.Ajax.requestEx({
					url:"scheduleArrangeAction_listArrangeNo.action",
					params:{
						'deptCode':dlg.getQueryForm().findField("deptCode").getValue(),
						'arrangeType':2
					},
					successCallback:function(result){
						if(Ext.isEmpty(result)){
							Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
							return false;
						}
						if(!Ext.isEmpty(result.arrangeNo)){
							if(!Ext.isEmpty(addWin) && addWin.isVisible()){
								addWin.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
							}
						}
					}
				})
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"winForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				columnWidth:.33,
				items:[{xtype:"hidden",name:"entity.arrangeType",value:2},
					{xtype:'hidden',name:"recordId"},
					{xtype:"hidden",name:"entity.dept.id"},{
					xtype:"textfield",
					fieldLabel:Ext.i18n.deptCode,
					readOnly:true,
					name:"entity.dept.deptCode",
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.arrangeNo,
					name:"entity.arrangeNo",
					readOnly:true,
					maxLength:50,
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"combo",
					hiddenName:"entity.valid",
					width:100,
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
				columnWidth:1,
				xtype:"fieldset",
				style:"padding-left:10px;padding-right:10px;padding-top:10px;",
				height:430,
				title:Ext.i18n.infos,
				layout:"border",
				items:[{
					region:"center",
					tbar:[{
						xtype:'label',
						style:'font-weight:bold;font-size:12;color:#15428b;',
						text:Ext.i18n.choosedInfo
					},'-',{xtype:'label',
						style:'color:#15428b;',
						text:Ext.i18n.alertInfo
					},'->',{
						text:Ext.i18n.chooseVehicle,minWidth:60,cls:'x-btn-normal',
						handler:function(){
							if(!Ext.getCmp("choosedGrid").getSelectionModel().hasSelection()){
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
								return false;
							}
							var records = Ext.getCmp("choosedGrid").getSelectionModel().getSelections();
							var flg = false;
							Ext.each(records,function(record){
								if(record.data.dataSource == 2){
									flg = true;
								}
							})
							if(flg){
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseInput);
								return false;
							}
							vehicleWin.show();
							Ext.getCmp('vehicleGrid').getStore().load();
						}
					},{text:Ext.i18n.cancelChoosed,minWidth:60,cls:'x-btn-normal',
						handler:function(){
							removeChoose();
						}
					}],
					xtype:'grid',
					height:180,
					id:"choosedGrid",
					border:false,
					style:"border: 1px solid #8db2e3;",
					enableHdMenu:false,
					sm:addSm,
					listeners: {
						'dblclick': removeChoose 
					},
					store:new Ext.data.ArrayStore({
					  	fields:['id',
					  	        'dataSource',
					  	        'lineOptimizeNo',
					  	        'modelBase',
					  	        'startTm',
					  	        'endTm',
					  	        'startDept',
					  	        'endDept',
					  	        'dept',
					  	        'vehicle',
					  	        'valid'],
						data:[]
				  	}),
					columns:[addSm,
							{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:55,
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
							},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:105
							},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:70
							},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
							},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
							},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
							},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
							},{header:Ext.i18n.ownerDept,dataIndex:"dept",sortable:false,width:85,
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
							},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:65,
								rendererCall:function(v){
									if(Ext.isEmpty(v)){
										return '';
									}
									return v.vehicleCode;
								}
							},{header:Ext.i18n.validState,dataIndex:"valid",sortable:false,width:40,
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
					}]
				},{
					region:'south',
					height:230,
					xtype:'grid',
					id:"arrangeGrid",
					border:false,
					style:"border-bottom: 1px solid #8db2e3;border-left: 1px solid #8db2e3;border-right: 1px solid #8db2e3;",
					loadMask: true,
					enableHdMenu:false,
					sm:addAllSm,
					store:addStore,
					listeners: {
						'dblclick': chooseArrange 
					},
					tbar:[{
						xtype:'label',
						style:'font-weight:bold;font-size:12;color:#15428b;',
						text:Ext.i18n.allInfo
					},'-',new Ext.PagingToolbar({
				        pageSize: 10,
				        store: addStore,
				        displayInfo: true
			    	}),'->',{
			    		xtype:"trigger",
						triggerClass : 'x-form-search-trigger',
						emptyText:Ext.i18n.pleaseEnterStartTm,
						id:"searchString",
						onTriggerClick : function() {
							Ext.getCmp('arrangeGrid').getStore().load();
						}
			    	},'-',{text:Ext.i18n.confirmChoosed,minWidth:60,cls:'x-btn-normal',
			    		handler:function(){
							chooseArrange();
			    		}
					}],      
					columns:[addAllSm,
						{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:55,
							rendererCall:function(v,m,record,idx){
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
						},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:105
						},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:70
						},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
						},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
						},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
						},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
						},{header:Ext.i18n.ownerDept,dataIndex:"dept",sortable:false,width:85,
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
						},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:65,
							rendererCall:function(v){
								if(Ext.isEmpty(v)){
									return '';
								}
								return v.vehicleCode;
							}
						},{header:Ext.i18n.validState,dataIndex:"valid",sortable:false,width:40,
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
					}]
				}]
			}]
		}]
	})
	addWin.show();
	addWin.getForm().findField("entity.dept.id").setValue(dlg.getQueryForm().findField("deptId").getValue());
	addWin.getForm().findField("entity.dept.deptCode").setValue(dlg.getQueryForm().findField("deptCode").getValue());
	Ext.getCmp('arrangeGrid').getStore().load();
	//获取班次代码
	Ext.Ajax.requestEx({
		url:"scheduleArrangeAction_listArrangeNo.action",
		params:{
			'deptCode':dlg.getQueryForm().findField("deptCode").getValue(),
			'arrangeType':2
		},
		successCallback:function(result){
			if(Ext.isEmpty(result)){
				Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
				return false;
			}
			if(!Ext.isEmpty(result.arrangeNo)){
				if(!Ext.isEmpty(addWin) && addWin.isVisible()){
					addWin.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
				}
			}
		}
	})
},dlg);
function setChoosedRed(){
	//获取旧数据
	var choosedStore = Ext.getCmp("choosedGrid").getStore();
	var allStore = Ext.getCmp("arrangeGrid").getStore();
	var grid = Ext.getCmp("arrangeGrid");
	if(allStore.getCount()<1){
		return false;
	}
	//颜色全部清空
	for(var i=0;i<allStore.getCount();i++ ){
		grid.getView().getRow(i).style.backgroundColor="#FFFFFF";
	}
	if(choosedStore.getCount()<1 ){
		return false;
	}
	var choosedIds = [];
	if(choosedStore.getCount()>0){
		var oldData = choosedStore.getRange();
		Ext.each(oldData,function(item){
			this.push(item.data.id);
		},choosedIds);
	}
	//检验数据
	for(var i=0;i<allStore.getCount();i++ ){
		var item = allStore.getAt(i);
		var flg = false;
		Ext.each(choosedIds,function(id){
			if(id == item.data.id){
				flg = true;
			}
		})
		if(flg){
  			grid.getView().getRow(i).style.backgroundColor="#FF8888";
		}
	}
}
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
	if(1 == record.data.isUsed && record.data.arrangeType == 1){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.isUsedReadOly);
		return false;
	}
	//机动班修改
	if(record.data.arrangeType == 1){
		update1(record);
	}else{
		//非机动班修改
		var updateWin = dlg.createModalDialog();
		if(1 == record.data.isUsed && 1 == record.data.valid){
			Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirmBefore,function(btn){
				if(btn=="yes"){
					var recordIds = [];
					recordIds.push(record.data.id);
					Ext.Ajax.requestEx({
						url:'scheduleArrangeAction_updateValid.action',
						params:{
							recordIds:recordIds,
							valid:0
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
							updateWin = addItem(updateWin);
							updateWin.show();
							updateWin.getForm().findField("entity.id").setValue(record.data.id);
							updateWin.getForm().findField("recordId").setValue(record.data.id);
							updateWin.getForm().findField("entity.valid").setValue(1);
							updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid1);
							if(!Ext.isEmpty(record.data.dept)){
								Ext.getCmp("updateDeptId").setValue(record.data.dept.id);
								updateWin.getForm().findField("deptCode").setRawValue(record.data.dept.deptCode);
							}
							//加载以选中班次和所有班次
							if(record.data.scheduleArrangeInfos){
								var infos = record.data.scheduleArrangeInfos;
								var data = [];
								Ext.each(infos,function(item){
									this.push([item.scheduleInfo.id,
									  	        item.scheduleInfo.dataSource,
									  	        item.scheduleInfo.lineOptimizeNo,
									  	        item.scheduleInfo.modelBase,
									  	        item.scheduleInfo.startTm,
									  	        item.scheduleInfo.endTm,
									  	        item.scheduleInfo.startDept,
									  	        item.scheduleInfo.endDept,
									  	        item.scheduleInfo.dept,
									  	        item.scheduleInfo.vehicle,
									  	        item.scheduleInfo.valid]);
								},data);
								//排序
								data.sort(function(a1,a2){
									if(Ext.isEmpty(a1) || Ext.isEmpty(a2)
										|| a1.length < 5 || a2.length < 5){
										return 0;
									}
									if(!/^[0-9]{2}:[0-9]{2}$/.test(a1[4])){
										return 0;
									}
									if(!/^[0-9]{2}:[0-9]{2}$/.test(a2[4])){
										return 0;
									}
									var a1part1 = eval(a1[4].substring(0,2));
									var a1part2 = eval(a1[4].substring(3,5));
									var a2part1 = eval(a2[4].substring(0,2));
									var a2part2 = eval(a2[4].substring(3,5));
									if(a1part1 > a2part1){
										return 1;
									}
									if(a1part1 < a2part1){
										return -1;
									}
									if(a1part2 > a2part2){
										return 1;
									}
									if(a1part2 < a2part2){
										return -1;
									}
									return 0;
								})
								Ext.getCmp('choosedGrid').getStore().loadData(data);
							}
							if(1 == record.data.isUsed){
								var arrangeNo = record.data.arrangeNo;
								//获取班次代码
								Ext.Ajax.requestEx({
									url:"scheduleArrangeAction_listArrangeNo.action",
									params:{
										'deptCode':arrangeNo.substring(0,arrangeNo.indexOf("-")),
										'arrangeType':2
									},
									successCallback:function(result){
										if(Ext.isEmpty(result)){
											Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
											return false;
										}
										if(!Ext.isEmpty(result.arrangeNo)){
											if(!Ext.isEmpty(updateWin) && updateWin.isVisible()){
												updateWin.getForm().findField("isNew").setValue(1);
												updateWin.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
											}
										}
									}
								})
							}else{
								updateWin.getForm().findField("entity.arrangeNo").setValue(record.data.arrangeNo);
								updateWin.getForm().findField("isNew").setValue(0);
							}
							Ext.getCmp('arrangeGrid').getStore().load();
						}
					})
				}
			})
		}else{
			updateWin = addItem(updateWin);
			updateWin.show();
			updateWin.getForm().findField("entity.id").setValue(record.data.id);
			updateWin.getForm().findField("recordId").setValue(record.data.id);
			if(!Ext.isEmpty(record.data.dept)){
				Ext.getCmp("updateDeptId").setValue(record.data.dept.id);
				updateWin.getForm().findField("deptCode").setRawValue(record.data.dept.deptCode);
			}
			//加载以选中班次和所有班次
			if(record.data.scheduleArrangeInfos){
				var infos = record.data.scheduleArrangeInfos;
				var data = [];
				Ext.each(infos,function(item){
					this.push([item.scheduleInfo.id,
					  	        item.scheduleInfo.dataSource,
					  	        item.scheduleInfo.lineOptimizeNo,
					  	        item.scheduleInfo.modelBase,
					  	        item.scheduleInfo.startTm,
					  	        item.scheduleInfo.endTm,
					  	        item.scheduleInfo.startDept,
					  	        item.scheduleInfo.endDept,
					  	        item.scheduleInfo.dept,
					  	        item.scheduleInfo.vehicle,
					  	        item.scheduleInfo.valid]);
				},data);
				//排序
				data.sort(function(a1,a2){
					if(Ext.isEmpty(a1) || Ext.isEmpty(a2)
						|| a1.length < 5 || a2.length < 5){
						return 0;
					}
					if(!/^[0-9]{2}:[0-9]{2}$/.test(a1[4])){
						return 0;
					}
					if(!/^[0-9]{2}:[0-9]{2}$/.test(a2[4])){
						return 0;
					}
					var a1part1 = eval(a1[4].substring(0,2));
					var a1part2 = eval(a1[4].substring(3,5));
					var a2part1 = eval(a2[4].substring(0,2));
					var a2part2 = eval(a2[4].substring(3,5));
					if(a1part1 > a2part1){
						return 1;
					}
					if(a1part1 < a2part1){
						return -1;
					}
					if(a1part2 > a2part2){
						return 1;
					}
					if(a1part2 < a2part2){
						return -1;
					}
					return 0;
				})
				Ext.getCmp('choosedGrid').getStore().loadData(data);
			}
			if(1 == record.data.isUsed){
				updateWin.getForm().findField("entity.valid").setValue(1);
				updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid1);
				var arrangeNo = record.data.arrangeNo;
				//获取班次代码
				Ext.Ajax.requestEx({
					url:"scheduleArrangeAction_listArrangeNo.action",
					params:{
						'deptCode':arrangeNo.substring(0,arrangeNo.indexOf("-")),
						'arrangeType':2
					},
					successCallback:function(result){
						if(Ext.isEmpty(result)){
							Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
							return false;
						}
						if(!Ext.isEmpty(result.arrangeNo)){
							if(!Ext.isEmpty(updateWin) && updateWin.isVisible()){
								updateWin.getForm().findField("isNew").setValue(1);
								updateWin.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
							}
						}
					}
				})
			}else{
				updateWin.getForm().findField("entity.arrangeNo").setValue(record.data.arrangeNo);
				updateWin.getForm().findField("isNew").setValue(0);
				updateWin.getForm().findField("entity.valid").setValue(record.data.valid);
				if(1 == record.data.valid){
					updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid1);
				}else{
					updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid0);
				}
			}
			Ext.getCmp('arrangeGrid').getStore().load();
		}
	}
},dlg)
function addItem(updateWin){
	updateWin.add({xtype:"panel",
		title:Ext.i18n.update,
		tools:[{id:'close',
			handler:function(){
				updateWin.getForm().reset();
				Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
				Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
				updateWin.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var updateForm = updateWin.getForm();
				if(!updateForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				var store = Ext.getCmp("choosedGrid").getStore();
				var vView = Ext.getCmp("choosedGrid").getView();
				if(store.getCount()<1){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.choosedNull);
					return false;
				}
				//检验数据
				var isValid = false;
				//清空已有的错误提示
				for(var i=0;i<store.getCount();i++ ){
					vView.getRow(i).title="";
				}
				for(var i=0;i<store.getCount();i++ ){
					var item = store.getAt(i);
					//车牌号校验-手工录入
					if(item.get("dataSource") == 1 && Ext.isEmpty(item.get("vehicle"))){
						vView.getCell(i,1).style.color="red";
						vView.getRow(i).title=Ext.i18n.chooseVehicleAlert;
				   		isValid = true;
				   		break;
					}
				}
				if(isValid){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.chooseVehicleAlert);
					return false;	
				}
				var isNew = updateForm.findField("isNew").getValue();
				var confirmMsg = Ext.i18n.saveConfirm;
				if(!Ext.isEmpty(isNew) && isNew==1){
					confirmMsg = Ext.i18n.saveConfirmNew;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,confirmMsg,function(btn){
					if('yes' == btn){
						var params = {};
						Ext.each(store.getRange(),function(item,i){
							params['entity.infoArrangeList['+i+"].id"] = item.data.id;
							params['entity.infoArrangeList['+i+"].startTm"] = item.data.startTm;
							params['entity.infoArrangeList['+i+"].endTm"] = item.data.endTm;
							params['entity.infoArrangeList['+i+"].vehicle.vehicleCode"] = item.data.vehicle.vehicleCode;
						});
						//校验是否超过16个小时
						updateForm.submitEx({
							url:"scheduleArrangeAction_listCheckEntity.action",
							timeout:1*60,
							params:params,
							successCallback:function(result){
								if(Ext.isEmpty(result)){
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
									return false;
								}
								//超过16个小时则提示
								if(!Ext.isEmpty(result.errorMsg) && result.errorMsg!="ok"){
									Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.morethan16,function(btn){
										if('yes' == btn){
											updateForm.submitEx({
												url:"scheduleArrangeAction_updateEntity.action",
												timeout:2*60,
												params:params,
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
														Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
														Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
														updateWin.hide();
														dlg.getGrid().getStore().reload();
													});
												}
											})
										}
									})
								}else{
									//没有超过16个小时则保存
									updateForm.submitEx({
										url:"scheduleArrangeAction_updateEntity.action",
										timeout:2*60,
										params:params,
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
												Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
												Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
												updateWin.hide();
												dlg.getGrid().getStore().reload();
											});
										}
									})
								}
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				updateWin.getForm().reset();
				Ext.getCmp("choosedGrid").getSelectionModel().clearSelections();
				Ext.getCmp("arrangeGrid").getSelectionModel().clearSelections();
				updateWin.hide();
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"winForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				columnWidth:.33,
				items:[
					{xtype:'hidden',name:"recordId"},
					{xtype:'hidden',name:"deptId",id:"updateDeptId"},
					{xtype:"hidden",name:"entity.id"},
					{xtype:"hidden",name:"isNew"},{
					xtype:"textfield",
					fieldLabel:Ext.i18n.deptCode,
					readOnly:true,
					name:"deptCode",
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.arrangeNo,
					name:"entity.arrangeNo",
					readOnly:true,
					maxLength:50,
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"combo",
					hiddenName:"entity.valid",
					width:100,
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
				columnWidth:1,
				xtype:"fieldset",
				style:"padding-left:10px;padding-right:10px;padding-top:10px;",
				height:430,
				title:Ext.i18n.infos,
				layout:"border",
				items:[{
					region:"center",
					tbar:[{
						xtype:'label',
						style:'font-weight:bold;font-size:12;color:#15428b;',
						text:Ext.i18n.choosedInfo
					},'-',{
						xtype:'label',
						style:'color:#15428b;',
						text:Ext.i18n.alertInfo
					},'->',{
						text:Ext.i18n.chooseVehicle,minWidth:60,cls:'x-btn-normal',
						handler:function(){
							if(!Ext.getCmp("choosedGrid").getSelectionModel().hasSelection()){
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
								return false;
							}
							var records = Ext.getCmp("choosedGrid").getSelectionModel().getSelections();
							var flg = false;
							Ext.each(records,function(record){
								if(record.data.dataSource == 2){
									flg = true;
								}
							})
							if(flg){
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseInput);
								return false;
							}
							vehicleWin.show();
							Ext.getCmp('vehicleGrid').getStore().load();
						}
					},{text:Ext.i18n.cancelChoosed,minWidth:60,cls:'x-btn-normal',
						handler:function(){
							removeChoose();
						}
					}],
					xtype:'grid',
					height:180,
					id:"choosedGrid",
					border:false,
					style:"border: 1px solid #8db2e3;",
					enableHdMenu:false,
					sm:addSm,
					listeners: {
						'dblclick': removeChoose
					},
					store:new Ext.data.ArrayStore({
					  	fields:['id',
					  	        'dataSource',
					  	        'lineOptimizeNo',
					  	        'modelBase',
					  	        'startTm',
					  	        'endTm',
					  	        'startDept',
					  	        'endDept',
					  	        'dept',
					  	        'vehicle',
					  	        'valid'],
						data:[]
				  	}),
					columns:[addSm,
							{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:55,
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
							},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:105
							},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:70
							},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
							},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
							},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
							},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
							},{header:Ext.i18n.ownerDept,dataIndex:"dept",sortable:false,width:85,
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
							},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:65,
								rendererCall:function(v){
									if(Ext.isEmpty(v)){
										return '';
									}
									return v.vehicleCode;
								}
							},{header:Ext.i18n.validState,dataIndex:"valid",sortable:false,width:40,
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
					}]
				},{
					region:'south',
					height:230,
					xtype:'grid',
					id:"arrangeGrid",
					border:false,
					style:"border-bottom: 1px solid #8db2e3;border-left: 1px solid #8db2e3;border-right: 1px solid #8db2e3;",
					loadMask: true,
					enableHdMenu:false,
					listeners: {
						'dblclick': chooseArrange 
					},
					tbar:[{
						xtype:'label',
						style:'font-weight:bold;font-size:12;color:#15428b;',
						text:Ext.i18n.allInfo
					},'-',new Ext.PagingToolbar({
				        pageSize: 10,
				        store: addStore,
				        displayInfo: true
			    	}),'->',{
			    		xtype:"trigger",
						triggerClass : 'x-form-search-trigger',
						emptyText:Ext.i18n.pleaseEnterStartTm,
						id:"searchString",
						onTriggerClick : function() {
							Ext.getCmp('arrangeGrid').getStore().load();
						}
			    	},'-',{text:Ext.i18n.confirmChoosed,minWidth:60,cls:'x-btn-normal',
			    		handler:function(){
							chooseArrange();
			    		}
					}],      
					sm:addAllSm,
					store:addStore,
					columns:[addAllSm,
						{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:55,
							rendererCall:function(v,m,record){
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
						},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:105
						},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:70
						},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
						},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
						},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
						},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
						},{header:Ext.i18n.ownerDept,dataIndex:"dept",sortable:false,width:85,
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
						},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:65,
							rendererCall:function(v){
								if(Ext.isEmpty(v)){
									return '';
								}
								return v.vehicleCode;
							}
						},{header:Ext.i18n.validState,dataIndex:"valid",sortable:false,width:40,
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
					}]
				}]
			}]
		}]
	})
	return updateWin;
}
//机动班新增
dlg.on(Ext.i18n.add1,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	var addWin1 = dlg.createModalDialog();
	addWin1.add({xtype:"panel",
		title:Ext.i18n.add1,
		tools:[{id:'close',
			handler:function(){
				addWin1.getForm().reset();
				addWin1.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var addForm = addWin1.getForm();
				if(!addForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if('yes' == btn){
						var startTm = addForm.findField("startTm1").getValue()+":"+addForm.findField("startTm2").getValue();
						var endTm = addForm.findField("endTm1").getValue()+":"+addForm.findField("endTm2").getValue();
						addForm.submitEx({
							url:"scheduleArrangeAction_saveEntityArr.action",
							timeout:2*60,
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
									addWin1.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				addWin1.getForm().reset();
				addWin1.hide();
			}
		},{text:Ext.i18n.refreshArrangeNo,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				Ext.Ajax.requestEx({
					url:"scheduleArrangeAction_listArrangeNo.action",
					params:{
						'deptCode':dlg.getQueryForm().findField("deptCode").getValue(),
						'arrangeType':1
					},
					successCallback:function(result){
						if(Ext.isEmpty(result)){
							Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
							return false;
						}
						if(!Ext.isEmpty(result.arrangeNo)){
							if(!Ext.isEmpty(addWin1) && addWin1.isVisible()){
								addWin1.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
							}
						}
					}
				})
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"winForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				items:[{xtype:"hidden",name:"entity.arrangeType",value:1},
					{xtype:"hidden",name:"entity.dept.id"},{
					xtype:"textfield",
					fieldLabel:Ext.i18n.deptCode,
					readOnly:true,
					name:"entity.dept.deptCode",
					width:130
				}]
			},{
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.arrangeNo1,
					name:"entity.arrangeNo",
					readOnly:true,
					maxLength:50,
					width:130
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
						fieldLabel:Ext.i18n.startTmTxt,
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
						fieldLabel:Ext.i18n.endTmTxt,
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
			}]
		}]
	})
	addWin1.show();
	addWin1.getForm().findField("entity.dept.id").setValue(dlg.getQueryForm().findField("deptId").getValue());
	addWin1.getForm().findField("entity.dept.deptCode").setValue(dlg.getQueryForm().findField("deptCode").getValue());
	//获取班次代码
	Ext.Ajax.requestEx({
		url:"scheduleArrangeAction_listArrangeNo.action",
		params:{
			'deptCode':dlg.getQueryForm().findField("deptCode").getValue(),
			'arrangeType':1
		},
		successCallback:function(result){
			if(Ext.isEmpty(result)){
				Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
				return false;
			}
			if(!Ext.isEmpty(result.arrangeNo)){
				if(!Ext.isEmpty(addWin1) && addWin1.isVisible()){
					addWin1.getForm().findField("entity.arrangeNo").setValue(result.arrangeNo);
				}
			}
		}
	})
},dlg);
//修改机动班
function update1(record){
	var updateWin1 = dlg.createModalDialog();
	updateWin1.add({xtype:"panel",
		title:Ext.i18n.update,
		tools:[{id:'close',
			handler:function(){
				updateWin1.getForm().reset();
				updateWin1.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var addForm = updateWin1.getForm();
				if(!addForm.isValid()){
					Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
					return false;
				}
				Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
					if('yes' == btn){
						var startTm = addForm.findField("startTm1").getValue()+":"+addForm.findField("startTm2").getValue();
						var endTm = addForm.findField("endTm1").getValue()+":"+addForm.findField("endTm2").getValue();
						addForm.submitEx({
							url:"scheduleArrangeAction_updateEntityArr.action",
							timeout:2*60,
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
									updateWin1.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				updateWin1.getForm().reset();
				updateWin1.hide();
			}
		}],
		layout:'fit',
		items:[{
			layout:"column",
			xtype:"form",
			id:"winForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				items:[{xtype:"hidden",name:"entity.arrangeType",value:1},
					{xtype:"hidden",name:"entity.id"},{
					xtype:"textfield",
					fieldLabel:Ext.i18n.deptCode,
					readOnly:true,
					name:"deptCode",
					width:130
				}]
			},{
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.arrangeNo1,
					name:"arrangeNo",
					readOnly:true,
					maxLength:50,
					width:130
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
						fieldLabel:Ext.i18n.startTmTxt,
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
						fieldLabel:Ext.i18n.endTmTxt,
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
			}]
		}]
	})
	updateWin1.show();
	var addForm = updateWin1.getForm();
	addForm.findField("entity.id").setValue(record.data.id);
	if(!Ext.isEmpty(record.data.dept)){
		addForm.findField("deptCode").setValue(record.data.dept.deptCode);
	}
	addForm.findField("arrangeNo").setValue(record.data.arrangeNo);
	if(!Ext.isEmpty(record.data.startTm)){
		addForm.findField("startTm1").setValue(record.data.startTm.substring(0,2));
		addForm.findField("startTm2").setValue(record.data.startTm.substring(3,5));
	}
	if(!Ext.isEmpty(record.data.endTm)){
		addForm.findField("endTm1").setValue(record.data.endTm.substring(0,2));
		addForm.findField("endTm2").setValue(record.data.endTm.substring(3,5));
	}
	addForm.findField("entity.valid").setValue(record.data.valid);
	if(1 == record.data.valid){
		addForm.findField("entity.valid").setRawValue(Ext.i18n.valid1);
	}else{
		addForm.findField("entity.valid").setRawValue(Ext.i18n.valid0);
	}
}
//查看明细
dlg.on(Ext.i18n.detail,function(){
	var sm = dlg.getGrid().getSelectionModel();
	if(!sm.hasSelection()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseArr);
		return false;
	}
	if(sm.getSelections().length > 1){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseRecord);
		return false;
	}
	var record = sm.getSelected();
	//机动班不可查看明细
	if(record.data.arrangeType == 1){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseArr2);
		return false;
	}
		
	var updateWin = dlg.createModalDialog();
	updateWin.add({xtype:"panel",
		title:Ext.i18n.detail,
		tools:[{id:'close',
			handler:function(){
				updateWin.getForm().reset();
				updateWin.hide();
			}
		}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
			disabled:true
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
			id:"winForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:120
			},
			items:[{
				columnWidth:.33,
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.deptCode,
					readOnly:true,
					name:"deptCode",
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"textfield",
					fieldLabel:Ext.i18n.arrangeNo,
					name:"arrangeNo",
					readOnly:true,
					maxLength:50,
					width:100
				}]
			},{
				columnWidth:.33,
				items:[{
					xtype:"combo",
					hiddenName:"entity.valid",
					width:100,
					fieldLabel:Ext.i18n.valid,
				    mode: 'local',
				    displayField:"text",
				    valueField:"value",
				    triggerAction: 'all',
				    editable:false,
				    allowBlank:false,
				    readOnly:true,
				    value:1,
				    store:new Ext.data.SimpleStore({
						fields:["text","value"],
						data:[[Ext.i18n.valid1,1],[Ext.i18n.valid0,0]]
					})
				}]
			},{
				columnWidth:1,
				xtype:"fieldset",
				style:"padding-left:10px;padding-right:10px;padding-top:10px;",
				height:430,
				title:Ext.i18n.infos,
				items:[{
					region:"center",
					title:Ext.i18n.choosedInfo,
					xtype:'grid',
					height:390,
					id:"choosedGrid",
					border:false,
					style:"border: 1px solid #8db2e3;",
					enableHdMenu:false,
					store:new Ext.data.ArrayStore({
					  	fields:['id',
					  	        'dataSource',
					  	        'lineOptimizeNo',
					  	        'modelBase',
					  	        'startTm',
					  	        'endTm',
					  	        'startDept',
					  	        'endDept',
					  	        'dept',
					  	        'vehicle',
					  	        'valid'],
						data:[]
				  	}),
					columns:[new Ext.grid.RowNumberer(),
							{header:Ext.i18n.dataSource,dataIndex:"dataSource",sortable:false,width:55,
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
							},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false,width:105
							},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false,width:70
							},{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
							},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
							},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85,
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
							},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85,
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
							},{header:Ext.i18n.ownerDept,dataIndex:"dept",sortable:false,width:85,
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
							},{header:Ext.i18n.vehicleCode,dataIndex:"vehicle",sortable:false,width:65,
								rendererCall:function(v){
									if(Ext.isEmpty(v)){
										return '';
									}
									return v.vehicleCode;
								}
							},{header:Ext.i18n.validState,dataIndex:"valid",sortable:false,width:40,
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
					}]
				}]
			}]
		}]
	})
	updateWin.show();
	updateWin.getForm().findField("entity.valid").setValue(record.data.valid);
	if(1 == record.data.valid){
		updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid1);
	}else{
		updateWin.getForm().findField("entity.valid").setRawValue(Ext.i18n.valid0);
	}
	if(!Ext.isEmpty(record.data.dept)){
		updateWin.getForm().findField("deptCode").setRawValue(record.data.dept.deptCode);
	}
	updateWin.getForm().findField("arrangeNo").setRawValue(record.data.arrangeNo);
	//加载以选中班次和所有班次
	if(record.data.scheduleArrangeInfos){
		var infos = record.data.scheduleArrangeInfos;
		var data = [];
		Ext.each(infos,function(item){
			this.push([item.scheduleInfo.id,
			  	        item.scheduleInfo.dataSource,
			  	        item.scheduleInfo.lineOptimizeNo,
			  	        item.scheduleInfo.modelBase,
			  	        item.scheduleInfo.startTm,
			  	        item.scheduleInfo.endTm,
			  	        item.scheduleInfo.startDept,
			  	        item.scheduleInfo.endDept,
			  	        item.scheduleInfo.dept,
			  	        item.scheduleInfo.vehicle,
			  	        item.scheduleInfo.valid]);
		},data);
		//排序
		data.sort(function(a1,a2){
			if(Ext.isEmpty(a1) || Ext.isEmpty(a2)
				|| a1.length < 5 || a2.length < 5){
				return 0;
			}
			if(!/^[0-9]{2}:[0-9]{2}$/.test(a1[4])){
				return 0;
			}
			if(!/^[0-9]{2}:[0-9]{2}$/.test(a2[4])){
				return 0;
			}
			var a1part1 = eval(a1[4].substring(0,2));
			var a1part2 = eval(a1[4].substring(3,5));
			var a2part1 = eval(a2[4].substring(0,2));
			var a2part2 = eval(a2[4].substring(3,5));
			if(a1part1 > a2part1){
				return 1;
			}
			if(a1part1 < a2part1){
				return -1;
			}
			if(a1part2 > a2part2){
				return 1;
			}
			if(a1part2 < a2part2){
				return -1;
			}
			return 0;
		})
		Ext.getCmp('choosedGrid').getStore().loadData(data);
	}
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
							url:'scheduleArrangeAction_saveFile.action',
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
										width:800,
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
				height:480+24*1,
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
									'href="../pages/vmsarrange/template/scheduleArrange.xls" target="blank">scheduleArrange.xls</a>',
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
				url:"scheduleArrangeAction_listReport.action",
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