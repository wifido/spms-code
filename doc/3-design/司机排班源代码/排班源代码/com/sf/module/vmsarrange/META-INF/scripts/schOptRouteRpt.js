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
	
	deptCode:"${app:i18n_def('common.js.deptCode','网点代码')}",
	createdEmpCode:"${app:i18n_def('common.js.createdEmp','创建人')}",
	createdTm:"${app:i18n_def('common.js.createdTm','创建时间')}",
	modifiedEmpCode:"${app:i18n_def('common.js.modifiedEmp','修改人')}",
	modifiedTm:"${app:i18n_def('common.js.modifedTm','修改时间')}",
	netError:"${app:i18n_def('common.js.netError','网络状况不好,请稍后重试')}",
	
	modelBase:"${app:i18n_def('scheduleInfo.js.4','车型')}",
	lineOptimizeNo:"${app:i18n_def('scheduleInfo.js.5','路径优化线路编号')}",
	startTm:"${app:i18n_def('scheduleInfo.js.6','出车时间')}",
	endTm:"${app:i18n_def('scheduleInfo.js.7','收车时间')}",
	startDept:"${app:i18n_def('scheduleInfo.js.8','始发网点')}",
	endDept:"${app:i18n_def('scheduleInfo.js.9','目的网点')}",
	ownerDept:"${app:i18n_def('scheduleInfo.js.14','归属网点')}",
	confirmDownload:"${app:i18n_def('scheduleInfo.js.30','导出数据过多时可能需要较长时间，是否导出')}",
	fileNameNull:"${app:i18n_def('scheduleInfo.js.31','生成的文件名称为空,请重新导出')}",
	areaName:"${app:i18n_def('schOptRouteRpt.js.1','区部')}",
	yearMonth:"${app:i18n_def('schOptRouteRpt.js.2','月份')}",
	optimizeRoute:"${app:i18n_def('schOptRouteRpt.js.3','优化路径')}",
	matchDays:"${app:i18n_def('schOptRouteRpt.js.4','路径匹配天数')}",
	selectYearMonth:"${app:i18n_def('schOptRouteRpt.js.5','请选择月份')}",
	one:"1"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	two:"2"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	three:"3"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	four:"4"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	five:"5"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	six:"6"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	seven:"7"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	eight:"8"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	nine:"9"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	ten:"10"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	eleven:"11"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twelve:"12"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	thirteen:"13"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	fourteen:"14"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	fifteen:"15"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	sixteen:"16"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	seventeen:"17"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	eighteen:"18"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	nineteen:"19"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twenty:"20"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyOne:"21"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyTwo:"22"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyThree:"23"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyFour:"24"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyFive:"25"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentySix:"26"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentySeven:"27"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyEight:"28"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	twentyNine:"29"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	thirty:"30"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
	thirtyOne:"31"+"${app:i18n_def('schOptRouteRpt.js.6','号')}"
};
var dlg=new Dialog();
Ext.override(Dialog,{
	createModalDialog:function(){
		var _win = Ext.getCmp("Dialog-win");
		if( Ext.isEmpty(_win,false) ){
			_win = new Ext.Window({
				id:"Dialog-win",
				width:780,
				height:480,
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
	<app:isPermission code="/vmsarrange/schOptRouteRpt_search.action">
	dlg.addBizButton({text:Ext.i18n.search});
	</app:isPermission>
  	//导出
  	<app:isPermission code="/vmsarrange/schOptRouteRpt_download.action">
  	this.addBizButton({text:Ext.i18n.exported,cls:'x-btn-normal'});
  	</app:isPermission>
	// 查询条件
	var queryCt = this.getQueryCt();
	queryCt.setTitle(Ext.i18n.searchCondition);
	queryCt.ownerCt.setHeight(80+(23*0));

	// 网点
	queryCt.add({
		labelAlign:"right",
		labelWidth:80,
		columnWidth:.33,
		items:[{xtype:"hidden",name:"deptId"},{
			xtype:"textfield",
			readOnly:true,
			name:"deptCode",
			allowBlank:false,
			fieldLabel:Ext.i18n.deptCode,
			width:120
		}]
	});
	//月份
	queryCt.add({
		columnWidth:.67,
		labelWidth:80,
		labelAlign:'right',
		items:[{
			xtype:"datefield",
			fieldLabel:Ext.i18n.yearMonth,
			name:"yearMonth",
			width:120,
			format:'Y-m',
			allowBlank:false,
			// 显示年月日期插件
			plugins:'monthPickerPlugin'
		}]
	});
	// 添加Grid
	var _store = new Ext.data.JsonStore({
    	url:"schOptRouteRptAction_listPage.action",
    	root:"page",
    	totalProperty:"total",
	  	fields:['lineOptimizeNo',
	  	        'dept',
	  	        'optDate',
	  	        'modelBase',
	  	        'startTm',
	  	        'endTm',
	  	        'optimizeRoute',
	  	        'matchDays',
			  	'one',
			  	'two',
			  	'three',
			  	'four',
			  	'five',
			  	'six',
			  	'seven',
			  	'eight',
			  	'nine',
			  	'ten',
			  	'eleven',
			  	'twelve',
			  	'thirteen',
			  	'fourteen',
			  	'fifteen',
			  	'sixteen',
			  	'seventeen',
			  	'eighteen',
			  	'nineteen',
			  	'twenty',
			  	'twentyOne',
			  	'twentyTwo',
			  	'twentyThree',
			  	'twentyFour',
			  	'twentyFive',
			  	'twentySix',
			  	'twentySeven',
			  	'twentyEight',
			  	'twentyNine',
			  	'thirty',
			  	'thirtyOne',
			  	'createdEmpCode',
			  	'createdTm',
			  	'modifiedEmpCode',
			  	'modifiedTm',
			  	'areaName'],
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
		         {header:Ext.i18n.areaName,dataIndex:"dept",sortable:false,
					rendererCall:function(v,m,record,rowidx,colidx,store){
						if(Ext.isEmpty(v)){
							return '';
						}
						if(Ext.isEmpty(v.deptName) && !Ext.isEmpty(v.deptCode)){
							return v.deptCode;
						}
						if(!Ext.isEmpty(v.deptName) && Ext.isEmpty(v.deptCode)){
							return v;
						}
						return v.deptCode+"/"+v.deptName;
					}
				},{header:Ext.i18n.lineOptimizeNo,dataIndex:"lineOptimizeNo",sortable:false
				},{header:Ext.i18n.modelBase,dataIndex:"modelBase",sortable:false
				},{header:Ext.i18n.optimizeRoute,dataIndex:"optimizeRoute",sortable:false
				},{header:Ext.i18n.matchDays,dataIndex:"matchDays",sortable:false
				},{header:Ext.i18n.createdTm,dataIndex:"createdTm",sortable:false,rendererCall:Ext.renderDateTime
				},{header:'01',dataIndex:"one",sortable:false,width:30
				},{header:'02',dataIndex:"two",sortable:false,width:30
				},{header:'03',dataIndex:"three",sortable:false,width:30
				},{header:'04',dataIndex:"four",sortable:false,width:30
				},{header:'05',dataIndex:"five",sortable:false,width:30
				},{header:'06',dataIndex:"six",sortable:false,width:30
				},{header:'07',dataIndex:"seven",sortable:false,width:30
				},{header:'08',dataIndex:"eight",sortable:false,width:30
				},{header:'09',dataIndex:"nine",sortable:false,width:30
				},{header:'10',dataIndex:"ten",sortable:false,width:30
				},{header:'11',dataIndex:"eleven",sortable:false,width:30
				},{header:'12',dataIndex:"twelve",sortable:false,width:30
				},{header:'13',dataIndex:"thirteen",sortable:false,width:30
				},{header:'14',dataIndex:"fourteen",sortable:false,width:30
				},{header:'15',dataIndex:"fifteen",sortable:false,width:30
				},{header:'16',dataIndex:"sixteen",sortable:false,width:30
				},{header:'17',dataIndex:"seventeen",sortable:false,width:30
				},{header:'18',dataIndex:"eighteen",sortable:false,width:30
				},{header:'19',dataIndex:"nineteen",sortable:false,width:30
				},{header:'20',dataIndex:"twenty",sortable:false,width:30
				},{header:'21',dataIndex:"twentyOne",sortable:false,width:30
				},{header:'22',dataIndex:"twentyTwo",sortable:false,width:30
				},{header:'23',dataIndex:"twentyThree",sortable:false,width:30
				},{header:'24',dataIndex:"twentyFour",sortable:false,width:30
				},{header:'25',dataIndex:"twentyFive",sortable:false,width:30
				},{header:'26',dataIndex:"twentySix",sortable:false,width:30
				},{header:'27',dataIndex:"twentySeven",sortable:false,width:30
				},{header:'28',dataIndex:"twentyEight",sortable:false,width:30
				},{header:'29',dataIndex:"twentyNine",sortable:false,width:30
				},{header:'30',dataIndex:"thirty",sortable:false,width:30
				},{header:'31',dataIndex:"thirtyOne",sortable:false,width:30
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
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptCode").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	if(Ext.isEmpty(dlg.getQueryForm().findField("yearMonth").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectYearMonth);
		return false;
	}
	if(!dlg.getQueryForm().isValid()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
		return false;
	}
	dlg.getGrid().getStore().load();
},dlg)
//导出
dlg.on(Ext.i18n.exported,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptCode").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectDepartment);
		return false;
	}
	if(Ext.isEmpty(dlg.getQueryForm().findField("yearMonth").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.selectYearMonth);
		return false;
	}
	if(!dlg.getQueryForm().isValid()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.validateError);
		return false;
	}
	Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmDownload,function(button,text){
		if(button == "yes"){
			dlg.getQueryForm().submitEx({
				url:"schOptRouteRptAction_listReport.action",
				timeout:60*30,
				successCallback:function(result){
					if(Ext.isEmpty(result)){
						Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
						return false;
					}
					if(!Ext.isEmpty(result.retMsg)){
						Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
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