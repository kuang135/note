/*
 * 将单个form表单的数据转成json
 * 		表单的形式为: name a, password 123456
 * 				  name b, password 654321
 * 		成对出现, 转成 [{"name": "a", "password": "123456"}, {"name": "b", "password": "654321"}]
 */
function convertFormData2Json(formId) {
	var arr = $('#' + formId).serializeArray()
		,obj = {};
	arr.forEach(function(item, index, array) {
//		console.dir(item);//{name: 'grade', value: 1}
		if (!obj[item.name] || !obj[item.name] instanceof Array) {
			obj[item.name] = [];
		}
		obj[item.name].push(item.value);
	});
	//console.dir(obj); //{grade: [1,2,3], riskStep: ['green','green','yellow']}
	var _nameArr = []
		,length = 0;
	for (var key in obj) {
		_nameArr.push(key); //['grade','riskStep']
		length = obj[key].length; 
	}
	var objArr = [];
	for (var i = 0; i < length; i++) {
		var _obj = {};
		for (var j=0; j<_nameArr.length; j++) {
	//		console.log('_nameArr['+ j + ']: ' + _nameArr[j]);
			_obj[_nameArr[j]] = obj[_nameArr[j]][i];
		}
	//	console.dir(_obj);
		objArr[i] = _obj;
	}
	var jsonData = JSON.stringify(objArr);
	//console.log(jsonData);
	return jsonData;
}