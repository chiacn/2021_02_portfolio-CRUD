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
	
	
	// 게시판 목록 표시하기 
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
	 //한 개 이미지 글쓰기
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
		articleMap.put("parentNO", 0);  // 부모글이므로 parentNO = 0으로 지정
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
			message += " alert('새글을 추가했습니다.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = " <script>";
			message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	*/
	
	/*
	//한개의 이미지 보여주기
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
	
	
	//다중 이미지 보여주기
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
   
	

	
  //한 개 이미지 수정 기능  
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
	   message += " alert('글을 수정했습니다.');";
	   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	   message +=" </script>";
       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }catch(Exception e) {
      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
      srcFile.delete();
      message = "<script>";
	  message += " alert('오류가 발생했습니다.다시 수정해주세요');";
	  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	  message +=" </script>";
      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }
    return resEnt;
  }
  */
	
 
  //(추가) 다중 이미지 수정기능 
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
			
			//추가
			System.out.println(name);
			System.out.println(value);
			
		}
		//추가
		String _articleNO = (String) articleMap.get("articleNO");
		int articleNO = Integer.parseInt(_articleNO);
				
		//추가2 imageFileNO 받아와서 imageVO에 넣기
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	    String _listSize = (String) articleMap.get("listSize");
	    int listSize = Integer.parseInt(_listSize);  
	    
	    
		    for(int i=0;i<listSize;i++) {
		    	//처음 imageVO를 생성해서 imageFileList가 참조하게 할 때, originalFileName도 imageVO에 같이 넣는 코드추가 
		    	//originalFileName은 특정 imageFileNO나 imageFileName과 연계되어 저장되지 않아도 되므로 순서 상관 없음. -> 어차피 해당 파일을 지우는데 사용될 뿐이므로
		    	String originalFileName = (String) articleMap.get("originalFileName" + i);
		    	String _imageFileNO = (String) articleMap.get("imageFileNO" + i);
		    	
		    	//originalFileName은 따로 if로 체크하지 않아도 되는 이유 -> 어차피 articleView에서부터 imageFileNO, originalFileName 둘 다
		    	// imageFileName값이 있는지를 기준으로  disabled을 풀었기 때문에 imageFileNO가 있으면 거기에 대응하여 originalFileName 값이 있기 때문에 수가 같다. 
		    	if(_imageFileNO != null && ! _imageFileNO.equals("")) {
			    	int imageFileNO = Integer.parseInt(_imageFileNO);   
			    	ImageVO imageVO = new ImageVO();
			    	imageVO.setImageFileNO(imageFileNO);	
	                
			    	//originalFileName도 추가해주기
			    	imageVO.setOriginalFileName(originalFileName);
			    	
			    	//List로 해당 vo 참조.
			    	imageFileList.add(imageVO);
		    	}		    			    		    	
		    }
	    
		
		// 1. imageFileName -> imageFileList로 고쳐줌
		List<String> fileList= upload(multipartRequest);
		if(fileList != null && fileList.size()!=0) { //유효성검사. fileList 값이 있어야 (이미지를 올려야) 작동)	
			int i =0; //밖에다 빼야 됐음 ㅡㅡ 미친..
			for(String fileName : fileList) {
				
				ImageVO imageVO = imageFileList.get(i);  
				//어차피 참조니까 해당 imageVO 값만 수정하면 바뀐다
				imageVO.setImageFileName(fileName);				
				//추가
			    imageVO.setArticleNO(articleNO);				    				
				// imageFileList.add(imageVO);   이미 위에서 imageVO를 참조하는 작업을 수행했으므로 괜찮다. 	
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
	       // imageFileName -> imageFileList로 바꿔주고, 유효성검사 .length()에서 .size()로 
	       if(imageFileList!=null && imageFileList.size()!=0) {
	//TEST2
	System.out.println("if문 이하 작동");
	    	   
		       //다중이미지 새 글 추가 참고. '다중' 이미지이므로 for문으로 
	           //기존파일 delete를 위한 i값 추가
	          
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
		            //imageFileList ->해당 vo에서, 해당 orignalFileName 값을 얻어준다. -> 그걸 이용 해당 파일 삭제
                	String originalFileName = imageVO.getOriginalFileName();               	               	                               
					    File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
					    oldFile.delete();                                    
		       }
	       }
	       message = "<script>";
		   message += " alert('글을 수정했습니다.');";
		   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
		   message +=" </script>";
	       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    }catch(Exception e) {
	      // catch~도 다중 이미지 업로드일 때로 수정 (if, for문) / try에서 올라간 파일 있으면 해당 파일 지우기 
		      if(imageFileList != null && imageFileList.size() != 0) {
		    	  for(ImageVO imageVO : imageFileList) {
			    	  String imageFileName = imageVO.getImageFileName();
				      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				      srcFile.delete();
		    	  }
		      }
	      message = "<script>";
		  message += " alert('오류가 발생했습니다.다시 수정해주세요');";
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
		message += " alert('글을 삭제했습니다.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	       
	}catch(Exception e) {
		message = "<script>";
		message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    e.printStackTrace();
	}
	return resEnt;
  }  
  
  //답글 쓰기 (로그인 체크 추가, 새로 구성) 

  //추가(1.viewArticle.jsp에서 replyForm.jsp 찾아가는 부분)
 
@Override
@RequestMapping(value="/board/replyForm.do", method = RequestMethod.POST)
public ModelAndView replyForm(@RequestParam("parentNO") int parentNO,
		                       HttpServletRequest request, HttpServletResponse response) {
	    String viewName = (String) request.getAttribute("viewName");
	    
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName(viewName);
	   // mav.addObject("parentNO", parentNO); //이걸로 parentNO(articleNO)받아서 바인딩?
	   //시도1 session으로 받기.
	   HttpSession session;
	   session = request.getSession();
	   session.setAttribute("parentNO", parentNO);
	    return mav;
}

  //답글쓰기
  //다중 이미지 업로드 기준
@Override
@RequestMapping(value="/board/addReply.do" , method=RequestMethod.POST)
@ResponseBody
public ResponseEntity addReply(MultipartHttpServletRequest multipartRequest, 
		                       HttpServletResponse response) throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");
        String imageFileName = null;
        
        //articleMap 얻어서 jsp에서 받아온 정보 넣어주기 
        Map<String, Object> articleMap = new HashMap<String, Object>();
        Enumeration enu = multipartRequest.getParameterNames();
        while(enu.hasMoreElements()) {
        	String name = (String) enu.nextElement();
        	String value = multipartRequest.getParameter(name);
        	//추가
        	System.out.println(name + "," +value);
        	articleMap.put(name, value);
        }
        //시도 session 값으로 id 값 받기
        HttpSession session;
        session = multipartRequest.getSession();
        int parentNO = (Integer) session.getAttribute("parentNO");
        System.out.println(parentNO);
        articleMap.put("parentNO", parentNO);
       
        //id 임시 추가  <- 아이디 유효성체크로 대체 
        String id = "a";
        articleMap.put("id",id);
        
        /*
        //session에서 로그인 정보 얻어 온다.
        HttpSession session = multipartRequest.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        String id = memberVO.getId();
        articleMap.put("id", id);
    */
        
        // 1. 파일 안 올라오면 아예 upload 작동 안 되게 
        // 2. 위에서 imageFileName = null로 선언한거 아래에서 쓰여야 하는데 없는듯?
        
        List<String> fileList = upload(multipartRequest);     
        List<ImageVO> imageFileList = new ArrayList<ImageVO>();
        
        // 디버깅  - fileList 값 확인하는 작업
        
        if(fileList != null && fileList.size() != 0) {
        	//디버깅 - 중간에 넣어서 해당 구문이 실행되는지 확인 
        	System.out.println("fileList 값이 있습니다.");
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
        
        //디버깅용
        //map값 안에 저장된 전체 객체 확인
        System.out.println(articleMap);
        for(ImageVO imageVO : imageFileList) {
        	String name = imageVO.getImageFileName();
            System.out.println(name);
        }
        System.out.println(imageFileList);
        
       // imageFileList = null;                     ------------------ 일단 주석처리
        
        //테스트 - imageFileName 채워 넣어보기
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
        	message += " alert('답글을 등록했습니다.');";
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
        	message += " alert('오류가 발생했습니다. 다시 시도해 주세요');";
        	message += " location.href='" + multipartRequest.getContextPath() + "/board/replyForm.do';";
        	message += " </script>";
        	resEnt = new ResponseEntity(message ,responseHeaders, HttpStatus.CREATED);
        	e.printStackTrace();
        }
       
        return resEnt;
      
}
  

  //다중 이미지 글 추가하기
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
	
	//로그인 시 세션에 저장된 회원 정보에서 글쓴이 아이디를 얻어와서 Map에 저장합니다.
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id",id);
	articleMap.put("parentNO", 0); //추가
	
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
    
    //디버깅용
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
		message += " alert('새글을 추가했습니다.');";
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
		message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
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
	// 한 개 이미지 업로드하기
	// 수정
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		String imageFileName= null;
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()){    // 사실 단일 파일 업로드에서는 while 안 써도 된다.
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" + fileName);  //mkdir 할 때 
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
					file.getParentFile().mkdir();
					mFile.transferTo(new File(ARTICLE_IMAGE_REPO+ "\\" + "temp" + imageFileName));  					
				}
			
			}
		}
		return imageFileName;
	}
	*/
	
/* 기존 메서드 보존 	
	//다중 이미지 업로드하기
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();		
		Iterator<String> fileNames = multipartRequest.getFileNames();		
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			
			//추가
			System.out.println("fileName = " + fileName);
			System.out.println("orignalFilename =" + originalFileName);
		     
			//수정 - 이미지를 첨부하지 않았을 경우, fileList 값이 없도록 
			if(mFile.getSize() != 0 && mFile != null) {
			fileList.add(originalFileName);   // ----------------여기서 그냥  공백값같은걸로 들어간듯 
			}

			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" +originalFileName);
			if(mFile.getSize()!=0){ //File Null Check;
				if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
					     file.getParentFile().mkdirs();
					     mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
				}
				
			}
		}
		return fileList;
	}
	*/
	
	// 테스트용   다중 이미지 업로드하기
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();		
		Iterator<String> fileNames = multipartRequest.getFileNames();		
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			
			//추가
			System.out.println("fileName = " + fileName);
			System.out.println("orignalFilename =" + originalFileName);
		     
			//수정 - 이미지를 첨부하지 않았을 경우, fileList 값이 없도록 
			if(mFile.getSize() != 0 && mFile != null) {
			fileList.add(originalFileName);   // ----------------여기서 그냥  공백값같은걸로 들어간듯 
			}

			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ "temp" +"\\" +originalFileName);
			if(mFile.getSize()!=0){ //File Null Check;
				if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
					     file.getParentFile().mkdirs();
					     mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
				}
				
			}
		}
		return fileList;
	}
	
	
}
