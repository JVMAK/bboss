/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset;

import java.sql.SQLException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.util.SQLUtil;

public class TestDate {

	public TestDate() {
		// TODO Auto-generated constructor stub
	}
	@BeforeClass
	public static void init() throws SQLException
	{
		//oracle
		SQLUtil.startPool("test","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@//10.0.15.51:1521/orcl","testpdp1","testpdp1",
				 "true",
				 null,//"READ_COMMITTED",
				"select 1 from dual",
				 "jndi-test",   
				 2,
				 2,
				 2,
		   		false,
		   		false,
		   		null        ,true,false
		   		);
		try {
			SQLExecutor.queryObjectWithDBName(int.class, "test","select 1 from datebean where 1=0");
			SQLExecutor.deleteWithDBName("test", "delete from datebean");
		} catch (SQLException e) {
			
			SQLExecutor.updateWithDBName("test", "CREATE TABLE datebean(  utildate       DATE,  sqldate        DATE,  timestampdate  DATE,  id             NUMBER)");
			
		}
		
		try {
			SQLExecutor.queryObjectWithDBName(int.class, "test","select 1 from datebean1 where 1=0");
			SQLExecutor.deleteWithDBName("test", "delete from datebean1");
		} catch (SQLException e) {
			
			SQLExecutor.updateWithDBName("test", "CREATE TABLE datebean1(  utildate       TIMESTAMP,  sqldate        TIMESTAMP,  timestampdate  TIMESTAMP,  id             NUMBER)");
			
		}
		
		//mysql
		SQLUtil.startPool("testmysql","com.mysql.jdbc.Driver","jdbc:mysql://10.0.15.134:3306/bbosstest","root","123456",
				 "true",
				 null,//"READ_COMMITTED",
				"select 1",
				 "jndi-testmysql",   
				 2,
				 2,
				 2,
		   		false,
		   		false,
		   		null        ,true,false
		   		);
		try {
			SQLExecutor.queryObjectWithDBName(int.class, "testmysql","select 1 from datebean where 1=0");
			SQLExecutor.deleteWithDBName("testmysql", "delete from datebean");
		} catch (SQLException e) {
			
			SQLExecutor.updateWithDBName("testmysql", "CREATE TABLE datebean(  utildate       DATE,  sqldate        DATE,  timestampdate  DATE,  id             INTEGER)");
			
		}
		
		try {
			SQLExecutor.queryObjectWithDBName(int.class, "testmysql","select 1 from datebean1 where 1=0");
			SQLExecutor.deleteWithDBName("testmysql", "delete from datebean1");
		} catch (SQLException e) {
			
			SQLExecutor.updateWithDBName("testmysql", "CREATE TABLE datebean1(  utildate       TIMESTAMP,  sqldate        TIMESTAMP,  timestampdate  TIMESTAMP,  id             INTEGER)");
			
		}
		
		try {
			SQLExecutor.queryObjectWithDBName(int.class, "testmysql","select 1 from datebean2 where 1=0");
			SQLExecutor.deleteWithDBName("testmysql", "delete from datebean2");
		} catch (SQLException e) {
			
			SQLExecutor.updateWithDBName("testmysql", "CREATE TABLE datebean2(  utildate       datetime,  sqldate        datetime,  timestampdate  datetime,  id             INTEGER)");
			
		}
	}
	@Test
	public void testDate() throws SQLException
	{
		Date utildate = new Date();
		Date sqldate = new Date();
		Date timestampdate = new Date();
		
		SQLExecutor.insertWithDBName("test", "insert into datebean(id,utildate,sqldate,timestampdate) values(?,?,?,?)", 1,utildate,sqldate,timestampdate);
		SQLExecutor.insertWithDBName("test", "insert into datebean1(id,utildate,sqldate,timestampdate) values(?,?,?,?)", 1,utildate,sqldate,timestampdate);
		DateBean datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "test","select * from datebean where id=1");
		System.out.println("datebean:"+datebean);
		datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "test","select * from datebean1 where sqldate=?", sqldate);
		
		utildate = new Date();
		
		timestampdate = new Date();
		SQLExecutor.updateWithDBName("test", "update datebean set utildate=?,timestampdate=? where id=1", utildate,timestampdate);
		datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "test","select * from datebean where id=1");
		System.out.println("after update datebean:"+datebean);
		//修改，查询，map
		
	}
	
	@Test
	public void testmysqlDate() throws SQLException
	{
		Date utildate = new Date();
		Date sqldate = new Date();
		Date timestampdate = new Date();
		
		SQLExecutor.insertWithDBName("testmysql", "insert into datebean(id,utildate,sqldate,timestampdate) values(?,?,?,?)", 1,utildate,sqldate,timestampdate);
		SQLExecutor.insertWithDBName("testmysql", "insert into datebean1(id,utildate,sqldate,timestampdate) values(?,?,?,?)", 1,utildate,sqldate,timestampdate);
		SQLExecutor.insertWithDBName("testmysql", "insert into datebean2(id,utildate,sqldate,timestampdate) values(?,?,?,?)", 1,utildate,sqldate,timestampdate);
		DateBean datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "testmysql","select * from datebean where sqldate=?", sqldate);
		System.out.println("testmysql datebean:"+datebean);
		datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "testmysql","select * from datebean1 where sqldate=?", sqldate);
		System.out.println("testmysql datebean1:"+datebean);
		datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "testmysql","select * from datebean2 where sqldate=?", sqldate);
		System.out.println("testmysql datebean2:"+datebean);
		
		utildate = new Date();
//		sqldate = new Date();
		timestampdate = new Date();
		SQLExecutor.updateWithDBName("testmysql", "update datebean2 set utildate=?,timestampdate=? where sqldate=?", utildate,timestampdate,sqldate);
		datebean = SQLExecutor.queryObjectWithDBName(DateBean.class, "testmysql","select * from datebean2 where sqldate=?", sqldate);
		System.out.println("testmysql after update datebean2:"+datebean);
		//修改，查询，map
		
	}
	
	/**
	 * CREATE TABLE datebean
(
  utildate       DATE,
  sqldate        DATE,
  timestampdate  TIMESTAMP(7),
  id             NUMBER
);


ALTER TABLE datebean ADD (
  CONSTRAINT datebeanpk
 PRIMARY KEY
 (id));
	 * <p>Title: TestDate.java</p>
	 *
	 * <p>Description: </p>
	 *
	 * <p>Copyright: Copyright (c) 2007</p>
	 * @Date 2015年8月26日 下午4:30:23
	 * @author biaoping.yin
	 * @version 1.0
	 */
	public static class DateBean
	{
		private int id;
		private java.util.Date utildate;
		private java.sql.Date sqldate;
		private java.sql.Timestamp timestampdate;
		public java.util.Date getUtildate() {
			return utildate;
		}
		public void setUtildate(java.util.Date utildate) {
			this.utildate = utildate;
		}
		public java.sql.Date getSqldate() {
			return sqldate;
		}
		public void setSqldate(java.sql.Date sqldate) {
			this.sqldate = sqldate;
		}
		public java.sql.Timestamp getTimestampdate() {
			return timestampdate;
		}
		public void setTimestampdate(java.sql.Timestamp timestampdate) {
			this.timestampdate = timestampdate;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("id=").append(id).append(",").append("utildate=").append(utildate).append(",").append("sqldate=").append(sqldate).append(",").append("timestampdate=").append(timestampdate);
			return builder.toString();
		}
		
		
	}

}
