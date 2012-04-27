#erikreed@cmu.edu
# change lib64 to lib on ubuntu

DAI_FLAGS=-DDAI_DEBUG -DDAI_WITH_BP -DDAI_WITH_FBP -DDAI_WITH_TRWBP -DDAI_WITH_MF -DDAI_WITH_HAK -DDAI_WITH_LC -DDAI_WITH_TREEEP -DDAI_WITH_JTREE -DDAI_WITH_MR -DDAI_WITH_GIBBS -DDAI_WITH_CBP -DDAI_WITH_DECMAP


export PLATFORM=Linux-amd64-64
export HADOOP_INSTALL=/home/erik/hadoop/hadoop-1.0.0

INCLUDE=-L${HADOOP_HOME}/c++/Linux-amd64-64/lib \
	-I${HADOOP_HOME}/c++/Linux-amd64-64 
LIBS=-lhadooppipes -lhadooputils -lgmpxx -lm -lc -lgmp \
	-lssl -lcrypto -lboost_serialization \
	-lpthread -lz -ldl

LIBS2=-lgmpxx -lm -lc -lgmp \
    -lboost_serialization -lpthread

all: daicontrol dai_map dai_reduce utils
cleanout: 
	hadoop fs -rmr out 
	
pipes: daicontrol
	hadoop pipes \
	-D hadoop.pipes.java.recordwriter=true \
	-input in/tab_content \
	-output out \
	-program daicontrol

mvdat: daicontrol
	./make_input.sh && \
	hadoop fs -put in . && \
	hadoop fs -put daicontrol .

.PHONY: run clean hadoop mvjar mvdat cleanout

clean:
	rm -f *.log *.o *.so *.a *.jar daicontrol *.class DaiControl.h && \
	rm -rf DaiReduce2dai_temp tmp /tmp/hadoop-erik*

run: all
	cat in/tab_content | ./dai_map | ./dai_reduce
dai_map: dai_map.cpp ../lib/libdai.a dai_mapreduce.h
	g++ -static -Wall -g -Wno-write-strings -I../include -I. $(DAI_FLAGS)  \
    -odai_map dai_map.cpp ../lib/libdai.a $(LIBS2)

dai_reduce: dai_reduce.cpp ../lib/libdai.a dai_mapreduce.h
	g++ -static -Wall -g -Wno-write-strings -I../include -I. $(DAI_FLAGS)  \
    -odai_reduce dai_reduce.cpp ../lib/libdai.a $(LIBS2)

daicontrol: daicontrol.cpp ../lib/libdai.a dai_mapreduce.h
	g++ -static -Wall -g -Wno-write-strings -I../include -I. $(DAI_FLAGS) $(INCLUDE)  \
	-odaicontrol daicontrol.cpp ../lib/libdai.a $(LIBS)

utils : utils.cpp ../lib/libdai.a
	g++ -Wall -g -Wno-write-strings -I../include -I. $(DAI_FLAGS)  \
    -outils utils.cpp ../lib/libdai.a $(LIBS2)
