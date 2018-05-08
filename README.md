# gstools
java simple tools for develop
v2018.05:
LitedaoConfig : 添加charset,用于所有cst.gu.litedao.*下的所有方法涉及编码方式时使用的编码格式:如string转blob byte等指定为charset,.如果设置为null,则使用无字符集参数,通过jvm默认方式执行,可能在系统,数据库等升级时发生乱那么
使用方式:在basedao等基类中使用static代码块,完成一次性配置

0508:litedao添加BeanInfos.java ,用于缓存Bean的反射信息.提高运行速度


