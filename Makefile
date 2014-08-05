CC = g++
CFLAGS =
INCLUDES = -I/usr/local/include
LDFLAGS = -L/usr/local/lib
LIBS = -ltorrent-rasterbar -lboost_system
SRCS = jlibtorrent.cpp
OBJS = $(SRCS:.cpp=.o)
TARGET = jlibtorrent.dylib

all: $(SRCS) $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(LDFLAGS) $(LIBS) $(OBJS) -dynamiclib -o $@

.cpp.o:
	$(CC) $(CFLAGS) $(INCLUDES) -c $< -o $@

clean:
	rm -rf *.o *~ $(TARGET)