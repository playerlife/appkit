// IMyAidlInterface.aidl
package com.joejoe.pictureviewer.mypictureviewer;

   //createTime,modifyTime, author, version;
interface IMyAidlInterface {

       String getCreateTime();
       String getModifyTime();
       String getAuthor();
       String getVersion();
       String getSysName();

}
