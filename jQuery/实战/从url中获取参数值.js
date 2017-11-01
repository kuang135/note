function getRequestParam(name){
	var pattern = /(\w*)=([a-zA-Z0-9\u4e00-\u9fa5]+)/ig, params = {};//定义正则表达式和一个空对象
	decodeURIComponent(window.location.href, true).replace(pattern, function(a, b, c){ params[b] = c; });
	return name ? (params[name] ? params[name] : null) : params;
}