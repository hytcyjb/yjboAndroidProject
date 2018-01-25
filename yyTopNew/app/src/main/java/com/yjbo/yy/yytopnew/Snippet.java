package com.yjbo.yy.yytopnew;

import org.apache.commons.lang3.CharUtils;

import java.io.UnsupportedEncodingException;


public class Snippet {
public static void main(String[] args) throws UnsupportedEncodingException {
String nickName = "12测\\U0001f42f[\ud83c\udc00-\ud83c\udfff]";
byte[] t = nickName.substring(0, 1).getBytes("UTF-8");
for (byte tt : t) {
System.out.println(tt);
}
System.out.println("====================");
byte[] t1 = nickName.getBytes("UTF-8");
for (int i = 0; i < t1.length;) {
byte tt = t1[i];
if ((char) tt < 128) {
byte[] ba = new byte[1];
ba[0] = tt;
i++;
String result = new String(ba);
System.out.println("1个字节的字符");
System.out.println("字符为：" + result);
}
if ((tt & 0xE0) == 0xC0) {
byte[] ba = new byte[2];
ba[0] = tt;
ba[1] = t1[i+1];
i++;
i++;
String result = new String(ba);
System.out.println("2个字节的字符");
System.out.println("字符为：" + result);
}
if ((tt & 0xF0) == 0xE0) {
byte[] ba = new byte[3];
ba[0] = tt;
ba[1] = t1[i+1];
ba[2] = t1[i+2];
i++;
i++;
i++;
String result = new String(ba);
System.out.println("3个字节的字符");
System.out.println("字符为：" + result);
}
if ((tt & 0xF8) == 0xF0) {
byte[] ba = new byte[4];
ba[0] = tt;
ba[1] = t1[i+1];
ba[2] = t1[i+2];
ba[3] = t1[i+3];
i++;
i++;
i++;
i++;
String result = new String(ba);
System.out.println("4个字节的字符");
System.out.println("字符为：" + result);
}
}
}



public static byte[] Bit4toUnderline(byte[] b_text) {
for (int i = 0; i < b_text.length; i++)
{
   if((b_text[i] & 0xF8)== 0xF0){
       for (int j = 0; j < 4; j++) {
        b_text[i+j] =0x5f;
       }
       i+=3;
   }
}
return b_text;
}

 public static String removeFourChar(String content) { 
 byte[] conbyte = content.getBytes();
 for (int i = 0; i < conbyte.length; i++) {
 if ((conbyte[i] & 0xF8) == 0xF0) { 
 for (int j = 0; j < 4; j++) { 
 conbyte[i+j]=0x30; } i += 3; }
 } 
 content = new String(conbyte);
 return content.replaceAll("0000", ""); }

}