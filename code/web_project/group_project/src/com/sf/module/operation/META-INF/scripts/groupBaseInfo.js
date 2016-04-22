//<%@ page language="java" contentType="text/html; charset=utf-8"%>
/**
 *
 */
var i18n_def={add:"${app:i18n_def('groupBaseInfo.add','新增')}",
			  edit:"${app:i18n_def('groupBaseInfo.edit','编辑')}",
			  search:"${app:i18n_def('groupBaseInfo.search','查询')}",
			  modify:"${app:i18n_def('groupBaseInfo.modify','修改')}",
			  del:"${app:i18n_def('groupBaseInfo.del','删除')}",
			  groupexport:"${app:i18n_def('groupBaseInfo.export','导出')}",
			  groupimport:"${app:i18n_def('groupBaseInfo.import','导入')}",
			  querycondition:"${app:i18n_def('groupBaseInfo.querycondition','查询条件')}",
			  groupcode:"${app:i18n_def('groupBaseInfo.groupcode','小组代码')}",
			  deptcode:"${app:i18n_def('groupBaseInfo.deptcode','网点代码')}",
			  groupname:"${app:i18n_def('groupBaseInfo.groupname','小组名称')}",
			  disabledt:"${app:i18n_def('groupBaseInfo.disabledt','失效日期')}",
			  remark:"${app:i18n_def('groupBaseInfo.remark','备注')}",
			  areacode:"${app:i18n_def('groupBaseInfo.areacode','地区代码')}",
			  deptname:"${app:i18n_def('groupBaseInfo.deptname','网点名称')}",
			  addtitle:"${app:i18n_def('groupBaseInfo.addtitle','新增小组信息')}",
			  cancel:"${app:i18n_def('groupBaseInfo.cancel','取消')}",
			  deptinfo:"${app:i18n_def('groupBaseInfo.deptinfo','网点信息')}",
			  sfname:"${app:i18n_def('groupBaseInfo.sfname','顺丰速运')}",
			  save:"${app:i18n_def('groupBaseInfo.save','保存')}"


};

function datetimeRenderer(v) {
	if(v){
		if (typeof(v) == "string") {
			var dDate = v.substr(0,v.indexOf('T'));
			return dDate;
		}
	}
	return '';

}

function deptCodeShow(v){
	if(v){
		if(typeof(v)=="string"){
			var v_grid_deptCode = v.split('/')[0];
			return v_grid_deptCode;
		}
	}
	return '';
}

function deptNameShow(v){
	if(v){
		if(typeof(v)=="string"){
			var v_grid_deptName = v.split('/')[1];
			return v_grid_deptName;
		}
	}
	return '';
}

var deptId="";
var areaCode="";
//查询按钮
var btnSearch = new Ext.Button({
	text : i18n_def.search,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		if(deptId == ""){
			Ext.MessageBox.alert('提示', "请选择网点");
			return;
		}
		loadMainGrid();
	}
});
//新增按钮
var btnAdd = new Ext.Button({
	text : i18n_def.add,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		addGroupInfo();
	}
});
//修改按钮
var btnEdit = new Ext.Button({
	text : i18n_def.modify,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		editGroupBaseInfo();
	}
});
//删除按钮
var btnDelete = new Ext.Button({
	text : i18n_def.del,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		onDelete();
	}
});
//导出按钮
var btnExport = new Ext.Button({
	text : i18n_def.groupexport,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		exportBaseInfos();
	}
});

//导入按钮
var btnImport = new Ext.Button({
	text : i18n_def.groupimport,
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		importGroupBaseInfo();
	}
});

var topFrom= new Ext.FormPanel({
    labelAlign: 'left',
    frame:true,
    xtype:'form',
    id:'saveForm',
    url:'saveGroupInfo.action',
    bodyStyle:'padding:5px 5px 0',
    border:false,
    width: 600,
    items: [{
        layout:'column',
        items:[{
            columnWidth:.5,
            layout: 'form',
            items: [{
                xtype:'textfield',
                fieldLabel: i18n_def.areacode,
                labelSeparator:'<span style="color:#FF0000;">*</span>',
                allowBlank:false,
                name: 'areaCode',
                id:'groupBaseInfo.areaCode',
                readOnly:true,
                anchor:'95%'
            }, {
            	xtype:'hidden',
            	name:'groupBaseInfo.id'
            },{
                xtype:'textfield',
                fieldLabel: i18n_def.groupname,
                labelSeparator:'<span style="color:#FF0000;">*</span>',
                allowBlank:false,
                id:'groupBaseInfo.groupName',
                name: 'groupBaseInfo.groupName',
                anchor:'95%'
            },{
            	xtype:'datefield',
            	fieldLabel:i18n_def.disabledt,
            	format:'Y-m-d',
            	id:'disableDt',
            	name:'disableDt',
            	anchor:'95%'
            }]
        },{
            columnWidth:.5,
            layout: 'form',
            items: [{
            	xtype:'hidden',
            	name:'groupBaseInfo.deptId',
            	id:'groupBaseInfo.deptId'
            },{
                xtype:'textfield',
                fieldLabel: i18n_def.deptcode,
                labelSeparator:'<span style="color:#FF0000;">*</span>',
                id: 'groupBaseInfo.deptCode',
                readOnly:true,
                allowBlank:false,
                anchor:'95%'
            },{
                xtype:'textfield',
                fieldLabel: i18n_def.groupcode,
                labelSeparator:'<span style="color:#FF0000;">*</span>',
                name: 'groupBaseInfo.groupCode',
                id:'groupBaseInfo.groupCode',
                allowBlank:false,
                anchor:'95%'
            }]
        }]
    },{
    	xtype:'textarea',
    	name:'groupBaseInfo.remark',
    	id:'groupBaseInfo.remark',
    	fieldLabel:i18n_def.remark,
    	anchor:'95%'
    }]
});


function validateForm(form){
	if(!form.isValid()){
		Ext.Msg.alert('提示','请填写所有必填项');
		return false;
	}
	var disableDt = form.findField('disableDt').getValue();
	if(disableDt){
		if(new Date() > disableDt){
			Ext.Msg.alert('提示','失效日期不能小于等于当前日期');
			return false;
		}
	}

	var groupName = form.findField('groupBaseInfo.groupName').getValue();
	var pattern = /^[a-zA-Z\u4e00-\u9faf]+$/;
	if (!pattern.test(groupName))
	{
	  Ext.Msg.alert('提示','小组名称只能是字母和汉字');
	  return false;
	}
	if(groupName.length > 20){
		Ext.Msg.alert('提示','小组名称输入过长，最大长度20');
		return false;
	}




	var groupCode = form.findField('groupBaseInfo.groupCode').getValue();
	if(groupCode.length > 15){
		Ext.Msg.alert('提示','组别代码输入过长，最大长度15');
		return false;
	}

	var remark = form.findField('groupBaseInfo.remark').getValue();
	if(remark.length > 30){
		Ext.Msg.alert('提示','备注信息输入过长，最大长度30');
		return false;
	}
	return true;
}

Ext.ux.FormSubmitFailure = function(form, action) {
	if (action.failureType == Ext.form.Action.CLIENT_INVALID) {
		return ;
	}
	Ext.MessageBox.alert("${app:i18n_def('groupBaseInfo.save.error','数据保存失败')}", action.result.status);
};


var win;

win=new Ext.Window({
	width:780,
	height:250,
	modal:true,
	border:true,
	bodyBorder:false,
	closable:false,
	resizable:false,
	layout:'fit',
	items:[topFrom],
	title : i18n_def.addtitle,
	fbar:[{
		text:i18n_def.save,
		handler : function(){
			var form = Ext.getCmp('saveForm').getForm();
			if(validateForm(form)){
				Ext.getCmp('saveForm').getForm().submit({
					success:function(form, action){
						if (action.result.msg) {
							Ext.Msg.alert("${app:i18n_def('group.prompt','提示')}", action.result.msg);
							loadMainGrid();

						} else {
							Ext.Msg.alert("${app:i18n_def('vehicle.prompt','提示')}", "${app:i18n_def('vehicle.prompt.operate.success','操作成功')}", function(){
								win.hide();
								loadMainGrid();
							});


						}
					},
					failure:Ext.ux.FormSubmitFailure,
					scope:this,
					waitTitle:"${app:i18n_def('groupBaseInfo.prompt.wait','请稍后')}",
					waitMsg:"${app:i18n_def('groupBaseInfo.prompt.execting','正在执行操作...')}"
				});

			}
		}
	},{
		text:i18n_def.cancel,
		handler : function() {
			win.hide();
		}
	}]
});

var addGroupInfo=function(){
	if(dept_leaf){
		if(deptId == ""){
			Ext.MessageBox.alert('提示', "请选择网点");
			return;
		}
		win.setTitle("新增小组信息");
		Ext.getCmp('saveForm').getForm().reset();
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptId").setValue(deptId);
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptCode").setValue(Ext.getCmp("query.deptCode").getValue().split('/')[0]);
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.areaCode").setValue(areaCode);
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptId").enable();
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupName").enable();
		Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupCode").enable();
		win.show();
	}else{
		Ext.MessageBox.alert('提示', "只有中转场和航空操作组才能新增小组");
		return;
	}





};


var editGroupBaseInfo = function(){
	if(grid.getSelectionModel().getSelections().length != 1){
		Ext.MessageBox.alert('提示', "请选择一条记录");
		return;
	}
	win.show();
	win.hide();
	Ext.getCmp('saveForm').getForm().reset();
	win.setTitle("修改小组信息");
	var record =grid.getSelectionModel().getSelections()[0];
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptId").setValue(record.data.deptId);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptCode").setValue(record.data.deptName.split('/')[0]);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupName").setValue(record.data.groupName);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupCode").setValue(record.data.groupCode);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.remark").setValue(record.data.remark);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.id").setValue(record.data.id);
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.areaCode").setValue(record.data.areaCode);
	if(record.data.disableDt)
		Ext.getCmp('saveForm').getForm().findField("disableDt").setValue(record.data.disableDt.substr(0,record.data.disableDt.indexOf('T')));
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.deptId").disable();
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupName").disable();
	Ext.getCmp('saveForm').getForm().findField("groupBaseInfo.groupCode").disable();
	win.show();


};

function onDelete() {
	var records = grid.getSelectionModel().getSelections();
	if (records.length < 1) {
		Ext.MessageBox.alert("提示", "请选择要删除的数据");
	} else {
		Ext.MessageBox.confirm("提示","确定删除选择的数据吗?", deleteRecord);
	}
}

function deleteRecord(result) {
	if (result == 'yes') {
		var records = grid.getSelectionModel().getSelections();
		var ids = new Array();
		for(var i = 0; i < records.length; i++) {
			ids.push(records[i].data.id);
		}

		Ext.Ajax.request({params: {ids: ids.toString()},
			url: "delGroupInfos.action",
			success: function(response) {
				var resp = Ext.util.JSON.decode(response.responseText);
				if (resp.status=="ok") {
					Ext.MessageBox.alert("提示","数据删除成功");
					grid.getStore().load();
				} else {
					Ext.MessageBox.alert("数据删除失败", resp.status);
				}
			}
		});
	}
}

//导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}

//导入方法
importGroupBaseInfo = function(){
			var win = new Ext.Window({
						width:580,
						height:200,
						modal:true,
						border:false,
						bodyBorder:false,
						closable:false,
						resizable:false,
						layout:'fit',
						title : '导入',
						items : [{
								xtype : 'form',
								border : false,
								fileUpload : true,
								frame : true,
								labelAlign : 'right',
								items : [{
										border : false,
										height : 20
								},{
										border : false,
										xtype : 'label',
										style : 'margin-left : 60px;',
										html : '<font color=red size=4>最多导入1000条数据</font>'
								},{
									border : false,
									height : 10
							},{
										 xtype : 'textfield',
										 inputType:'file',
										 width : 290,
										 name : 'upload',
										 id:'upload',
										 fieldLabel : '文件路径'
								},{
									xtype:'hidden',
									name:'fileName',
									id:'fileName'
								},
							{
									border : false,
									height : 10
							},{
									border : false,
									xtype : 'label',
									style : 'margin-left : 40px;',
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemplate()">小组导入模板下载</a>'
							}],
							fbar : [{
									text : '上传',
									handler :function(){
											var form = this.ownerCt.ownerCt;
											var basicForm = form.getForm();
											var fileName = Ext.getCmp('upload').getValue();
											basicForm.findField('fileName').setValue(fileName);
											if(!/.xls$/.test(fileName)){
												Ext.Msg.alert("提示","系统只支持xls类型文件上传，请下载模版");
												return false;
											}
											basicForm.submit({
												url: '../schedulingBase/groupBaseaInfoUploadFile.action',
												success: function(form, action) {
													win.close();
													var url = "../common/downloadReportFile.action?"+ encodeURI(encodeURI(action.result.fileName));
													//window.location = url;
													var aTag= "";
													if(action.result.fileName){
															aTag="<a href='#' url='"+url+"'     onclick='downError(this)'>错误数据下载</a>";
													}
													Ext.Msg.alert('提示', action.result.msg+aTag);
													loadMainGrid();
												},
												failure : function(form, action) {
													Ext.Msg.alert('提示', action.result.msg);
													win.close();
												},
												waitTitle:'提示',
												waitMsg:'正在导入，请稍后'



											});
									}
							},{
								text : '取消',
								handler :function(){
										win.close();
								}
							}]
						}]
			}).show();

}

//模板下载
var downTemplate = function(){
		window.location = "../pages/schedulingBase/template/小组导入模版.xls";
}

var exportBaseInfos=function(){
	if(deptId == ""){
		Ext.MessageBox.alert('提示', "请选择网点");
		return;
	}
	sumitExportBaseInfos();
}

function sumitExportBaseInfos()  {
	Ext.getCmp('searchForm').getForm().doAction(
							'submit',{
								waitTitle:"提示",
								waitMsg:"报表导出",
								url:"exportGroupInfos.action",
								success:showExportResult,
								failure:function(form, action){
									Ext.MessageBox.alert('提示',action.result.status);
								}
							});
					}

function showExportResult(form, action) {
	var fileName = action.result.fileName;
	var url = "../common/downloadReportFile.action?" + fileName;
	window.location = url;
}

var dept_leaf = false;
//左侧网点树
var treePanel = new Ext.tree.TreePanel({
	region:'west',
	margins:'1 1 1 1',
	width:245,
	title:i18n_def.deptinfo,
	collapsible:true,
	autoScroll:true,
	root :new Ext.tree.AsyncTreeNode(
			{
				id : '0',
				text : i18n_def.sfname,
				loader : new Ext.tree.TreeLoader(
						{
							dataUrl : "../common/deptTree.action?textField=deptName&idField=id&leafField=areaCode&clsField=&childrenField="
						})
			}),
	  listeners : {
		  	beforeclick : function(node,e){
		  			Ext.getCmp("query.deptCode").setValue(node.text);
				 	Ext.getCmp("query.deptId").setValue(node.id);
				 	deptId=node.id;
				 	dept_leaf = node.leaf;
				 	loadMainGrid();
		  			 if(node.leaf){
		  				 	areaCode=node.attributes.parentCode;
		  			 }
		  	}
	  }
});

var testPanel = new Ext.Panel({
							frame:true,
							height:100,
							html:"text",
							items:[{
								  columnWidth : .4,
								  labelWidth:120,
								  labelAlign:'right',
								  layout:'form',
								  items : [{
										xtype:'textfield',
										id:'query.deptIds',
										readOnly : true,
										fieldLabel:'<font color=red>i18n_def.deptcode*</font>',
										anchor : '90%'
									}]
						}]

						}
		);

Ext.ux.GridDataLoadCallback = function (r, o, success){
	if (!success) {
		this.removeAll();
		Ext.Msg.alert("${app:i18n_def('groupBaseInfo.prompt','提示')}", "${app:i18n_def('groupBaseInfo.prompt.load.data.failure','系统出现异常,加载数据失败!')}");
	}
};




var groupStore = new Ext.data.JsonStore({
	url:'listByPageGroupInfo.action',
	root:'groupBaseInfos',
	totalProperty:'totalSize',
	fields:['deptId', 'groupCode', 'groupName', 'disableDt','id','areaCode','deptName','remark'],
	listeners:{beforeload:function(s){
		s.baseParams = Ext.getCmp('searchForm').getForm().getValues();
		s.baseParams["limit"] = grid.getTopToolbar().pageSize;
	}
	}
});

function loadMainGrid() {
	groupStore.load({params : {start : 0}, callback:Ext.ux.GridDataLoadCallback});
}

//分页组件
var pageBar =   new Ext.PagingToolbar({
    store: groupStore,
    displayInfo: true,
    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
    pageSize: 20,
   emptyMsg : '未检索到数据'
});

var mSm = new Ext.grid.CheckboxSelectionModel({singleSelect:false, header:' '});

var checkboxSelectionModel2 = new Ext.grid.CheckboxSelectionModel({});


var grid = new Ext.grid.GridPanel({
	store:groupStore,
	 sm: checkboxSelectionModel2,
	 frame: true,
	 tbar:pageBar,
	 //title:'结果显示区',
	 iconCls: 'icon-grid',
	 viewConfig:{
		 forceFit:true
	},
	 columns: [
	           	new Ext.grid.RowNumberer(),
	           	checkboxSelectionModel2,
	            {header: i18n_def.areacode, dataIndex: 'areaCode'},
	            {header:i18n_def.deptcode,dataIndex:'deptName',renderer:deptCodeShow},
	            {header: i18n_def.deptname, dataIndex: 'deptName',renderer:deptNameShow},
	            {header: "ID", dataIndex: 'id',hidden:true},
	            {header: "deptId", dataIndex: 'deptId',hidden:true},
	            {header: "remark", dataIndex: 'remark',hidden:true},
	            {header: i18n_def.groupcode, dataIndex: 'groupCode'},
	            {header:i18n_def.groupname,dataIndex:'groupName'},
	            {header:i18n_def.disabledt,dataIndex:'disableDt',renderer:datetimeRenderer}
	        ]


});


//顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout:'column',
	height : 160,
	tbar:['-',
	      <app:isPermission code="/scheduling/groupInfoSearch.action">btnSearch</app:isPermission>
	      	,' '
	      <app:isPermission code="/scheduling/groupInfoAdd.action">,btnAdd</app:isPermission>
	       , ' '
	      <app:isPermission code="/scheduling/groupInfoEdit.action">,btnEdit</app:isPermission>
	        ,' '
	      <app:isPermission code="/scheduling/groupInfoDelete.action">,btnDelete</app:isPermission>
	      	,' '
	      <app:isPermission code="/scheduling/groupInfoExport.action">,btnExport</app:isPermission>
	      	,' '
	      <app:isPermission code="/scheduling/groupInfoImport.action">,btnImport</app:isPermission>
	],
	items : [{
		xtype:'form',
		title:i18n_def.querycondition,
		id:'searchForm',
		layout:"column",
		columnWidth : 1,
		style : 'margin-top:5px;',
		frame:true,
		items : [{
				  columnWidth : .4,
				  labelWidth:120,
				  labelAlign:'right',
				  layout:'form',
				  items : [{
						xtype:'textfield',
						id:'query.deptCode',
						readOnly : true,
						fieldLabel:i18n_def.deptcode,
						labelSeparator:'<span style="color:#FF0000;">*</span>',
						anchor : '90%'
					},{
						xtype:'hidden',
						id:'query.deptId',
						name:'groupBaseInfo.deptId'

					}]
		},{
			  columnWidth : .4,
			  labelWidth:120,
			  labelAlign:'right',
			  layout:'form',
			  items : [{
					xtype:'textfield',
					id:'query.groupName',
					name:'groupBaseInfo.groupName',
					fieldLabel:i18n_def.groupname,
					anchor : '90%'
				}]
	},{
		  columnWidth : .4,
		  labelWidth:120,
		  labelAlign:'right',
		  layout:'form',
		  items : [{
				xtype:'textfield',
				id:'query.groupCode',
				name:'groupBaseInfo.groupCode',
				fieldLabel:i18n_def.groupcode,
				anchor : '90%'
			}]
},{
	  columnWidth : .4,
	  labelWidth:120,
	  labelAlign:'right',
	  layout:'form',
	  items : [{
			xtype:'combo',
			id:'query.state',
			typeAhead: true,
			lazyRender:true,
			hiddenName:'groupBaseInfo.status',
		    triggerAction: 'all',
		    mode: 'local',
			fieldLabel:'状态查询',
			store : [['0','全部'],['1','有效'],['2','失效']],
			anchor : '90%'
		}]
}]
	}]
});



//中部的Panel
var centerPanel = new Ext.Panel({
	region:'center',
	margins:'1 1 1 0',
	items : [topPanel,grid],
	listeners : {
  		resize : function(p,adjWidth,adjHeight,rawWidth,rawHeight) {
  				grid.setWidth(adjWidth-5);
  				grid.setHeight(adjHeight-165);
  		}
  }

});

/**
 * 右下角提示信息window  begin
 */
//通知界面Store
var noticeStore = new Ext.data.JsonStore({
	url:"noticeList.action",
	root : 'noticeList'
	,fields: ["groupName","disableDt"]
});

function loadNoticeGrid() {
	noticeStore.load({params : {}, callback:Ext.ux.GridDataLoadCallback});
}

//通知界面表格
var noticeGrid = new Ext.grid.GridPanel({
	store: noticeStore,
	 columns: [
	            {header:i18n_def.groupname,dataIndex:'groupName'},
	            {header:i18n_def.disabledt,dataIndex:'disableDt',renderer:datetimeRenderer}
	        ]
	});
//通知弹出框
var noticeWindow = new Ext.Window({
			title:'通知',
			height:180,
			width:450,
			closeAction:"hide",
			plain:true,
			modal:false,
			resizable : false,
			layout : 'fit',
			items: [noticeGrid]
});

//移动window通知框
var h =0;
var moveWindow = function(){
		noticeWindow.setPosition(document.body.clientWidth-450,document.body.clientHeight-h);
		h=h+5;
		if(185==h){
			runner.stop(moveTask);
			h=0;
		}
}

//移动window的任务
var moveTask = {
			 run : function(){
				 moveWindow();
			 },
			  interval: 10//0.01秒
}

//显示通知框
var isShow = function(){

	Ext.Ajax.request({
		url : 'noticeShow.action',
		method : 'POST',
		success : function(res,config){
			var resp = Ext.util.JSON.decode(res.responseText);
			if(resp.noticeShow){
				noticeWindow.show();
				loadNoticeGrid();
				runner.start(moveTask);
			}
		},
		failure : function(res,config){
				Ext.Msg.alert("提示","获取即将失效小组信息失效！");
		}
});



}

//定义数据查询任务
var task = {
	    run: function(){
	    	//detailsAllListStore.load({callback:istrue});
	    	isShow();
	    },
	    interval: 300000//5分钟
}

//定义多线程任务
var runner = new Ext.util.TaskRunner();
//执行任务task
runner.start(task);




//初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout=300000;
	var viewreprot = new Ext.Viewport({
		layout : "border",
		items : [ treePanel,centerPanel]
	});

});