-- 1. ���̺� ����
DROP TABLE contents;
DROP TABLE cate;
DROP TABLE categrp;

-- 2. ���̺� ����
categrp
cate

-- 3. CASCADE option�� �̿��� �ڽ� ���̺��� ������ ���̺� ����, ���õ� ���������� ������.
DROP TABLE cate CASCADE CONSTRAINTS;



/**********************************/
/* Table Name: ī�װ� �׷� */
/**********************************/
CREATE TABLE categrp(
		categrpno                     		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		name                          		VARCHAR2(50)		 NOT NULL,
		seqno                         		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		visible                       		CHAR(1)		 DEFAULT 'Y'		 NOT NULL,
		rdate                         		DATE		 NOT NULL
);

COMMENT ON TABLE categrp is 'ī�װ� �׷�';
COMMENT ON COLUMN categrp.categrpno is 'ī�װ� �׷� ��ȣ';
COMMENT ON COLUMN categrp.name is '�̸�';
COMMENT ON COLUMN categrp.seqno is '��� ����';
COMMENT ON COLUMN categrp.visible is '��� ���';
COMMENT ON COLUMN categrp.rdate is '�׷� ������';

DROP SEQUENCE categrp_seq;
CREATE SEQUENCE categrp_seq
  START WITH 1              -- ���� ��ȣ
  INCREMENT BY 1          -- ������
  MAXVALUE 9999999999 -- �ִ밪: 9999999 --> NUMBER(7) ����
  CACHE 2                       -- 2���� �޸𸮿����� ���
  NOCYCLE;                     -- �ٽ� 1���� �����Ǵ� ���� ����
  
/**********************************/
/* Table Name: ī�װ� */
/**********************************/
CREATE TABLE cate(
		cateno                        		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		categrpno                     		NUMBER(10)		 NULL ,
		name                          		VARCHAR2(100)		 NOT NULL,
		seqno                         		NUMBER(10)		 DEFAULT 0		 NOT NULL,
		visible                       		CHAR(1)		 DEFAULT 'Y'		 NOT NULL,
		rdate                         		DATE		 NOT NULL,
		cnt                           		NUMBER(10)		 DEFAULT 0		 NOT NULL,
  FOREIGN KEY (categrpno) REFERENCES categrp (categrpno)
);

COMMENT ON TABLE cate is 'ī�װ�';
COMMENT ON COLUMN cate.cateno is 'ī�װ� ��ȣ';
COMMENT ON COLUMN cate.categrpno is 'ī�װ� �׷� ��ȣ';
COMMENT ON COLUMN cate.name is 'ī�װ� �̸�';
COMMENT ON COLUMN cate.seqno is '��� ����';
COMMENT ON COLUMN cate.visible is '��� ���';
COMMENT ON COLUMN cate.rdate is '�����';
COMMENT ON COLUMN cate.cnt is '��ϵ� �� ��';

DROP SEQUENCE cate_seq;
CREATE SEQUENCE cate_seq
  START WITH 1              -- ���� ��ȣ
  INCREMENT BY 1          -- ������
  MAXVALUE 9999999999 -- �ִ밪: 9999999 --> NUMBER(7) ����
  CACHE 2                       -- 2���� �޸𸮿����� ���
  NOCYCLE;                     -- �ٽ� 1���� �����Ǵ� ���� ����
  
-- ���
INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 1000, '����', 1, 'Y', sysdate, 0);
���� ���� -
ORA-02291: integrity constraint (AI7.SYS_C008048) violated - parent key not found
-- FK �÷��� �� 1000�� categrp ���̺� ��� ���� �߻���.

-- �θ� ���̺� ���� �߰�
INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '���� ����', 1, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '�ؿ� ����', 2, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '����', 3, 'Y', sysdate);

COMMIT;

SELECT * FROM categrp ORDER BY categrpno ASC;
 CATEGRPNO NAME                                                    SEQNO V RDATE              
---------- -------------------------------------------------- ---------- - -------------------
         1 ���� ����                                                   1 Y 2020-10-23 02:43:44
         2 �ؿ� ����                                                   2 Y 2020-10-23 02:43:44
         3 ����                                                        3 Y 2020-10-23 02:43:44

-- ���: ���� �������� ����/�ܿ� ������ ��õ
INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 1, '����', 1, 'Y', sysdate, 0);

INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 1, '�ܿ�', 1, 'Y', sysdate, 0);

INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 1, '��', 1, 'Y', sysdate, 0);

COMMIT;

-- ���
SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
ORDER BY cateno ASC;
        PK           FK 
    CATENO  CATEGRPNO NAME     SEQNO V RDATE                      CNT
---------- ---------- ------------------ ---------- - ------------------- ----------
         2           1           ����              1 Y 2020-10-23 02:46:36          0
         3           1           �ܿ�              1 Y 2020-10-23 02:47:53          0
         4           1           ��                 1 Y 2020-10-23 02:48:21          0
         
-- ��ȸ
SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
WHERE cateno=3;

-- ����
UPDATE cate
SET categrpno=1, name='�Ĵ�', seqno = 10, visible='N', cnt=0
WHERE cateno = 3;

commit;

-- ����
DELETE cate
WHERE cateno = 3;

SELECT * FROM cate;

-- ��� ���� ����, 10 �� 1
UPDATE cate
SET seqno = seqno - 1
WHERE cateno=2;

SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
ORDER BY seqno ASC;

-- ��¼��� ����, 1 �� 10
UPDATE cate
SET seqno = seqno + 1
WHERE cateno=2;

-- ��� ����� ����
UPDATE cate
SET visible='Y'
WHERE cateno=2;

UPDATE cate
SET visible='N'
WHERE cateno=2;

commit;

---------------------------------------------         
-- FK�� ���� ���̺� �߰� ����
---------------------------------------------
-- ī�װ� �׷쿡���� ī�װ� ���
SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
WHERE categrpno=1
ORDER BY seqno ASC;

-- �θ� ���̺� ���ڵ� ����
DELETE FROM categrp
WHERE categrpno = 1;
���� ���� -
ORA-02292: integrity constraint (AI7.SYS_C008048) violated - child record found

-- �����Ϸ��� ���ڵ��� categrpno�� ��𿡼� ���̴��� �˷��־����.
SELECT COUNT(*) as cnt
FROM cate
WHERE categrpno=1;

       CNT
----------
         2
         
-- �ڽ� ���̺��� FK�� 1�� ���ڵ� ��� ����
DELETE FROM cate
WHERE categrpno=1;

-- �θ� ���̺� ���ڵ� ����
DELETE FROM categrp
WHERE categrpno=1;

commit;

-- �θ� ���̺� ���ڵ� ���� Ȯ��
SELECT * FROM categrp ORDER BY categrpno ASC;

-- ���� VO
SELECT r.categrpno as r_categrpno, r.name as r_name,
           c.cateno, c.categrpno, c.name, c.seqno, c.visible, c.rdate, c.cnt
FROM categrp r, cate c
WHERE r.categrpno = c.categrpno
ORDER BY r.categrpno ASC, c.seqno ASC;

-- ���� VO, categrpno �� cate ���
SELECT r.categrpno as r_categrpno, r.name as r_name,
           c.cateno, c.categrpno, c.name, c.seqno, c.visible, c.rdate, c.cnt
FROM categrp r, cate c
WHERE (r.categrpno = c.categrpno) AND r.categrpno=1
ORDER BY r.categrpno ASC, c.seqno ASC;

R_CATEGRPNO R_NAME  CATENO  CATEGRPNO  NAME      SEQNO VISIBLE RDATE            CNT
----------- --------------- ---------- ----------- --------------------- ---------- - ------------------- ----------
          1 ��ȭ                  1           1 SF                              1 Y 2020-05-12 04:04:28          0
          1 ��ȭ                  2           1 ���                         2 Y 2020-05-12 04:04:28          0
          1 ��ȭ                  3           1 ������                         3 Y 2020-05-12 04:04:28          0

-- contents �߰������� ��ϵ� �ۼ��� ����
UPDATE cate 
SET cnt = cnt + 1 
WHERE cateno=1;
 
-- contents �߰������� ��ϵ� �ۼ��� ����
UPDATE cate 
SET cnt = cnt - 1 
WHERE cateno=1; 

-- �ۼ� �ʱ�ȭ
UPDATE cate
SET cnt = 0;

COMMIT;










 
 