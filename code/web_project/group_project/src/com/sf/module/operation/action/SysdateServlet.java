package com.sf.module.operation.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sf.module.operation.biz.ISysdateBiz;

/**
 * 
 */
public class SysdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
         WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());   
         ISysdateBiz dt = (ISysdateBiz) ctx.getBean("sysdateBiz");
		 PrintWriter out=response.getWriter();
		 out.print(dt.getSysTime());
		 out.flush();
		 out.close();
	}

	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
   
}