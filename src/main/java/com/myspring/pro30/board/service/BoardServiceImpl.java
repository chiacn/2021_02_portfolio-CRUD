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
	
	/* ����¡ ���� ����, ���� listArticles
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList();
        return articlesList;
	}
	*/ 
	//�������̽����� listArticles �ٲ� Ÿ�� �����������.
	public Map listArticles(Map pagingMap) throws Exception {
		Map articlesMap = new HashMap();
		List<ArticleVO> articlesList= boardDAO.selectAllArticlesList(pagingMap);
		int totArticles = boardDAO.selectTotArticles(); //���̺� �����ϴ� ��ü �� ��
		
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		return articlesMap;		
	}

	
	//���� �̹��� �߰��ϱ�
	/*
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		return boardDAO.insertNewArticle(articleMap);
	}
	*/
	
	 //���� �̹��� �߰��ϱ�
	
	@Override
	public int addNewArticle(Map articleMap) throws Exception{
		int articleNO = boardDAO.insertNewArticle(articleMap);
		articleMap.put("articleNO", articleNO);
		
		// if ������ imageFileName �־�� �۵��ϰ� 
		if(articleMap.get("imageFileList") != null) {
		      boardDAO.insertNewImage(articleMap);
		}
		      return articleNO;
		
	}
	
	
	//���� ���� ���̱�
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		articleMap.put("article", articleVO);  // articleVO�� ���ε����ְ� imageFileList�� ���ε�����
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}
   
	
	/*
	 //���� ���� ���̱�
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
	
	//������ �׽�Ʈ �� �߰� �ڵ�
	@Override
	public void addNewTestArticle(int number) throws Exception {
		boardDAO.addNewTestArticle(number);
	}
	//������ �׽�Ʈ �� ���� �ڵ�
	@Override
	public void deleteTestArticle(int deleteNumber) throws Exception {
		boardDAO.deleteTestArticle(deleteNumber);
	}

	
}
