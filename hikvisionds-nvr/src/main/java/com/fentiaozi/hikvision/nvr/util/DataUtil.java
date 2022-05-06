package com.fentiaozi.hikvision.nvr.util;

import com.fentiaozi.hikvision.nvr.domain.NvrTime;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wgq
 */
@Component
public class DataUtil {
    public NvrTime dateToNum(Date date) {
        NvrTime nvrTime = new NvrTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            date = new Date();
        }
        String time = format.format(date);
        nvrTime.setDwYear(Integer.parseInt(time.substring(0, 4)));
        nvrTime.setDwMonth(Integer.parseInt(time.substring(5, 7)));
        nvrTime.setDwDay(Integer.parseInt(time.substring(8, 10)));
        nvrTime.setDwHour(Integer.parseInt(time.substring(11, 13)));
        nvrTime.setDwMinute(Integer.parseInt(time.substring(14, 16)));
        nvrTime.setDwSecond(Integer.parseInt(time.substring(17, 19)));
        return nvrTime;
    }


    /**
     * 获取海康录像机格式的时间
     *
     * @param time
     * @return
     */
    public NET_DVR_TIME getHkTime(Date time) {
        NET_DVR_TIME structTime = new NET_DVR_TIME();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
        String[] times = str.split("-");
        structTime.dwYear = Integer.parseInt(times[0]);
        structTime.dwMonth = Integer.parseInt(times[1]);
        structTime.dwDay = Integer.parseInt(times[2]);
        structTime.dwHour = Integer.parseInt(times[3]);
        structTime.dwMinute = Integer.parseInt(times[4]);
        structTime.dwSecond = Integer.parseInt(times[5]);
        return structTime;
    }

    /**
     * 组装视频流回放时间格式
     * @param date
     * @return
     */
    public String backTimeAssemble(Date date) {
        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        StringBuffer buffer = new StringBuffer();
        buffer.append(str.substring(0,4));
        buffer.append(str.substring(5,7));
        buffer.append(str.substring(8,10));
        buffer.append("T");
        buffer.append(str.substring(11,13));
        buffer.append(str.substring(14,16));
        buffer.append(str.substring(17,19));
        buffer.append("Z");
        return buffer.toString();
    }


}
