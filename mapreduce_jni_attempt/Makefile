#erikreed@cmu.edu
# change lib64 to lib on ubuntu
JAVA1=/usr/lib/jvm/java-7-openjdk-amd64/include/
JAVA2=$(JAVA1)linux/
GNU_LIBS=/usr/lib/gcc/x86_64-linux-gnu/4.6.1/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../../x86_64-linux-gnu/lib/x86_64-linux-gnu/4.6.1/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../../x86_64-linux-gnu/lib/x86_64-linux-gnu/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../../x86_64-linux-gnu/lib/../lib/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../x86_64-linux-gnu/4.6.1/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../x86_64-linux-gnu/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../../lib/:/lib/x86_64-linux-gnu/4.6.1/:/lib/x86_64-linux-gnu/:/lib/../lib/:/usr/lib/x86_64-linux-gnu/4.6.1/:/usr/lib/x86_64-linux-gnu/:/usr/lib/../lib/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../../x86_64-linux-gnu/lib/:/usr/lib/gcc/x86_64-linux-gnu/4.6.1/../../../:/lib/:/usr/lib/
#/lib/x86_64-linux-gnu:/lib64:/usr/lib/x86_64-linux-gnu:/usr/lib:/lib

export LD_LIBRARY_PATH=$(GNU_LIBS)

all: DaiControl.class libdaicontrol.so lib.jar daicontrol

hadoop/bin/DaiReduce2.class: hadoop/DaiReduce2.java
	javac -cp ~/hadoop/hadoop-1.0.0/hadoop-core-1.0.0.jar -d hadoop/bin/ hadoop/DaiReduce2.java

lib.jar: hadoop/bin/DaiReduce2.class 
	ant -f hadoop/make_jar.xml

hadoop: libdaicontrol.so lib.jar
	export LD_LIBRARY_PATH=$(GNU_LIBS) && \
	rm -rf DaiReduce2dai_temp && \
		hadoop jar lib.jar \
			1 1

#-Djava.library.path="$(GNU_LIBS)" /
#-Dmapred.child.env="LD_LIBRARY_PATH=$(GNU_LIBS)" \
#-Dmapred.map.child.java.opts="LD_LIBRARY_PATH=$(GNU_LIBS);java.library.path=$(GNU_LIBS)"
mvso: libdaicontrol.so
	hadoop fs -copyFromLocal libdaicontrol.so /libraries/libdaicontrol.so && \
	hadoop fs -copyFromLocal dat dat

.PHONY: run clean hadoop mvjar mvso

clean:
	rm -f *.log *.o *.so *.a *.jar daicontrol *.class DaiControl.h && \
	rm -rf DaiReduce2dai_temp tmp /tmp/hadoop-erik*

run: all
	java -Djava.library.path=. DaiControl

DaiControl.class: DaiControl.java
	javac DaiControl.java 
	
DaiControl.h: DaiControl.class
	javah -jni DaiControl

daicontrol: daicontrol.cpp DaiControl.h
	g++ -O3  -I../include -I. -fPIC -g -DDAI_WITH_BP -DDAI_WITH_FBP -DDAI_WITH_TRWBP -DDAI_WITH_MF -DDAI_WITH_HAK -DDAI_WITH_LC -DDAI_WITH_TREEEP -DDAI_WITH_JTREE -DDAI_WITH_MR -DDAI_WITH_GIBBS -DDAI_WITH_CBP -DDAI_WITH_DECMAP -odaicontrol daicontrol.cpp ../lib/libdai.a ../lib/libdai.a -lgmpxx -lgmp \
				-I$(JAVA1) -I$(JAVA2)

libdaicontrol.so: daicontrol.cpp DaiControl.h libdaicontrol.o
	g++ -shared -o libdaicontrol.so -Wl,--whole-archive,-Bstatic /home/erik/Desktop/libdai/include/gmp-5.0.4/bin/lib/libgmp.a /home/erik/Desktop/libdai/include/gmp-5.0.4/bin/lib/libgmpxx.a ../lib/libdai.a libdaicontrol.o -Wl,--no-whole-archive,-Bdynamic

libdaicontrol.o: daicontrol.cpp DaiControl.h
	g++ -c -Wall -fPIC  -I../include -I. -g -DDAI_WITH_BP -DDAI_WITH_FBP -DDAI_WITH_TRWBP -DDAI_WITH_MF -DDAI_WITH_HAK -DDAI_WITH_LC -DDAI_WITH_TREEEP -DDAI_WITH_JTREE -DDAI_WITH_MR -DDAI_WITH_GIBBS -DDAI_WITH_CBP -DDAI_WITH_DECMAP -olibdaicontrol.o daicontrol.cpp -I/usr/lib/jvm/java-7-openjdk-amd64/include/ -I/usr/lib/jvm/java-7-openjdk-amd64/include/linux/ -I/home/erik/Desktop/libdai/include/gmp-5.0.4/bin/include

#libdaicontrol.so: libdaicontrol.a
#	g++ -shared -Wall -fPIC  -I../include -I. -g -DDAI_WITH_BP -DDAI_WITH_FBP -DDAI_WITH_TRWBP -DDAI_WITH_MF -DDAI_WITH_HAK -DDAI_WITH_LC -DDAI_WITH_TREEEP -DDAI_WITH_JTREE -DDAI_WITH_MR -DDAI_WITH_GIBBS -DDAI_WITH_CBP -DDAI_WITH_DECMAP -olibdaicontrol.so libdaicontrol.a  \
#				-I$(JAVA1) -I$(JAVA2)

