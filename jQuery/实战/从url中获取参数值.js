function getRequestParam(name){
	var pattern = /(\w*)=([a-zA-Z0-9\u4e00-\u9fa5]+)/ig, params = {};//����������ʽ��һ���ն���
	decodeURIComponent(window.location.href, true).replace(pattern, function(a, b, c){ params[b] = c; });
	return name ? (params[name] ? params[name] : null) : params;
}