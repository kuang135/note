#! /usr/bin/env node
var fs = require('fs');
var crypto = require('crypto');

var dir = process.argv[2],
	account = process.argv[3],
	password = process.argv[4];
// 未输入指令
if (dir === undefined) {
	console.log('please input pswd -h for help');
	return;
}
// 帮助
if (dir === '-h') {
	console.log('pswd -l 账号 : 搜索');
	console.log('pswd -i 账号 密码 : 插入');
	console.log('pswd -u 账号 密码 : 更新');
	console.log('pswd -d 账号 : 删除');
}
// 插入
if (dir === '-i') {
	if (!account || !password) {
		console.log('you must input account and password.');
		return;
	}
	var map = {};
	map.account = account;
	map.password = password;
	insert(map);
}
// 更新
if (dir === '-u'){
	if (!account || !password) {
		console.log('you must input account and password.');
		return;
	}
	var map = {};
	map.account = account;
	map.password = password;
	update(map);
}
// 删除
if (dir === '-d') {
	if (!account) {
		console.log('you must input account.');
		return;
	}
	var map = {};
	map.account = account;
	del(map);
}
// 查看
if (dir === '-l') {
	list(account);
}
// 其他
if (dir !== '-h' && dir !== '-l' && dir !== '-i' && dir !== '-u' && dir !== '-d') {
	console.log('no such directive, please input pswd -h for help.');
}

function insert(map) {
	fs.readFile(__dirname + '/password.json','utf8' ,function(err, data) {
		if(err) throw err;
		if (data === '') {
			writeFile(JSON.stringify([map]));
		} else {
			data = decipher(data);
			var arr = JSON.parse(data);
			var	existObj = null;
			var	exist = arr.some(function(item, index, arr) {
					if(item.account === map.account) {
						existObj = item;
						return true;
					}
				});
			if (exist) {
				console.log(existObj.account + ' : ' + existObj.password);
				console.log('this account has areadly exist, use pswd -u for update, use pase -d for delete');
			} else {
				arr.push(map);
				writeFile(JSON.stringify(arr));
			}
		}
	});
}

function update(map) {
	fs.readFile(__dirname + '/password.json','utf8' ,function(err, data) {
		if(err) throw err;
		if (data === '') {
			console.log('no account exist, use pswd -i for insert');
		} else {
			data = decipher(data);
			var arr = JSON.parse(data);
			var	existIndex = null;
			var	exist = arr.some(function(item, index, arr) {
					if(item.account === map.account) {
						existIndex = index;
						return true;
					}
				});
			if (exist) {
				arr[existIndex] = map;
				writeFile(JSON.stringify(arr));
			} else {
				console.log('this account did not exist, use pswd -i for insert');
			}			
		}
	});
}

function del(map) {
	fs.readFile(__dirname + '/password.json','utf8' ,function(err, data) {
		if(err) throw err;
		if (data === '') {
			console.log('no account exist, use pswd -i for insert');
		} else {
			data = decipher(data);
			var arr = JSON.parse(data);
			var	existIndex = null;
			var	exist = arr.some(function(item, index, arr) {
					if(item.account === map.account) {
						existIndex = index;
						return true;
					}
				});
			if (exist) {
				arr.splice(existIndex, 1);
				if (arr.length > 0) {
					writeFile(JSON.stringify(arr));
				} else {
					writeFile('');
				}
			} else {
				console.log('this account did not exist, use pswd -i for insert');
			}
		}
	});
}
function writeFile(jsonString) {
	if (jsonString !== '') {
		jsonString = cipher(jsonString);
	}
	fs.writeFile(__dirname + '/password.json', jsonString, 'utf8', function(err){
		if(err) throw err;
	});
}
function list(acc) {
	fs.readFile(__dirname + '/password.json','utf8' ,function(err, data) {
		if(err) throw err;
		if (data !== '') {
			data = decipher(data);
			var arr = JSON.parse(data);
			arr.forEach(function(item, index, arr) {
				if (!!(acc)) {
					if (item.account.indexOf(acc) > -1) {
						console.log(item.account + ' : ' + item.password);
					}
				} else {
					console.log(item.account + ' : ' + item.password);
				}
			});
		}
	});
}
// 加密
function cipher(data) {
	var cipher = crypto.createCipher('aes192', 'missxu');
	var encrypted = cipher.update(data, 'utf8', 'hex');
	encrypted += cipher.final('hex');
	return encrypted;
}
// 解密
function decipher(encrypted) {
	var decipher = crypto.createDecipher('aes192', 'missxu');
	var decrypted = decipher.update(encrypted, 'hex', 'utf8');
	decrypted += decipher.final('utf8');
	return decrypted;	
}