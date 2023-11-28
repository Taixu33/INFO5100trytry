create database library;

use library;

DROP TABLE IF EXISTS admin_code

CREATE TABLE admin_code(
   id int(11) not null auto_increment,
   code varchar(50) not null,
   count int(11) not null default '2',
   primary key (id),
   unique key (code)
);

INSERT INTO admin_code(id,code,count)
values('1','12345678','12'),
      ('2','20231113','13');


--
DROP TABLE IF EXISTS admin;


CREATE TABLE Admin(
   adminID int(11) not null auto_increment,
   username varchar(30) not null,
   password varchar(30) not null,
   primary key (adminID),
   unique key (username)
);

INSERT INTO Admin(adminID,username,password)
values('1','Monical','Monical1234'),
      ('2','Miker','Miker1234'),
      ('3','Jenny','Jenny1234')
      
--

DROP TABLE IF EXISTS book;

create table book(
    bookid int(11) not null auto_increment,
    bookname varchar(100) not null,
    author varchar(30) not null,
    number bigint(20) not null,
    borrow varchar(10) not null,
    quantity int(11) not null,
    location varchar(30) not null,
    primary key (bookid),
    unique key (number)
);


insert into book(bookId,bookname,author,number,borrow,quantity,location)
values('1','Clean Code Fundamentals','Robert C.Martin','131010','Y','4','3rd floor'),
      ('2','Effective Java','Joshua Bloch','131011','N','0','2nd floor'),
      ('3','Designing Data-Intensive Applications','Martin Kleppmann','131012','N','0','1st floor'),
      ('4','React-The Complete Guide','Maximillian','131013','Y','5','4th floor'),
      ('5','Hands-on Machine learning','Aurelien Geron','131014','Y','6','2nd floor'),
      ('6','Desining Machine Learning System','David Foster','131015','N','0','1st floor'),
      ('7','The Object-Oriented Thought Process','Matt Weisfeld','131016','Y','7','3rd floor'),
      ('8','A student Guide to Object-Oriented Development','Mike','131017','Y','8','4th floor'),
      ('9','Applied Network Security Monitoring','Chris Sanders','131018','N','0','2nd floor'),
      ('10','Cyber Security and Network Security','M.Vinay','131019','Y','9','1st floor');

--
DROP TABLE IF EXISTS users;

create table Users(
  userId int(11) not null auto_increment,
  username varchar(30) not null,
  password varchar(30) not null,
  primary key (userId)
);

insert into users(userId,username,password)
values ('1','Olivia_2023','12345678'),
       ('2','Emma_1990','12345678'),
       ('3','Sophia_2021','12345678'),
       ('5','James_2023','12345678');



