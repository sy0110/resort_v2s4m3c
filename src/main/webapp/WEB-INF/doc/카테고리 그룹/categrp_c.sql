/**********************************/
/* Table Name: ī�װ� �׷� */
/**********************************/
DROP TABLE contents;
DROP TABLE cate;
DROP TABLE categrp;

DROP TABLE categrp CASCADE CONSTRAINTS;

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
  
-- insert
INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '���� ����', 1, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '�ؿ� ����', 2, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '����', 3, 'Y', sysdate);

COMMIT;

-- list
SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY categrpno ASC;

 CATEGRPNO NAME                                                    SEQNO V RDATE              
---------- -------------------------------------------------- ---------- - -------------------
         1 ���� ����                                                   1 Y 2020-04-17 04:49:13
         2 �ؿ� ����                                                   2 Y 2020-04-17 04:49:13
         3 ���� �ڷ�                                                   3 Y 2020-04-17 04:49:13

-- ��ȸ + ������
SELECT categrpno, name, seqno, visible, rdate 
FROM categrp
WHERE categrpno = 1;
 
 CATEGRPNO NAME  SEQNO VISIBLE RDATE
 --------- ----- ----- ------- ---------------------
         1 ���� ����     1 Y       2019-05-13 13:07:50.0
         
-- ����
UPDATE categrp
SET name='���� ���', seqno = 3, visible='N'
WHERE categrpno = 3;

commit;

-- ��ȸ + ������ + ������
SELECT categrpno, name, seqno, visible, rdate 
FROM categrp
WHERE categrpno = 1;
 
 CATEGRPNO NAME  SEQNO VISIBLE RDATE
 --------- ----- ----- ------- ---------------------
         1 ���� ����     1 Y       2019-05-13 13:07:50.0

-- ����         
DELETE FROM categrp
WHERE categrpno = 10;

DELETE FROM categrp
WHERE name='3';

commit;
 
-- ��� ���������� ��ü ���
SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY seqno ASC;
 
-- ��� ���� ����, 10 �� 1
UPDATE categrp
SET seqno = seqno - 1
WHERE categrpno=1;
 
-- ��¼��� ����, 1 �� 10
UPDATE categrp
SET seqno = seqno + 1
WHERE categrpno=1;

-- ��� ����� ����
UPDATE categrp
SET visible='Y'
WHERE categrpno=1;

UPDATE categrp
SET visible='N'
WHERE categrpno=1;

commit;
         
         
         
         