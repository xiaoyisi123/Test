package com.yc.httpserver.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/test")
public class TestServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4366498092206415193L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		switch(request.getMethod()) {
		case "GET" : doGet(request, response); break;
		case "POST" : doPost(request, response); break;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		
		System.out.println("id:" + id + ", name:" + name);
		
		PrintWriter out = response.getWriter();
		out.print("success");
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String id = request.getParameter("id");
		//String name = request.getParameter("name");
		int uploadTotalSize = request.getContentLength();
		byte[] saveUploadArray = new byte[uploadTotalSize];
		
		int readLength = 0;
		int totalRead = 0;
		
		ServletInputStream sis = request.getInputStream();
		for(totalRead = 0; totalRead < uploadTotalSize; totalRead += readLength) {
			readLength = sis.read(saveUploadArray, totalRead, uploadTotalSize - totalRead);
		}
		
		int currentIndex = 0;
		int startData = 0;
		int endData = 0;
		int size = 0;
		byte[] bt = null;
		int flag = 0;
		
		for(; currentIndex < uploadTotalSize; currentIndex ++) {
			if(saveUploadArray[currentIndex] == 13) {
				endData = currentIndex;
				size = endData - startData;
				
				bt = new byte[size];
				
				System.arraycopy(saveUploadArray, startData, bt, 0, size);
				System.out.println(URLDecoder.decode(new String(bt), "utf-8"));
				
				startData = currentIndex + 2;
				flag ++;
				
				if(flag == 2) {
					break;
				}
			}
		}
		endData = uploadTotalSize;
		size = endData - startData;
		bt = new byte[size];
		System.arraycopy(saveUploadArray, startData, bt, 0, size);
		
		
		FileOutputStream fos = new FileOutputStream(new File("D:\\"));
		fos.write(bt);
		fos.flush();
		fos.close();
		
		PrintWriter out = response.getWriter();
		out.print("success");
		out.flush();
	}
}
