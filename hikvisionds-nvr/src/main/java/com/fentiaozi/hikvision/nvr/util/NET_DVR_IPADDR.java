package com.fentiaozi.hikvision.nvr.util;

import com.sun.jna.Structure;
import lombok.Data;

@Data
public class NET_DVR_IPADDR extends Structure {
    public byte[] sIpV4 = new byte[16];
    public byte[] byRes = new byte[128];

    @Override
    public String toString() {
        return "NET_DVR_IPADDR.sIpV4: " + new String(sIpV4) + "\n" + "NET_DVR_IPADDR.byRes: " + new String(byRes) + "\n";
    }
}
