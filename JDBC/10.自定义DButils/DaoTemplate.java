package cn.itcast.DBTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoTemplate {

	private DataSource dataSource=null;
	
	public DaoTemplate(DataSource dataSource){
		this.dataSource=dataSource;
	}
	
	public int update(String sql,Object[] args){
		Connection conn=null;
		PreparedStatement ps=null;
		int line=0;
		try {
			conn=dataSource.getConnection();
			ps=conn.prepareStatement(sql);
			ParameterMetaData pmd = ps.getParameterMetaData();
			int count = pmd.getParameterCount();//��ȡ��������
			if(count>0){
				if(args==null){
					throw new IllegalArgumentException("sql�����Ҫ����,��������Ϊ��");
				}
				if(count!=args.length){
					throw new IllegalArgumentException("��鿴sql���Ĳ���,������������ȷ");
				}
				for(int i=0;i<count;i++){
					ps.setObject(i+1, args[i]);
				}
			}
			line=ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("���ݿ����ʧ��");
		}finally{
			this.close(null, ps, null);
		}
		return line;
	}
	
	public Object query(String sql,Object[] args,ResultMapper rm){
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			Object obj=null;
			try {
				conn=dataSource.getConnection();
				ps=conn.prepareStatement(sql);
				ParameterMetaData pmd = ps.getParameterMetaData();
				int count = pmd.getParameterCount();
				if(count>0){
					if(args==null){
						throw new IllegalArgumentException("sql�����Ҫ����,��������Ϊ��");
					}
					if(count!=args.length){
						throw new IllegalArgumentException("��鿴sql���Ĳ���,������������ȷ");
					}
					for(int i=0;i<count;i++){
						ps.setObject(i+1, args[i]);
					}
				}
				rs=ps.executeQuery();
				obj=rm.mapper(rs);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("���ݿ����ʧ��");
			}finally{
				this.close(rs, ps, conn);
			}
			return obj;
	}


	private void close(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		rs=null;
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		stmt=null;
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		conn=null;
	}
	
}
