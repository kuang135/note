package cn.itcast.DBTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper {

	//�ṩ������ʵ����,����������һ�������һ����������
	//�õ��Ƿ���ķ�ʽ,JavaBean�е����Ա���ͱ��ж�Ӧ������һ��
	//��ȻҲ�����Զ���ʵ����,���÷���ķ�ʽ
	public Object mapper(ResultSet rs) throws Exception ;
}
