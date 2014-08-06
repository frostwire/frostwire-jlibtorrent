CC = g++
CFLAGS = -Wc++11-extensions
INCLUDES = -I/usr/local/include -Iinclude
LDFLAGS = -L/usr/local/lib
LIBS = -ltorrent-rasterbar -lboost_system
CPP_FILES := $(wildcard src/*.cpp)
OBJ_FILES := $(addprefix obj/,$(notdir $(CPP_FILES:.cpp=.o)))
TARGET = jlibtorrent.dylib

all: $(SRCS) $(TARGET)

$(TARGET): $(OBJ_FILES)
	$(CC) $(LDFLAGS) $(LIBS) -dynamiclib -o $@ $^

obj/%.o: src/%.cpp
	$(CC) $(CFLAGS) $(INCLUDES) -c -o $@ $<

clean:
	rm -rf *.dylib obj/*.o *~ $(TARGET)