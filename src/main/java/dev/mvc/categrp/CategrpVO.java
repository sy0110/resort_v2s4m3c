package dev.mvc.categrp;
 
/*
    categrpno                         NUMBER(10)     NOT NULL    PRIMARY KEY,
    name                              VARCHAR2(50)     NOT NULL,
    seqno                             NUMBER(7)    DEFAULT 0     NOT NULL,
    visible                             CHAR(1)    DEFAULT 'Y'     NOT NULL,
    rdate                               DATE     NOT NULL 
 */
public class CategrpVO {
  /** ī�װ� �׷� ��ȣ */
  private int categrpno = 0;
  /** �̸� */
  private String name = "";
  /** ��� ���� */
  private int seqno = 0;
  /** ��� ��� */
  private String visible = "";
  /** �׷� ������ */
  private String rdate = "";
  
  public int getCategrpno() {
    return categrpno;
  }
  public void setCategrpno(int categrpno) {
    this.categrpno = categrpno;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getSeqno() {
    return seqno;
  }
  public void setSeqno(int seqno) {
    this.seqno = seqno;
  }
  public String getVisible() {
    return visible;
  }
  public void setVisible(String visible) {
    this.visible = visible;
  }
  public String getRdate() {
    return rdate;
  }
  public void setRdate(String rdate) {
    this.rdate = rdate;
  }
  
  
} 

