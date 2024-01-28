package com.whl.hotelService.domain.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
public class HotelFileInfoService {

    @Transactional(rollbackFor = Exception.class)
    public boolean isDeleteHotelFileInfo(String filename, String hotelname) {
        String uploadPath = "c:\\" + File.separator + "hotelimage" + File.separator + hotelname;
        File dir = new File(uploadPath);

        File[] files = dir.listFiles();

        for (File file : files) {
            if(file.getName().equals(filename)) {
                file.delete();
            }
        }

        return true;
    }
}
