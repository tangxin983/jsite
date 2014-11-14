package com.github.tx.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpHelper {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private FTPClient ftp;

	private static FtpHelper instance;

	private String host;

	private int port;

	private String username;

	private String password;
	
	 
	private FtpHelper(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**
	 * 返回实例
	 * @return
	 */
	public static FtpHelper getInstance(String host, int port, String username, String password) {
		if (instance == null)
			instance = new FtpHelper(host, port, username, password);
		return instance;
	}

	/**
	 * 连接FTP
	 * 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	private boolean connect() throws SocketException, IOException {
		ftp.connect(host, port);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			logger.info("refused connection.");
			return false;
		}
		return true;
	}

	/**
	 * 登录FTP
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean login() throws IOException {
		if (!ftp.login(username, password)) {
			ftp.logout();
			logger.info("login fail.");
			return false;
		}
		return true;
	}

	/**
	 * 连接并登录FTP，并设置文件传输方式
	 * 
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public boolean ready() throws SocketException, IOException {
		if (connect() && login()) {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setControlEncoding("UTF-8");
			return true;
		}
		return false;
	}

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (ftp.isConnected()) {
			ftp.logout();
			ftp.disconnect();
		}
	}

	/**
	 * 
	 * @param fileName
	 *            上传文件的绝对路径
	 * @param ftpPath
	 *            保存的ftp路径(以/开头的绝对路径,如果是""说明路径为根目录)
	 * @param newFileName
	 *            ftp上保存的文件名
	 * @return 是否成功
	 */
	public boolean uploadFile(String fileName, String ftpPath, String newFileName) {
		boolean success = false;
		FileInputStream fis = null;
		try {
			if (ready()) {
				//改变工作目录，如果不存在则创建目录
				if (!ftp.changeWorkingDirectory(ftpPath)) {
					if (ftp.makeDirectory(ftpPath)) {
						ftp.changeWorkingDirectory(ftpPath);
					}else{
						throw new Exception("create remote path fail.");
					}
				}
				fis = new FileInputStream(fileName);
				if (ftp.storeFile(newFileName, fis)) {
					logger.info("upload file:" + fileName + " success.");
					success = true;
				}
			}
		} catch (Exception e) {
			logger.error("upload file:" + fileName + " fail,reason:" + e.getMessage());
		} finally {
			try {
				close();
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				logger.error("close fail,reason:" + e.getMessage());
			}
		}
		return success;
	}

	/**
	 * 
	 * 删除文件
	 * @param path 文件所在ftp目录（以/开头的绝对路径）
	 * @param name 文件名
	 * @return 是否成功
	 */
	public boolean removeFile(String path, String name) {
		boolean success = false;
		try {
			if(ready()){
				if(!ftp.changeWorkingDirectory(path)){
					throw new Exception("directory doesnt exist.");
				}
				if (ftp.deleteFile(name)) {
					logger.info("remove file:" + name + " success.");
					success = true;
				}
			}
		} catch (Exception e) {
			logger.error("remove file:" + name + " fail, reason:" + e.getMessage());
		} finally {
			try {
				close();
			} catch (IOException e) {
				logger.error("close fail,reason:" + e.getMessage());
			}
		}
		return success;
	}
	
	/**
	 * 
	 * 下载文件
	 * @param downloadFileName 目标文件绝对路径
	 * @param path 下载文件所在ftp目录（以/开头的绝对路径）
	 * @param name 下载文件名
	 * @return 是否成功
	 */
	public boolean downloadFile(String downloadFileName, String path, String name) {
		boolean success = false;
		FileOutputStream out = null;
		InputStream input = null;
		try {
			if(ready()){
				if(!ftp.changeWorkingDirectory(path)){
					throw new Exception("directory doesnt exist.");
				}
				out = new FileOutputStream(downloadFileName);
				input = ftp.retrieveFileStream(name);
				int buf = -1;
				while ((buf = input.read()) != -1) {
					out.write(buf);
				}
				out.flush();
				logger.info("download file:" + name + " success.");
			}
		} catch (Exception e) {
			logger.error("remove file:" + name + " fail, reason:" + e.getMessage());
		} finally {
			try {
				close();
				if(out != null){
					out.close();
				}
				if(input != null){
					input.close();
				}
			} catch (IOException e) {
				logger.error("close fail,reason:" + e.getMessage());
			}
		}
		return success;
	}
	
}
