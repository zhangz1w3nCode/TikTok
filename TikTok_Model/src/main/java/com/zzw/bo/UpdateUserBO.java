package com.zzw.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserBO {

    private String id;

    private String nickname;

    private String imoocNum;

    private String face;

    private Integer sex;

    private Date birthday;

    private String country;

    private String province;

    private String city;

    private String district;
    private String description;


    private String bgImg;

    private Integer canImoocNumBeUpdated;



}
