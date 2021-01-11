package dev.mvc.categrp;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.admin.AdminProcInter;

@Controller
public class CategrpCont {
  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;

  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc") // �̸� ����
  private AdminProcInter adminProc;
  
  public CategrpCont() {
    System.out.println("--> CategrpCont created.");
  }

  /**
   * ����� http://localhost:9090/resort/categrp/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/create.do", method = RequestMethod.GET)
  public ModelAndView create() {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/create"); // /webapp/categrp/create.jsp

    return mav; // forward
  }

  /**
   * ��� ó�� http://localhost:9090/resort/categrp/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/create.do", method = RequestMethod.POST)
  public ModelAndView create(CategrpVO categrpVO) {
    // request.setAttribute("categrpVO", categrpVO) �ڵ� ����

    ModelAndView mav = new ModelAndView();
    // mav.setViewName("/categrp/create_msg"); // /webapp/categrp/create_msg.jsp

    int cnt = this.categrpProc.create(categrpVO); // ��� ó��
    
    if (cnt == 0) {
      mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)

      mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter ����
      mav.setViewName("redirect:/categrp/msg.do"); 
      
    } else {
      mav.setViewName("redirect:/categrp/list.do");
    }
    
    return mav;
  }
  
  /**
   * Ajax ��� ��� ó�� http://localhost:9090/resort/categrp/create_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/create_ajax.do", method = RequestMethod.POST,
                          produces = "text/plain;charset=UTF-8")
  public String create_ajax(CategrpVO categrpVO) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    int cnt = this.categrpProc.create(categrpVO); // ��� ó��
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);

    return json.toString();
  }

//  /**
//   * ��� http://localhost:9090/resort/categrp/list.do
//   * 
//   * @return
//   */
//  @RequestMapping(value = "/categrp/list.do", method = RequestMethod.GET)
//  public ModelAndView list() {
//    ModelAndView mav = new ModelAndView();
//
//    // List<CategrpVO> list = this.categrpProc.list_categrpno_asc();
//    List<CategrpVO> list = this.categrpProc.list_seqno_asc();
//    mav.addObject("list", list);
//
//    // mav.setViewName("/categrp/list"); // /webapp/categrp/list.jsp
//    mav.setViewName("/categrp/list_ajax"); // /webapp/categrp/list_ajax.jsp
//    
//    return mav; // forward
//  }
  
  /**
   * ��� http://localhost:9090/resort/categrp/list.do
   * 
   * @return
   */
  @RequestMapping(value="/categrp/list.do", method=RequestMethod.GET)
  public ModelAndView list(HttpSession session) {
    ModelAndView mav = new ModelAndView();
    
    if (adminProc.isAdmin(session)) { // ������ �α����� ���
      List<CategrpVO> list = categrpProc.list_seqno_asc();
      
      mav.addObject("list", list);
      // mav.setViewName("/categrp/list"); // /webapp/categrp/list.jsp
      mav.setViewName("/categrp/list_ajax"); // /webapp/categrp/list_ajax.jsp
    } else {
      mav.setViewName("/admin/login_need"); // /webapp/admin/login_need.jsp    
    }

    // mav.setViewName("redirect:/categrp/create_msg.jsp?count=" + count);
        
    return mav;
  }


  /**
   * ��ȸ + ������ http://localhost:9090/resort/categrp/read_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/categrp/read_update.do", method = RequestMethod.GET)
  public ModelAndView read_update(int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/read_update"); // /webapp/categrp/read_update.jsp

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    List<CategrpVO> list = this.categrpProc.list_seqno_asc();
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * Ajax + read http://localhost:9090/resort/categrp/read_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/read_ajax.do", method = RequestMethod.GET,
                          produces = "text/plain;charset=UTF-8")
  public String read_ajax(int categrpno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    CategrpVO categrpVO = this.categrpProc.read(categrpno);

    JSONObject json = new JSONObject();
    json.put("categrpno", categrpno);
    json.put("name", categrpVO.getName());
    json.put("seqno", categrpVO.getSeqno());
    json.put("visible", categrpVO.getVisible());

    return json.toString();
  }

  
  /**
   * ���� ó��
   * 
   * @param categrpVO
   * @return
   */
  @RequestMapping(value = "/categrp/update.do", method = RequestMethod.POST)
  public ModelAndView update(CategrpVO categrpVO) {
    // CategrpVO categrpVO <FORM> �±��� ������ �ڵ� ������.
    // request.setAttribute("categrpVO", categrpVO); �ڵ� ����

    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update(categrpVO);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("/categrp/update_msg"); // webapp/categrp/update_msg.jsp

    return mav;
  }
  
  /**
   * Ajax ��� ��� ó�� http://localhost:9090/resort/categrp/update_ajax.do
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/update_ajax.do", method = RequestMethod.POST,
                          produces = "text/plain;charset=UTF-8")
  public String update_ajax(CategrpVO categrpVO) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    int cnt = this.categrpProc.update(categrpVO); // ��� ó��
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);

    return json.toString();
  }

  /**
   * ������ http://localhost:9090/resort/categrp/read_delete.do
   * @return
   */
  @RequestMapping(value = "/categrp/read_delete.do", method = RequestMethod.GET)
  public ModelAndView read_delete(int categrpno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/categrp/read_delete"); // /webapp/categrp/read_delete.jsp

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);

    List<CategrpVO> list = this.categrpProc.list_seqno_asc();
    mav.addObject("list", list);

    return mav; // forward
  }

  /**
   * ���� ó��
   * @param categrpno
   * @return
   */
  @RequestMapping(value = "/categrp/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.delete(categrpno);
    // mav.addObject("cnt", cnt); // request�� ����

    // mav.setViewName("/categrp/delete_msg"); // /webapp/categrp/delete_msg.jsp

    if (cnt == 0) {
      mav.addObject("cnt", cnt); // request.setAttribute("cnt", cnt)

      mav.addObject("url", "delete_msg"); // create_msg.jsp, redirect parameter ����
      mav.setViewName("redirect:/categrp/msg.do"); 
      
    } else {
      mav.setViewName("redirect:/categrp/list.do");
    }
    
    return mav;
  }
  
  /**
   * ���� ó�� + Ajax
   * @param categrpno
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/categrp/delete_ajax.do", method = RequestMethod.POST,
                          produces = "text/plain;charset=UTF-8")
  public String delete_ajax(int categrpno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    int cnt = this.categrpProc.delete(categrpno);
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
    
    return json.toString();
  }
 
  // http://localhost:9090/resort/categrp/update_seqno_up.do?categrpno=1
  // http://localhost:9090/resort/categrp/update_seqno_up.do?categrpno=1000
  /**
   * �켱���� ���� up 10 �� 1
   * @param categrpno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/categrp/update_seqno_up.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_up(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update_seqno_up(categrpno);
    mav.addObject("cnt", cnt); // request�� ����
    
    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);
    

    // mav.setViewName("/categrp/update_seqno_up_msg"); // /categrp/update_seqno_up_msg.jsp
    mav.setViewName("redirect:/categrp/list.do"); // request ��ü�� ������ �ȵ�. 

    return mav;
  }
  
  // http://localhost:9090/resort/categrp/update_seqno_down.do?categrpno=1
  // http://localhost:9090/resort/categrp/update_seqno_down.do?categrpno=1000
  /**
   * �켱���� ���� up 1 �� 10
   * @param categrpno ī�װ� ��ȣ
   * @return
   */
  @RequestMapping(value="/categrp/update_seqno_down.do", 
                              method=RequestMethod.GET )
  public ModelAndView update_seqno_down(int categrpno) {
    ModelAndView mav = new ModelAndView();

    int cnt = this.categrpProc.update_seqno_down(categrpno);
    mav.addObject("cnt", cnt); // request�� ����

    CategrpVO categrpVO = this.categrpProc.read(categrpno);
    mav.addObject("categrpVO", categrpVO);
    
    // mav.setViewName("/categrp/update_seqno_down_msg"); // /categrp/update_seqno_down_msg.jsp
    mav.setViewName("redirect:/categrp/list.do"); // request ��ü�� ������ �ȵ�. 

    return mav;
  }
  
  /**
   * ��¸�� ����
   * 
   * @param categrpVO
   * @return
   */
  @RequestMapping(value = "/categrp/update_visible.do", method = RequestMethod.GET)
  public ModelAndView update_visible(CategrpVO categrpVO) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("--> categrpno: " + categrpVO.getCategrpno());
    // System.out.println("--> visible: " + categrpVO.getVisible());
        
    int cnt = this.categrpProc.update_visible(categrpVO);
    mav.addObject("cnt", cnt); // request�� ����

    mav.setViewName("redirect:/categrp/list.do"); // request ��ü�� ������ �ȵ�. 

    return mav;
  }
  
  /**
   * ���ΰ�ħ�� �����ϴ� �޽��� ���
   * @return
   */
  @RequestMapping(value="/categrp/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    
    // ��� ó�� �޽���: create_msg --> /categrp/create_msg.jsp
    // ���� ó�� �޽���: update_msg --> /categrp/update_msg.jsp
    // ���� ó�� �޽���: delete_msg --> /categrp/delete_msg.jsp
    mav.setViewName("/categrp/" + url); // forward
    
    return mav; // forward
  }
  
}





