//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var filterDeptCodeType = '${filterDeptCodeType}';

var fieldDeptCode = new Ext.form.TextField({width:160,name:"fieldDeptCode", xtype : 'textfield'
	, listeners: {
        specialkey: function(field, e){
            if (e.getKey() == e.ENTER) {
            	queryDeptCode();
            }
        }
    }});

var queryDeptCode = function() {
	if (fieldDeptCode.getValue()=="") {
		Ext.Msg.alert('提示','网点代码不能为空', function(){
			fieldDeptCode.focus();
		});
		return;
	}
	
	Ext.Ajax.request({
		url:"../operation/userDeptAction_queryDeptCode.action",
		params:{fieldDeptCode:fieldDeptCode.getValue()},
		success:function(response){
			//debugger;
			var dept_ = Ext.util.JSON.decode(response.responseText);
			var path = dept_.path;
			if(path && path != '/0'){
				treePanel.root.reload();
				treePanel.selectPath(path);
				Ext.getCmp('query.deptId').setValue(dept_.deptCode + '/' + dept_.deptName);
				deptId = dept_.deptId;
				queryEmployee();
				
			} else {
				Ext.Msg.alert('提示','该网点不存在！', function(){
					fieldDeptCode.selectText();
				}, this);
			}
		} ,scope : this
	}, false);
};

//左侧网点树 
var treePanel = new Ext.tree.TreePanel({
	region:'west',
	margins:'1 1 1 1',
	width:245,
	title:'网点信息',
	collapsible:true,
	autoScroll:true,
	tbar : [
	        {text : "网点代码", xtype : 'label'}, 
	        fieldDeptCode ,
	        {
	        	icon : "../images/search.gif", 
	        	xtype : 'button', 
	        	style : 'margin-left:2px',
	        	scope : this,
	        	handler : queryDeptCode
	        }], 
	root :new Ext.tree.AsyncTreeNode(
			{
				id : '0',
				text : '顺丰速运',
				loader : new Ext.tree.TreeLoader(
						{
							dataUrl : "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
						})
			}),
	  listeners : {
		  	beforeclick : function(node,e){		  			
			  		if(node!=null && node.id!=0){
	  				 	Ext.getCmp("query.deptId").setValue(node.text);
	  				 	deptId = node.id;
	  				 	// 执行查询方法
  				 		queryEmployee(node);
	  			 }		
		  	} 
	  }		
});

// 查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		queryEmployee();
	}
});
// 新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
			 addEmployee();	
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
	}
});

var btnBatchModify = new Ext.Button({
	text : "批量修改分组",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
			var records = grid.getSelectionModel().getSelections();
			if(records.length<1){
				Ext.Msg.alert("提示","请选择要修改的数据！");		
				return;
			}
			var employees=[];
			for(var i=0;i<records.length;i++){
				var record = records[i];
				employees.push(record.data);
			}
			var record = grid.getSelectionModel().getSelected();
			updateBtnBatchModifyEmployee(record,employees);
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
		
		
	}
});

// 修改按钮
var btnEdit = new Ext.Button({
	text : "修 改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
			var records = grid.getSelectionModel().getSelections();
			if(records.length>1){
				Ext.Msg.alert("提示","只能选择一条数据!");
				return ;
			}
			var record = grid.getSelectionModel().getSelected();
			updateEmployee(record);
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
	}
});
// 删除按钮
var btnDelete = new Ext.Button({
	text : "删 除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		
		
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){

			var records = grid.getSelectionModel().getSelections();
			if(records.length>0){
				var codes="";
				var ids ="";
				var obj = {};
				for(var i=0;i<records.length;i++){						 
					 var record = records[i];
					 codes += record.json["EMP_CODE"]+"@@";
					 ids += record.json["EMP_ID"]+"@@";
					 obj[record.json["EMP_CODE"]]=record;		
					 if(record){
							if(record.json["EMP_CODE"].length<9){
								Ext.Msg.alert("提示","内部人员不能删除！");
								return;
							}	    
					 }
				}
				Ext.Ajax.request({
		    			url : '../operation/outEmployeeMgt_isSchedulMgtbyEmpCode.action',
		    			method : 'POST',
		    			params : {
		    				empCodes : codes
		    			},
		    			success : function(res,config){
		    				var data = Ext.decode(res.responseText);	
		    				if(data.success==true){
		    						deleteEmployee(ids, codes);
		    				}else{
			    					var msg = "";
		    						for(var j=0;j<data.root.length;j++){
		    							var id = data.root[j];
		    							msg += obj[id].json["EMP_NAME"]+";"
		    						}
		    						msg = msg +"已排班不能删除！"
		    						Ext.Msg.alert("提示",msg);
		    						return;
		    				}
		    			},
		    			failure : function(){
		    					Ext.Msg.alert("提示","人员删除出错！");
		    					return;
		    			}
				 });
			}else{
				 Ext.Msg.alert("提示","请选择要删除的数据！");				
				 return;
			}
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
	}
});

// 外包人员导入
var btnImport = new Ext.Button({
	text : "外包人员导入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
			importEmployee();
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
		
	}
});

var btnGroupImport = new Ext.Button({
	text : "人员分组导入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
			groupImportEmployee();
		}else{
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
	}
});

// 导出按钮
var btnExport = new Ext.Button({
	text : "导 出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			exportEmployee();
	}
});

// 模板下载
var downloadEmployeeAttributeTemplate = function () {
    url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=场地代码导入模板.xls&moduleName=operation&entityName=SchedulingBase&isTemplate=true";
    window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
}

// 导入功能校验没通过返回数据下载
var downloadImportEmployeeAttributeError = function (connectionObject) {
    window.location = connectionObject.getAttribute("url");
}

var btnImportEmployeeAttribute = new Ext.Button({
    text: '场地代码导入',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function () {
    	var node = treePanel.getSelectionModel ().getSelectedNode();			
		if(!(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)){
			Ext.Msg.alert("提示","只能选择区部以下的网点！");
			return;
		}
    	
        var departmentCode = Ext.getCmp('query.deptId').getValue();
        if (departmentCode == null || departmentCode == "") {
            Ext.Msg.alert('提示', '请选择网点代码！');
            return;
        }
        var win = new Ext.Window({
            width: 580,
            height: 200,
            modal: true,
            border: false,
            bodyBorder: false,
            closable: false,
            resizable: false,
            layout: 'fit',
            title: '导入',
            items: [
                {
                    xtype: 'form',
                    border: false,
                    fileUpload: true,
                    frame: true,
                    labelAlign: 'right',
                    items: [
                        {
                            border: false,
                            height: 20
                        },
                        {
                            border: false,
                            xtype: 'label',
                            style: 'margin-left : 60px;',
                            html: '<font color=red size=4>最多导入1000条数据</font>'
                        },
                        {
                            border: false,
                            height: 10
                        },
                        {
                            xtype: 'textfield',
                            inputType: 'file',
                            width: 290,
                            name: 'upload',
                            fieldLabel: '文件路径'
                        },
                        {
                            xtype: 'hidden',
                            name: 'importDeptId',
                            id: 'importDeptId'
                        },
                        {
                            border: false,
                            height: 10
                        },
                        {
                            border: false,
                            xtype: 'label',
                            style: 'margin-left : 40px;',
                            html: '<font size=3>模板文件： </font> <a href="#" onclick="downloadEmployeeAttributeTemplate()">导入文件模板下载</a>'
                        }
                    ],
                    fbar: [
                        {
                            text: '上传',
                            handler: function () {
                                var form = this.ownerCt.ownerCt;
                                var basicForm = form.getForm();
                                var fileName = basicForm.findField("upload").getValue();
                                var xls = fileName.substr(fileName.lastIndexOf(".")).toLowerCase();// 获得文件后缀名
                                basicForm.findField('importDeptId').setValue(departmentCode.split("/")[0]);
                                if (xls != '.xls') {
                                    Ext.Msg.alert("提示", "系统只支持xls类型文件上传，请下载模板！");
                                    return;
                                }
                                basicForm.submit({
                                    url: '../operation/importEmployeeAttribute.action',
                                    success: function (form, action) {
                                        if (!action.result.success) {
                                            Ext.Msg.alert("提示", action.msg);
                                            return;
                                        }
                                        var url = "../common/downloadReportFile.action?" + encodeURI(encodeURI(action.result.downLoadUrl));
                                        var aTag = "";
                                        if (action.result.downLoadUrl) {
                                            aTag = "<a href='#' url='" + url + "' onclick='downloadImportEmployeeAttributeError(this)'>错误数据下载</a>";
                                        }
                                        Ext.Msg.alert('提示', action.result.msg + aTag);
                                        win.close();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示', action.result.msg);
                                        win.close();
                                    },
                                    waitTitle: "请稍后",
                                    waitMsg: "正在执行操作..."
                                });
                            }
                        },
                        {
                            text: '取消',
                            handler: function () {
                                win.close();
                            }
                        }
                    ]
                }
            ]
        }).show();
    }
});

var SITE_CODE = 'query.siteCode';
var KEY_SITE_CODE = "SITE_CODE";
var KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID = "OPERATION_EMP_ATTRIBUTE_ID";

// 顶部的Panel
var topPanel = new Ext.Panel({
				frame : true,
				layout:'column',
				height : 160,
				tbar:['-'
				      <app:isPermission code="/operation/outEmployeeMgt_queryOutEmployee.action">,btnSearch</app:isPermission>
				      	,'-'
				      <app:isPermission code="/operation/outEmployeeMgt_saveEmployee.action">,btnAdd</app:isPermission>
				       , '-'
				      <app:isPermission code="/operation/outEmployeeMgt_updateEmployee.action">,btnEdit</app:isPermission>
				        ,'-'
				      <app:isPermission code="/operation/outEmployeeMgt_deleteEmployee.action">,btnDelete</app:isPermission>
				      	,'-'
				     <app:isPermission code="/operation/employeeUploadFile.action">,btnImport</app:isPermission>
				      	,'-'
				      <app:isPermission code="/operation/outEmployeeMgt_exportEmployee.action">,btnExport</app:isPermission>
				      ,'-'
				      <app:isPermission code="/operation/outEmployeeMgt_groupImportEmployee.action">,btnGroupImport</app:isPermission>
				  		,'-'
				      <app:isPermission code="/operation/outEmployeeMgt_batchModifyEmployee.action">,btnBatchModify</app:isPermission>
				  		,'-'
				      <app:isPermission code="/operation/importEmployeeAttribute.action">,btnImportEmployeeAttribute</app:isPermission>
				],
				items : [{
					xtype:'fieldset',
					title:'查询条件',
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
									id:'query.deptId',
									readOnly : true,
									fieldLabel:'<font color=red>网点代码*</font>',
									anchor : '90%'
								}]
					},{
						  columnWidth : .4,
						  labelWidth:120,
						  labelAlign:'right',
						  layout:'form',
						  items : [{
								xtype:'textfield',
								id:'query.empcode',
								fieldLabel:'工号',
								anchor : '90%'
							}]
				},{
					  columnWidth : .4,
					  labelWidth:120,
					  labelAlign:'right',
					  layout:'form',
					  items : [{
							xtype:'textfield',
							id:'query.empname',
							fieldLabel:'姓名',
							anchor : '90%'
						}]
			},{
				  columnWidth : .4,
				  labelWidth:120,
				  labelAlign:'right',
				  layout:'form',
				  items : [{
						xtype:'combo',
						id:'query.empStatus',
						typeAhead: true,
					    triggerAction: 'all',
					    lazyRender:true,
					    mode: 'local',
						fieldLabel:'在职状态',
						value : 1,
						store : [['1','在职'],['0','离职']],
						anchor : '90%'
					}]
			},{
                        columnWidth : .4,
                        labelWidth:120,
                        labelAlign:'right',
                        layout:'form',
                        items : [{
                            xtype:'textfield',
                            id: SITE_CODE,
                            fieldLabel:'场地代码',
                            anchor : '90%'
                        }]
                    },{
                        columnWidth : .4,
                        labelWidth:120,
                        labelAlign:'right',
                        layout:'form',
                        items : [{
         						xtype : 'combo',
        						fieldLabel : "小组名称",
        						hiddenName : "groupId",
        						mode : 'remote',
        						id : "query.groupName",
        						displayField : "groupName",
        						valueField : "groupName",
        						typeAhead : true,
        						selectOnFocus : true,
        						triggerAction : "all",
        						resizable : true,
        						forceSelection : true,
        						editable : false,
        						value : null,
        						emptyText : '请选择',
        						anchor : '90%',
        						listeners : {
        							beforequery : function(qe) {
        								var deptCode = Ext.getCmp('query.deptId').getValue();
        								qe.combo.getStore().baseParams['deptId'] = (deptCode == '001' ? null : treePanel.getSelectionModel ().getSelectedNode().id);
        								delete qe.combo.lastQuery;
        								return deptCode;
        							}
        						},
        						store : new Ext.data.JsonStore({
        							pruneModifiedRecords : true,
        							url : "../operation/queryGroup.action",
        							fields : [ {
        								name : 'groupName',
        								mapping : 'GROUP_NAME'
        							}, {
        								name : 'groupId',
        								mapping : 'GROUP_ID'
        							} ],
        							listeners : {
        								load : function(s) {
        									if (!s.getCount() || s.getAt(0).data.groupName != '--')
        										s.insert(s.getCount(), new s.recordType({
        											groupName : '未分组',
        											groupId : null
        										}));
        								}
        							}
        						})
        					}]
                    }]
				}]
	});
// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});
// 列头构建
var cm =  new Ext.grid.ColumnModel({
	 columns:  [
	 sm,
	 {
	 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
	 },{
	 			header: '网点代码', sortable: true, dataIndex: 'DEPT_CODE'
	 },{
	 			header: '工号', sortable: true, dataIndex: 'EMP_CODE',
	 			renderer : function(value, metaData, record){
	 				if(!record.data['TMONTHID'] && record.data["EMP_STATUS"]==1 ){
	 					return "<label style = 'color:red'>" + value+ "</label>"
	 				}
	 				return value;
	 			}
	 },{
	 			header: '姓名',  sortable: true, dataIndex: 'EMP_NAME'
	 },{
	 			header: '职位名称',  sortable: true, dataIndex: 'EMP_DUTY_NAME',
	 			renderer : function(value, metaData, record){
	 				if(Ext.isEmpty(record.data['EMP_DUTY_NAME'])){
	 					return "运作员";
	 				}
	 				return value;
	 			}
	 },{
	 			header: '用工类型',  sortable: true, dataIndex: 'WORK_TYPE_STR'
	 },{
	 			header: '小组名称',  sortable: true, dataIndex: 'GROUP_NAME'
	 },{
         		header: 'GROUP_ID', hidden:true
     },{
	 			header: '小组代码',  sortable: true, dataIndex: 'GROUP_CODE',
	 			renderer : function(value, metaData, record){
	 				if('GREEN'==record.data['COLUMN_COLOR']){
	 					metaData.attr = 'style="background-color:green;color:white;"';
	 				}
	 				if('YELLOW'==record.data['COLUMN_COLOR']){
	 					metaData.attr = 'style="background-color:yellow;"';
	 				}
	 				return value;
	 			}
	 },
	 {
	 			header: '在职状态',  sortable: true, dataIndex: 'EMP_STATUS',
	 			renderer : function(val){
	 					if(val==1)
	 						return "在职";
	 					else if(val==0)
	 						return "离职";
	 					else
	 						return val;
	 			}
	 },{
             header: '场地代码',  sortable: true, dataIndex: KEY_SITE_CODE
     },{
             header: '场地ID', hidden : true, sortable: true, dataIndex: KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID
     },{
             header: '员工子组文本', hidden : true, sortable: true, dataIndex: 'PERSK_TXT'
     }]
});

// 数据格构建
var record = Ext.data.Record.create([{
				name : 'AREA_CODE' ,
				mapping : 'AREA_CODE',
				type : 'string'
		},{
				name : 'DEPT_CODE'  ,
				mapping : 'DEPT_CODE',
				type : 'string'
		},{
				name : 'EMP_CODE' ,
				mapping : 'EMP_CODE',
				type : 'string'
		},{
				name : 'EMP_NAME' ,
				mapping : 'EMP_NAME',
				type : 'string'
		},{
				name : 'EMP_DUTY_NAME' ,
				mapping : 'EMP_DUTY_NAME',
				type : 'string'
		},{
				name : 'EMP_STATUS' ,
				mapping : 'EMP_STATUS',
				type : 'string'
		},{
				name : 'WORK_TYPE_STR' ,
				mapping : 'WORK_TYPE_STR',
				type : 'string'
		},{
				name : 'GROUP_NAME' ,
				mapping : 'GROUP_NAME',
				type : 'string'
		},{
				name : 'GROUP_CODE' ,
				mapping : 'GROUP_CODE',
				type : 'string'
		},{
				name : 'GROUP_ID' ,
				mapping : 'GROUP_ID',
				type : 'string'
		},{
				name : 'COLUMN_COLOR' ,
				mapping : 'COLUMN_COLOR',
				type : 'string'
		},{
				name : 'CHANGE_GROUP_NAME' ,
				mapping : 'CHANGE_GROUP_NAME',
				type : 'string'
		},{
				name : 'ENABLE_TM' ,
				mapping : 'ENABLE_TM',
				type : 'string'
		},{
	            name : KEY_SITE_CODE ,
	            mapping : KEY_SITE_CODE,
	            type : 'string'
        },{
	            name : KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID ,
	            mapping : KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID,
	            type : 'string'
        },{
				name : 'TMONTHID' ,
				mapping : 'TMONTHID',
				type : 'string'
		},{
			name : 'PERSK_TXT' ,
			mapping : 'PERSK_TXT',
			type : 'string'
		}]);

// 构建数据存储Store
var store = new Ext.data.Store({
		proxy : new  Ext.data.HttpProxy({
			 url : '../operation/outEmployeeMgt_queryOutEmployee.action'		        		
		}),
		reader: new  Ext.data.JsonReader({
			root: 'root',
			totalProperty: 'totalSize'	    		
		},record)
});

// 分页组件
var pageBar =   new Ext.PagingToolbar({
		    store: store,      
		    displayInfo: true,
		    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
		    pageSize: 20,
		   emptyMsg : '未检索到数据'
})
// 表格构建
var grid = new Ext.grid.GridPanel({
		cm : cm,
		sm : sm,
		store : store,	    		
		autoScroll : true,
		loadMask : true,
		tbar : pageBar,
		 viewConfig: {
		        forceFit: true
		 }      
});
grid.on('dblclick',function(){
	var record = grid.getSelectionModel().getSelected();
	updateEmployee(record);
});
// 中部的Panel
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


// 查询方法
var queryEmployee = function(node){
	if (deptId == "") {
		Ext.Msg.alert("提示","请选择网点代码！");
		return;
	} else {
            store.setBaseParam("deptId",deptId);
			store.setBaseParam("empcode",Ext.getCmp('query.empcode').getValue());
			store.setBaseParam("empname",Ext.getCmp('query.empname').getValue());
			store.setBaseParam("groupName",Ext.getCmp('query.groupName').getValue());
			store.setBaseParam("empStatus", Ext.getCmp('query.empStatus').getValue());
            store.setBaseParam(KEY_SITE_CODE, Ext.getCmp(SITE_CODE).getValue());
			store.load({
					params : {
							start: 0,          
					        limit: 20
					}		
			});	
	}
}

// 新增方法
var addEmployee = function(){
	var node = treePanel.getSelectionModel ().getSelectedNode();
	// 工号
	var empCode = "";
	// 网点代码
	var deptCode = "";
    if (node != null && (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)) {
        deptId = node.id;
        deptCode = node.attributes.code;
    } else {
        Ext.Msg.alert("提示", "只有中转场和航空操作组才能新增外包人员信息！");
        return;
    }
	Ext.Ajax.request({
				url : '../operation/outEmployeeMgt_queryInsertEmpCode.action',
				method : 'POST',
				success : function(res,config){
						var data = Ext.decode(res.responseText);
						empCode = data.root[0]["NEXTVAL"];						
						win.show();
						if(Ext.getCmp("empCodeId")){
								Ext.getCmp("empCodeId").setValue(empCode);
						}
				},
				failure : function(res,config){
						Ext.Msg.alert("提示","工号获取失败！");
				}
	});
	var groupStore = new Ext.data.Store({     
				        proxy:new Ext.data.HttpProxy({     
				            url:'../operation/queryGroup.action'    
				        }),     
				        reader:new Ext.data.JsonReader({
				            	fields:['GROUP_ID','GROUP_NAME']     
				        }     
				    )
		});
	groupStore.setBaseParam("deptId",deptId);
	
	var win = new Ext.Window({
		width:780,
		height:230,
		modal:true,
		border:false,
		bodyBorder:false,
		closable:false,
		resizable:false,
		layout:'fit',
		title : '外包人员新增',
		items : [{
			xtype : 'form',
			labelAlign : 'right',
			layout : 'column',
			buttonAlign : 'center',								
			items : [{
					border : false,
					height : 10,
					columnWidth : 1
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',		
					    readOnly : true,
					    name : 'deptCode',
					    value :  deptCode,
					    allowBlank : false,
					    fieldLabel : '<font color=red>网点代码*</font>'
				  }]
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',						    
					    readOnly : true,					    
					    name : 'empCode',		
					    id : 'empCodeId',
					    allowBlank : false,
					    maxLength : 20,
					    fieldLabel : '<font color=red>工号*</font>'
				  }]
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',						    
					    allowBlank : false,				    
					    name : 'empName',		
					    maxLength : 20,
					    fieldLabel : '<font color=red>姓名*</font>'
				  }]
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'combo',
					    anchor : '95%',
					    name : 'groupName',
					    hiddenName : 'groupId',
					    typeAhead: false,
					    editable:false,
					    triggerAction: 'all',
					    lazyRender:true,
					    displayField : 'GROUP_NAME',
					    valueField  : 'GROUP_ID',					   
					    store : groupStore,
					    allowBlank : false,
					    fieldLabel : '<font color=red>小组名称*</font>'
				  }]
			},
			{
			  columnWidth : .45,
			  border : false,
			  layout : 'form',
			  items : [{				
					xtype : 'datefield',
				    anchor : '95%',
				    format : 'Y-m-d',
				    name : 'dimissionDt',
				    fieldLabel : '离职时间'
				   }]
			},
			{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',		    
					    name : 'empDutyName',		
					    maxLength : 20,
					    fieldLabel : '职位名称'
				  }]
			}],
			fbar : [{
				text : '保存',
				handler : function(){
						var form = this.ownerCt.ownerCt;
						if(form.getForm().isValid ()){
								form.getForm().submit({
									url: '../operation/outEmployeeMgt_saveEmployee.action',
									success: function(form, action) {
										Ext.Msg.alert('提示', action.result.msg);
										win.close();
										queryEmployee();
									},
									failure : function(form, action) {
										Ext.Msg.alert('提示', action.result.msg);
										// win.close();
									} ,
									waitTitle:"请稍后",
									waitMsg:"正在执行操作...",
									timeout : 300  // 5分钟
								});										
						}
				}
			},{
				text : '取消',
				handler : function(){
						win.close();
				}
			}]
		}]
	});	
}

// 修改方法
var updateEmployee = function(record){
	if(record==null){
		Ext.Msg.alert("提示","请选择要修改的数据！");		
		return;
	}
	var deptId =  record.json["DEPT_ID"];
	var groupStore = new Ext.data.Store({     
		        proxy:new Ext.data.HttpProxy({     
		            url:'../operation/queryEmployeeGroup.action'    
		        }),     
		        reader:new Ext.data.JsonReader({
		            	fields:['GROUP_ID','GROUP_NAME']     
		        }     
		    )
		});
		groupStore.setBaseParam("deptId",deptId);
		// 是否外包人员
		var isOutEmployee = record.json["EMP_CODE"]	.length>=9?true:false;
		var win = new Ext.Window({
					width:780,
					height:230,
					modal:true,
					border:false,
					bodyBorder:false,
					closable:false,
					resizable:false,
					layout:'fit',
					title : '人员信息修改',
					items : [{
						xtype : 'form',
						labelAlign : 'right',
						layout : 'column',
						buttonAlign : 'center',								
						items : [{
								border : false,
								height : 10,
								columnWidth : 1
						},{
							xtype : 'hidden',
							name : 'isOutEmployee',
							value : isOutEmployee
						},{
							xtype : 'hidden',
							name : 'empId',
							value : record.json["EMP_ID"]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',		
								    disabled  : true,
								    name : 'deptCode',								    
								    allowBlank : false,
								    value : record.json["DEPT_CODE"],
								    fieldLabel : '<font color=red>网点代码*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',						    
								    disabled : true,					    
								    name : 'empCode',		
								    id : 'empCodeId',
								    allowBlank  : false,
								    value : record.json["EMP_CODE"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>工号*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',						    
								    allowBlank : false,				    
								    name : 'empName',
								    disabled  : true,
								    value : record.json["EMP_NAME"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>姓名*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',						    
								    allowBlank : false,				    
								    name : 'CHANGE_GROUP_NAME',
								    disabled  : true,
								    value : record.json["CHANGE_GROUP_NAME"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>最近小组变更名称*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'combo',
								    anchor : '95%',
								    name : 'groupName',
								    hiddenName : 'groupId',
								    typeAhead: false,
								    editable:false,
								    triggerAction: 'all',
								    lazyRender:true,								
								    displayField : 'GROUP_NAME',
								    valueField  : 'GROUP_ID',
								    store : groupStore,
								    allowBlank : false,
								    fieldLabel : '<font color=red>小组名称*</font>',
								    maxLength : 20,
								    listeners : {
								    		render : function(combo){
								    					var s = combo.store;
								    					s.on("load",function(Store,records,options){
								    							combo.setValue(record.json["GROUP_ID"]);
								    							s.purgeListeners();
								    					});
								    					s.load();								    					
								    		},
								    		select: function( combo, record, index ){
								    			combo.removeClass("red_t");
								    			if(record.data["GROUP_NAME"].indexOf("删除小组") != -1){
								    				combo.setValue("删除小组");
								    				combo.addClass("red_t");
								    			}
								    			
								    		}
								    }
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',						    
								    allowBlank : false,				    
								    name : 'ENABLE_TM',
								    disabled  : true,
								    value : record.json["ENABLE_TM"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>变更生效时间*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{				
									xtype : 'datefield',
								    anchor : '95%',
								    format : 'Y-m-d',
								    name : 'dimissionDt',
								    disabled  : !isOutEmployee,
								    value : record.json["DIMISSION_DT"]==null?"":record.json["DIMISSION_DT"].substr(0,10),
								    fieldLabel : '离职时间'
								   }]
							},
						{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',		    
								    name : 'empDutyName',		
								    disabled  : !isOutEmployee,
								    value : record.json["EMP_DUTY_NAME"],
								    maxLength : 20,
								    fieldLabel : '职位名称'
							  }]
						},{
                                columnWidth: .45,
                                border: false,
                                layout: 'form',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        anchor: '95%',
                                        name: KEY_SITE_CODE,
                                        value: record.json[KEY_SITE_CODE],
                                        maxLength: 20,
                                        fieldLabel: '场地代码'
                                    }
                                ]
                            },{
                                columnWidth: .45,
                                border: false,
                                hidden: true,
                                layout: 'form',
                                items: [
                                    {
                                        xtype: 'textfield',
                                        anchor: '95%',
                                        name: KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID,
                                        value: record.json[KEY_OPERATION_EMPLOYEE_ATTRIBUTE_ID],
                                        maxLength: 20,
                                        fieldLabel: '场地ID'
                                }]
                            }],
						fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if(form.getForm().isValid ()){
											form.getForm().submit({
												url: '../operation/outEmployeeMgt_updateEmployee.action',
												success: function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													win.close();
													queryEmployee();
												},
												failure : function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													// win.close();
												} ,
												waitTitle:"请稍后",
												waitMsg:"正在执行操作...",
												timeout : 300  // 5分钟
											});										
									}
							}
						},{
							text : '取消',
							handler : function(){
									win.close();
							}
						}]
					}]
		 });
		win.show();
}

// 批量修改小组方法
var updateBtnBatchModifyEmployee = function(record,employees){
	var deptId =  record.json["DEPT_ID"];
	var groupStores = new Ext.data.Store({     
		        proxy:new Ext.data.HttpProxy({     
		            url:'../operation/queryEmployeeGroup.action'    
		        }),     
		        reader:new Ext.data.JsonReader({
		            	fields:['GROUP_ID','GROUP_NAME']     
		        }     
		    )
		});
	var today = new Date();
	today.setDate(today.getDate() - 7);
	groupStores.setBaseParam("deptId",deptId);
	var wins = new Ext.Window({
		width:300,
		height:200,
		modal:true,
		border:false,
		bodyBorder:false,
		closable:false,
		resizable:false,
		layout:'fit',
		title : '批量修分组',
		items : [{
					xtype : 'form',
					labelAlign : 'right',
					layout : 'column',
					buttonAlign : 'center',
					items : [{
						border : false,
						height : 10,
						columnWidth : 1
					},{
						  columnWidth : 1,
						  border : false,
						  layout : 'form',
							items : [{
								xtype : 'combo',
								anchor : '95%',
								name : 'groupName',
								hiddenName : 'groupId',
								typeAhead: false,
								editable:false,
								triggerAction: 'all',
								lazyRender:true,								
								displayField : 'GROUP_NAME',
								valueField  : 'GROUP_ID',
								store : groupStores,
								allowBlank : false,
								fieldLabel : '<font color=red>小组名称*</font>',
								maxLength : 20,
								listeners : {
										render : function(combo){
												var s = combo.store;
												s.on("load",function(Store,records,options){
													combo.setValue(record.json["GROUP_ID"]);
												    s.purgeListeners();
												});
												s.load();								    					
										},
							    		select: function( combo, record, index ){
							    			combo.removeClass("red_t");
							    			if(record.data["GROUP_NAME"].indexOf("删除小组") != -1){
							    				combo.setValue("删除小组");
							    				combo.addClass("red_t");
							    			}
							    		}
								}
							 },{				
									xtype : 'datefield',
								    anchor : '95%',
								    format : 'Y-m-d',
								    editable:false,
								    allowBlank : false,
								    minValue:today,
								    name : 'enableDate',
								    fieldLabel : '<font color=red>生效时间*</font>'
								   }]
						}],fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if(form.getForm().isValid ()){
											form.getForm().submit({
												url: '../operation/outEmployeeMgt_updateBtnBatchModify.action',
												params : {
													employees : Ext.util.JSON.encode(employees),
													department_id: deptId
												},
												success: function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													wins.close();
													queryEmployee();
												},
												failure : function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													// win.close();
												} ,
												waitTitle:"请稍后",
												waitMsg:"正在执行操作...",
												timeout : 300  // 5分钟
											});										
									}
							}
						},{
							text : '取消',
							handler : function(){
								wins.close();
							}
						}]
		}]
	});
	wins.show();	
}
	
// 删除方法
var deleteEmployee = function(ids, codes){
			if(ids){
				Ext.Msg.confirm ("提示","是否确定删除该记录？",function(btn){
					if(btn=="yes"){
							Ext.Ajax.request({
										url : '../operation/outEmployeeMgt_deleteEmployee.action',
										method : 'POST',
										params : {
											empIds : ids,
                                            empCodes : codes
										},
										success : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
												queryEmployee();
										},
										failure : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
										}								
							});
					}
				}, this)
			}else{
				 Ext.Msg.alert("提示","请选择要删除的数据！");				
				 return;
			}	
}

// 导出方法
var exportEmployee=function(){
			var node = treePanel.getSelectionModel ().getSelectedNode();
			var deptId = "";
			if(node!=null && node.id!=0){
				deptId = node.id;
			}else{
				Ext.Msg.alert("提示","请先选择网点！");
				return;
			}
			var myMask = new Ext.LoadMask(centerPanel.getEl(), {msg:"正在导出..."});
			myMask.show();
			Ext.Ajax.request({
						url : "../operation/outEmployeeMgt_exportEmployee.action",
						method : 'POST',
						params : {
							deptId : deptId,
							empcode : Ext.getCmp('query.empcode').getValue(),
							empname : Ext.getCmp('query.empname').getValue(),
							empStatus : Ext.getCmp('query.empStatus').getValue()
						},
						success : function(res,config){
								myMask.hide();
								var obj = Ext.decode(res.responseText);
								if(!obj.success){
									Ext.Msg.alert("提示",obj.msg);
									return;
								}
								var url = obj.fileName;								
								window.location= '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
						},
						failure : function(res,config){
								var obj = Ext.decode(res.responseText);
								Ext.Msg.alert("提示",obj.msg);
								myMask.hide();
						}					
			});
}

// 模板下载
var downTemp = function(){
		url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=外包人员导入模板.xls&moduleName=operation&entityName=SchedulingBase&isTemplate=true";
		window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		// window.location = "../pages/schedulingBase/template/外包人员导入模板.xls";
}

var groupdownTemp = function(){
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=人员分组导入模板.xls&moduleName=operation&entityName=SchedulingBase&isTemplate=true";
	window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
	// window.location = "../pages/schedulingBase/template/外包人员导入模板.xls";
}
// 导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}

// 导入方法
var importEmployee = function(){
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
										 fieldLabel : '文件路径'
								},{
									xtype:'hidden',
									name:'importDeptId',
									id:'importDeptId'
								},{
									border : false,
									height : 10
							},{
									border : false,
									xtype : 'label',
									style : 'margin-left : 40px;',
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">导入文件模板下载</a>'
							}],
							fbar : [{
									text : '上传',
									handler :function(){											
											var form = this.ownerCt.ownerCt;	
											var basicForm = form.getForm();									
											var fileName = basicForm.findField("upload").getValue();
											var xls=fileName.substr(fileName.lastIndexOf(".")).toLowerCase();// 获得文件后缀名
											var importDeptId = Ext.getCmp('query.deptId').getValue();
											basicForm.findField('importDeptId').setValue(importDeptId);
											if(xls!='.xls'){
												Ext.Msg.alert("提示","系统只支持xls类型文件上传，请下载模板！");
												return;
											}											
											basicForm.submit({
												url: '../operation/employeeUploadFile.action',												
												success: function(form, action) {
														if(!action.result.success){
															Ext.Msg.alert("提示",action.result.msg);
															return;
														}
														var url = "../common/downloadReportFile.action?"+ encodeURI(encodeURI(action.result.downLoadUrl));		
														var aTag= "";
														if(action.result.downLoadUrl){
																aTag="<a href='#' url='"+url+"'     onclick='downError(this)'>错误数据下载</a>";
														}
														Ext.Msg.alert('提示', action.result.msg+aTag);													
														win.close();		
												},
												failure : function(form, action) {
													Ext.Msg.alert('提示', action.result.msg);
													win.close();
												} ,
												waitTitle:"请稍后",
												waitMsg:"正在执行操作...",
												timeout : 300  // 5分钟
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

var groupImportEmployee = function(){
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
								 fieldLabel : '文件路径'
						},{
							xtype:'hidden',
							name:'groupimportDeptId',
							id:'groupimportDeptId'
						},{
							border : false,
							height : 10
					},{
							border : false,
							xtype : 'label',
							style : 'margin-left : 40px;',
							html : '<font size=3>模板文件： </font> <a href="#" onclick="groupdownTemp()">导入文件模板下载</a>'
					}],
					fbar : [{
							text : '上传',
							handler :function(){											
									var form = this.ownerCt.ownerCt;	
									var basicForm = form.getForm();									
									var fileName = basicForm.findField("upload").getValue();
									var xls=fileName.substr(fileName.lastIndexOf(".")).toLowerCase();// 获得文件后缀名
									var importDeptId = Ext.getCmp('query.deptId').getValue();
									basicForm.findField('groupimportDeptId').setValue(importDeptId);
									if(xls!='.xls'){
										Ext.Msg.alert("提示","系统只支持xls类型文件上传，请下载模板！");
										return;
									}											
									basicForm.submit({
										url: '../operation/groupEmployeeUploadFile.action',												
										success: function(form, action) {
												if(!action.result.success){
													Ext.Msg.alert("提示",action.result.msg);
													return;
												}
												var url = "../common/downloadReportFile.action?"+ encodeURI(encodeURI(action.result.downLoadUrl));		
												var aTag= "";
												if(action.result.downLoadUrl){
														aTag="<a href='#' url='"+url+"' onclick='downError(this)'>错误数据下载</a>";
												}
												Ext.Msg.alert('提示', action.result.msg+aTag);													
												win.close();		
										},
										failure : function(form, action) {
											Ext.Msg.alert('提示', action.result.msg);
											win.close();
										} ,
										waitTitle:"请稍后",
										waitMsg:"正在执行操作...",
										timeout : 300  // 5分钟
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

// 初始化
Ext.onReady(function() {
		Ext.QuickTips.init();	
		Ext.Ajax.timeout=300000;
		Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
		var viewreprot = new Ext.Viewport({
			layout : "border",
			items : [ treePanel,centerPanel]
		});
	});	