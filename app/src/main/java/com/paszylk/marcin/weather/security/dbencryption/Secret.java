package com.paszylk.marcin.weather.security.dbencryption;

import java.util.Arrays;

public class Secret {

    private char[] secret = new char[]{'\uA6B6', '\uA6E8', '\uA68E', '\uA6F4', '\u8645', '\uA6F7',
                                    '\uA7DE', '\uAE7A', '\uA6D1', '\uA767', '\uA1B6', '\uA669',
                                    '\uA6C4', '\uA6FD', '\uA6B0', '\uA766', '\u8138', '\uA655',
                                    '\u81AB', '\uA6B5', '\u8115', '\u81F7', '\u81B2', '\uD000',
                                    '\uAE79', '\u8211', '\uAE35', '\uA6FC', '\uAE2F', '\u814F',
                                    '\uB5D3', '\u8646', '\u81E2', '\uA788', '\uA66B', '\uA6E2',
                                    '\uB615', '\u8219', '\u8224', '\uA6AD', '\uCF74', '\u820C',
                                    '\uA926', '\u821A', '\uAB33', '\uA676', '\uA46A', '\u820F',
                                    '\u81B6', '\u81C2', '\uB600', '\uB5AD', '\uB598', '\uCC92',
                                    '\uCC8E', '\uA50A', '\uD02E', '\uBA96', '\uBA94', '\uA85F',
                                    '\uB61E', '\uCC7F', '\uD02E', '\uAB66' };

    public int getLength(){
        return secret.length;
    }

    public char[] generate() {
        for (
                int jGosn = 0, oQVIl = 0;
                jGosn < 64; jGosn++)

        {
            oQVIl = secret[jGosn];
            oQVIl -= jGosn;
            oQVIl ^= 0x5256;
            oQVIl -= 0x13F6;
            oQVIl -= jGosn;
            oQVIl--;
            oQVIl -= jGosn;
            oQVIl += 0x8236;
            oQVIl ^= jGosn;
            oQVIl--;
            oQVIl ^= 0xF043;
            oQVIl ^= jGosn;
            oQVIl ^= 0xFFFF;
            oQVIl -= jGosn;
            oQVIl += 0x9760;
            oQVIl ^= jGosn;
            oQVIl ^= 0xFFFF;
            oQVIl -= 0x9DCE;
            oQVIl ^= 0xFFFF;
            oQVIl += 0x1D28;
            oQVIl ^= 0x90EE;
            oQVIl--;
            oQVIl ^= 0x87EC;
            oQVIl--;
            oQVIl ^= 0xC46C;
            oQVIl += 0xCE0E;
            oQVIl += jGosn;
            oQVIl += 0xCB41;
            oQVIl ^= 0xBB6E;
            oQVIl -= jGosn;
            oQVIl ^= 0x72F7;
            oQVIl -= jGosn;
            oQVIl ^= 0xFFFF;
            oQVIl -= 0xED9B;
            oQVIl ^= 0x829A;
            oQVIl++;
            oQVIl ^= 0x9446;
            oQVIl += jGosn;
            oQVIl++;
            oQVIl ^= jGosn;
            oQVIl ^= 0xDDA9;
            oQVIl ^= 0xFFFF;
            oQVIl--;
            oQVIl += jGosn;
            oQVIl ^= jGosn;
            oQVIl ^= 0xFFFF;
            oQVIl ^= 0x71C7;
            oQVIl += jGosn;
            oQVIl ^= 0x28CA;
            oQVIl--;
            oQVIl ^= 0xD0A8;
            secret[jGosn] = (char) (oQVIl & 0xFFFF);
        }

        return secret;
    }

    public void clean(){
        Arrays.fill(secret, '\0');
    }
}