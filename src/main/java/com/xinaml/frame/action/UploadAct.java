package com.xinaml.frame.action;

import com.xinaml.frame.common.custom.result.ActResult;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload.isMultipartContent;

@Controller
public class UploadAct {

    private final static String ROOT = "/storage/file/";

    /**
     * 列表
     * @return
     */
    @ResponseBody
    @GetMapping("/images/list")
    public ActResult list() {
        File dir = new File(ROOT);
        File[] files = dir.listFiles();
        String[] paths = new String[0];
        if(null!=files && files.length>0){
            paths = new String[files.length];
            int index=0;
            for(File file:files){
                paths[index++]= StringUtils.substringAfter(file.getPath(),ROOT);
            }
        }
        return new ActResult(paths);
    }

    /**
     * 读取图片
     * @param path
     * @param response
     */
    @ResponseBody
    @GetMapping("/images/read")
    public void images( String path, HttpServletResponse response) {
        File file = new File(ROOT + path);
        try {
            writeOutFile(response, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传
     * @param path
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public ActResult upload(String path, HttpServletRequest request) {
            List<MultipartFile> mFiles = getMultipartFile(request);

            for (MultipartFile mfile : mFiles) {
                File dir = new File(ROOT + path);
                if(!dir.exists()){ //目录不存在
                    dir.mkdirs();
                }
                File file = new File(ROOT + path + "/" + mfile.getOriginalFilename());
                try {
                    mfile.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new ActResult("success");
        }

        /**
         * 输出文件
         *
         * @param response
         * @param file
         * @throws IOException
         */
        private void writeOutFile (HttpServletResponse response, File file) throws IOException {
            try {
                if (file.exists()) {
                    String dfileName = file.getName();
                    InputStream fis = new BufferedInputStream(new FileInputStream(file));
                    response.reset();
                    response.addHeader("Content-Disposition", "attachment;filename=" + new String(dfileName.getBytes(), "iso-8859-1"));
                    response.addHeader("Content-Length", "" + file.length());
                    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                    response.setContentType("application/octet-stream");
                    byte[] buffer = new byte[1024 * 1024 * 4];
                    int i = -1;
                    while ((i = fis.read(buffer)) != -1) {
                        toClient.write(buffer, 0, i);
                    }
                    fis.close();
                    toClient.flush();
                    toClient.close();

                } else {
                    PrintWriter out = response.getWriter();
                    out.print("<script>alert(\"not find the file\")</script>");
                }
            } catch (IOException ex) {
                PrintWriter out = response.getWriter();
                out.print("<script>alert(\"not find the file\")</script>");
            }
        }

        /**
         * 获得上传文件
         * @param request
         * @return
         * @throws ServiceException
         */
        public static List<MultipartFile> getMultipartFile (HttpServletRequest request) throws ServiceException {

            if (null != request && !isMultipartContent(request)) {
                throw new ServiceException("上传表单不是multipart/form-data类型");
            }
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request; // 转换成多部分request
            Map<String, MultipartFile> map = multiRequest.getFileMap();
            List<MultipartFile> files = new ArrayList<MultipartFile>(map.size());
            for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
                files.add(entry.getValue());
            }
            return files;

        }
    }
