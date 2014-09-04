UNAME=$(shell uname)

INCLUDES = -Iinclude
ifeq ($(UNAME), Linux)
CC = g++
INCLUDES += -I/usr/lib/jvm/java-1.7.0-openjdk-amd64/include
TARGET = jlibtorrent.so
endif

ifeq ($(UNAME), Darwin)
CC = clang
INCLUDES += -I/usr/local/include
TARGET = jlibtorrent.dylib
endif

LDFLAGS = -L/usr/local/lib
LIBS = -ltorrent-rasterbar -lboost_system
CFLAGS = -Wno-c++11-extensions -std=c++11
CPP_FILES := $(wildcard src/*.cpp)
OBJ_FILES := $(addprefix obj/,$(notdir $(CPP_FILES:.cpp=.o)))

all: $(SRCS) $(TARGET)

$(TARGET): $(OBJ_FILES)
	$(CC) $(LDFLAGS) $(LIBS) -dynamiclib -o $@ $^

obj/%.o: src/%.cpp
	$(CC) $(CFLAGS) $(INCLUDES) -c -o $@ $<

clean:
	rm -rf *.dylib obj/*.o *~ $(TARGET)
