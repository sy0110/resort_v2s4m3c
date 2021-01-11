package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.attachfile.AttachfileProcInter;
import dev.mvc.attachfile.AttachfileVO;
import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVO;
import dev.mvc.categrp.CategrpProcInter;
import dev.mvc.categrp.CategrpVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
public class ContentsCont {
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;
  
  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;
  
  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  @Autowired
  @Qualifier("dev.mvc.attachfile.AttachfileProc")
  private AttachfileProcInter attachfileProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc = null;
  
  public ContentsCont() {
    System.out.println("--> ContentsCont created.");
  }
  
  /**
   * 등록폼 http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.GET)
  public ModelAndView create(int cateno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    
    mav.addObject("cateVO", cateVO);
    mav.addObject("categrpVO", categrpVO);
    
    mav.setViewName("/contents/create"); // /webapp/categrp/create.jsp
    // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
    // mav.addObject("content", content);

    return mav; // forward
  }
  
  /**
   * 등록 처리 http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value = "/contents/create.do", method = RequestMethod.POST)
   * public ModelAndView create(HttpServletRequest request, ContentsVO contentsVO)
   * { // System.out.println("IP: " + contentsVO.getIp()); // Oracle은 "" -> null로
   * 인식 // System.out.println("IP: " + request.getRemoteAddr());
   * contentsVO.setIp(request.getRemoteAddr());
   * 
   * ModelAndView mav = new ModelAndView(); //
   * ------------------------------------------------------------------- // 파일 전송
   * 코드 시작 // -------------------------------------------------------------------
   * String file1 = ""; // main image String thumb1 = ""; // preview image
   * 
   * String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); //
   * 절대 경로
   * 
   * // 전송 파일이 없어서도 fnamesMF 객체가 생성됨. // <input type='file' class="form-control"
   * name='file1MF' id='file1MF' // value='' placeholder="파일 선택"
   * multiple="multiple"> MultipartFile mf = contentsVO.getFile1MF();
   * 
   * long size1 = mf.getSize(); // 파일 크기 if (size1 > 0) { // 파일 크기 체크 // mp3 =
   * mf.getOriginalFilename(); // 원본 파일명, spring.jpg // 파일 저장 후 업로드된 파일명이 리턴됨,
   * spring.jsp, spring_1.jpg... file1 = Upload.saveFileSpring(mf, upDir);
   * 
   * if (Tool.isImage(file1)) { // 이미지인지 검사 // thumb 이미지 생성후 파일명 리턴됨, width: 200,
   * height: 150 thumb1 = Tool.preview(upDir, file1, 200, 150); }
   * 
   * }
   * 
   * contentsVO.setFile1(file1); contentsVO.setThumb1(thumb1);
   * contentsVO.setSize1(size1); //
   * ------------------------------------------------------------------- // 파일 전송
   * 코드 종료 // -------------------------------------------------------------------
   * 
   * int cnt = this.contentsProc.create(contentsVO); if (cnt == 1) {
   * cateProc.increaseCnt(contentsVO.getCateno()); } mav.addObject("cnt", cnt); //
   * request.setAttribute("cnt", cnt)
   * 
   * // <c:import> 정상 작동안됨. // mav.setViewName("/contents/create_msg"); //
   * /webapp/contents/create_msg.jsp
   * 
   * // System.out.println("--> cateno: " + contentsVO.getCateno()); // redirect시에
   * hidden tag로 보낸것들이 전달이 안됨으로 request에 다시 저장 mav.addObject("cateno",
   * contentsVO.getCateno()); // redirect parameter 적용 mav.addObject("url",
   * "create_msg"); // create_msg.jsp, redirect parameter 적용
   * mav.setViewName("redirect:/contents/msg.do");
   * 
   * return mav; // forward }
   */

  /**
   * 등록 처리 http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request, ContentsVO contentsVO) {
    // System.out.println("IP: " + contentsVO.getIp());  // Oracle은 "" -> null로 인식
    // System.out.println("IP: " + request.getRemoteAddr());     
    contentsVO.setIp(request.getRemoteAddr()); // IP 지정
    
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // 파일 전송 코드 시작
    // -------------------------------------------------------------------
    String file1 = "";     // main image
    String thumb1 = ""; // preview image
        
    String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
    
    // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
    // <input type='file' class="form-control" name='file1MF' id='file1MF' 
    //           value='' placeholder="파일 선택" multiple="multiple">
    MultipartFile mf = contentsVO.getFile1MF();
    
    long size1 = mf.getSize();  // 파일 크기
    if (size1 > 0) { // 파일 크기 체크
      // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
      // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
      file1 = Upload.saveFileSpring(mf, upDir); 
      
      if (Tool.isImage(file1)) { // 이미지인지 검사
        // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
        thumb1 = Tool.preview(upDir, file1, 200, 150); 
      }
      
    }    
    
    contentsVO.setFile1(file1);
    contentsVO.setThumb1(thumb1);
    contentsVO.setSize1(size1);
    // -------------------------------------------------------------------
    // 파일 전송 코드 종료
    // -------------------------------------------------------------------
    // Call By Reference: 메모리 공유, Hashcode 전달
    int cnt = this.contentsProc.create(contentsVO); 
    
    // -------------------------------------------------------------------
    // PK의 return
    // -------------------------------------------------------------------
    System.out.println("--> contentsno: " + contentsVO.getContentsno());
    mav.addObject("contentsno", contentsVO.getContentsno()); // redirect parameter 적용
    // -------------------------------------------------------------------
    
    if (cnt == 1) {
      cateProc.increaseCnt(contentsVO.getCateno());
    }
    mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)

    // <c:import> 정상 작동안됨.
    // mav.setViewName("/contents/create_msg"); // /webapp/contents/create_msg.jsp
    
    // System.out.println("--> cateno: " + contentsVO.getCateno());
    // redirect시에 hidden tag로 보낸것들이 전달이 안됨으로 request에 다시 저장
    mav.addObject("cateno", contentsVO.getCateno()); // redirect parameter 적용
    mav.addObject("url", "create_continue"); // create_continue.jsp, redirect parameter 적용
    mav.setViewName("redirect:/contents/msg.do"); 
    
    return mav; // forward
  }
  
  /**
   * 목록 http://localhost:9090/resort/contents/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_all.do", method = RequestMethod.GET)
  public ModelAndView list() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/list_all"); // /webapp/contents/list_all.jsp

    List<ContentsVO> list = this.contentsProc.list_all();
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * 카테고리별 목록 http://localhost:9090/resort/contents/list.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
   * public ModelAndView list_by_cateno(int cateno) { ModelAndView mav = new
   * ModelAndView(); // /webapp/contents/list_by_cateno.jsp //
   * mav.setViewName("/contents/list_by_cateno");
   * 
   * // 테이블 이미지 기반, /webapp/contents/list_by_cateno.jsp
   * mav.setViewName("/contents/list_by_cateno_table_img1");
   * 
   * CateVO cateVO = this.cateProc.read(cateno); mav.addObject("cateVO", cateVO);
   * 
   * CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
   * mav.addObject("categrpVO", categrpVO);
   * 
   * List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
   * mav.addObject("list", list);
   * 
   * return mav; // forward }
   */
  
  /**
   * 목록 + 검색 지원
   * http://localhost:9090/resort/contents/list.do
   * http://localhost:9090/resort/contents/list.do?cateno=1&word=스위스
   * @param cateno
   * @param word
   * @return
   */
  /*
   * @RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
   * public ModelAndView list_by_cateno_search(
   * 
   * @RequestParam(value="cateno", defaultValue="1") int cateno,
   * 
   * @RequestParam(value="word", defaultValue="") String word ) {
   * 
   * ModelAndView mav = new ModelAndView(); //
   * /contents/list_by_cateno_table_img1_search.jsp
   * mav.setViewName("/contents/list_by_cateno_table_img1_search");
   * 
   * // 숫자와 문자열 타입을 저장해야함으로 Obejct 사용 HashMap<String, Object> map = new
   * HashMap<String, Object>(); map.put("cateno", cateno); // #{cateno}
   * map.put("word", word); // #{word}
   * 
   * // 검색 목록 List<ContentsVO> list = contentsProc.list_by_cateno_search(map);
   * mav.addObject("list", list);
   * 
   * // 검색된 레코드 갯수 int search_count = contentsProc.search_count(map);
   * mav.addObject("search_count", search_count);
   * 
   * CateVO cateVO = cateProc.read(cateno); mav.addObject("cateVO", cateVO);
   * 
   * CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
   * mav.addObject("categrpVO", categrpVO);
   * 
   * return mav; }
   */
    
  /**
   * 목록 + 검색 + 페이징 지원
   * http://localhost:9090/resort/contents/list.do
   * http://localhost:9090/resort/contents/list.do?cateno=1&word=스위스&nowPage=1
   * @param cateno
   * @param word
   * @param nowPage
   * @return
   */
  @RequestMapping(value = "/contents/list.do", 
                                       method = RequestMethod.GET)
  public ModelAndView list_by_cateno_search_paging(
      @RequestParam(value="cateno", defaultValue="1") int cateno,
      @RequestParam(value="word", defaultValue="") String word,
      @RequestParam(value="nowPage", defaultValue="1") int nowPage
      ) { 
    System.out.println("--> nowPage: " + nowPage);
    
    ModelAndView mav = new ModelAndView();
    
    // 숫자와 문자열 타입을 저장해야함으로 Obejct 사용
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("cateno", cateno); // #{cateno}
    map.put("word", word);     // #{word}
    map.put("nowPage", nowPage);  // 페이지에 출력할 레코드의 범위를 산출하기위해 사용     
    
    // 검색 목록
    List<Contents_MemberVO> list = contentsProc.list_by_cateno_search_paging_join(map);
    mav.addObject("list", list);
    
    // 검색된 레코드 갯수
    int search_count = contentsProc.search_count(map);
    mav.addObject("search_count", search_count);
  
    CateVO cateVO = cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    /*
     * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
     * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
     * 
     * @param listFile 목록 파일명 
     * @param cateno 카테고리번호 
     * @param search_count 검색(전체) 레코드수 
     * @param nowPage     현재 페이지
     * @param word 검색어
     * @return 페이징 생성 문자열
     */ 
    String paging = contentsProc.pagingBox("list.do", cateno, search_count, nowPage, word);
    mav.addObject("paging", paging);
  
    mav.addObject("nowPage", nowPage);

    // /contents/list_by_cateno_table_img1_search_paging.jsp
    mav.setViewName("/contents/list_by_cateno_table_img1_search_paging_join");   
    
    return mav;
  }    
 

  /**
   * 카테고리별 목록 http://localhost:9090/resort/contents/list_by_cateno_grid1.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_by_cateno_grid1.do", method = RequestMethod.GET)
  public ModelAndView list_by_cateno_grid1(int cateno) {
    ModelAndView mav = new ModelAndView();
    
    // 테이블 이미지 기반, /webapp/contents/list_by_cateno_grid1.jsp
    mav.setViewName("/contents/list_by_cateno_grid1");

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  // http://localhost:9090/resort/contents/read.do
  /**
   * 전체 목록
   * @return
   */
  @RequestMapping(value="/contents/read.do", method=RequestMethod.GET )
  public ModelAndView read(int contentsno) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO); 

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO); 
    
    // 첨부 파일 목록
    List<AttachfileVO> attachfile_list = this.attachfileProc.list_by_contentsno(contentsno);
    mav.addObject("attachfile_list", attachfile_list);
    System.out.println("--> 첨부 파일 갯수: " + attachfile_list.size());
    
    // mav.setViewName("/contents/read"); // /webapp/contents/read.jsp
    // mav.setViewName("/contents/read_img"); // /webapp/contents/read_img.jsp
    // mav.setViewName("/contents/read_img_attachfile"); // /webapp/contents/read_img_attachfile.jsp    
    // mav.setViewName("/contents/read_img_attachfile_reply"); // /webapp/contents/read_img_attachfile_reply.jsp
    // mav.setViewName("/contents/read_img_attachfile_reply_add"); // /webapp/contents/read_img_attachfile_reply_add.jsp
    mav.setViewName("/contents/read_img_attachfile_reply_add_pg"); // /webapp/contents/read_img_attachfile_reply_add_pg.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/update.do
  /**
   * 수정 폼
   * @return
   */
  @RequestMapping(value="/contents/update.do", method=RequestMethod.GET )
  public ModelAndView update(int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // 수정용 읽기
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);
    
    mav.setViewName("/contents/update"); // webapp/contents/update.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/update.do
  /**
   * 수정 처리
   * @param contentsVO
   * @return
   */
  @RequestMapping(value="/contents/update.do", method=RequestMethod.POST )
  public ModelAndView update(ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    // mav.addObject("cateVO", cateVO); // 전달안됨.
    mav.addObject("cate_name", cateVO.getName());
    mav.addObject("cateno", cateVO.getCateno());

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    // mav.addObject("categrpVO", categrpVO); // 전달안됨.
    mav.addObject("categrp_name", categrpVO.getName());
    
    mav.addObject("contentsno", contentsVO.getContentsno());
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    int passwd_cnt = 0; // 패스워드 일치 레코드 갯수
    int cnt = 0;             // 수정된 레코드 갯수 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // 패스워드가 일치할 경우 글 수정
      cnt = this.contentsProc.update(contentsVO);
    }

    mav.addObject("cnt", cnt); // request에 저장
    mav.addObject("passwd_cnt", passwd_cnt); // request에 저장
        
    mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
    
    return mav;
  }
  
  /*
   * @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
   * public ModelAndView delete(int contentsno) { ModelAndView mav = new
   * ModelAndView();
   * 
   * ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // 수정용 읽기
   * mav.addObject("contentsVO", contentsVO); //
   * request.setAttribute("contentsVO", contentsVO);
   * 
   * mav.setViewName("/contents/delete"); // webapp/contents/delete.jsp
   * 
   * return mav; }
   */
  
  // http://localhost:9090/resort/contents/delete.do
//  /**
//   * 삭제 폼, attachfile Ajax 기반 삭제 지원
//   * @return
//   */
//  @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
//  public ModelAndView delete(int contentsno) {
//    ModelAndView mav = new ModelAndView();
//    
//    ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // 수정용 읽기
//    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);
//    
//    mav.setViewName("/contents/delete"); // webapp/contents/delete.jsp
//    
//    return mav;
//  }
  
//  /**
//   * 로그인 여부만 체크함으로 다른 사용자의 글을 삭제 할 수 있는 화면이 출력됨.
//   * 삭제 폼, attachfile Ajax 기반 삭제 지원
//   * @return
//   */
//  @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
//  public ModelAndView delete(HttpSession session, int contentsno) {
//    ModelAndView mav = new ModelAndView();
//    
//    if (this.memberProc.isMember(session)) {
//      ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // 수정용 읽기
//      mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);
//      
//      mav.setViewName("/contents/delete"); // webapp/contents/delete.jsp
//    } else {
//      mav.setViewName("redirect:/member/login_need.jsp");  // ,jsp 확장자 선언
//    }
//    
//    return mav;
//  }

  /**
  * 로그인 여부만 체크함으로 다른 사용자의 글을 삭제 할 수 있는 화면이 출력됨.
  * 삭제 폼, attachfile Ajax 기반 삭제 지원
  * @return
  */
  @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
  public ModelAndView delete(HttpSession session, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    int memberno = (int)session.getAttribute("memberno");
    
    if (memberno == this.contentsProc.read(contentsno).getMemberno()) {
      ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // 수정용 읽기
      mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);
      
      mav.setViewName("/contents/delete"); // webapp/contents/delete.jsp
    } else {
      mav.setViewName("redirect:/member/mconfirm_fail_msg.jsp");
    }
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/delete.do
  /**
   * 삭제 처리 +  파일 삭제
   * @param contentsVO
   * @return
   */
  @RequestMapping(value="/contents/delete.do", method=RequestMethod.POST )
  public ModelAndView delete(HttpServletRequest request,
                                           int cateno, 
                                           int contentsno, 
                                           String passwd,
                                           @RequestParam(value="word", defaultValue="") String word,
                                           @RequestParam(value="nowPage", defaultValue="1") int nowPage) {
    ModelAndView mav = new ModelAndView();
   
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    String title = contentsVO.getTitle();
    mav.addObject("title", title);
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    int passwd_cnt = 0; // 패스워드 일치 레코드 갯수
    int cnt = 0;             // 수정된 레코드 갯수 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    boolean sw = false;
    
    if (passwd_cnt == 1) { // 패스워드가 일치할 경우 글 수정
      cnt = this.contentsProc.delete(contentsno);
      if (cnt == 1) {
        cateProc.decreaseCnt(cateno);
        
        // -------------------------------------------------------------------------------------
        // 마지막 페이지의 레코드 삭제시의 페이지 번호 -1 처리
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("cateno", cateno);
        map.put("word", word);
        // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
        if (contentsProc.search_count(map) % Contents.RECORD_PER_PAGE == 0) {
          nowPage = nowPage - 1;
          if (nowPage < 1) {
            nowPage = 1; // 시작 페이지
          }
        }
        // -------------------------------------------------------------------------------------
      }
      
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder에서 1건의 파일 삭제
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder에서 1건의 파일 삭제

    }

    mav.addObject("cnt", cnt); // request에 저장
    mav.addObject("passwd_cnt", passwd_cnt); // request에 저장
    mav.addObject("nowPage", nowPage); // request에 저장
    // System.out.println("--> ContentsCont.java nowPage: " + nowPage);
    
    mav.addObject("cateno", contentsVO.getCateno()); // redirect parameter 적용
    mav.addObject("url", "delete_msg"); // delete_msg.jsp, redirect parameter 적용
    
    // mav.setViewName("/contents/delete_msg"); // webapp/contents/delete_msg.jsp
    mav.setViewName("redirect:/contents/msg.do"); 
    
    return mav;
  }

  /**
   * 메인 이미지 등록 폼 http://localhost:9090/resort/contents/img_create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.GET)
  public ModelAndView img_create(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_create"); // /webapp/contents/img_create.jsp

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);
    
    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO); 

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO); 
    
    return mav; // forward
  }

  /**
   * 메인 이미지 등록 처리 http://localhost:9090/resort/contents/img_create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.POST)
  public ModelAndView img_create(HttpServletRequest request, 
                                    ContentsVO contentsVO,
                                    @RequestParam(value="nowPage", defaultValue="1") int nowPage) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    int passwd_cnt = 0; // 패스워드 일치 레코드 갯수
    int cnt = 0;             // 수정된 레코드 갯수 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // 패스워드가 일치할 경우 파일 업로드
      // -------------------------------------------------------------------
      // 파일 전송 코드 시작
      // -------------------------------------------------------------------
      String file1 = "";     // main image
      String thumb1 = ""; // preview image
          
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
      // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF' 
      //           value='' placeholder="파일 선택" multiple="multiple">
      MultipartFile mf = contentsVO.getFile1MF();
      long size1 = mf.getSize();  // 파일 크기
      if (size1 > 0) { // 파일 크기 체크
        // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150); 
        }
      }    
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------
      
      mav.addObject("nowPage", nowPage);
      mav.addObject("contentsno", contentsVO.getContentsno());
      
      mav.setViewName("redirect:/contents/read.do");
      
      cnt = this.contentsProc.img_create(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request에 저장
    mav.addObject("passwd_cnt", passwd_cnt); // request에 저장
            
    return mav;    
  }
  
  /**
   * 메인 이미지 삭제/수정 폼 http://localhost:9090/resort/contents/img_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.GET)
  public ModelAndView img_update(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_update"); // /webapp/contents/img_update.jsp

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);
    
    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO); 

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO); 
    
    return mav; // forward
  }
  
  /**
   * 메인 이미지 삭제 처리 http://localhost:9090/resort/contents/img_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_delete.do", method = RequestMethod.POST)
  public ModelAndView img_delete(HttpServletRequest request,
                                       int contentsno, 
                                       int cateno, 
                                       String passwd,
                                       @RequestParam(value="nowPage", defaultValue="1") int nowPage) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    int passwd_cnt = 0; // 패스워드 일치 레코드 갯수
    int cnt = 0;             // 수정된 레코드 갯수 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // 패스워드가 일치할 경우 파일 업로드
      // -------------------------------------------------------------------
      // 파일 삭제 코드 시작
      // -------------------------------------------------------------------
      // 삭제할 파일 정보를 읽어옴.
      ContentsVO contentsVO = contentsProc.read(contentsno);
      // System.out.println("file1: " + contentsVO.getFile1());
      
      String file1 = contentsVO.getFile1().trim();
      String thumb1 = contentsVO.getThumb1().trim();
      long size1 = contentsVO.getSize1();
      boolean sw = false;
      
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder에서 1건의 파일 삭제
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder에서 1건의 파일 삭제
      // System.out.println("sw: " + sw);
      
      file1 = "";
      thumb1 = "";
      size1 = 0;
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 삭제 종료 시작
      // -------------------------------------------------------------------
      
      mav.addObject("nowPage", nowPage);  // redirect시에 get parameter로 전송됨
      mav.addObject("contentsno", contentsno);
      mav.setViewName("redirect:/contents/read.do");
      
      cnt = this.contentsProc.img_delete(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request에 저장
    mav.addObject("passwd_cnt", passwd_cnt); // request에 저장
            
    return mav;    
  }
  
  /**
   * 메인 이미지 수정 처리 http://localhost:9090/resort/contents/img_update.do
   * 기존 이미지 삭제후 새로운 이미지 등록(수정 처리)
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.POST)
  public ModelAndView img_update(HttpServletRequest request, 
                                     ContentsVO contentsVO,
                                     @RequestParam(value="nowPage", defaultValue="1") int nowPage) {
    ModelAndView mav = new ModelAndView();
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    int passwd_cnt = 0; // 패스워드 일치 레코드 갯수
    int cnt = 0;             // 수정된 레코드 갯수 
    
    passwd_cnt = this.contentsProc.passwd_check(hashMap);
    
    if (passwd_cnt == 1) { // 패스워드가 일치할 경우 파일 업로드
      // -------------------------------------------------------------------
      // 파일 삭제 코드 시작
      // -------------------------------------------------------------------
      // 삭제할 파일 정보를 읽어옴.
      ContentsVO vo = contentsProc.read(contentsVO.getContentsno());
      // System.out.println("file1: " + contentsVO.getFile1());
      
      String file1 = vo.getFile1().trim();
      String thumb1 = vo.getThumb1().trim();
      long size1 = 0;
      boolean sw = false;
      
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
      sw = Tool.deleteFile(upDir, contentsVO.getFile1());  // Folder에서 1건의 파일 삭제
      sw = Tool.deleteFile(upDir, contentsVO.getThumb1());  // Folder에서 1건의 파일 삭제
      // System.out.println("sw: " + sw);
      // -------------------------------------------------------------------
      // 파일 삭제 종료 시작
      // -------------------------------------------------------------------
      
      // -------------------------------------------------------------------
      // 파일 전송 코드 시작
      // -------------------------------------------------------------------
      // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF' 
      //           value='' placeholder="파일 선택" multiple="multiple">
      MultipartFile mf = contentsVO.getFile1MF();
      size1 = mf.getSize();  // 파일 크기
      if (size1 > 0) { // 파일 크기 체크
        // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150); 
        }
      }    
      
      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      mav.addObject("nowPage", nowPage);
      mav.addObject("contentsno", contentsVO.getContentsno());
      mav.setViewName("redirect:/contents/read.do");
      
      
      cnt = this.contentsProc.img_create(contentsVO);
      // mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)
      
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      
    }

    mav.addObject("cnt", cnt); // request에 저장
    mav.addObject("passwd_cnt", passwd_cnt); // request에 저장
            
    return mav;    
  }
  
  /**
   * 새로고침을 방지하는 메시지 출력
   * @return
   */
  @RequestMapping(value="/contents/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    
    // 등록 처리 메시지: create_msg --> /contents/create_msg.jsp
    // 수정 처리 메시지: update_msg --> /contents/update_msg.jsp
    // 삭제 처리 메시지: delete_msg --> /contents/delete_msg.jsp
    mav.setViewName("/contents/" + url); // forward
    
    return mav; // forward
  }
  
  // http://localhost:9090/resort/contents/map_create.do?cateno=25&contentsno=28
  /**
   * 지도 등록 폼
   * @return
   */
  @RequestMapping(value="/contents/map_create.do", method=RequestMethod.GET )
  public ModelAndView map_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/map_create"); // webapp/contents/map_create.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/passwd.do?contentsno=28&passwd=123
  /**
   * 글의 패스워드 체크, JSON 출력
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/contents/passwd.do", method=RequestMethod.GET ,
                              produces = "text/plain;charset=UTF-8" )
  public String passwd(int contentsno, String passwd) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    int cnt = this.contentsProc.passwd_check(hashMap);
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
    
    return json.toString(); 
  }
  
  // http://localhost:9090/resort/contents/map_create.do?cateno=25&contentsno=28&passwd=1234
  /**
   * 지도 등록
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param map 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/map_create.do", method=RequestMethod.POST )
  public ModelAndView map_create(int cateno, int contentsno, String map, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("map", map);
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.map(hashMap); // 지동 등록
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp
    
    return mav;
  }

  // http://localhost:9090/resort/contents/map_delete.do?cateno=25&contentsno=28
  /**
   * 지도 삭제 폼
   * @return
   */
  @RequestMapping(value="/contents/map_delete.do", method=RequestMethod.GET )
  public ModelAndView map_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/map_delete"); // webapp/contents/map_delete.jsp
    
    return mav;
  }

  // http://localhost:9090/resort/contents/map_delete.do?cateno=25&contentsno=28&passwd=1234
  /**
   * 지도 삭제 처리
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param map 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/map_delete.do", method=RequestMethod.POST )
  public ModelAndView map_delete_proc(int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("map", ""); // map 삭제
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.map(hashMap); // 지도 삭제 처리
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/youtube_create.do?cateno=25&contentsno=28
  /**
   * Youtube 등록 폼
   * @return
   */
  @RequestMapping(value="/contents/youtube_create.do", method=RequestMethod.GET )
  public ModelAndView youtube_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/youtube_create"); // webapp/contents/youtube_create.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/youtube_create.do
  /**
   * Youtube 등록
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param youtube 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/youtube_create.do", method=RequestMethod.POST )
  public ModelAndView youtube_create(int cateno, int contentsno, String youtube, String passwd) {
    ModelAndView mav = new ModelAndView();

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("youtube", youtube);
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.youtube(hashMap); // 지도 등록
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp
    
    return mav;
  }

  // http://localhost:9090/resort/contents/youtube_delete.do
  /**
   * Youtube 삭제 처리
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param paswd 패스워드  
   * @param map 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/youtube_delete.do", method=RequestMethod.POST )
  public ModelAndView youtube_delete_proc(int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("youtube", ""); // map 삭제
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.youtube(hashMap); // 지도 삭제 처리
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp
    
    return mav;
  }
  
  //http://localhost:9090/resort/contents/mp3_create.do
  /**
  * MP3 등록 폼
  * @return
  */
  @RequestMapping(value="/contents/mp3_create.do", 
                             method=RequestMethod.GET )
  public ModelAndView mp3_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
   
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
   
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
   
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp3_create");  // webapp/contents/mp3_create.jsp
   
    return mav;
  }
 
  // http://localhost:9090/resort/contents/mp3_create.do
  /**
   * MP3 등록
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param youtube 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/mp3_create.do", 
                              method=RequestMethod.POST )
  public ModelAndView mp3_create(HttpServletRequest request,
                                                    ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // 파일 전송 코드 시작
    // -------------------------------------------------------------------
    String mp3 = ""; // mp3 파일
    
    String upDir = Tool.getRealPath(request, "/contents/storage/mp3"); // 절대 경로
    // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
    MultipartFile mf = contentsVO.getMp3MF();  // 파일 목록
    long fsize = mf.getSize();  // 파일 크기
    if (fsize > 0) { // 파일 크기 체크
      // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
      // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
      mp3 = Upload.saveFileSpring(mf, upDir); 
    }    
    // -------------------------------------------------------------------
    // 파일 전송 코드 종료
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp3", mp3);
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    this.contentsProc.mp3(hashMap); 
    
    mav.addObject("contentsno", contentsVO.getContentsno());
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp
    
    return mav;
  }
    
  // http://localhost:9090/resort/contents/mp3_delete.do
  /**
   * MP3 삭제 폼
   * @return
   */
  @RequestMapping(value="/contents/mp3_delete.do", 
                              method=RequestMethod.GET )
  public ModelAndView mp3_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp3_delete"); // webapp/contents/mp3_delete.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/mp3_delete.do
  /**
   * MP3 삭제 처리
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param paswd 패스워드  
   * @param map 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/mp3_delete.do", 
                              method=RequestMethod.POST )
  public ModelAndView mp3_delete_proc(HttpServletRequest request,
                                                            int cateno, 
                                                            int contentsno, 
                                                            String passwd) {
    ModelAndView mav = new ModelAndView();
    
    // -------------------------------------------------------------------
    // 파일 삭제 코드 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ContentsVO contentsVO = contentsProc.read(contentsno);
    System.out.println("mp3: " + contentsVO.getMp3());
    
    String upDir = Tool.getRealPath(request, "/contents/storage/mp3"); // 절대 경로
    boolean sw = Tool.deleteFile(upDir, contentsVO.getMp3());  // Folder에서 1건의 파일 삭제
    System.out.println("sw: " + sw);
    // -------------------------------------------------------------------
    // 파일 삭제 코드 종료
    // -------------------------------------------------------------------
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp3", ""); // mp3 파일명 삭제
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.mp3(hashMap);
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/mp4_create.do
  /**
   * MP4 등록 폼
   * @return
   */
  @RequestMapping(value="/contents/mp4_create.do", 
                              method=RequestMethod.GET )
  public ModelAndView mp4_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp4_create"); // webapp/contents/mp4_create.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/mp4_create.do
  /**
   * MP4 등록
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param youtube 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/mp4_create.do", 
                              method=RequestMethod.POST )
  public ModelAndView mp4_create(HttpServletRequest request,
                                                    ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // 파일 전송 코드 시작
    // -------------------------------------------------------------------
    String mp4 = ""; // mp3 파일
    
    String upDir = Tool.getRealPath(request, "/contents/storage/mp4"); // 절대 경로
    // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
    MultipartFile mf = contentsVO.getMp4MF();  // 파일 목록
    long fsize = mf.getSize();  // 파일 크기
    if (fsize > 0) { // 파일 크기 체크
      // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
      // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
      mp4 = Upload.saveFileSpring(mf, upDir); 
    }    
    // -------------------------------------------------------------------
    // 파일 전송 코드 종료
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp4", mp4);
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());
    
    this.contentsProc.mp4(hashMap); 
    
    mav.addObject("contentsno", contentsVO.getContentsno());
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp
    
    return mav;
  }
    
  // http://localhost:9090/resort/contents/mp4_delete.do
  /**
   * MP4 삭제 폼
   * @return
   */
  @RequestMapping(value="/contents/mp4_delete.do", 
                              method=RequestMethod.GET )
  public ModelAndView mp4_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);
    
    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp4_delete"); // webapp/contents/mp4_delete.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/mp4_delete.do
  /**
   * MP4 삭제 처리
   * @param cateno 카테고리 번호
   * @param contentsno 글번호
   * @param paswd 패스워드  
   * @param map 지도 스크립트
   * @return
   */
  @RequestMapping(value="/contents/mp4_delete.do", 
                              method=RequestMethod.POST )
  public ModelAndView mp4_delete_proc(HttpServletRequest request,
                                                            int cateno, 
                                                            int contentsno, 
                                                            String passwd) {
    ModelAndView mav = new ModelAndView();
    
    // -------------------------------------------------------------------
    // 파일 삭제 코드 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ContentsVO contentsVO = contentsProc.read(contentsno);
    // System.out.println("mp4: " + contentsVO.getMp4());
    
    String upDir = Tool.getRealPath(request, "/contents/storage/mp4"); // 절대 경로
    boolean sw = Tool.deleteFile(upDir, contentsVO.getMp4());  // Folder에서 1건의 파일 삭제
    // System.out.println("sw: " + sw);
    // -------------------------------------------------------------------
    // 파일 삭제 종료 시작
    // -------------------------------------------------------------------
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp4", ""); // map 삭제
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);
    
    this.contentsProc.mp4(hashMap);
    
    mav.addObject("contentsno", contentsno);
    
    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp
    
    return mav;
  }
  
  // http://localhost:9090/resort/contents/reply.do
  /**
   * 답변 폼
   * @return
   */
  @RequestMapping(value="/contents/reply.do", method=RequestMethod.GET )
  public ModelAndView reply(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    System.out.println("답변 대상: " + contentsno);
    
    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    mav.setViewName("/contents/reply"); // webapp/contents/reply.jsp
    
    return mav;
  }

  // http://localhost:9090/resort/contents/reply.do
  /**
   * 답변 등록 처리
   * @return
   */
  @RequestMapping(value="/contents/reply.do", 
                              method=RequestMethod.POST )
  public ModelAndView reply(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // 파일 전송 코드 시작
    // -------------------------------------------------------------------
    String file1 = "";     // main image
    String thumb1 = ""; // preview image
        
    String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // 절대 경로
    // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
    MultipartFile mf = contentsVO.getFile1MF();
    long size1 = mf.getSize();  // 파일 크기
    if (size1 > 0) { // 파일 크기 체크
      // mp3 = mf.getOriginalFilename(); // 원본 파일명, spring.jpg
      // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
      file1 = Upload.saveFileSpring(mf, upDir); 
      
      if (Tool.isImage(file1)) { // 이미지인지 검사
        // thumb 이미지 생성후 파일명 리턴됨, width: 120, height: 80
        thumb1 = Tool.preview(upDir, file1, 250, 200); 
      }
      
    }    
    // -------------------------------------------------------------------
    // 파일 전송 코드 종료
    // -------------------------------------------------------------------
    
    contentsVO.setIp(request.getRemoteAddr()); // 접속자 IP

    // PK return 됨
    contentsVO.setFile1(file1);
    contentsVO.setThumb1(thumb1);
    contentsVO.setSize1(size1);
    
     // --------------------------- 답변 관련 코드 시작 --------------------------
     ContentsVO parentVO = contentsProc.read(contentsVO.getContentsno()); // 부모글 정보 추출
     
     HashMap<String, Object> map = new HashMap<String, Object>();
     map.put("grpno", parentVO.getGrpno());
     map.put("ansnum",  parentVO.getAnsnum());
     contentsProc.increaseAnsnum(map); // 현재 등록된 부모글 ansnum을 1 증가시킴.

     contentsVO.setGrpno(parentVO.getGrpno()); // 부모의 그룹번호 할당
     contentsVO.setIndent(parentVO.getIndent() + 1); // 답변 차수 증가
     contentsVO.setAnsnum(parentVO.getAnsnum() + 1); // 부모 바로 아래 등록
     // --------------------------- 답변 관련 코드 종료 --------------------------
     
    int cnt = this.contentsProc.reply(contentsVO); // Call By Reference로 전송
    
    System.out.println("cnt: " + cnt);
    mav.addObject("cnt", cnt);
    
    // ---------------------------------------------------------------------------------------
    // cnt, contentsno return 
    // ---------------------------------------------------------------------------------------
    // Spring <-----> contentsVO <-----> MyBATIS
    // Spring과 MyBATIS가 ContentsVO를 공유하고 있음.
    // MyBATIS는 insert후 PK 컬럼인 contentsno변수에 새로 생성된 PK를 할당함.
    int contentsno = contentsVO.getContentsno();  // MyBATIS 리턴된 PK
    System.out.println("contentsno: " + contentsno);
    mav.addObject("contentsno", contentsno); // request에 저장
    // ---------------------------------------------------------------------------------------
    
    mav.addObject("cateno", contentsVO.getCateno());
    mav.addObject("url", "reply_msg"); // // webapp/contents/reply_msg.jsp
    
    if (cnt == 1) {  // 정상적으로 글이 등록된 경우만 글 수 증가
      this.cateProc.increaseCnt(contentsVO.getCateno()); // 글수 증가
    }
    /*
     * // mav.setViewName("/contents/create_msg"); //
     * mav.setViewName("redirect:/contents/list.do"); // spring 재호출
     * mav.setViewName("redirect:/contents/msg.do"); } else { //
     * mav.setViewName("/contents/create_msg"); // webapp/contents/create_msg.jsp }
     */    
    
    mav.setViewName("redirect:/contents/msg.do");
    
    return mav;
  }
      
  
}





