
##签名配置信息

签名文件：catopuma.jks 
库密码：OQpmraPw93STeNZ8
秘钥库1：test 密码 123456

>  keytool -list -v -keystore catopuma.jks -storepass OQpmraPw93STeNZ8 命令打印
```
密钥库类型: jks
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: test
创建日期: 2021-1-27
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: CN=hugh, OU=hugh, O=hugh, L=shanghai, ST=cn, C=86
发布者: CN=hugh, OU=hugh, O=hugh, L=shanghai, ST=cn, C=86
序列号: 4edd854f
有效期为 Wed Jan 27 16:35:48 CST 2021 至 Fri Jan 20 16:35:48 CST 2051
证书指纹:
         MD5:  79:81:6D:E2:8F:42:1B:C4:4F:23:12:23:E4:69:67:B8
         SHA1: 21:19:C8:6C:9D:D3:10:3E:5C:90:29:A2:4F:35:C1:53:0A:42:A5:07
         SHA256: 7B:8A:EC:B8:B8:E6:7F:69:82:B5:BD:1C:D5:2C:76:EC:7D:7F:55:A5:9E:6D:8F:56:B3:96:1F:84:EF:EF:BE:42
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 8C 70 E2 9A BD C6 0C 26   08 98 E0 5E 24 2B 3E 1A  .p.....&...^$+>.
0010: 7F A1 A4 5F                                        ..._
]
]



*******************************************
*******************************************

Warning:
JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore D:\catopuma.jks -destkeystore D:\catopuma.jks -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。
```