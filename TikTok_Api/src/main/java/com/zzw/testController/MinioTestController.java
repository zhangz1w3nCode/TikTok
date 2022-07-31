package com.zzw.testController;

import com.zzw.MinIOConfig;
import com.zzw.base.BaseInfoProperties;

import com.zzw.grace.result.GraceJSONResult;
import com.zzw.utils.MinIOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件上传接口")
@RestController
@CrossOrigin
@Slf4j
// url: serverUrl + "/userInfo/query?userId=" + myUserId,
public class MinioTestController extends BaseInfoProperties {


    @Autowired
    private MinIOConfig minIOConfig;


    @PostMapping("/upload")
    public GraceJSONResult upload(MultipartFile file) throws Exception {
        String bucketName = minIOConfig.getBucketName();
        String filename = file.getOriginalFilename();

        MinIOUtils.uploadFile(bucketName,filename,file.getInputStream());

        String url = minIOConfig.getFileHost()+"/"+bucketName+"/"+filename;



        return GraceJSONResult.ok(url);
    }


}
