/***********************************************
 * Copyright sf-express.
 * All rights reserved. 
 * 
 * HISTORY
 ********************************************************
 *  ID    DATE               PERSON             REASON
 *  1     2012-9-5           文俊 (337291)       创建 
 ********************************************************/
package com.sf.module.common.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
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
import java.util.Properties;
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
import com.sf.module.common.domain.CSVDomain;

/**
 * 
 * @author 文俊 (337291) 2012-9-5
 */

public abstract class OssBaseJdbcDao extends BaseJdbcDao {

    private class RowMapp<T> implements RowMapper<T> {
        /**
         * @author 文俊 (337291)
         * @date 2012-12-28
         */
        private Class<T> entityType;

        public RowMapp(Class<T> entityType) {
            this.entityType = entityType;
        }

        // public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        // T instance = null;
        // try {
        // instance = entityType.newInstance();
        // Field[] fields = entityType.getDeclaredFields();
        // for (Field field : fields) {
        // try {
        // entityType.getMethod(fieldNameToGetter(field.getName()));
        // } catch (java.lang.NoSuchMethodException e) {
        // continue;
        // }
        //
        // Object value = this.convertValueToRequiredType(rs,
        // this.fieldNameToColumnName(field.getName()), field.getType());
        // field.set(instance, value);
        // }
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
        // return instance;
        // }
        private Map<String, Field> fields;

        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            T instance = null;
            try {
                instance = entityType.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                if (fields == null) {
                    Field[] fields = entityType.getDeclaredFields();
                    this.fields = new HashMap<String, Field>(fields.length);
                    for (Field f : fields) {
                        this.fields.put(f.getName(), f);
                    }
                }
                for (int i = 1, len = metaData.getColumnCount(); i <= len; i++) {
                    // try {
                    // field =
                    // entityType.getDeclaredField(columnNameToFieldName(metaData.getColumnName(i)));
                    // } catch (NoSuchFieldException e) {
                    // continue;
                    // }
                    Field field = this.fields.get(columnNameToFieldName(metaData.getColumnName(i)));
                    if (field != null) {
                        field.setAccessible(true);
                        Object value = this.convertValueToRequiredType(rs, i, field.getType());
                        if (value != null) {
                            field.set(instance, value);
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

        // private String fieldNameToGetter(String input) {
        // StringBuilder sb = new StringBuilder("get");
        // Matcher matcher = getterPattern.matcher(input);
        // while (matcher.find()) {
        // String g1 = matcher.group(1);
        // String g2 = matcher.group(2);
        // sb.append(g1 == null ? "" : g1.toUpperCase()).append(g2 == null ? ""
        // : g2);
        // }
        // return sb.toString();
        // }

        // private Pattern getterPattern = Pattern.compile("^(.)(.*)");
        // private Pattern fieldNamePattern =
        // Pattern.compile("([^\\p{Upper}]+)(\\p{Upper}?)");
        private Pattern columnNamePattern = Pattern.compile("([^_]+)(_.)?");

        // private String fieldNameToColumnName(String input) {
        // StringBuilder sb = new StringBuilder();
        // Matcher matcher = fieldNamePattern.matcher(input);
        // while (matcher.find()) {
        // String g1 = matcher.group(1);
        // String g2 = matcher.group(2);
        // sb.append(g1 == null ? "" : g1.toUpperCase()).append(g2 == null ? ""
        // : g2.replace("_", ""));
        // }
        // return sb.toString();
        // }

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
     * 查询 将结果写入csv文件格式到path路径 pageSize万行分文件
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
   /* protected <T> String queryForCSVFile(String sql, Object[] args, String path, final String[] header, final Class<T> entityType, final int pageSize, final Object[] argsCSVDomain,
            final String fileName) {

        String r = null;
        ExecutorService threadPool = null;
        try {
            String name = UUID.randomUUID().toString();
            final File file = new File(path, name);
            if (!file.exists()) {
                file.mkdir();
            }

            StringBuilder totalSQL = this.getCountSql(sql);
            // 总记录数
            showSql(totalSQL.toString());
            final int totlCount = this.getJdbcTemplate().queryForInt(totalSQL.toString(), args);

            //long s = System.currentTimeMillis();
            if (totlCount > 0) {
                int len = (int) Math.ceil(totlCount / (pageSize * 1.0));
                
                //最多30万数据
                len = len > 5 ? 5 : len;
                
                StringBuilder paginationSQL = new StringBuilder();
                final Object dest[] = this.getPaginationSQL(sql, args, pageSize, 0, paginationSQL);
                final String paginationSQLStr = paginationSQL.toString();
                final StringBuffer error = new StringBuffer();
                List<Callable<String>> callables = new ArrayList<Callable<String>>();
                for (int i = 0; i < len; i++) {
                    final int pageIndex = i;
                    
                    callables.add(new Callable<String>() {
                        
                        private String name = fileName + (pageIndex + 1);
                        private int startIndex = pageIndex;
                        private Object[] params = dest.clone();
                        
                        
                        public String call() throws Exception {
                            RowMappWriterCSV<T> rmwc = null;
                            try {
                                rmwc = new RowMappWriterCSV<T>(file.getPath(), header, entityType, argsCSVDomain, this.name);                            
                                setPageIndex(params, pageSize, this.startIndex * pageSize);
                                List<T> list = getJdbcTemplate().query(replace2Orders(paginationSQLStr), params, rmwc);
                                
                                if (list != null && !list.isEmpty()) {
                                    for (int j = 0; j < list.size(); j++) {
                                        rmwc.write(list.get(j));
                                    }
                                }
                            } catch (Exception e) {
                                error.append(e);
                                e.printStackTrace();
                            } finally {
                                if (rmwc != null) {
                                    rmwc.close();
                                }
                            }
                            return "0";
                        }
                    });
                }
                //s = System.currentTimeMillis();
                threadPool = Executors.newCachedThreadPool();
                List<Future<String>> futures = threadPool.invokeAll(callables);

                if (futures == null || futures.size() != len || error.length() > 0) {
                    throw new RuntimeException("queryForCSVFile Failure " + error.toString());
                }

                //System.out.println("totoal : " + (System.currentTimeMillis() - s));
                //s = System.currentTimeMillis();
                File target = new File(path, name + ".zip");
                r = target.getPath();
                new ZipCompresser().compress(file.listFiles(), target);
                //System.out.println("compress : " + (System.currentTimeMillis() - s));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (threadPool != null) {
                threadPool.shutdown();
            }
        }
        return r;
    }*/

    /**
     * 查询 将结果写入csv文件格式到path路径 6万行分文件
     * 
     * @author 文俊 (337291)
     * @date May 27, 2013
     * @param <T>
     * @param sql
     * @param args
     * @param path
     * @param header
     * @param entityType
     * @param argsCSVDomain
     * @return
     */
    /*protected <T> String queryForCSVFile(String sql, Object[] args, String path, String[] header, Class<T> entityType, Object[] argsCSVDomain, String fileName) {
        return this.queryForCSVFile(sql, args, path, header, entityType, 60000, argsCSVDomain, fileName);
    }*/

    /**
     * 查询 将结果写入csv文件格式到path路径 6万行分文件
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
     * @return
     */
    /*protected <T> String queryForCSVFile(String sql, Object[] args, String path, String[] header, Class<T> entityType, int pageSize, String fileName) {
        return this.queryForCSVFile(sql, args, path, header, entityType, pageSize, null, fileName);
    }*/

    /**
     * 查询 将结果写入csv文件格式到path路径 6万行分文件
     * 
     * @author 文俊 (337291)
     * @date May 27, 2013
     * @param <T>
     * @param sql
     * @param args
     * @param path
     * @param header
     * @param entityType
     * @return
     */
    /*protected <T> String queryForCSVFile(String sql, Object[] args, String path, String[] header, Class<T> entityType) {
        return this.queryForCSVFile(sql, args, path, header, entityType, null, null);
    }*/

    private class RowMappWriterCSV<T> implements RowMapper<T> {
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

        public RowMappWriterCSV(String path, String[] header, Class<T> entityType, /*
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
                out = new FileOutputStream(file);
                osw = new OutputStreamWriter(out, enc);
                bw = new BufferedWriter(osw);
                bw.write(header[0]);
                for (int i = 1; i < header.length; i++) {
                    bw.write(sep);
                    bw.write(header[i]);
                }
                bw.newLine();
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

        public void write(Object a) throws Exception {
            if (a != null) {
                CSVDomain o = (CSVDomain) a;
                bw.write(o.toCSVString(this.argsCSVDomain));
                bw.newLine();
                bw.flush();
            }
        }
    };

    /**
     * SQL 配置文件
     */
    private Properties properties = null;




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

    private Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);

    /**
     * 去除orderby 子句
     */
    protected String removeOrders(String sql) {
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static Pattern orderby = Pattern.compile("\\$\\{sort\\}", Pattern.CASE_INSENSITIVE);

    /**
     * 替换[orderby] to order by 子句
     */
    protected static String replace2Orders(String sql) {
        Matcher m = orderby.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, " ORDER BY ");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private StringBuilder getCountSql(String sql) {
        return new StringBuilder("SELECT COUNT(1) FROM (").append(replace2Orders(removeOrders(sql))).append(") totalTable");
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
        int totalRows = this.getJdbcTemplate().queryForInt(totalSQL.toString(), args);
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
        int totlCount = this.getJdbcTemplate().queryForInt(totalSQL.toString(), args);
        if (totlCount >= pageSize) {
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
