    create table announcement (
       GUID varchar(255) not null,
        OPTLOCK int4 not null,
        creationDate timestamp,
        creationUser varchar(255),
        modificationDate timestamp,
        modificationUser varchar(255),
        appId varchar(255),
        content varchar(255),
        endDate timestamp,
        name varchar(255),
        priority varchar(255),
        startDate timestamp,
        status varchar(255),
        title varchar(255),
        type varchar(255),
        primary key (GUID)
    );

