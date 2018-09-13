package com.haokuo.happyclub.network.bean;

import java.io.File;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zjf on 2018-07-21.
 */
@Getter
@Setter
public class UploadFileParams {
    public static final String FILE_TYPE_IMAGE = "image/*";

    private List<File> file;
    private String type;

    public UploadFileParams(List<File> file, String type) {
        this.file = file;
        this.type = type;
    }
}
