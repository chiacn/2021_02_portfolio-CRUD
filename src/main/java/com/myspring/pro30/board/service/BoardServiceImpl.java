package com.myspring.pro30.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;


@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl  implements BoardService{
	@Autowired
	BoardDAO boardDAO;
	
	/* 페이징 적용 이전, 기존 listArticles
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList();
        return articlesList;
	}
	*/ 
	//인터페이스에서 listArticles 바뀐 타입 수정해줘야함.
	public Map listArticles(Map pagingMap) throws Exception {
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList= boardDAO.selectAllArticlesList(pagingMap);
		int totArticles = boardDAO.selectTotArticles(); //테이블에 존재하는 전체 글 수
		
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		return articlesMap;		
	}

	
	//단일 이미지 추가하기
	/*
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		return boardDAO.insertNewArticle(articleMap);
	}
	*/
	
	 //다중 이미지 추가하기
	
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap);
		articleMap.put("articleNO", articleNO);
		
		// if 문으로 imageFileName 있어야 작동하게 
		if(articleMap.get("imageFileList") != null) {
		      boardDAO.insertNewImage(articleMap);
		}
		      return articleNO;
		
	}
	
	
	//다중 파일 보이기
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		articleMap.put("article", articleVO);  // articleVO도 바인딩해주고 imageFileList도 바인딩해줌
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}
   
	
	/*
	 //단일 파일 보이기
	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception {
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		return articleVO;
	}
	*/
	
	
	@Override
	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateArticle(articleMap);
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);
	}
	
	//임의의 테스트 글 추가 코드
	@Override
	public void addNewTestArticle(int number) throws Exception {
		boardDAO.addNewTestArticle(number);
	}
	//임의의 테스트 글 삭제 코드
	@Override
	public void deleteTestArticle(int deleteNumber) throws Exception {
		boardDAO.deleteTestArticle(deleteNumber);
	}

	
}
