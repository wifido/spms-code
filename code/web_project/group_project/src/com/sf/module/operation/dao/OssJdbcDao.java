package com.sf.module.operation.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.jdbc.driver.OracleTypes;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import com.sf.framework.base.IPage;
import com.sf.framework.core.Page;
import com.sf.framework.server.base.dao.BaseJdbcDao;
import com.sf.module.operation.domain.CSVDomain;

public class OssJdbcDao extends BaseJdbcDao {

    private class RowMapp<T> implements RowMapper<T> {
        /**
         * @author 文俊 (337291)
         * @date 2012-12-28
         */
        private Class<T> entityType;

        public RowMapp(Class<T> entityType) {
            this.entityType = entityType;
        }

        private Map<String, PropertyDescriptor> fields;

        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            T instance = null;
            try {
                instance = entityType.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                if (fields == null) {
                	
        			BeanInfo beanInfo = Introspector.getBeanInfo(entityType);
        			PropertyDescriptor[] pd = beanInfo.getPropertyDescriptors();
                	
                    this.fields = new HashMap<String, PropertyDescriptor>(pd.length);
                    for (PropertyDescriptor f : pd) {
                    	if (f != null && f.getWriteMethod() != null) {
                    		this.fields.put(f.getName(), f);
                    	}
                    }
                }
                for (int i = 1, len = metaData.getColumnCount(); i <= len; i++) {
                    // try {
                    // field =
                    // entityType.getDeclaredField(columnNameToFieldName(metaData.getColumnName(i)));
                    // } catch (NoSuchFieldException e) {
                    // continue;
                    // }
                	PropertyDescriptor field = this.fields.get(columnNameToFieldName(metaData.getColumnName(i)));
                    if (field != null) {
                        Object value = this.convertValueToRequiredType(rs, i, field.getPropertyType());
                        if (value != null) {
                            field.getWriteMethod().invoke(instance, value);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return instance;
        }

        private Object convertValueToRequiredType(ResultSet rs, int i, Class<?> requiredType) throws Exception {
            Object result = rs.getObject(i);
            if (result == null) {
                return null;
            }
            if (String.class.equals(requiredType)) {
                result = result.toString();
            } else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
                result = rs.getInt(i);
            } else if (double.class.equals(requiredType) || Double.class.equals(requiredType)) {
                result = rs.getDouble(i);
            } else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
                result = rs.getLong(i);
            } else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
                result = rs.getFloat(i);
            } else if (Date.class.equals(requiredType)) {
                Timestamp timestamp = rs.getTimestamp(i);
                if (timestamp != null) {
                    result = new Date(timestamp.getTime());
                }
            }
            return result;
        }

        private Pattern columnNamePattern = Pattern.compile("([^_]+)(_.)?");


        private String columnNameToFieldName(String input) {
            Matcher matcher = columnNamePattern.matcher(input);
            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                String g1 = matcher.group(1);
                String g2 = matcher.group(2);
                sb.append(g1 == null ? "" : g1.toLowerCase()).append(g2 == null ? "" : g2.replace("_", ""));
            }
            return sb.toString();
        }
    };



    /**
     * 验证字符串是否有较
     * 
     * @author 文俊 (337291)
     * @date 2012-8-28
     * @param str
     * @return
     */
    protected boolean isValidStr(String str) {
        return !(str == null || "".equals(str.trim()));
    }

    private String oldChar      = "{0}";
    private String likeAnyWhere = "%{0}%";
    private String likeEnd      = "{0}%";
    private String likeStart    = "%{0}";
    private String regex        = "(%|_)";
    private String replacement  = "\\\\$1";

    private String likeReplace(String like, String value) {
        if (value == null) {
            return "";
        }
        return like.replace(oldChar, value.replaceAll(regex, replacement));
    }

    /**
     * '%{value}%'
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected String likeValue(String value) {
        return this.likeReplace(likeAnyWhere, value);
    }

    /**
     * '{value}%'
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected String likeEndValue(String value) {
        return this.likeReplace(likeEnd, value);
    }

    /**
     * '%{value}'
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected String likeStartValue(String value) {
        return this.likeReplace(likeStart, value);
    }

    /**
     * java.util.Date converTo java.sql.Date
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected java.sql.Date converDate(Date value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Date(value.getTime());
    }

    /**
     * java.util.Date converTo java.sql.Time
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected java.sql.Time converTime(Date value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Time(value.getTime());
    }

    /**
     * java.util.Date converTo java.sql.Timestamp
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param value
     * @return
     */
    protected java.sql.Timestamp converTimestamp(Date value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Timestamp(value.getTime());
    }


    private String where = " WHERE 1=1 ";

    protected void where(StringBuilder sql) {
        if (sql.indexOf(where) == -1) {
            sql.append(where);
        }
    }


    /**
     * 去除orderby 子句
     */
    protected String removeOrders(String sql) {
    	Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }


    /**
     * 替换[orderby] to order by 子句
     */
    protected static String replace2Orders(String sql) {
    	Pattern orderby = Pattern.compile("\\$\\{sort\\}", Pattern.CASE_INSENSITIVE);
        Matcher m = orderby.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, " ORDER BY ");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private StringBuilder getCountSql(String sql) {
        return new StringBuilder("SELECT COUNT(1) FROM (").append(sql/*replace2Orders(removeOrders(sql))*/).append(") totalTable");
    }
    

    /**
     * 分页查询 注意：语句结尾不要有空格字符。并且order by 字句一定放到最后。
     * 
     * @author 文俊 (337291)
     * @date 2012-8-28
     * @param <T>
     * @param sql
     * @param args
     * @param pageSize
     * @param pageIndex
     * @param elementType
     * @return
     */
    protected <T> IPage<T> fetchPage(String sql, Object args[], int pageSize, int pageIndex, Class<T> elementType) {
    	return fetchPage(new StringBuilder(sql), args, pageSize, pageIndex, elementType);
    }
    

    /**
     * 分页查询 注意：语句结尾不要有空格字符。并且order by 字句一定放到最后。
     * 
     * @author 文俊 (337291)
     * @date 2012-8-28
     * @param <T>
     * @param sql
     * @param args
     * @param pageSize
     * @param pageIndex
     * @param elementType
     * @return
     */
    protected <T> IPage<T> fetchPage(StringBuilder sql, Object args[], int pageSize, int pageIndex, Class<T> elementType) {

        StringBuilder totalSQL = this.getCountSql(sql.toString());
        // 给JdbcTemplate赋值
        // 总记录数
        showSql(totalSQL.toString());
        Integer _totalRows = this.getJdbcTemplate().queryForObject(totalSQL.toString(), args, Integer.class);
        int totalRows = _totalRows == null ? 0 : _totalRows.intValue();
        IPage<T> page = new Page<T>(new ArrayList<T>(pageSize), totalRows, pageSize, pageIndex / pageSize);
        if (totalRows == 0) {
            return page;
        }

        // 装入结果集
        page.addAll(this.queryForList(sql.toString(), args, pageSize, pageIndex, elementType));
        return page;
    }

    /**
     * call分页存储过程 最后两个必须 输出参数,P_OUT_TOTAL_SIZE OUT NUMBER,P_OUT_CURSOR OUT
     * CURSOR_TYPE
     * 
     * @author 文俊 (337291)
     * @date 2012-12-31
     * @param <T>
     * @param procedureName
     * @param args
     * @param entityType
     * @param pageSize
     * @param pageIndex
     * @return
     */
    protected <T> IPage<T> callSTPOut(String procedureName, final List<Object> args, final Class<T> entityType, final int pageSize, final int pageIndex) {
        return super.getJdbcTemplate().execute(procedureName, new CallableStatementCallback<IPage<T>>() {

            public IPage<T> doInCallableStatement(CallableStatement call) throws SQLException, DataAccessException {
                int i = 1;
                for (Object o : args) {
                    call.setObject(i++, o);
                }
                int totalSizeIndex = i;
                call.registerOutParameter(totalSizeIndex, OracleTypes.NUMBER);
                int resetSetIndex = i + 1;
                call.registerOutParameter(resetSetIndex, OracleTypes.CURSOR);
                call.execute();
                IPage<T> page = new Page<T>(new ArrayList<T>(pageSize), call.getLong(totalSizeIndex), pageSize, pageIndex / pageSize);
                if (page.getTotalSize() == 0) {
                    return page;
                }
                ResultSet rs = (ResultSet) call.getObject(resetSetIndex);
                RowMapp<T> rowMapp = new RowMapp<T>(entityType);

                i = 0;
                while (rs.next()) {
                    page.add((T) rowMapp.mapRow(rs, i++));
                }
                rs.close();
                return page;
            }
        });
    }

    private Object[] getPaginationSQL(String sql, Object[] args, int pageSize, int pageIndex, StringBuilder paginationSQL) {
        paginationSQL.append("SELECT * FROM (SELECT temp.*,ROWNUM num_ FROM (");
        paginationSQL.append(sql).append(") temp WHERE ROWNUM <= ?) WHERE num_ > ?");
        Object dest[] = new Object[args.length + 2];
        System.arraycopy(args, 0, dest, 0, args.length);
        setPageIndex(dest, pageSize, pageIndex);
        return dest;
    }

    private Object[] setPageIndex(Object[] dest, int pageSize, int pageIndex) {
        dest[dest.length - 1] = Integer.valueOf(pageIndex);
        dest[dest.length - 2] = Integer.valueOf(pageIndex + pageSize);
        return dest;
    }

    /**
     * 分页查询
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param <T>
     * @param sql
     * @param args
     * @param pageSize
     * @param pageIndex
     * @param entityType
     * @return
     */
    protected <T> List<T> queryForList(String sql, Object[] args, int pageSize, int pageIndex, Class<T> entityType) {
        // 构造oracle数据库的分页语句
        StringBuilder paginationSQL = new StringBuilder(/* "SELECT * FROM (SELECT temp.*,ROWNUM num_ FROM (" */);
        // paginationSQL.append(sql).append(") temp WHERE ROWNUM <= ?) WHERE num_ > ?");
        Object dest[] = this.getPaginationSQL(sql, args, pageSize, pageIndex, paginationSQL);// new
                                                                                             // Object[args.length
                                                                                             // +
                                                                                             // 2];
        // System.arraycopy(args, 0, dest, 0, args.length);
        // dest[dest.length - 1] = Integer.valueOf(pageIndex);
        // dest[dest.length - 2] = Integer.valueOf(pageIndex + pageSize);
        return this.queryForList(paginationSQL.toString(), dest, entityType);
    }

    private void showSql(String sql) {
        System.out.print("Spring JdbcTemplate: ");
        System.out.println(sql);
        logger.debug("Spring JdbcTemplate: " + sql);
    }

    /**
     * 查询
     * 
     * @author 文俊 (337291)
     * @date 2012-10-16
     * @param <T>
     * @param sql
     * @param args
     * @param entityType
     * @return
     */
    protected <T> List<T> queryForList(String sql, Object[] args, final Class<T> entityType) {
        showSql(sql);
        return super.getJdbcTemplate().query(replace2Orders(sql), args, new RowMapp<T>(entityType));
    }
    
    public class OutEntity extends OracleTypes {
        
        private String parameterName;
        private int paramIndex;
        private int sqlType;
        private Object value;
        
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param parameterName
         * @param sqlType
         * @param value
         */
        public OutEntity(String parameterName, int sqlType) {
            super();
            this.parameterName = parameterName;
            this.sqlType = sqlType;
        }
        
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param parameterName
         * @param sqlType
         * @param value
         */
        public OutEntity(int paramIndex, int sqlType) {
            super();
            this.paramIndex = paramIndex;
            this.sqlType = sqlType;
        }
        
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @return the parameterName
         */
        public String getParameterName() {
            return parameterName;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param to set parameterName the parameterName 
         */
        public void setParameterName(String parameterName) {
            this.parameterName = parameterName;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @return the sqlType
         */
        public int getSqlType() {
            return sqlType;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param to set sqlType the sqlType 
         */
        public void setSqlType(int sqlType) {
            this.sqlType = sqlType;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @return the value
         */
        public Object getValue() {
            return value;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param to set value the value 
         */
        public void setValue(Object value) {
            this.value = value;
        }
        
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @return the paramIndex
         */
        public int getParamIndex() {
            return paramIndex;
        }
        /**
         * @author 文俊 (337291)
         * @date Jun 28, 2013 
         * @param to set paramIndex the paramIndex 
         */
        public void setParamIndex(int paramIndex) {
            this.paramIndex = paramIndex;
        }
        
    }
    
    /**
     * call分页存储过程 最后两个必须 输出参数,P_OUT_TOTAL_SIZE OUT NUMBER,P_OUT_CURSOR OUT
     * CURSOR_TYPE
     * 
     * @author 文俊 (337291)
     * @date 2012-12-31
     * @param <T>
     * @param procedureName
     * @param args
     * @param entityType
     * @param pageSize
     * @param pageIndex
     * @return
     */
    protected void callSTPOut(String procedureName, final List<Object> args, final OutEntity[] parameterNames) {
        showSql(procedureName);
        super.getJdbcTemplate().execute(procedureName, new CallableStatementCallback<Object>() {

            public Object doInCallableStatement(CallableStatement call) throws SQLException, DataAccessException {
                if (args != null && !args.isEmpty()) {
                    int i = 1;
                    for (Iterator<Object> it = args.iterator();it.hasNext();) {
                        call.setObject(i++, it.next());
                    }
                }
                
                if (parameterNames != null && parameterNames.length > 0) {
                    for (int i=0;i<parameterNames.length;i++) {
                        if (parameterNames[i].parameterName == null) {
                            call.registerOutParameter(parameterNames[i].paramIndex, parameterNames[i].sqlType);
                        } else {
                            call.registerOutParameter(parameterNames[i].parameterName, parameterNames[i].sqlType);
                        }
                    }
                }
                call.execute();
                
                if (parameterNames != null && parameterNames.length > 0) {
                    for (int i=0;i<parameterNames.length;i++) {
                        if (parameterNames[i].parameterName == null) {
                            parameterNames[i].value = call.getObject(parameterNames[i].paramIndex);
                        } else {
                            parameterNames[i].value = call.getObject(parameterNames[i].parameterName);
                        }
                    }
                }
                
                return null;
            }
        });
    }

    // private Map<String,Field> fieldsMap;
    // private Pattern columnNamePattern = Pattern.compile("([^_]+)(_.)?");
    
    
    private class RowWriterCSV<T> implements RowMapper<T> {
        /**
         * @author 文俊 (337291)
         * @date 2012-12-28
         */
        // private OutputStream out;
        // private List<File> files;
        private String             path;
        private String[]           header;
        private FileOutputStream   out    = null;
        private OutputStreamWriter osw    = null;
        private BufferedWriter     bw     = null;
        // private int fileCount = 1;
        private String             sep    = ",";
        private String             suffix = ".csv";
        private String             enc    = "GBK";
        private RowMapp<T>         rm     = null;
        private Object[]           argsCSVDomain;
        // private int pageSize;
        private String             fileName;
        private String 			   filePath;

        public String getFilePath() {
			return filePath;
		}

		public RowWriterCSV(String path, String[] header, Class<T> entityType, /*
                                                                                    * int
                                                                                    * pageSize
                                                                                    * ,
                                                                                    */Object[] argsCSVDomain, String fileName) {
            this.path = path;
            this.header = header;
            // files = new ArrayList<File>();
            this.argsCSVDomain = argsCSVDomain;
            // this.pageSize = pageSize;
            rm = new RowMapp<T>(entityType);
            this.fileName = fileName;
            init();
        }

        public void init() {
            try {
                File file = new File(path, fileName + suffix);
                this.filePath = file.getCanonicalPath();
                out = new FileOutputStream(file);
                osw = new OutputStreamWriter(out, enc);
                bw = new BufferedWriter(osw);
                bw.write(header[0]);
                for (int i = 1; i < header.length; i++) {
                    bw.write(sep);
                    bw.write(header[i]);
                }
                bw.newLine();
                bw.flush();
                // files.add(file);
            } catch (Exception e) {
                close();
                throw new RuntimeException(e);
            }
        }

        /**
         * @author 文俊 (337291)
         * @date May 25, 2013
         * @return the files
         */
        // public List<File> getFiles() {
        // return files;
        // }

        public void close() {

            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    bw = null;
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    osw = null;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    out = null;
                }
            }

        }

        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                write(this.rm.mapRow(rs, rowNum));
            } catch (Exception e) {
                logger.error("mapRow", e);
                throw new RuntimeException(e);
            }
            return null;
        }

        private void write(Object a) throws Exception {
            if (a != null) {
                CSVDomain o = (CSVDomain) a;
                bw.write(o.toCSVString(this.argsCSVDomain));
                bw.newLine();
                bw.flush();
            }
        }
    };

    private String outOfMaxPageSize = "outOfMaxPageSize";
    
    
    public String getOutOfMaxPageSize() {
		return outOfMaxPageSize;
	}

	/**
     * 查询 将结果写入csv文件格式到path路径 最大pageSize万行
     * 
     * @author 文俊 (337291)
     * @date May 27, 2013
     * @param <T>
     * @param sql
     * @param args
     * @param path
     * @param header
     * @param entityType
     * @param pageSize
     * @param argsCSVDomain
     * @return
     */
    protected <T> String queryForRowCSV(String sql, Object[] args, String path, final String[] header, final Class<T> entityType, final int pageSize, final int pageIndex, final Object[] argsCSVDomain ) {

    	
    	

        StringBuilder totalSQL = this.getCountSql(sql);
        // 总记录数
        showSql(totalSQL.toString());
        Integer totlCount = this.getJdbcTemplate().queryForObject(totalSQL.toString(), args, Integer.class);
        if (totlCount != null && totlCount.intValue() >= pageSize) {
        	return outOfMaxPageSize;
        }
        
        String r = null;

        String name = UUID.randomUUID().toString();

        StringBuilder paginationSQL = new StringBuilder();
        Object dest[] = this.getPaginationSQL(sql, args, pageSize, pageIndex, paginationSQL);


        RowWriterCSV<T> rmwc = null;
         try {
             rmwc = new RowWriterCSV<T>(path, header, entityType, argsCSVDomain, name);                            
            
             getJdbcTemplate().query(replace2Orders(paginationSQL.toString()), dest, rmwc);
             r = rmwc.getFilePath();     
         } catch (Exception e) {
             e.printStackTrace();
             logger.error("queryForRowCSV", e);
             throw new RuntimeException(e);
         } finally {
             if (rmwc != null) {
                 rmwc.close();
             }
         }
        
        return r;
    }
}
