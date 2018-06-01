package com.xinaml.frame.ser.storage;

import com.xinaml.frame.common.custom.exception.SerException;
import com.xinaml.frame.vo.storage.FileInfo;
import com.xinaml.frame.vo.storage.FileVO;
import com.xinaml.frame.vo.storage.TreeVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface StorageSer {
    List<FileVO> list(String path) throws SerException;

    List<TreeVO> tree(String path) throws SerException;

    Boolean savePartFile(FileInfo fileInfo) throws SerException;

    void bigUploaded(FileInfo fileInfo) throws SerException;

    List<FileVO> upload(List<MultipartFile> multipartFiles, String path) throws SerException;

    Boolean existsFile(String path) throws SerException;

    Boolean md5Exist(String md5, String path, String fileName) throws SerException;

    File download(String path, boolean isFolder) throws SerException;

    void mkDir(String path, String dir) throws SerException;

    File rename(String path, String oldName, String newName) throws SerException;

    Boolean move(String[] fromPaths, String toPath) throws SerException;

    void delFile(String[] paths) throws SerException;

    Boolean copy(String[] fromPaths, String toPath) throws SerException;

    byte[] thumbnails(String path, String width, String heigth) throws SerException;
}
