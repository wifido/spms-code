/***********************************************
 * Copyright sf-express.
 * All rights reserved.
 *
 * HISTORY
 **********************************************
 *  ID    DATE           PERSON       REASON
 *  1     2010-11-12      谢年兵        创建
 **********************************************/
package com.sf.module.common.action;

import static java.io.File.separator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import com.sf.framework.server.base.action.BaseAction;
import com.sf.module.common.util.IOUtil;
import com.sf.module.esbinterface.fileutil.FileUtil;
import com.sf.module.operation.util.CommonUtil;

/**
 * 报表文件公共下载Action
 *
 * @author 谢年兵 2010-11-12
 */
@Scope("prototype")
public class ReportFileDownloadAction extends BaseAction {
    private static final long serialVersionUID = 1L;
    private static Log logger = LogFactory
            .getLog(ReportFileDownloadAction.class);

    private String fileName;
    private String moduleName;
    private String entityName;
    private String uniquePart;
    private InputStream reportInputStream;
    

    public InputStream getReportInputStream() {
		return reportInputStream;
	}

	public void setReportInputStream(InputStream reportInputStream) {
		this.reportInputStream = reportInputStream;
	}

	/**
     * 下载文件，文件下载后自动删除
     *
     * @return
     */
    public String download() {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
            logger.info("获取下载文件的路径" + fileName);
            String filePath = CommonUtil.getGeneralReportSaveDir(entityName,
                    moduleName)
                    + File.separator
                    + uniquePart
                    + CommonUtil.FILE_NAME_SEPARATOR
                    + fileName;
            //兼容模板下载 update by 632898 李鹏 2014-07-16
            HttpServletRequest request = ServletActionContext.getRequest();
            if (!StringUtils.isEmpty(request.getParameter("isTemplate"))) {
                filePath = CommonUtil.getReportTemplatePath(fileName).replace("common", moduleName);
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return ERROR;
            }
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            reportInputStream = new ByteArrayInputStream(buffer);
            //兼容模板下载 update by 632898 李鹏  2014-07-16
            if (StringUtils.isEmpty(request.getParameter("isTemplate"))) {
                logger.info("delete " + entityName + " report file failure, file Path:" + filePath);
            }
            
            fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
        } catch (UnsupportedEncodingException ex) {
            logger.error("convert Chinese failure!", ex);
        } catch (Exception e) {
            logger.error("export report file failure!", e);
            return ERROR;
        } finally {
        	IOUtil.close(bufferedInputStream);
            IOUtil.close(fileInputStream);
        }
        return SUCCESS;
    }
    
    /**
     * 下载文件压缩
     *
     * @return
     */
    public String downloadZip() {
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
        	logger.info("获取下载文件转码前的路径" + fileName);
        	
            fileName = URLDecoder.decode(fileName, "UTF-8");
            String filePath = fileName;
            
            logger.info("获取下载文件转码后的路径" + fileName);
            //兼容模板下载 update by 632898 李鹏 2014-07-16
            HttpServletRequest request = ServletActionContext.getRequest();
            if (!StringUtils.isEmpty(request.getParameter("isTemplate"))) {
                filePath = CommonUtil.getReportTemplatePath(fileName).replace("common", moduleName);
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return ERROR;
            }
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(buffer);
            reportInputStream = new ByteArrayInputStream(buffer);
            fileName = new String(fileName.substring(fileName.lastIndexOf(separator) + 1).getBytes("GBK"), "ISO8859-1");
            logger.info("下载成功,文件全路径" + fileName);
        } catch (UnsupportedEncodingException ex) {
            logger.error("convert Chinese failure!", ex);
        } catch (Exception e) {
            logger.error("export report file failure!", e);
            return ERROR;
        } finally {
            IOUtil.close(fileInputStream);
            IOUtil.close(bufferedInputStream);
        }
        return SUCCESS;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUniquePart(String uniquePart) {
        this.uniquePart = uniquePart;
    }
}