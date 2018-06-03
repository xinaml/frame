package com.xinaml.frame.action.storage;

import com.alibaba.fastjson.JSON;
import com.xinaml.frame.common.custom.annotation.Login;
import com.xinaml.frame.common.custom.exception.ActException;
import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.common.custom.result.ActResult;
import com.xinaml.frame.common.custom.result.Result;
import com.xinaml.frame.common.utils.ASEUtil;
import com.xinaml.frame.common.utils.FileUtil;
import com.xinaml.frame.common.utils.ResponseUtil;
import com.xinaml.frame.ser.storage.StorageSer;
import com.xinaml.frame.vo.storage.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@RequestMapping(value = "storage")
@Controller
@Login
public class StorageAct {

    @Autowired
    private StorageSer storageSer;

    @RequestMapping(value = "page")
    public String page() throws ActException {
        return "/storage/storage";
    }

    /**
     * 文件列表
     * <p>
     * 文件路径
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list")
    public Result list(HttpServletRequest request) throws ActException {
        try {
            String path = getParameter(request, "path", true);
            return new ActResult(storageSer.list(path));
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 文件列表
     * <p>
     * 文件路径
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "tree")
    public String tree(HttpServletRequest request) throws SerException {
        try {
            String path = getParameter(request, "path", true);
            return JSON.toJSONString(storageSer.tree(path));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 文件上传（支持大文件） 大文件没有关联id(relevanceId)，照样会保存数据库,以提供MD5
     *
     * @param info 上传文件信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "upload")
    public String upload(HttpServletRequest request, FileInfo info) throws SerException {
        try {
            initFileInfo(info, request); // 初始化文件上传信息
            if (info.getChunks() != null && info.getChunk() != null) { // 大文件分片上传
                storageSer.savePartFile(info); // 将文件分块保存到临时文件夹里，便于之后的合并文件
                storageSer.bigUploaded(info); // 完整的上传
                return "success";
            } else { // 普通小文件上传
                try {
                    storageSer.upload(FileUtil.getMultipartFile(request), info.getPath());
                    return "success";
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SerException(e.getMessage());
                }
            }
        } catch (Exception e) {
            HttpServletResponse response = ResponseUtil.get();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.setHeader("icop-content-type", "exception");
            response.setStatus(500);
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
            writer.flush();
            writer.close();
            writer.write(e.getMessage());
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 检测文件是否存在
     */
    @ResponseBody
    @RequestMapping(value = "exists")
    public String exist(HttpServletRequest request) {
        try {
            String path = getParameter(request, "path", true);
            return String.valueOf(storageSer.existsFile(path));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 检测MD5是否存在，实现秒传
     */
    @ResponseBody
    @RequestMapping(value = "md5/exist")
    public String md5Exist(HttpServletRequest request) {
        try {
            String fileMd5 = getParameter(request, "fileMd5", true);
            String toPath = getParameter(request, "toPath", true);
            String fileName = getParameter(request, "fileName", true);
            return String.valueOf(storageSer.md5Exist(fileMd5, toPath, fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 文件下载（支持大文件下载）
     * <p>
     * 文件路径
     *
     * @param isFolder 是否为文件夹，默认为文件(下载文件夹设置为true)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "download")
    public void downLoad(HttpServletRequest request, HttpServletResponse response, boolean isFolder) throws SerException {
        try {
            String path = getParameter(request, "path", true);
            File file = storageSer.download(path, isFolder);
            if (file.exists()) {
                FileUtil.writeOutFile(response, file);
                if (isFolder) {
                    file.delete(); // 删除压缩文件
                }
            }

        } catch (Exception e) {
            throw new SerException(e.getMessage());
        }
    }


    /**
     * 创建文件夹
     * <p>
     * 文件路径
     * 文件夹名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mkdir")
    public String mkdir(HttpServletRequest request) throws SerException {
        try {
            String path = getParameter(request, "path", true);
            String dir = getParameter(request, "dir", true);
            storageSer.mkDir(path, dir);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 重命名文件或文件夹
     * <p>
     * 文件路径
     * 新的文件名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "rename")
    public String rename(HttpServletRequest request) throws SerException {
        try {
            String path = getParameter(request, "path", true);
            String newName = getParameter(request, "newName", true);
            String oldName = getParameter(request, "oldName", true);
            storageSer.rename(path, oldName, newName);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 移动文件至文件夹
     * <p>
     * 文件路径
     * 移动到的文件路径
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "move")
    public String move(HttpServletRequest request) throws SerException {
        try {
            String from = getParameter(request, "fromPath", true);
            String toPath = getParameter(request, "toPath", true);
            String[] fromPath = from.split("##");
            storageSer.move(fromPath, toPath);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 删除文件
     * <p>
     * 多个文件路径
     *
     * @return
     * @throws SerException
     */
    @ResponseBody
    @RequestMapping(value = "delfile")
    public String delFile(HttpServletRequest request) throws SerException {
        try {
            String values = getParameter(request, "paths", true);
            String[] paths = values.split("##");
            if (null != paths) {
                storageSer.delFile(paths);
                return "success";
            } else {
                throw new SerException("paths不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 复制到
     * <p>
     * 文件路径
     * 文件夹名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "copy")
    public String copy(HttpServletRequest request) throws SerException {
        try {
            String from = getParameter(request, "fromPath", true);
            String toPath = getParameter(request, "toPath", true);
            String[] fromPaths = from.split("##");
            storageSer.copy(fromPaths, toPath);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerException(e.getMessage());
        }
    }

    /**
     * 获取缩略图
     * <p>
     * 文件路径
     *
     * @param response
     * @throws SerException
     */
    @ResponseBody
    @RequestMapping(value = "thumbnails")
    public void thumbnails(HttpServletRequest request, HttpServletResponse response) throws SerException {
        String path = getParameter(request, "path", true);
        String width = getParameter(request, "width", false);
        String height = getParameter(request, "height", false);
        try {
            writeImage(response, storageSer.thumbnails(path, width, height));
        } catch (IOException e) {
            throw new SerException("获取缩略图错误！");
        }
    }

    /**
     * 获取gif
     * <p>
     * 文件路径
     *
     * @param response
     * @throws SerException
     */
    @ResponseBody
    @RequestMapping(value = "gif")
    public void gif(HttpServletRequest request, HttpServletResponse response) throws SerException {
        String path = getParameter(request, "path", true);
        try {
            FileUtil.writeOutFile(response, storageSer.download(path, false));
        } catch (IOException e) {
            throw new SerException("获取缩略图错误！");
        }
    }

    /**
     * 通过name获取参数
     *
     * @param request
     * @return
     * @throws SerException
     */
    private String getParameter(HttpServletRequest request, String name, boolean notNull) throws SerException {
        String parameter = request.getParameter(name);
        if (StringUtils.isNotBlank(parameter)) {
            if(StringUtils.isNumeric(parameter)){
                return parameter;
            }
            parameter = ASEUtil.decrypt(parameter);
            return parameter;
        } else {
            if (notNull) {
                throw new SerException(name + "不能为空!");
            }
            return null;
        }
    }

    /**
     * 输出图片
     *
     * @param response
     * @param bytes
     * @throws IOException
     */
    private void writeImage(HttpServletResponse response, byte[] bytes) throws IOException {
        response.reset();
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        ByteArrayInputStream in = new ByteArrayInputStream(bytes); // 将b作为输入流；
        BufferedImage image = ImageIO.read(in);
        ImageIO.scanForPlugins();
        ImageIO.write(image, "jpg", response.getOutputStream());

    }


    /**
     * 初始化上传文件信息
     *
     * @param info    文件信息
     * @param request
     */
    private void initFileInfo(FileInfo info, HttpServletRequest request) throws SerException {
        String value = request.getParameter("relevanceId");
        Long relevanceId = null;
        if (null != value) {
            relevanceId = Long.parseLong(value);
        }
        int ext_index = info.getName().lastIndexOf(".");
        String ext = null;
        if (ext_index != -1) {
            ext = info.getName().substring(ext_index);// 后缀
        } else {
            ext = null == ext ? "" : ext;
        }
        info.setExt(ext);
        if (null != info.getChunk()) {
            int index = Integer.parseInt(info.getChunk()); // 文件分片序号
            String partName = String.valueOf(index) + ext; // 分片文件保存名
            info.setPartName(partName);
        }
        info.setRelevanceId(relevanceId);
        String path =ASEUtil.decrypt(info.getPath());
        info.setPath(path);
    }
}
