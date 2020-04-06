package org.javaboy.fileupload.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadController {

    SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd/");

    /**
     * 单个文件上传
     * @param file
     * @param request
     * @return
     */
    @PostMapping("upload")
    public String upload(MultipartFile file, HttpServletRequest request){
        // 文件路径: 项目路径(/image)+日期+文件名
        String dateStr = dateFormat.format(new Date());
        String path = request.getServletContext().getRealPath("/image")+dateStr;
        // 创建文件夹
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String oldFileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString()+oldFileName.substring(oldFileName.lastIndexOf("."));

        try {
            file.transferTo(new File(folder,newFileName));
            String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/image"+dateStr+newFileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * 多文件上传
     * 1. 一个<input type="file"/>选择多个文件
     * 2. 多个<input type="file"/>具有相同的name
     * @param files
     * @param request
     * @return
     */
    @PostMapping("uploads")
    public String uploads(MultipartFile[] files,HttpServletRequest request){
        // 文件路径: 项目路径(/image)+日期+文件名
        String dateStr = dateFormat.format(new Date());
        String path = request.getServletContext().getRealPath("/image")+dateStr;
        // 创建文件夹
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }

        for (MultipartFile file:files){
            String oldFileName = file.getOriginalFilename();
            String newFileName = UUID.randomUUID().toString()+oldFileName.substring(oldFileName.lastIndexOf("."));

            try {
                file.transferTo(new File(folder,newFileName));
                String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/image"+dateStr+newFileName;
                System.out.println(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}
