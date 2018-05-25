package com.sean.arya.stark.backstage.common.data;

import java.util.regex.Pattern;

public class RegexConst {
    //邮箱
    public static final Pattern MAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    //手机号
    public static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
    //名称
    public static final Pattern NAME_PATTERN = Pattern.compile("^[\\u4E00-\\u9FBF][\\u4E00-\\u9FBF(.|·)]{0,13}[\\u4E00-\\u9FBF]$");
    //昵称
    public static final Pattern NICKNAME_PATTERN = Pattern.compile("^((?!\\d{5})[\\u4E00-\\u9FBF(.|·)|0-9A-Za-z_]){2,11}$");
    //密码
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[1-9A-Za-z!@#%_+=-.]{6,30}$");
    //邮编
    public static final Pattern POSTCODE_PATTERN = Pattern.compile("^\\d{6}$");
    //身份证
    public static final Pattern ID_PATTERN = Pattern.compile("^((\\d{18})|([0-9x]{18})|([0-9X]{18}))$");
    //银行卡
    public static final Pattern BANK_CARD_PATTERN = Pattern.compile("^\\d{16,30}$");
    //汉字
    public static final Pattern CHINA_PATTERN=Pattern.compile("^[\\u4e00-\\u9FBF]+$");
    //域名
    public static final Pattern DOMAIN_PATTERN=Pattern.compile("^((http://)|(https://))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}(/)$");
    //ip
    public static final Pattern IP_PATTERN=Pattern.compile("^((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))$");
    //日期
    public static final Pattern DATE_PATTERN=Pattern.compile("^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$");
}
