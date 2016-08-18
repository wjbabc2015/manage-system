use membership_management;

select * from membership into outfile 'C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/test.cvs' fields terminated by ','
enclosed by '''' lines terminated by '\n';