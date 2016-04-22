
Ext.ux.DatePicker = Ext.extend(Ext.DatePicker, {
	 
	statusDays:[],
	
	onRender : function(container, position){
	    var m = [
	         '<table cellspacing="0">',
	            '<tr><td class="x-date-left"><a href="#" title="', this.prevText ,'">&#160;</a></td><td class="x-date-middle" align="center"></td><td class="x-date-right"><a href="#" title="', this.nextText ,'">&#160;</a></td></tr>',
	            '<tr><td colspan="3"><table class="x-date-inner" cellspacing="0"><thead><tr>'],
	            dn = this.dayNames,
	            i;
	    for(i = 0; i < 7; i++){
	        var d = this.startDay+i;
	        if(d > 6){
	            d = d-7;
	        }
	        m.push('<th><span>', dn[d].substr(0,1), '</span></th>');
	    }
	    m[m.length] = '</tr></thead><tbody><tr>';
	    for(i = 0; i < 42; i++) {
	        if(i % 7 === 0 && i !== 0){
	            m[m.length] = '</tr><tr>';
	        }
	        m[m.length] = '<td><a href="#" hidefocus="on" class="x-date-date" tabIndex="1"><em><span></span></em></a></td>';
	    }
	    m.push('</tr></tbody></table></td></tr>',
	            this.showToday ? '<tr><td colspan="3" class="x-date-bottom" align="center"></td></tr>' : '',
	            '</table><div class="x-date-mp"></div>');

	    var el = document.createElement('div');
	    el.className = 'x-date-picker';
	    el.innerHTML = m.join('');

	    container.dom.insertBefore(el, position);

	    this.el = Ext.get(el);
	    this.eventEl = Ext.get(el.firstChild);

	    this.prevRepeater = new Ext.util.ClickRepeater(this.el.child('td.x-date-left a'), {
	        handler: this.showPrevMonth,
	        scope: this,
	        preventDefault:true,
	        stopDefault:true
	    });

	    this.nextRepeater = new Ext.util.ClickRepeater(this.el.child('td.x-date-right a'), {
	        handler: this.showNextMonth,
	        scope: this,
	        preventDefault:true,
	        stopDefault:true
	    });

	    this.monthPicker = this.el.down('div.x-date-mp');
	    this.monthPicker.enableDisplayMode('block');

	    this.keyNav = new Ext.KeyNav(this.eventEl, {
	        'left' : function(e){
	            if(e.ctrlKey){
	                this.showPrevMonth();
	            }else{
	                this.update(this.activeDate.add('d', -1));
	            }
	        },

	        'right' : function(e){
	            if(e.ctrlKey){
	                this.showNextMonth();
	            }else{
	                this.update(this.activeDate.add('d', 1));
	            }
	        },

	        'up' : function(e){
	            if(e.ctrlKey){
	                this.showNextYear();
	            }else{
	                this.update(this.activeDate.add('d', -7));
	            }
	        },

	        'down' : function(e){
	            if(e.ctrlKey){
	                this.showPrevYear();
	            }else{
	                this.update(this.activeDate.add('d', 7));
	            }
	        },

	        'pageUp' : function(e){
	            this.showNextMonth();
	        },

	        'pageDown' : function(e){
	            this.showPrevMonth();
	        },

	        'enter' : function(e){
	            e.stopPropagation();
	            return true;
	        },

	        scope : this
	    });

	    this.el.unselectable();

	    this.cells = this.el.select('table.x-date-inner tbody td');
	    this.textNodes = this.el.query('table.x-date-inner tbody span');

	    this.mbtn = new Ext.Button({
	        text: '&#160;',
	        tooltip: this.monthYearText,
	        renderTo: this.el.child('td.x-date-middle', true)
	    });
	    //this.mbtn.el.child('em').addClass('x-btn-arrow');

	    if(this.showToday){
	        this.todayKeyListener = this.eventEl.addKeyListener(Ext.EventObject.SPACE, this.selectToday,  this);
	        var today = (new Date()).dateFormat(this.format);
	        this.todayBtn = new Ext.Button({
	            renderTo: this.el.child('td.x-date-bottom', true),
	            text: String.format(this.todayText, today),
	            tooltip: String.format(this.todayTip, today),
	            handler: this.selectToday,
	            scope: this
	        });
	    }
	    this.mon(this.eventEl, 'click', this.handleDateClick,  this, {delegate: 'a.x-date-date'});
	    this.onEnable(true);
	},
	 handleDateClick : function(e, t){
	        e.stopEvent();
	        if(!this.disabled && t.dateValue && !Ext.fly(t.parentNode).hasClass('x-date-disabled')){
	            this.cancelFocus = this.focusOnSelect === false;
	            this.setValue(new Date(t.dateValue));
	            delete this.cancelFocus;
	            this.fireEvent('select', this, this.value);
	        }
	    },

// private
update : function(date, forceRefresh){
	this.dtArr = [];
	if(this.rendered){
	var vd = this.activeDate, vis = this.isVisible();
	this.activeDate = date;
	if(!forceRefresh && vd && this.el){
		var t = date.getTime();
		if(vd.getMonth() == date.getMonth() && vd.getFullYear() == date.getFullYear()){
			this.cells.removeClass('x-date-selected');
			this.cells.each(function(c){
				if(c.dom.firstChild.dateValue == t){
					c.addClass('x-date-selected');
					if(vis && !this.cancelFocus){
						Ext.fly(c.dom.firstChild).focus(50);
					}
					return false;
				}
			}, this);
			return;
		}
	}
	var days = date.getDaysInMonth(),
	firstOfMonth = date.getFirstDateOfMonth(),
	startingPos = firstOfMonth.getDay()-this.startDay;

	if(startingPos < 0){
		startingPos += 7;
	}
	days += startingPos;

	var pm = date.add('mo', -1),
	prevStart = pm.getDaysInMonth()-startingPos,
	cells = this.cells.elements,
	textEls = this.textNodes,

	// convert everything to numbers so it's fast
	d = (new Date(pm.getFullYear(), pm.getMonth(), prevStart, this.initHour)),
	today = new Date().clearTime().getTime(),
	sel = date.clearTime(true).getTime(),
	min = this.minDate ? this.minDate.clearTime(true) : Number.NEGATIVE_INFINITY,
	max = this.maxDate ? this.maxDate.clearTime(true) : Number.POSITIVE_INFINITY,
	ddMatch = this.disabledDatesRE,
	ddText = this.disabledDatesText,
	ddays = this.disabledDays ? this.disabledDays.join('') : false,
	sdays = this.statusDays ? this.statusDays : false,
	ddaysText = this.disabledDaysText,
	format = this.format;

	if(this.showToday){
		var td = new Date().clearTime(),
		disable = (td < min || td > max ||
					(ddMatch && format && ddMatch.test(td.dateFormat(format))) ||
					(ddays && ddays.indexOf(td.getDay()) != -1));

		if(!this.disabled){
			this.todayBtn.setDisabled(disable);
			this.todayKeyListener[disable ? 'disable' : 'enable']();
		}
	}

	var setCellClass = function(cal, cell){
		cell.title = '';
		var t = d.clearTime(true).getTime();
		cell.firstChild.dateValue = t;
		/*if(t == today){
			cell.className += 'x-date-today';
			cell.title = cal.todayText;
		}*/
		//console.info(new Date(t).format('Y-m-d'));
		/*if(t == sel){
			if(!Ext.isEmpty(this.dtArr)){
				var seldtIndex = this.dtArr.indexOf(new Date(t).format('Y-m-d'));
				console.info(seldtIndex);
				console.info(this.cells.elements[seldtIndexthis]);
				this.cells.elements[seldtIndexthis].className += 'x-date-status-green';
				if(vis){
					Ext.fly(cell.firstChild).focus(50);
				}
			}
		}*/
		// disabling
		if(t < min) {
			cell.className = ' x-date-disabled';
			cell.title = cal.minText;
			return;
		}
		if(t > max) {
			cell.className = ' x-date-disabled';
			cell.title = cal.maxText;
			return;
		}
		if(ddays){
			if(ddays.indexOf(d.getDay()) != -1){
				cell.title = ddaysText;
				cell.className = ' x-date-disabled';
			}
		}
		if(sdays){
			var s = (d.getMonth() + 1)<10?"0"+(d.getMonth() + 1)+"/":(d.getMonth() + 1) + "/";
			s += d.getDate()<10?"0"+d.getDate()+"/":d.getDate() + "/";
				if(Ext.isIE){
					s += d.getYear();
				}else{
					s += d.getYear() + 1900;
				}
				Ext.each(sdays, function(sday, i){
					if(Ext.util.Format.date(sday,'Y-m-d') == Ext.util.Format.date(s,'Y-m-d')){
						cell.className = 'x-date-status-green';
					}
				}, this);
			}

		if(ddMatch && format){
			var fvalue = d.dateFormat(format);
			if(ddMatch.test(fvalue)){
				cell.title = ddText.replace('%0', fvalue);
				// cell.className = ' x-date-disabled';
			}
		}
	};

	var i = 0;
	for(; i < startingPos; i++) {
		 textEls[i].innerHTML = (++prevStart);
         d.setDate(d.getDate()+1);
         var tmpdt = Ext.util.Format.date(d,'Y-m-d');
         this.dtArr.push(tmpdt);
         cells[i].className = 'x-date-prevday';
         setCellClass(this, cells[i]);
	}
	
	for(; i < days; i++){
		var intDay = i - startingPos + 1;
		textEls[i].innerHTML = (intDay);
		d.setDate(d.getDate()+1);
		var tmpdt = Ext.util.Format.date(d,'Y-m-d');
		this.dtArr.push(tmpdt);
		/*if(date.clearTime(true).getTime()==d.clearTime(true).getTime() && this._$flag === 1) {
			cells[i].className = 'x-date-status-green';
		} else {
			cells[i].className = 'x-date-active';
		}*/
		cells[i].className = 'x-date-active';
		setCellClass(this, cells[i]);
	}
	
	var extraDays = 0;
	for(; i < 42; i++) {
		 textEls[i].innerHTML = (++extraDays);
         d.setDate(d.getDate()+1);
         var tmpdt = Ext.util.Format.date(d,'Y-m-d');
         this.dtArr.push(tmpdt);
         cells[i].className = 'x-date-nextday';
         setCellClass(this, cells[i]);
	}
	
	this.mbtn.setText(this.monthNames[date.getMonth()] + ' ' + date.getFullYear());

	if(!this.internalRender){
		var main = this.el.dom.firstChild,
		w = main.offsetWidth;
		this.el.setWidth(w + this.el.getBorderWidth('lr'));
		Ext.fly(main).setWidth(w);
		this.internalRender = true;
		if(Ext.isOpera && !this.secondPass){
			main.rows[0].cells[1].style.width = (w - (main.rows[0].cells[0].offsetWidth+main.rows[0].cells[2].offsetWidth)) + 'px';
			this.secondPass = true;
			this.update.defer(10, this, [date]);
		}
	}
}
},
listeners : {
	select : function(dp,dt){
		var i = dt.getFirstDayOfMonth() + dt.getDate() - 1;
		var cell = this.cells.elements[i];
		var selDateArr = this.statusDays;
		var seldt = dt.format('Y-m-d');
		if(cell.className == 'x-date-status-green x-date-selected'){
			cell.className = 'x-date-active';
			selDateArr.remove(seldt);
		}else{
			if(cell.firstChild.dateValue==dt.clearTime(true).getTime())
			{
			cell.className = 'x-date-status-green';
			}
			if(selDateArr.indexOf(seldt)==-1)
			selDateArr.push(seldt);
		}
	}
}

}); 