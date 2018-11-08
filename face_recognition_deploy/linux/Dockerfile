FROM centos
MAINTAINER zhangxuhai "zhangxuhai@kyee.com.cn"

# yum
RUN rm -rf /etc/yum.repos.d/CentOS-Base.repo
COPY CentOS-Base.repo /etc/yum.repos.d

# jdk
RUN mkdir /opt/java
ADD jdk-8u161-linux-x64.tar.gz /opt/java
ENV JAVA_HOME /opt/java/jdk1.8.0_161
ENV PATH $JAVA_HOME/bin:$PATH

# maven
RUN mkdir /opt/maven && mkdir /opt/maven/maven_repository
ADD apache-maven-3.5.3-bin.tar.gz /opt/maven
RUN rm -rf /opt/maven/apache-maven-3.5.3/conf/settings.xml
COPY settings.xml /opt/maven/apache-maven-3.5.3/conf
ENV MAVEN_HOME /opt/maven/apache-maven-3.5.3
ENV PATH $MAVEN_HOME/bin:$PATH

# build tools
RUN yum groupinstall "Development Tools" -y
RUN yum install cmake gcc gtk2-devel numpy pkconfig ant -y

# opencv pre
RUN mkdir /opt/opencv && mkdir /opt/opencv/opencv_src && mkdir /opt/opencv/opencv_lib
ADD opencv-master.zip /opt/opencv/opencv_src
RUN unzip /opt/opencv/opencv_src/opencv-master.zip -d /opt/opencv/opencv_src && rm -rf /opt/opencv/opencv_src/opencv-master.zip
ADD opencv_contrib-master.zip /opt/opencv/opencv_src
RUN unzip /opt/opencv/opencv_src/opencv_contrib-master.zip -d /opt/opencv/opencv_src && rm -rf /opt/opencv/opencv_src/opencv_contrib-master.zip

# opencv
RUN mkdir /opt/opencv/build
WORKDIR /opt/opencv/build
RUN cmake -DOPENCV_EXTRA_MODULES_PATH=/opt/opencv/opencv_src/opencv_contrib-master/modules -DCMAKE_BUILD_TYPE=RELEASE -DCMAKE_INSTALL_PREFIX=/opt/opencv/opencv_lib -DBUILD_TESTS=OFF /opt/opencv/opencv_src/opencv-master && make -j8 && make install && mvn install:install-file -Dfile=/opt/opencv/opencv_lib/share/OpenCV/java/opencv-341.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=3.4.1 -Dpackaging=jar && rm -rf /opt/opencv/build

# face recognition 
RUN mkdir /opt/face_recognition && mkdir /opt/face_recognition/sdk && mkdir /opt/face_recognition/working_dir && mkdir /opt/face_recognition/working_dir/temp && mkdir /opt/face_recognition/working_dir/face_db && mkdir /opt/face_recognition/working_dir/logs
ADD sdk.zip /opt/face_recognition/sdk
RUN unzip /opt/face_recognition/sdk/sdk.zip -d /opt/face_recognition/sdk && rm -rf /opt/face_recognition/sdk/sdk.zip
WORKDIR /opt/face_recognition/sdk
RUN mvn install
ADD face_recognition.zip /opt/face_recognition
RUN unzip /opt/face_recognition/face_recognition.zip -d /opt/face_recognition && rm -rf /opt/face_recognition/face_recognition.zip
WORKDIR /opt/face_recognition
RUN mvn package
RUN echo "nohup java -Djava.library.path=/opt/opencv/opencv_lib/share/OpenCV/java/ -jar target/face_recognition-0.0.2.jar > working_dir/logs/face_recognition_server.log &" > startup.sh








