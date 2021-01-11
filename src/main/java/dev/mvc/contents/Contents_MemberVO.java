package dev.mvc.contents;

import org.springframework.web.multipart.MultipartFile;

public class Contents_MemberVO {
  /** ������ ��ȣ */
  private int contentsno;
  /** ȸ�� ��ȣ */
  private int memberno;
  /** ī�װ� ��ȣ*/
  private int cateno;
  /** ���� */
  private String title = "";
  /** ���� */
  private String content = "";
  /** ��õ�� */
  private int recom;
  /** ��ȸ�� */
  int cnt = 0;
  /** ���ͳ� �ּ�*/
  private String web = "";
  /** Map, ���� */
  private String map = "";
  /** Youtube */
  private String youtube = "";
  /** MP3 */
  private String mp3 = "";
  /** MP3 Spring */
  private MultipartFile mp3MF;
  /** MP 4 */
  private String mp4 = "";
  /** MP4 Spring */
  private MultipartFile mp4MF;
  /** IP */
  private String ip = "";
  /** �н����� */
  private String passwd = "";
  /** �˻��� */
  private String word = "";
  /** ��� ��¥ */
  private String rdate = "";
  
  /** �̹��� */
  private String file1 = "";
  /** preview �̹��� preview */
  private String thumb1 = "";
  /** ����� ���� ������ */
  private long size1;
  /** 
   �̹��� MultipartFile 
   <input type='file' class="form-control" name='file1MF' id='file1MF' 
                    value='' placeholder="���� ����" multiple="multiple">
   */
  private MultipartFile file1MF;
  
  // �亯 ���� ����
  // -----------------------------------------------------
  /** �׷��ȣ */
  private int grpno;
  /** �亯 ���� */
  private int indent;
  /** �亯 ��� ���� */
  private int ansnum;
  // -----------------------------------------------------  
  // �亯 ���� ����
  
  // member table
  
  /** id�� mid�� ��� */
  private String mid; 
  
  public int getContentsno() {
    return contentsno;
  }
  public void setContentsno(int contentsno) {
    this.contentsno = contentsno;
  }
  public int getMemberno() {
    return memberno;
  }
  public void setMemberno(int memberno) {
    this.memberno = memberno;
  }
  public int getCateno() {
    return cateno;
  }
  public void setCateno(int cateno) {
    this.cateno = cateno;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getWeb() {
    return web;
  }
  public void setWeb(String web) {
    // System.out.println(web + " �����");
    this.web = web;
  }
  public String getIp() {
    return ip;
  }
  public void setIp(String ip) {
    this.ip = ip;
  }
  public String getPasswd() {
    return passwd;
  }
  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }
  public String getWord() {
    return word;
  }
  public void setWord(String word) {
    this.word = word;
  }
  public String getRdate() {
    return rdate;
  }
  public void setRdate(String rdate) {
    this.rdate = rdate;
  }
  public int getRecom() {
    return recom;
  }
  public void setRecom(int recom) {
    this.recom = recom;
  }
  
  public String getMap() {
    return map;
  }
  public void setMap(String map) {
    this.map = map;
  }
  
  public String getYoutube() {
    return youtube;
  }
  public void setYoutube(String youtube) {
    this.youtube = youtube;
  }
  
  public String getMp3() {
    return mp3;
  }
  public void setMp3(String mp3) {
    this.mp3 = mp3;
  }
  public String getMp4() {
    return mp4;
  }
  public void setMp4(String mp4) {
    this.mp4 = mp4;
  }
  
  public MultipartFile getMp3MF() {
    return mp3MF;
  }
  public void setMp3MF(MultipartFile mp3mf) {
    mp3MF = mp3mf;
  }
  public MultipartFile getMp4MF() {
    return mp4MF;
  }
  public void setMp4MF(MultipartFile mp4mf) {
    mp4MF = mp4mf;
  }
  public String getFile1() {
    return file1;
  }
  public void setFile1(String file1) {
    this.file1 = file1;
  }
  public String getThumb1() {
    return thumb1;
  }
  public void setThumb1(String thumb1) {
    this.thumb1 = thumb1;
  }
  public long getSize1() {
    return size1;
  }
  public void setSize1(long size1) {
    this.size1 = size1;
  }
  public MultipartFile getFile1MF() {
    return file1MF;
  }
  public void setFile1MF(MultipartFile file1mf) {
    file1MF = file1mf;
  }
  public int getCnt() {
    return cnt;
  }
  public void setCnt(int cnt) {
    this.cnt = cnt;
  }
  public int getGrpno() {
    return grpno;
  }
  public void setGrpno(int grpno) {
    this.grpno = grpno;
  }
  public int getIndent() {
    return indent;
  }
  public void setIndent(int indent) {
    this.indent = indent;
  }
  public int getAnsnum() {
    return ansnum;
  }
  public void setAnsnum(int ansnum) {
    this.ansnum = ansnum;
  }
  public String getMid() {
    return mid;
  }
  public void setMid(String mid) {
    this.mid = mid;
  }

  
}


