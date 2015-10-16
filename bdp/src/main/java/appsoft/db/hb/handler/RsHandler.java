package appsoft.db.hb.handler;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;


public interface RsHandler<T> {
	public T handle(String tablename,ResultScanner rs)throws IOException;
	public T handle(String tablename,Result[] rs)throws IOException;
	public T handle(String tablename,Result r)throws IOException;

}
