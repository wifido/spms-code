//<%@ page language="java" contentType="text/html; charset=UTF-8"%>
Ext.Ajax.timeout = 1000 * 60 * 30;  
Ext.form.Action.timeout = 15 * 60;
Ext.form.BasicForm.timeout = 15 * 60;
Ext.form.Action.Load.timeout = 15 * 60;
Ext.form.Action.Submit.timeout = 15 * 60;
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = 'side';
Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
Ext.i18n = {
		search:"${app:i18n('search')}",
		detail:"${app:i18n_def('scheduleArrange.js.13','查看明细')}",
		optimize:"${app:i18n_def('preSchedule.js.32','调班')}",
		created:"${app:i18n('add')}",
		modified:"${app:i18n('edit')}",
		upload:"${app:i18n_def('common.js.upload','导入')}",
		exported:"${app:i18n('export')}",
		save:"${app:i18n('save')}",
		cancel:"${app:i18n('canel')}",
		saveSuccess:"${app:i18n('saveSuccess')}",
		pleaseChooseDept:"${app:i18n('selectDepartment')}",
		treeTitle:"${app:i18n('deptTreeTitle')}",
		treeTitle1:"${app:i18n('sf_express')}",
		prompt:"${app:i18n('prompt')}",
		reset:"${app:i18n('reset')}",
		alertDetail1:"${app:i18n('selectOneRecord')}",
		yearMonth:"${app:i18n_def('schOptRouteRpt.js.2','月份')}",
		chooseYearMonth:"${app:i18n_def('schOptRouteRpt.js.5','请选择月份')}",
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
		thirtyOne:"31"+"${app:i18n_def('schOptRouteRpt.js.6','号')}",
		classType:"${app:i18n_def('preSchedule.js.41','班次类别')}",
		alertAdd2:"${app:i18n('validateError')}",
		createdTm:"${app:i18n_def('common.js.createdTm','创建时间')}",
		createdEmpCode:"${app:i18n_def('common.js.createdEmp','创建人')}",
		modifiedTm:"${app:i18n_def('common.js.modifedTm','修改时间')}",
		modifiedEmpCode:"${app:i18n_def('common.js.modifiedEmp','修改人')}",
		startTm:"${app:i18n_def('scheduleInfo.js.6','出车时间')}",
		endTm:"${app:i18n_def('scheduleInfo.js.7','收车时间')}",
		startDept:"${app:i18n_def('scheduleInfo.js.8','始发网点')}",
		endDept:"${app:i18n_def('scheduleInfo.js.9','目的网点')}",
		isValid:"${app:i18n_def('common.js.isValid','是否有效')}",
		valid1:"${app:i18n_def('common.js.valid1','有效')}",
		valid0:"${app:i18n_def('common.js.valid0','无效')}",
		pleaseChooseFile:"${app:i18n_def('scheduleInfo.js.20','请选择文件')}",
		pleaseChooseExcel:"${app:i18n_def('scheduleInfo.js.21','文件格式有误，只能选择excel(.xls后缀文件)')}",
		saveConfirm:"${app:i18n_def('common.js.saveConfirm','是否保存')}",
		savaSuccess:"${app:i18n('saveSuccess')}",
		netError:"${app:i18n_def('common.js.netError','网络状况不好,请稍后重试')}",
		wrongInfo:"${app:i18n_def('scheduleInfo.js.29','错误信息')}",
		filePath:"${app:i18n_def('scheduleInfo.js.27','文件路径')}",
		fileNameNull:"${app:i18n_def('scheduleInfo.js.31','生成的文件名称为空,请重新导出')}",
		templateLabel:"${app:i18n_def('scheduleInfo.js.28','导入模板')}",
		confirmDownload:"${app:i18n_def('scheduleInfo.js.30','导出数据过多时可能需要较长时间，是否导出')}",
		uploadSuccess:"${app:i18n_def('common.js.uploadSuccess','上传成功')}",
		html1:"${app:i18n_def('scheduleInfo.js.23','注意事项')}",
		html2:"${app:i18n_def('scheduleInfo.js.24','提示信息中的行号均指excel表格的序号(数据中间必须无空行)')}",
		confirmUpload:"${app:i18n_def('scheduleArrange.js.1','该操作可能需要较长时间,是否导入')}",
		html4:"${app:i18n_def('scheduleInfo.js.26','请阅读模板中的注意事项及业务规则,并遵循业务规则进行操作')}",
		
		printed:"${app:i18n_def('preSchedule.js.60','预排班导出')}",
		pleaseInput:"${app:i18n_def('preSchedule.js.61','请先输入数据')}",
		addSchedule:"${app:i18n_def('preSchedule.js.62','增加一行')}",
		deleteSchedule:"${app:i18n_def('preSchedule.js.63','减少一行')}",
		classStatus0:"${app:i18n_def('preSchedule.js.65','全部')}",
		classStatus1:"${app:i18n_def('preSchedule.js.66','正常')}",
		classStatus2:"${app:i18n_def('preSchedule.js.67','顶班')}",
		classStatus3:"${app:i18n_def('preSchedule.js.68','机动')}",		
		deptCodeDesc:"${app:i18n_def('preSchedule.js.69','地区')}",
		deptCode:"${app:i18n_def('preSchedule.js.70','网点代码')}",
		driverName:"${app:i18n_def('preSchedule.js.71','驾驶员名字')}",
		empCode:"${app:i18n_def('preSchedule.js.72','驾驶员工号')}",
		planDay:"${app:i18n_def('preSchedule.js.73','计划休息天数')}",
		realDay:"${app:i18n_def('preSchedule.js.74','实际休息天数')}",
		rate:"${app:i18n_def('preSchedule.js.75','排班吻合率')}",
		draftFlag:"${app:i18n_def('preSchedule.js.76','草稿标识')}",
		draft0:"${app:i18n_def('preSchedule.js.77','正式')}",
		draft1:"${app:i18n_def('preSchedule.js.78','草稿')}",
		
		driverClassDayNull1:"${app:i18n_def('preSchedule.js.90','红色字体标出的行：驾驶员/班次类别/1到')}",
		driverClassDayNull2:"${app:i18n_def('preSchedule.js.91','1到')}",
		driverClassDayNull3:"${app:i18n_def('preSchedule.js.92','号的排班均不能为空')}",
		dayIsNull:"${app:i18n_def('preSchedule.js.79','1号到28号的班次代码有为空的')}",
		driverNull:"${app:i18n_def('preSchedule.js.80','驾驶员不能为空')}",
		msg6:"${app:i18n_def('preSchedule.js.82','连续出勤6天，存在疲劳驾驶员风险，是否保存？')}",
		classTypeNull:"${app:i18n_def('preSchedule.js.83','班次类别不能为空')}",
		empCodeRepeat:"${app:i18n_def('preSchedule.js.84','驾驶员不能重复')}",
		alertInfo:"${app:i18n_def('preSchedule.js.85','请核对以上提示信息，确认是否保存？')}",
		wrongInfo1:"${app:i18n_def('preSchedule.js.86','提示信息')}",
		moreData28:"${app:i18n_def('preSchedule.js.87','本月没有29到31号，请清除29到31号的排班')}",
		chooseDriver:"${app:i18n_def('preSchedule.js.88','请先选择驾驶员')}",
		chooseArrange:"${app:i18n_def('preSchedule.js.89','请先选择班次类别')}"
};
Ext.override(Dialog,{
	createModalDialogFn:function(title){
		var _win = Ext.getCmp("Dialog-win-fn");
		if( Ext.isEmpty(_win,false) ){
			_win = new Ext.Window({
				id:"Dialog-win-fn",
				width:800,
				height:500,
				modal:true,
				border:false,
				bodyBorder:false,
				closable:true,
				maximizable:true,
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
		_win.setTitle(title);
		return _win;	
	}
})
var dlg=new Dialog();
var tree = new Ext.tree.AsyncTreeNode({
	id:'0',
	text:Ext.i18n.treeTitle1,
	loader:new Ext.tree.TreeLoader({
		url : "../vmsarrange/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
		dataUrl : "../vmsarrange/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
	})
});
//校验数据长度
function checkOverLength(value,maxLength){
	if(Ext.isEmpty(value)){
		return false;
	}
	var regex = /[^\x00-\xff]/g;  
  	var repalceValue = value.replace(regex,'***');
    if(repalceValue.length>maxLength){
    	return true;
    }
    return false;
}
//获取指定月最大天数
function getMaxDate(year,month){
	var d = new Date(year,month,0);
	return d.getDate();
}
//过滤空格和空值
function trimStr(value){
	if(!Ext.isEmpty(value)){
		return value.trim();
	}else{
		return '';
	}
}
//班次类型
function dispalyClassName(value){
	if(Ext.isEmpty(value)){
		return '';
	}
	if(0 == value){
		return Ext.i18n.classStatus0;
	}
	if(1 == value){
		return Ext.i18n.classStatus1;
	}
	if(2 == value){
		return Ext.i18n.classStatus2;
	}
	if(3 == value){
		return Ext.i18n.classStatus3;
	}
	return value;
}
//截取日期到天数
function getDay(value){
	if(value){
		return value.substring(0,10);
	}
	return '';
}
//是否有效
function getValid(v){
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
//渲染班次显示
function menuFormat(value,meta,record,dayIdx){
	if(Ext.isEmpty(value)){
		return '';
	}
	if(value=="假" || value=="休" || value.indexOf("-") == -1){
		return value;
	}else{
		return "<a href='javascript:newMenuWindow("+record.get('id')+","+dayIdx+");'>"+ value +"</a>";
	}
}	
/***************************************************
 ** 对话框显示前
 **************************************************/
dlg.on("beforeinit",function(){
	//网点树
	dlg.setNavigatorTitle(Ext.i18n.treeTitle);
	dlg.setNavigatorRootNode(tree);
	
	//内容面板业务按钮设置
	//查询
	<app:isPermission code="/vmsarrange/preSchedule_searchBtn.action">
	dlg.addBizButton({text:Ext.i18n.search});
	</app:isPermission>
	//新增按钮
	<app:isPermission code="/vmsarrange/preSchedule_createBtn.action">
	this.addBizButton({text : Ext.i18n.created});
	</app:isPermission>
	//修改按钮
	<app:isPermission code="/vmsarrange/preSchedule_modifyBtn.action">
	this.addBizButton({text :Ext.i18n.modified});
	</app:isPermission>
	//导入
	<app:isPermission code="/vmsarrange/preSchedule_uploadBtn.action">
	this.addBizButton({text:Ext.i18n.upload,cls:'x-btn-normal'});
  	</app:isPermission>
  	//导出
  	<app:isPermission code="/vmsarrange/preSchedule_downloadBtn.action">
  	this.addBizButton({text:Ext.i18n.exported,cls:'x-btn-normal'});
  	</app:isPermission>
  	
  	//预排班导出
  	<app:isPermission code="/vmsarrange/preSchedule_printBtn.action">
  	this.addBizButton({text:Ext.i18n.printed,cls:'x-btn-normal'});
  	</app:isPermission>
	//内容面板查询容器
				
	// 查询条件
	var queryCt = this.getQueryCt();
	queryCt.setTitle("${app:i18n_def('preSchedule.js.3','查询条件')}");
	queryCt.ownerCt.setHeight(80+(23*1));
	queryCt.ownerCt.add({xtype:"hidden",name:"deptId"}); //用于新增时combobox查询车牌
	queryCt.ownerCt.add({xtype:"hidden",name:"driverId"}); //用于新增时combobox查询驾驶员
	queryCt.ownerCt.add({xtype:"hidden",name:"deptName"}); //用于新增时combobox查询驾驶员
	// 查询条件<网点代码>
	queryCt.add({labelAlign:"right",labelWidth:80,columnWidth:.33,items:[{
			xtype:'hidden',name:'param.department.id'
		},{
			xtype:"textfield",
			name:"param.department.deptCode",
			readOnly:true,
			fieldLabel:"${app:i18n_def('','网点代码')}",
			width:120,
			labelSeparator:'<font color=red>*</font>'
	}]});
	//工号
	queryCt.add({
		labelAlign:"right",
		labelWidth:80,
		columnWidth:.33,
		items:{
		xtype:"textfield",
		name:"param.driver.empCode",
		fieldLabel:"${app:i18n_def('','工号')}",
		width:120
	}});
	//班次类型
	queryCt.add({
		xtype:'panel',
		columnWidth:.33,
		layout:'form',
		labelWidth:80,
		labelAlign:'right',
		items:[{xtype:"combo",
			labelAlign:"right",
			hiddenName:"param.classType",
			fieldLabel:Ext.i18n.classType,
			typeAhead: true,
	  		mode: 'local',
	  		width:120,
	  		columnWidth:.33,
	  		displayField:"text",
	  		valueField:"key",
	  		triggerAction: 'all',
	  		editable:false,
	  		selectOnFocus:true,
			store:new Ext.data.SimpleStore({
				fields:["key","text"],
				data:[[null,"${app:i18n_def('preSchedule.js.4','全部')}"],
						['1',Ext.i18n.classStatus1],
                    	['2',Ext.i18n.classStatus2],
						['3',Ext.i18n.classStatus3]]
			})
		}]
	});
	
	//月份
	queryCt.add({
		columnWidth:.5,
		labelWidth:80,
		labelAlign:'right',
		items:[{
			xtype:"datefield",
			fieldLabel:Ext.i18n.yearMonth,
			name:"param.yearMonth",
			width:120,
			format:'Y-m',
			allowBlank:false,
			maxValue:(new Date()).add(Date.MONTH,1).getLastDateOfMonth(),
			// 显示年月日期插件
			plugins:'monthPickerPlugin'
		}]
	});
	// 添加Grid的数据字段
	var _store = new Ext.data.JsonStore({
    	url:"preSchedule_Search.action",
    	root:"root",
    	totalProperty:"totalSize",
	  	fields:['id','classType','yearMonth','planDay','realDay','rate',
	  			'draftFlag','one','two','three','four','five','six','seven',
	  			'eight','nine','ten','eleven','twelve','thirteen','fourteen',
	  			'fifteen','sixteen','seventeen','eighteen','nineteen','twenty',
	  			'twentyOne','twentyTwo','twentyThree','twentyFour','twentyFive',
	  			'twentySix','twentySeven','twentyEight','twentyNine','thirty','driverName','empCode',
	  			'thirtyOne','department','driver','createdTm','createdEmpCode',
	  			'modifiedTm','modifiedEmpCode','areaDeptCode','areaDeptName'],
	  	listeners:{
	  		beforeLoad:function(){
	  			this.baseParams = dlg.getQueryForm().getValues();
	  			this.baseParams['limit'] = dlg.getGrid().getTopToolbar().pageSize;
	  		}
	  	}
  	});
	var smm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	dlg.addGrid({
		region:'center',
		xtype:'grid',
		id:'searchGrid',
		border:false,
		loadMask: true,
		enableHdMenu:false,
		sm:smm,
		store:_store,
		tbar:new Ext.PagingToolbar({
	        pageSize: 50,
	        store: _store,
	        displayInfo: true
    	}),      
		columns:[new Ext.grid.RowNumberer(),smm,{header:Ext.i18n.deptCodeDesc,dataIndex:"areaDeptName",sortable:false
				},{header:Ext.i18n.deptCode,dataIndex:"department",sortable:false,
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
				},{header:Ext.i18n.driverName,dataIndex:"driver",sortable:false,
					rendererCall:function(v,meta,record){
						if(Ext.isEmpty(v)){
							if(Ext.isEmpty(record.data.driverName)){
								return '';
							}
							return record.data.driverName;
						}
						return v.driverName;
					}
				},{header:Ext.i18n.empCode,dataIndex:"driver",sortable:true,
					rendererCall:function(v,meta,record){
						if(Ext.isEmpty(v)){
							if(Ext.isEmpty(record.data.empCode)){
								return '';
							}
							return record.data.empCode;
						}
						return v.empCode;
					}
				},{header:Ext.i18n.classType,dataIndex:"classType",sortable:false,
					rendererCall:function(value){
						if(Ext.isEmpty(value)){
							return '';
						}
						if(1 == value){
							return Ext.i18n.classStatus1;
						}
						if(2 == value){
							return Ext.i18n.classStatus2;
						}
						if(3 == value){
							return Ext.i18n.classStatus3;
						}
						return value;
					}
				},{header:Ext.i18n.yearMonth,dataIndex:"yearMonth",sortable:false
				},{header:Ext.i18n.one,dataIndex:"one",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,1);
					}
				},{header:Ext.i18n.two,dataIndex:"two",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,2);
					}
				},{header:Ext.i18n.three,dataIndex:"three",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,3);
					}
				},{header:Ext.i18n.four,dataIndex:"four",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,4);
					}
				},{header:Ext.i18n.five,dataIndex:"five",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,5);
					}
				},{header:Ext.i18n.six,dataIndex:"six",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,6);
					}
				},{header:Ext.i18n.seven,dataIndex:"seven",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,7);
					}
				},{header:Ext.i18n.eight,dataIndex:"eight",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,8);
					}
				},{header:Ext.i18n.nine,dataIndex:"nine",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,9);
					}
				},{header:Ext.i18n.ten,dataIndex:"ten",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,10);
					}
				},{header:Ext.i18n.eleven,dataIndex:"eleven",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,11);
					}
				},{header:Ext.i18n.twelve,dataIndex:"twelve",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,12);
					}
				},{header:Ext.i18n.thirteen,dataIndex:"thirteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,13);
					}
				},{header:Ext.i18n.fourteen,dataIndex:"fourteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,14);
					}
				},{header:Ext.i18n.fifteen,dataIndex:"fifteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,15);
					}
				},{header:Ext.i18n.sixteen,dataIndex:"sixteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,16);
					}
				},{header:Ext.i18n.seventeen,dataIndex:"seventeen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,17);
					}
				},{header:Ext.i18n.eighteen,dataIndex:"eighteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,18);
					}
				},{header:Ext.i18n.nineteen,dataIndex:"nineteen",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,19);
					}
				},{header:Ext.i18n.twenty,dataIndex:"twenty",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,20);
					}
				},{header:Ext.i18n.twentyOne,dataIndex:"twentyOne",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,21);
					}
				},{header:Ext.i18n.twentyTwo,dataIndex:"twentyTwo",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,22);
					}
				},{header:Ext.i18n.twentyThree,dataIndex:"twentyThree",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,23);
					}
				},{header:Ext.i18n.twentyFour,dataIndex:"twentyFour",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,24);
					}
				},{header:Ext.i18n.twentyFive,dataIndex:"twentyFive",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,25);
					}
				},{header:Ext.i18n.twentySix,dataIndex:"twentySix",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,26);
					}
				},{header:Ext.i18n.twentySeven,dataIndex:"twentySeven",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,27);
					}
				},{header:Ext.i18n.twentyEight,dataIndex:"twentyEight",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,28);
					}
				},{header:Ext.i18n.twentyNine,dataIndex:"twentyNine",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,29);
					}
				},{header:Ext.i18n.thirty,dataIndex:"thirty",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,30);
					}
				},{header:Ext.i18n.thirtyOne,dataIndex:"thirtyOne",sortable:false,
					rendererCall:function(value,meta,record){
						return menuFormat(value,meta,record,31);
					}
				},{header:Ext.i18n.planDay,dataIndex:"planDay",sortable:false
				},{header:Ext.i18n.realDay,dataIndex:"realDay",sortable:false
				},{header:Ext.i18n.rate+"(%)",dataIndex:"rate",sortable:false
				},{header:Ext.i18n.draftFlag,dataIndex:"draftFlag",sortable:false,
					rendererCall:function(value,m,r){
						if(Ext.isEmpty(value)){
							return Ext.i18n.draft0;
						}
						if(1 == value){
							return Ext.i18n.draft1;
						}
						if(0 == value){
							return Ext.i18n.draft0;
						}
						return value;
					}
				},{header:Ext.i18n.createdTm,dataIndex:"createdTm",sortable:false,width:120,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						return v.replace("T"," ");
					}
				},{header:Ext.i18n.createdEmpCode,dataIndex:"createdEmpCode",sortable:false
				},{header:Ext.i18n.modifiedTm,dataIndex:"modifiedTm",sortable:false,width:120,
					rendererCall:function(v){
						if(Ext.isEmpty(v)){
							return '';
						}
						return v.replace("T"," ");
					}
				},{header:Ext.i18n.modifiedEmpCode,dataIndex:"modifiedEmpCode",sortable:false
				}]
	});
},dlg);


/***************************************************
 ** 网点树节点选中
 **************************************************/
dlg.on("selectionchange",function(sm,node){
	if(Ext.isEmpty(node) ||(!Ext.isEmpty(node) && 0 == node.id)){
		sm.clearSelections();
		dlg.getQueryForm().setValues({
			"param.department.deptCode":null,
			"deptName":null,
			"param.department.id":null,
			"deptId":null
		});
		return false;	
	}
	dlg.getQueryForm().setValues({
		"param.department.deptCode":node.text.split("/")[0],
		"deptName":node.text.split("/")[1],
		"param.department.id":node.id,
		"deptId":node.id
	});
},dlg);

/***************************************************
 ** 对话框查询业务
 **************************************************/
dlg.on(Ext.i18n.search,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("param.department.deptCode").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseDept);
		return ;
	}
	if(Ext.isEmpty(dlg.getQueryForm().findField("param.yearMonth").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.chooseYearMonth);
		return ;
	}
	if(!dlg.getQueryForm().isValid()){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.alertAdd2);
		return false;
	}
	dlg.getGrid().getStore().load();
},dlg);

var classType;
var driverDeptId;
function createdArrangeCombo(arrangeType,driverDeptId){
	return new Ext.form.ComboBox({
		valueField:'arrangeNo',
		displayField:'arrangeNo',
		triggerAction:'all',
		editable:true,
		mode:'remote',
		queryParam:'arrangeNo',
		minChars:1,
		typeAhead:true,
		lazyRender:true,
		forceSelection:true,
		resizable:true,
		pageSize:50,
		listWidth:250,
		store:new Ext.data.JsonStore({
			url:'../vmsarrange/scheduleArrCombox.action',
			root:'arrangeNos',
			arrangeType:arrangeType,
			driverDeptId:driverDeptId,
			totalProperty:"totalSize",
			fields:['id','arrangeNo'],
			listeners:{
				beforeload:function(s){
					if(Ext.isEmpty(driverDeptId) || driverDeptId == -1){
						driverDeptId = dlg.getQueryForm().findField("deptId").getValue();
					}
					s.baseParams["arrangeType"] = arrangeType;
					s.baseParams["deptId"] = driverDeptId;
				},
				load:function(s){
					var idx = s.getCount();
					var plantJia = new s.recordType({id:'-1',arrangeNo:"假"});
					var plantXiu = new s.recordType({id:'-2',arrangeNo:"休"});
					if(idx == 1){
						s.insert(idx, plantXiu);
						s.insert(idx+1, plantJia);
					}else{
						s.insert(0, plantXiu);
						s.insert(1, plantJia);
					}
				}
			}
		}),
		listeners:{
			'beforequery': function(qe){
				//每次都查询
				delete qe.combo.lastQuery;  
			}
		}
	});
}
//创建一行空记录
function createdRecord(vStore,deptCode){
	return new vStore.recordType({
						'department.deptCode':deptCode,
						'driver.empCode':'',
						classType:'',
						one:'',
						two:'',
						three:'',
						three:'',
						four:'',
						five:'',
						six:'',
						seven:'',
						eight:'',
						nine:'',
						ten:'',
						eleven:'',
						twelve:'',
						thirteen:'',
						fourteen:'',
						fifteen:'',
						sixteen:'',
						seventeen:'',
						eighteen:'',
						nineteen:'',
						twenty:'',
						twentyOne:'',
						twentyTwo:'',
						twentyThree:'',
						twentyFour:'',
						twentyFive:'',
						twentySix:'',
						twentySeven:'',
						twentyEight:'',
						twentyNine:'',
						thirty:'',
						thirtyOne:''
					});
}
/***************************************************
 ** 对话框新增业务
 **************************************************/
dlg.on(Ext.i18n.created,function(){	
	if(Ext.isEmpty(dlg.getQueryForm().findField("param.department.deptCode").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseDept);
		return false;
	}
	var deptCode = dlg.getQueryForm().findField("param.department.deptCode").getValue();
	var win = this.createModalDialogFn(Ext.i18n.created);
	var arrangeTypes = [0,0,0,0,0,0,0,0,0,0];
	var driverDeptIds = [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1];
	//获取指定月的最大天数
	var current = new Date();
	var maxMonthDay = getMaxDate(current.getFullYear(),(current.getMonth()+2));
	var hide29 = false;
	var hide30 = false;
	var hide31 = false;
	if(maxMonthDay == 28){
		hide29 = true;
		hide30 = true;
		hide31 = true;
	}
	if(maxMonthDay == 29){
		hide30 = true;
		hide31 = true;
	}
	if(maxMonthDay == 30){
		hide31 = true;
	}
	win.add({xtype:"panel",
		//title:Ext.i18n.created,
		//tools:[{id:'close',handler:function(){win.getComponent(0).getComponent(1).stopEditing();win.hide();}}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',id:'add-submit'
		},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				win.getComponent(0).getComponent(1).stopEditing();win.hide();
			}
		},{
			text:Ext.i18n.reset,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				//创建一行
				var vGrid = win.getComponent(0).getComponent(1);
				vGrid.stopEditing();
				var vStore = vGrid.getStore();
				vStore.removeAll();
				var plant = createdRecord(vStore,deptCode);
				var rowIndex = vStore.getCount();
				vStore.insert(rowIndex,plant);
				vGrid.getSelectionModel().selectRow(rowIndex);
				vGrid.startEditing(0,1);
			}
		},{
				xtype:'label',
				style:"margin-left:5px;",
				text:"${app:i18n_def('preSchedule.js.5','说明:最多允许一次新增10笔记录')}"
		},'->',{text:Ext.i18n.addSchedule,minWidth:60,cls:'x-btn-normal',id:'addVehicleRow',
			handler:function(){
				var vGrid = win.getComponent(0).getComponent(1);
				var vStore = vGrid.getStore();
				var plant = createdRecord(vStore,deptCode);
				vGrid.stopEditing();
				var rowIndex = vStore.getCount();
				if(rowIndex<10){
					vStore.insert(rowIndex,plant);
					vGrid.getSelectionModel().selectRow(rowIndex);
					vGrid.startEditing(rowIndex,1);
				}
			}
		},{text:Ext.i18n.deleteSchedule,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				var vGrid = win.getComponent(0).getComponent(1);
				vGrid.stopEditing();
				var vStore = vGrid.getStore().remove(vGrid.getSelectionModel().getSelected());
				vGrid.getSelectionModel().selectLastRow();
			}
		}],
		listeners:{
			'bodyresize':function(f,awidth,aheight){
				Ext.getCmp("editorGrid").stopEditing();
				Ext.getCmp("editorGrid").setHeight(aheight-30);
				Ext.getCmp("editorGrid").setWidth(awidth);
			}
		},
		items:[{xtype:'panel',
		       frame:true, 
		       html:"<div style='width:768px;text-align:center;font-weight:bold;'>" + 
		       		dlg.getQueryForm().findField("deptName").getValue() +
		       		(new Date().getMonth()+2)%12 + "${app:i18n_def('preSchedule.js.6','月预排班表')}</div>"},
				new Ext.grid.EditorGridPanel({
					height:23*16+17,
					autoScroll:true,
					id:'editorGrid',
					width:768,
					sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
					clicksToEdit :'auto',
					store:new Ext.data.SimpleStore({
						fields:['deptId','deptCode','driverName','empCode','driverId',
						'classTypeDesc','classTypeKey','one','oneDesc','twoDesc','threeDesc',
						'two','three','four','five','six','seven',
						'eight','nine','ten','eleven','twelve','thirteen',
						'fourteen','fifteen','sixteen','seventeen','eighteen','nineteen',
						'twenty','twentyOne','twentyTwo','twentyThree','twentyFour','twentyFive',
						'twentySix','twentySeven','twentyEight','twentyNine','thirty','thirtyOne']
					}),
					columns:[{
						header:Ext.i18n.deptCode,width:60,align:'center',dataIndex:'deptCode',
						readOnly:true,rendererCall:function(){
							return deptCode;
						}
					},{header:Ext.i18n.empCode,width:130,align:'center',dataIndex:'empCode',
						editor:new Ext.form.ComboBox({
							valueField:'codeName',
							displayField:'codeName',
							triggerAction:'all',
							editable:true,
							mode:'remote',
							queryParam:'empCode',
							pageSize:50,
							minChars:1,
							typeAhead:true,
							lazyRender:true,
							resizable:true,
							forceSelection:true,
							listWidth:250,
							store:new Ext.data.JsonStore({
								url:'../vmsarrange/scheduleDriCombox.action',
								root:'drivers',
								fields:['id','codeName','department'],
								totalProperty:"totalSize",
								listeners:{
									beforeload:function(s){
										s.baseParams["deptId"] = dlg.getQueryForm().findField("deptId").getValue();
									}
								}
							}),
							listeners: {  
								'beforequery': function(qe){
									//每次都查询
									delete qe.combo.lastQuery;  
								},
								'select':function(f,record,index){
									if(null == record.data.department){
										driverDeptId=-1;
									}else{
										driverDeptId=record.data.department.id;
									}
								}
							},
							allowBlank:false
						}),
						rendererCall:function(val,x,store,idx){
							if(!Ext.isEmpty(driverDeptId)){
								driverDeptIds[idx]=driverDeptId;
								driverDeptId = null;
							}
							return val;
						}
					},{header:Ext.i18n.classType,align:'center',width:80,dataIndex:'classTypeDesc',
						editor:new Ext.form.ComboBox({
						    triggerAction: 'all',
						    displayField:'classType',
						    valueField:"classType",
						    hiddenName:'classType',
						    mode:'local',
						    editable:false,
						    store:new Ext.data.SimpleStore({
						    	fields:["key","classType"],
								data:[['1',Ext.i18n.classStatus1],
					                    ['2',Ext.i18n.classStatus2],
										['3',Ext.i18n.classStatus3]]
						    }),
						    listeners: {  
								'select':function(f,record,index){
									classType=record.data.key;
								}
							}
						}),
						rendererCall:function(val,x,store,idx){
							if(!Ext.isEmpty(classType)){
								store.data['classType']= classType;
								arrangeTypes[idx]=classType;
								classType = null;
							}
							return val;
						}
					},{header:Ext.i18n.one,align:'center',width:80,dataIndex:'one',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.two,align:'center',width:80,dataIndex:'two',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.three,align:'center',width:80,dataIndex:'three',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.four,align:'center',width:80,dataIndex:'four',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.five,align:'center',width:80,dataIndex:'five',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.six,align:'center',width:80,dataIndex:'six',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.seven,align:'center',width:80,dataIndex:'seven',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.eight,align:'center',width:80,dataIndex:'eight',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.nine,align:'center',width:80,dataIndex:'nine',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.ten,align:'center',width:80,dataIndex:'ten',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.eleven,align:'center',width:80,dataIndex:'eleven',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twelve,align:'center',width:80,dataIndex:'twelve',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.thirteen,align:'center',width:80,dataIndex:'thirteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.fourteen,align:'center',width:80,dataIndex:'fourteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.fifteen,align:'center',width:80,dataIndex:'fifteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.sixteen,align:'center',width:80,dataIndex:'sixteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.seventeen,align:'center',width:80,dataIndex:'seventeen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.eighteen,align:'center',width:80,dataIndex:'eighteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.nineteen,align:'center',width:80,dataIndex:'nineteen',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twenty,align:'center',width:80,dataIndex:'twenty',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyOne,align:'center',width:80,dataIndex:'twentyOne',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyTwo,align:'center',width:80,dataIndex:'twentyTwo',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyThree,align:'center',width:80,dataIndex:'twentyThree',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyFour,align:'center',width:80,dataIndex:'twentyFour',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyFive,align:'center',width:80,dataIndex:'twentyFive',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentySix,align:'center',width:80,dataIndex:'twentySix',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentySeven,align:'center',width:80,dataIndex:'twentySeven',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyEight,align:'center',width:80,dataIndex:'twentyEight',
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.twentyNine,align:'center',width:80,dataIndex:'twentyNine',hidden:hide29,
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.thirty,align:'center',width:80,dataIndex:'thirty',hidden:hide30,
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					},{header:Ext.i18n.thirtyOne,align:'center',width:80,dataIndex:'thirtyOne',hidden:hide31,
						rendererCall :function(value, meta, record) {   
                             meta.attr = 'style="white-space:normal;"';    
                             return value;    
                        },
						editor:createdArrangeCombo()
					}],
					listeners:{
						beforeedit:function(o){
							//编辑的1到31号，则重新指定编辑器
							if(o.column>2){
								//为编辑列重新指定编辑器,设置班次类别为指定行的类别
								this.getColumnModel().setEditor(o.column,
									new Ext.grid.GridEditor(createdArrangeCombo(arrangeTypes[o.row],driverDeptIds[o.row])));
							}
						}
					}
				}),{xtype:"form",id:"addForm"}
		]
	});
	win.show(this);
	Ext.getCmp('add-submit').setHandler(function(){
		var vGrid = win.getComponent(0).getComponent(1);
		vGrid.stopEditing();
		var vStore = vGrid.getStore();
		var vView = vGrid.getView(); 
		if(vStore.getCount()<1){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseInput);
			return false;
		}
		//获取指定月的最大天数
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+2;
		var maxDate = getMaxDate(year,month);
		var isValid = false;
		//驾驶员、班次类别、班次代码均不能为空
		var hasNullValue = false;
		//排班不能超过实际日期
		var moreData28 = false;
		var moreData29 = false;
		var moreData30 = false;
		//清空已有的错误提示
		for(var i=0;i<vStore.getCount();i++ ){
			vView.getRow(i).title="";
		}
		var empCodes = [];
		//驾驶员是否重复
		var empCodeRepeat = false;
		//检验数据
		for(var i=0;i<vStore.getCount();i++ ){
			var item = vStore.getAt(i);
			//工号校验
			if(Ext.isEmpty(item.get("empCode"))){
				vView.getCell(i,0).style.color="red";
		   		vView.getCell(i,1).style.color="red";
		   		hasNullValue = true;
		   		continue;
			}
			var repeatFlg = false;
			Ext.each(empCodes,function(item){
				if(item == this){
					empCodeRepeat = true;
					repeatFlg = true;
				}
			},item.get("empCode"))
			empCodes.push(item.get("empCode"));
			if(repeatFlg){
				vView.getCell(i,0).style.color="red";
		   		vView.getCell(i,1).style.color="red";
		   		continue;
			}
			var classType = item.get("classType")==null?"":item.get("classType").trim();
			//班次类别校验
			if(Ext.isEmpty(classType)){
				hasNullValue = true;
				vView.getCell(i,0).style.color="red";
		   		vView.getCell(i,1).style.color="red";
				continue;
			}
			var one=item.get("one")==null?"":item.get("one").trim();
			var two=item.get("two")==null?"":item.get("two").trim();
			var three=item.get("three")==null?"":item.get("three").trim();
			var four=item.get("four")==null?"":item.get("four").trim();
			var five=item.get("five")==null?"":item.get("five").trim();
			var six=item.get("six")==null?"":item.get("six").trim();
			var seven=item.get("seven")==null?"":item.get("seven").trim();
			var eight=item.get("eight")==null?"":item.get("eight").trim();
			var nine=item.get("nine")==null?"":item.get("nine").trim();
			var ten=item.get("ten")==null?"":item.get("ten").trim();
			var eleven=item.get("eleven")==null?"":item.get("eleven").trim();
			var twelve=item.get("twelve")==null?"":item.get("twelve").trim();
			var thirteen=item.get("thirteen")==null?"":item.get("thirteen").trim();
			var fourteen=item.get("fourteen")==null?"":item.get("fourteen").trim();
			var fifteen=item.get("fifteen")==null?"":item.get("fifteen").trim();
			var sixteen=item.get("sixteen")==null?"":item.get("sixteen").trim();
			var seventeen=item.get("seventeen")==null?"":item.get("seventeen").trim();
			var eighteen=item.get("eighteen")==null?"":item.get("eighteen").trim();
			var nineteen=item.get("nineteen")==null?"":item.get("nineteen").trim();
			var twenty=item.get("twenty")==null?"":item.get("twenty").trim();
			var twentyOne=item.get("twentyOne")==null?"":item.get("twentyOne").trim();
			var twentyTwo=item.get("twentyTwo")==null?"":item.get("twentyTwo").trim();
			var twentyThree=item.get("twentyThree")==null?"":item.get("twentyThree").trim();
			var twentyFour=item.get("twentyFour")==null?"":item.get("twentyFour").trim();
			var twentyFive=item.get("twentyFive")==null?"":item.get("twentyFive").trim();
			var twentySix=item.get("twentySix")==null?"":item.get("twentySix").trim();
			var twentySeven=item.get("twentySeven")==null?"":item.get("twentySeven").trim();
			var twentyEight=item.get("twentyEight")==null?"":item.get("twentyEight").trim();
			var twentyNine=item.get("twentyNine")==null?"":item.get("twentyNine").trim();
			var thirty=item.get("thirty")==null?"":item.get("thirty").trim();
			var thirtyOne=item.get("thirtyOne")==null?"":item.get("thirtyOne").trim();
			if(Ext.isEmpty(one)|| Ext.isEmpty(two)||Ext.isEmpty(three)||Ext.isEmpty(four)||Ext.isEmpty(five)||Ext.isEmpty(six)||Ext.isEmpty(seven)||
					Ext.isEmpty(eight)||Ext.isEmpty(nine)||Ext.isEmpty(ten)||Ext.isEmpty(eleven)||Ext.isEmpty(twelve)||Ext.isEmpty(thirteen)||
					Ext.isEmpty(fourteen)||Ext.isEmpty(fifteen)||Ext.isEmpty(sixteen)||Ext.isEmpty(seventeen)||Ext.isEmpty(eighteen)||Ext.isEmpty(nineteen)||
					Ext.isEmpty(twenty)||Ext.isEmpty(twentyOne)||Ext.isEmpty(twentyTwo)||Ext.isEmpty(twentyThree)||Ext.isEmpty(twentyFour)||Ext.isEmpty(twentyFive)||
					Ext.isEmpty(twentySix)||Ext.isEmpty(twentySeven)||Ext.isEmpty(twentyEight)){
				hasNullValue = true;
				vView.getCell(i,0).style.color="red";
		   		vView.getCell(i,1).style.color="red";
				continue;
			}
			if(maxDate == 28){
				if(!Ext.isEmpty(twentyNine) || !Ext.isEmpty(thirty) || !Ext.isEmpty(thirtyOne)){
					moreData28 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 29){
				if(Ext.isEmpty(twentyNine)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
				if(!Ext.isEmpty(thirty) || !Ext.isEmpty(thirtyOne)){
					moreData29 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 30){
				if(Ext.isEmpty(twentyNine) || Ext.isEmpty(thirty)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
				if(!Ext.isEmpty(thirtyOne)){
					moreData30 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 31){
				if(Ext.isEmpty(twentyNine) || Ext.isEmpty(thirty) || Ext.isEmpty(thirtyOne)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
		}
		if(hasNullValue){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.driverClassDayNull1+maxDate+Ext.i18n.driverClassDayNull3);
			return false;
		}
		if(empCodeRepeat){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.empCodeRepeat);
			return false;
		}
		if(moreData28){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.moreData28);
			return false;
		}
		if(moreData29){
			Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.7','本月没有30号和31号，请清除30号和31号的排班')}");
			return false;
		}
		if(moreData30){
			Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.8','本月没有31号，请清除31号的排班')}");
			return false;
		}
		// 判断是否存在无效数据
		for(var i=0;i<vStore.getCount();i++ ){
			if(!Ext.isEmpty(vView.getRow(i).title)){
				isValid = true;
				break;
			}
		}
		if( isValid ){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.alertAdd2);
			return false;	
		}
		//校验重复数据
		Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn,text){
			if(btn == 'yes'){
				var json = {};
				var deptId = dlg.getQueryForm().findField('param.department.id').getValue();
				for(var i=0;i<vStore.getCount();i++ ){
					var item = vStore.getAt(i);
					json['preSchedules['+i+'].department.id']=deptId;
					json['preSchedules['+i+'].classType']=item.get("classType");
					var driverNameCode = item.get("empCode");
					var empCode='';
					if(!Ext.isEmpty(driverNameCode)){
						if(driverNameCode.indexOf("/")>0){
							empCode = driverNameCode.substr(0,driverNameCode.indexOf("/"));
						}else{
							empCode = driverNameCode;
						}
					}
					json['preSchedules['+i+'].driver.empCode']=empCode;
					json['preSchedules['+i+'].one']=trimStr(item.get("one"));
					json['preSchedules['+i+'].two']=trimStr(item.get("two"));
					json['preSchedules['+i+'].three']=trimStr(item.get("three"));
					json['preSchedules['+i+'].four']=trimStr(item.get("four"));
					json['preSchedules['+i+'].five']=trimStr(item.get("five"));
					json['preSchedules['+i+'].six']=trimStr(item.get("six"));
					json['preSchedules['+i+'].seven']=trimStr(item.get("seven"));
					json['preSchedules['+i+'].eight']=trimStr(item.get("eight"));
					json['preSchedules['+i+'].nine']=trimStr(item.get("nine"));
					json['preSchedules['+i+'].ten']=trimStr(item.get("ten"));
					json['preSchedules['+i+'].eleven']=trimStr(item.get("eleven"));
					json['preSchedules['+i+'].twelve']=trimStr(item.get("twelve"));
					json['preSchedules['+i+'].thirteen']=trimStr(item.get("thirteen"));
					json['preSchedules['+i+'].fourteen']=trimStr(item.get("fourteen"));
					json['preSchedules['+i+'].fifteen']=trimStr(item.get("fifteen"));
					json['preSchedules['+i+'].sixteen']=trimStr(item.get("sixteen"));
					json['preSchedules['+i+'].seventeen']=trimStr(item.get("seventeen"));
					json['preSchedules['+i+'].eighteen']=trimStr(item.get("eighteen"));
					json['preSchedules['+i+'].nineteen']=trimStr(item.get("nineteen"));
					json['preSchedules['+i+'].twenty']=trimStr(item.get("twenty"));
					json['preSchedules['+i+'].twentyOne']=trimStr(item.get("twentyOne"));
					json['preSchedules['+i+'].twentyTwo']=trimStr(item.get("twentyTwo"));
					json['preSchedules['+i+'].twentyThree']=trimStr(item.get("twentyThree"));
					json['preSchedules['+i+'].twentyFour']=trimStr(item.get("twentyFour"));
					json['preSchedules['+i+'].twentyFive']=trimStr(item.get("twentyFive"));
					json['preSchedules['+i+'].twentySix']=trimStr(item.get("twentySix"));
					json['preSchedules['+i+'].twentySeven']=trimStr(item.get("twentySeven"));
					json['preSchedules['+i+'].twentyEight']=trimStr(item.get("twentyEight"));
					json['preSchedules['+i+'].twentyNine']=trimStr(item.get("twentyNine"));
					json['preSchedules['+i+'].thirty']=trimStr(item.get("thirty"));
					json['preSchedules['+i+'].thirtyOne']=trimStr(item.get("thirtyOne"));
				}
				Ext.Ajax.requestEx({
					url:'preSchedule_testRepeat.action',
					params:json,
					successCallback:function(result){
						if(Ext.isEmpty(result)){
							Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
							return false;
						}
						if(!Ext.isEmpty(result.retMsg)){
							var msgStr = "";
							var msgs = new Array();
							msgs = result.retMsg.replace(/;$/,"").split(";");
							for(var i = 0; i < msgs.length; i++) {
								msgStr = msgStr+"<p>"+msgs[i]+"</p>";
							}
							msgStr = msgStr+"<p><font color='red'>"+Ext.i18n.alertInfo+"</font></p>";
							var winInfo = new Ext.Window({
								title:Ext.i18n.wrongInfo1,
								width:600,
								height:400,
								layout:'fit',
								modal:true,
								autoScroll:false,
								tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
									handler:function(){
										winInfo.hide();
										Ext.getCmp("addForm").getForm().submitEx({
											url:'preSchedule_saveSchedule.action',
											timeout:2*60,
											params:json,
											successCallback:function(result){
												if(Ext.isEmpty(result)){
													Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
													return false;
												}
												if(!Ext.isEmpty(result.retMsg)){
													Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
													return false;
												}
												Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.savaSuccess,function(){
													dlg.getGrid().getSelectionModel().clearSelections();
													dlg.getGrid().getStore().reload();
													win.hide();
												});
											}
										})
									}
								},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
									handler:function(){winInfo.hide();}
								}],
								items:[{xtype:'panel',autoScroll:true,frame:true,html:msgStr}]
							});
							winInfo.show();
						}else{
							Ext.getCmp("addForm").getForm().submitEx({
								url:'preSchedule_saveSchedule.action',
								params:json,
								timeout:2*60,
								successCallback:function(result){
									if(Ext.isEmpty(result)){
										Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
										return false;
									}
									if(!Ext.isEmpty(result.retMsg)){
										Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
										return false;
									}
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.savaSuccess,function(){
										dlg.getGrid().getSelectionModel().clearSelections();
										dlg.getGrid().getStore().reload();
										win.hide();
									});
								}
							})
						}
					}
				})
			}
		})
	});
	setTimeout(function(){
		//创建一行
		var vGrid = win.getComponent(0).getComponent(1);
		var vStore = vGrid.getStore();
		var plant = createdRecord(vStore,deptCode);
		vGrid.stopEditing();
		var rowIndex = vStore.getCount();
		vStore.insert(rowIndex,plant);
		vGrid.getSelectionModel().selectRow(rowIndex);
		vGrid.startEditing(0,1);
	},100);
},dlg);
/***************************************************
 ** 对话框修改业务
 **************************************************/
dlg.on(Ext.i18n.modified,function(){
	var vSelectionModel = dlg.getGrid().getSelectionModel();
	if( !vSelectionModel.hasSelection() ){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.alertDetail1);
		return false;
	}  
	var draftFlag = dlg.getGrid().getSelectionModel().getSelected().get("draftFlag");
	if(0 == draftFlag){
		Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.12','实际排班不允许修改，只能调班')}");
		return false;
	} 
	var record = vSelectionModel.getSelected();
	var arrangeType = record.data.classType;
	var driverDeptId = -1;
	if(!Ext.isEmpty(record.data.department)){
		driverDeptId = record.data.department.id;
	}
	var deptId = 0;
	if(null != record.data.driver 
		&& null != record.data.driver.department 
		&& null != record.data.driver.department.id){
		deptId = record.data.driver.department.id;
	}
	var win = this.createModalDialogFn(Ext.i18n.modified);
	//获取指定月的最大天数
	var current = new Date();
	var maxMonthDay = getMaxDate(current.getFullYear(),(current.getMonth()+2));
	var hide29 = false;
	var hide30 = false;
	var hide31 = false;
	if(maxMonthDay == 28){
		hide29 = true;
		hide30 = true;
		hide31 = true;
	}
	if(maxMonthDay == 29){
		hide30 = true;
		hide31 = true;
	}
	if(maxMonthDay == 30){
		hide31 = true;
	}
	win.add({xtype:"panel",
		//title:Ext.i18n.modified,
		//tools:[{id:'close',handler:function(){win.hide();}}],
		tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',id:'add-submit'
			},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',handler:function(){win.hide();}
			},{
				text:Ext.i18n.reset,minWidth:60,cls:'x-btn-normal',handler:function(){
					var record = vSelectionModel.getSelected();
					var vGrid = win.getComponent(0).getComponent(0);
					var vStore = vGrid.getStore();
					var plant = createdUpdateRecord(vStore,record);
					vStore.removeAll();
					var rowIndex = vStore.getCount();
					vStore.insert(rowIndex,plant);
					vGrid.startEditing(0,2);
				}
			},'->',{text:Ext.i18n.addSchedule,minWidth:60,disabled:true,cls:'x-btn-normal'}
			,{text:Ext.i18n.deleteSchedule,minWidth:60,disabled:true,cls:'x-btn-normal'}],
		listeners:{
			'bodyresize':function(f,awidth,aheight){
				Ext.getCmp("editorGrid").stopEditing();
				Ext.getCmp("editorGrid").setHeight(aheight);
				Ext.getCmp("editorGrid").setWidth(awidth);
			}
		},
		items:[new Ext.grid.EditorGridPanel({
				height:23*16+17,
				autoScroll:true,
				id:'editorGrid',
				width:768,
				sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
				clicksToEdit:'auto',
				store: new Ext.data.ArrayStore({
					fields:['id','deptCode','empCode','classType','one','two','three',
					'four','five','six','seven','eight','nine','ten','eleven','twelve',
					'thirteen','fourteen','fifteen','sixteen','seventeen','eighteen','nineteen',
					'twenty','twentyOne','twentyTwo','twentyThree','twentyFour','twentyFive',
					'twentySix','twentySeven','twentyEight','twentyNine','thirty','thirtyOne']
				}),
				columns:[{header:Ext.i18n.empCode,width:70,align:'center',dataIndex:'empCode',readOnly:true
				},{header:Ext.i18n.classType,align:'center',width:80,dataIndex:'classType',readOnly:true,
					rendererCall:function(val,x,store){
						if(!Ext.isEmpty(classType)){
							store.data['classType']= classType;
							classType = null;
						}
						var classTypeValue = dispalyClassName(val);
						return classTypeValue;
					}
				},{header:Ext.i18n.one,align:'center',width:80,dataIndex:'one',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.two,align:'center',width:80,dataIndex:'two',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.three,align:'center',width:80,dataIndex:'three',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.four,align:'center',width:80,dataIndex:'four',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.five,align:'center',width:80,dataIndex:'five',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.six,align:'center',width:80,dataIndex:'six',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.seven,align:'center',width:80,dataIndex:'seven',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.eight,align:'center',width:80,dataIndex:'eight',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.nine,align:'center',width:80,dataIndex:'nine',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.ten,align:'center',width:80,dataIndex:'ten',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.eleven,align:'center',width:80,dataIndex:'eleven',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twelve,align:'center',width:80,dataIndex:'twelve',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.thirteen,align:'center',width:80,dataIndex:'thirteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.fourteen,align:'center',width:80,dataIndex:'fourteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.fifteen,align:'center',width:80,dataIndex:'fifteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.sixteen,align:'center',width:80,dataIndex:'sixteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.seventeen,align:'center',width:80,dataIndex:'seventeen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.eighteen,align:'center',width:80,dataIndex:'eighteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.nineteen,align:'center',width:80,dataIndex:'nineteen',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twenty,align:'center',width:80,dataIndex:'twenty',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyOne,align:'center',width:80,dataIndex:'twentyOne',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyTwo,align:'center',width:80,dataIndex:'twentyTwo',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyThree,align:'center',width:80,dataIndex:'twentyThree',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyFour,align:'center',width:80,dataIndex:'twentyFour',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyFive,align:'center',width:80,dataIndex:'twentyFive',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentySix,align:'center',width:80,dataIndex:'twentySix',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentySeven,align:'center',width:80,dataIndex:'twentySeven',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyEight,align:'center',width:80,dataIndex:'twentyEight',
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.twentyNine,align:'center',width:80,dataIndex:'twentyNine',hidden:hide29,
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.thirty,align:'center',width:80,dataIndex:'thirty',hidden:hide30,
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				},{header:Ext.i18n.thirtyOne,align:'center',width:80,dataIndex:'thirtyOne',hidden:hide31,
					rendererCall :function(value, meta, record) {   
                         meta.attr = 'style="white-space:normal;"';    
                         return value;    
                    },
					editor:createdArrangeCombo(arrangeType,driverDeptId)
				}]
			}),{xtype:"form",id:"addForm"}]
	});
	win.show(this);
	Ext.getCmp('add-submit').setHandler(function(){
		var vGrid = win.getComponent(0).getComponent(0);
		vGrid.stopEditing();
		var vStore = vGrid.getStore();
		var vView = vGrid.getView(); 
		if(vStore.getCount()<1){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseInput);
			return false;
		}
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+2;
		//获取指定年月的最大天数
		var maxDate = getMaxDate(year,month);
		var isValid = false;
		//驾驶员、班次类别、班次代码均不能为空
		var hasNullValue = false;
		var moreData28 = false;
		var moreData29 = false;
		var moreData30 = false;
		//清空已有的错误提示
		for(var i=0;i<vStore.getCount();i++ ){
			vView.getRow(i).title="";
		}
		//检验是否存在重复数据
		for(var i=0;i<vStore.getCount();i++ ){
			var item = vStore.getAt(i);
			var empCode = item.get("empCode");
			var classType = trimStr(item.get("classTypeKey"));
			var one=trimStr(item.get("one"));
			var two=trimStr(item.get("two"));
			var three=trimStr(item.get("three"));
			var four=trimStr(item.get("four"));
			var five=trimStr(item.get("five"));
			var six=trimStr(item.get("six"));
			var seven=trimStr(item.get("seven"));
			var eight=trimStr(item.get("eight"));
			var nine=trimStr(item.get("nine"));
			var ten=trimStr(item.get("ten"));
			var eleven=trimStr(item.get("eleven"));
			var twelve=trimStr(item.get("twelve"));
			var thirteen=trimStr(item.get("thirteen"));
			var fourteen=trimStr(item.get("fourteen"));
			var fifteen=trimStr(item.get("fifteen"));
			var sixteen=trimStr(item.get("sixteen"));
			var seventeen=trimStr(item.get("seventeen"));
			var eighteen=trimStr(item.get("eighteen"));
			var nineteen=trimStr(item.get("nineteen"));
			var twenty=trimStr(item.get("twenty"));
			var twentyOne=trimStr(item.get("twentyOne"));
			var twentyTwo=trimStr(item.get("twentyTwo"));
			var twentyThree=trimStr(item.get("twentyThree"));
			var twentyFour=trimStr(item.get("twentyFour"));
			var twentyFive=trimStr(item.get("twentyFive"));
			var twentySix=trimStr(item.get("twentySix"));
			var twentySeven=trimStr(item.get("twentySeven"));
			var twentyEight=trimStr(item.get("twentyEight"));
			var twentyNine=trimStr(item.get("twentyNine"));
			var thirty=trimStr(item.get("thirty"));
			var thirtyOne=trimStr(item.get("thirtyOne"));
			if(maxDate == 28){
				if(!Ext.isEmpty(twentyNine) || !Ext.isEmpty(thirty) || !Ext.isEmpty(thirtyOne)){
					moreData28 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 29){
				if(Ext.isEmpty(twentyNine)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
				if(!Ext.isEmpty(thirty) || !Ext.isEmpty(thirtyOne)){
					moreData29 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 30){
				if(Ext.isEmpty(twentyNine) || Ext.isEmpty(thirty)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
				if(!Ext.isEmpty(thirtyOne)){
					moreData30 = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(maxDate == 31){
				if(Ext.isEmpty(twentyNine) || Ext.isEmpty(thirty) || Ext.isEmpty(thirtyOne)){
					hasNullValue = true;
					vView.getCell(i,0).style.color="red";
			   		vView.getCell(i,1).style.color="red";
					continue;
				}
			}
			if(Ext.isEmpty(one)|| Ext.isEmpty(two)||Ext.isEmpty(three)||Ext.isEmpty(four)||Ext.isEmpty(five)||Ext.isEmpty(six)||Ext.isEmpty(seven)||
					Ext.isEmpty(eight)||Ext.isEmpty(nine)||Ext.isEmpty(ten)||Ext.isEmpty(eleven)||Ext.isEmpty(twelve)||Ext.isEmpty(thirteen)||
					Ext.isEmpty(fourteen)||Ext.isEmpty(fifteen)||Ext.isEmpty(sixteen)||Ext.isEmpty(seventeen)||Ext.isEmpty(eighteen)||Ext.isEmpty(nineteen)||
					Ext.isEmpty(twenty)||Ext.isEmpty(twentyOne)||Ext.isEmpty(twentyTwo)||Ext.isEmpty(twentyThree)||Ext.isEmpty(twentyFour)||Ext.isEmpty(twentyFive)||
					Ext.isEmpty(twentySix)||Ext.isEmpty(twentySeven)||Ext.isEmpty(twentyEight)){
				hasNullValue = true;
				vView.getCell(i,0).style.color="red";
		   		vView.getCell(i,1).style.color="red";
				continue;
			}
				
			
		}
		if(hasNullValue){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.driverClassDayNull2+maxDate+Ext.i18n.driverClassDayNull3);
			return false;
		}
		if(moreData28){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.moreData28);
			return false;
		}
		if(moreData29){
			Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.7','本月没有30号和31号，请清除30号和31号的排班')}");
			return false;
		}
		if(moreData30){
			Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.8','本月没有31号，请清除31号的排班')}");
			return false;
		}
		// 判断是否存在无效数据
		for(var i=0;i<vStore.getCount();i++ ){
			if(!Ext.isEmpty(vView.getRow(i).title)){
				isValid = true;
				break;
			}
		}
		if( isValid ){
			Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.alertAdd2);
			return false;	
		}
		//校验重复数据
		Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn,text){
			if(btn == 'yes'){
				var json = {};
				for(var i=0;i<vStore.getCount();i++ ){
					var item = vStore.getAt(i);
					json['preSchedules['+i+'].id']=item.get("id");
					json['preSchedules['+i+'].driver.empCode']=item.get("empCode");
					json['preSchedules['+i+'].classType']=item.get("classType");
					json['preSchedules['+i+'].one']=trimStr(item.get("one"));
					json['preSchedules['+i+'].two']=trimStr(item.get("two"));
					json['preSchedules['+i+'].three']=trimStr(item.get("three"));
					json['preSchedules['+i+'].four']=trimStr(item.get("four"));
					json['preSchedules['+i+'].five']=trimStr(item.get("five"));
					json['preSchedules['+i+'].six']=trimStr(item.get("six"));
					json['preSchedules['+i+'].seven']=trimStr(item.get("seven"));
					json['preSchedules['+i+'].eight']=trimStr(item.get("eight"));
					json['preSchedules['+i+'].nine']=trimStr(item.get("nine"));
					json['preSchedules['+i+'].ten']=trimStr(item.get("ten"));
					json['preSchedules['+i+'].eleven']=trimStr(item.get("eleven"));
					json['preSchedules['+i+'].twelve']=trimStr(item.get("twelve"));
					json['preSchedules['+i+'].thirteen']=trimStr(item.get("thirteen"));
					json['preSchedules['+i+'].fourteen']=trimStr(item.get("fourteen"));
					json['preSchedules['+i+'].fifteen']=trimStr(item.get("fifteen"));
					json['preSchedules['+i+'].sixteen']=trimStr(item.get("sixteen"));
					json['preSchedules['+i+'].seventeen']=trimStr(item.get("seventeen"));
					json['preSchedules['+i+'].eighteen']=trimStr(item.get("eighteen"));
					json['preSchedules['+i+'].nineteen']=trimStr(item.get("nineteen"));
					json['preSchedules['+i+'].twenty']=trimStr(item.get("twenty"));
					json['preSchedules['+i+'].twentyOne']=trimStr(item.get("twentyOne"));
					json['preSchedules['+i+'].twentyTwo']=trimStr(item.get("twentyTwo"));
					json['preSchedules['+i+'].twentyThree']=trimStr(item.get("twentyThree"));
					json['preSchedules['+i+'].twentyFour']=trimStr(item.get("twentyFour"));
					json['preSchedules['+i+'].twentyFive']=trimStr(item.get("twentyFive"));
					json['preSchedules['+i+'].twentySix']=trimStr(item.get("twentySix"));
					json['preSchedules['+i+'].twentySeven']=trimStr(item.get("twentySeven"));
					json['preSchedules['+i+'].twentyEight']=trimStr(item.get("twentyEight"));
					json['preSchedules['+i+'].twentyNine']=trimStr(item.get("twentyNine"));
					json['preSchedules['+i+'].thirty']=trimStr(item.get("thirty"));
					json['preSchedules['+i+'].thirtyOne']=trimStr(item.get("thirtyOne"));
				}
				Ext.Ajax.requestEx({
					url:'preSchedule_testRepeat.action',
					params:json,
					successCallback:function(result){
						if(Ext.isEmpty(result)){
							Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
							return false;
						}
						if(!Ext.isEmpty(result.retMsg)){
							var msgStr = "";
							var msgs = new Array();
							msgs = result.retMsg.replace(/;$/,"").split(";");
							for(var i = 0; i < msgs.length; i++) {
								msgStr = msgStr+"<p>"+msgs[i]+"</p>";
							}
							msgStr = msgStr+"<p><font color='red'>"+Ext.i18n.alertInfo+"</font></p>";
							var winInfo = new Ext.Window({
								title:Ext.i18n.wrongInfo1,
								width:600,
								height:400,
								layout:'fit',
								modal:true,
								autoScroll:false,
								tbar:[{text:Ext.i18n.save,minWidth:60,cls:'x-btn-normal',
									handler:function(){
										winInfo.hide();
										Ext.getCmp("addForm").getForm().submitEx({
											url:'preSchedule_updateSchedule.action',
											params:json,
											timeout:2*60,
											successCallback:function(result){
												if(Ext.isEmpty(result)){
													Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
													return false;
												}
												if(!Ext.isEmpty(result.retMsg)){
													Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
													return false;
												}
												Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.savaSuccess,function(){
													dlg.getGrid().getSelectionModel().clearSelections();
													dlg.getGrid().getStore().reload();
													win.hide();
												});
											}
										});
									}
								},{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
									handler:function(){winInfo.hide();}
								}],
								items:[{xtype:'panel',autoScroll:true,frame:true,html:msgStr}]
							});
							winInfo.show();
						}else{
							Ext.getCmp("addForm").getForm().submitEx({
								url:'preSchedule_updateSchedule.action',
								params:json,
								timeout:2*60,
								successCallback:function(result){
									if(Ext.isEmpty(result)){
										Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
										return false;
									}
									if(!Ext.isEmpty(result.retMsg)){
										Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
										return false;
									}
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.savaSuccess,function(){
										dlg.getGrid().getSelectionModel().clearSelections();
										dlg.getGrid().getStore().reload();
										win.hide();
									});
								}
							});
						}
					}
				})
			}
		})
	});
	var vGrid = win.getComponent(0).getComponent(0);
	var vStore = vGrid.getStore();
	var plant = createdUpdateRecord(vStore,record);
	var rowIndex = vStore.getCount();
	vStore.insert(rowIndex,plant);
	vGrid.startEditing(0,2);
},dlg);
function createdUpdateRecord(vStore,record){
	return new vStore.recordType({
		'id':record.data.id,
		'deptCode':(null==record.data.department?'':record.data.department.deptCode),
		'empCode':(null==record.data.driver?'':record.data.driver.empCode),
		'classType':record.data.classType,
		'one':record.data.one,
		'two':record.data.two,
		'three':record.data.three,
		'four':record.data.four,
		'five':record.data.five,
		'six':record.data.six,
		'seven':record.data.seven,
		'eight':record.data.eight,
		'nine':record.data.nine,
		'ten':record.data.ten,
		'eleven':record.data.eleven,
		'twelve':record.data.twelve,
		'thirteen':record.data.thirteen,
		'fourteen':record.data.fourteen,
		'fifteen':record.data.fifteen,
		'sixteen':record.data.sixteen,
		'seventeen':record.data.seventeen,
		'eighteen':record.data.eighteen,
		'nineteen':record.data.nineteen,
		'twenty':record.data.twenty,
		'twentyOne':record.data.twentyOne,
		'twentyTwo':record.data.twentyTwo,
		'twentyThree':record.data.twentyThree,
		'twentyFour':record.data.twentyFour,
		'twentyFive':record.data.twentyFive,
		'twentySix':record.data.twentySix,
		'twentySeven':record.data.twentySeven,
		'twentyEight':record.data.twentyEight,
		'twentyNine':record.data.twentyNine,
		'thirty':record.data.thirty,
		'thirtyOne':record.data.thirtyOne});
}
//弹出调班窗体
function newMenuWindow(recordId,dayIdx){
	var newWin = dlg.createModalDialog();
	var dayArr = [['1',"${app:i18n_def('preSchedule.js.13','年假')}"],
		           ['2',"${app:i18n_def('preSchedule.js.14','加班补休')}"],
		           ['3',"${app:i18n_def('preSchedule.js.15','事假')}"],
		           ['4',"${app:i18n_def('preSchedule.js.16','病假')}"],
		           ['5',"${app:i18n_def('preSchedule.js.17','婚假')}"],
		           ['6',"${app:i18n_def('preSchedule.js.18','护理假')}"],
		           ['7',"${app:i18n_def('preSchedule.js.19','产检假')}"],
		           ['8',"${app:i18n_def('preSchedule.js.20','产假')}"],
		           ['9',"${app:i18n_def('preSchedule.js.21','哺乳假')}"],
		           ['10',"${app:i18n_def('preSchedule.js.22','绩优假')}"],
		           ['11',"${app:i18n_def('preSchedule.js.23','外派假')}"],
		           ['12',"${app:i18n_def('preSchedule.js.24','疗养假')}"],
		           ['13',"${app:i18n_def('preSchedule.js.25','奖励假')}"],
		           ['14',"${app:i18n_def('preSchedule.js.26','探亲假')}"],
		           ['15',"${app:i18n_def('preSchedule.js.27','工伤假')}"],
		           ['16',"${app:i18n_def('preSchedule.js.28','流产假')}"],
		           ['17',"${app:i18n_def('preSchedule.js.29','特别年假')}"],
		           ['18',"${app:i18n_def('preSchedule.js.30','丧假')}"],
		           ['19',"${app:i18n_def('preSchedule.js.25','奖励假')}"]];
	
	var addSm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	newWin.add({xtype:"panel",
		title:"${app:i18n_def('preSchedule.js.31','查看明细')}",
		tools:[{id:'close',handler:function(){newWin.hide();}}],
		tbar:['-',
		    <app:isPermission code="/vmsarrange/preSchedule_optimizeBtn.action">
				{text:"${app:i18n_def('preSchedule.js.32','调班')}",minWidth:60,cls:'x-btn-normal',id:'optimizeBt',handler:function(){
					var optform = Ext.getCmp("optimizeWinForm").getForm();
					//获取是否选中请假
					var vacationBox = optform.findField("vacationBox").getValue();
					//获取是否选中调休
					var restBox = optform.findField("restBox").getValue();
					//获取是否选中调班
					var optimizeBox = optform.findField("optimizeBox").getValue();
					var vacationComboValue,scheduleId,dayDt,flag;
					var scheduleIdOld = optform.findField("scheduleIdOld").getValue();
					var dayDtOld = optform.findField("dayDtOld").getValue();	
					
					if(vacationBox){
						//请假
						vacationComboValue = optform.findField("vacationCombo").getValue();
						if(Ext.isEmpty(vacationComboValue)){
							Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.33','请选择请假类型')}");
							return false;
						}
						flag = '1';
					}else if(restBox){
						//调休
						flag = '2';
					}else if(optimizeBox){
						//调班
						var sm = Ext.getCmp("choosedGrid").getSelectionModel();
						if(!sm.hasSelection()){
							Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.34','请选择一条记录')}");
							return false;
						}
						scheduleId = sm.getSelected().get("scheduleId");
						dayDt = getDay(sm.getSelected().get("dayDt"));
						flag = '3';
					}else {
						Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.35','请选择一种调班类型！')}");
						return false;
					}
					
					//先校验是否连续6天排班
					Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.saveConfirm,function(btn){
						if('yes' == btn){
							Ext.getCmp("optDriverForm").getForm().submitEx({
								url:"preSchedule_saveOptInfo.action",
								params:{
									'paramsDto.vacationComboValue':vacationComboValue,
									'paramsDto.scheduleId':scheduleId,
									'paramsDto.dayDt':dayDt,
									'paramsDto.scheduleIdOld':scheduleIdOld,
									'paramsDto.dayDtOld':dayDtOld,
									'paramsDto.flag':flag,
									'ignore':2
									
								},
								successCallback:function(result){
									if(Ext.isEmpty(result)){
										Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
										return false;
									}
									if(!Ext.isEmpty(result.retMsg)){
										if(result.retMsg == "day6New" || result.retMsg == "day6Old"
											|| result.retMsg == "day6"){
											var alertMsg;
											if(result.retMsg == "day6New"){
												alertMsg = "${app:i18n_def('preSchedule.js.36','被调班人连续出勤6天，存在疲劳驾驶风险，是否保存？')}";
											}else if(result.retMsg == "day6Old"){
												alertMsg = "${app:i18n_def('preSchedule.js.37','需调班人连续出勤6天，存在疲劳驾驶风险，是否保存？')}";
											}else{
												alertMsg = "${app:i18n_def('preSchedule.js.38','被调班人和需调班人均连续出勤6天，存在疲劳驾驶风险，是否保存？')}";
											}
											Ext.Msg.confirm(Ext.i18n.prompt,alertMsg,function(btn){
												if('yes' == btn){
													Ext.getCmp("optDriverForm").getForm().submitEx({
														url:"preSchedule_saveOptInfo.action",
														params:{
															'paramsDto.vacationComboValue':vacationComboValue,
															'paramsDto.scheduleId':scheduleId,
															'paramsDto.dayDt':dayDt,
															'paramsDto.scheduleIdOld':scheduleIdOld,
															'paramsDto.dayDtOld':dayDtOld,
															'paramsDto.flag':flag,
															'ignore':1
															
														},
														successCallback:function(result){
															if(Ext.isEmpty(result)){
																Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
																return false;
															}
															if(!Ext.isEmpty(result.retMsg)){
																Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
																return false;
															}
															Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.saveSuccess,function(){
																optform.reset();
																newWin.hide();
																dlg.getGrid().getStore().reload();
															});
														}
													})
												}
											});
										}else{
											Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
										}
										return false;
									}
									Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.saveSuccess,function(){
										optform.reset();
										newWin.hide();
										dlg.getGrid().getStore().reload();
									});
								}
							})
						}
					});
				}
				},
			</app:isPermission>
		{text:Ext.i18n.cancel,minWidth:60,cls:'x-btn-normal',
			handler:function(){
				newWin.hide();
			}
		}],
		layout:"fit",
		items:[{
			layout:"column",
			xtype:"form",
			id:"optimizeWinForm",
			frame:true,
			defaults:{
				xtype:'panel',
				columnWidth:.5,
				layout:"form",
				labelAlign:"right",
				labelWidth:65
			},
			items:[{	
			    columnWidth:.55,
			    border:false,
			    id : 'deltailPanel',
			    layout:"column",	
			    defaults:{
					xtype:"panel",
					layout:"form",
					columnWidth:.5,
					labelWidth:80,
					labelAlign:"right"
				},
				items:[{xtype:'hidden',name:"scheduleIdOld"},
				{xtype:'hidden',name:"deptId"},{
					items:[{
						xtype:'textfield',
						name:'driver.empCode',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.39','驾驶员工号')}",
						readOnly:true,
						width:110
					}]
				},{
					items:[{
						xtype:'textfield',
						name:'driver.driverName',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.40','驾驶员名字')}",
						readOnly:true,
						width:110
					}]
				},{
					items:[{
						xtype:'textfield',
						name:'classType',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.41','班次类别')}",
						readOnly:true,
						width:110
					}]
				},{
					items:[{
						xtype:'textfield',
						name:'arrangeNo',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.42','班次代码')}",
						readOnly:true,
						width:110
					}]
				},{
					items:[{
						xtype:'textfield',
						name:'isValid',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.43','是否有效')}",
						readOnly:true,
						width:110
					}]
				},{
					items:[{
						xtype:'textfield',
						name:'dayDtOld',
						width:120,
						fieldLabel:"${app:i18n_def('preSchedule.js.44','日期')}",
						readOnly:true,
						width:110
					}]
				},{columnWidth:.1,xtype:"panel"},{
					columnWidth:.97,
					height:250,
					xtype:'grid',
					id:"arrangeGrid",
					style:"margin-left:15px;border-bottom: 1px solid #8db2e3;border-left: 1px solid #8db2e3;border-right: 1px solid #8db2e3;",
					enableHdMenu:false,
					store:new Ext.data.ArrayStore({
					  	fields:['startTm',
					  	        'endTm',
					  	        'startDept',
					  	        'endDept',
					  	        'valid'],
					  	data:[]
					}),
					columns:[{header:Ext.i18n.startTm,dataIndex:"startTm",sortable:false,width:60
						},{header:Ext.i18n.endTm,dataIndex:"endTm",sortable:false,width:60
						},{header:Ext.i18n.startDept,dataIndex:"startDept",sortable:false,width:85
						},{header:Ext.i18n.endDept,dataIndex:"endDept",sortable:false,width:85
						},{header:Ext.i18n.isValid,dataIndex:"valid",sortable:false,width:60,
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
					columnWidth:1,
					style:'padding-top:5px;',
					items:[{
						xtype:'textarea',
						name:'remark',
						width:315,
						height:35,
						fieldLabel:"${app:i18n_def('preSchedule.js.45','调班过程信息')}",
						readOnly:true
					}]
				}]
			},{
				columnWidth:.45,
				layout:"column",
				defaults:{
					columnWidth:1,
					labelWidth:20,
					layout:"form",
					xtype:"panel"
				},
				items:[{
					items:[{
					 	xtype:"checkbox",
						fieldLabel:'假',
						name:"vacationBox",
						handler:function(obj,checked){
							if(checked){
								var form = Ext.getCmp("optimizeWinForm").getForm();
								form.findField("restBox").setValue(false);
								form.findField("optimizeBox").setValue(false);
								Ext.getCmp("searchBtn").setDisabled(true);
								Ext.getCmp("choosedGrid").getStore().removeAll();
							}
						}
			        }]
				},{
					items:[{
						xtype:"combo",
						hiddenName:"vacationCombo",
						width:220,
						fieldLabel:'',
					    mode: 'local',
					    displayField:"value",
					    valueField:"value",
					    triggerAction: 'all',
					    editable:false,
					    store:new Ext.data.SimpleStore({
							fields:["text","value"],
							data:dayArr
						})
					}]
				},{
					items:[{
					 	xtype:"checkbox",
						fieldLabel:'休',
						name:"restBox",
						width:220,
						handler:function(obj,checked){
							if(checked){
								var form = Ext.getCmp("optimizeWinForm").getForm();
								form.findField("vacationBox").setValue(false);
								form.findField("optimizeBox").setValue(false);
								Ext.getCmp("searchBtn").setDisabled(true);
								Ext.getCmp("choosedGrid").getStore().removeAll();
							}
						}
			        }]
				},{
					items:[{
					 	xtype:"checkbox",
						fieldLabel:"${app:i18n_def('preSchedule.js.46','调')}",
						name:"optimizeBox",
						width:220,
						maxLength:50,
						handler:function(obj,checked){
							if(checked){
								var form = Ext.getCmp("optimizeWinForm").getForm();
								form.findField("vacationBox").setValue(false);
								form.findField("restBox").setValue(false);
								Ext.getCmp("searchBtn").setDisabled(false);
							}
						}
			        }]
				},{
					columnWidth:1,
					xtype:"fieldset",
					style:"padding-left:10px;padding-right:10px;padding-top:10px;",
					height:280,
					title:"${app:i18n_def('preSchedule.js.47','被调班人班次明细')}",
					layout:"column",
					defaults:{
						layout : 'form',
						border : false,
						labelWidth:80
					},
					items:[{
						columnWidth : .33,
						labelWidth :40,
						items : [{
							xtype:"textfield",
							fieldLabel:"${app:i18n_def('preSchedule.js.48','工号')}",
							allowBlank:false,
							name:"empCode",														
							anchor : '99%'													
						}]
					},{
						columnWidth : .55,
						labelWidth :65,
						items : [{
							xtype:'datefield',
							name:'optDriverDate',
							allowBlank:false,
							fieldLabel:"${app:i18n_def('preSchedule.js.49','调班日期')}",
							format:'Y-m-d',
							readOnly:true,
							anchor : '99%'
						}]
					},{
						xtype:'button',
						text:"${app:i18n_def('preSchedule.js.50','查询')}",
						anchor : '95%',
						id:"searchBtn",
						columnWidth:.07,
						cls:'x-btn-normal',
						handler:function(){
							var form = Ext.getCmp("optimizeWinForm").getForm();
							//选中调班，则需要查询.
							var empCode = form.findField("empCode").getValue();
							var yearMonthDay = form.findField("optDriverDate").getRawValue();
							if(Ext.isEmpty(empCode) || Ext.isEmpty(yearMonthDay)) {
								Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.51','请输入条件')}");	
								return false;
							}
							var deptId = dlg.getQueryForm().findField("deptId").getValue();
							var store = Ext.getCmp("choosedGrid").getStore();
							var params = {"optEmpCode":empCode,"optDate":yearMonthDay,"deptId":deptId};
							store.load({params:params});
						}
					},{
						xtype:'grid',
						height:210,
						columnWidth:1,
						id:"choosedGrid",
						loadMask: true,
						style:"border: 1px solid #8db2e3;",
						sm:addSm,
						store:new Ext.data.JsonStore({
							url:"preSchedule_findOptDriver.action",
							timeout:1000*60*3,
							root:"optDrivers",
						  	fields:[{name:'scheduleId',mapping:'SCHEDULE_ID'},
						  	      {name:'driver.name',mapping:'EMP_CODE'},
						  	      {name:'driver.code',mapping:'DRIVER_NAME'},
						  	      {name:'dayDt',mapping:'DAY_DT'},
						  	      {name:'arrangeNo',mapping:'ARRANGE_NO'}]
					  	}),
						columns:[addSm,{header:'ID',dataIndex:"scheduleId",hidden:true,sortable:false,width:55
						},{header:"${app:i18n_def('preSchedule.js.52','姓名')}",dataIndex:"driver.name",sortable:false,width:55
						},{header:"${app:i18n_def('preSchedule.js.48','工号')}",dataIndex:"driver.code",sortable:false,width:50
						},{header:"${app:i18n_def('preSchedule.js.44','日期')}",dataIndex:"dayDt",sortable:false,width:70,
							rendererCall:function(value){
					        	if(Ext.isEmpty(value)){
					        		return '';
					        	}
					        	return value.substring(0,10);
					        }
						},{header:"${app:i18n_def('preSchedule.js.42','班次代码')}",dataIndex:"arrangeNo",sortable:false,width:80
						}]
					}]
		        }]
			}]
		},{xtype:'form',id:'optDriverForm'}]
	});
	//获取选中年月
	var record = Ext.getCmp("searchGrid").getStore().getById(recordId);
	Ext.getCmp("optimizeWinForm").getForm().findField("deptId").setValue(dlg.getQueryForm().findField("deptId").getValue());
	var yearMonth = record.data.yearMonth;
	var param_year = eval(yearMonth.substring(0,4));
	var param_month = eval(yearMonth.substring(5));
	//获取当前年月
	var curr_year = new Date().getFullYear();
	var curr_month = new Date().getMonth()+1;
	//只有选中当月数据才允许调班
	if(param_year == curr_year && param_month == curr_month){
		if(!Ext.isEmpty(Ext.getCmp("optimizeBt"))){
			Ext.getCmp("optimizeBt").enable();
		}
	}else{
		if(!Ext.isEmpty(Ext.getCmp("optimizeBt"))){
			Ext.getCmp("optimizeBt").disable();
		}
	}
	newWin.show();
	Ext.Ajax.requestEx({
		url:"preSchedule_deltailSchedule.action",
		timeout:1000*60*1,
		params:{"param.id":recordId,"param.yearMonth":yearMonth,"dayNum":dayIdx},
		successCallback:function(result){
			var form = Ext.getCmp("optimizeWinForm").getForm();
			//窗体已经关闭，则不进行赋值
			if(Ext.isEmpty(form) || !newWin.isVisible()){
				return false;
			}
			if(Ext.isEmpty(result)){
				Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.netError);
				return false;
			}
			if(!Ext.isEmpty(result.retMsg)){
				Ext.Msg.alert(Ext.i18n.prompt,result.retMsg);
				return false;
			}
			/*if(Ext.isEmpty(result.paramMap) || Ext.isEmpty(result.paramMap.scheduleInfos)){
				Ext.Msg.alert(Ext.i18n.prompt,"${app:i18n_def('preSchedule.js.53','排班信息不存在，请刷新页面')}");
				return false;
			}*/
			var dataArray = [];
			if(!Ext.isEmpty(result.paramMap.scheduleInfos) && result.paramMap.scheduleInfos.length>0){
				Ext.each(result.paramMap.scheduleInfos,function(item){
					dataArray.push([item.startTm,item.endTm,item.startDept,item.endDept,item.valid])
				})
			}
			if(!Ext.isEmpty(dataArray) && dataArray.length > 0 && !Ext.isEmpty(Ext.getCmp("arrangeGrid"))){
				Ext.getCmp("arrangeGrid").getStore().loadData(dataArray);
			}
			form.findField("driver.driverName").setValue(result.paramMap.driverName);
			form.findField("driver.empCode").setValue(result.paramMap.driverCode);
			form.findField("classType").setValue(dispalyClassName(result.paramMap.classType));
			form.findField("arrangeNo").setValue(result.paramMap.arrangeNo);
			form.findField("isValid").setValue(getValid(result.paramMap.isValid));
			form.findField("dayDtOld").setValue(getDay(result.paramMap.dayDt));
			//被调班人查询条件-日期
			form.findField("optDriverDate").setValue(getDay(result.paramMap.dayDt));
			form.findField("scheduleIdOld").setValue(result.paramMap.scheduleId);
			form.findField("remark").setValue(result.paramMap.remark);
		}
	});
}
//导入
dlg.on(Ext.i18n.upload,function(){
	var uploadWin = this.createModalDialog();
	uploadWin.add({xtype:"form",
		fileUpload:true,
		title:Ext.i18n.upload,
		tools:[{id:'close',handler:function(){uploadWin.hide();}}],
		tbar:[{text:Ext.i18n.save,cls:'x-btn-normal',minWidth:60,handler:function(){
				var form = uploadWin.getComponent(0).getForm();
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
							url:'preSchedule_saveFile.action',
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
								if(!Ext.isEmpty(result.retMsg)){
									var msgStr = "";
									var msgs = new Array();
									msgs = result.retMsg.replace(/;$/,"").split(";");
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
										items:[{xtype:'panel',frame:true,
										autoScroll:true,html:msgStr}]
									});
									winInfo.show();
									return false;
								}
								Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.uploadSuccess,function(){
									uploadWin.hide();
									dlg.getGrid().getStore().reload();
								});
							}
						})
					}
				})
			}
		},{text:Ext.i18n.cancel,cls:'x-btn-normal',minWidth:60,handler:function(){uploadWin.hide();}
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
						+"<div style='text-indent:2em;font-weight:normal;color:red;padding-left:1em;'>3.&nbsp;&nbsp;&nbsp;&nbsp;"+Ext.i18n.html4+"</div>"
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
									'href="../pages/vmsarrange/template/preSchedule.xls" target="blank">preSchedule.xls</a>',
							style:'margin-left:4em;width:100%;height:23px;line-height:23px;'
					}]
			}]
	});
	uploadWin.show();
},dlg);

//导出
dlg.on(Ext.i18n.exported,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseDept);
		return false;
	}
	if(Ext.isEmpty(dlg.getQueryForm().findField("param.yearMonth").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.chooseYearMonth);
		return ;
	}
	Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmDownload,function(button,text){
		if(button == "yes"){
			dlg.getQueryForm().submitEx({
				url:"preSchedule_listRealReport.action",
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
					window.location = "../vmsarrange/preSchedule_downloadFile.action?fileName="+encodeURI(encodeURI(result.fileName));
				}
			})
		}
	})
},dlg);

//预排班导出
dlg.on(Ext.i18n.printed,function(){
	if(Ext.isEmpty(dlg.getQueryForm().findField("deptId").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.pleaseChooseDept);
		return false;
	}
	if(Ext.isEmpty(dlg.getQueryForm().findField("param.yearMonth").getValue())){
		Ext.Msg.alert(Ext.i18n.prompt,Ext.i18n.chooseYearMonth);
		return ;
	}
	Ext.Msg.confirm(Ext.i18n.prompt,Ext.i18n.confirmDownload,function(button,text){
		if(button == "yes"){
			dlg.getQueryForm().submitEx({
				url:"preSchedule_listReport.action",
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
					window.location = "../vmsarrange/preSchedule_printFile.action?fileName="+encodeURI(encodeURI(result.fileName));
				}
			})
		}
	})
},dlg);