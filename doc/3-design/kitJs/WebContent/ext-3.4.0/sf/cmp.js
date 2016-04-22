/**
 * Oscar.Xie Ext的扩展UI组件
 */

Ext.ns('SF.cmp');

/**
 * 分类显示树
 */
SF.cmp.CategoryTree = Ext.extend(Ext.tree.TreePanel, {
    title : "${app:i18n_def('cmp.js.1','分类显示')}",
    rootVisible:false,
    initComponent:function(){
    	var mkCfg=arCfg='1234567';
    	var mkCks=arCks='';
    	if (this.markerCfg) {
    		if (Ext.isArray(this.markerCfg)) {
    			mkCfg = this.markerCfg.join('');
    		} else {
    			throw "markerCfg is not Array";
    		}
    	}
    	if (this.markerChecked) {
    		if (Ext.isArray(this.markerChecked)) {
    			mkCks = this.markerChecked.join('');
    		} else {
    			throw "markerChecked is not Array";
    		}
    	}
    	if (this.areaCfg) {
    		if (Ext.isArray(this.areaCfg)) {
    			arCfg = this.areaCfg.join('');
    		} else {
    			throw "areaCfg is not Array";
    		}
    	}
    	if (this.areaChecked) {
    		if (Ext.isArray(this.areaChecked)) {
    			arCks = this.areaChecked.join('');
    		} else {
    			throw "areaChecked is not Array";
    		}
    	}
    	Ext.apply(this, {
    		root: {
    	    	expanded: true,
    	        children: [{
    	            text: '${app:i18n_def('cmp.js.2','标记')}',
    	            expanded: true,
    	            checked:mkCks.length != 0,
    	            hidden: mkCfg.length == 0,
    	            children:[{
    	            	text:'${app:i18n_def('cmp.js.3','总部')}',
    	            	checked:mkCks.indexOf('1') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('1') == -1,
    	            	nodeValue:'1'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.4','经营本部')}',
    	            	checked:mkCks.indexOf('2') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('2') == -1,
    	            	nodeValue:'2'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.5','区部')}',
    	            	checked:mkCks.indexOf('3') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('3') == -1,
    	            	nodeValue:'3'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.6','分部')}',
    	            	checked:mkCks.indexOf('4') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('4') == -1,
    	            	nodeValue:'4'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.7','点部')}',
    	            	checked:mkCks.indexOf('5') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('5') == -1,
    	            	nodeValue:'5'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.8','组合区域')}',
    	            	checked:mkCks.indexOf('6') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('6') == -1,
    	            	nodeValue:'6'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.9','单元区域')}',
    	            	checked:mkCks.indexOf('7') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('7') == -1,
    	            	nodeValue:'7'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.18','建筑物')}',
    	            	checked:mkCks.indexOf('8') != -1,
    	            	leaf:true,
    	            	hidden: mkCfg.indexOf('8') == -1,
    	            	nodeValue:'8'
    	            }]
    	        }, {
    	            text: '${app:i18n_def('cmp.js.10','区域')}',
    	            expanded: true,
    	            checked:arCks.length != 0,
    	            hidden: arCfg.length == 0,
    	            children:[{
    	            	text:'${app:i18n_def('cmp.js.3','总部')}',
    	            	checked:arCks.indexOf('1') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('1') == -1,
    	            	nodeValue:'1'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.4','经营本部')}',
    	            	checked:arCks.indexOf('2') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('2') == -1,
    	            	nodeValue:'2'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.5','区部')}',
    	            	checked:arCks.indexOf('3') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('3') == -1,
    	            	nodeValue:'3'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.6','分部')}',
    	            	checked:arCks.indexOf('4') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('4') == -1,
    	            	nodeValue:'4'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.7','点部')}',
    	            	checked:arCks.indexOf('5') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('5') == -1,
    	            	nodeValue:'5'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.8','组合区域')}',
    	            	checked:arCks.indexOf('6') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('6') == -1,
    	            	nodeValue:'6'
    	            },{
    	            	text:'${app:i18n_def('cmp.js.9','单元区域')}',
    	            	checked:arCks.indexOf('7') != -1,
    	            	leaf:true,
    	            	hidden: arCfg.indexOf('7') == -1,
    	            	nodeValue:'7'
    	            }]
    	        }]
    	    }
    	});
    	
    	SF.cmp.CategoryTree.superclass.initComponent.apply(this, arguments);
    },
    onNodeCheckChange:function(node, checked){
		var pt = node.parentNode;
		var i=0;
		for(; i<pt.childNodes.length; i++) {
			if (pt.childNodes[i].attributes.checked) {
				break;
			}
		}
		if (checked || i == pt.childNodes.length) {
			pt.getUI().checkbox.checked = checked;
			pt.getUI().checkbox.defaultChecked = checked;
	        pt.attributes.checked = checked;
		}
    },
    onRootCheckChange:function(node, checked){
		node.eachChild(function(sNode){
			sNode.getUI().checkbox.checked = checked;
			sNode.getUI().checkbox.defaultChecked = checked;
			sNode.attributes.checked = checked;
		});
    },
    getMarkerSpecialLevels:function(){
    	var cs = this.getRootNode().childNodes[0];
    	var mr = [];
    	for(var i=0; i < cs.childNodes.length; i++) {
    		if (!cs.childNodes[i].hidden && cs.childNodes[i].attributes.checked) {
    			mr.push(cs.childNodes[i].attributes.nodeValue);
    		}
    	}
    	return mr.toString();
    },
    getAreaSpecialLevels:function(){
    	var cs = this.getRootNode().childNodes[1];
    	var ar = [];
    	for(var i=0; i < cs.childNodes.length; i++) {
    		if (!cs.childNodes[i].hidden && cs.childNodes[i].attributes.checked) {
    			ar.push(cs.childNodes[i].attributes.nodeValue);
    		}
    	}
    	return ar.toString();
    },
    afterRender:function(){
    	SF.cmp.CategoryTree.superclass.afterRender.apply(this, arguments);
    	this.getRootNode().eachChild(function(node){
    		node.on('checkchange', this.onRootCheckChange);
    		node.eachChild(function(sNode){
    			sNode.on('checkchange', this.onNodeCheckChange);
    		}, this);
    	}, this);
    }
});
Ext.reg('categoryTree', SF.cmp.CategoryTree);

/**
 * 组合区域/单元区域机构树
 */
SF.cmp.SubOrganizationTree = Ext.extend(Ext.tree.TreePanel, {
	initComponent:function() {
		SF.cmp.SubOrganizationTree.superclass.initComponent.call(this);
		
		this.addEvents('deptClick');
		this.on('click', function(node, e){
			if (node == this.getRootNode()) {
				return ;
			} else {
				this.fireEvent('deptClick', node.text.split('/')[0], node, e);
			}
		}, this);
	},
	autoScroll: true,
    animate: true,
    enableDD: true,
    containerScroll: true,
    dataUrl : "../statistic/searchAreasTree.action",
    root : {
    	nodeType: 'async',
        text : "${app:i18n_def('cmp.js.11','区域选择')}"
    },
    setApplyScope:function(scope){
    	this.getLoader().baseParams.applyscope = scope;
   		this.loadData();
    },
    loadData:function(deptCode) {
    	if (deptCode) {
    		this.getLoader().baseParams.deptCode = deptCode ;
    	}
    	if (this.getLoader().baseParams.applyscope && this.getLoader().baseParams.deptCode) {
    		this.getLoader().load(this.getRootNode(), this.loadSubTreeRootComplete);
    	}
    },
    loadSubTreeRootComplete:function(root) {
    	root.getOwnerTree().expandAll();
    }
});

Ext.reg('subOrgTree', SF.cmp.SubOrganizationTree);

/**
 * 组合机构树
 */
SF.cmp.OrganizationTree = Ext.extend(Ext.tree.TreePanel, {
	initComponent:function() {
		SF.cmp.OrganizationTree.superclass.initComponent.call(this);
		
		this.addEvents('deptClick');
		this.on('click', function(node, e){
			if (node == this.getRootNode()) {
				return ;
			} else {
				this.fireEvent('deptClick', node.text.split('/')[0], node, e);
			}
		}, this);
	},
    autoScroll: true,
    animate: true,
    enableDD: true,
    containerScroll: true,
	dataUrl : "../tcmscommon/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
    root : {
    	nodeType: 'async',
        text : "${app:i18n_def('cmp.js.12','顺丰速运')}",
        id : "0"
    }
});
Ext.reg('orgTree', SF.cmp.OrganizationTree);

/**
 * 组合机构面板
 */
SF.cmp.OrganizationPanel = Ext.extend(Ext.Panel, {
    initComponent:function(){
    	this.orgTree = new SF.cmp.OrganizationTree({flex:1});
    	this.subOrgTree = new SF.cmp.SubOrganizationTree({flex:1});
    	
    	Ext.apply(this, {
    		title:"${app:i18n_def('cmp.js.13','组织机构定位')}",
    		layout:{
    			type:'vbox',
    			align:'stretch'
            },
    		items:[this.orgTree, this.subOrgTree]
    	});
    	
    	SF.cmp.OrganizationPanel.superclass.initComponent.apply(this, arguments);
    },
    afterRender:function(){
    	SF.cmp.OrganizationPanel.superclass.afterRender.apply(this, arguments);
    	this.orgTree.on('click', this.onOrgTreeClick, this);
    },
    onOrgTreeClick:function(node){
    	if (node != this.orgTree.getRootNode()) {
    		this.subOrgTree.loadData(node.text.split('/')[0]);
    	}
    },
    getOrgTree:function(){
    	return this.orgTree;
    },
    getSubOrgTree:function(){
    	return this.subOrgTree;
    }
});
Ext.reg('orgPanel', SF.cmp.OrganizationPanel);

/**
 * 经纬度定位面板
 */
SF.cmp.CoordinateLocatePanel = Ext.extend(Ext.Panel, {
	initComponent:function(){
		Ext.apply(this, {
			title : "${app:i18n_def('cmp.js.14','经纬度定位')}",
			layout : 'column',
			bodyStyle : 'padding-left:5px;padding-top:5px;',
			items : [{
					xtype:'panel',
					layout:'form',
					labelWidth:40,
					labelAlign:'right',
					border:false,
					width:180,
					items:[{
						allowBlank:false,
						decimalPrecision:15,
						fieldLabel:"${app:i18n_def('cmp.js.15','经度')}",
						xtype:'numberfield'
					},{
						allowBlank:false,
						decimalPrecision:15,
						fieldLabel:"${app:i18n_def('cmp.js.16','纬度')}",
						xtype:'numberfield'
					}]
				},{
				xtype:'panel',
				border:false,
				bodyStyle : 'padding-top:26px;',
				items:[{
					xtype:'button',
					text:"${app:i18n_def('cmp.js.17','座标定位')}",
					scope:this,
					handler:this.onBtnClick
			    }]
			}]
		});
		
		SF.cmp.CoordinateLocatePanel.superclass.initComponent.apply(this, arguments);
		
		this.addEvents('locateBtnClick');
	},
	onBtnClick:function(){
		var txts = this.findByType('numberfield');
		var lng = txts[0]; 
		var lat = txts[1];
		if (lng.isValid() && lat.isValid()) {
			this.fireEvent('locateBtnClick', lng.getValue(), lat.getValue());
    	}
	}
});
Ext.reg('coordinateLocatePanel', SF.cmp.CoordinateLocatePanel);

/**
 * 行政区域类型
 */
SF.cmp.Region = {
	LEVEL1:1, //省
	LEVEL2:2, //市
	LEVEL3:3  //县/区
};

/**
 * 行政区域地址
 */
SF.cmp.RegionAddress = function(province, city, area){
	this.country = "${app:i18n_def('CN','中国')}";
	this.province = province;
	this.city = city;
	this.area = area;
};

SF.cmp.RegionAddress.prototype = {
	setProvince:function(province) {
		this.province = province;
		this.city = '';
		this.area = '';
	},
	setCity:function(city) {
		this.city = city;
		this.area = '';
	},
	setArea:function(area) {
		this.area = area;
	},
	getCountry:function() {
		return this.country;
	},
	toString:function() {
		return this.province + this.city + this.area;
	}
};

/**
 * 行政区域数据加载器
 */
SF.cmp.RegionDataLoader = function(pUrl, cUrl, aUrl) {
	if (!pUrl) {
		throw "provinceURL is required!";
	}
	if (!cUrl) {
		throw "cityURL is required!";
	}
	if (!aUrl) {
		throw "areaURL is required!";
	}
	this.provinceURL = pUrl;
	this.cityURL = cUrl;
	this.areaURL = aUrl;
	
	this.addEvents({
		'loadProvinceSuccess':true,
		'loadCitySuccess':true,
		'loadAreaSuccess':true,
		'loadCountySuccess':true,
		'notDataFound':true
	});
};

Ext.extend(SF.cmp.RegionDataLoader, Ext.util.Observable, {
	loadProvinces:function() {
		Ext.Ajax.request({
			url:this.provinceURL,
			success: this.onLoadProvincesSuccess,
			failure: SF.cmp.AjaxRequestFailure,
			scope:this
		});
	},
	onLoadProvincesSuccess:function(response) {
		var result = Ext.util.JSON.decode(response.responseText);
		if (result.provinces != null && result.provinces.length > 0) {
			this.fireEvent('loadProvinceSuccess', result.provinces);
		} else {
			this.fireEvent('notDataFound', SF.cmp.Region.LEVEL1);
		}
	},
	loadCitys:function(e, t) {
		this.provinceid = Ext.get(t).getAttribute('provinceid');
		this.province = Ext.get(t).getAttribute('province');
		if (this.provinceid) {
			Ext.Ajax.request({
				url:this.cityURL,
				params : {father:this.provinceid},
				success: this.onLoadCitysSuccess,
				failure: SF.cmp.AjaxRequestFailure,
				scope:this
			});
		}
	},
	onLoadCitysSuccess:function(response) {
		var result = Ext.util.JSON.decode(response.responseText);
		if (result.citys != null && result.citys.length > 0) {
			this.fireEvent('loadCitySuccess', result.citys, this.provinceid, this.province);
		} else {
			this.fireEvent('notDataFound', SF.cmp.Region.LEVEL2, this.province);
		}
	},
	loadAreas:function(e, t) {
		this.cityid = Ext.get(t).getAttribute('cityid');
		this.city = Ext.get(t).getAttribute('city');
		if (this.cityid) {
			Ext.Ajax.request({
				url:this.areaURL,
				params : {father:this.cityid},
				success: this.onLoadAreasSuccess,
				failure: SF.cmp.AjaxRequestFailure,
				scope:this
			});
		}
	},
	onLoadAreasSuccess:function(response) {
		var result = Ext.util.JSON.decode(response.responseText);
		if (result.areas != null && result.areas.length > 0) {
			this.fireEvent('loadAreaSuccess', result.areas, this.cityid, this.city);
		} else {
			this.fireEvent('notDataFound', SF.cmp.Region.LEVEL3, this.city);
		}
	},
	loadCounty:function(e, t) {
		this.fireEvent('loadCountySuccess', Ext.get(t).getAttribute('area'));
	}
});

/**
 * 行政区域选择面板
 */
SF.cmp.RegionSelectPanel = function() {
	Ext.apply(this, {
		title:'<span class="sf-region-title"><a href="javascript:void(0);" regionType="1" class="x-region-title-on">${app:i18n_def('CN','中国')}</a></span>'+
		'<span class="sf-region-title">&nbsp;&gt;&nbsp;<a href="javascript:void(0);" regionType="2" class="x-region-title-on" provinceid="" province="">&nbsp</a></span>'+
		'<span class="sf-region-title">&nbsp;&gt;&nbsp;<a href="javascript:void(0);" regionType="3" class="x-region-title-on" cityid="" city="">&nbsp;</a></span>'+
		'<span class="sf-region-title">&nbsp;&gt;&nbsp;<a href="javascript:void(0);" class="x-region-title-off">&nbsp;</a></span>'
	});
	SF.cmp.RegionSelectPanel.superclass.constructor.apply(this, arguments);
	
	this.initTemplates();
	this.address = new SF.cmp.RegionAddress('', '', '');
	this.dataLoader = new SF.cmp.RegionDataLoader(this.provinceURL, this.cityURL, this.areaURL);
	
	this.dataLoader.on('loadProvinceSuccess', this.showProvinces, this);
	this.dataLoader.on('loadCitySuccess', this.showCitys, this);
	this.dataLoader.on('loadAreaSuccess', this.showAreas, this);
	this.dataLoader.on('loadCountySuccess', this.showCounty, this);
	this.dataLoader.on('notDataFound', this.showNotFound, this);
	
	this.addEvents({
		'regionChange':true
	});
};

Ext.extend(SF.cmp.RegionSelectPanel, Ext.Panel, {
	showTitleStyle:'display:inline',
	hideTitleStyle:'display:none',
	showTitle:function(type, id, name){
		var sps = Ext.query("span[class=sf-region-title]", this.header.dom);
		switch(type) {
			case 1: {
				var sps = Ext.query("span[class=sf-region-title]", this.header.dom);
				Ext.get(sps[0]).applyStyles(this.showTitleStyle);
				Ext.get(sps[1]).applyStyles(this.hideTitleStyle);
				Ext.get(sps[2]).applyStyles(this.hideTitleStyle);
				Ext.get(sps[3]).applyStyles(this.hideTitleStyle);
				break;
			}
			case 2: {
				var sps = Ext.query("span[class=sf-region-title]", this.header.dom);
				Ext.get(sps[0]).applyStyles(this.showTitleStyle);
				Ext.get(sps[1]).child('a').set({
					provinceid:id,
					province:name
				});
				Ext.get(sps[1]).child('a').dom.firstChild.nodeValue = name;
				if (id == '') {
					Ext.get(sps[1]).child('a').addClass('x-region-title-off');
				} else {
					Ext.get(sps[1]).child('a').removeClass('x-region-title-off');
				}
				Ext.get(sps[1]).applyStyles(this.showTitleStyle);
				Ext.get(sps[2]).applyStyles(this.hideTitleStyle);
				Ext.get(sps[3]).applyStyles(this.hideTitleStyle);
				break;
			}
			case 3: {
				Ext.get(sps[0]).applyStyles(this.showTitleStyle);
				Ext.get(sps[1]).applyStyles(this.showTitleStyle);
				Ext.get(sps[2]).child('a').set({
					cityid:id,
					city:name
				});
				Ext.get(sps[2]).child('a').dom.firstChild.nodeValue = name;
				if (id == '') {
					Ext.get(sps[2]).child('a').addClass('x-region-title-off');
				} else {
					Ext.get(sps[2]).child('a').removeClass('x-region-title-off');
				}
				Ext.get(sps[2]).applyStyles(this.showTitleStyle);
				Ext.get(sps[3]).applyStyles(this.hideTitleStyle);
				break;
			}
			default : {
				Ext.get(sps[0]).applyStyles(this.showTitleStyle);
				Ext.get(sps[1]).applyStyles(this.showTitleStyle);
				Ext.get(sps[2]).applyStyles(this.showTitleStyle);
				Ext.get(sps[3]).child('a').dom.firstChild.nodeValue = name;
				Ext.get(sps[3]).applyStyles(this.showTitleStyle);
			}
		}
	},
	onTitleClick:function(e, t){
		switch(Ext.get(t).getAttribute('regionType')) {
			case '1': 
				this.dataLoader.loadProvinces();
				break;
			case '2': 
				this.dataLoader.loadCitys(e, t);
				break;
			case '3': 
				this.dataLoader.loadAreas(e, t);
				break;
			default : 
				break;
		}
	},
	afterRender:function(){
		SF.cmp.RegionSelectPanel.superclass.afterRender.apply(this, arguments);
		Ext.select('a', false, this.header.dom).on('click', this.onTitleClick, this);
		this.showTitle(1);
		this.dataLoader.loadProvinces();
	},
	initTemplates:function(){
		this.provinceTemplate = new Ext.XTemplate(
			'<div class="x-region-list"><tpl for=".">',
			'<a href="javascript:void(0);" class="x-region-list-link" provinceid="{provinceid}" province="{province}" onclick1="regionDataLoader.loadCitys({provinceid},\'{province}\')">{province}</a>',
			'</tpl></div>'
		);
		this.provinceTemplate.compile();
		
		this.cityTemplate = new Ext.XTemplate(
			'<div class="x-region-list"><tpl for=".">',
			'<a href="javascript:void(0);" class="x-region-list-link" cityid="{cityid}" city="{city}" onclick1="regionDataLoader.loadAreas({cityid},\'{city}\')">{city}</a>',
			'</tpl></div>'
		);
		this.cityTemplate.compile();
		
		this.areaTemplate = new Ext.XTemplate(
			'<div class="x-region-list"><tpl for=".">',
			'<a href="javascript:void(0);" class="x-region-list-link" area="{area}" onclick1="regionDataLoader.loadCounty(\'{area}\')">{area}</a>',
			'</tpl></div>'
		);
		this.areaTemplate.compile();
		
		this.notFoundTemplate = new Ext.XTemplate(
			'<div></div>'
		);
		this.notFoundTemplate.compile();
	},
	showProvinces:function(datas) {
		this.showTitle(1);
		this.provinceTemplate.overwrite(this.body, datas);
		Ext.select("a", false, this.body.dom).on('click', this.dataLoader.loadCitys, this.dataLoader);
	},
	showCitys:function(datas, id, name) {
		this.address.setProvince(name);
		this.showTitle(2, id, name);
		this.cityTemplate.overwrite(this.body, datas);
		Ext.select("a", false, this.body.dom).on('click', this.dataLoader.loadAreas, this.dataLoader);
		
		this.fireEvent('regionChange', this.address.toString());
	},
	showAreas:function(datas, id, name) {
		this.address.setCity(name);
		this.showTitle(3, id, name);
		this.areaTemplate.overwrite(this.body, datas);
		Ext.select("a", false, this.body.dom).on('click', this.dataLoader.loadCounty, this.dataLoader);
		
		this.fireEvent('regionChange', this.address.toString());
	},
	showCounty:function(name) {
		this.address.setArea(name);
		this.showTitle(4, null, name);
		this.notFoundTemplate.overwrite(this.body, null);
		this.fireEvent('regionChange', this.address.toString());
	},
	showNotFound:function(level, name) {
		if (SF.cmp.Region.LEVEL2 == level) {
			this.showTitle(2, '', name);
			this.address.setProvince(name);
		} else if(SF.cmp.Region.LEVEL3 == level) {
			this.showTitle(3, '', name);
			this.address.setCity(name);
		}
		this.notFoundTemplate.overwrite(this.body, null);
		
		this.fireEvent('regionChange', this.address.toString());
	}
});

/**
 * 行政区域
 */
SF.cmp.RegionPanel = function(pUrl, cUrl, aUrl){
	this.regionSelectUI = new SF.cmp.RegionSelectPanel({
		provinceURL : pUrl || '../sfmcommon/searchProvinces.action', 
		cityURL : cUrl || '../sfmcommon/searchCitys.action', 
		areaURL : aUrl || '../sfmcommon/searchAreas.action' 
	});
	Ext.apply(this, {
		items:[this.regionSelectUI]
	});
	SF.cmp.RegionPanel.superclass.constructor.call(this);
}

Ext.extend(SF.cmp.RegionPanel, Ext.Panel, {
	title:'${app:i18n_def('cmp.js.19','行政区域')}',
	layout:'fit',
	getRegionSelectUI:function(){
		return this.regionSelectUI;
	}
});

/**
 * 网点定位器
 */
SF.cmp.DeptLocater = Ext.extend(Ext.util.Observable, {
	constructor:function(cfg){
		SF.cmp.DeptLocater.superclass.constructor.call(this);

		this.initCfg = cfg;
    	if (!cfg) {
    		throw "config is required!";
    	}
    	if (!cfg.callback) {
    		throw "callback function is required!";
    	}
    	if (cfg.btnId) {
    		var btn = Ext.getCmp(cfg.btnId);
        	if (!btn) {
        		throw "Button is not exists!";
        	}
        	if (btn.getXType() != 'button') {
        		throw "btnId is not button component!";
        	}
        	btn.on('click', this.onClick, this);
    	}
    	if (cfg.txtId) {
    		var txt = Ext.getCmp(cfg.txtId);
        	if (!txt) {
        		throw "DeptCode input is not exists!";
        	}
        	if (txt.enableKeyEvents) {
        		txt.on('keypress', function(t, e){
        			if(e.getKey() == 13) {
        				this.onClick();
        			}
        		}, this);
        	}
    	}
	},
	tipTpl:"${app:i18n_def('cmp.js.20','没有找到 {0} 对应的标记 ')}",
	successCallBack:function(result, rsp, opt) {
		if (result.spatial == null) {
			if (this.initCfg.disableTip != true) {
				if (opt.params.zoneCode) {
					Ext.Msg.showTip(String.format(this.tipTpl, opt.params.zoneCode));
				}
			}
		} else {
			this.initCfg.callback.call(this.initCfg.scope || this, result.spatial);
		}
	},
	getRequestParams:function(){
		var deptCode = Ext.getCmp(this.initCfg.txtId).getValue().trim();
    	if (deptCode == '') {
    		return null;
    	}
    	params = new Object();
    	params[Ext.getCmp(this.initCfg.txtId).getName().trim()] = deptCode;
    	return params;
	},
	onClick:function(){
		this.locate(this.getRequestParams());
	},
	locate:function(params){
		if (params != null) {
			if (typeof(params) == 'string') {
				params = {
					zoneCode:params
				};
			}
			Ext.Ajax.requestEx({
                url : this.initCfg.url || '../statistic/loadMarker.action',
                params : params,
                successCallback : this.successCallBack,
                scope:this
            });
		}
    },
    locateToUserDept:function() {
    	Ext.defer(this.locate, 1500, this, ['']);
    }
});

/**
 * 邮政编码定位器
 */
SF.cmp.PostCodeLocater = Ext.extend(Ext.util.Observable, {
	constructor:function(cfg){
		SF.cmp.PostCodeLocater.superclass.constructor.call(this);

		this.initCfg = cfg;
    	if (!cfg) {
    		throw "config is required!";
    	}
    	if (!cfg.callback) {
    		throw "callback function is required!";
    	}
    	if (!cfg.btnId) {
    		throw "btnId is required!";
    	}
    	var btn = Ext.getCmp(cfg.btnId);
    	if (!btn) {
    		throw "Button is not exists!";
    	}
    	if (btn.getXType() != 'button') {
    		throw "btnId is not button component!";
    	}
    	btn.on('click', this.locate, this);
    	
    	if (!cfg.txtId) {
    		throw "postCodeInputId is required!";
    	}
    	var txt = Ext.getCmp(cfg.txtId);
    	if (!txt) {
    		throw "postCode input is not exists!";
    	}
    	if (txt.enableKeyEvents) {
    		txt.on('keypress', function(t, e){
    			if(e.getKey() == 13) {
    				this.locate();
    			}
    		}, this);
    	}
    	if (!cfg.cbxId) {
    		throw "combobox id is required!";
    	}
    	var cbx = Ext.getCmp(cfg.cbxId);
    	if (!cbx) {
    		throw "country combobox is not exists!";
    	}
	},
	getRequestParams:function(){
		var ct = Ext.getCmp(this.initCfg.cbxId).getValue();
    	if (ct == '') {
    		return null;
    	}
		var pc = Ext.getCmp(this.initCfg.txtId).getValue().trim();
    	if (pc == '') {
    		return null;
    	}
    	return {
    		'country' : ct,
			'postCode' : pc
    	};
	},
	successCallBack:function(result, rsp, opt) {
		if (result.bound == null) {
			Ext.Msg.showTip("${app:i18n_def('cmp.js.21','无法搜索到此邮政编码')}");
		} else {
			this.initCfg.callback.call(this.initCfg.scope || this, result.bound, opt.params);
		}
	},
	locate:function(){
		var params = this.getRequestParams();
    	if (params != null) {
			Ext.Ajax.requestEx({
				url : '../statistic/searchCADBound.action',
				params : params,
				successCallback : this.successCallBack,
				scope:this
			});
    	}
    }
});

/**
 * 适用范围
 */
SF.cmp.ApplyScoper = Ext.extend(Ext.util.Observable, {
	constructor:function(cfg){
		SF.cmp.ApplyScoper.superclass.constructor.call(this);
		
		this.initCfg = cfg;
    	if (!cfg) {
    		throw "config is required!";
    	}
    	if (!cfg.callback) {
    		throw "callback function is required!";
    	}
    	if (!cfg.radios) {
    		throw "radios is required!";
    	}
    	if (!Ext.isArray(cfg.radios)) {
    		throw "radios is not Array";
    	}

    	this.radios = [];
    	this.labs = [];
    	for(var i=0; i<cfg.radios.length; i++) {
    		var rd = Ext.getCmp(cfg.radios[i]);
    		if (rd) {
    			this.radios.push(rd);
    			this.labs.push(new Ext.Element(rd.wrap.dom.lastChild));
    		}
    	}
    	
    	this.initApplyScope();
	},
	initApplyScope:function(){
		var currentScope;
        switch (new Date().getDay()) { 
            case 6 :
            	currentScope = '6';
                break;
            case 0 :
            	currentScope = '7';
                break;
            default :
            	currentScope = '1';
                break;
        }
        for(var i=0; i<this.radios.length; i++) {
        	if (currentScope == this.radios[i].getRawValue()) {
        		this.radios[i].setValue(true);
        		this.applyScope = currentScope;
				this.labs[i].addClass(this.checkedCls);
        	}
        	this.radios[i].on('check', this.onRadioCheck, this);
        }
	},
	checkedCls:'sf-applyScope-checked',
	onRadioCheck:function(t, checked){
		if (checked) {
			this.changeStyle(t);
			this.applyScope = t.getRawValue();
			this.initCfg.callback.call(this.initCfg.scope || this, this.applyScope);
		}
	},
	changeStyle:function(checkedRadio) {
		for(var i=0; i<this.radios.length; i++) {
			if (this.radios[i] == checkedRadio) {
				this.labs[i].addClass(this.checkedCls);
			} else {
				this.labs[i].removeClass(this.checkedCls);
			}
		}
	},
	getApplyScope:function(){
		return this.applyScope;
    }
});

SF.App = function() {
	Ext.QuickTips.init();
	Ext.onReady(this.initApp, this);
}

Ext.extend(SF.App, Ext.util.Observable, {
	initApp:function(){
		this.createUI();
		this.initUI();
	},
	createUI:Ext.emptyFn,
	initUI:Ext.emptyFn
});

SF.SpatialDataProvider = function(cfg) {
	SF.SpatialDataProvider.superclass.constructor.call(this);
	this.initCfg = cfg;
}

Ext.extend(SF.SpatialDataProvider, Ext.util.Observable, {
	load:function(params) {
		if (this.commitId) {
			Ext.Ajax.abort(this.commitId);
		}
		this.commitId = Ext.Ajax.requestEx({
            url : this.initCfg.url,
            params : params,
            scope:this,
            successCallback : function(result, rsp, opt){
            	this.commitId = undefined;
            	this.initCfg.successCallback.call(this.initCfg.scope || this, result, rsp, opt);
            }
        });
	}
});

if (Ext.loadDebug) {
	alert('cmp.js load ok!');
}