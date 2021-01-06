package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

import java.util.Date;


@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articlesList = articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

	
	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle",articleMap);
		return articleNO;
	}
    
	//다중 파일 업로드
	
	@Override
	public void insertNewImage(Map articleMap) throws DataAccessException {
		List<ImageVO> imageFileList = (ArrayList)articleMap.get("imageFileList");
		int articleNO = (Integer)articleMap.get("articleNO");
		int imageFileNO = selectNewImageFileNO();
		for(ImageVO imageVO : imageFileList){
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		sqlSession.insert("mapper.board.insertNewImage",imageFileList);
	}
	
   
	
	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}

	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
		sqlSession.update("mapper.board.updateArticle", articleMap);
		
		//추가
		System.out.println(articleMap);
		
		//디버깅 
		//articleNO가 안 담긴다
		/*
				System.out.println(articleMap);
				List<ImageVO> imageFileList = (List) articleMap.get("imageFileList");
				for(ImageVO imageVO : imageFileList) {
					String imageFileName = imageVO.getImageFileName();
					int articleNO = imageVO.getArticleNO();
					int imageFileNO = imageVO.getImageFileNO();
					Date regDate =  imageVO.getRegDate();
					
			
					
					System.out.println(imageFileName);
					System.out.println(articleNO);
					System.out.println(imageFileNO);
					System.out.println(regDate);
					
					
				}
		*/		
		
		//추가
	
	 //imageFileList에 담으면? 
		
	   	//첫 번째 축을 originalFileName으로 
	   	//먼저 originalFileName을 기준으로 vo를 불러 온 다음, 거기에 맞는 imageFileName을 넣는다? ----> 이걸 Controller에서 해야되나 ? imageFileName이 어디서 할당되지
			
		//추가 -실험용  
		//추가로 imageFileList 유효성 체크 
		List imageFileList = (List) articleMap.get("imageFileList");	
		// System.out.println(imageFileList.size()); <- 이거도 마찬가지 size 적용시킬 수없다? 
		if(imageFileList != null) { // 아 이렇게 하면 안되나.. imageFileList는   애초에 null값임. size 라는 함수를 적용할 수 없다 ? 
		    sqlSession.update("mapper.board.updateImage",articleMap);
		}
		
	}

	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
		
	}
	
	@Override
	public List selectImageFileList(int articleNO) throws DataAccessException {
		List<ImageVO> imageFileList = null;
		imageFileList = sqlSession.selectList("mapper.board.selectImageFileList",articleNO);
		//디버깅
		for(ImageVO imageVO : imageFileList) {
			String imageFileName = imageVO.getImageFileName();
			int imageFileNO = imageVO.getImageFileNO();							
			System.out.println(imageFileName);			
			System.out.println(imageFileNO);									
		}
		return imageFileList;
	}
	
	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}
	
	private int selectNewImageFileNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}

}
