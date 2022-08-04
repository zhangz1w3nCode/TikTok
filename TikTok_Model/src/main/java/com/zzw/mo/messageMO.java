package com.zzw.mo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("message")
public class messageMO {
    @Id
    private String id;//主键id

    @Field("fromUserId")
    private String fromUserId;
    @Field("fromNickname")
    private String fromNickname;
    @Field("fromFace")
    private String fromFace;


    @Field("toUserId")
    private String toUserId;


    @Field("msgType")
    private Integer msgType;

    @Field("msgContent")
    private Map msgContent;

    @Field("createDate")
    private Date createDate;


}