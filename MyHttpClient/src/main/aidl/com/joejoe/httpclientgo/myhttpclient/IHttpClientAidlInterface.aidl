// IMyAidlInterface.aidl
package com.joejoe.httpclientgo.myhttpclient;

   //createTime,modifyTime, author, version;
interface IHttpClientAidlInterface {

       String getCreateTime();
       String getModifyTime();
       String getAuthor();
       String getVersion();
       String getSysName();

}
