package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;


@Controller("boardController")
public class BoardControllerImpl  implements BoardController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;
	
	
	// �Խ��� ��� ǥ���ϱ� 
	@Override
	@RequestMapping(value= "/board/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
		
	}
/*	
	 //�� �� �̹��� �۾���
	@Override
	@RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, 
	HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName= upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("parentNO", 0);  // �θ���̹Ƿ� parentNO = 0���� ����
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
	
			message = "<script>";
			message += " alert('������ �߰��߽��ϴ�.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = " <script>";
			message +=" alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	*/
	
	/*
	//�Ѱ��� �̹��� �����ֱ�
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		articleVO=boardService.viewArticle(articleNO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;
	}
	*/
	
	
	//���� �̹��� �����ֱ�
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			  HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		Map articleMap =  boardService.viewArticle(articleNO);  
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("articleMap", articleMap);
		return mav;
	}
   
	

	
  //�� �� �̹��� ���� ���  
	/*
  @RequestMapping(value="/board/modArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
    HttpServletResponse response) throws Exception{
    multipartRequest.setCharacterEncoding("utf-8");
	Map<String,Object> articleMap = new HashMap<String, Object>();
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	String imageFileName= upload(multipartRequest);
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id", id);
	articleMap.put("imageFileName", imageFileName);
	
	String articleNO=(String)articleMap.get("articleNO");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
    try {
       boardService.modArticle(articleMap);
       if(imageFileName!=null && imageFileName.length()!=0) {
         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
         FileUtils.moveFileToDirectory(srcFile, destDir, true);
         
         String originalFileName = (String)articleMap.get("originalFileName");
         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
         oldFile.delete();
       }	
       message = "<script>";
	   message += " alert('���� �����߽��ϴ�.');";
	   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	   message +=" </script>";
       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }catch(Exception e) {
      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
      srcFile.delete();
      message = "<script>";
	  message += " alert('������ �߻��߽��ϴ�.�ٽ� �������ּ���');";
	  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	  message +=" </script>";
      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }
    return resEnt;
  }
  */
	
 
  //(�߰�) ���� �̹��� ������� 
	 @RequestMapping(value="/board/modArticle.do" ,method = RequestMethod.POST)
	  @ResponseBody
	  public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
	    HttpServletResponse response) throws Exception{
	    multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);	
			
			//�߰�
			System.out.println(name);
			System.out.println(value);
			
		}
		//�߰�
		String _articleNO = (String) articleMap.get("articleNO");
		int articleNO = Integer.parseInt(_articleNO);
				
		//�߰�2 imageFileNO �޾ƿͼ� imageVO�� �ֱ�
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	    String _listSize = (String) articleMap.get("listSize");
	    int listSize = Integer.parseInt(_listSize);  
	    
	    
		    for(int i=0;i<listSize;i++) {
		    	//ó�� imageVO�� �����ؼ� imageFileList�� �����ϰ� �� ��, originalFileName�� imageVO�� ���� �ִ� �ڵ��߰� 
		    	//originalFileName�� Ư�� imageFileNO�� imageFileName�� ����Ǿ� ������� �ʾƵ� �ǹǷ� ���� ��� ����. -> ������ �ش� ������ ����µ� ���� ���̹Ƿ�
		    	String originalFileName = (String) articleMap.get("originalFileName" + i);
		    	String _imageFileNO = (String) articleMap.get("imageFileNO" + i);
		    	
		    	//originalFileName�� ���� if�� üũ���� �ʾƵ� �Ǵ� ���� -> ������ articleView�������� imageFileNO, originalFileName �� ��
		    	// imageFileName���� �ִ����� ��������  disabled�� Ǯ���� ������ imageFileNO�� ������ �ű⿡ �����Ͽ� originalFileName ���� �ֱ� ������ ���� ����. 
		    	if(_imageFileNO != null && ! _imageFileNO.equals("")) {
			    	int imageFileNO = Integer.parseInt(_imageFileNO);   
			    	ImageVO imageVO = new ImageVO();
			    	imageVO.setImageFileNO(imageFileNO);	
	                
			    	//originalFileName�� �߰����ֱ�
			    	imageVO.setOriginalFileName(originalFileName);
			    	
			    	//List�� �ش� vo ����.
			    	imageFileList.add(imageVO);
		    	}		    			    		    	
		    }
	    
		
		// 1. imageFileName -> imageFileList�� ������
		List<String> fileList= upload(multipartRequest);
		if(fileList != null && fileList.size()!=0) { //��ȿ���˻�. fileList ���� �־�� (�̹����� �÷���) �۵�)	
			int i =0; //�ۿ��� ���� ���� �Ѥ� ��ģ..
			for(String fileName : fileList) {
				
				ImageVO imageVO = imageFileList.get(i);  
				//������ �����ϱ� �ش� imageVO ���� �����ϸ� �ٲ��
				imageVO.setImageFileName(fileName);				
				//�߰�
			    imageVO.setArticleNO(articleNO);				    				
				// imageFileList.add(imageVO);   �̹� ������ imageVO�� �����ϴ� �۾��� ���������Ƿ� ������. 	
			    i++;
			}				
			articleMap.put("imageFileList", imageFileList);
	    }
		
		
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("id", id);		
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    try {
	       boardService.modArticle(articleMap);
	       // imageFileName -> imageFileList�� �ٲ��ְ�, ��ȿ���˻� .length()���� .size()�� 
	       if(imageFileList!=null && imageFileList.size()!=0) {
	//TEST2
	System.out.println("if�� ���� �۵�");
	    	   
		       //�����̹��� �� �� �߰� ����. '����' �̹����̹Ƿ� for������ 
	           //�������� delete�� ���� i�� �߰�
	          
		       for(ImageVO imageVO : imageFileList) {
		    	 String imageFileName = imageVO.getImageFileName(); 
		    	 
		         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
		         FileUtils.moveFileToDirectory(srcFile, destDir, true);       
                    
                 /*
		         String originalFileName = (String)articleMap.get("originalFileName");
		         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
		         oldFile.delete();
		         */    
		            //imageFileList ->�ش� vo����, �ش� orignalFileName ���� ����ش�. -> �װ� �̿� �ش� ���� ����
                	String originalFileName = imageVO.getOriginalFileName();               	               	                               
					    File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
					    oldFile.delete();                                    
		       }
	       }
	       message = "<script>";
		   message += " alert('���� �����߽��ϴ�.');";
		   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
		   message +=" </script>";
	       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    }catch(Exception e) {
	      // catch~�� ���� �̹��� ���ε��� ���� ���� (if, for��) / try���� �ö� ���� ������ �ش� ���� ����� 
		      if(imageFileList != null && imageFileList.size() != 0) {
		    	  for(ImageVO imageVO : imageFileList) {
			    	  String imageFileName = imageVO.getImageFileName();
				      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				      srcFile.delete();
		    	  }
		      }
	      message = "<script>";
		  message += " alert('������ �߻��߽��ϴ�.�ٽ� �������ּ���');";
		  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
		  message +=" </script>";
	      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    }
	    return resEnt;
	  }
  
  
  @Override
  @RequestMapping(value="/board/removeArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity  removeArticle(@RequestParam("articleNO") int articleNO,
                              HttpServletRequest request, HttpServletResponse response) throws Exception{
	response.setContentType("text/html; charset=UTF-8");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	try {
		boardService.removeArticle(articleNO);
		File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
		FileUtils.deleteDirectory(destDir);
		
		message = "<script>";
		message += " alert('���� �����߽��ϴ�.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	       
	}catch(Exception e) {
		message = "<script>";
		message += " alert('�۾��� ������ �߻��߽��ϴ�.�ٽ� �õ��� �ּ���.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    e.printStackTrace();
	}
	return resEnt;
  }  
  
  //��� ���� (�α��� üũ �߰�, ���� ����) 

  //�߰�(1.viewArticle.jsp���� replyForm.jsp ã�ư��� �κ�)
 
@Override
@RequestMapping(value="/board/replyForm.do", method = RequestMethod.POST)
public ModelAndView replyForm(@RequestParam("parentNO") int parentNO,
		                       HttpServletRequest request, HttpServletResponse response) {
	    String viewName = (String) request.getAttribute("viewName");
	    
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName(viewName);
	   // mav.addObject("parentNO", parentNO); //�̰ɷ� parentNO(articleNO)�޾Ƽ� ���ε�?
	   //�õ�1 session���� �ޱ�.
	   HttpSession session;
	   session = request.getSession();
	   session.setAttribute("parentNO", parentNO);
	    return mav;
}

  //��۾���
  //���� �̹��� ���ε� ����
@Override
@RequestMapping(value="/board/addReply.do" , method=RequestMethod.POST)
@ResponseBody
public ResponseEntity addReply(MultipartHttpServletRequest multipartRequest, 
		                       HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");
        String imageFileName = null;
        
        //articleMap �� jsp���� �޾ƿ� ���� �־��ֱ� 
        Map<String, Object> articleMap = new HashMap<String, Object>();
        Enumeration enu = multipartRequest.getParameterNames();
        while(enu.hasMoreElements()) {
        	String name = (String) enu.nextElement();
        	String value = multipartRequest.getParameter(name);
        	//�߰�
        	System.out.println(name + "," +value);
        	articleMap.put(name, value);
        }
        //�õ� session ������ id �� �ޱ�
        HttpSession session;
        session = multipartRequest.getSession();
        int parentNO = (Integer) session.getAttribute("parentNO");
        System.out.println(parentNO);
        articleMap.put("parentNO", parentNO);
       
        //id �ӽ� �߰�  <- ���̵� ��ȿ��üũ�� ��ü 
        String id = "a";
        articleMap.put("id",id);
        
        /*
        //session���� �α��� ���� ��� �´�.
        HttpSession session = multipartRequest.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        String id = memberVO.getId();
        articleMap.put("id", id);
    */
        
        // 1. ���� �� �ö���� �ƿ� upload �۵� �� �ǰ� 
        // 2. ������ imageFileName = null�� �����Ѱ� �Ʒ����� ������ �ϴµ� ���µ�?
        
        List<String> fileList = upload(multipartRequest);     
        List<ImageVO> imageFileList = new ArrayList<ImageVO>();
        
        // �����  - fileList �� Ȯ���ϴ� �۾�
        
        if(fileList != null && fileList.size() != 0) {
        	//����� - �߰��� �־ �ش� ������ ����Ǵ��� Ȯ�� 
        	System.out.println("fileList ���� �ֽ��ϴ�.");
        	for(String fileName : fileList) {
        		ImageVO imageVO = new ImageVO();
        		imageVO.setImageFileName(fileName);
        		imageFileList.add(imageVO);
        	}
        	articleMap.put("imageFileList",imageFileList);
        }
        String message;
        ResponseEntity resEnt = null;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/html; charset=utf-8");
        
        //������
        //map�� �ȿ� ����� ��ü ��ü Ȯ��
        System.out.println(articleMap);
        for(ImageVO imageVO : imageFileList) {
        	String name = imageVO.getImageFileName();
            System.out.println(name);
        }
        System.out.println(imageFileList);
        
       // imageFileList = null;                     ------------------ �ϴ� �ּ�ó��
        
        //�׽�Ʈ - imageFileName ä�� �־��
       /* ImageVO imageVO = new ImageVO();
        imageVO.setImageFileName("ddd");
        imageFileList.add(imageVO);
       */
        try {
        	int articleNO = boardService.addNewArticle(articleMap);
        	if(imageFileList != null && imageFileList.size() != 0) {
        		for(ImageVO imageVO : imageFileList) {
        			imageFileName = imageVO.getImageFileName();
        			File srcFile = new File(ARTICLE_IMAGE_REPO +"\\" + "temp" +"\\" + imageFileName);
        			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
        			destDir.mkdirs();
        			FileUtils.moveFileToDirectory(srcFile, destDir, true);
        			
        		}
        	}
        	message = "<script>";
        	message += " alert('����� ����߽��ϴ�.');";
            message += " location.href = '" +multipartRequest.getContextPath() + "/board/listArticles.do';";
            message += " </script>";
            resEnt = new ResponseEntity(message ,responseHeaders, HttpStatus.CREATED);
        }catch(Exception e) {
        	if(imageFileList != null && imageFileList.size() != 0) {
        		for(ImageVO imageVO : imageFileList) {
        			imageFileName = imageVO.getImageFileName();
        			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
        			srcFile.delete();
        		}
        	}
        	message = "<script>";
        	message += " alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');";
        	message += " location.href='" + multipartRequest.getContextPath() + "/board/replyForm.do';";
        	message += " </script>";
        	resEnt = new ResponseEntity(message ,responseHeaders, HttpStatus.CREATED);
        	e.printStackTrace();
        }
       
        return resEnt;
      
}
  

  //���� �̹��� �� �߰��ϱ�
  @Override
  @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
	multipartRequest.setCharacterEncoding("utf-8");
	String imageFileName=null;
	
	Map<String, Object> articleMap = new HashMap<String, Object>();
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	//�α��� �� ���ǿ� ����� ȸ�� �������� �۾��� ���̵� ���ͼ� Map�� �����մϴ�.
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id",id);
	articleMap.put("parentNO", 0); //�߰�
	
	List<String> fileList =upload(multipartRequest);
	List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	if(fileList!= null && fileList.size()!=0) {
		for(String fileName : fileList) {
			ImageVO imageVO = new ImageVO();
			imageVO.setImageFileName(fileName);
			imageFileList.add(imageVO);
		}
		articleMap.put("imageFileList", imageFileList);
	}
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
    
    //������
    System.out.println(articleMap);
    for(ImageVO imageVO : imageFileList) {
    	String name = imageVO.getImageFileName();
        System.out.println(name);
    }
    
	try {
		int articleNO = boardService.addNewArticle(articleMap);
		if(imageFileList!=null && imageFileList.size()!=0) {
			for(ImageVO  imageVO:imageFileList) {
				imageFileName = imageVO.getImageFileName();
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				//destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
		}
		    
		message = "<script>";
		message += " alert('������ �߰��߽��ϴ�.');";
		message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    
		 
	}catch(Exception e) {
		if(imageFileList!=null && imageFileList.size()!=0) {
		  for(ImageVO  imageVO:imageFileList) {
		  	imageFileName = imageVO.getImageFileName();
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		 	srcFile.delete();
		  }
		}

		
		message = " <script>";
		message +=" alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');');";
		message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do';";
		message +=" </script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		e.printStackTrace();
	}
	return resEnt;
  }
	


	

	@RequestMapping(value = "/board/*Form.do", method =  {RequestMethod.GET, RequestMethod.POST})
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
/*
	// �� �� �̹��� ���ε��ϱ�
	// ����
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		String imageFileName= null;
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()){    // ��� ���� ���� ���ε忡���� while �� �ᵵ �ȴ�.
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" + fileName);  //mkdir �� �� 
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					file.getParentFile().mkdir();
					mFile.transferTo(new File(ARTICLE_IMAGE_REPO+ "\\" + "temp" + imageFileName));  					
				}
			
			}
		}
		return imageFileName;
	}
	*/
	
/* ���� �޼��� ���� 	
	//���� �̹��� ���ε��ϱ�
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();		
		Iterator<String> fileNames = multipartRequest.getFileNames();		
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			
			//�߰�
			System.out.println("fileName = " + fileName);
			System.out.println("orignalFilename =" + originalFileName);
		     
			//���� - �̹����� ÷������ �ʾ��� ���, fileList ���� ������ 
			if(mFile.getSize() != 0 && mFile != null) {
			fileList.add(originalFileName);   // ----------------���⼭ �׳�  ���鰪�����ɷ� ���� 
			}

			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" +originalFileName);
			if(mFile.getSize()!=0){ //File Null Check;
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					     file.getParentFile().mkdirs();
					     mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
				}
				
			}
		}
		return fileList;
	}
	*/
	
	// �׽�Ʈ��   ���� �̹��� ���ε��ϱ�
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();		
		Iterator<String> fileNames = multipartRequest.getFileNames();		
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			
			//�߰�
			System.out.println("fileName = " + fileName);
			System.out.println("orignalFilename =" + originalFileName);
		     
			//���� - �̹����� ÷������ �ʾ��� ���, fileList ���� ������ 
			if(mFile.getSize() != 0 && mFile != null) {
			fileList.add(originalFileName);   // ----------------���⼭ �׳�  ���鰪�����ɷ� ���� 
			}

			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" +originalFileName);
			if(mFile.getSize()!=0){ //File Null Check;
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					     file.getParentFile().mkdirs();
					     mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
				}
				
			}
		}
		return fileList;
	}
	
	
}
