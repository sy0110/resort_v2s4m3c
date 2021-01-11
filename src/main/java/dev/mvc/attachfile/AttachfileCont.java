package dev.mvc.attachfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.contents.ContentsProcInter;
import dev.mvc.contents.ContentsVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
public class AttachfileCont {
  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  @Autowired
  @Qualifier("dev.mvc.attachfile.AttachfileProc")
  private AttachfileProcInter attachfileProc;

  public AttachfileCont() {
    System.out.println("--> AttachfileCont created.");
  }

  // http://localhost:9090/resort/attachfile/create.do
  /**
   * ��� �� http://localhost:9090/resort/attachfile/create.do X
   * http://localhost:9090/resort/attachfile/create.do?cateno=2&contentsno=1 O
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/create.do", method = RequestMethod.GET)
  public ModelAndView create(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/attachfile/create"); // webapp/attachfile/create.jsp

    return mav;
  }

  /**
   * ��� ó��
   * 
   * @param ra
   * @param request
   * @param attachfileVO
   * @param categrpno
   * @return
   */
  @RequestMapping(value = "/attachfile/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request, AttachfileVO attachfileVO, int cateno) {
    // System.out.println("--> categrpno: " + categrpno);

    ModelAndView mav = new ModelAndView();
    // ---------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // ---------------------------------------------------------------
    int contentsno = attachfileVO.getContentsno(); // �θ�� ��ȣ
    String fname = ""; // ���� ���ϸ�
    String fupname = ""; // ���ε�� ���ϸ�
    long fsize = 0; // ���� ������
    String thumb = ""; // Preview �̹���
    int upload_count = 0; // ����ó���� ���ڵ� ����

    String upDir = Tool.getRealPath(request, "/attachfile/storage");

    // ���� ������ ����� fnamesMF ��ü�� ������.
    List<MultipartFile> fnamesMF = attachfileVO.getFnamesMF();

    int count = fnamesMF.size(); // ���� ���� ����
    if (count > 0) {
      for (MultipartFile multipartFile : fnamesMF) { // ���� ����, 1���̻� ���� ó��
        fsize = multipartFile.getSize(); // ���� ũ��
        if (fsize > 0) { // ���� ũ�� üũ
          fname = multipartFile.getOriginalFilename(); // ���� ���ϸ�
          fupname = Upload.saveFileSpring(multipartFile, upDir); // ���� ����, ���ε�� ���ϸ�

          if (Tool.isImage(fname)) { // �̹������� �˻�
            thumb = Tool.preview(upDir, fupname, 200, 150); // thumb �̹��� ����
          }
        }
        AttachfileVO vo = new AttachfileVO();
        vo.setContentsno(contentsno);
        vo.setFname(fname);
        vo.setFupname(fupname);
        vo.setThumb(thumb);
        vo.setFsize(fsize);

        // ���� 1�� ��� ���� dbms ����, ������ 20���̸� 20���� record insert.
        upload_count = upload_count + attachfileProc.create(vo);
      }
    }
    // -----------------------------------------------------
    // ���� ���� �ڵ� ����
    // -----------------------------------------------------

    mav.addObject("contentsno", contentsno); // redirect parameter ����
    mav.addObject("cateno", cateno); // redirect parameter ����
    mav.addObject("upload_count", upload_count); // redirect parameter ����
    mav.addObject("url", "create_msg"); // create_msg.jsp, redirect parameter ����

    mav.setViewName("redirect:/attachfile/msg.do"); // ���ΰ�ħ ����

    return mav;
  }

  /**
   * ���ΰ�ħ�� �����ϴ� �޽��� ���
   * 
   * @param memberno
   * @return
   */
  @RequestMapping(value = "/attachfile/msg.do", method = RequestMethod.GET)
  public ModelAndView msg(String url) {
    ModelAndView mav = new ModelAndView();

    // ��� ó�� �޽���: create_msg --> /attachfile/create_msg.jsp
    // ���� ó�� �޽���: update_msg --> /attachfile/update_msg.jsp
    // ���� ó�� �޽���: delete_msg --> /attachfile/delete_msg.jsp
    mav.setViewName("/attachfile/" + url); // forward

    return mav; // forward
  }

  /**
   * ��� http://localhost:9090/ojt/attachfile/list.do
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/list.do", method = RequestMethod.GET)
  public ModelAndView list() {
    ModelAndView mav = new ModelAndView();

    List<AttachfileVO> list = attachfileProc.list();
    mav.addObject("list", list);

    mav.setViewName("/attachfile/list");

    return mav;
  }

  /**
   * �ϳ��� contentsno�� ��� http://localhost:9090/ojt/attachfile/list_by_contentsno.do
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/list_by_contentsno.do", method = RequestMethod.GET)
  public ModelAndView list_by_contentsno(int contentsno) {
    ModelAndView mav = new ModelAndView();

    List<AttachfileVO> list = attachfileProc.list_by_contentsno(contentsno);
    mav.addObject("list", list);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    // System.out.println("--> title: " + contentsVO.getTitle());

    // mav.addObject("title", contentsVO.getTitle());
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/attachfile/list_by_contentsno"); // list_by_contentsno.jsp

    return mav;
  }

  /**
   * ÷�� ���� 1�� ���� ó��
   * 
   * @return
   */
  @RequestMapping(value = "/attachfile/delete.do", method = RequestMethod.POST)
  public ModelAndView delete_proc(HttpServletRequest request, int attachfileno,
      @RequestParam(value = "contentsno", defaultValue = "0") int contentsno, String rurl) {
    ModelAndView mav = new ModelAndView();

    // ������ ���� ������ �о��.
    AttachfileVO attachfileVO = attachfileProc.read(attachfileno);

    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // ���� ���
    Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder���� 1���� ���� ����
    Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1���� Thumb ���� ����

    // DBMS���� 1���� ���� ����
    attachfileProc.delete(attachfileno);

    List<AttachfileVO> list = attachfileProc.list(); // ��� ���� ��ħ
    mav.addObject("list", list);

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/attachfile/" + rurl);

    return mav;
  }

  // http://localhost:9090/resort/attachfile/count_by_contentsno.do?contentsno=14
  /**
   * �θ�Ű�� ���� ����
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/attachfile/count_by_contentsno.do", method = RequestMethod.GET, 
                          produces = "text/plain;charset=UTF-8")
  public String count_by_contentsno(int contentsno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    int cnt = this.attachfileProc.count_by_contentsno(contentsno);
 
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);

    return json.toString();
  }

  // http://localhost:9090/resort/attachfile/delete_by_contentsno.do?contentsno=13
  /**
   * FK�� ����� ���ڵ� ����
   * @param request
   * @param contentsno
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/attachfile/delete_by_contentsno.do", method = RequestMethod.POST,
                          produces = "text/plain;charset=UTF-8")
  public String delete_by_contentsno(HttpServletRequest request,
                                              @RequestParam(value = "contentsno", defaultValue = "0") int contentsno) {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    List<AttachfileVO> list = this.attachfileProc.list_by_contentsno(contentsno);
    int cnt = 0; // ������ ���ڵ� ����

    String upDir = Tool.getRealPath(request, "/attachfile/storage"); // ���� ���
    
    for (AttachfileVO attachfileVO: list) { // ���� ������ŭ ��ȯ
      Tool.deleteFile(upDir, attachfileVO.getFupname()); // Folder���� 1���� ���� ����
      Tool.deleteFile(upDir, attachfileVO.getThumb()); // 1���� Thumb ���� ����
    
      attachfileProc.delete(attachfileVO.getAttachfileno());  // DBMS���� 1���� ���� ����
      cnt = cnt + 1;

    }
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);

    return json.toString();
  }

  /**
   * ZIP ���� �� ���� �ٿ�ε�
   * @param request
   * @param contentsno ���� ����� �����ö� ����� �۹�ȣ
   * @return
   */
  @RequestMapping(value = "/attachfile/downzip.do", 
                             method = RequestMethod.GET)
  public ModelAndView downzip(HttpServletRequest request, int contentsno) {
    ModelAndView mav = new ModelAndView();
    
    // �۹�ȣ�� �ش��ϴ� ���� ��� ����
    List<AttachfileVO> attachfile_list = attachfileProc.list_by_contentsno(contentsno);
    
    // ���� ����� ���ϸ� ����
    ArrayList<String> files_array = new ArrayList<String>();
    for(AttachfileVO attachfileVO:attachfile_list) {
      files_array.add(attachfileVO.getFupname());
    }
    
    String dir = "/attachfile/storage";
    String upDir = Tool.getRealPath(request, dir); // ���� ���
    
    // ����� ���ϸ�, ���� ������ �ٿ�ε��� �浹 ó��
    String zip = "download_files_" + Tool.getRandomDate() + ".zip"; 
    String zip_filename = upDir + zip;
    
    String[] zip_src = new String[files_array.size()]; // ���� ������ŭ �迭 ����
    
    for (int i=0; i < files_array.size(); i++) {
      zip_src[i] = upDir + "/" + files_array.get(i); // ���� ��� ����      
    }
 
    byte[] buffer = new byte[4096]; // 4 KB
    
    try {
      ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zip_filename));
      
      for(int index=0; index < zip_src.length; index++) {
        FileInputStream in = new FileInputStream(zip_src[index]);
        
        Path path = Paths.get(zip_src[index]);
        String zip_src_file = path.getFileName().toString();
        // System.out.println("zip_src_file: " + zip_src_file);
        
        ZipEntry zipEntry = new ZipEntry(zip_src_file);
        zipOutputStream.putNextEntry(zipEntry);
        
        int length = 0;
        // 4 KB�� �о buffer �迭�� ������ ���� ����Ʈ���� length�� ����
        while((length = in.read(buffer)) > 0) {
          zipOutputStream.write(buffer, 0, length); // ����� ����, ���뿡���� ���� ��ġ, ����� ����
          
        }
        zipOutputStream.closeEntry();
        in.close();
      }
      zipOutputStream.close();
      
      File file = new File(zip_filename);
      
      if (file.exists() && file.length() > 0) {
        System.out.println(zip_filename + " ���� �Ϸ�");
      }
      
//      if (file.delete() == true) {
//        System.out.println(zip_filename + " ������ �����߽��ϴ�.");
//      }
 
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
 
    // download ���� ����
    mav.setViewName("redirect:/download2?dir=" + dir + "&filename=" + zip + "&downname=" + zip);    
    
    return mav;
  }
  
  
  
}








