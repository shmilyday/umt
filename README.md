Duckling UMT
=======
User Management tools (UMT) Version 8 @2016

####Prerequisities:
To be updated

#####Java Development Kit
`Version 7.0+`

#####Tomcat
```
# vi /etc/profile                  //environment setting
#-------------------------- 
export JAVA_HOME=/usr/local/jdk 
export CLASSPATH=.:$JAVA_HOME/jre/lib:$JAVA_HOME/lib/tool.jar 
export PATH=$JAVA_HOME/bin:$PATH 
ulimit -HSn 102400 
#-------------------------- 
# export /etc/profile 
# tar zxvf apache-tomcat-7.0.59-modified.tar.gz    //tomcat installation
# mv apache-tomcat-7.0.59 /usr/local/
# ln -s /usr/local/apache-tomcat-7.0.59 /usr/local/tomcat 
# copy the umt share lib and memcache lib (environment directory) to the /usr/local/tomcat/directory
# vi /usr/local/tomcat/conf/catalina.properties    //add the umt and memcache lib to the common.loader section
# /usr/local/tomcat/bin/startup.sh          //startup
# /usr/local/tomcat/bin/shutdown.sh         //shutdown
```

#####Memcache
```
# yum install wget gcc gcc-c++ make libevent*   //packages installation
# vi /etc/security/limits.conf   //add the following lines to the file
 * soft nofile 102400
 * hard nofile 102400
# useradd memcached  //create users
# ./configure --prefix=/usr/local/memcached    //configure
# make && make install                  //make and make install
# /usr/local/memcached/bin/memcached -d -m 6144M -p 11212 -u memcached    //start
# vi /usr/local/tomcat/conf/context.xml              //configure memcache configuration for tomcat
	<Manager className="de.javakaffee.web.msm.MemcachedBackupSessionManager"
	sticky="true" memcachedNodes="n1:192.168.88.1:11211 n2:192.168.88.2:11211"
	failoverNodes="" requestUriIgnorePattern=".*\.(png|gif|jpg|css|js|ico)$"
	sessionBackupAsync="false" sessionBackupTimeout="100"
	transcoderFactoryClass="de.javakaffee.web.msm.serializer.kryo.KryoTranscoderFactory"
	customConverter="net.duckling.serializer.CustomKryoRegistration" />
```

#####MySQL
```
MySQL version 5.5 and above. Please refer to the MySQL installation. 
Please modify max_connections to above 1000 in the MySQL configuration
```

####Installation:
- Option 1:
- Option 2:

####Optional:

#####Nginx
```
# yum install wget gcc gcc-c++ pcre pcre-devel zlib* openssl openssl-devel make  //packages installation
# downloading the nginx software
# useradd nginx
# ./configure --prefix=/usr/local/nginx --with-http_ssl_module --with-http_gunzip_module 
--with-http_gzip_static_module --with-http_realip_module --user=nginx --group=nginx --with-ipv6       //configure 
# make && make install
# vi /usr/local/nginx/sbin/logcut.sh     //log cutting shell for nginx
#!/bin/bash
nginx_log_path="/usr/local/nginx/logs" 
nginx_oldlog_path="/usr/local/nginx/logs" 
log_filenames=`/bin/ls $nginx_log_path/*.log`
#echo $log_filenames 
#exit
for log_name in $log_filenames 
do 
/bin/mv $log_name $log_name-`date +%Y%m%d -d "1 days ago"` 
done
#/usr/local/nginx/sbin/nginx                         //nginx startup or by using "-s reload"
# /usr/local/nginx/sbin/nginx -s stop                 //nginx stop
# echo "00 00 * * * root /usr/local/nginx/sbin/logcut.sh" >>  /etc/crontab  //adding to the crontab
```
