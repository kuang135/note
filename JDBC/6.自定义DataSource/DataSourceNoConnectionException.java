package cn.itcast.datasource;

public class DataSourceNoConnectionException extends RuntimeException{

	public DataSourceNoConnectionException(){
		super();
	}
	public DataSourceNoConnectionException(String message){
		super(message);
	}
}
