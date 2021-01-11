package dev.mvc.reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReplyDAOInter {
  public int create(ReplyVO replyVO);
  
  public List<ReplyVO> list();

  public List<ReplyMemberVO> list_member_join();
  
  public List<ReplyVO> list_by_contentsno(int contentsno);
  
  /**
   * Ư���� ���� ��ü ��� ���
   * @param contentsno
   * @return
   */
  public List<ReplyMemberVO> list_by_contentsno_join(int contentsno);
  
  public int checkPasswd(Map<String, Object> map);

  public int delete(int replyno);

  /**
   * ������ ��ư
   * @param map
   * @return
   */
  public List<ReplyMemberVO> list_by_contentsno_join_add(HashMap<String, Object> map);
  
}

