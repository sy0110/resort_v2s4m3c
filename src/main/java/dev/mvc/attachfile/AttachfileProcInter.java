package dev.mvc.attachfile;

import java.util.List;

public interface AttachfileProcInter {
  /**
   * ���� ���  
   * @param attachfileVO
   * @return
   */
  public int create(AttachfileVO attachfileVO);
  
  /**
   * ��ü �̹��� ���
   * @return
   */
  public List<AttachfileVO> list();
  
  
  /**
   * ��ȸ
   * @param attachfileno
   * @return
   */
  public AttachfileVO read(int attachfileno);
  
  /**
   * contentsno�� ���� ���� ���
   * @param contentsno
   * @return
   */
  public List<AttachfileVO> list_by_contentsno(int contentsno);
  
  /**
   * ����
   * @param attachfileno
   * @return
   */
  public int delete(int attachfileno);
  
  /**
   * contentsno�� ī��Ʈ
   * @param contentsno
   * @return
   */
  public int count_by_contentsno(int contentsno);

  /**
   * contentsno�� ����
   * @param contentsno
   * @return
   */
  public int delete_by_contentsno(int contentsno);
  
}


