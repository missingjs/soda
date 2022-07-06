SOURCES := $(wildcard *.cpp)
OBJS = $(SOURCES:.cpp=.o)
DEPS = $(SOURCES:.cpp=.d)
CC = g++
CPPFLAGS = -shared -fPIC ${SODA_CPP_FLAGS}

LIB_DIR = .
LIB_NAME = libleetcode.so
LIB_FILE = $(LIB_DIR)/$(LIB_NAME)
TARGET = $(LIB_FILE)

all: $(TARGET)

%.d: %.cpp
	@set -e; rm -f $@; \
	$(CC) $(CPPFLAGS) -MM $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$

sinclude $(DEPS)

$(TARGET): $(OBJS)
	$(CC) $(CPPFLAGS) -o $(LIB_FILE) $(OBJS)

clean:
	rm -f $(OBJS) $(DEPS) $(LIB_FILE)
