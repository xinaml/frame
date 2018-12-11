/**
 * @fileoverview union common.js
 * @author bjzuoshilong
 * @version 1.1
 * @date 2013.5.31
 */
(function() {
	var b = '';
	$("a[id^='J_']").each(function() {
		b += $(this).attr('id') + ','
	});
	if (b) {
		$.getJSON('http://p.3.cn/prices/mgets?skuids=' + b
				+ '&type=1&callback=?', function(a) {
			if (typeof a == 'object') {
				for ( var i = 0, j = a.length, p = ''; i < j; i++) {
					if (a[i].p < 0) {
						p = '暂无报价'
					} else if (a[i].p == 0) {
						p = '免费'
					} else {
						p = '￥' + a[i].p
					}
					$("a[id='" + a[i].id + "']").html('<em>' + p + '</em>');
					/*$("#" + a[i].id).html('<em>' + p + '</em>')*/
				}
			}
		})
	}
})();
(function() {
	function getElementsByClassName(d, e) {
		if (d.getElementsByClassName) {
			return d.getElementsByClassName(e)
		} else {
			return (function(a, b) {
				if (b == null)
					b = document;
				var c = [], els = b.getElementsByTagName("*"), elsLen = els.length, pattern = new RegExp(
						"(^|\\s)" + a + "(\\s|$)"), i, j;
				for (i = 0, j = 0; i < elsLen; i++) {
					if (pattern.test(els[i].className)) {
						c[j] = els[i];
						j++
					}
				}
				return c
			})(e, d)
		}
	}
	;
	function parseURL(c) {
		var a = document.createElement('a');
		a.href = c;
		return {
			source : c,
			protocol : a.protocol.replace(':', ''),
			host : a.hostname,
			port : a.port,
			query : a.search,
			params : (function() {
				var b = {}, seg = a.search.replace(/^\?/, '').split('&'), len = seg.length, i = 0, s;
				for (; i < len; i++) {
					if (!seg[i]) {
						continue
					}
					s = seg[i].split('=');
					b[s[0]] = s[1]
				}
				return b
			})(),
			file : (a.pathname.match(/\/([^\/?#]+)$/i) || [ , '' ])[1],
			hash : a.hash.replace('#', ''),
			path : a.pathname.replace(/^([^\/])/, '/$1'),
			relative : (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [ , '' ])[1],
			segments : a.pathname.replace(/^\//, '').split('/')
		}
	}
	;
	var f = {};
	f.btnList = null;
	f.itemList = null;
	f.timeIndex = 0;
	f.init = function(a, b) {
		this.btnList = getElementsByClassName(document, a);
		this.itemList = getElementsByClassName(document, b);
		var c = this;
		for ( var i = 0; i < c.btnList.length; i++) {
			c.itemList[i].style.display = "none";
			c.btnList[i].setAttribute("btn-index", i);
			c.btnList[i].onmouseover = function() {
				c.changeItem(this.getAttribute("btn-index"));
				c.timeIndex = parseInt(this.getAttribute("btn-index"));
				clearInterval(e);
				e = setInterval(function() {
					c.timerChange()
				}, 4500)
			}
		}
		c.changeItem(0);
		var e = setInterval(function() {
			c.timerChange()
		}, 4500)
	};
	f.changeItem = function(a) {
		if (!this.btnList[a]) {
			return false
		}
		this.btnList[a].setAttribute("class", "triggers-btn current");
		this.btnList[a].setAttribute("className", "triggers-btn current");
		this.itemList[a].style.display = "block";
		for ( var j = 0; j < this.btnList.length; j++) {
			if (j != a) {
				this.btnList[j].setAttribute("class", "triggers-btn");
				this.btnList[j].setAttribute("className", "triggers-btn");
				this.itemList[j].style.display = "none"
			}
		}
	};
	f.timerChange = function() {
		this.timeIndex = this.timeIndex + 1;
		if (this.timeIndex == this.btnList.length) {
			this.timeIndex = 0
		}
		this.changeItem(this.timeIndex)
	};
	function unionAdInit() {
		var a = parseURL(window.location.href);
		if (typeof a.params.w != "undefined"
				&& typeof a.params.h != "undefined") {
			var b = document.getElementById("union-layout");
			if(b){
				b.style.width = a.params.w + "px";
				b.style.height = a.params.h + "px"
			}
		}
	}
	;
	unionAdInit();
	f.init("triggers-btn", "union-list-box")
})()
