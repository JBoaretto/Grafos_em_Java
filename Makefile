all:
	javac *.java

run:
	java Main

clean:
	rm -f *.class

zip:
	zip Grafo.zip *.java Makefile