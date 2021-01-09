package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;


public interface BoardDAO {
	//public List selectAllArticlesList() throws DataAccessException;
	public List selectAllArticlesList(Map<String, Integer> pagingMap) throws DataAccessException;
	
	public int insertNewArticle(Map articleMap) throws DataAccessException;
	public void insertNewImage(Map articleMap) throws DataAccessException;
	
	public ArticleVO selectArticle(int articleNO) throws DataAccessException;
	public void updateArticle(Map articleMap) throws DataAccessException;
	public void deleteArticle(int articleNO) throws DataAccessException;
	public List selectImageFileList(int articleNO) throws DataAccessException;
	
	//페이징 관련 추가
	public int selectTotArticles();
	
	//임의의 테스트 글 추가 코드 
	public void addNewTestArticle(int number) throws DataAccessException;
	
	//임의의 테스트 글 삭제 코드
	public void deleteTestArticle(int deletNumber) throws DataAccessException;
	
}
