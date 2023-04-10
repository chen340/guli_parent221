package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceOmpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        /**
         * 1、获取oss的id、key等
         * 2、创建oss实例
         * 3、获取要上传的文件的输入流
         * 4、获取要上传的文件名称
         * 5、调用oss方法上传到oss
         * 6、关闭流对象
         * 7、拼接url并返回
         *
         */

        //1.工具类获取数据
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        //2.创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //3.获取到将要上传的文件的输入流
            InputStream inputStream = file.getInputStream();
            //4.获取到将要上传的文件的名称
            /**
             * 给上传的文件路径按照年月日/uuid/上传名
             */
            String filename = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;
            String date = new DateTime().toString("yyyy/MM/dd");
            filename = date + "/" +filename;


            //5.调用oss的方法实现上传
            ossClient.putObject(bucketName, filename, inputStream);
            //6.关闭OSS对象
            ossClient.shutdown();
            //7.拼接路径
            String url = "https://" + bucketName + "." + endpoint + "/" + filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
