Question:

A       B
x       x-y-z
y  		x-p
z		y-z

Get all B's whose value contains an entry in A.


Data:
	Table : a
		ID	TENANT	CLASS_IDS
		1	hp		[100]
		2	jb		[100-200]
		3	kk		[500]
		4	mgm		[100-200-300]

	Table : b
		CLASS_ID	TENANT
		100			hp
		100			jb
		100			mgm
		200			jb
		200			mgm
		300			mgm

Link:
http://sqlfiddle.com/#!2/3f1f24/1

Schema:
CREATE TABLE a 
	(
     id int auto_increment primary key, 
     tenant varchar(20), 
     class_ids varchar(30)
    );

CREATE TABLE b 
	(
     class_id int, 
     tenant varchar(20)
    );

INSERT INTO a
(tenant, class_ids)
VALUES
('hp', '[100]'),
('jb', '[100-200]'),
('kk', '[500]'),
('mgm', '[100-200-300]');

INSERT INTO b
(class_id, tenant)
VALUES
('100', 'hp'),
('100', 'jb'),
('100', 'mgm'),
('200', 'jb'),
('200', 'mgm'),
('300', 'mgm');


Query:
select distinct aa.*  
FROM a aa
    INNER JOIN  b bb 
      On aa.class_ids rLIKE ('(?<=[^\d])' + bb.class_id + '(?=[^\d])')
      
      
Result:

ID	TENANT	CLASS_IDS
1	hp		[100]
2	jb		[100-200]
4	mgm		[100-200-300]

