package com.ejt.mock.servlet;

import com.ejt.mock.MockCallable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MockJspPage implements javax.servlet.jsp.HttpJspPage {

    private MockCallable mockCallable;

    public MockJspPage(MockCallable mockCallable) {
        this.mockCallable = mockCallable;
    }

    @Override
    public void _jspService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        new MockServlet(mockCallable).service(httpServletRequest, httpServletResponse);
    }

    @Override
    public void jspInit() {
    }

    @Override
    public void jspDestroy() {
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }

}
