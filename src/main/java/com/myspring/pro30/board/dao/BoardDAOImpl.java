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

	/*   ����¡ó�� ���� �޼���
	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articlesList = articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}
   */
	// ����¡ ó��
	@Override
	// select ->selectOne���� ��ġ��, return �߰�    (  selectList�� ��ߵǴ��� .. )
    public List selectAllArticlesList(Map<String, Integer> pagingMap) throws DataAccessException {
		//selectList�� ���ĺ�
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList", pagingMap);
		
		//�����
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
	// select -> selectOne���� ��ġ�� return �߰�
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
    
	//���� ���� ���ε�
	
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
		
		//�߰�
		System.out.println(articleMap);
				
		//�߰� -�����  
		//�߰��� imageFileList ��ȿ�� üũ 
		List imageFileList = (List) articleMap.get("imageFileList");	
		// System.out.println(imageFileList.size()); <- �̰ŵ� �������� size �����ų ������? 
		if(imageFileList != null) { // �� �̷��� �ϸ� �ȵǳ�.. imageFileList��   ���ʿ� null����. size ��� �Լ��� ������ �� ���� ? 
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
		//�����
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

	//������ �׽�Ʈ �� �߰� �ڵ�
	@Override
	public void addNewTestArticle(int number) throws DataAccessException {	
		Map<String, Object> articleMap = new HashMap<String, Object>();
		for(int i=0;i<number;i++) {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		//���Ἲ�� ������� �ʰ� id�� ���� ���̺��� member���̺� �־�� �Ѵ�. �� testUser�̶�� ȸ���� �־���Ѵ�.
		articleMap.put("id", "testUser");
		articleMap.put("title", "�׽�Ʈ�� ���Դϴ�." + i);
		articleMap.put("content", "�׽�Ʈ �� ����" + i);
		articleMap.put("parentNO", 0);
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		}
	}
	
	//������ �׽�Ʈ �� ���� �ڵ�
	@Override
	public void deleteTestArticle(int deleteNumber) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteTestArticle", deleteNumber);
	}
}
