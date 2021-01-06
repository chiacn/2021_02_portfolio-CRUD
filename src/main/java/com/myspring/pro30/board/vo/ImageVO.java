package com.myspring.pro30.board.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;

public class ImageVO {
	private int imageFileNO;
	private String imageFileName;
	private Date regDate;
	private int articleNO;	
	//추가
	private String originalFileName;
	
	
	
	public int getImageFileNO() {
		return imageFileNO;
	}
	public void setImageFileNO(int imageFileNO) {
		this.imageFileNO = imageFileNO;
	}
	public String getImageFileName() {
		try {
			if(imageFileName != null && imageFileName.length() != 0) {
				imageFileName = URLDecoder.decode(imageFileName,"UTF-8");
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		try {
			if(imageFileName!= null && imageFileName.length()!=0) {
				this.imageFileName = URLEncoder.encode(imageFileName,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public int getArticleNO() {
		return articleNO;
	}
	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}
	
	//orginalFileName 저장 부분 추가 
	public String getOriginalFileName() {
		try {
			if(originalFileName != null && originalFileName.length() != 0) {
				originalFileName = URLDecoder.decode(originalFileName,"UTF-8");
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		try {
			if(originalFileName!= null && originalFileName.length()!=0) {
				this.originalFileName = URLEncoder.encode(originalFileName,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	
	}
	
   
	

}
