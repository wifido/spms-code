Ext.namespace("Ext.ux.plugins");

Ext.ux.plugins.GroupHeaderGrid = function(config) {
	Ext.apply(this, config);
};

Ext.extend(Ext.ux.plugins.GroupHeaderGrid, Ext.util.Observable, {
	init: function(grid) {
		var v = grid.getView();
		v.beforeMethod('initTemplates', this.initTemplates);
		v.renderHeaders = this.renderHeaders.createDelegate(v, [v.renderHeaders]);
        v.afterMethod('onColumnWidthUpdated', this.updateGroupStyles);
        v.afterMethod('onAllColumnWidthsUpdated', this.updateGroupStyles);
		v.afterMethod('onColumnHiddenUpdated', this.updateGroupStyles);
		v.afterMethod('handleHdOver', this.handleHdOver);
		v.afterMethod('handleHdOut', this.handleHdOut);
		v.getHeaderCell = this.getHeaderCell;
		v.updateSortIcon = this.updateSortIcon;
		v.getGroupStyle = this.getGroupStyle;
		v.groupRows = this.rows;
		v.findGroupCells = this.findGroupCells;
	},

	initTemplates: function() {
		var ts = this.templates || {};
		if (!ts.gcell) {
			ts.gcell = new Ext.Template(
				'<td class="x-grid3-hd {cls} x-grid3-td-{id}" style="{style}">',
				'<div {tooltip} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">{value}</div>',
				'</td>'
			);
		}
		this.templates = ts;
	},

	renderHeaders: function(renderHeaders) {
		var ts = this.templates, rows = [], tw = this.getTotalWidth();
		for (var i = 0; i < this.groupRows.length; i++) {
			var r = this.groupRows[i], cells = [], col = 0;
			for (var j = 0; j < r.length; j++) {
				var c = r[j];
				c.colspan = c.colspan || 1;
				c.col = col;
				col += c.colspan;
				var gs = this.getGroupStyle(c, false);
				cells[j] = ts.gcell.apply({
					id: c.id || i + '-' + col,
					cls: c.header ? 'ux-grid-hd ux-grid-hd-group-cell x-grid3-header' : 'ux-grid-hd ux-grid-hd-nogroup-cell',
					style: 'width:' + gs.width + ';' + (gs.hidden ? 'display:none;' : '') + (c.align ? 'text-align:' + c.align + ';' : ''),
					tooltip: c.tooltip ? (Ext.QuickTips.isEnabled() ? 'ext:qtip' : 'title') + '="' + c.tooltip + '"' : '',
					value: c.header || '&#160;',
					istyle: c.align == 'right' ? 'padding-right:16px' : ''
				});
			}
			rows[i] = ts.header.apply({
				tstyle: 'width:' + tw + ';',
				cells: cells.join('')
			});
		}
		rows[rows.length] = renderHeaders.call(this);
		return rows.join('');
	},
	findGroupCells : function(el, index) {
		if (!el) {
			return false;
		}
		var grpcells = [];
		if (index == 0) {
			for (var i = 0; i < this.groupRows.length; i++) {
				grpcells.push(0);
			}
		} else {
			for (var i = 0; i < this.groupRows.length; i++) {
				var r = this.groupRows[i], col = 0;
				for (var j = 0; j < r.length; j++) {
					var c = r[j];
					c.colspan = c.colspan || 1;
					col += c.colspan;
					if (col > index) {
					    grpcells.push(j);
						break;
					}
				}
			}
		}
		if (grpcells.length == 0)
			return false;
		return grpcells;
	},
	handleHdOver: function(e, target) {
		var header = this.findHeaderCell(target);
		if (header && !this.headersDisabled) {
			var headerFly = this.fly(header);
			if (!headerFly.hasClass('x-grid3-hd-over')) {
				return;
			}
			var sort1 = headerFly.hasClass(this.sortClasses[0]);
			var sort2 = headerFly.hasClass(this.sortClasses[1]);
			var hdIndex = this.getCellIndex(header);
			if(hdIndex){
				var grpcells = this.findGroupCells(target, hdIndex);
				if (grpcells) {
					var hdTbl = this.fly(target).findParent('table', 10, true);
					for (var i = 0; i < grpcells.length; i++) {
						var grpTbl = hdTbl.prev();
						if (grpTbl) {
							var grpHead = grpTbl.query('td.ux-grid-hd')[grpcells[i]];
							if (grpHead) {
								var grpHeadFly = this.fly(grpHead);
								grpHeadFly.addClass('x-grid3-hd-over');
								if (sort1) {
									grpHeadFly.addClass(this.sortClasses[0]);
								}
								if (sort2) {
									grpHeadFly.addClass(this.sortClasses[1]);
								}
							}
							hdTbl = grpTbl;
						} else {
							break;
						}
					}
				}
			}
		}
	},
	handleHdOut: function(e, target) {
		var header = this.findCell(target);
		if (header) {
			var headerFly = this.fly(header);
			if (headerFly.hasClass('x-grid3-hd-over')) {
				return;
			}
			var hdIndex = this.getCellIndex(header);
			if (hdIndex) {
				var grpcells = this.findGroupCells(target, hdIndex);
				if (grpcells) {
					var hdTbl = this.fly(target).findParent('table', 10, true);
					for (var i = 0; i < grpcells.length; i++) {
						var grpTbl = hdTbl.prev();
						if (grpTbl) {
							var grpHead = grpTbl.query('td.ux-grid-hd')[grpcells[i]];
							if (grpHead) {
								var grpHeadFly = this.fly(grpHead);
								grpHeadFly.removeClass('x-grid3-hd-over');
							}
							hdTbl = grpTbl;
						} else {
							break;
						}
					}
				}
			}
		}
	},
	getGroupStyle: function(c, isUp) {
		var w = 0, h = true;
		for (var i = c.col; i < c.col + c.colspan; i++) {
			if (!this.cm.isHidden(i)) {
				var cw;
				if (isUp) {
					var wstr = this.getHeaderCell(i).style.width;
					cw = parseInt(wstr.substr(0, wstr.length - 2 ));
					cw += !Ext.isBorderBox ? this.borderWidth : 0;
				} else {
					cw = this.cm.getColumnWidth(i);
				}
				if(typeof cw == 'number'){
					w += cw;
				}
				h = false;
			}
		}
		return {
			width: (Ext.isBorderBox ? w : Math.max(w - this.borderWidth, 0)) + 'px',
			hidden: h
		}
	},

	updateGroupStyles: function(col) {
		var tables = this.mainHd.query('.x-grid3-header-offset > table'), tw = this.getTotalWidth();
		for (var i = 0; i < tables.length; i++) {
			tables[i].style.width = tw;
			if (i < this.groupRows.length) {
				var cells = tables[i].firstChild.firstChild.childNodes;
				for (var j = 0; j < cells.length; j++) {
					var c = this.groupRows[i][j];
					if ((typeof col != 'number') || (col >= c.col && col < c.col + c.colspan)) {
						var gs = this.getGroupStyle(c, true);
						cells[j].style.width = gs.width;
						cells[j].style.display = gs.hidden ? 'none' : '';
					}
				}
			}
		}
	},

	getHeaderCell : function(index){
		return this.mainHd.query('td.x-grid3-cell')[index];
	},

	updateSortIcon : function(col, dir){
		var sc = this.sortClasses;
		var hds = this.mainHd.select('td.x-grid3-cell').removeClass(sc);
		hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
	}
});
