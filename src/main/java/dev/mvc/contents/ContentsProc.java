package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Tool;

@Component("dev.mvc.contents.ContentsProc")
  public class ContentsProc implements ContentsProcInter {
    @Autowired
    private ContentsDAOInter contentsDAO;

    public ContentsProc() {
      System.out.println("--> ContentsProc created.");
    }

    @Override
    public int create(ContentsVO contentsVO) {
       int cnt=this.contentsDAO.create(contentsVO);
      return cnt;
    }

    @Override
    public List<ContentsVO> list_all() {
      List<ContentsVO> list = this.contentsDAO.list_all();
      return list;
    }
    
    @Override
    public List<ContentsVO> list_by_cateno(int cateno) {
      List<ContentsVO> list = this.contentsDAO.list_by_cateno(cateno);
      return list;
    }
    
    /**
     * ��ȸ
     */
    @Override
    public ContentsVO read(int contentsno) {
      ContentsVO contentsVO = this.contentsDAO.read(contentsno);
      
      String title = contentsVO.getTitle();
      String content = contentsVO.getContent();
      
      title = Tool.convertChar(title);  // Ư�� ���� ó��
      // content = Tool.convertChar(content); // Ckeditor ���� ������� �� ��.
      
      contentsVO.setTitle(title);
      // contentsVO.setContent(content);  // Ckeditor ���� ������� �� ��.
      
      long size1 = contentsVO.getSize1();
      contentsVO.setSize1_label(Tool.unit(size1));

      return contentsVO;
    }
    
    @Override
    public ContentsVO read_update(int contentsno) {
      ContentsVO contentsVO = this.contentsDAO.read(contentsno);
      return contentsVO;
    }

    @Override
    public int update(ContentsVO contentsVO) {
      int cnt = this.contentsDAO.update(contentsVO);
      return cnt;
    }

    @Override
    public int passwd_check(HashMap<String, Object> hashMap) {
      int passwd_cnt = this.contentsDAO.passwd_check(hashMap);
      return passwd_cnt;
    } 
 
    @Override
    public int delete(int contentsno) {
      int cnt = this.contentsDAO.delete(contentsno);
      return cnt;
    }

    @Override
    public int img_create(ContentsVO contentsVO) {
      int cnt = this.contentsDAO.update_img(contentsVO);
      return cnt;
    }

    @Override
    public int img_update(ContentsVO contentsVO) {
      int cnt = this.contentsDAO.update_img(contentsVO);
      return cnt;
    }

    @Override
    public int img_delete(ContentsVO contentsVO) {
      int cnt = this.contentsDAO.update_img(contentsVO);
      return cnt;
    }

    @Override
    public int total_count() {
      int cnt = this.contentsDAO.total_count();
      return cnt;
    }

    @Override
    public int map(HashMap<String, Object> hashMap) {
      int cnt = this.contentsDAO.map(hashMap);
      return cnt;
    }

//    @Override
//    public ContentsVO map_read(int contentsno) {
//      ContentsVO contentsVO = this.contentsDAO.map_read(contentsno);
//      return contentsVO;
//    }

    @Override
    public int youtube(HashMap<String, Object> hashMap) {
      int cnt = this.contentsDAO.youtube(hashMap);
      return cnt;
    }

    @Override
    public int mp3(HashMap<String, Object> hashMap) {
      int cnt = this.contentsDAO.mp3(hashMap);
      return cnt;
    }

    @Override
    public int mp4(HashMap<String, Object> hashMap) {
      int cnt = this.contentsDAO.mp4(hashMap);
      return cnt;
    }

    @Override
    public List<ContentsVO> list_by_cateno_search(HashMap<String, Object> hashMap) {
      List<ContentsVO> list = contentsDAO.list_by_cateno_search(hashMap);
      return list;
    }

    @Override
    public int search_count(HashMap<String, Object> hashMap) {
      int count = contentsDAO.search_count(hashMap);
      return count;
    }

    @Override
    public List<ContentsVO> list_by_cateno_search_paging(HashMap<String, Object> map) {
      /* 
      ���������� ����� ���� ���ڵ� ��ȣ ��� ���ذ�, nowPage�� 1���� ����
      1 ������ ���� rownum: nowPage = 1, (1 - 1) * 10 --> 0 
      2 ������ ���� rownum: nowPage = 2, (2 - 1) * 10 --> 10
      3 ������ ���� rownum: nowPage = 3, (3 - 1) * 10 --> 20
      */
      int beginOfPage = ((Integer)map.get("nowPage") - 1) * Contents.RECORD_PER_PAGE;
     
      // ���� rownum ����
      // 1 ������ = 0 + 1, 2 ������ = 10 + 1, 3 ������ = 20 + 1 
      int startNum = beginOfPage + 1;
      
      //  ���� rownum
      // 1 ������ = 0 + 10, 2 ������ = 0 + 20, 3 ������ = 0 + 30
      int endNum = beginOfPage + Contents.RECORD_PER_PAGE;   
      /*
      1 ������: WHERE r >= 1 AND r <= 10
      2 ������: WHERE r >= 11 AND r <= 20
      3 ������: WHERE r >= 21 AND r <= 30
      */
      map.put("startNum", startNum);
      map.put("endNum", endNum);
     
      List<ContentsVO> list = this.contentsDAO.list_by_cateno_search_paging(map);
      
      return list;
    }
    
    /** 
     * SPAN�±׸� �̿��� �ڽ� ���� ����, 1 ���������� ���� 
     * ���� ������: 11 / 22   [����] 11 12 13 14 15 16 17 18 19 20 [����] 
     *
     * @param listFile ��� ���ϸ� 
     * @param cateno ī�װ���ȣ 
     * @param search_count �˻�(��ü) ���ڵ�� 
     * @param nowPage     ���� ������
     * @param word �˻���
     * @return ����¡ ���� ���ڿ�
     */ 
    @Override
    public String pagingBox(String listFile, int cateno, int search_count, int nowPage, String word){ 
      int totalPage = (int)(Math.ceil((double)search_count/Contents.RECORD_PER_PAGE)); // ��ü ������  
      
      int totalGrp = (int)(Math.ceil((double)totalPage/Contents.PAGE_PER_BLOCK));// ��ü �׷� 
      
      int nowGrp = (int)(Math.ceil((double)nowPage/Contents.PAGE_PER_BLOCK));    // ���� �׷� 
      
      int startPage = ((nowGrp - 1) * Contents.PAGE_PER_BLOCK) + 1; // Ư�� �׷��� ������ ��� ����  
      
      int endPage = (nowGrp * Contents.PAGE_PER_BLOCK);             // Ư�� �׷��� ������ ��� ����   
       
      StringBuffer str = new StringBuffer(); 
       
      str.append("<style type='text/css'>"); 
      str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}"); 
      str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}"); 
      str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}"); 
      str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}"); 
      str.append("  .span_box_1{"); 
      str.append("    text-align: center;");    
      str.append("    font-size: 1em;"); 
      str.append("    border: 1px;"); 
      str.append("    border-style: solid;"); 
      str.append("    border-color: #cccccc;"); 
      str.append("    padding:1px 6px 1px 6px; /*��, ������, �Ʒ�, ����*/"); 
      str.append("    margin:1px 2px 1px 2px; /*��, ������, �Ʒ�, ����*/"); 
      str.append("  }"); 
      str.append("  .span_box_2{"); 
      str.append("    text-align: center;");    
      str.append("    background-color: #668db4;"); 
      str.append("    color: #FFFFFF;"); 
      str.append("    font-size: 1em;"); 
      str.append("    border: 1px;"); 
      str.append("    border-style: solid;"); 
      str.append("    border-color: #cccccc;"); 
      str.append("    padding:1px 6px 1px 6px; /*��, ������, �Ʒ�, ����*/"); 
      str.append("    margin:1px 2px 1px 2px; /*��, ������, �Ʒ�, ����*/"); 
      str.append("  }"); 
      str.append("</style>"); 
      str.append("<DIV id='paging'>"); 
//      str.append("���� ������: " + nowPage + " / " + totalPage + "  "); 
   
      // ���� 10�� �������� �̵�
      // nowGrp: 1 (1 ~ 10 page)
      // nowGrp: 2 (11 ~ 20 page)
      // nowGrp: 3 (21 ~ 30 page) 
      // ���� 2�׷��� ���: (2 - 1) * 10 = 1�׷��� ������ ������ 10
      // ���� 3�׷��� ���: (3 - 1) * 10 = 2�׷��� ������ ������ 20
      int _nowPage = (nowGrp-1) * Contents.PAGE_PER_BLOCK;  
      if (nowGrp >= 2){ 
        str.append("<span class='span_box_1'><A href='"+listFile+"?&word="+word+"&nowPage="+_nowPage+"&cateno="+cateno+"'>����</A></span>"); 
      } 
   
      // �߾��� ������ ���
      for(int i=startPage; i<=endPage; i++){ 
        if (i > totalPage){ // ������ �������� �Ѿ�ٸ� ���� ��� ����
          break; 
        } 
    
        if (nowPage == i){ // �������� ������������ ���ٸ� CSS ����(������ ��)
          str.append("<span class='span_box_2'>"+i+"</span>"); // ���� ������, ���� 
        }else{
          // ���� �������� �ƴ� �������� �̵��� �����ϵ��� ��ũ�� ����
          str.append("<span class='span_box_1'><A href='"+listFile+"?word="+word+"&nowPage="+i+"&cateno="+cateno+"'>"+i+"</A></span>");   
        } 
      } 
   
      // 10�� ���� �������� �̵�
      // nowGrp: 1 (1 ~ 10 page),  nowGrp: 2 (11 ~ 20 page),  nowGrp: 3 (21 ~ 30 page) 
      // ���� 1�׷��� ���: (1 * 10) + 1 = 2�׷��� ���������� 11
      // ���� 2�׷��� ���: (2 * 10) + 1 = 3�׷��� ���������� 21
      _nowPage = (nowGrp * Contents.PAGE_PER_BLOCK)+1;  
      if (nowGrp < totalGrp){ 
        str.append("<span class='span_box_1'><A href='"+listFile+"?&word="+word+"&nowPage="+_nowPage+"&cateno="+cateno+"'>����</A></span>"); 
      } 
      str.append("</DIV>"); 
       
      return str.toString(); 
    }
   
    @Override
    public int increaseAnsnum(HashMap<String, Object> map) {
      int count = contentsDAO.increaseAnsnum(map);
      return count;
    }
    
    @Override
    public int reply(ContentsVO contentsVO) {
      int count = contentsDAO.reply(contentsVO);
      return count;
    }

    @Override
    public List<Contents_MemberVO> list_by_cateno_search_paging_join(HashMap<String, Object> map) {
      /* 
      ���������� ����� ���� ���ڵ� ��ȣ ��� ���ذ�, nowPage�� 1���� ����
      1 ������ ���� rownum: nowPage = 1, (1 - 1) * 10 --> 0 
      2 ������ ���� rownum: nowPage = 2, (2 - 1) * 10 --> 10
      3 ������ ���� rownum: nowPage = 3, (3 - 1) * 10 --> 20
      */
      int beginOfPage = ((Integer)map.get("nowPage") - 1) * Contents.RECORD_PER_PAGE;
     
      // ���� rownum ����
      // 1 ������ = 0 + 1, 2 ������ = 10 + 1, 3 ������ = 20 + 1 
      int startNum = beginOfPage + 1;
      
      //  ���� rownum 
      // 1 ������ = 0 + 10, 2 ������ = 0 + 20, 3 ������ = 0 + 30
      int endNum = beginOfPage + Contents.RECORD_PER_PAGE;   
      /*
      1 ������: WHERE r >= 1 AND r <= 10
      2 ������: WHERE r >= 11 AND r <= 20
      3 ������: WHERE r >= 21 AND r <= 30
      */
      map.put("startNum", startNum);
      map.put("endNum", endNum);
     
      List<Contents_MemberVO> list = this.contentsDAO.list_by_cateno_search_paging_join(map);
      
      return list;
    }

    @Override
    public int increaseReplycnt(int contentsno) {
      int count = contentsDAO.increaseReplycnt(contentsno);
      return count;
    }

    @Override
    public int decreaseReplycnt(int contentsno) {
      int count = contentsDAO.decreaseReplycnt(contentsno);
      return count;
    }
    
}





