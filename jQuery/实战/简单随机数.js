//字母最大为f,长度为8
Math.random().toString(16).substring(2);

//10-20之间的随机数
Math.floor(Math.random() * (20 - 10 + 1)) + 10;

//随机获取数组中的值
var items = [12, 548 , 'a' , 2 , 5478 , 'foo' , 8852, 'Doe' , 2145 , 119];
items[Math.floor(Math.random() * items.length-1)];