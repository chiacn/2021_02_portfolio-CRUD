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
import java.util.HashMap;


@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession sqlSession;

	/*   페이징처리 이전 메서드
	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articlesList = articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}
   */
	// 페이징 처리
	@Override
	// select ->selectOne으로 고치고, return 추가    (  selectList를 써야되는지 .. )
    public List selectAllArticlesList(Map<String, Integer> pagingMap) throws DataAccessException {
		//selectList로 고쳐봄
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList", pagingMap);
		
		//디버깅
		for(ArticleVO articleVO : articlesList) {
			String title = articleVO.getTitle();
			int articleNO = articleVO.getArticleNO();
			int parentNO = articleVO.getParentNO();
			int level = articleVO.getLevel();
			
			System.out.println(title);
			System.out.println(articleNO);
			System.out.println(parentNO);	
			System.out.println(level);
		}
		
		return articlesList;
	}
	@Override
	// select -> selectOne으로 고치고 return 추가
	public int selectTotArticles() {
		return sqlSession.selectOne("mapper.board.selectTotArticles");	
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

	//임의의 테스트 글 추가 코드
	@Override
	public void addNewTestArticle(int number) throws DataAccessException {	
		Map<String, Object> articleMap = new HashMap<String, Object>();
		for(int i=0;i<number;i++) {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		//무결성에 위배되지 않게 id로 들어가는 테이블이 member테이블에 있어야 한다. 즉 testUser이라는 회원이 있어야한다.
		articleMap.put("id", "testUser");
		articleMap.put("title", "테스트용 글입니다." + i);
		articleMap.put("content", "테스트 글 내용" + i);
		articleMap.put("parentNO", 0);
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		}
	}
	
	//임의의 테스트 글 삭제 코드
	@Override
	public void deleteTestArticle(int deleteNumber) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteTestArticle", deleteNumber);
	}
}
