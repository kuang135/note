//��ĸ���Ϊf,����Ϊ8
Math.random().toString(16).substring(2);

//10-20֮��������
Math.floor(Math.random() * (20 - 10 + 1)) + 10;

//�����ȡ�����е�ֵ
var items = [12, 548 , 'a' , 2 , 5478 , 'foo' , 8852, 'Doe' , 2145 , 119];
items[Math.floor(Math.random() * items.length-1)];