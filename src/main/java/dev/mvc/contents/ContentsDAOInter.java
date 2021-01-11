package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

public interface ContentsDAOInter {
  /**
   * ���
   * @param contentsVO
   * @return
   */
  public int create(ContentsVO contentsVO);
  
  /**
   * ��� ī�װ��� ��ϵ� �۸��
   * @return
   */
  public List<ContentsVO> list_all();

  /**
   * Ư�� ī�װ��� ��ϵ� �۸��
   * @return
   */
  public List<ContentsVO> list_by_cateno(int cateno);
  
  /**
   * ��ȸ
   * @param contentsno
   * @return
   */
  public ContentsVO read(int contentsno);
  
  /**
   * ���� ó��
   * @param contentsVO
   * @return
   */
  public int update(ContentsVO contentsVO);
  
  /**
   * �н����� �˻�
   * @param hashMap
   * @return
   */
  public int passwd_check(HashMap<String, Object> hashMap);
  
  /**
   * ����
   * @param contentsno
   * @return
   */
  public int delete(int contentsno);
  
  /**
   * �̹��� ����
   * @param contentsVO
   * @return
   */
  public int update_img(ContentsVO contentsVO);
  
  /**
   * ��ü ���ڵ� ����
   * @return
   */
  public int total_count();
  
  /**
   * Map ���, ����, ����
   * @param hashMap
   * @return
   */
  public int map(HashMap<String, Object> hashMap);
 
  /**
   * map read
   * @param contentsno
   * @return
   */
//  public ContentsVO map_read(int contentsno);
  
  /**
   * Youtube ���, ����, ����
   * @param hashMap
   * @return
   */
  public int youtube(HashMap<String, Object> hashMap);

  /**
   * mp3 ���, ����, ����
   * @param hashMap
   * @return
   */
  public int mp3(HashMap<String, Object> hashMap);

  /**
   * mp4 ���, ����, ����
   * @param hashMap
   * @return
   */
  public int mp4(HashMap<String, Object> hashMap);
  
  /**
   * ī�װ��� �˻� ���
   * @param hashMap
   * @return
   */
  public List<ContentsVO> list_by_cateno_search(HashMap<String, Object> hashMap);

  /**
   * ī�װ��� �˻� ���ڵ� ����
   * @param hashMap
   * @return
   */
  public int search_count(HashMap<String, Object> hashMap);
  
  /**
   * �˻� + ����¡ ���
   * @param map
   * @return
   */
  public List<ContentsVO> list_by_cateno_search_paging(HashMap<String, Object> map);
  
  /**
   * �亯 ���� ����
   * @param map
   * @return
   */
  public int increaseAnsnum(HashMap<String, Object> map);
   
  /**
   * �亯
   * @param contentsVO
   * @return
   */
  public int reply(ContentsVO contentsVO);
 
  /**
   * �˻� + ����¡ ��� + Member join
   * @param map
   * @return
   */
  public List<Contents_MemberVO> list_by_cateno_search_paging_join(HashMap<String, Object> map);
  
  /**
   * �� �� ����
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int contentsno);
 
  /**
   * �� �� ����
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int contentsno);
  
  
}














