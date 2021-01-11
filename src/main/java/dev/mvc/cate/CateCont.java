package dev.mvc.cate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.categrp.CategrpProcInter;
import dev.mvc.categrp.CategrpVO;
import dev.mvc.contents.ContentsProcInter;

@Controller
public class CateCont {
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;
  
  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;
  
  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  public CateCont() {
    System.out.println("--> CateCont created.");
  }

  /**
   * ����� http://localhost:9090/resort/cate/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/create.do", method = RequestMethod.GET)
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/create"); // /cate/create.jsp

    return mav;
  }

  /**
   * ���ó��
   * http://localhost:9090/resort/cate/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/create.do", method = RequestMethod.POST)
  public ModelAndView create(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/create_msg");

    // System.out.println("--> categrpno: " + cateVO.getCategrpno());
    
    int cnt = this.cateProc.create(cateVO);
    mav.addObject("cnt", cnt);

    return mav;
  }
  
  /**
   * ��ü ��� http://localhost:9090/resort/cate/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/list_all.do", method = RequestMethod.GET)
  public ModelAndView list_all() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/list_all"); // /webapp/cate/list_all.jsp

    // List<CateVO> list = this.cateProc.list_cateno_asc();
    List<CateVO> list = this.cateProc.list_seqno_asc();
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * ��� http://localhost:9090/resort/cate/list.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value = "/cate/list.do", method = RequestMethod.GET) public
   * ModelAndView list(int categrpno) { ModelAndView mav = new ModelAndView();
   * mav.setViewName("/cate/list"); // /webapp/cate/list.jsp
   * 
   * // Categrp ���� CategrpVO categrpVO = this.categrpProc.read(categrpno);
   * mav.addObject("categrpVO", categrpVO);
   * 
   * // List<CateVO> list = this.cateProc.list_cateno_asc(); List<CateVO> list =
   * this.cateProc.list_by_categrpno(categrpno); mav.addObject("list", list);
   * 
   * return mav; // forward }
   */
  
  /**
   * ��ȸ + ������ http://localhost:9090/resort/cate/read_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/read_update.do", method = RequestMethod.GET)
  public ModelAndView read_update(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/read_update"); // /webapp/cate/read_update.jsp

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    List<CateVO> list = this.cateProc.list_by_categrpno(categrpno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ���� ó��
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/update.do", method = RequestMethod.POST)
  public ModelAndView update(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update(cateVO);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("/cate/update_msg"); // webapp/cate/update_msg.jsp

    return mav;
  }
  
  /**
   * ��ȸ + ������ http://localhost:9090/resort/cate/read_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/cate/read_delete.do", method = RequestMethod.GET)
  public ModelAndView read_delete(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/cate/read_delete"); // /webapp/cate/read_delete.jsp

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    List<CateVO> list = this.cateProc.list_by_categrpno(categrpno);
    mav.addObject("list", list);

    return mav; // forward
  }
  
  /**
   * ���� ó��
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(int cateno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.delete(cateno);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("/cate/delete_msg"); // webapp/cate/delete_msg.jsp

    return mav;
  }
  
  /**
   * �켱���� ���� up 10 �� 1
   * @param cateno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/cate/update_seqno_up.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_up(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_seqno_up(cateno);
    // mav.addObject("cnt", cnt); // request�� ����
    
    mav.setViewName("redirect:/cate/list.do?categrpno=" + categrpno); // �Ķ���� �ڵ� ���� �ȵ�. 

    return mav;
  }
  
  /**
   * �켱���� ���� up 1 �� 10
   * @param cateno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/cate/update_seqno_down.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_down(int cateno, int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_seqno_down(cateno);
    // mav.addObject("cnt", cnt); // request�� ����
    
    mav.setViewName("redirect:/cate/list.do?categrpno=" + categrpno); // �Ķ���� �ڵ� ���� �ȵ�. 

    return mav;
  }
  
  /**
   * ��¸�� ����
   * 
   * @param cateVO
   * @return
   */
  @RequestMapping(value = "/cate/update_visible.do", method = RequestMethod.GET)
  public ModelAndView update_visible(CateVO cateVO) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.cateProc.update_visible(cateVO);
    mav.addObject("cnt", cnt); // request�� ����

    if (cnt == 1) { 
      mav.setViewName("redirect:/cate/list.do?categrpno=" + cateVO.getCategrpno()); // request ��ü�� ������ �ȵ�. 
    } else {
      CateVO vo = this.cateProc.read(cateVO.getCateno());
      String name = vo.getName();
      mav.addObject("name", name);
      mav.setViewName("/cate/update_visible_msg"); // /cate/update_visible_msg.jsp
    }
    return mav;
  }
  
  // http://localhost:9090/resort/cate/list_join.do
  /**
   * categrp + cate join ��ü ���
   * @return
   */
  @RequestMapping(value="/cate/list_join.do", method=RequestMethod.GET )
  public ModelAndView list_join() {
    ModelAndView mav = new ModelAndView();
    
    List<Categrp_Cate_join> list = this.cateProc.list_join();
    mav.addObject("list", list); // request.setAttribute("list", list);

    mav.setViewName("/cate/list_join"); // webapp/cate/list_join.jsp
    return mav;
  }
  
  // http://localhost:9090/resort/cate/list.do ������ url ���
  /**
   * categrp + cate join ��ü ���
   * @return
   */
  @RequestMapping(value="/cate/list.do", method=RequestMethod.GET )
  public ModelAndView list_join_by_categrpno(int categrpno) {
    ModelAndView mav = new ModelAndView();
    
    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);
    
    List<Categrp_Cate_join> list = this.cateProc.list_join_by_categrpno(categrpno);
    mav.addObject("list", list); // request.setAttribute("list", list);

    mav.setViewName("/cate/list_join_by_categrpno"); // webapp/cate/list_join_by_categrpno.jsp
    return mav;
  }
 
  /**
   * ���յ� ī�װ� �׷캰 ī�װ� ���
   * http://localhost:9090/resort/cate/list_index_left.do
   * @param request
   * @return
   */
 @RequestMapping(value="/cate/list_index_left.do", method=RequestMethod.GET)
 public ModelAndView list_index_left(HttpServletRequest request){
   // System.out.println("--> list_index() GET called.");
   ModelAndView mav = new ModelAndView();
   mav.setViewName("/cate/list_index_left"); // webapp/cate/list_index_left.jsp
   
   List<CategrpVO> categrp_list = categrpProc.list_seqno_asc(); // ī�װ� �׷� ���
   
   // Categrp: name, Cate: name ���� ���
   ArrayList<String> name_title_list = new ArrayList<String>();   
   
   StringBuffer url = new StringBuffer(); // ī�װ� ���� ��ũ ����
 
   // ī�װ� �׷� ���� ��ŭ ��ȯ
   for (int index = 0; index < categrp_list.size(); index++) {
     CategrpVO categrpVO = categrp_list.get(index); // �ϳ��� ī�װ� �׷� ����
 
     // ī�װ��׷�� �߰�
     name_title_list.add("<LI class='categrp_name'>"+ categrpVO.getName() + "</LI>");

     // ī�װ� Join ���, Ư�� ī�װ� �׷쿡 �ش��ϴ� ī�װ� ������.
     List<Categrp_Cate_join> cate_list = cateProc.list_join_by_categrpno(categrpVO.getCategrpno()); 
     
     // ī�װ� ������ŭ ��ȯ
     for (int j=0; j < cate_list.size(); j++) {
       Categrp_Cate_join categrp_Cate_join = cate_list.get(j);
       
       String name = categrp_Cate_join.getName(); // ī�װ� �̸�
       int cnt = categrp_Cate_join.getCnt();
       
       url.append("<LI class='cate_name'>");
       url.append("  <A href='" + request.getContextPath()+ "/contents/list.do?cateno="+categrp_Cate_join.getCateno()+"'>");
       url.append(name);
       url.append("  </A>");
       url.append("  <span style='font-size: 0.9em; color: #555555;'>("+cnt+")</span>");
       url.append("</LI>");
       
       name_title_list.add(url.toString()); // ��� ��Ͽ� �ϳ��� cate�� �߰� 
       
       url.delete(0, url.toString().length()); // ���ο� ī�װ� ����� �����ϱ����� StringBuffer ���ڿ� ����
       
     }
   }
   
   mav.addObject("name_title_list", name_title_list);
   mav.addObject("total_count", contentsProc.total_count());
   
   return mav;
 } 
  
  
}




